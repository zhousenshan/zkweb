package com.yasenagat.zkweb.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.dbutils.QueryRunner;
import org.h2.jdbcx.JdbcConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yasenagat.zkweb.web.ZkController;

public class ZkCfgManagerImpl implements ZkCfgManager {

	private static Logger log = LoggerFactory.getLogger(ZkCfgManagerImpl.class);
//	jdbc:h2:tcp://localhost/~/test
//		jdbc:h2:~/zkcfg
	private static JdbcConnectionPool cp = JdbcConnectionPool.create("jdbc:h2:tcp://localhost/~/zkcfg","sa","sa"); 
	private static Connection conn = null;
	static QueryRunner run = new QueryRunner(H2Util.getDataSource());
	
	public ZkCfgManagerImpl() {
		cp.setMaxConnections(20);
		cp.setLoginTimeout(1000 * 50);
	};
	private Connection getConnection() throws SQLException{
		if(null == conn){
			conn = cp.getConnection();
		}
		return conn;
	}
	
	private void closeConn(){
		if(null != conn){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean init() {
		PreparedStatement ps = null;
		try {
			ps = getConnection().prepareStatement(ZkCfgManager.initSql);
			return ps.executeUpdate()>0;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("init zkCfg error : {}" , e.getMessage());
		} finally {
			if(null != ps){
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public boolean add(String des, String connectStr, String sessionTimeOut) {
		PreparedStatement ps = null;
		try {
			ps = getConnection().prepareStatement("INSERT INTO ZK VALUES(?,?,?,?)");
			ps.setString(1, UUID.randomUUID().toString().replaceAll("-", ""));
			ps.setString(2, des);
			ps.setString(3, connectStr);
			ps.setString(4, sessionTimeOut);
			return ps.executeUpdate()>0;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("add zkCfg error : {}",e.getMessage());
		} finally {
			if(null != ps){
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public List<Map<String, Object>> query() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = getConnection().prepareStatement("SELECT * FROM ZK");
			rs = ps.executeQuery();
			
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			
			ResultSetMetaData meta = rs.getMetaData();
			Map<String, Object> map = null;
			int cols = meta.getColumnCount();
			while(rs.next()){
				map = new HashMap<String, Object>();
				for(int i = 0 ; i < cols ;i++){
					map.put(meta.getColumnName(i+1), rs.getObject(i+1));
				}
				list.add(map);
			}
			
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(null != rs){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(null != ps){
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		return new ArrayList<Map<String,Object>>();
	}

	public boolean update(String id, String des, String connectStr,
			String sessionTimeOut) {
		PreparedStatement ps = null;
		try {
			ps = getConnection().prepareStatement("UPDATE ZK SET DES=?,CONNECTSTR=?,SESSIONTIMEOUT=? WHERE ID=?;");
			ps.setString(1, des);
			ps.setString(2, connectStr);
			ps.setString(3, sessionTimeOut);
			ps.setString(4, id);
			return ps.executeUpdate()>0;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("update id={} zkCfg error : {}",new Object[]{id,e.getMessage()});
		} finally {
			if(null != ps){
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return false;
	}

	public boolean delete(String id) {
		
		PreparedStatement ps = null;
		try {
			ps = getConnection().prepareStatement("DELETE ZK WHERE ID=?");
			ps.setString(1, id);
			return ps.executeUpdate()>0;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("delete id={} zkCfg error : {}",new Object[]{id,e.getMessage()});
		}  finally {
			if(null != ps){
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public Map<String, Object> findById(String id) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = getConnection().prepareStatement("SELECT * FROM ZK WHERE ID = ?");
			ps.setString(1, id);
			rs = ps.executeQuery();
			Map<String, Object> map = new HashMap<String, Object>();
			ResultSetMetaData meta = rs.getMetaData();
			int cols = meta.getColumnCount();
			if(rs.next()){
				for(int i = 0 ; i < cols ;i++){
					map.put(meta.getColumnName(i+1).toLowerCase(), rs.getObject(i+1));
				}
			}
			return map;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(null != rs){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(null != ps){
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public List<Map<String, Object>> query(int page, int rows) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = getConnection().prepareStatement("SELECT * FROM ZK limit ?,?");
			ps.setInt(1, (page-1) * rows);
			ps.setInt(2, rows);
			rs = ps.executeQuery();
			
			List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
			
//			ResultSetMetaData meta = rs.getMetaData();
			Map<String, Object> map = null;
//			int cols = meta.getColumnCount();
			while(rs.next()){
				map = new HashMap<String, Object>();
				for(int i = 0 ; i < rs.getMetaData().getColumnCount() ;i++){
					map.put(rs.getMetaData().getColumnName(i+1), rs.getObject(i+1));
				}
				list.add(map);
			}
			
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(null != rs){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(null != ps){
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return new ArrayList<Map<String,Object>>();
	}

	public boolean add(String id, String des, String connectStr,
			String sessionTimeOut) {
		PreparedStatement ps = null;
		try {
			ps = getConnection().prepareStatement("INSERT INTO ZK VALUES(?,?,?,?);");
			ps.setString(1, id);
			ps.setString(2, des);
			ps.setString(3, connectStr);
			ps.setString(4, sessionTimeOut);
			return ps.executeUpdate()>0;
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("add zkCfg error : {}",e.getMessage());
		} finally {
			if(null != ps){
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} 
		return false;
	}

	public int count() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			 ps = getConnection().prepareStatement("SELECT count(id) FROM ZK");
			 rs = ps.executeQuery();
			 if(rs.next()){
				 return rs.getInt(1);
			 }
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("count zkCfg error : {}",e.getMessage());
		} finally {
			if(null != rs){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(null != ps){
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return 0;
	}

}
