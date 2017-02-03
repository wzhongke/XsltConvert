package com.wang.controller;

import com.wang.data.Person;
import com.wang.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping("/operate")
public class OperateController {

    @Autowired
    PersonService personService;

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String getView(@SessionAttribute("username") String username, @RequestParam int order, Model model) {
        Person person = personService.getPerson(username);
        model.addAttribute("imageList", person.getImageList(order));
        return "view";
    }
}
