package util;

import model.ZKnode;
import net.sf.json.JSONObject;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * Created by lzz on 17/5/8.
 */
public class ZKnodeUtil {
    private static final String ZK_ADDRESS = "localhost:2181";


    public static CuratorFramework getClient(){
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                ZK_ADDRESS,
                new RetryNTimes(10, 5000)
        );
        client.start();
        return client;
    }

    public static JSONObject getDetailPath(String path){
        CuratorFramework client = getClient();
        Stat stat = null;
        JSONObject jsonObject = new JSONObject();
        try {
            stat = client.checkExists().forPath( path );
            jsonObject = JSONObject.fromObject(stat);
            byte[] datas = client.getData().forPath( path );
            jsonObject.put("data", new String(datas));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject getAllPath(){
        CuratorFramework client = getClient();
        ZKnode zKnode = new ZKnode();
        String path = "/";
        zKnode.setId( path );
        zKnode.setText( path );
        zKnode.setType( "root" );
        try {
            getPath( client, zKnode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = JSONObject.fromObject(zKnode);
        return jsonObject;
    }

    private static void getPath(CuratorFramework client, ZKnode zKnode) throws Exception {
        String path = zKnode.getId();
        List<String> paths =  client.getChildren().forPath( path );
        for( int i = 0; i < paths.size(); i++ ){
            List<ZKnode> zKnodeChildren = zKnode.getChildren();
            if( zKnodeChildren.size() > 0 ){
                zKnode.setType("root");
            }
            ZKnode temZknode = new ZKnode();
            String childrenPath;
            String pathName = paths.get(i);
            if( "/".equals( path ) ){
                childrenPath = path + pathName;
            }else{
                childrenPath = path + "/" + pathName;
            }
            temZknode.setId( childrenPath );
            temZknode.setText( pathName );
            temZknode.setType("file");
            zKnodeChildren.add( temZknode );
            zKnode.setChildren( zKnodeChildren );
            getPath( client, temZknode);
        }
    }

}
