package com.dao;

import com.modle.DataInfo;
import com.modle.KeyType;
import com.zzc.cassandra.NezhaAccessor;
import com.zzc.cassandra.constant.Enum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zxc on 2020/4/7.
 */
@Component
public class TagDaoImplss{

    private static final Logger logger = LogManager.getLogger("TagDaoImplss");
    private static final String TBL_PLACEHOLDER_QUERY_TAG = "$tbl$_query_tag";
    private static final String TBL_PLACEHOLDER_QUERY_GAOGA = "$tbl$_query_gaofa";
    private static final String TBL_PLACEHOLDER_QUERY_P2P = "$tbl$_query_p2p";
    private static final String KKY = "$$olumn_query";

    public static final String QUERY_TAG_SQL = "SELECT count(1) count from " + TBL_PLACEHOLDER_QUERY_TAG + " where `key`="+KKY;
    public static final String QUERY_GAOFA_SQL = "SELECT count(1) count from " + TBL_PLACEHOLDER_QUERY_GAOGA + " where docId=:pid and docType=0";
    public static final String QUERY_P2P_SQL = "SELECT count(1) count from " + TBL_PLACEHOLDER_QUERY_P2P + " where (docId=:pid and subjectType=0) or mobile=:mobile";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired @Qualifier("namedParameterJdbcTemplate")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private NezhaAccessor nezhaAccessor;

    @Autowired @Qualifier("namedParameterJdbcTemplateBack")
    public NamedParameterJdbcTemplate tagbackJdbcTemplate;



    public int getsq(DataInfo dataInfo,String dateType){
        int mobilecopunt=0;
        int pidcount=0;

        try {
            if(dataInfo.getMobile256()!=null && !"".equals(dataInfo.getMobile256())){
                mobilecopunt=nezhaAccessor.getByTable(Enum.Table.BY_PHONE,dataInfo.getMobile256()).size();
            }
            if(dataInfo.getPid256()!=null && !"".equals(dataInfo.getPid256())){
                pidcount= nezhaAccessor.getByTable(Enum.Table.BY_PID,dataInfo.getPid256()).size();
            }
        }catch (Exception e){
            logger.info("getsq"+e);
        }
        return mobilecopunt+pidcount;
    }

    public int getgaofa(DataInfo info,String dateType){
        try {
            Map<String,String> map=new HashMap<>();
            map.put("mobile",info.getMobile256());
            map.put("pid",info.getPid256());
            String sql=QUERY_GAOFA_SQL.replace(TBL_PLACEHOLDER_QUERY_GAOGA," tb_high_court_lose_credit ");
            if(sql!=null && !"".equals(sql)){
                int count=namedParameterJdbcTemplate.queryForObject(sql,map,Integer.class);
                return count;
            }
        }catch (Exception e){
            logger.info("getgaofa"+e);
        }
        return 0;
    }

    public int getp2p(DataInfo info,String dataType){
        try {
            Map<String,String> map=new HashMap<>();
            map.put("mobile",info.getMobile256());
            map.put("pid",info.getPid256());
            String sql=QUERY_P2P_SQL.replace(TBL_PLACEHOLDER_QUERY_P2P," tb_p2p_blacklist ");
            if(sql!=null && !"".equals(sql)){
                int count=namedParameterJdbcTemplate.queryForObject(sql,map,Integer.class);
                return count;
            }

        }catch (Exception e){
            logger.info("getp2p"+e);

        }
        return 0;
    }


