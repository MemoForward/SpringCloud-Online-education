package com.memoforward.vdTest;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.*;

import java.util.List;

public class TestVod {
    /*获取播放地址函数*/
    public static GetPlayInfoResponse getPlayInfo(DefaultAcsClient client) throws Exception {
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId("1c0dc9260c46490b98c5fe4177c5a24f");
        return client.getAcsResponse(request);
    }
    /*获取播放凭证函数*/
    public static GetVideoPlayAuthResponse getVideoPlayAuth(DefaultAcsClient client) throws Exception {
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId("1c0dc9260c46490b98c5fe4177c5a24f");
        return client.getAcsResponse(request);
    }

    /*以下为调用示例 -- 视频地址 不加密*/
    public static void main1(String[] argv) throws ClientException {
        DefaultAcsClient client = InitObject.initVodClient("LTAI4GC8oyrNqxqnMS4YnvxA", "Jn6Wgyb8NPMWPam6tvceibz0kG5vl1");
        GetPlayInfoResponse response = new GetPlayInfoResponse();
        try {
            response = getPlayInfo(client);
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();
            //播放地址
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
                System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
            }
            //Base信息
            System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }

    /*以下为调用示例 -- 获取视频凭证 加不加密都可以*/
    public static void main2(String[] argv) throws ClientException {
        DefaultAcsClient client = InitObject.initVodClient("LTAI4GC8oyrNqxqnMS4YnvxA", "Jn6Wgyb8NPMWPam6tvceibz0kG5vl1");
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        try {
            response = getVideoPlayAuth(client);
            //播放凭证
            System.out.print("PlayAuth = " + response.getPlayAuth() + "\n");
            //VideoMeta信息
            System.out.print("VideoMeta.Title = " + response.getVideoMeta().getTitle() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }
}
