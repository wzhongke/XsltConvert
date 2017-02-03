package com.learn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by admin on 2017/1/6.
 */
public interface Controller {
    String handleRequest (HttpServletRequest request, HttpServletResponse response);
}
