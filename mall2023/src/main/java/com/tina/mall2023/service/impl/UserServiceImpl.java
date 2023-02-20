package com.tina.mall2023.service.impl;

import com.tina.mall2023.dao.UserDao;
import com.tina.mall2023.dto.UserRegisterRequest;
import com.tina.mall2023.model.User;
import com.tina.mall2023.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User getUserById(Integer userID) {
        return userDao.getUserById(userID);
    }

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        return userDao.createUser(userRegisterRequest);
    }
}
