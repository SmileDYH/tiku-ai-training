package com.edu.tiku.common.massage;

import com.edu.tiku.model.request.JywQueryQuestionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * description: 消息队列配置类
 * 需求：阻塞 有界 单向链表就够用
 *      需要阻塞，所以使用put take
 * author: dyh
 * date: 2023/2/6 11:51
 */
@Configuration
@Slf4j
public class MassageConfiguration {

    @Autowired
    private MassageConsumer massageConsumer;

    /**
     * 执行消费者
     * 如果不单独启动一个线程，启动时就会进入一个死循环，需要一个线程托管
     */
    @Bean
    public void execute() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                massageConsumer.execute();
            }
        }).start();
    }


    //创建消息队列
    @Bean(name = "massageQueue")
    @Qualifier("massageQueue")
    public LinkedBlockingQueue<JywQueryQuestionRequest> massageQueue() {
        log.info("初始化队列长度LinkedBlockingQueue：" + 2048);
        return new LinkedBlockingQueue<JywQueryQuestionRequest>(2048);
    }

}
