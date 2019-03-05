package com.tc.web.controller;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.*;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.schedule.ScheduleResult;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.tc.dto.app.ChatMsgDto;
import com.tc.dto.app.PushMsgState;
import com.tc.dto.app.TaskMsg;
import com.tc.exception.ValidException;
import com.tc.until.StringResourceCenter;
import com.tc.until.TimestampHelper;
import com.tc.until.TranstionHelper;
import org.junit.Test;

public class TestUntil {
    @Test
    public void testOss() {

        String endpoint = "sts.aliyuncs.com";
        String accessKeyId = "LTAIS17qvNECIMxb";
        String accessKeySecret = "VSFSlJ6mJ2XsZb35Dmw1H33XCQwyfA";
        String roleArn = "acs:ram::1631359334772636:role/aliyunosstokengeneratorrole";
        String roleSessionName = "session-name";
        String policy = "{\n" +
                "    \"Version\": \"1\", \n" +
                "    \"Statement\": [\n" +
                "        {\n" +
                "            \"Action\": [\n" +
                "                \"oss:*\"\n" +
                "            ], \n" +
                "            \"Resource\": [\n" +
                "                \"acs:oss:*:*:*\" \n" +
                "            ], \n" +
                "            \"Effect\": \"Allow\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        try {
            // 添加endpoint（直接使用STS endpoint，前两个参数留空，无需添加region ID）
            DefaultProfile.addEndpoint("", "", "Sts", endpoint);
            // 构造default profile（参数留空，无需添加region ID）
            IClientProfile profile = DefaultProfile.getProfile("", accessKeyId, accessKeySecret);
            // 用profile构造client
            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setMethod(MethodType.POST);
            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(policy); // Optional
            final AssumeRoleResponse response = client.getAcsResponse(request);
            System.out.println("Expiration: " + response.getCredentials().getExpiration());
            System.out.println("Access Key Id: " + response.getCredentials().getAccessKeyId());
            System.out.println("Access Key Secret: " + response.getCredentials().getAccessKeySecret());
            System.out.println("Security Token: " + response.getCredentials().getSecurityToken());
            System.out.println("RequestId: " + response.getRequestId());
        } catch (ClientException e) {
            System.out.println("Failed：");
            System.out.println("Error code: " + e.getErrCode());
            System.out.println("Error message: " + e.getErrMsg());
            System.out.println("RequestId: " + e.getRequestId());
        }
    }


    private String jgAppKey = "2cc8cc0b7c40c0da475d63a5";

    private String jgMasterSecret = "47019aa537adedb5b6b3c6e7";

    @Test
    public void testTaskPush() {
        JPushClient jpushClient = new JPushClient(jgMasterSecret, jgAppKey, null, ClientConfig.getInstance());

        TaskMsg taskMsg = new TaskMsg();
        taskMsg.setContent("测试任务推送");
        taskMsg.setExtraData("20190201040203278856064");
        taskMsg.setTitle("测试标题");

        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.all())
                //.setNotification(Notification.android("出现的内容", "测试标题", stringObjectHashMap))//最后一个用来携带参数
                .setMessage(Message.newBuilder().setMsgContent("")
                        .addExtra("type", PushMsgState.TASK.name())
                        .addExtra("extra", TranstionHelper.toGson(taskMsg))
                        .build())//自定义消息用的
                .build();
        try {
            PushResult result = jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            throw new ValidException(StringResourceCenter.VALIDATOR_CONNECTION_ERROR);
        } catch (APIRequestException e) {
            throw new ValidException(StringResourceCenter.VALIDATOR_REQUEST_ERROR);
        }
    }

    @Test
    public void testHunterTaskPush() {
        JPushClient jpushClient = new JPushClient(jgMasterSecret, jgAppKey, null, ClientConfig.getInstance());

        TaskMsg taskMsg = new TaskMsg();
        taskMsg.setContent("测试猎刃任务推送");
        taskMsg.setExtraData("20190201040254723856064");
        taskMsg.setTitle("测试标题");

        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.all())
                //.setNotification(Notification.android("出现的内容", "测试标题", stringObjectHashMap))//最后一个用来携带参数
                .setMessage(Message.newBuilder().setMsgContent("")
                        .addExtra("type", PushMsgState.HUNTER_TASK.name())
                        .addExtra("extra", TranstionHelper.toGson(taskMsg))
                        .build())//自定义消息用的
                .build();
        try {
            PushResult result = jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            throw new ValidException(StringResourceCenter.VALIDATOR_CONNECTION_ERROR);
        } catch (APIRequestException e) {
            throw new ValidException(StringResourceCenter.VALIDATOR_REQUEST_ERROR);
        }
    }

    @Test
    public void testHunterListPush() {
        JPushClient jpushClient = new JPushClient(jgMasterSecret, jgAppKey, null, ClientConfig.getInstance());

        TaskMsg taskMsg = new TaskMsg();
        taskMsg.setContent("测试猎刃列表推送");
        taskMsg.setExtraData("20190201040203278856064");
        taskMsg.setTitle("测试标题");

        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.all())
                //.setNotification(Notification.android("出现的内容", "测试标题", stringObjectHashMap))//最后一个用来携带参数
                .setMessage(Message.newBuilder().setMsgContent("")
                        .addExtra("type", PushMsgState.HUNTER_LIST.name())
                        .addExtra("extra", TranstionHelper.toGson(taskMsg))
                        .build())//自定义消息用的
                .build();
        try {
            PushResult result = jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            throw new ValidException(StringResourceCenter.VALIDATOR_CONNECTION_ERROR);
        } catch (APIRequestException e) {
            throw new ValidException(StringResourceCenter.VALIDATOR_REQUEST_ERROR);
        }
    }

    @Test
    public void testChatMsgPush() {
        JPushClient jpushClient = new JPushClient(jgMasterSecret, jgAppKey, null, ClientConfig.getInstance());

        ChatMsgDto chatMsgDto = new ChatMsgDto();
        chatMsgDto.setTitle("来自xx用户的新消息");
        chatMsgDto.setUserIcon("https://i0.hdslb.com/bfs/face/156d5d3b3f4b66d940365b3b0e3a809e1fcc0d97.jpg");
        chatMsgDto.setHunterIcon("https://i0.hdslb.com/bfs/face/156d5d3b3f4b66d940365b3b0e3a809e1fcc0d97.jpg");
        chatMsgDto.setContent("你看我帅吗你看我我帅吗你看我帅吗");
        chatMsgDto.setCreateTime(TimestampHelper.today());
        chatMsgDto.setHunterId(13l);
        chatMsgDto.setHunterTaskId("20190201040203278856064");
        chatMsgDto.setUserId(6l);
        chatMsgDto.setSender(6l);

        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.all())
                //.setNotification(Notification.android("出现的内容", "测试标题", stringObjectHashMap))//最后一个用来携带参数
                .setMessage(Message.newBuilder().setMsgContent("")
                        .addExtra("type", PushMsgState.CHAT.name())
                        .addExtra("extra", TranstionHelper.toGson(chatMsgDto))
                        .build())//自定义消息用的
                .build();
        try {
            PushResult result = jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            throw new ValidException(StringResourceCenter.VALIDATOR_CONNECTION_ERROR);
        } catch (APIRequestException e) {
            throw new ValidException(StringResourceCenter.VALIDATOR_REQUEST_ERROR);
        }
    }

    @Test
    public void testPush2() {
        JPushClient jpushClient = new JPushClient(jgMasterSecret, jgAppKey, null, ClientConfig.getInstance());

        String name = "test_schedule_example";
        String time = "2019-01-31 19:40:25";
        PushPayload push = PushPayload.alertAll("test schedule example.");
        try {
            ScheduleResult result = jpushClient.createSingleSchedule(name, time, push);
        } catch (APIConnectionException e) {
            throw new ValidException(StringResourceCenter.VALIDATOR_CONNECTION_ERROR);
        } catch (APIRequestException e) {
            throw new ValidException(StringResourceCenter.VALIDATOR_REQUEST_ERROR);
        }
    }

}
