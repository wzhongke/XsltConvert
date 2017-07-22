package spring.configuration;

import spring.intercepting.TimeBasedAccessInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.resource.VersionResourceResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.servlet.view.xslt.XsltViewResolver;
import org.springframework.web.util.UrlPathHelper;

import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 2016/10/21.
 */
@Configuration
@EnableWebMvc    // this register a RequestMappingHandlerMapping, a RequestMappingHandlerAdapter, ExceptionHandlerExceptionResolver
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 多个路径可以用逗号分开
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/static/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS).cachePublic())
                .resourceChain(true).addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // add formatter and/or converters
        // @NumberFormat 和 @DateTimeFormat 默认已经添加到环境中
    }

    // 添加拦截控制器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TimeBasedAccessInterceptor()).addPathPatterns("/**").excludePathPatterns("/admin/**");
    }

    // 配置匹配路径
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(true)
                .setUseTrailingSlashMatch(false)
                .setUseRegisteredSuffixPatternMatch(true)
                .setUrlPathHelper(urlPathHelper());
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("example");
    }
    /* configures content negotiation view resolution using FreeMarker HTML templates and Jackson as a default View for JSON rendering */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.enableContentNegotiation(new MappingJackson2JsonView());
        registry.jsp();
    }

    @Bean
    public UrlPathHelper urlPathHelper(){
        return  new UrlPathHelper();
    }

    @Bean
    public XsltViewResolver xsltViewResolver() {
        XsltViewResolver viewResolver = new XsltViewResolver();
        viewResolver.setPrefix("/WEB-INF/xsl");
        viewResolver.setSuffix(".xslt");
        return viewResolver;
    }
}
