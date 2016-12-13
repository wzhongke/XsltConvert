package com.wang.test;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Created by admin on 2016/10/28.
 */
public class BuildURI {

    public static void main(String [] args) {
        UriComponents uriComponents = UriComponentsBuilder.
                fromUriString("http://example.com/hotels/{hotel}/bookings/{booking}")
                .build();
        URI uri = uriComponents.expand("42", "21").encode().toUri();
    }
}
