package com.example.demo1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by SK
 **/
@Controller
public class SomeController {
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

    //для кнопки обновить данные
    @RequestMapping("/update")
    public String update_page(/*@RequestBody SessionRequest params*/) {
        /*System.out.print(params.getCategory());
        System.out.print(params.getPeriod());*/
        return "index";
    }
}
