package com.example.intangibleheritagedemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.intangibleheritagedemo.entity.User;
import com.example.intangibleheritagedemo.mapper.UserMapper;
import com.example.intangibleheritagedemo.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
