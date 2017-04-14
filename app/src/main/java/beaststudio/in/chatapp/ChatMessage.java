package beaststudio.in.chatapp;

import java.util.Date;

/**
 * Created by beast on 14/4/17.
 */

public class ChatMessage {

    private String msgText;
    private String msgUser;
    private long msgTime;

    public ChatMessage(String msgText, String msgUser){

        this.msgText = msgText;
        this.msgUser = msgUser;

        msgTime = new Date().getTime();

    }

    public ChatMessage(){

    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public void setMsgUser(String msgUser) {
        this.msgUser = msgUser;
    }

    public void setMsgTime(long msgTime) {
        this.msgTime = msgTime;
    }

    public String getMsgText() {
        return msgText;
    }

    public String getMsgUser() {
        return msgUser;
    }

    public long getMsgTime() {
        return msgTime;
    }
}
