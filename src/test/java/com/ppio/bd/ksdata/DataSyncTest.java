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


        int i = 20210908;
        String noonNight = "noon";
        task.sync(i + "", noonNight, "ifaceOffLine");
        task.sync(i + "", noonNight, "reportOffLine");
        task.sync(i + "", noonNight, "coredump");
        task.sync(i + "", noonNight, "cachePerG");
        task.sync(i + "", noonNight, "appGtNicBw");
        task.sync(i + "", noonNight, "tcpRetran");
        task.sync(i + "", noonNight, "bwWave");
        task.sync(i + "", noonNight, "ioWait");
        task.sync(i + "", noonNight, "hotPushFailed");
        task.sync(i + "", noonNight, "ispIdentifyError");
        task.sync(i + "", noonNight, "natIdentifyError");
        task.sync(i + "", noonNight, "lineSpeedError");
        task.sync(i + "", noonNight, "err500");
        task.sync(i + "", noonNight, "cpuTooHigh");
        task.sync(i + "", noonNight, "speedTooLow");


        //Thread.sleep(1000000L);
    }
}
