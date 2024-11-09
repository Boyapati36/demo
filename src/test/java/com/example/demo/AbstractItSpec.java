package com.example.demo;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import spock.lang.Specification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = DemoApplication.class)
public abstract class AbstractItSpec extends Specification {
    protected MockMvc mvc;
}