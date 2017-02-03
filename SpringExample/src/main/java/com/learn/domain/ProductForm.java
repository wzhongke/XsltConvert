package com.learn.domain;

/**
 * Created by wangzhognke on 2017/1/6.
 *
 * 表单对象会传递ServletRequest给其他组件，类似Validator，而ServletRequest是一个Servlet层对象，不应该暴露给其他层
 * 表单类不需要实现Serializable接口，因为表单对象很少保存在HttpSession中
 */
public class ProductForm {
    private String name;
    private String descriptioin;
    private String price;

    public String getDescriptioin() {
        return descriptioin;
    }

    public void setDescriptioin(String descriptioin) {
        this.descriptioin = descriptioin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
