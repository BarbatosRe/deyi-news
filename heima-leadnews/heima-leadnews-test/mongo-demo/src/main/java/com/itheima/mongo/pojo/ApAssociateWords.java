package com.itheima.mongo.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 联想词表
 * </p>
 *
 * @author itheima
 */

@Document("ap_associate_words")
public class ApAssociateWords implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 联想词
     */
    private String associateWords;

    /**
     * 创建时间
     */
    private Date createdTime;

    public ApAssociateWords() {

    }

    public ApAssociateWords(String associateWords, Date createdTime) {
        this.associateWords = associateWords;
        this.createdTime = createdTime;
    }

    public ApAssociateWords(String associateWords) {
        this(associateWords,new Date());
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAssociateWords() {
        return associateWords;
    }

    public void setAssociateWords(String associateWords) {
        this.associateWords = associateWords;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }


}