package com.blockChain.service.Impl;

import com.blockChain.Enum.CodeEnum;
import com.blockChain.dao.UserDAO;
import com.blockChain.entity.UserEntity;
import com.blockChain.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDAO userDAO;

    @Override
    public int addUser(String username, String password, String email) {

        if (userDAO.selectUserByUsername(username) != null){
            return CodeEnum.USERNAME_REGISTRIED.getCode();
        }
        if (userDAO.selectUserByEmail(email) != null){
            return CodeEnum.EMAIL_REGISTRIED.getCode();
        }

        try {
            userDAO.insertUser(username,password,email);
        }catch (Exception e){
            e.printStackTrace();
            return CodeEnum.INNER_ERROR.getCode();
        }

        return CodeEnum.SUCCESS.getCode();
    }

    @Override
    public int checkUser(String username, String password) {

        if (userDAO.selectUserCountByUsername(username) <= 0){
            return CodeEnum.USER_NOT_EXIST.getCode();
        }

        UserEntity user;
        try{
            user = userDAO.selectUserByUsername(username);
        }catch (Exception e){
            e.printStackTrace();
            return CodeEnum.INNER_ERROR.getCode();
        }
        if (!password.equals(user.getPassword())){
            return CodeEnum.USER_CHECK_FAILED.getCode();
        }
        return CodeEnum.SUCCESS.getCode();
    }
}
