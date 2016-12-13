package com.wang.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @ControllerAdvice可以包含@ExceptionHandler, @InitBinder 和 @ModelAttribute 方法，这些方法将会用在所标记的controller上
 */


@ControllerAdvice("com.wang.controller")
public class BasePackageAdvice{
    /**
     * 可以包含 @ExceptionHandler, @InitBinder, @ModelAttribute
     */
}

// 对所有使用RestController标记的类生效
@ControllerAdvice(annotations = RestController.class)
class AnnotationAdvice {

}

@ControllerAdvice(assignableTypes = {AbstractController.class})
class AssignablTypesAdvice{}
