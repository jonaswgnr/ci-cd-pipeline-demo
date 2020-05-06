package de.jonaswgnr.cicddemo.cicddemo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class TestedController {

    @RequestMapping("/demo/tested")
    public String index() {
        return "Demo Application for CI/CD Pipeline; I am tested";
    }

}
