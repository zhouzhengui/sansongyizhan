package cn.stylefeng.guns.modular.suninggift.utils;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChatBotMsgVO implements Serializable {

    ChatBotMsgVO(){}
    ChatBotMsgVO(String content){
        this.content = content;
    }

    private String content;
}
