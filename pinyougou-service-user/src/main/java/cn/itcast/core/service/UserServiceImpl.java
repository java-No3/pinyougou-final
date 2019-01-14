package cn.itcast.core.service;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户管理
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private Destination smsDestination;
    @Autowired
    private UserDao userDao;
    //发送验证码
    public void sendCode(String phone){
        //1:生成6位随机数
        String randomNumeric = RandomStringUtils.randomNumeric(6);
        //2:
        redisTemplate.boundValueOps(phone).set(randomNumeric);
        redisTemplate.boundValueOps(phone).expire(5, TimeUnit.DAYS);//实际是5分钟
        //3:发消息
        jmsTemplate.send(smsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("phone",phone);
                mapMessage.setString("signName","品优购商城");
                mapMessage.setString("templateCode","SMS_126462276");
                mapMessage.setString("templateParam","{\"number\":\""+randomNumeric+"\"}");
                return mapMessage;
            }
        });

    }

    //添加
    @Override
    public void add(User user, String smscode) {
        //1:判断验证码是否正确
        String code = (String) redisTemplate.boundValueOps(user.getPhone()).get();
        if(null != code){
            if(code.equals(smscode)){
                //添加
                user.setCreated(new Date());
                user.setUpdated(new Date());
                userDao.insertSelective(user);
            }else{
                //验证码错误
                throw new RuntimeException("验证码错误");
            }
        }else{
            //验证码失效
            throw new RuntimeException("验证码失效");
        }
    }

    //个人信息注册 wph
    @Override
    public void regis(User user) {
        UserQuery userQuery = new UserQuery();
        userQuery.createCriteria().andUsernameEqualTo(user.getUsername());
        userDao.updateByExampleSelective(user,userQuery);
    }


    @Override
    public PageResult search(Integer page, Integer rows, User user) {
        //分页插件
        PageHelper.startPage(page, rows);

        UserQuery userQuery = new UserQuery();
        UserQuery.Criteria criteria = userQuery.createCriteria();
        //判断用户名称   "   "
        if(null != user.getUsername() && !"".equals(user.getUsername().trim())){
            criteria.andNameLike("%"+ user.getUsername().trim() + "%");
        }
        //判断用户id
        if(null != user.getId() && !"".equals(user.getId())){
            criteria.andIdEqualTo(user.getId());
        }

        //查询
        Page<User> p = (Page<User>) userDao.selectByExample(userQuery);

        return new PageResult(p.getTotal(), p.getResult());
    }

    @Override
    public User findOne(Long id) {
        return userDao.selectByPrimaryKey(id);
    }

    @Override
    public Map<String, Integer> findUserbyStatus() {
        UserQuery userQuery = new UserQuery();
        userQuery.createCriteria().andStatusEqualTo("1");
        List<User> users = userDao.selectByExample(userQuery);
        List<User> users1 = userDao.selectByExample(null);
        HashMap<String, Integer> map = new HashMap<>();
        map.put("nomal",(users1.size() - users.size()));
        map.put("unnomal",users.size());
        return map;
    }

    /**
     * 冻结用户 szj
     * @param ids
     */
    @Override
    public void blockUser(Long[] ids) {
        User user = new User();
        user.setStatus("1");
        UserQuery userQuery = new UserQuery();
        UserQuery.Criteria criteria = userQuery.createCriteria();
        criteria.andIdIn(Arrays.asList(ids));
        userDao.updateByExampleSelective(user,userQuery);
    }


}
