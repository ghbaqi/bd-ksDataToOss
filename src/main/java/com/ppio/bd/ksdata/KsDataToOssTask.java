package com.ppio.bd.ksdata;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 下载 ks 的数据上传到 oss  上
 */

@Slf4j
@Service
public class KsDataToOssTask {

    @Autowired
    private OssConfiguration ossConfiguration;

    public static final String KS_DATA_URL = "http://103.107.219.15:4445/pcdn/dby71200235777/%s/%s.txt";


    @Autowired
    private OkHttpClient okHttpClient;

    @Autowired
    private ScheduledExecutorService threadPool;

    public static int KS_DATA_RETRY_TIME = 20;


    String bucketName = "ks-error-data";
    // <yourObjectName>上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。

    public void sync(String dateStr, String biz) {

        if (dateStr == null || dateStr.trim().equals("")) {
            LocalDate localDate = LocalDate.now().minusDays(1);
            dateStr = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
        biz = biz.trim();

        Request.Builder builder = new Request.Builder();

        String url = String.format(KS_DATA_URL, dateStr, biz);
        builder.url(url);
        Request request = builder.build();
        Response response = null;


        CountDownLatch latch;
        GetKsDataRunnable runnable;
        for (int i = 1; i <= KS_DATA_RETRY_TIME; i++) {
            latch = new CountDownLatch(1);
            runnable = new GetKsDataRunnable(i, latch, request);
            if (i == 1) {
                threadPool.schedule(runnable, 0L, TimeUnit.MINUTES);
            } else {
                request = builder.build();
                runnable.setRequest(request);
                threadPool.schedule(runnable, 10L, TimeUnit.MINUTES);
            }
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            response = runnable.getResponse();
            if (response == null || !response.isSuccessful() || response.code() != 200) {
                log.error("获取 ks {} 数据失败 i = {}", biz, i);
                if (i == KS_DATA_RETRY_TIME) {
                    log.error("最终获取 ks {} 数据失败", biz);
                    return;
                }
            }

            if (response != null && response.code() == 200) {
                log.info("获取 ks  {}  数据成功 , i = {} ", biz, i);
                break;
            }
        }


        log.info("往 oss 发送 {} 数据 , dt = {}", biz, dateStr);
        String objectName = dateStr + "/" + biz + ".txt";
        InputStream is = response.body().byteStream();
        OSS oss = ossConfiguration.getOssClient();
        PutObjectResult result = oss.putObject(bucketName, objectName, is);
        log.info(result.toString());
        oss.shutdown();
        response.close();
    }

    private void sync(String biz) {
        sync(null, biz);
    }


    @Scheduled(cron = "0 10 0 1/1 * ? ")
    public void _500err() {
        sync("500err");
    }

    @Scheduled(cron = "0 10 0 1/1 * ? ")
    public void _cpu() {
        sync("cpu");
    }


    @Scheduled(cron = "0 15 0 1/1 * ? ")
    public void io_wait() {
        sync("io_wait");
    }


    @Scheduled(cron = "0 15 0 1/1 * ? ")
    public void retran() {
        sync("retran");
    }

    @Scheduled(cron = "0 20 0 1/1 * ? ")
    public void speed() {
        sync("speed");
    }


    @Scheduled(cron = "0 20 0 1/1 * ? ")
    public void upload() {
        sync("upload");
    }

    @Scheduled(cron = "0 25 0 1/1 * ? ")
    public void hotPushFail() {
        sync("hotPushFail");
    }

    @Scheduled(cron = "0 25 0 1/1 * ? ")
    public void ifaceOffLine() {
        sync("ifaceOffLine");
    }

    @Scheduled(cron = "0 25 0 1/1 * ? ")
    public void reportOffLine() {
        sync("reportOffLine");
    }

    @Scheduled(cron = "0 30 0 1/1 * ? ")
    public void coredump() {
        sync("coredump");
    }

    @Scheduled(cron = "0 30 0 1/1 * ? ")
    public void cachePerG() {
        sync("cachePerG");
    }

    @Scheduled(cron = "0 30 0 1/1 * ? ")
    public void appGtNicBw() {
        sync("appGtNicBw");
    }

    @Scheduled(cron = "0 35 0 1/1 * ? ")
    public void tcpRetran() {
        sync("tcpRetran");
    }

    @Scheduled(cron = "0 35 0 1/1 * ? ")
    public void bwWave() {
        sync("bwWave");
    }


    private class GetKsDataRunnable implements Runnable {

        private int t;
        private CountDownLatch latch;
        private Request request;
        private Response response;

        public void setRequest(Request request) {
            this.request = request;
        }

        public GetKsDataRunnable(int t, CountDownLatch latch, Request request) {
            this.t = t;
            this.latch = latch;
            this.request = request;
        }

        public Response getResponse() {
            return response;
        }

        @Override
        public void run() {
            try {
                response = okHttpClient.newCall(request).execute();
            } catch (IOException e) {
                log.error("请求 ks 数据 错误 t = {} , e = {}", t, e);
            } finally {
                latch.countDown();
            }
        }
    }
}
