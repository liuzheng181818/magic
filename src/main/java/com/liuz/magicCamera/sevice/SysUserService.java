package com.liuz.magicCamera.sevice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liuz.magicCamera.entity.SysUser;

import java.util.List;
import java.util.Map;

public interface SysUserService extends IService<SysUser> {

    List<SysUser> getUserList(Map map);
}
