package com.wang.controller;

import com.wang.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by admin on 2016/11/4.
 */
@Controller
@RequestMapping("/manage")
@SessionAttributes("username")
public class ManageController {
    @Autowired
    private PersonService personService;

    @RequestMapping(method = RequestMethod.POST)
    public String handleManageRequest(@RequestParam("name") String name, Model model, HttpSession session) {
        session.setAttribute("username", name);
        model.addAttribute("person",personService.getPerson(name));
        return "manage";
    }
}
