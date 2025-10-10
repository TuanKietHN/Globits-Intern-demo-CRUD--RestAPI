package vn.globits.demo.rest;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hello-world")
public class HelloWorldController {

    @GetMapping
    public String helloWorld() {
        return "Hello World!";
    }
}
