package com.mine.protocol.demo.msg;

import cn.hutool.core.util.RandomUtil;
import com.x.iot.protocol.support.message.TagTypeLenValue;
import com.x.iot.protocol.support.message.standard.ThingModelMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zoro.kong
 * @className ThingsModelMsgConverter
 * @date 2025/6/17
 * @description TODO 自定义消息与物模型消息映射 可以通过设备调试或虚拟客户端熟悉TTLV数据组装格式。
 */
public class ThingsModelMsgConverter {

    public static ThingModelMessage convert(DeviceStateMsg deviceStateMsg,String payload) {
        ThingModelMessage thingModelMessage = new ThingModelMessage();
        //防止时间戳冲突
        thingModelMessage.setMsgId(deviceStateMsg.getTimestamp() + RandomUtil.randomNumbers(3));
        thingModelMessage.setMsgSize((long)payload.length());
        thingModelMessage.setSubType(ThingModelMessage.ThingsModelSubType.REPORT);
        thingModelMessage.setPayload(payload);
        List<TagTypeLenValue> tagTypeLenValues = new ArrayList<>();
        if (deviceStateMsg.getState() != null){
            TagTypeLenValue tagTypeLenValue = new TagTypeLenValue();
            tagTypeLenValue.setId(TslEnum.STATE.id);
            tagTypeLenValue.setType(TslEnum.STATE.type);
            tagTypeLenValue.setValue(State.getTslValue(deviceStateMsg.getState()));
            tagTypeLenValues.add(tagTypeLenValue);
        }
        if (deviceStateMsg.getBrightness() != null){
            TagTypeLenValue tagTypeLenValue = new TagTypeLenValue();
            tagTypeLenValue.setId(TslEnum.BRIGHTNESS.id);
            tagTypeLenValue.setType(TslEnum.BRIGHTNESS.type);
            tagTypeLenValue.setValue(deviceStateMsg.getBrightness());
            tagTypeLenValues.add(tagTypeLenValue);
        }
        thingModelMessage.setMessage(tagTypeLenValues);

        return thingModelMessage;
    }
    public static ThingModelMessage convert(DeviceErrorMsg deviceErrorMsg,String payload) {
        ThingModelMessage thingModelMessage = new ThingModelMessage();
        //随机生成一个
        thingModelMessage.setMsgId(RandomUtil.randomNumbers(6));
        thingModelMessage.setMsgSize((long)payload.length());
        thingModelMessage.setSubType(ThingModelMessage.ThingsModelSubType.REPORT);
        thingModelMessage.setPayload(payload);
        List<TagTypeLenValue> tagTypeLenValues = new ArrayList<>();
        if (deviceErrorMsg.getCode().equalsIgnoreCase("ERR_OVERHEAT")){
            TagTypeLenValue tagTypeLenValue = new TagTypeLenValue();
            tagTypeLenValue.setId(TslEnum.ERR_OVERHEAT.id);
            tagTypeLenValue.setType(TslEnum.ERR_OVERHEAT.type);
            List<TagTypeLenValue> values = new ArrayList<>();
            TagTypeLenValue value = new TagTypeLenValue();
            SEVERITY severity = SEVERITY.getByCode(deviceErrorMsg.getSeverity());
            if (severity != null) {
                value.setId(severity.id);
                value.setType("enum");
                value.setValue(severity.value);
                values.add(value);
            }
            tagTypeLenValue.setValue(values);
            tagTypeLenValues.add(tagTypeLenValue);
        }
        thingModelMessage.setMessage(tagTypeLenValues);
        return thingModelMessage;
    }

    public static ThingModelMessage convert(DeviceCommandMsg deviceCommandMsg,String payload) {
        ThingModelMessage thingModelMessage = new ThingModelMessage();
        //随机生成一个
        thingModelMessage.setMsgId(RandomUtil.randomNumbers(6));
        thingModelMessage.setMsgSize((long)payload.length());
        thingModelMessage.setSubType(ThingModelMessage.ThingsModelSubType.REPORT);
        thingModelMessage.setPayload(payload);
        List<TagTypeLenValue> tagTypeLenValues = new ArrayList<>();
        if (deviceCommandMsg.getPayload().getState() != null){
            TagTypeLenValue tagTypeLenValue = new TagTypeLenValue();
            tagTypeLenValue.setId(TslEnum.STATE.id);
            tagTypeLenValue.setType(TslEnum.STATE.type);
            tagTypeLenValue.setValue(State.getTslValue(deviceCommandMsg.getPayload().getState()));
            tagTypeLenValues.add(tagTypeLenValue);
        }
        if (deviceCommandMsg.getPayload().getBrightness() != null){
            TagTypeLenValue tagTypeLenValue = new TagTypeLenValue();
            tagTypeLenValue.setId(TslEnum.BRIGHTNESS.id);
            tagTypeLenValue.setType(TslEnum.BRIGHTNESS.type);
            tagTypeLenValue.setValue(deviceCommandMsg.getPayload().getBrightness());
            tagTypeLenValues.add(tagTypeLenValue);
        }
        thingModelMessage.setMessage(tagTypeLenValues);

        return thingModelMessage;
    }
    /**
     * 记录自定义消息与物模型映射关系
     */
    public static enum TslEnum {
        STATE("state",1,"enum"),
        BRIGHTNESS("brightness",2,"number"),
        //事件输出参数类型为结构体
        ERR_OVERHEAT("ERR_OVERHEAT",3,"struct");
        private final String code;
        private final int id;
        private final String type;
        TslEnum(String code, int id, String type) {
            this.code = code;
            this.id = id;
            this.type = type;
        }

        public static TslEnum getByCode(String code) {
            for (TslEnum tslEnum : TslEnum.values()) {
                if (tslEnum.code.equalsIgnoreCase(code)) {
                    return tslEnum;
                }
            }
            return null;
        }
    }

    public static enum State{
        ON("ON",1),
        OFF("OFF",0);
        private final String code;
        private final int value;
        State(String code, int value) {
            this.code = code;
            this.value = value;
        }
        public static Integer getTslValue(String code) {
            for (State state : State.values()) {
                if (state.code.equals(code)) {
                    return state.value;
                }
            }
            return null;
        }
    }

    /**
     * 定义OVERHEAT事件参数SEVERITY
     */
    public static enum SEVERITY{
        LOW("LOW",4,"1"),
        MEDIUM("MEDIUM",4,"2"),
        HIGH("HIGH",4,"3"),
        CRITICAL("CRITICAL",4,"4");
        private final String code;
        private final int id;
        private final String value;
        SEVERITY(String code, int id, String value) {
            this.code = code;
            this.id = id;
            this.value = value;
        }

        public static SEVERITY getByCode(String code) {
            for (SEVERITY severity : SEVERITY.values()) {
                if (severity.code.equals(code)) {
                    return severity;
                }
            }
            return null;
        }
    }
}
