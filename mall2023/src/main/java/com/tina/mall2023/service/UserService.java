package com.tina.mall2023.service;

import com.tina.mall2023.dto.UserRegisterRequest;
import com.tina.mall2023.model.User;

public interface UserService {
    User getUserById(Integer userID);
    Integer register(UserRegisterRequest userRegisterRequest);
}
