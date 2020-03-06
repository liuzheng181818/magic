package com.liuz.magicCamera.sevice.impl;

import com.baidu.aip.imageclassify.AipImageClassify;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liuz.magicCamera.action.RecordAction;
import com.liuz.magicCamera.dao.RecordInfoDao;
import com.liuz.magicCamera.entity.RecordInfo;
import com.liuz.magicCamera.sevice.RecordInfoService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("recordInfoService")
public class RecordInfoServiceImpl extends ServiceImpl<RecordInfoDao, RecordInfo> implements RecordInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecordInfoServiceImpl.class);

    //设置百度 APPID/AK/SK
    public static final String APP_ID = "16296788";
    public static final String API_KEY = "4nibpj5GthNfTgaic6T4nznO";
    public static final String SECRET_KEY = "pXMOpIrrKvh2MybgR1nbyOilHobUQXVv";

    @Override
    public List<RecordInfo> getRecordList(Map map) {
        return baseMapper.getRecordList(map);
    }

    @Override
    public JSONObject baiduIdentification(String path){
        // 初始化一个AipImageClassify
        AipImageClassify client = new AipImageClassify(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        /*client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理*/

        // 调用接口
        //this.getClass().getClassLoader().getResource("/staic/img/aaa.jpg").getPath();
        try {

            //File file = ResourceUtils.getFile("classpath:static/img/aaa.jpg");
            File file = ResourceUtils.getFile("classpath:"+path.substring(1));
            if (file.exists()) {
                path = file.getPath();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("baike_num", "5");
        System.out.println("path=" + path);
        LOGGER.info("path=" + path);

        try {
            StringBuffer sb = new StringBuffer(ResourceUtils.getURL("classpath:").getPath());
            String filePath = sb.append("static/upload/").toString();
            LOGGER.info("filePath=" + filePath);
            System.out.println("filePath=" + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 参数为本地路径
        JSONObject res = client.advancedGeneral(path, options);

        return res;
    }
}
