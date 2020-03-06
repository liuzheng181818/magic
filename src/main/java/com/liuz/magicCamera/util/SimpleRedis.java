package com.liuz.magicCamera.util;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
@Component
public class SimpleRedis implements InitializingBean {

    public static Map redis=new HashMap<>();

    public static CopyOnWriteArrayList<Timebean> timeList=new CopyOnWriteArrayList<>();

    public static void put(Object key, Object value){
        redis.put(key,value);
    }

    public static void put(Object key, Object value,int expire){
        redis.put(key,value);
        Calendar calendar=Calendar.getInstance();
        Date currentDate=new Date();
        System.out.println("currentDate="+currentDate);
        calendar.setTime(currentDate);
        calendar.add(Calendar.SECOND,expire);
        Date expireDate=calendar.getTime();
        System.out.println("expireDate="+expireDate);
        Timebean timebean = new SimpleRedis().new Timebean(key,expireDate);
        timeList.add(timebean);
    }

    public static Object get(Object key){
       return redis.get(key);
    }

    public static String get(String key){
        if(redis.get(key)!=null){
            return redis.get(key).toString();
        }
        return  null;

    }

    public static void remove(Object key){
        redis.remove(key);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("启动simpleRedis");
        new Thread(() -> run()).start();
    }

    class Timebean{
        public Timebean(Object key,Date expireTime){
            this.key=key;
            this.expireTime=expireTime;
        }

        private Object key;

        private Date expireTime;

        public Object getKey() {
            return key;
        }

        public void setKey(Object key) {
            this.key = key;
        }

        public Date getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(Date expireTime) {
            this.expireTime = expireTime;
        }
    }

    private void clearRedis(){
        while(true){
            try{
                for (Timebean timebean : timeList) {
                    System.out.println("==1="+timebean.getExpireTime());
                    Date currentDate=new Date();
                    System.out.println("==2="+currentDate);
                    if(currentDate.getTime() >= timebean.getExpireTime().getTime()){
                        redis.remove(timebean.getKey());
                        timeList.remove(timebean);
                        System.out.println("清理过期simpleReids:"+timebean.getKey().toString());
                    }
                }
                Thread.sleep(200L);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    private void run(){
        clearRedis();
    }

}
