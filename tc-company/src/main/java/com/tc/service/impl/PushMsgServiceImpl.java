package com.tc.service.impl;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.tc.dto.app.ChatMsgDto;
import com.tc.dto.app.PushMsgState;
import com.tc.dto.app.TaskMsg;
import com.tc.service.PushMsgService;
import com.tc.until.TranstionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class PushMsgServiceImpl implements PushMsgService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${taskcircle.jiguang.AppKey}")
    private String jgAppKey;

    @Value("${taskcircle.jiguang.MasterSecret}")
    private String jgMasterSecret;

    @Override
    public void pushTask(String title, String content, String taskid, List<Long> userids) {
        initPushPayload(title, content, PushMsgState.TASK, taskid, userids);
    }

    @Override
    public void pushTask(String title, String content, String taskid, Long... userid) {
        initPushPayload(title, content, PushMsgState.TASK, taskid, userid);
    }

    @Override
    public void pushHunterTask(String title, String content, String hunterTaskid, List<Long> userids) {
        initPushPayload(title, content, PushMsgState.HUNTER_TASK, hunterTaskid, userids);
    }

    @Override
    public void pushHunterTask(String title, String content, String hunterTaskid, Long userid) {
        initPushPayload(title, content, PushMsgState.HUNTER_TASK, hunterTaskid, userid);
    }

    @Override
    public void pushHunterList(String title, String content, String taskid, List<Long> userids) {
        initPushPayload(title, content, PushMsgState.HUNTER_LIST, taskid, userids);
    }

    @Override
    public void pushHunterList(String title, String content, String taskid, Long userid) {
        initPushPayload(title, content, PushMsgState.HUNTER_LIST, taskid, userid);
    }

    @Override
    public void pushNotice(String title, String content) {
        Map<String, String> param = new HashMap<>();
        param.put("type", PushMsgState.NOTICE.name());
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setNotification(Notification.android(content, title, param))
                .setAudience(Audience.all())
                .build();
        sendPush(payload);
    }

    @Override
    public void pushNotice(String title, String content, List<String> userids) {
        Map<String, String> param = new HashMap<>();
        param.put("type", PushMsgState.NOTICE.name());
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setNotification(Notification.android(content, title, param))
                .setAudience(Audience.alias(userids))
                .build();
        sendPush(payload);
    }

    @Override
    public void pushNewChat(Long userid, ChatMsgDto chatMsgDto) {
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.alias(u2p(userid)))
                .setMessage(Message.newBuilder().setMsgContent("")
                        .addExtra("type", PushMsgState.CHAT.name())
                        .addExtra("extra", TranstionHelper.toGson(chatMsgDto))
                        .build())
                .build();
        sendPush(payload);
    }

    public String u2p(Long id) {
        return "app_" + id;
    }

    @Override
    public void initPushPayload(String title, String content, PushMsgState state, String taskid, Long... userids) {
        initPushPayload(title, content, state, taskid, Arrays.asList(userids));
    }

    @Override
    public void initPushPayload(String title, String content, PushMsgState state, String taskid, List<Long> userids) {
        TaskMsg taskMsg = new TaskMsg();
        taskMsg.setContent(content);
        taskMsg.setExtraData(taskid);
        taskMsg.setTitle(title);

        List<String> userIdList = new ArrayList<>();
        for (Long userid : userids) {
            userIdList.add(u2p(userid));
        }
        initPushPayload(state, taskMsg, userIdList);
    }

    @Override
    public void initPushPayload(PushMsgState state, TaskMsg taskMsg, List<String> userids) {
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.alias(userids))
                .setMessage(Message.newBuilder().setMsgContent("")
                        .addExtra("type", state.name())
                        .addExtra("extra", TranstionHelper.toGson(taskMsg))
                        .build())//自定义消息用的
                .build();
        sendPush(payload);
    }

    @Override
    public void sendPush(PushPayload payload) {
        JPushClient jpushClient = new JPushClient(jgMasterSecret, jgAppKey, null, ClientConfig.getInstance());
        try {
            PushResult result = jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            logger.error("Connection error, should retry later", e);
        } catch (APIRequestException e) {
            logger.error("Should review the error, and fix the request", e);
            logger.info("HTTP Status: " + e.getStatus());
            logger.info("Error Code: " + e.getErrorCode());
            logger.info("Error Message: " + e.getErrorMessage());
        }
    }


}
