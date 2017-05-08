package control;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import util.ZKnodeUtil;

/**
 * Created by lzz on 17/5/8.
 */
@Controller
public class ConfigController {
    @RequestMapping("/tree/config")
    public String treeConfig(Model model) {
        String path = "/zktest/node1/nn2/user";
        JSONObject nodeDetail = ZKnodeUtil.getDetailPath(path);
        JSONObject data = (JSONObject) nodeDetail.get("data");
        model.addAttribute("congigDatas", data);
        return "tree_config";
    }
}
