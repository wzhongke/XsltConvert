package com.wang.data;

/**
 * Created by admin on 2016/11/4.
 */
public class ImageElement {
    private String caption;
    private String description;
    private String path;

    public ImageElement(){}
    public ImageElement(String caption){this.caption = caption;}
    public ImageElement(String caption, String description) {this.caption = caption; this.description = description;}
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
