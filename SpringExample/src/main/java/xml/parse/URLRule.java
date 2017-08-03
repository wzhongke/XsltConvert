package xml.parse;

import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.List;

/**
 * url level set 类
 */

public class URLRule {
    private String id;
    private String name;
    private List<String> urls = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public void addUrl(String url) {
        this.urls.add(url);
    }

    public void initAttr(Attributes attr) {
        this.id = attr.getValue("id");
        this.name = attr.getValue("name");
    }

    @Override
    public String toString() {
        return "URLRule{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", urls=" + urls +
                '}';
    }
}
