package com.wang.data;

import com.fasterxml.jackson.annotation.JsonView;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by admin on 2016/10/21.
 */
public class Person {
    public interface WithoutPasswordView {}
    public interface WithPasswordView extends WithoutPasswordView {}

    @NotNull
    @Max(30)
    private String name;
    private String password;
    private String description;
    private List<ImageList> imageList = new LinkedList<>();

    @JsonView(WithoutPasswordView.class)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonView(WithoutPasswordView.class)
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    @JsonView(WithPasswordView.class)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ImageList> getImageList() {return imageList;}
    public ImageList getImageList(int order) {return imageList.get(order);}
    public void addImageList(ImageList element) {imageList.add(element);}
}
