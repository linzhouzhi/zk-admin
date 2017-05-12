package control;

import net.sf.json.JSONObject;
import org.apache.zookeeper.data.Stat;
import org.springframework.web.bind.annotation.*;
import util.ZKnodeUtil;

/**
 * Created by lzz on 17/5/8.
 */
@RestController
public class TreeRestController {
    @RequestMapping(value="/tree/get_all_path_ajax", method = RequestMethod.GET)
    public JSONObject getAllPath(@RequestParam(value="path", defaultValue="/") String path){
        if( !path.startsWith("/") ){
            return new JSONObject();
        }
        JSONObject jsonObject = ZKnodeUtil.getAllPath( path );
        System.out.println( jsonObject );
        return jsonObject;
    }

    @RequestMapping(value="/tree/get_path_detail_ajax", method = RequestMethod.GET)
    public JSONObject getPathDetail(@RequestParam(value="path", defaultValue="/") String path){
        if( !path.startsWith("/") ){
            return new JSONObject();
        }
        JSONObject jsonObject = ZKnodeUtil.getDetailPath( path );
        return jsonObject;
    }

    @RequestMapping(value="/tree/update_path_data_ajax", method = RequestMethod.POST)
    public JSONObject updatePathData(@RequestBody JSONObject requestBody){
        String path = requestBody.getString("path");
        if( !path.startsWith("/") ){
            return new JSONObject();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            Stat stat = ZKnodeUtil.updatePathData(path, requestBody.getString("nodeData"));
            JSONObject statObject = JSONObject.fromObject(stat);
            jsonObject.put("stat", statObject);
            jsonObject.put("data", requestBody.getString("nodeData"));
        } catch (Exception e) {
            e.printStackTrace();
            return jsonObject;
        }
        return jsonObject;
    }

    @RequestMapping(value="/tree/update_all_path_data_ajax", method = RequestMethod.POST)
    public JSONObject updateAllPathData(@RequestBody JSONObject requestBody){
        String path = requestBody.getString("path");
        if( !path.startsWith("/") ){
            return new JSONObject();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            Stat stat = ZKnodeUtil.updateAllPathData(path, requestBody.getString("nodeData"));
            JSONObject statObject = JSONObject.fromObject(stat);
            jsonObject.put("stat", statObject);
            jsonObject.put("data", requestBody.getString("nodeData"));
        } catch (Exception e) {
            e.printStackTrace();
            return jsonObject;
        }
        return jsonObject;
    }
}
