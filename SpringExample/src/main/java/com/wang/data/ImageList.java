package com.wang.data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by admin on 2016/11/5.
 */
public class ImageList {
    private String listName;
    private String description;
    private List<ImageElement> elements = new LinkedList<>();

    public void addImageElemnt(ImageElement element) {
        this.elements.add(element);
    }

    public void removeElement(ImageElement element){
        this.elements.remove(element);
    }

    public List<ImageElement> getElements() {
        return this.elements;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
