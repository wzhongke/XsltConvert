package com.wang.test.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.wang.test.data.Account;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2016/8/29.
 */
@RestController
@RequestMapping("/editPet.do")
/**
 * The type-level @SessionAttributes annotation declares session attributes used by a specific method, serving as form-backing beans between subsequent requests.
 *
 * For use cases that require adding or removing session attributes consider injecting org.springframework.web.context.request.WebRequest or
    javax.servlet.http.HttpSession into the controller method.
 */
@SessionAttributes("account")
public class SessionAttributeController {

    @RequestMapping("/")
    public String handle(@SessionAttribute Account account){
        // ...
        return "hello";
    }

    // retrieve the cookie
    @RequestMapping("/displayHeaderInfo.do")
    public void displayHeaderInfo(@CookieValue("JSESSIONID") String cookie){

    }

    /**  Mapping request header attributes with the @RequestHeader annotation
     *   When a @RequestHeader annotataion is used on a Map<String, String>, MultiValueMap<String, String>, or HttpHeaders argument, the map is populated with all
         header values.
     */
    public void displayHeader(@RequestHeader("Accept-Encoding") String encoding, @RequestHeader("Keep-Alive") long keepAlive){

    }

    /**
     *  To use it with an @ResponseBody controller method or controller methods that return ResponseEntity, simply add the @JsonView annotation with a class argument specifying the view
        class or interface to be used
     */

    @GetMapping("/user")
    @JsonView(User.WithoutPasswordView.class)
    public String getUser(Model model){
        model.addAttribute("user", new User("wang","love wen pan"));
        model.addAttribute(JsonView.class.getName(), User.WithoutPasswordView.class);
        return "userView";
    }
    /**
     * Some simple types such as int, long, Date, etc. can automatically converts to the appropriate type.
     * You can further customize the conversion process through a WebDataBinder (see the section called “Customizing WebDataBinder initialization”) or by
       registering Formatters with the FormattingConversionService (see Section 9.6, “Spring Field Formatting”).
     */


    /**
     * The following example demonstrates the use of @InitBinder to configure a CustomDateEditor for all java.util.Date form properties.
     */
    @InitBinder
    protected  void initBinder(WebDataBinder binder){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));

        //or with the same approach to be reused for controller-specific tweaking of the binding rules
        binder.addCustomFormatter(new DateFormatter("yyyy-MM-dd"));
    }

}

class User{
    public interface WithoutPasswordView{};
    public interface WithPasswordView extends WithoutPasswordView{}

    private String username;
    private String password;
    public User(){

    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @JsonView(WithoutPasswordView.class)
    public String getUsername() {
        return username;
    }

    @JsonView(WithPasswordView.class)
    public String getPassword() {
        return password;
    }

}
