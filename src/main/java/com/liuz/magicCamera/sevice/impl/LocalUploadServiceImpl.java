package com.liuz.magicCamera.sevice.impl;

import com.liuz.magicCamera.entity.UploadInfo;
import com.liuz.magicCamera.sevice.UploadService;
import com.liuz.magicCamera.util.RandomUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

@Service("localService")
public class LocalUploadServiceImpl implements UploadService {
    @Override public String upload(MultipartFile file) throws IOException{
        byte[] data = file.getBytes();
        String extName = file.getOriginalFilename().substring(
                file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID() + extName;
        String contentType = file.getContentType();
        StringBuffer sb = new StringBuffer(ResourceUtils.getURL("classpath:").getPath());
        String filePath = sb.append("static/upload/").toString();
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(data);
        out.flush();
        out.close();
        String webUrl = "/static/upload/"+fileName;
        return webUrl;
    }

    @Override
    public Boolean delete(String path) {
        path = path.replaceFirst("\\/","classpath:");
        File file = new File(path);
        if(file.exists()){
            file.delete();
            return true;
        }else{
            return false;
        }
    }

    @Override
    public String uploadNetFile(String url) throws IOException, NoSuchAlgorithmException {

        String extName = url.substring(url.lastIndexOf("."));
        String fileName = UUID.randomUUID() + extName;
        StringBuffer sb = new StringBuffer(ResourceUtils.getURL("classpath:").getPath());
        String filePath = sb.append("static/upload/").toString();
        File uploadDir = new File(filePath);
        if(!uploadDir.exists()){
            uploadDir.mkdirs();
        }
        URL neturl=new URL(url);
        HttpURLConnection conn=(HttpURLConnection)neturl.openConnection();
        conn.connect();
        BufferedInputStream br = new BufferedInputStream(conn.getInputStream());
        byte[] buf = new byte[1024];
        int len = 0;
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        while ((len = br.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        File targetFile = new File(filePath+fileName);
        String webUrl = "";
        if(targetFile.exists()){
            webUrl = "/static/upload/"+fileName;

        }
        br.close();
        out.flush();
        out.close();
        conn.disconnect();
        return webUrl;
    }

    @Override
    public String uploadLocalImg(String localPath) {
        File file = new File(localPath);

        StringBuffer sb = null;
        try {
            sb = new StringBuffer(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String filePath = sb.append("static/upload/").toString();
        StringBuffer name = new StringBuffer(""+Math.random());
        StringBuffer returnUrl = new StringBuffer("/static/upload/");
        String  extName = "";
        extName = file.getName().substring(
                file.getName().lastIndexOf("."));
        sb.append(name).append(extName);
        File uploadDir = new File(filePath);
        if(!uploadDir.exists()){
            uploadDir.mkdir();
        }
        try {
            InputStream input = new FileInputStream(file);
            byte[] buf = new byte[input.available()];
            int len = 0;
            FileOutputStream out = new FileOutputStream(sb.toString());
            while ((len = input.read(buf)) > 0){
                out.write(buf, 0, len);
            }
            input.close();
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        returnUrl.append(name).append(extName);
        return returnUrl.toString();
    }

    @Override
    public String uploadBase64(String base64) {
        StringBuffer webUrl=new StringBuffer("/static/upload/");
        try
        {
            //Base64解码
            byte[] b = Base64.getDecoder().decode(base64);
            for(int i=0;i<b.length;++i)
            {
                if(b[i]<0)
                {//调整异常数据
                    b[i]+=256;
                }
            }
            //生成jpeg图片
            StringBuffer ss = new StringBuffer(ResourceUtils.getURL("classpath:").getPath());
            String filePath = ss.append("static/upload/").toString();
            File targetFileDir = new File(filePath);
            if(!targetFileDir.exists()){
                targetFileDir.mkdirs();
            }
            StringBuffer sb = new StringBuffer(filePath);
            StringBuffer fileName = new StringBuffer(RandomUtil.generateString(12));
            sb.append(fileName);
            sb.append(".jpg");
            String imgFilePath = sb.toString();//新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return webUrl.append(sb).toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


}
