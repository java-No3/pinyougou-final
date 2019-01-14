package cn.itcast.core.controller;

import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.PageResult;

import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 用户统计
 */
@RestController
@RequestMapping("/user")
public class userController {

    @Reference
    private UserService userService;

    @RequestMapping("/search")
    public PageResult search(Integer pageNum, Integer pageSize, @RequestBody User user) {

        return userService.search(pageNum, pageSize, user);
    }


    @RequestMapping("/findOne")
    public User findOne(Long id){

        return userService.findOne(id);

    }

    @RequestMapping("/findUserbyStatus")
    public Map<String,Integer> findUserbyStatus(){
        return userService.findUserbyStatus();
    }

    /**
     * 冻结用户
     * @param ids
     * @return
     */
    @RequestMapping("/blockUser")
    public Result blockUser(Long[] ids){
        try {
            userService.blockUser(ids);
            return new Result(true,"成功");
        } catch (Exception e) {
            return new Result(false,"失败,请重试");
        }
    }

}
