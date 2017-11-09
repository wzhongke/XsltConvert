package spring.configuration;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.nio.charset.Charset;

@Configuration
@ComponentScan
@PropertySource("classpath:spring/configuration/test.properties")
public class PropertyReader {

	@Value("I love you")
	private String normal;

	@Value("#{systemProperties['os.name']}")
	private String osName;

	@Value("#{T(java.lang.Math).random() * 100.0 }")
	private double randomNumber;

	@Value("#{demoService.another}")
	private String fromAnother;

	@Value("class:spring/configuration/test.txt")
	private Resource testFile;

	@Value("http://www.baidu.com")
	private Resource testUrl;

	@Value("${book.name}")
	private String bookName;

	@Autowired
	private Environment environment;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigure () {
		return new PropertySourcesPlaceholderConfigurer();
	}

	public void outputResource () {
		try {
			System.out.println(normal);
			System.out.println(osName);
			System.out.println(randomNumber);
			System.out.println(fromAnother);

			System.out.println(IOUtils.toString(testFile.getInputStream(), Charset.defaultCharset()));
			System.out.println(IOUtils.toString(testUrl.getInputStream(), Charset.defaultCharset()));
			System.out.println(bookName);
			System.out.println(environment.getProperty("book.author"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
