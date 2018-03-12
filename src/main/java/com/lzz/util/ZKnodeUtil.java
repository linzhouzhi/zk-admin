package com.lzz.util;

import com.lzz.model.ZKnode;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
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
            JSONObject statObject = JSONObject.fromObject(stat);
            byte[] datas = client.getData().forPath( path );
            jsonObject.put("data", new String(datas));
            jsonObject.put("stat", statObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static Stat updateAllPathData(String path, String data) throws Exception {
        if( "/".equals(path) ){
            return null;
        }
        String parent_path = getParentPath( path );
        CuratorFramework client = getClient();
        List<String> paths =  client.getChildren().forPath( parent_path );
        Stat stat = null;
        // 只有一个节点
        if( paths.size() == 0 ){
            stat = updatePathData( path, data);
            return stat;
        }
        for( int i = 0; i < paths.size(); i++ ){
            String currentPath = parent_path + "/" + paths.get(i);
            stat = updatePathData( currentPath, data);
        }
        return stat;
    }

    public static Stat updatePathData(String path, String data) throws Exception {
        CuratorFramework client = getClient();
        client.setData().forPath(path, data.getBytes());
        Stat stat = client.checkExists().forPath( path );
        return stat;
    }

    public static String getParentPath( String path ){
        if( "/".equals( path ) ){
            return path;
        }
        String parent_path = StringUtils.substringBeforeLast( path, "/" );
        return parent_path;
    }

    public static JSONObject getAllPath(String path){
        CuratorFramework client = getClient();
        ZKnode zKnode = new ZKnode();
        zKnode.setId( path );
        zKnode.setText( path );
        zKnode.setType( "parent" );
        try {
            getPath( client, zKnode, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = JSONObject.fromObject(zKnode);
        return jsonObject;
    }

    private static void getPath(CuratorFramework client, ZKnode zKnode, Boolean forceAll) throws Exception {
        String path = zKnode.getId();
        List<String> childrenNames = client.getChildren().forPath( path );
        for(int i = 0; i < childrenNames.size(); i++){
            String childName = childrenNames.get(i);
            ZKnode childNode = new ZKnode();
            zKnode.setType("parent");
            childNode.setText( childName );
            String childPath = "";
            if( "/".equals( path ) ){
                childPath = path + childName;
            }else{
                childPath = path + "/" + childName;
            }
            childNode.setId( childPath );
            List<ZKnode> zkChildren = zKnode.getChildren();
            if( forceAll ){
                childNode.setText( childName );
                childNode.setType("file");
                zkChildren.add( childNode );
                getPath( client, childNode, true);
            }else{
                Stat stat = client.checkExists().forPath( childPath );
                if( stat.getNumChildren() > 0 ){
                    childNode.setType("parent");
                    List<ZKnode> grandson = new ArrayList<>();
                    grandson.add( new ZKnode(childPath + "/...", "...", "ellipsis"));
                    childNode.setChildren( grandson );
                    zkChildren.add( childNode );
                }else{
                    if( stat.getEphemeralOwner() != 0 ){
                        childNode.setType("tmp-leaf");
                    }else{
                        childNode.setType("leaf");
                    }
                    zkChildren.add( childNode );
                }
                if( i > 50 ){
                    zkChildren.add( new ZKnode(path+ "/...", "...", "ellipsis") );
                    break;
                }
            }
        }
    }


    private static void getAllPath(CuratorFramework client, ZKnode zKnode) throws Exception {
        String path = zKnode.getId();
        List<String> paths =  client.getChildren().forPath( path );
        for( int i = 0; i < paths.size(); i++ ){
            List<ZKnode> zKnodeChildren = zKnode.getChildren();
            zKnode.setType("root");

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
            getAllPath( client, temZknode);
        }
    }

}
