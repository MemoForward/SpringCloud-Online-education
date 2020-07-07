package com.memoforward.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IVodService {
    String uploadVideoAliyun(MultipartFile file);

    void removeAliyunVideos(List<String> videoIds);
}
