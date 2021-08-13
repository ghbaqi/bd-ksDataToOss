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
        task.sync(20210812 + "", "hotPushFail");
        task.sync(20210812 + "", "ifaceOffLine");
        task.sync(20210812 + "", "reportOffLine");
        task.sync(20210812 + "", "coredump");
        task.sync(20210812 + "", "cachePerG");
        task.sync(20210812 + "", "appGtNicBw");
        task.sync(20210812 + "", "tcpRetran");
        task.sync(20210812 + "", "bwWave");


        //Thread.sleep(1000000L);
    }
}
