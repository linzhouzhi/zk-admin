package com.lzz.control;

import net.sf.json.JSONObject;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.lzz.util.ZKnodeUtil;

/**
 * Created by lzz on 17/5/7.
 */
@Controller
public class IndexController {
    @RequestMapping("/index")
    public String treeAdmin(Model model) {
        return "index";
    }


    @RequestMapping(value="/get_all_path_ajax", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getAllPath(@RequestParam(value="path", defaultValue="/") String path){
        if( !path.startsWith("/") ){
            return new JSONObject();
        }
        JSONObject jsonObject = ZKnodeUtil.getAllPath( path );
        System.out.println( jsonObject );
        return jsonObject;
    }

    @RequestMapping(value="/get_path_detail_ajax", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getPathDetail(@RequestParam(value="path", defaultValue="/") String path){
        if( !path.startsWith("/") ){
            return new JSONObject();
        }
        JSONObject jsonObject = ZKnodeUtil.getDetailPath( path );
        return jsonObject;
    }

    @RequestMapping(value="/update_path_data_ajax", method = RequestMethod.POST)
    @ResponseBody
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

    @RequestMapping(value="/update_all_path_data_ajax", method = RequestMethod.POST)
    @ResponseBody
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
