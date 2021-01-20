package com.ppio.bd.ksdata;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class OssConfiguration {


    // Endpoint以杭州为例，其它Region请按实际情况填写。
    private String endpoint = "https://oss-cn-hangzhou.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
    private String accessKeyId = "LTAI4GFy8z6y9KYpuGUpJQRU";
    private String accessKeySecret = "aCSMTvu2vJP2rfSFWYSxxgJUzevAr1";
    String bucketName = "ks-error-data";
    // <yourObjectName>上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。

    public OSS getOssClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
}
