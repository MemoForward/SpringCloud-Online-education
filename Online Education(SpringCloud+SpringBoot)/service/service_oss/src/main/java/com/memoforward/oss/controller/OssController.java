package com.memoforward.oss.controller;

import com.memoforward.commonutils.R;
import com.memoforward.oss.service.IOssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(description = "阿里云Oss文件上传系统")
@RestController
@CrossOrigin
@RequestMapping("/eduoss/fileoss")
public class OssController {

    @Autowired
    private IOssService ossService;

    // 上传头像方法
    @ApiOperation(value = "图片上传")
    @PostMapping
    public R uploadOssFile(MultipartFile file){
        // 返回上传到的OSS的路径
        String url = ossService.uploadFileAvatar(file);
        return R.ok().data("url", url);
    }

}
