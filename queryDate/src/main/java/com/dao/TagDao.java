package com.dao;

import com.modle.DataInfo;

/**
 * Created by zxc on 2020/4/7.
 */
public interface TagDao {

    public  int getInfoBykey(DataInfo info, String dateType);
    public void getSha256Info(DataInfo dataInfo,int encryptType);
    public void get();

}
