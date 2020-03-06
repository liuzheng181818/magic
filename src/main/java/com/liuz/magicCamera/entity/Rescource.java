package com.liuz.magicCamera.entity;

import com.baomidou.mybatisplus.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>
 * 系统资源
 * </p>
 *
 * @author wangl
 * @since 2018-01-14
 */
@TableName("sys_rescource")
public class Rescource {

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @TableField("id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 创建者
     */
    @TableField(value = "create_by", fill = FieldFill.INSERT)
    protected Long createBy;

    /**
     * 创建日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    @TableField(value = "create_date", fill = FieldFill.INSERT)
    protected Date createDate;

    /**
     * 更新者
     */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    protected Long updateBy;
    /**
     * 更新日期
     */
    @TableField(value = "update_date", fill = FieldFill.INSERT_UPDATE)
    protected Date updateDate;

    /**
     * 删除标记（Y：正常；N：删除；A：审核；）
     */
    @TableField(value = "del_flag")
    protected Boolean delFlag;

    /**
     * 备注
     */
    @TableField
    protected String remarks;


    /**
     * 文件名称
     */
    @TableField("file_name")
    private String fileName;
    /**
     * 来源
     */
    private String source;
    /**
     * 资源网络地址
     */
    @TableField("web_url")
    private String webUrl;
    /**
     * 文件标识
     */
    private String hash;
    /**
     * 文件大小
     */
    @TableField("file_size")
    private String fileSize;
    /**
     * 文件类型
     */
    @TableField("file_type")
    private String fileType;

    @TableField("original_net_url")
    private String originalNetUrl;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getOriginalNetUrl() {
        return originalNetUrl;
    }

    public void setOriginalNetUrl(String originalNetUrl) {
        this.originalNetUrl = originalNetUrl;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "Rescource{" +
                ", fileName=" + fileName +
                ", source=" + source +
                ", webUrl=" + webUrl +
                ", hash=" + hash +
                ", fileSize=" + fileSize +
                ", fileType=" + fileType +
                ", originalNetUrl=" + originalNetUrl +
                "}";
    }
}
