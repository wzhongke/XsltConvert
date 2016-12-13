package com.wang.controller;

import com.wang.data.Person;
import com.wang.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 2016/10/28.
 */
@Controller
@RequestMapping("/cache")
public class HTTPCacheController {
    @Autowired
    private PersonService service;
    /* ETag 是HTTP响应头中标记请求内容是否改变的标记位
    * setCachePeriod(int seconds): -1 不会生成"Cache-Control'的响应头
    *                               0 不能使用"Cache-Control: no-store:
    *                               n>0 返回"Cache-Control:max-age=n"的响应头
    * // Cache for an hour - "Cache-Control: max-age=3600"
        CacheControl ccCacheOneHour = CacheControl.maxAge(1, TimeUnit.HOURS);
        // Prevent caching - "Cache-Control: no-store"
        CacheControl ccNoStore = CacheControl.noStore();
        // Cache for ten days in public and private caches,
        // public caches should not transform the response
        // "Cache-Control: max-age=864000, public, no-transform"
        CacheControl ccCustom = CacheControl.maxAge(10, TimeUnit.DAYS)
        .noTransform().cachePublic();
    *                               */

    /**
     * ETags 通过Servlet filter 设置ShallowEtagHeaderFilter来启用
     * */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Person> showPerson(@RequestParam String name) {
        Person p = service.getPerson(name);
        String hash = p.hashCode() + "";
        return ResponseEntity.ok().cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
                .eTag(hash)
                .body(p);
    }

    @RequestMapping(value = "/lastModify", method = RequestMethod.GET)
    public String myHandleMethod(WebRequest webRequest, Model model) {
        long lastModified = service.hashCode(); // 应用中计算结果是否变化的值
        // 检查 'If-Modified-Since' or 'If-Unmodified-Since'   {request.checkNotModified(eTag) 检查'If-None-Match'}
        if(webRequest.checkNotModified(lastModified)) {
            return null; // 如果没有变化，退出
        }
        // 如果变化了，准备数据
        model.addAttribute("message", "something happened");
        return "helloWorld";

    }
}
