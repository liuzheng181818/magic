package com.liuz.magicCamera.sevice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liuz.magicCamera.dao.RescourceDao;
import com.liuz.magicCamera.entity.Rescource;
import com.liuz.magicCamera.sevice.RescourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 系统资源 服务实现类
 * </p>
 *
 * @author wangl
 * @since 2018-01-14
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class RescourceServiceImpl extends ServiceImpl<RescourceDao, Rescource> implements RescourceService {

    @Override
    public int getCountByHash(String hash) {
        QueryWrapper<Rescource> wrapper = new QueryWrapper<>();
        wrapper.eq("hash",hash);
        return this.count(wrapper);
    }

    @Override
    public Rescource getRescourceByHash(String hash) {
        QueryWrapper<Rescource> wrapper = new QueryWrapper<>();
        wrapper.eq("hash",hash);
        return this.getOne(wrapper);
    }
}
