package com.liuz.magicCamera.sevice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liuz.magicCamera.entity.Rescource;

/**
 * <p>
 * 系统资源 服务类
 * </p>
 *
 * @author wangl
 * @since 2018-01-14
 */
public interface RescourceService extends IService<Rescource> {

    int getCountByHash(String hash);

    Rescource getRescourceByHash(String hash);

}
