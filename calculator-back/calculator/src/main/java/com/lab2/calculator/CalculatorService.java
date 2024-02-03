package com.lab2.calculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Arrays;
@Service
public class CalculatorService {
    public double evaluate(String[] exp) {
        double num1 = Double.parseDouble(exp[0]);
        double num2 = Double.parseDouble(exp[2]);
        double result = 0;
        switch (exp[1]) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                result = num1 / num2;
                break;
        }
        return result;
    }
    public double doSpecialOperations(String[] exp) {
        double num = Double.parseDouble(exp[0]);
        double result = 0;
        switch (exp[1]) {
            case "square":
                result = Math.pow(num, 2);
                break;
            case "root":
                result = Math.sqrt(num);
                break;
            case "reciprocal":
                result = 1 / num;
                break;
            case "percent":
                result = num/100;
                break;
        }
        return result;
    }
}