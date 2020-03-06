package com.liuz.magicCamera.util;

import java.util.List;

/**
 * Created by wangl on 2017/11/27.
 * todo:
 */
public class ElementData<T> {
    private Integer code = 0;
    private Integer count;
    private List<T> data;
    private String msg = "";

    public ElementData(){

    }

    public ElementData(Integer code, String msg){
        setCode(code);
        setMsg(msg);
        setCount(0);
    }

    public ElementData(List<T> data){
        if(data==null){
            this.data=null;
            count=0;
        }else {
            setData(data);
            setCount(data.size());
        }
    }

    public ElementData(List<T> data, Integer total){
        setData(data);
        setCount(total);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
