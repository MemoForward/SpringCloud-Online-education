package com.memoforward.eduservice.controller;


import com.memoforward.commonutils.R;
import com.memoforward.eduservice.client.VodClient;
import com.memoforward.eduservice.model.EduVideo;
import com.memoforward.eduservice.service.IEduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author memoforward
 * @since 2020-07-04
 */
@Api(description = "课程章节下的小节操作")
@RestController
@CrossOrigin
@RequestMapping("/eduservice/video")
public class EduVideoController {

    @Autowired
    private IEduVideoService videoService;

    @Autowired
    private VodClient vodClient;

    @ApiOperation("添加小节")
    @PostMapping("/addVideo")
    public R addVideo(@RequestBody EduVideo eduVideo){
        boolean save = videoService.save(eduVideo);
        return save ? R.ok() : R.error();
    }

    @ApiOperation(("删除小节"))
    @DeleteMapping("/{videoId}/deleteVideo")
    // 这个方法需要修改的
    public R deleteVideoById(@PathVariable String videoId){
        // 根据小节的id，获得视频的id
        EduVideo eduVideo = videoService.getById(videoId);
        String videoSourceId = eduVideo.getVideoSourceId();
        if(!StringUtils.isEmpty(videoSourceId)) vodClient.removeAliyunVideo(videoSourceId);
        videoService.removeById(videoId);
        return R.ok();
    }

    @ApiOperation("修改小节")
    @PostMapping("/updateVideo")
    public R updateVideo(@RequestBody EduVideo video){
        boolean update = videoService.updateById(video);
        return update ? R.ok() : R.error();
    }

    @ApiOperation("查找小节")
    @GetMapping("/{videoId}/getVideoInfo")
    public R getVideoInfo(@PathVariable String videoId){
        EduVideo video = videoService.getById(videoId);
        return R.ok().data("video", video);
    }
}
