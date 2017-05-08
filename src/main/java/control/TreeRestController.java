package control;

import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import util.ZKnodeUtil;

/**
 * Created by lzz on 17/5/8.
 */
@RestController
public class TreeRestController {
    @RequestMapping(value="/tree/get_all_path_ajax", method = RequestMethod.GET)
    public JSONObject getAllPath(){
        JSONObject jsonObject = ZKnodeUtil.getAllPath();
        return jsonObject;
    }

    @RequestMapping(value="/tree/get_path_detail_ajax", method = RequestMethod.GET)
    public JSONObject getPathDetail(@RequestParam(value="path", defaultValue="/") String path){
        JSONObject jsonObject = ZKnodeUtil.getDetailPath( path );
        return jsonObject;
    }
}
