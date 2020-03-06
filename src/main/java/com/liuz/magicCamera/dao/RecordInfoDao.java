package com.liuz.magicCamera.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liuz.magicCamera.entity.RecordInfo;
import com.liuz.magicCamera.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

public interface RecordInfoDao extends BaseMapper<RecordInfo> {

    List<RecordInfo> getRecordList(Map map);

}
