package com.memoforward.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.memoforward.oss.service.IOssService;
import com.memoforward.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Repository
public class OssService implements IOssService {

    // 上传文件到OSS
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ConstantPropertiesUtils.END_POINT;
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        try {
            // <yourObjectName>上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
            // 获取文件名
            String filename = file.getOriginalFilename();
            // 防止同名文件覆盖？--》
            // 1. 使用随机数添加到文件前
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            filename = uuid + filename;
            // 2. 把文件按照日期来分类
            // 获取当前年月日
            String datePath = new DateTime().toString("yyyy/MM/dd");
            String objectName = "avatar/" + datePath + "/" + filename;

            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 上传文件到指定的存储空间（bucketName）并将其保存为指定的文件名称（objectName）。
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(file.getBytes()));

            // 关闭OSSClient。
            ossClient.shutdown();

            // https://memo-edu.oss-cn-beijing.aliyuncs.com/avatar/myblog.png
            String url = "https://"+bucketName+"."+endpoint+"/"+objectName;
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
