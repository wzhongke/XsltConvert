package com.wang.intercepting;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;

/**
 * 该控制器会拦截所有的@Controller请求，如果想要缩小拦截器拦截的请求，可以使用MVC namespace或者MVC Java config，或者声明一个MappedInterceptor的bean
 */
public class TimeBasedAccessInterceptor extends HandlerInterceptorAdapter{
    private int openingTime;
    private int closingTime;

    public void setOpeningTime(int openingTime) {
        this.openingTime = openingTime;
    }

    public void setClosingTime(int closingTime) {
        this.closingTime = closingTime;
    }

    // 注意引的类包的正确性
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        System.out.println("hour: " + hour);
        if (openingTime <= hour && hour < closingTime) {
            return true;
        }
        response.sendRedirect("http://www.sogou.com");
        return false;
    }
}
