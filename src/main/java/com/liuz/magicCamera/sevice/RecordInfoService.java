package com.liuz.magicCamera.sevice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liuz.magicCamera.entity.RecordInfo;
import com.liuz.magicCamera.entity.SysUser;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface RecordInfoService extends IService<RecordInfo> {

    List<RecordInfo> getRecordList(Map map);

    JSONObject baiduIdentification(String path);


}
