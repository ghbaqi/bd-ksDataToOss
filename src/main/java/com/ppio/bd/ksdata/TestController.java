package com.ppio.bd.ksdata;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    int i;

    @GetMapping("/hi")
    public String hi() {
        return "hi : " + i++;
    }
}
