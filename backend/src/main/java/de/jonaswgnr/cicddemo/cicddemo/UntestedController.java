package de.jonaswgnr.cicddemo.cicddemo;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@ConditionalOnProperty(value="controller.untested.enabled", havingValue="true")
@RestController
public class UntestedController {

    @CrossOrigin(origins="*")
    @RequestMapping("/demo/untested")
    public String index() {
        return "Demo Application for CI/CD Pipeline; I am untested";
    }

}
