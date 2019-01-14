package cn.itcast.core.controller;

import cn.itcast.core.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {
    @Reference
    private UserService userService;

    @RequestMapping("/findUserbyStatus")
    public Map<String,Integer> findUserbyStatus(){
       return userService.findUserbyStatus();
    }
}
