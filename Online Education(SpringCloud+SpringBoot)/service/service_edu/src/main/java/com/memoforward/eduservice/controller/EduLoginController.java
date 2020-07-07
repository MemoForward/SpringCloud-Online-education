package com.memoforward.eduservice.controller;

import com.memoforward.commonutils.R;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

@Api(description = "后台登录接口")
@RestController
@RequestMapping("/eduservice/user")
@CrossOrigin
public class EduLoginController {

    @PostMapping("/login")
    public R login(){
//        System.out.println("loginnnnnnn....");;
        return R.ok().data("token", "admin");
    }

    @GetMapping("/info")
    public R info(){
        return R.ok().data("roles","[admin]").data("name","admin").data("avatar","https://online-teach-file.oss-cn-beijing.aliyuncs.com/teacher/2019/10/30/65423f14-49a9-4092-baf5-6d0ef9686a85.png");
    }
}
