package spring.validator;

import org.springframework.validation.Errors;

/**
 * Created by admin on 2016/10/28.
 */
public class PersonValidator {//implements Validator {
    public boolean supports(Class<?> aClass) {
        return aClass.getName().equals("Person");
    }

    public void validate(Object o, Errors errors) {
    }
}
