package com.wang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/8/31.
 */
@Controller
public class ChatController {

    @GetMapping("/index")
    public String handleIndex(@ModelAttribute String name, Model model) throws IOException {

        return "chatroom";
    }


}