package com.edu.tiku.common.massage;

import com.edu.tiku.controller.JywController;
import com.edu.tiku.model.request.JywQueryQuestionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * description: 消息消费者
 * author: dyh
 * date: 2023/2/6 11:56
 */
@Component
@Slf4j
public class MassageConsumer {

    @Autowired
    @Qualifier("massageQueue")
    private LinkedBlockingQueue<JywQueryQuestionRequest> queue;

    @Autowired
    private JywController jywController;

    public void execute(){
        log.info("启动任务监听...");
        while (true){
            if (!queue.isEmpty()){
                log.info("队列长度:{}", queue.size());
                JywQueryQuestionRequest request = null;
                try {
                    request = queue.take();
                }catch (Exception e){
                    log.info("消费异常：{}", e);
                }
                log.info("消息队列开始消费...");
                jywController.queryQuestion(request);
            }
        }
    }
}
