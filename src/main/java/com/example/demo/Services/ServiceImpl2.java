package com.example.demo.Services;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class ServiceImpl2 implements ApiService{
    public ServiceImpl2(){
        System.out.println("impl2 created");
    }
}
