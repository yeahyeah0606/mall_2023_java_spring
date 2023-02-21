package com.tina.mall2023.dao.impl;

import com.tina.mall2023.dao.UserDao;
import com.tina.mall2023.dto.UserRegisterRequest;
import com.tina.mall2023.model.User;
import com.tina.mall2023.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private NamedParameterJdbcTemplate npjt;

    @Override
    public User getUserById(Integer userID) {
        String sql = "SELECT user_id, email, password, created_date, last_modified_date "+
                "FROM user WHERE user_id = :userID";
        Map<String, Object> map = new HashMap<>();
        map.put("userID", userID);

        List<User> userList = npjt.query(sql, map, new UserRowMapper());
        if(userList.size()>0){
            return userList.get(0);
        }else{
            return null;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT user_id, email, password, created_date, last_modified_date "+
                "FROM user WHERE email = :email";
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);

        List<User> userList = npjt.query(sql, map, new UserRowMapper());
        if(userList.size()>0){
            return userList.get(0);
        }else{
            return null;
        }
    }

    @Override
    public Integer createUser(UserRegisterRequest userRegisterRequest) {
        String sql = "INSERT INTO user(email, password,  created_date, last_modified_date) " +
                "VALUES (:email, :password, :createdDate, :lastModifiedDate)";

        Map<String, Object> map = new HashMap<>();
        map.put("email", userRegisterRequest.getEmail());
        map.put("password", userRegisterRequest.getPassword());

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        npjt.update(sql, new MapSqlParameterSource(map), keyHolder);

        int useID = keyHolder.getKey().intValue();
        return  useID;
    }
}
