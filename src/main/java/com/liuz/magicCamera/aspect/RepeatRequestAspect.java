package com.liuz.magicCamera.aspect;

import com.liuz.magicCamera.annotation.RepeatRequest;
import com.liuz.magicCamera.util.QETag;
import com.liuz.magicCamera.util.RestResponse;
import com.liuz.magicCamera.util.SimpleRedis;
import com.liuz.magicCamera.util.ToolUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;

@Aspect
@Component
public class RepeatRequestAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepeatRequestAspect.class);

    /**
     * 执行切面拦截逻辑
     */
    @Around("@annotation(repeatRequest)")
    public Object execute(ProceedingJoinPoint joinPoint, RepeatRequest repeatRequest) throws Throwable {
        if (repeatRequest != null) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            StringBuffer sb = new StringBuffer("");
            //1.获取参数
            HttpServletRequest request = attributes.getRequest();
            HttpServletResponse response = attributes.getResponse();

            //ip
            sb.append("ip:");
            String ip = ToolUtil.getClientIp(request);
            sb.append(ip);

            //header
            sb.append("head:");
            Enumeration<String> headers = request.getHeaderNames();
            while (headers.hasMoreElements()) {
                String heardName = headers.nextElement();
                String heardValue = request.getHeader(heardName);
                sb.append(heardName).append("=").append(heardValue).append(";");
            }

            //body
            sb.append("body:");
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < args.length; i++) {
                Object o = args[i];
                sb.append(o.toString());
            }

            //2.所有 请求头、报文体 进行组装，查询redis 是否存在记录

            LOGGER.info("sb=" + sb.toString());

            //sha 加密 、base64
            String hash = calcETag(sb.toString().getBytes(),sb.toString().getBytes().length);
            LOGGER.info("hash=" + hash);

            //存redis
            String key = "request_key_" + hash;
            String value = SimpleRedis.get(key);

            if (value!=null && "".equals(value)) {
                //3.存在记录
                //response.setStatus(304);
                //怕摘要重复 可进行value处理，碰撞几率太小，可以忽略...
                return RestResponse.failure("请勿重复请求");
            } else {
                //4.不存在记录，将所有参数组装写入redis，失效时间为3秒
                SimpleRedis.put(key, sb.toString(), 3);
            }


        }
        return joinPoint.proceed();
    }

    private String calcETag(byte[] datas, long dataLength) throws IOException,
            NoSuchAlgorithmException {
        String etag = "";
        if (dataLength <= CHUNK_SIZE) {
            System.out.println("datas="+byteArrayToHexString(datas));
            byte[] fileData = new byte[(int) dataLength];
            System.arraycopy(datas, 0, fileData, 0, datas.length);
            System.out.println("fileData="+byteArrayToHexString(fileData));
            byte[] sha1Data = sha1(fileData);
            System.out.println("sha1Data="+byteArrayToHexString(sha1Data));
            int sha1DataLen = sha1Data.length;
            byte[] hashData = new byte[sha1DataLen + 1];
            System.arraycopy(sha1Data, 0, hashData, 1, sha1DataLen);
            hashData[0] = 0x16;
            System.out.println("hashData="+byteArrayToHexString(hashData));
            etag = urlSafeBase64Encode(hashData);
            System.out.println("etag="+etag);

        } else {
            int chunkCount = (int) (dataLength / CHUNK_SIZE);
            if (dataLength % CHUNK_SIZE != 0) {
                chunkCount += 1;
            }
            byte[] allSha1Data = new byte[0];
            int copyCount = 0;
            System.out.println("==data==="+byteArrayToHexString(datas));
            for (int i = 0; i < chunkCount; i++) {
                int bytesReadLen;
                if (datas.length < CHUNK_SIZE) {
                    bytesReadLen = datas.length;
                } else {
                    bytesReadLen = CHUNK_SIZE;
                }
                byte[] chunkData = new byte[bytesReadLen];
                System.arraycopy(datas, 0, chunkData, copyCount, bytesReadLen);

                copyCount += bytesReadLen;
                System.out.println("==copy==="+byteArrayToHexString(chunkData));

                byte[] chunkDataSha1 = sha1(chunkData);
                byte[] newAllSha1Data = new byte[chunkDataSha1.length
                        + allSha1Data.length];
                System.arraycopy(allSha1Data, 0, newAllSha1Data, 0,
                        allSha1Data.length);

                System.arraycopy(chunkDataSha1, 0, newAllSha1Data,
                        allSha1Data.length, chunkDataSha1.length);
                allSha1Data = newAllSha1Data;
            }
            byte[] allSha1DataSha1 = sha1(allSha1Data);
            byte[] hashData = new byte[allSha1DataSha1.length + 1];
            System.arraycopy(allSha1DataSha1, 0, hashData, 1,
                    allSha1DataSha1.length);
            hashData[0] = (byte) 0x96;
            etag = urlSafeBase64Encode(hashData);
        }
        return etag;
    }


    private final int CHUNK_SIZE = 1 << 22;

    public byte[] sha1(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("sha1");
        return mDigest.digest(data);
    }

    public String urlSafeBase64Encode(byte[] data) {
        String encodedString = DatatypeConverter.printBase64Binary(data);
        encodedString = encodedString.replace('+', '-').replace('/', '_');
        return encodedString;
    }


    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }


    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

        }
        return d;
    }


    public static  void main(String [] arg){
        try{
            RepeatRequestAspect a=new RepeatRequestAspect();
            byte [] data=new byte[5];
            data[0]=0x11;
            data[1]=0x11;
            data[2]=0x11;
            data[3]=0x11;
            data[4]=0x11;

            String b="69703a303a303a303a303a303a303a303a31686561643a636f6f6b69653d4a53455353494f4e49443d30383738444433303135333336374537444139363737413330314636373443463b757365722d6167656e743d506f73746d616e52756e74696d652f372e31372e313b6163636570743d2a2f2a3b63616368652d636f6e74726f6c3d6e6f2d63616368653b706f73746d616e2d746f6b656e3d62336433383930632d656264642d343331342d396336302d3037323732313838663436303b686f73743d6c6f63616c686f73743a393130303b6163636570742d656e636f64696e673d677a69702c206465666c6174653b636f6e74656e742d6c656e6774683d303b636f6e6e656374696f6e3d6b6565702d616c6976653b626f64793a6f72672e6170616368652e636174616c696e612e636f6e6e6563746f722e526571756573744661636164654033643038386161";
            byte[] data1=hexStringToBytes(b);
            //byte[] aa= a.sha1(data);
            byte[] aa= a.sha1(data1);
            System.out.println(byteArrayToHexString(aa));
            String aaa = a.urlSafeBase64Encode(aa);
            System.out.println(aaa);
        }catch(Exception e){
            e.printStackTrace();
        }



    }


}








