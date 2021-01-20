package com.ppio.bd.ksdata;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.File;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class OssTest02 {

    @Test
    public void t2() {

        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
        String accessKeyId = "LTAI4GFy8z6y9KYpuGUpJQRU";
        String accessKeySecret = "aCSMTvu2vJP2rfSFWYSxxgJUzevAr1";
        String bucketName = "ks-error-data";
// <yourObjectName>上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
        String objectName = "20210102/500err.txt";

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 上传文件到指定的存储空间（bucketName）并将其保存为指定的文件名称（objectName）。
//        String content = "Hello OSS";
//        PutObjectResult result = ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(content.getBytes()));

        File file = new File("./ksData/500err.txt");
        System.out.println(file.getAbsolutePath());
        PutObjectResult result = ossClient.putObject(bucketName, objectName, file);

        System.out.println(result);

// 关闭OSSClient。
        ossClient.shutdown();

    }
}
