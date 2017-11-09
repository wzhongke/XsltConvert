package utils.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;

// Http 的工具类
public class HttpUtils {

    private static Log log = LogFactory.getLog(HttpUtils.class);

    private static CloseableHttpClient httpClient;

    private final static String PFX_PATH = "ssyqClient.p12";   //客户端证书路径
    private final static String PFX_PWD = "ssyqClient20170605"; //客户端证书密码
    private final static String CERT = "ssyqClient.cer";


    /**
     * 返回内容为可读文件格式时，可以使用该方式，将内容读取为string
     * @param url
     * @return
     * @throws IOException
     */
    public static String getUrl(String url) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");
        CloseableHttpResponse response = httpClient.execute(request);
        org.apache.http.HttpEntity entity = response.getEntity();
        // 请求内容为文件时，需要用该方法读取，否则中文可能乱码
        InputStream in = entity.getContent();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int readLength = 0;
        while ((readLength=in.read(buffer)) > 0) {
            out.write(buffer, 0, readLength);
        }
        return new String(out.toByteArray(), "utf8");
    }

    /** HttpURLConnection 方式 */
    public static String executeRequestWithUrl (String url, String query) throws IOException {
        String requestUrl = url + URLEncoder.encode(query, "gbk");
        log.info(requestUrl);
        URL urlRequest = new URL(requestUrl);
        HttpURLConnection con = (HttpURLConnection) urlRequest.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");
        con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        con.setConnectTimeout(3000);
        con.setRequestProperty("Proxy-Connection", "keep-alive");
        int responseCode = con.getResponseCode();
        System.out.println("Response Code : " + responseCode);
        //将输入流转换成字符串
        InputStream is=con.getInputStream();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        byte [] buffer=new byte[1024];
        int len=0;
        while((len=is.read(buffer))!=-1){
            baos.write(buffer, 0, len);
        }
        String jsonString=baos.toString();
        log.info(jsonString);
        return jsonString;
    }

    /**
     * POST 方式提交 键值对 或 请求体 数据
     * @param postUrl url
     * @param params 提交参数
     * @param isHttps 是否为https
     * @throws Exception
     */
    public static String post(String postUrl, String params, boolean isHttps) throws Exception {
        CloseableHttpClient httpClient ;
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000)
                .build();
        if (isHttps) {
            httpClient = createSSLClientDefault();
        } else {
            httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
        }

        HttpPost httpPost = new HttpPost(postUrl);
        httpPost.setHeader("Content-Type", "application/json;charset=utf-8");

        // 键值对方式提交
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("name", "value"));
        httpPost.setEntity(new UrlEncodedFormEntity(pairs));

        // 请求体方式提交
        httpPost.setEntity(new StringEntity(params, "utf-8"));
        try ( CloseableHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                return EntityUtils.toString(httpEntity, "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 双向认证使用的http方式
     * @return 返回一个httpClient
     * @throws Exception
     */
    private static CloseableHttpClient createSSLClientDefault() throws Exception {
        if (httpClient == null) {
            SSLContext ctx = getSSLContext(PFX_PWD, PFX_PATH, CERT);
            LayeredConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(ctx);
            httpClient = HttpClients.custom()
                    .setSSLSocketFactory(sslSocketFactory)
                    .build();
        }
        return httpClient;
    }

    private static SSLContext getSSLContext(String password, String keyStorePath, String trustStorePath) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, IOException, CertificateException, KeyManagementException {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        InputStream in = HttpUtils.class.getClassLoader().getResourceAsStream(keyStorePath);
        keyStore.load(in, PFX_PWD.toCharArray());
        in.close();
        keyManagerFactory.init(keyStore, password.toCharArray());
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(keyManagerFactory.getKeyManagers(), getTrustManagers(trustStorePath) , null);
        return ctx;
    }

    private static TrustManager[] getTrustManagers (String ... crtPath) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException {
        if (crtPath == null || crtPath.length < 1) {
            return null;
        }
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null);
        for (int i=0, j=crtPath.length; i<j; i++) {
            String path = crtPath[i];
            InputStream is = HttpUtils.class.getClassLoader().getResourceAsStream(path);
            System.out.println(path);
            keyStore.setCertificateEntry(Integer.toString(i), certificateFactory.generateCertificate(is));
            is.close();
        }
        TrustManagerFactory trustManagerFactory =TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        return trustManagerFactory.getTrustManagers();
    }

    public static void main(String [] args) throws IOException {
        System.out.println(getUrl("http://zhihu.sogou.com.inner"));
    }
}
