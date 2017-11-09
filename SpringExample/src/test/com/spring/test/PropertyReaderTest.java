package com.spring.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import spring.configuration.PropertyReader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml", "classpath:dispatcher-servlet.xml"})
public class PropertyReaderTest {

	@Autowired
	private PropertyReader propertyReader;

	@Test
	public void test () {
		propertyReader.outputResource();
	}

}
