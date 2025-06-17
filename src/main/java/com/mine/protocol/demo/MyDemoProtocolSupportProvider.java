package com.mine.protocol.demo;

import com.x.iot.protocol.support.CompositeProtocolSupport;
import com.x.iot.protocol.support.DefaultTransport;
import com.x.iot.protocol.support.ProtocolSupport;
import com.x.iot.protocol.support.context.ServiceContext;
import com.x.iot.protocol.support.spi.ProtocolSupportProvider;

/**
 * @author zoro.kong
 * @className MyDemoProtocolSupportProvider
 * @date 2025/6/14
 * @description TODO
 */
public class MyDemoProtocolSupportProvider implements ProtocolSupportProvider {
    @Override
    public ProtocolSupport create(ServiceContext context) {
        System.out.println("Create demo protocol support...");
        CompositeProtocolSupport support = new CompositeProtocolSupport();
        support.setId("my demo protocol");
        support.setName("示例自定义协议");
        support.addAuthenticator(DefaultTransport.MQTT, new MyAuthenticator());
        support.addMessageCodecSupport(DefaultTransport.MQTT, new MyMqttMessageCodec());
        return support;
    }
    @Override
    public void close() {
        ProtocolSupportProvider.super.close();
    }
}
