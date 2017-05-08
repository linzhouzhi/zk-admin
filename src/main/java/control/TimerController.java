package control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lzz on 17/5/8.
 */
@Controller
public class TimerController {
    @RequestMapping("/tree/timer")
    public String timer(Model model) {
        return "timer";
    }
}
