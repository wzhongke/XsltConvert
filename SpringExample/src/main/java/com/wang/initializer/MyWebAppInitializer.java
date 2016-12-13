package com.wang.initializer;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * 在Servlet3.0+ 中使用该方式可以作为web.xml的替代或者结合来配置项目
 * 基于java配置时， 继承 AbstractAnnotationConfigDispatcherServletInitializer
 */
public class MyWebAppInitializer extends AbstractDispatcherServletInitializer{


    @Override
    public void onStartup(ServletContext context) {
        System.out.println("--------on start up-----------");
        XmlWebApplicationContext appContext = new XmlWebApplicationContext();
        appContext.setConfigLocation("/resources/dispatcher-servlet.xml");
        ServletRegistration.Dynamic registration = context.addServlet("dispatcher", new DispatcherServlet(appContext));
        registration.setAsyncSupported(true);
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
    }

    // 添加filter
    @Override
    protected Filter[] getServletFilters() {
        return new Filter[] { new ShallowEtagHeaderFilter() };
    }

    @Override
    protected WebApplicationContext createServletApplicationContext() {
        return null;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        return null;
    }
}
