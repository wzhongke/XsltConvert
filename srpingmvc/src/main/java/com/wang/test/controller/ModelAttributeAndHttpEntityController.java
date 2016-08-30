package com.wang.test.controller;

import com.wang.test.data.Account;
import com.wang.test.validator.AccountValidator.AccountValidator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

/**
 * Created by admin on 2016/8/29.
 */
@Controller
public class ModelAttributeAndHttpEntityController {
    /**
     *  indicates the purpose of that method is to add one or more model attributes.
     *  All ModelAttribute methods are invoked before @RequestMapping methods of the same controller
     */
    @ModelAttribute
    public void populateModel(@RequestParam String number, Model model){
        model.addAttribute("wang",number);
        // add more ...
    }

    /**
     *  An @ModelAttribute on a method argument indicates the argument should be retrieved from the model.
     *  Once present in the model, the argument’s fields should be populated from all request parameters that
     have matching names.
     *  It may already be in the model due to use of @SessionAttributes
     *  It may already be in the model due to an @ModelAttribute method in the same controller
     *  It may be instantiated using its default constructor.
     */
    @PostMapping("/owners/{ownerId}/pets/{petId}/edit")
    public String processSubmit(@ModelAttribute String pet) {
        return  "hello";
    }
    /**
     * Data Binding
     */
    @PostMapping("/owners/{ownerId}/pets/{petId}/edit")
    public String processSubmit(@ModelAttribute("pet") Account account, BindingResult result) {
        // invoke validation using your own custom validator
        new AccountValidator().validate(account, result);
        if (result.hasErrors()) {  // whit a bindingResult you can check if errors were found
            return "petForm";
        }
        // do something
        return "hello";
    }

    /**
     *  An @ModelAttribute method is a common way to retrieve an attribute from the database, which may
     optionally be stored between requests through the use of @SessionAttributes.
     *  In some cases it may be convenient to retrieve the attribute by using an URI template variable and a type converter.
     */
    @PutMapping("/accounts/{account}")
    public String save(@ModelAttribute("account") Account account) {
        return "hello";
    }

    /* The HttpEntity is similar to @RequestBody and @ResponseBody. Besides getting access to the
    request and response body, HttpEntity (and the response-specific subclass ResponseEntity) also
    allows access to the request and response headers
    */
    @RequestMapping("/something")
    public ResponseEntity<String> handler(HttpEntity<byte []> requestEntity) throws UnsupportedEncodingException{
        String requestHeader = requestEntity.getHeaders().getFirst("MyRequertHeader");
        byte [] requestBody = requestEntity.getBody();
        // do something with request header and body

        HttpHeaders responseHanders = new HttpHeaders();
        responseHanders.set("MyResponseHeader", "MyValue");
        return new ResponseEntity<String>("Hello World", responseHanders, HttpStatus.CREATED);
    }


}
