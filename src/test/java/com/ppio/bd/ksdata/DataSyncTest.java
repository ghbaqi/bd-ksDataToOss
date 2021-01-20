package com.ppio.bd.ksdata;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class DataSyncTest {

    @Autowired
    KsDataToOssTask task;

    @Test
    public void t() throws InterruptedException {
        for (int i = 20210111; i <= 20210119; i++) {
            task.sync(i + "", "speed");
        }

        Thread.sleep(1000000L);
    }
}
