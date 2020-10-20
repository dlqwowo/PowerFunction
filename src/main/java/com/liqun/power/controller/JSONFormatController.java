package com.liqun.power.controller;


import com.liqun.power.annotation.ResponseBodyResultAdvice;
import com.liqun.power.entity.Person;
import com.liqun.power.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 *@Author: PowerQun
 */
@RestController
public class JSONFormatController {

    @GetMapping("/getResultFormat")
    public Result<Person> getResultFormat() {
        return Result.success(new Person("Tom", "Li Gong Di branch"));
    }

    @GetMapping("/getObjectFormat")
    public Person getObjectFormat() {
        return new Person("Tom", "Li Gong Di branch");
    }

    /**
     * PF-001
     * @return
     */
    @GetMapping("/getSelfFormat")
    @ResponseBodyResultAdvice
    public Person getSelfFormat() {
        return new Person("Tom", "Li Gong Di branch");
    }
}
