package com.dao;

import com.modle.DataInfo;
import com.modle.KeyType;
import com.zzc.cassandra.NezhaAccessor;
import com.zzc.cassandra.constant.Enum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zxc on 2020/4/7.
 */
@Component
public class TagDaoImpl{

    private static final Logger logger = LogManager.getLogger("TagDaoImpl");
    private static final String TBL_PLACEHOLDER_QUERY_TAG = "$tbl$_query_tag";
    private static final String TBL_PLACEHOLDER_QUERY_GAOGA = "$tbl$_query_gaofa";
    private static final String TBL_PLACEHOLDER_QUERY_P2P = "$tbl$_query_p2p";
    private static final String KKY = "$$olumn_query";

    public static final String QUERY_TAG_SQL = "SELECT count(1) count from " + TBL_PLACEHOLDER_QUERY_TAG + " where `key`="+KKY;
    public static final String QUERY_GAOFA_SQL = "SELECT count(1) count from " + TBL_PLACEHOLDER_QUERY_GAOGA + " where docId=:pid and docType=0";
    public static final String QUERY_P2P_SQL = "SELECT count(1) count from " + TBL_PLACEHOLDER_QUERY_P2P + " where docId=:pid and subjectType=0 and mobile=:mobile";

//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//    @Autowired
//    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
////    @Autowired
//    private NezhaAccessor nezhaAccessor;

//
//    @Override
//    public  int getInfoBykey(DataInfo info, String dateType) {
//        try {
//            Map<String,String> map=new HashMap<>();
//            map.put("mobile",info.getMobile256());
//            map.put("pid",info.getPid256());
//            String sql=getSqlStr(info,dateType);
//            int count=0;
//            if(sql!=null && !"".equals(sql)){
//                List<Map<String, Object>> result=namedParameterJdbcTemplate.queryForList(sql,map);
//                result =result.stream().filter(e ->!e.get("count").toString().equals("0")).collect(Collectors.toList());
//                count=result.size();
//            }
//            if(dateType.equals("sq")){
//                return nezhaAccessor.getByTable(Enum.Table.BY_PHONE,info.getMobile256()).size()+nezhaAccessor.getByTable(Enum.Table.BY_PID,info.getPid256()).size();
//            }else if(dateType.equals("all")){
//                return nezhaAccessor.getByTable(Enum.Table.BY_PHONE,info.getMobile256()).size()+nezhaAccessor.getByTable(Enum.Table.BY_PID,info.getPid256()).size()+count;
//            }else{
//                return count;
//            }
//
//        }catch (Exception e){
//            logger.error("mysql select err occured: "+e);
//            logger.error(e);
//        }
//        return 0;
//    }

//    public String getSqlStr(DataInfo info, String dateType){
//        StringBuffer sql=new StringBuffer();
//        String overSqlMobile=null;
//        String overSqlPid=null;
//        String overSql=null;
//        if(info.getMobile256()!=null && !"".equals(info.getMobile256())){
//            String tblNoMoble="tag_mobile_"+Integer.parseInt(info.getMobile256().substring(6,8),16)%256+" ";
//            overSqlMobile=QUERY_TAG_SQL.replace(TBL_PLACEHOLDER_QUERY_TAG,tblNoMoble).replace(KKY,":mobile");
//        }
//        if(info.getPid256()!=null && !"".equals(info.getPid256())){
//            String tblNoPid="tag_pid_"+Integer.parseInt(info.getPid256().substring(6,8),16)%256+" ";
//            overSqlPid=QUERY_TAG_SQL.replace(TBL_PLACEHOLDER_QUERY_TAG,tblNoPid).replace(KKY,":pid");
//            String p2pSql=QUERY_P2P_SQL.replace(TBL_PLACEHOLDER_QUERY_P2P," tb_p2p_blacklist ");
//        }
//
//        String gaofaSql=QUERY_GAOFA_SQL.replace(TBL_PLACEHOLDER_QUERY_GAOGA," tb_high_court_lose_credit ");
//        String p2pSql=QUERY_P2P_SQL.replace(TBL_PLACEHOLDER_QUERY_P2P," tb_p2p_blacklist ");
//
//        if(overSqlMobile!=null || overSqlPid!=null){
//            if(overSqlMobile==null)
//                overSql=overSqlPid;
//            if (overSqlPid==null)
//                overSql=overSqlMobile;
//            if(overSqlMobile!=null && overSqlPid!=null)
//                overSql=overSqlMobile+" UNION ALL "+overSqlPid;
//        }
//        if(dateType.equals("all")){
//            sql=sql.append(gaofaSql).append(" UNION ALL ").append(p2pSql);
//            if(overSql!=null){
//                sql.append(" UNION ALL ").append(overSql);
//            }
//        }
//        if (dateType.equals("overdue")){
//            sql=sql.append(overSql);
//        }
//        if(dateType.equals("gaofa")){
//            sql=sql.append(gaofaSql);
//        }
//        if (dateType.equals("p2p")){
//            sql=sql.append(p2pSql);
//        }
//        return sql.toString();
//    }


//    public String getSh256FromMd5(KeyType type, String md5Value) {
//        String keyType = type == KeyType.pid ? "pid" : "mobile";
//        try {
//            // 只取16位，数据库中
//            md5Value = md5Value.length() == 32 ? md5Value.substring(8, 24) : md5Value;
//
//            String table = getBridgeTableName(md5Value, "md5_" + keyType);
//            String sql = "SELECT sha256 FROM " + table + " as t where t.md5=?";
//            return jdbcTemplate.queryForObject(sql, new Object[]{md5Value}, String.class);
//        } catch (Exception e) {
//            logger.warn("getSha256 failed:{}", e.getMessage());
//            return null;
//        }
//    }
//
//    public String getSh256FromSm3(KeyType type, String md5Value) {
//        String keyType = type == KeyType.pid ? "pid" : "mobile";
//        try {
//            String table = getBridgeTableName(md5Value, "sm3_" + keyType);
//            String sql = "SELECT sha256 FROM " + table + " as t where t.sm3=?";
//            return jdbcTemplate.queryForObject(sql, new Object[]{md5Value}, String.class);
//        } catch (Exception e) {
//            logger.warn("getSha256 failed:{}", e.getMessage());
//            return null;
//        }
//    }
//
//    private static String getBridgeTableName(String keyValue, String tablePrefix) {
//        StringBuilder result = new StringBuilder();
//        int mod = Integer.parseInt(keyValue.substring(6, 8), 16) % 64;
//        result.append(tablePrefix).append("_").append(mod);
//        return result.toString();
//    }
//

//    @Override
//    public void get(){
//        try {
//            Object object = jdbcTemplate.queryForObject("select sha256 mobile from md5_mobile_1 ", String.class);
//            System.out.println();
//        }catch (Exception e){
//            System.out.println(e);
//           }
//    }
//
//
//
//    @Override
//    public void getSha256Info(DataInfo dataInfo,int encryptType){
//        if(encryptType==3){
//            String mobile=getSh256FromMd5(KeyType.mobile,dataInfo.getMoble());
//            String pid=getSh256FromMd5(KeyType.pid,dataInfo.getPid());
//            dataInfo.setMobile256(mobile==null?"":mobile);
//            dataInfo.setPid256(pid==null?"":mobile);
//        }
//        if(encryptType==4){
//            String mobile=getSh256FromSm3(KeyType.mobile,dataInfo.getMoble());
//            String pid=getSh256FromSm3(KeyType.pid,dataInfo.getPid());
//            dataInfo.setMobile256(mobile==null?"":mobile);
//            dataInfo.setPid256(pid==null?"":mobile);
//        }
//    }

}
