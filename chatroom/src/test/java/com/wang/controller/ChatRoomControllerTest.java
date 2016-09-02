package com.wang.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by admin on 2016/9/1.
 */
@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration("dispatcher-servlet.xml")
public class ChatRoomControllerTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void getindex() throws Exception {
        this.mockMvc.perform(get("/index").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk()).andDo(print()).andReturn();

//        mockMvc.perform(post("/hotels/{id}", 42).accept(MediaType.APPLICATION_JSON));

        /**You can also perform file upload requests that internally use MockMultipartHttpServletRequest
            so that there is no actual parsing of a multipart request but rather you have to set it up:
        */
//        mockMvc.perform(fileUpload("/doc").file("a1", "ABC".getBytes("UTF-8")));

        /**
         * you can add Servlet request parameters representing either query of form parameters
         */
//        mockMvc.perform(get("/hotels").param("foo", "bar"));

    }
}
