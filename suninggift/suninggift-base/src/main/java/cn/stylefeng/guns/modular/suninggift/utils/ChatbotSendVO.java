package cn.stylefeng.guns.modular.suninggift.utils;

import java.io.Serializable;

public class ChatbotSendVO implements Serializable {

    private String msgtype;

    private ChatBotMsgVO text;

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public ChatBotMsgVO getText() {
        return text;
    }

    public void setText(ChatBotMsgVO text) {
        this.text = text;
    }
}
