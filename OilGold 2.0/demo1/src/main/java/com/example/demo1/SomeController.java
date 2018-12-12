package com.example.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SomeController {

    @Autowired
    AnalyticService analyticService;
    @GetMapping(value="/")
    public String start_page(){
        return "index";
    }

    @GetMapping(value="/index")
    public String index_page(){
        return "index";
    }

    @GetMapping(value="/prediction")
    public String prediction_page(){
        return "prediction";
    }

    @GetMapping(value="/instruments")
    public String instruments_page(){
        return "instruments";
    }
}
