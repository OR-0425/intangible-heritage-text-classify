package com.example.intangibleheritagedemo.controller;

import com.example.intangibleheritagedemo.dto.LoginDTO;
import com.example.intangibleheritagedemo.entity.User;
import com.example.intangibleheritagedemo.service.UserService;
import com.example.intangibleheritagedemo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /** 注册 */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody LoginDTO dto) {

        // 1. 参数校验
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "用户名不能为空");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "密码不能为空");
        }

        // 2. 用户名查重
        User exist = userService.lambdaQuery()
                .eq(User::getUsername, dto.getUsername().trim())
                .one();
        if (exist != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "用户名已存在");
        }

        // 3. 插入新用户，角色固定为 user
        User user = new User();
        user.setUsername(dto.getUsername().trim());
        user.setPassword(dto.getPassword());   // 演示用明文
        user.setRole("user");
        userService.save(user);

        Map<String, Object> result = new HashMap<>();
        result.put("msg", "注册成功");
        result.put("id", user.getId());
        return result;
    }

    /** 登录 */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginDTO dto) {

        User user = userService.lambdaQuery()
                .eq(User::getUsername, dto.getUsername())
                .one();

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "账号不存在");
        }
        if (!user.getPassword().equals(dto.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "密码错误");
        }

        String token = JwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("username", user.getUsername());
        result.put("role", user.getRole());
        return result;
    }
}
