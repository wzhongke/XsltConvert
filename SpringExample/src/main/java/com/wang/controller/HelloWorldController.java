package com.wang.controller;

import com.wang.data.Person;
import com.wang.service.PersonService;
import com.wang.validator.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by admin on 2016/10/21.
 */
@Controller
@RequestMapping("/example")
//@SessionAttributes("name")
public class HelloWorldController {

    @Autowired
    private PersonService personService;

    /* 当@RequestParam使用在Map<String, String> 或者MultiValueMap<String, String> 上时， map中包含所有的请求参数  */
    @RequestMapping("/helloWorld")
    public String helloWorld(@RequestParam(value = "message"   , required = false) String message, Model model) {
        message = (message == null? "Hello World!" : message);
        model.addAttribute("message", message);
        return "helloWorld";
    }

    /* 使用@PathVariable绑定annotation */
    @RequestMapping(value="/owners/{ownerId}")
    public String findOwner(@PathVariable String ownerId, Model model) {
        model.addAttribute("message", ownerId);
        return "helloWorld";
    }

    @GetMapping("/owners/{ownerId}/pets/{petId}")
    public String findPet(@PathVariable String ownerId, @PathVariable String petId, Model model) {
        model.addAttribute("message", ownerId + "  " + petId);
        return "helloWorld";
    }

    /* 使用正则表达式匹配URL {varName:regex}
    * /example/version/spring-web-3.0.5.jar
    * */
    @RequestMapping("/version/{symbolicName:[a-z-]+}-{version:\\d\\.\\d\\.\\d}{extension:\\.[a-z]+}")
    public String handle(@PathVariable String version, @PathVariable String extension, Model model) {
        model.addAttribute("message", "version:" + version + " extension: " + extension);
        return "helloWorld";
    }
    /* example: http://localhost:8080/example/2013-10-18 */
    @RequestMapping(value = "/{day}", method= RequestMethod.GET)
    public String getForDay(@PathVariable @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) Date day, Model model) {
        model.addAttribute("message", day.toString());
        return "helloWorld";
    }

    /* GET /owners/42;q=11/pets/21;q=22
    * 需要配置： <mvc:annotation-driven enable-matrix-variables="true"/> */
    @GetMapping("/person/{ownerId}/pets/{petId}")
    public String findPetMatrix(
            @MatrixVariable(name="q", pathVar="ownerId", required = false, defaultValue = "1") int q1,
            @MatrixVariable(name="q", pathVar="petId") int q2, Model model) {
                model.addAttribute("message", "q: " + q1 + "q2: " + q2);
        return "helloWorld";
    }

    @PostMapping(path = "/pets", consumes = "application/json")
    public String addPet(@RequestBody String json, Model model) {
        model.addAttribute("message", json);
        return "helloWorld";
    }

    /* The request will be matched only if the Accept request header matches one of these values */
    @GetMapping(path = "/person/{name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Person getPerson(@PathVariable String name, @ModelAttribute("person") Person p) {
        return p;
    }

    @GetMapping(value="person/{ownerId}", params = "myParam=myValue")
    public void getPersonByParam(@PathVariable String ownerId, Model model) {
    }

    @GetMapping(path = "/personheader", headers = "myHeader=myValue")
    public void getPersonByHeader( @PathVariable String petId, Model model) {
    }

    /* 添加一个attribute， 等同于 model.addAttribute("person", personService.getPerson(name));
    * 同下边getMorePerson等同
    * 自动添加到匹配的请求中
    * */
//    @ModelAttribute // @ModelAttribute("customize")
//    public Person getPersonWithModel(@PathVariable String name) {
//        System.out.println("PathVariable model attribute");
//        return personService.getPerson(name);
//    }

    // 请求该控制器中所有的url，都需要带name属性
    @ModelAttribute
    public void getMorePerson(@RequestParam(value = "name", required = false) String name, Model model) {
        System.out.println("RequestParam model attribute");
        model.addAttribute("person", personService.getPerson(name));
    }
    /* @RequestMapping支持的参数 */
    @RequestMapping("/supportParam")
    public void supportParam(
        WebRequest webRequest,  //Allows for generic request parameter access as well as request/session attribute access,
        NativeWebRequest nativeWebRequest,
        Locale locale, // 是指定的locale resolver
        TimeZone timeZone, //for the time zone associated  with the current request, as determined by a LocaleContextResolver.
        InputStream inputStream, Reader reader, // 访问reques的内容， 其中的内容是原始的内容
        OutputStream outputStream, Writer writer, // 生成的response的内容
        HttpMethod httpMethod, // http 方法
        Principal principal, // 包含着目前授权的用户
        Model model, // view处理相关
        RedirectAttributes redirect, // 重定向相关
        SessionStatus sessionStatus, // 标记完成表单处理，触发清除由@SessionAttributes指定的session attributes
        UriComponentsBuilder uriComponentsBuilder, // 准备与当前请求host，port，scheme，路径相关的URL
        HttpSession session,

        @ModelAttribute("person") Person person, // 在方法参数上使用，表明该参数是从model中获取的；如果model中不存在，则初始化变量并添加到model中
        BindingResult result, // 必须紧跟被绑定的对象

        @PathVariable String pathVariable,
        @MatrixVariable int matrixVariable,
        @RequestParam String requestParam, // annotated parameters for access to specific Servlet request parameters. Parameter
                                            //values are converted to the declared method argument type.
        @RequestHeader String requestHeader, // 请求的头部
        @RequestHeader("Accept-Encoding") String encoding, // 请求头部的部分内容
        @RequestBody String requestBody, // 请求的body
        @RequestPart String requestPart, // annotated parameters for access to the content of a "multipart/form-data" request part
        @SessionAttribute(name="name") String name,
        @RequestAttribute(name="personName") String personName,

        @CookieValue("JSESSIONID") String cookie // 获取 名称为JSESSIONID的cookie值

    ) {
        System.out.println("requestHeader: " + requestHeader);
    }

    /* @RequestMapping支持的返回类型：
     * Model, ModelAndView, Map, View, String, void,
      * HttpEntity<?>, ResponseEntity<?>, Callable<?> DeferredResult<?>, ListenableFuture<?>,
       * ResponseBodyEmitter, SseEmitter, StreamingResponseBody*/



    @InitBinder
    protected  void initBinder(WebDataBinder binder) {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
        dataFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dataFormat, false));
//        binder.addValidators(new PersonValidator()); //添加校验器
    }
}
