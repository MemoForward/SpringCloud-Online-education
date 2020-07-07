package com.memoforward.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.memoforward.commonutils.R;
import com.memoforward.servicebase.exceptionhandler.MyException;
import com.memoforward.vod.service.IVodService;
import com.memoforward.vod.utils.ConstantVodUtils;
import com.memoforward.vod.utils.InitVodClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Path;
import java.util.List;

@Api(description = "小节视频模块接口")
@RestController
@CrossOrigin
@RequestMapping("/eduvod/video")
public class VodController {

    @Autowired
    IVodService vodService;

    @ApiOperation("上传视频到阿里云")
    @PostMapping("/uploadAliyunVideo")
    public R uploadAliyunVideo(@RequestBody  MultipartFile file){
        String videoId = vodService.uploadVideoAliyun(file);
        return R.ok().data("videoId", videoId);
    }

    @ApiOperation("根据视频id删除视频")
    @DeleteMapping("/{videoId}/removeAliyunVideo")
    public R removeAliyunVideo(@PathVariable String videoId){
        try{
            // 初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID,
                                                                  ConstantVodUtils.ACCESS_KEY_SECRET);
            DeleteVideoRequest request  = new DeleteVideoRequest();
            request.setVideoIds(videoId);
            client.getAcsResponse(request);
        }catch (Exception e){
            e.printStackTrace();
            throw new MyException(20001, "删除视频失败！");
        }
        return R.ok();
    }

    @ApiOperation("删除多个阿里云的视频")
    @DeleteMapping("/removeAliyunVideos")
    public R removeAliyunVideos(@RequestParam("videoIds") List<String> videoIds){
        vodService.removeAliyunVideos(videoIds);
        return R.ok();
    }
}
