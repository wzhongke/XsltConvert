package com.wang.configuration;

import com.wang.data.ImageElement;
import com.wang.data.ImageList;
import com.wang.data.Person;
import com.wang.service.ChatRoomService;
import com.wang.service.PersonService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by admin on 2016/10/21.
 */
@Configuration
public class BeanConfig {

    @Bean
    public PersonService getPersonService(){
        Person person = new Person();
        person.setName("wang");
        person.setDescription("秦皇岛");
        person.setPassword("123456");
        ImageElement imageElement = new ImageElement("秦皇岛", "旅游");
        imageElement.setPath("/static/images/snow.jpg");
        ImageList imageList = new ImageList();
        imageList.addImageElemnt(imageElement);
        person.addImageList(imageList);
        PersonService service = new PersonService();
        service.addPerson(person);
        return service;
    }

    @Bean
    public ChatRoomService getChatRoomService () {
        return new ChatRoomService();
    }
}
