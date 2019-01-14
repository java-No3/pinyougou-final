package cn.itcast.core.service;

import cn.itcast.core.pojo.user.User;
import entity.PageResult;

import java.util.Map;

public interface UserService {
    void sendCode(String phone);

    void add(User user, String smscode);

    //个人信息注册 wph
    void regis(User user);



    PageResult search(Integer page, Integer rows, User user);

    User findOne(Long id);


    Map<String, Integer> findUserbyStatus();

    void blockUser(Long[] ids);
}
