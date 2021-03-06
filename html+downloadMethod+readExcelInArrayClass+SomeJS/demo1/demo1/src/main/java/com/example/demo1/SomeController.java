package com.example.demo1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping(value="/update")
    public String update_page() {
        GetXLS.getDataExcel();
        return "index";
    }
}
