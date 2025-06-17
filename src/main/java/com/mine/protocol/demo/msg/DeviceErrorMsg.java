package com.mine.protocol.demo.msg;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zoro.kong
 * @className DeviceErrorMsg
 * @date 2025/6/17
 * @description TODO
 */
@Setter
@Getter
public class DeviceErrorMsg {
    private String timestamp;
    private String code;
    private String message;
    private String severity;
}
