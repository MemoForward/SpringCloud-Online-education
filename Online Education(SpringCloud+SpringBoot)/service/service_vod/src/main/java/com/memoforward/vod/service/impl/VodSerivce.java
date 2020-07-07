package com.memoforward.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.memoforward.commonutils.R;
import com.memoforward.servicebase.exceptionhandler.MyException;
import com.memoforward.vod.service.IVodService;
import com.memoforward.vod.utils.ConstantVodUtils;
import com.memoforward.vod.utils.InitVodClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@Service
public class VodSerivce implements IVodService {

    @Override
    public String uploadVideoAliyun(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String title =fileName.substring(0, fileName.lastIndexOf("."));
            InputStream inputStream = file.getInputStream();
            System.out.println("title"+title);
            System.out.println("fileName"+fileName);
            UploadStreamRequest request = new UploadStreamRequest(ConstantVodUtils.ACCESS_KEY_ID,
                    ConstantVodUtils.ACCESS_KEY_SECRET,
                    title, fileName, inputStream);
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            String videoId = null;
            System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
            if (response.isSuccess()) {
//                System.out.print("VideoId=" + response.getVideoId() + "\n");
                videoId = response.getVideoId();
                return videoId;
            } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
                System.out.print("VideoId=" + response.getVideoId() + "\n");
                System.out.print("ErrorCode=" + response.getCode() + "\n");
                System.out.print("ErrorMessage=" + response.getMessage() + "\n");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void removeAliyunVideos(List<String> videoIds) {
        try{
            // 初始化对象
            DefaultAcsClient client = InitVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID,
                    ConstantVodUtils.ACCESS_KEY_SECRET);
            DeleteVideoRequest request  = new DeleteVideoRequest();
            String aliyunVideoIds = StringUtils.join(videoIds.toArray(), ",");
            request.setVideoIds(aliyunVideoIds);
            client.getAcsResponse(request);
        }catch (Exception e){
            e.printStackTrace();
            throw new MyException(20001, "删除视频失败！");
        }
    }
}
