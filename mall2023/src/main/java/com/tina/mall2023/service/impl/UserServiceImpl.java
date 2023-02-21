package com.tina.mall2023.service.impl;

import com.tina.mall2023.dao.UserDao;
import com.tina.mall2023.dto.UserLoginRequest;
import com.tina.mall2023.dto.UserRegisterRequest;
import com.tina.mall2023.model.User;
import com.tina.mall2023.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UserServiceImpl implements UserService {

    private  final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Override
    public User getUserById(Integer userID) {
        return userDao.getUserById(userID);
    }

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        // 檢查註冊的 email
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());
        if(user != null){
            log.warn("該 email {} 已經被註冊", userRegisterRequest.getEmail());
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        // 使用MD5 生成密碼的雜湊
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);

        // 創建帳號
        return userDao.createUser(userRegisterRequest);
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());

        // 檢查user是否存在
        if(user==null){
            log.warn("該 email {} 尚未註冊", userLoginRequest.getEmail());
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        // 使用  MD5  生成密碼的雜湊值
        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

        // 比較密碼
        if(user.getPassword().equals(hashedPassword)){
            return user;
        }else{
            log.warn("email {} 的密碼不正確", userLoginRequest.getEmail());
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }
}
