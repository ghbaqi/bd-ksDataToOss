package com.ppio.bd.ksdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

/**
 * 测试环境
 * * nohup java -jar -Dspring.profiles.active=dev  ks_data_to_oss-0.0.1-SNAPSHOT.jar > /dev/null 2>&1  &
 * *
 * * 生产环境
 * <p>
 * <p>
 * 部署在机器   172.16.76.185   ,   目录  /opt/ksDataToOss
 */
@EnableScheduling
@SpringBootApplication
public class KsDataToOssApplication {

    public static void main(String[] args) {
        SpringApplication.run(KsDataToOssApplication.class, args);
    }

    /**
     *  解决springboot 无法统一时间调度触发任务
     * @return
     */
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskExecutor = new ThreadPoolTaskScheduler();
        taskExecutor.setPoolSize(50);
        return taskExecutor;
    }


}
