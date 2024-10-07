package ggs.srr.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @GetMapping("/admin/init_data")
    public String initData() {
        return "init user data";
    }
}
