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

    //public static final String KS_DATA_URL = "http://103.107.219.15:4445/pcdn/dby71200235777/%s/%s.txt";
    //http://103.107.219.15:4445/pcdn/ks2021/20210904/noon/err500.txt   午高峰数据
    //http://103.107.219.15:4445/pcdn/ks2021/20210904/night/err500.txt  晚高峰数据
    public static final String KS_DATA_URL = "http://103.107.219.15:4445/pcdn/dby71200235777/%s/%s/%s.txt";


    @Autowired
    private OkHttpClient okHttpClient;

    @Autowired
    private ScheduledExecutorService threadPool;

    public static int KS_DATA_RETRY_TIME = 20;


    String bucketName = "ks-error-data";
    // <yourObjectName>上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。

    public void sync(String dateStr, String noonNight, String biz) {

        if (dateStr == null || dateStr.trim().equals("")) {
            LocalDate localDate = LocalDate.now().minusDays(0);
            dateStr = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        }
        if (noonNight == null || noonNight.trim().equals("")) {
            noonNight = "noon";
        }
        biz = biz.trim();

        Request.Builder builder = new Request.Builder();

        String url = String.format(KS_DATA_URL, dateStr, noonNight, biz);
        System.out.println(url);
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
                threadPool.schedule(runnable, i - 1, TimeUnit.MINUTES);
//                threadPool.schedule(runnable, 1, TimeUnit.MINUTES);
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


        log.info("往 oss 发送 {} 数据 ,午晚高峰{}, dt = {}", biz, noonNight, dateStr);
        String objectName = dateStr + "/" + noonNight + "/" + biz + ".txt";
        InputStream is = response.body().byteStream();
        OSS oss = ossConfiguration.getOssClient();
        PutObjectResult result = oss.putObject(bucketName, objectName, is);
        log.info(result.toString());
        oss.shutdown();
        response.close();
    }


    @Scheduled(cron = "0 30 14 * * ?")
    public void noon_ifaceOffLine() {
        sync(null, "noon", "ifaceOffLine");
    }

    ;

    @Scheduled(cron = "0 30 14 * * ?")
    public void noon_reportOffLine() {
        sync(null, "noon", "reportOffLine");
    }

    ;

    @Scheduled(cron = "0 30 14 * * ?")
    public void noon_coredump() {
        sync(null, "noon", "coredump");
    }

    ;

    @Scheduled(cron = "0 30 14 * * ?")
    public void noon_cachePerG() {
        sync(null, "noon", "cachePerG");
    }

    ;

    @Scheduled(cron = "0 30 14 * * ?")
    public void noon_appGtNicBw() {
        sync(null, "noon", "appGtNicBw");
    }

    ;

    @Scheduled(cron = "0 30 14 * * ?")
    public void noon_tcpRetran() {
        sync(null, "noon", "tcpRetran");
    }

    ;

    @Scheduled(cron = "0 30 14 * * ?")
    public void noon_bwWave() {
        sync(null, "noon", "bwWave");
    }

    ;

    @Scheduled(cron = "0 30 14 * * ?")
    public void noon_ioWait() {
        sync(null, "noon", "ioWait");
    }

    ;

    @Scheduled(cron = "0 30 14 * * ?")
    public void noon_hotPushFailed() {
        sync(null, "noon", "hotPushFailed");
    }

    ;

    @Scheduled(cron = "0 30 14 * * ?")
    public void noon_ispIdentifyError() {
        sync(null, "noon", "ispIdentifyError");
    }

    ;

    @Scheduled(cron = "0 30 14 * * ?")
    public void noon_natIdentifyError() {
        sync(null, "noon", "natIdentifyError");
    }

    ;

    @Scheduled(cron = "0 30 14 * * ?")
    public void noon_lineSpeedError() {
        sync(null, "noon", "lineSpeedError");
    }

    ;

    @Scheduled(cron = "0 30 14 * * ?")
    public void noon_err500() {
        sync(null, "noon", "err500");
    }

    ;

    @Scheduled(cron = "0 30 14 * * ?")
    public void noon_cpuTooHigh() {
        sync(null, "noon", "cpuTooHigh");
    }

    ;

    @Scheduled(cron = "0 30 14 * * ?")
    public void noon_speedTooLow() {
        sync(null, "noon", "speedTooLow");
    }

    ;

    @Scheduled(cron = "0 30 23 * * ?")
    public void night_ifaceOffLine() {
        sync(null, "night", "ifaceOffLine");
    }

    ;

    @Scheduled(cron = "0 30 23 * * ?")
    public void night_reportOffLine() {
        sync(null, "night", "reportOffLine");
    }

    ;

    @Scheduled(cron = "0 30 23 * * ?")
    public void night_coredump() {
        sync(null, "night", "coredump");
    }

    ;

    @Scheduled(cron = "0 30 23 * * ?")
    public void night_cachePerG() {
        sync(null, "night", "cachePerG");
    }

    ;

    @Scheduled(cron = "0 30 23 * * ?")
    public void night_appGtNicBw() {
        sync(null, "night", "appGtNicBw");
    }

    ;

    @Scheduled(cron = "0 30 23 * * ?")
    public void night_tcpRetran() {
        sync(null, "night", "tcpRetran");
    }

    ;

    @Scheduled(cron = "0 30 23 * * ?")
    public void night_bwWave() {
        sync(null, "night", "bwWave");
    }

    ;

    @Scheduled(cron = "0 30 23 * * ?")
    public void night_ioWait() {
        sync(null, "night", "ioWait");
    }

    ;

    @Scheduled(cron = "0 30 23 * * ?")
    public void night_hotPushFailed() {
        sync(null, "night", "hotPushFailed");
    }

    ;

    @Scheduled(cron = "0 30 23 * * ?")
    public void night_ispIdentifyError() {
        sync(null, "night", "ispIdentifyError");
    }

    ;

    @Scheduled(cron = "0 30 23 * * ?")
    public void night_natIdentifyError() {
        sync(null, "night", "natIdentifyError");
    }

    ;

    @Scheduled(cron = "0 30 23 * * ?")
    public void night_lineSpeedError() {
        sync(null, "night", "lineSpeedError");
    }

    ;

    @Scheduled(cron = "0 30 23 * * ?")
    public void night_err500() {
        sync(null, "night", "err500");
    }

    ;

    @Scheduled(cron = "0 30 23 * * ?")
    public void night_cpuTooHigh() {
        sync(null, "night", "cpuTooHigh");
    }

    ;

    @Scheduled(cron = "0 30 23 * * ?")
    public void night_speedTooLow() {
        sync(null, "night", "speedTooLow");
    }

    ;

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
