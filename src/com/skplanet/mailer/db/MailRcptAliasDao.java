package com.skplanet.mailer.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tb_rcpt_alias")
public class MailRcptAliasDao {

    private Integer rcptId;
    private String alias;
    private String rcptAddr;
    private String updateTime;
    
    @Id
    @GeneratedValue
    @Column(name = "rcpt_id")
    public Integer getRcptId() {
        return rcptId;
    }
    public void setRcptId(Integer rcptId) {
        this.rcptId = rcptId;
    }
    @Column(name = "alias")
    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }
    @Column(name = "rcpt_addr")
    public String getRcptAddr() {
        return rcptAddr;
    }
    
    public void setRcptAddr(String rcptAddr) {
        this.rcptAddr = rcptAddr;
    }
    @Column(name = "update_Time")
    public String getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    
    
}
