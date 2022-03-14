package com.aibaixun.iotdm.rule.send;

import com.aibaixun.iotdm.enums.ResourceType;
import com.aibaixun.iotdm.rule.pool.PoolResource;
import com.aibaixun.iotdm.rule.pool.ResourceLruCache;
import com.aibaixun.iotdm.support.BaseResourceConfig;
import com.aibaixun.iotdm.support.BaseTargetConfig;
import com.aibaixun.iotdm.support.RabbitResourceConfig;
import com.aibaixun.iotdm.support.RabbitTargetConfig;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author wangxiao@aibaixun.com
 * @date 2022/3/14
 */
@Component
public class RabbitSendService implements SendService{

    private final Logger log = LoggerFactory.getLogger(RabbitSendService.class);

    private ResourceLruCache<String, RabbitConnectionResource> connectionPool;

    @Value("${bx.rule.rabbit.max-idle-connections}")
    private Integer maxIdleConnections;


    @Override
    public <T> void doSendMessage(T message, BaseResourceConfig resourceConfig, BaseTargetConfig targetConfig)  {

        RabbitResourceConfig rabbitResourceConfig = (RabbitResourceConfig) resourceConfig;
        RabbitTargetConfig rabbitTargetConfig = (RabbitTargetConfig) targetConfig;
        String host = rabbitResourceConfig.getHost();
        int port = rabbitResourceConfig.getPort();
        String virtualHost = rabbitResourceConfig.getVirtualHost();
        String username = rabbitResourceConfig.getUsername();
        String password = rabbitResourceConfig.getPassword();
        Integer connectTimeout = rabbitResourceConfig.getConnectTimeout();
        Integer keepLive = rabbitResourceConfig.getKeepLive();
        RabbitConnectionResource rabbitConnection = connectionPool.get(host+port);
        if (Objects.isNull(rabbitConnection)){
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            factory.setPort(port);
            factory.setVirtualHost(virtualHost);
            factory.setUsername(username);
            factory.setPassword(password);
            factory.setAutomaticRecoveryEnabled(true);
            factory.setConnectionTimeout(connectTimeout);
            factory.setHandshakeTimeout(keepLive);
            try {
                Connection connection = factory.newConnection();
                rabbitConnection = new RabbitConnectionResource(connection);
                String exchangeName = rabbitTargetConfig.getExchange();
                String routingKey = rabbitTargetConfig.getRoutingKey();
                BuiltinExchangeType exchangeType = rabbitTargetConfig.getExchangeType();
                if (Objects.isNull(rabbitConnection.channel)){
                    return;
                }
                Channel channel = rabbitConnection.channel;
                String data = OBJECT_MAPPER.writeValueAsString(message);
                channel.exchangeDeclare(exchangeName,exchangeType,true,false,null);
                channel.basicPublish(
                        exchangeName,
                        routingKey,
                        MessageProperties.BASIC,
                        data.getBytes(StandardCharsets.UTF_8));
            }catch (Exception e){
                log.error("RabbitSendService.doSendMessage >> create connection is error, msg is:{}",e.getMessage());
            }
        }
    }

    @Override
    @PostConstruct
    public void init() {
        registerService(ResourceType.RABBIT,this);
        connectionPool = new ResourceLruCache<>(maxIdleConnections);
    }

    static class RabbitConnectionResource implements PoolResource {
      private final Connection connection;

      private final Channel channel;
        @Override
        public void releaseResource() {
            try {
                if (connection!= null){
                    connection.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            };
        }

        public RabbitConnectionResource(Connection connection) throws IOException {
            this.connection = connection;
            this.channel = connection.createChannel();
        }
    }


}