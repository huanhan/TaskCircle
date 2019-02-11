package com.tc.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.tc.dto.app.OssToken;
import com.tc.exception.ValidException;
import com.tc.until.StringResourceCenter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/app/resource")
public class AppResourceController {

    @Value("${taskcircle.oss.Endpoint}")
    private String endpoint;

    @Value("${taskcircle.oss.AccessKeyID}")
    private String accessKeyId;

    @Value("${taskcircle.oss.AccessKeySecret}")
    private String accessKeySecret;

    @Value("${taskcircle.oss.RoleArn}")
    private String roleArn;


    @GetMapping("/osstoken")
    @ApiOperation(value = "获取阿里云oss图片上传token")
    public OssToken osstoken() {
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
            return new OssToken(response.getCredentials().getExpiration(),
                    response.getCredentials().getAccessKeyId(),
                    response.getCredentials().getAccessKeySecret(),
                    response.getCredentials().getSecurityToken());
        } catch (ClientException e) {
            throw new ValidException(StringResourceCenter.VALIDATOR_OSS_TOKEN_FAILED);
        }
    }


}
