package com.memoforward.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface IOssService {
    String uploadFileAvatar(MultipartFile file);
}
