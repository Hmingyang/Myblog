package com.yang.myblog_back.service;

import com.yang.myblog_back.po.User;

public interface UserService {
    User checkUser(String username, String password);
}
