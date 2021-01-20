package com.ppio.bd.ksdata;


import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class KsDataDownloadTest {

    @Autowired
    private OkHttpClient okHttpClient;


    @Test
    public void t() throws Exception {

        Request.Builder builder = new Request.Builder();
        builder.url("https://ks-error-data.oss-cn-hangzhou.aliyuncs.com/20210102/500err.txt");
        Request request = builder.build();
        Response response = okHttpClient.newCall(request).execute();
        log.info(response.toString());

        String dir = "./ksData/20210103/";
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }

        InputStream is = response.body().byteStream();
        FileOutputStream fos = new FileOutputStream(new File(dir + "500err.txt"));
        int len;
        byte[] buffer = new byte[2048];
        while (-1 != (len = is.read(buffer))) {
            fos.write(buffer, 0, len);
        }
        fos.flush();
        fos.close();
        is.close();

    }
}
