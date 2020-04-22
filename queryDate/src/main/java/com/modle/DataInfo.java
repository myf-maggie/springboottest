package com.modle;

/**
 * Created by zxc on 2020/4/10.
 */
public class DataInfo {
    private String moble;
    private String pid;
    private String mobile256;
    private String pid256;
    private int type;
    private String time;

    public DataInfo(String pid, String moble) {
        this.moble = moble;
        this.pid = pid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMoble() {
        return moble;
    }

    public void setMoble(String moble) {
        this.moble = moble;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getMobile256() {
        return mobile256;
    }

    public void setMobile256(String mobile256) {
        this.mobile256 = mobile256;
    }

    public String getPid256() {
        return pid256;
    }

    public void setPid256(String pid256) {
        this.pid256 = pid256;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
}
