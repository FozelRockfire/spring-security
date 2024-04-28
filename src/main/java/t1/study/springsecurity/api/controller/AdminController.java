package t1.study.springsecurity.api.controller;

import org.springframework.web.bind.annotation.RestController;
import t1.study.springsecurity.api.AdminApi;

@RestController
public class AdminController implements AdminApi {

    @Override
    public String testAdmin(){
        return "Я Админ!";
    }
}