package com.tc.service;

import cn.jpush.api.push.model.PushPayload;
import com.tc.dto.app.ChatMsgDto;
import com.tc.dto.app.PushMsgState;

import java.util.List;


public interface PushMsgService {

    void initPushPayload(String title, String content, PushMsgState state, String taskid, String... userids);

    void initPushPayload(String title, String content, PushMsgState state, String taskid, List<String> userids);

    void sendPush(PushPayload payload);

    //推送任务状态
    void pushTask(String title, String content, String taskid, List<String> userids);

    void pushTask(String title, String content, String taskid, String... userid);

    //推送猎刃任务状态
    void pushHunterTask(String title, String content, String hunterTaskid, List<String> userids);

    void pushHunterTask(String title, String content, String hunterTaskid, String userid);

    //推送猎刃列表
    void pushHunterList(String title, String content, String taskid, List<String> userids);

    void pushHunterList(String title, String content, String taskid, String userid);

    //推送公共通知
    void pushNotice(String title, String content);

    void pushNotice(String title, String content, List<String> userids);

    //推送新消息
    void pushNewChat(String userid, ChatMsgDto chatMsgDto);
}
