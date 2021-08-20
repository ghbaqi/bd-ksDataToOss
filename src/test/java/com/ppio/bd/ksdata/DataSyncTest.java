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

        int i = 20210819;
        task.sync(i + "", "500err");
        task.sync(i + "", "io_wait");
        task.sync(i + "", "retran");
        task.sync(i + "", "speed");
        task.sync(i + "", "upload");
        task.sync(i + "", "hotPushFail");
        task.sync(i + "", "ifaceOffLine");
        task.sync(i + "", "reportOffLine");
        task.sync(i + "", "coredump");
        task.sync(i + "", "cachePerG");
        task.sync(i + "", "appGtNicBw");
        task.sync(i + "", "tcpRetran");
        task.sync(i + "", "bwWave");


        //Thread.sleep(1000000L);
    }
}
