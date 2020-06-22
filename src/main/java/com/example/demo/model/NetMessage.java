package com.example.demo.model;

import java.util.Date;

/**
 * 用于在网络上传送数据的message（另一个Message类用于持久化）。<br><br>
 * 1. 当需要向某个好友发送单聊消息时，type为1，fromId填入当前用户uid，toId填入好友uid。<br><br>
 * 2. 当需要在某个群内发送群聊消息时，type为2，fromId填入当前用户uid，toId填入群rid。<br><br>
 * 3. 当需要添加某个人为好友时，type为3，fromId填入当前用户uid，toId填入搜索后得到的uid。<br><br>
 * 4. 当需要邀请好友加入某个群时，type为4，fromId填入当前用户uid，toId填入好友uid。<br><br>
 * 5. 当收到加好友请求并作出应答时，type为5，fromId和toId与加好友消息中的相反。<br><br>
 * 6. 关于style和content：type为1、2时，style为1时content是文本内容，style为2时content是图片完整URL；<br><br>
 *    type为3时，style为1，content为验证消息；type为4时，style为1，content为rid；<br><br>
 *    type为5时，style为1，content为"true"或"false"（String类型）。
 */
public class NetMessage {

    /**
     * 消息类型：
     * 1 单聊，
     * 2 群聊，
     * 3 加好友，
     * 4 邀请进群，
     * 5 同意/拒绝加好友
     */
    private int type;

    /**
     * 发送方uid
     */
    private int fromId;

    /**
     * 接收方uid或rid
     */
    private int toId;

    /**
     * 发送时间
     */
    private Date sendTime = new Date();

    /**
     * 辨别记录是文字还是图片
     * 1 文字
     * 2 图片
     */
    private int style = 1;

    /**
     * 文本内容或图片URL
     */
    private String content;

    public NetMessage() {
    }

    public NetMessage(int type, int fromId, int toId, String content) {
        this.type = type;
        this.fromId = fromId;
        this.toId = toId;
        this.content = content;
    }

    public NetMessage(int type, int fromId, int toId, int style, String content) {
        this.type = type;
        this.fromId = fromId;
        this.toId = toId;
        this.style = style;
        this.content = content;
    }

    public NetMessage(Message message) {
        this.type = message.getType();
        this.fromId = message.getFromId();
        this.toId = message.getToId();
        this.sendTime = message.getSendTime();
        this.style = message.getStyle();
        this.content = message.getContent();
    }

    public int getType() {
        return type;
    }

    public int getFromId() {
        return fromId;
    }

    public int getToId() {
        return toId;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public int getStyle() {
        return style;
    }

    public String getContent() {
        return content;
    }
}
