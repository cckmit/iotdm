package com.aibaixun.iotdm.business;

import com.aibaixun.iotdm.queue.QueueSendServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息队列处理器
 * @author wangxiao@aibaixun.com
 * @date 2022/3/11
 */
@Component
public class QueueBusinessProcessor extends AbstractReportProcessor<PostPropertyBusinessMsg,MessageBusinessMsg>{

    private QueueSendServer queueSendService;

    @Override
    public void processProperty(PostPropertyBusinessMsg propertyBusinessMsg) {
        queueSendService.sendPropertyTsData(propertyBusinessMsg);
    }

    @Override
    public void processMessage(MessageBusinessMsg messageBusinessMsg) {
        queueSendService.sendMessageTsData(messageBusinessMsg);
    }

    @Autowired
    public void setQueueSendService(QueueSendServer queueSendService) {
        this.queueSendService = queueSendService;
    }
}