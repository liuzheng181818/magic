package com.liuz.magicCamera.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@TableName("sys_user")
public class SysUser {
    @TableField("id")
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("login_name")
    private String loginName;
    @TableField("user_name")
    private String userName;
    @TableField("password")
    private String password;
    @TableField("mobile")
    private String mobile;
    @TableField("state")
    private Integer state;
    @TableField("del_flag")
    private Integer delFlag;
    @TableField("create_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private Date createDate;
    @TableField("create_by")
    private Integer createBy;
    @TableField("update_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private Date updateDate;
    @TableField("update_by")
    private Integer updateBy;
    @TableField("remarks")
    private String remarks;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
