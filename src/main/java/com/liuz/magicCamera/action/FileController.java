package com.liuz.magicCamera.action;

import com.liuz.magicCamera.sevice.UploadService;
import com.liuz.magicCamera.util.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("file")
public class FileController {
    private static final Logger LOG = LoggerFactory.getLogger(FileController.class);

    @Value("uploadType")
    private String uploadType;

    @Value("localUploadPath")
    private String localUploadPath;

    @Autowired
    @Qualifier("localService")
    private UploadService localService;

    @RequestMapping("upload")
    @ResponseBody
    public RestResponse uploadFile( MultipartFile file, HttpServletRequest httpServletRequest) {
        if (file == null) {
            return RestResponse.failure("上传文件为空 ");
        }
        Map m = new HashMap();
        try {
            String url = "/" + localService.upload(file);
            m.put("url", url);
            m.put("name", file.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
            return RestResponse.failure(e.getMessage());
        }
        return RestResponse.success().setData(m);
    }

}
