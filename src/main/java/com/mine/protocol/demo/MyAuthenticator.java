package com.mine.protocol.demo;

import com.x.iot.protocol.support.DefaultTransport;
import com.x.iot.protocol.support.authentication.AuthenticationRequest;
import com.x.iot.protocol.support.authentication.AuthenticationResponse;
import com.x.iot.protocol.support.authentication.MqttAuthenticationRequest;
import com.x.iot.protocol.support.context.DeviceSessionCtx;
import com.x.iot.protocol.support.exception.AuthenticationException;
import com.x.iot.protocol.support.spi.Authenticator;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author zoro.kong
 * @className MyAuthenticator
 * @date 2025/6/13
 * @description TODO
 */
@Slf4j
public class MyAuthenticator implements Authenticator {
    /**
     * 设备使用MD5算法对设备秘钥加密传输
     * @param request 认证请求消息
     * @param deviceSession -初始化session信息
     * @return 认证响应消息-携带Session
     * @throws AuthenticationException 认证失败异常
     */
    @Override
    public AuthenticationResponse authenticate(@Nonnull AuthenticationRequest request, @Nonnull DeviceSessionCtx deviceSession) throws AuthenticationException {
        log.info("自定义设备身份认证...");
        if (request.getTransport().isSame(DefaultTransport.MQTT)){
            MqttAuthenticationRequest mqttAuthenticationRequest = (MqttAuthenticationRequest) request;
            String password = mqttAuthenticationRequest.getPassword();
            String platformPwd  = deviceSession.getMetaDevice().getDeviceSecret();
            if (platformPwd == null){
                throw new AuthenticationException(61002, "设备秘钥未配置");
            }
            platformPwd = encryptMD5(platformPwd);
            if (!platformPwd.equals(password)){
                throw new AuthenticationException(61001, "用户名或密码错误");
            }
            //会话信息没有更新的话直接返回
            return AuthenticationResponse.success(deviceSession);
        }

        throw new AuthenticationException(61000, "不支持的消息协议");
    }

    // MD5加密方法
    private String encryptMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5加密失败", e);
        }
    }
}
