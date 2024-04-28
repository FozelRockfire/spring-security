package t1.study.springsecurity.api.controller;

import org.springframework.web.bind.annotation.RestController;
import t1.study.springsecurity.api.UserApi;

@RestController
public class UserController implements UserApi {

    @Override
    public String testUser(){
        return "Я Юзер...";
    }
}