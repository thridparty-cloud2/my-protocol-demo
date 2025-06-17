package com.mine.protocol.demo.msg;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zoro.kong
 * @className DeviceStateMsg
 * @date 2025/6/17
 * @description TODO
 */
@Getter
@Setter
public class DeviceStateMsg {
    private String timestamp;
    private String state ;
    private Integer brightness;
}
