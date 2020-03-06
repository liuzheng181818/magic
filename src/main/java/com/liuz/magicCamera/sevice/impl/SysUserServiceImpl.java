package com.liuz.magicCamera.sevice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liuz.magicCamera.dao.RecordInfoDao;
import com.liuz.magicCamera.dao.SysUserDao;
import com.liuz.magicCamera.entity.RecordInfo;
import com.liuz.magicCamera.entity.SysUser;
import com.liuz.magicCamera.sevice.RecordInfoService;
import com.liuz.magicCamera.sevice.SysUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUser> implements SysUserService {
    @Override
    public List<SysUser> getUserList(Map map) {
        return baseMapper.getUserList(map);
    }
}
