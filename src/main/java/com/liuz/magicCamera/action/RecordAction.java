package com.liuz.magicCamera.action;

import com.baidu.aip.imageclassify.AipImageClassify;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liuz.magicCamera.annotation.RepeatRequest;
import com.liuz.magicCamera.entity.RecordInfo;
import com.liuz.magicCamera.entity.Rescource;
import com.liuz.magicCamera.sevice.RecordInfoService;
import com.liuz.magicCamera.sevice.RescourceService;
import com.liuz.magicCamera.sevice.SysUserService;
import com.liuz.magicCamera.sevice.UploadService;
import com.liuz.magicCamera.util.ElementData;
import com.liuz.magicCamera.util.QETag;
import com.liuz.magicCamera.util.RestResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/record")
public class RecordAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecordAction.class);


    @Autowired
    SysUserService sysUserService;

    @Autowired
    RecordInfoService recordInfoService;

    @Autowired
    @Qualifier("localService")
    private UploadService localService;

    @Autowired
    private RescourceService rescourceService;


    //设置百度 APPID/AK/SK
    public static final String APP_ID = "16296788";
    public static final String API_KEY = "4nibpj5GthNfTgaic6T4nznO";
    public static final String SECRET_KEY = "pXMOpIrrKvh2MybgR1nbyOilHobUQXVv";

    @GetMapping("test")

    @ResponseBody
    public String list(HttpServletRequest request) {

        HttpSession session = request.getSession();

        // 初始化一个AipImageClassify
        AipImageClassify client = new AipImageClassify(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        /*client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理*/

        // 调用接口
        String path = "";
        //this.getClass().getClassLoader().getResource("/staic/img/aaa.jpg").getPath();
        try {
            File file = ResourceUtils.getFile("classpath:static/img/aaa.jpg");
            if (file.exists()) {
                path = file.getPath();
                /*File[] files = file.listFiles();
                if(files != null){
                    for(File childFile:files){
                        System.out.println(childFile.getName());
                    }
                }*/
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        /*File file =new File(path);
        System.out.print(file.exists());
        JSONObject res = client.objectDetect(path, new HashMap<String, String>());*/
        // return res.toString(2);

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
        return (res.toString(2));


    }


    @PostMapping("recordList")
    @ResponseBody
    @RepeatRequest
    public ElementData recordList(HttpServletRequest request) {
        Map map = new HashMap<>();
        map.put("userId", LoginController.getCurrentUserId());
        List<RecordInfo> recordInfoList = recordInfoService.getRecordList(map);
        ElementData<RecordInfo> elementData = new ElementData(recordInfoList);
        return elementData;
    }


    @RequestMapping("addRecord")
    @ResponseBody
    public RestResponse addRecord(MultipartFile file, HttpServletRequest httpServletRequest) {
        if (file == null) {
            return RestResponse.failure("上传文件为空 ");
        }
        Integer currentId= LoginController.getCurrentUserId();
        if(currentId.intValue()==0){
            return RestResponse.failure("游客不能进行此操作");
        }
        Map m = new HashMap();
        try {
            String fileName ="", extName = "", filePath = "";
            QETag tag = new QETag();
            String hash = tag.calcETag(file);
            Rescource rescource = rescourceService.getRescourceByHash(hash);
            String title="未能识别图像";
            String desc="未能识别图像的描述";
            if (rescource != null) {
                m.put("url", rescource.getWebUrl());
                m.put("name", rescource.getFileName());
            } else {
                String url = localService.upload(file);
                m.put("url", url);
                m.put("name", file.getOriginalFilename());
                fileName=file.getOriginalFilename();
                filePath = file.getOriginalFilename();
                rescource=new Rescource();
                rescource.setFileName(fileName);
                rescource.setFileSize(new java.text.DecimalFormat("#.##").format(file.getSize() / 1024) + "kb");
                rescource.setHash(hash);
                rescource.setFileType(extName);
                rescource.setWebUrl(url);
                rescource.setSource("local");
                rescourceService.save(rescource);

                JSONObject jsonObject= recordInfoService.baiduIdentification(url);
                LOGGER.info(jsonObject.toString(2));
                JSONArray jsonArray=jsonObject.getJSONArray("result");
                StringBuffer tmpDesc=new StringBuffer();
                if(jsonArray.length()>0){
                    JSONObject jobject=jsonArray.getJSONObject(0);
                    String tmpTitle=jobject.getString("keyword");
                    for (int i=0;i<jsonArray.length();i++){
                        jobject=jsonArray.getJSONObject(i);
                        tmpDesc.append(jobject.getString("keyword")+"、");
                    }
                    title=tmpTitle;
                    desc=tmpDesc.toString();
                }

            }

            RecordInfo recordInfo = new RecordInfo();
            recordInfo.setUserId(currentId);
            recordInfo.setFileId(rescource.getId());
            recordInfo.setRecordTitle(title);
            recordInfo.setRecordDate(new Date());
            recordInfo.setRecordDesc(desc);
            recordInfo.setRecordType(1);
            recordInfoService.save(recordInfo);
            m.put("dataId",recordInfo.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
        return RestResponse.success().setData(m);
    }


    @RequestMapping("deleteRecord")
    @ResponseBody
    public RestResponse deleteRecord(HttpServletRequest httpServletRequest) {
        String recordIdStr=httpServletRequest.getParameter("recordId");
        Long recordId;
        Integer currentId= LoginController.getCurrentUserId();
        if(currentId.intValue()==0){
            return RestResponse.failure("游客不能进行此操作");
        }
        if (recordIdStr == null || recordIdStr == "") {
            return RestResponse.failure("recordId为空 ");
        }else{
            recordId=Long.parseLong(recordIdStr);
        }
        try {
          RecordInfo recordInfo=  recordInfoService.getById(recordId);
            if (recordInfo != null) {
                boolean isDelete=recordInfoService.removeById(recordId);
                if(isDelete){
                    List recordList=recordInfoService.listObjs(new QueryWrapper<RecordInfo>().eq("file_id",recordInfo.getFileId()));
                    if(recordList.size()==0){
                        rescourceService.remove(new QueryWrapper<Rescource>().eq("id",recordInfo.getFileId()));
                    }
                }
            } else {
                return RestResponse.failure("记录不存在 ");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
        return RestResponse.success();
    }


    @GetMapping("testHttp")
    public void testHttp(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("join testHttp...");
        response.setStatus(303);
        //response.setHeader("Location","http://www.baidu.com");
    }


}
