package com.memoforward.vdTest;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.memoforward.vod.VodApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = VodApplication.class)
public class ServiceVodTest {

    // 测试获取视频地址 -- 不加密
    @Test
    public void testGetPlayInfo() throws ClientException {
        DefaultAcsClient client = InitObject.initVodClient("LTAI4GC8oyrNqxqnMS4YnvxA", "Jn6Wgyb8NPMWPam6tvceibz0kG5vl1");
        GetPlayInfoResponse response = new GetPlayInfoResponse();
        try {
            response = TestVod.getPlayInfo(client);
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
}
