package com.mine.protocol.demo.msg;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zoro.kong
 * @className DeviceCommandMsg
 * @date 2025/6/17
 * @description TODO
 */
@Setter
@Getter
public class DeviceCommandMsg {
    private String action;
    private Props payload;
}
