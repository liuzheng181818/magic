package com.liuz.magicCamera.action;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liuz.magicCamera.entity.SysUser;
import com.liuz.magicCamera.sevice.SysUserService;
import com.liuz.magicCamera.util.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/login")
public class LoginController {

    public static Map LOGIN_USER_MAP=new HashMap<>();

    public static final String RANDOM_CODE = "RANDOM_CODE";

    @Autowired
    SysUserService sysUserService;

    @PostMapping("main")
    @ResponseBody
    public RestResponse main(HttpServletRequest request,@RequestParam(value = "userName", required = false) String userName) {
        HttpSession session = request.getSession();
       // String userName=request.getParameter("userName");
        String passWord=request.getParameter("passWord");
        SysUser loginUser=sysUserService.getOne(new QueryWrapper<SysUser>().eq("login_name",userName));
        if(loginUser==null){
            return RestResponse.failure("用户不存在");
        }
        loginUser=sysUserService.getOne(new QueryWrapper<SysUser>().eq("login_name",userName).eq("password",passWord));
        if(loginUser==null){
            return RestResponse.failure("用户密码错误");
        }

        String randomCode =  UUID.randomUUID().toString();
        session.setAttribute(RANDOM_CODE, randomCode);
        LOGIN_USER_MAP.put(randomCode,loginUser.getId());
        return RestResponse.success();
    }

    public SysUser getCurrentUser(){
        SysUser loginUser=null;
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        Object randomCode =session.getAttribute(RANDOM_CODE);
        if(randomCode!=null){
            Object object= LOGIN_USER_MAP.get((String)randomCode);
            if(object!=null){
                loginUser=sysUserService.getOne(new QueryWrapper<SysUser>().eq("id",object));
            }
        }
        return  loginUser;

    }

    public static Integer getCurrentUserId(){
        Integer currentUserId=new Integer(0);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        Object randomCode =session.getAttribute(RANDOM_CODE);
        if(randomCode!=null){
            Object object= LOGIN_USER_MAP.get((String)randomCode);
            if(object!=null){
                currentUserId= Integer.valueOf(String.valueOf(object));
            }
        }
        return  currentUserId;

    }
}
