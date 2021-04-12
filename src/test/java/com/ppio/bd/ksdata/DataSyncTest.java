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
        for (int i = 20210305; i <= 20210307; i++) {
            task.sync(i + "", "500err");
            task.sync(i + "", "cpu");
            task.sync(i + "", "io_wait");
            task.sync(i + "", "retran");
            task.sync(i + "", "speed");
            task.sync(i + "", "upload");
        }

        Thread.sleep(1000000L);
    }
}
