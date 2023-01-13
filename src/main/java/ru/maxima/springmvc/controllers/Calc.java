package ru.maxima.springmvc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Calc {
    @GetMapping("/calc")
    public String numbers(@RequestParam(value="num1", required=false) String var1,
                          @RequestParam(value="num2", required=false) String var2,
                          @RequestParam(value="symb", required=false) String symbol,
                          Model model){

        double res = 0;

        double number1 = Double.parseDouble(var1);
        double number2 = Double.parseDouble(var2);

        switch(symbol){
            case "sum" -> res = number1 + number2;
            case "sub" -> res = number1 - number2;
            case "mul" -> res = number1 * number2;
            case "div" -> res = number1 / number2;
            default -> res = Double.parseDouble(null);
        }
        model.addAttribute("result",res);
        return "calc";
    }
}