    public int getoverdue(DataInfo info,String dataType){
        Map<String,String> map=new HashMap<>();
        map.put("mobile",info.getMobile256());
        map.put("pid",info.getPid256());
        int mobilecount=0;
        int pidcount=0;
        try {
        if(info.getTime()==null || "".equals(info.getTime())){
            //不回溯
            if(info.getMobile256()!=null && !"".equals(info.getMobile256())){
                String tblNoMoble="tag_mobile_"+Integer.parseInt(info.getMobile256().substring(6,8),16)%256+" ";
                String overSqlMobile=QUERY_TAG_SQL.replace(TBL_PLACEHOLDER_QUERY_TAG,tblNoMoble).replace(KKY,":mobile");
                mobilecount=namedParameterJdbcTemplate.queryForObject(overSqlMobile,map,Integer.class);
            }
            if(info.getPid256()!=null && !"".equals(info.getPid256())){
                String tblNoPid="tag_pid_"+Integer.parseInt(info.getPid256().substring(6,8),16)%256+" ";
                String overSqlPid=QUERY_TAG_SQL.replace(TBL_PLACEHOLDER_QUERY_TAG,tblNoPid).replace(KKY,":pid");
                pidcount=namedParameterJdbcTemplate.queryForObject(overSqlPid,map,Integer.class);
            }
        }else {
            //回溯
            if(info.getMobile256()!=null && !"".equals(info.getMobile256())){
                String tblNoMoble="tag_mobile_"+info.getTime().replaceAll("-","").substring(0,6);
                String overSqlMobile=QUERY_TAG_SQL.replace(TBL_PLACEHOLDER_QUERY_TAG,tblNoMoble).replace(KKY,":mobile");
                mobilecount=tagbackJdbcTemplate.queryForObject(overSqlMobile,map,Integer.class);
            }
            if(info.getPid256()!=null && !"".equals(info.getPid256())){
                String tblNoPid="tag_pid_"+info.getTime().replaceAll("-","").substring(0,6);
                String overSqlPid=QUERY_TAG_SQL.replace(TBL_PLACEHOLDER_QUERY_TAG,tblNoPid).replace(KKY,":pid");
                pidcount=tagbackJdbcTemplate.queryForObject(overSqlPid,map,Integer.class);
            }
        }
        }catch (Exception e){
            logger.info("getoverdue"+e);
        }
        return mobilecount+pidcount;
    }

    public void getSha256Info(DataInfo dataInfo,int encryptType){
        if(encryptType==3){
            String mobile=getSh256FromMd5(KeyType.mobile,dataInfo.getMoble());
            String pid=getSh256FromMd5(KeyType.pid,dataInfo.getPid());
            dataInfo.setMobile256(mobile);
            dataInfo.setPid256(pid);
        }
        if(encryptType==4){
            String mobile=getSh256FromSm3(KeyType.mobile,dataInfo.getMoble());
            String pid=getSh256FromSm3(KeyType.pid,dataInfo.getPid());
            dataInfo.setMobile256(mobile);
            dataInfo.setPid256(pid);
        }
    }


    public String getSh256FromMd5(KeyType type, String md5Value) {
        String keyType = type == KeyType.pid ? "pid" : "mobile";
        try {
            // 只取16位，数据库中
            md5Value = md5Value.length() == 32 ? md5Value.substring(8, 24) : md5Value;

            String table = getBridgeTableName(md5Value, "md5_" + keyType);
            String sql = "SELECT sha256 FROM " + table + " as t where t.md5=?";
            return jdbcTemplate.queryForObject(sql, new Object[]{md5Value}, String.class);
        } catch (Exception e) {
            logger.warn("getSha256 failed:{}", e.getMessage());
            return null;
        }
    }

    public String getSh256FromSm3(KeyType type, String md5Value) {
        String keyType = type == KeyType.pid ? "pid" : "mobile";
        try {
            String table = getBridgeTableName(md5Value, "sm3_" + keyType);
            String sql = "SELECT sha256 FROM " + table + " as t where t.sm3=?";
            return jdbcTemplate.queryForObject(sql, new Object[]{md5Value}, String.class);
        } catch (Exception e) {
            logger.warn("getSha256 failed:{}", e.getMessage());
            return null;
        }
    }

    private static String getBridgeTableName(String keyValue, String tablePrefix) {
        StringBuilder result = new StringBuilder();
        int mod = Integer.parseInt(keyValue.substring(6, 8), 16) % 64;
        result.append(tablePrefix).append("_").append(mod);
        return result.toString();
    }

    public void get() {
        Map<String,String> map=new HashMap<>();
        map.put("mobile","2abcb8b8e0f3970f15b50a9e6159755f004680819303fd93424fe83988bb5ad4");
        map.put("pid",null);

        int a=namedParameterJdbcTemplate.queryForObject("SELECT count(1) count from tb_p2p_blacklist where  mobile=:mobile",map,Integer.class);

    }
}
