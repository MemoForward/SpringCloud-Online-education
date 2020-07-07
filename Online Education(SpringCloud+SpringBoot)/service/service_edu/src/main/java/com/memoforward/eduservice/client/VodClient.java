package com.memoforward.eduservice.client;

import com.google.gson.internal.$Gson$Preconditions;
import com.memoforward.commonutils.R;
import com.memoforward.servicebase.exceptionhandler.MyException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("service-vod")
@Component
public interface VodClient {

    @DeleteMapping("/eduvod/video/{videoId}/removeAliyunVideo")
    R removeAliyunVideo(@PathVariable("videoId") String videoId);

    @DeleteMapping("/eduvod/video/removeAliyunVideos")
    R removeAliyunVideos(@RequestParam("videoIds") List<String> videoIds);
}
