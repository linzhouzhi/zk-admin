package control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lzz on 17/5/7.
 */
@Controller
public class TreeController {
    @RequestMapping("/tree/admin")
    public String greeting(Model model) {
        return "tree_admin";
    }
}
