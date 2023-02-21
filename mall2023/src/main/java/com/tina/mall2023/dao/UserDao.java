package com.tina.mall2023.dao;

import com.tina.mall2023.dto.UserRegisterRequest;
import com.tina.mall2023.model.User;

public interface UserDao {
    User getUserByEmail(String email);
    User getUserById(Integer userID);
    Integer createUser(UserRegisterRequest userRegisterRequest);
}
