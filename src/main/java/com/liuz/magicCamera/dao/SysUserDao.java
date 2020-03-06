package com.liuz.magicCamera.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liuz.magicCamera.entity.SysUser;

import java.util.List;
import java.util.Map;

public interface SysUserDao extends BaseMapper<SysUser> {

    List<SysUser> getUserList(Map map);

}
