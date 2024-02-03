package com.lab2.calculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class CalculatorController {
    @Autowired
    private CalculatorService service;
    @PostMapping("/normalOp")
    public double pressEqual(@RequestBody String[] exp){
        System.out.println( service.evaluate(exp));
        return service.evaluate(exp);
    }

    @PostMapping("/specialOp")
    public double pressSpecialOperation(@RequestBody String[] exp){
        System.out.println(exp);
        return service.doSpecialOperations(exp);
    }
}