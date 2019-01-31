package com.tc.web.controller;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.*;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.schedule.ScheduleResult;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.tc.exception.ValidException;
import com.tc.until.StringResourceCenter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static cn.jpush.api.push.model.notification.PlatformNotification.ALERT;

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
    public void testPush() {
        JPushClient jpushClient = new JPushClient(jgMasterSecret, jgAppKey, null, ClientConfig.getInstance());

        Map<String, String> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("test", "1212");

        PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.alias("123"))
                .setNotification(Notification.android("出现的内容", "测试标题", stringObjectHashMap))//最后一个用来携带参数
                //.setMessage(Message.newBuilder().setTitle("Message测试标题").setMsgContent("Message测试内容").build())//自定义消息用的
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
