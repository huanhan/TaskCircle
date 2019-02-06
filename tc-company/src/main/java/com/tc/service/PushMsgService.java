package com.tc.service;

import cn.jpush.api.push.model.PushPayload;
import com.tc.dto.app.ChatMsgDto;
import com.tc.dto.app.PushMsgState;
import com.tc.dto.app.TaskMsg;

import java.util.List;


public interface PushMsgService {

    void pushTask(String title, String content, String taskid, List<Long> userids);

    void pushTask(String title, String content, String taskid, Long... userid);

    void pushHunterTask(String title, String content, String hunterTaskid, List<Long> userids);

    void pushHunterTask(String title, String content, String hunterTaskid, Long userid);

    void pushHunterList(String title, String content, String taskid, List<Long> userids);

    void pushHunterList(String title, String content, String taskid, Long userid);

    void pushNotice(String title, String content);

    void pushNotice(String title, String content, List<String> userids);

    void pushNewChat(Long userid, ChatMsgDto chatMsgDto);

    void initPushPayload(String title, String content, PushMsgState state, String taskid, Long... userids);

    void initPushPayload(String title, String content, PushMsgState state, String taskid, List<Long> userids);

    void initPushPayload(PushMsgState state, TaskMsg taskMsg, List<String> userids);

    void sendPush(PushPayload payload);
}
