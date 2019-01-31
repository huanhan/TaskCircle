package com.tc.service;

import cn.jpush.api.push.model.PushPayload;
import com.tc.dto.app.ChatMsgDto;
import com.tc.dto.app.PushMsgState;

import java.util.List;


public interface PushMsgService {

    void initPushPayload(String title, String content, PushMsgState state, String taskid, String... userids);

    void initPushPayload(String title, String content, PushMsgState state, String taskid, List<String> userids);

    void sendPush(PushPayload payload);

    void pushTask(String title, String content, String taskid, List<String> userids);

    void pushTask(String title, String content, String taskid, String... userid);

    void pushHunterTask(String title, String content, String hunterTaskid, List<String> userids);

    void pushHunterTask(String title, String content, String hunterTaskid, String userid);

    void pushHunterList(String title, String content, String taskid, List<String> userids);

    void pushHunterList(String title, String content, String taskid, String userid);

    void pushNotice(String title, String content);

    void pushNotice(String title, String content, List<String> userids);

    void pushNewChat(String userid, ChatMsgDto chatMsgDto);
}
