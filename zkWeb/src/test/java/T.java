import java.io.IOException;
import java.util.Properties;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

import com.yasenagat.zkweb.util.ZkManagerImpl;



public class T {

	@Test
	public void t(){
		
		try {
			Properties p = new Properties();
			p.setProperty("host", "192.168.20.111:2181");
			p.setProperty("sessionTimeOut", "3000");
			ZkManagerImpl zk = ZkManagerImpl.createZk();
			zk.connect(p);
			System.out.println(zk.getChildren(null));;
//			System.out.println(zk.getNodeMeta("/root"));;
			System.out.println(zk.getACLs("/aaa"));
			// List<String> list = zk.getChildren(null);
			// System.out.println(list);
			// for(String p1 : list){
			// // System.out.println(zk.getChildren("/"+p1));
			// System.out.println("/"+p1+" data : "+zk.getData("/"+p1));;
			// }
			// zk.disconnect();
			// Map<String, String> map = zk.getNodeMeta("/");
			//
			// for (String key : map.keySet()) {
			// System.out.println(key + " : " + map.get(key));
			// }
			//List<Map<String, String>> l = zk.getACLs("/");
			//for (Map<String, String> m : l) {
//				System.out.println(m);
			//}

			try {
//				zk.setData("/root", new String("哈哈123".getBytes("utf-8"),"utf-8"));
//				String d = zk.getData("/root");
//				System.out.println(d);
				
//				zk.createNode("/3f", "4f","4f");
//				zk.deleteNode("/ffff");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
