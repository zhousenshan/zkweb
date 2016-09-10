package com.yasenagat.zkweb.util;

import java.util.List;
import java.util.Map;

public interface ZkCfgManager {

	public boolean init();
	public boolean add(String des,String connectStr,String sessionTimeOut);
	public boolean add(String id,String des,String connectStr,String sessionTimeOut);
	public List<Map<String, Object>> query();
	public List<Map<String, Object>> query(int page,int rows);
	public boolean update(String id,String des,String connectStr,String sessionTimeOut);
	public boolean delete(String id);
	public Map<String, Object> findById(String id);
	public int count();
	
	// 建表SQL
	static String initSql = "CREATE TABLE IF NOT EXISTS ZK(ID VARCHAR PRIMARY KEY, DES VARCHAR, CONNECTSTR VARCHAR, SESSIONTIMEOUT VARCHAR)";
	
}
