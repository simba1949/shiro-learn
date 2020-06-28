package top.simba1949.controller.core;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author SIMBA1949
 * @date 2020/6/27 22:58
 */
@Controller
@RequestMapping("permission")
public class PageController {

    @GetMapping("{num}")
    public String toPage(@PathVariable("num")int num){
        System.out.println("num is " + num);
        return "redirect:/permission/" + num + ".jsp";
    }
}
