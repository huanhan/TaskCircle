package com.tc.controller;

import com.tc.db.enums.*;
import com.tc.dto.trans.TransEnum;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/basic")
public class BasicController {
    @GetMapping("/enums/{key}")
    @ApiOperation(value = "根据类别获取系统中的所使用的类别")
    public List<TransEnum> basicEnum(@PathVariable("key") bdState key){
        List<TransEnum> result = new ArrayList<>();
        switch (key){
            case ADMIN_STATE:
                result = AdminState.toList();
                break;
            case AUDIT_STATE:
                result = AuditState.toList();
                break;
            case AUDIT_TYPE:
                result = AuditType.toList();
                break;
            case COMMENT_TYPE:
                result = CommentType.toList();
                break;
            case DATE_TYPE:
                result = DateType.toList();
                break;
            case HUNTER_TASK_STATE:
                result = HunterTaskState.toList();
                break;
            case IE_CATEGORY:
                result = IECategory.toList();
                break;
            case MESSAGE_STATE:
                result = MessageState.toList();
                break;
            case MESSAGE_TYPE:
                result = MessageType.toList();
                break;
            case MONEY_TYPE:
                result = MoneyType.toList();
                break;
            case OP_TYPE:
                result = OPType.toList();
                break;
            case TASK_STATE:
                result = TaskState.toList();
                break;
            case TASK_TYPE:
                result = TaskType.toList();
                break;
            case USER_CATEGORY:
                result = UserCategory.toList();
                break;
            case USER_CONTACT_NAME:
                result = UserContactName.toList();
                break;
            case USER_GENDER:
                result = UserGender.toList();
                break;
            case USER_IMG_NAME:
                result = UserIMGName.toList();
                break;
            case USER_STATE:
                result = UserState.toList();
                break;
            case WITHDRAW_STATE:
                result = WithdrawState.toList();
                break;
            case WITHDRAW_TYPE:
                result = WithdrawType.toList();
                break;
            default:
                break;
        }
        return result;
    }



    private enum bdState{
        ADMIN_STATE,
        AUDIT_STATE,
        AUDIT_TYPE,
        COMMENT_TYPE,
        DATE_TYPE,
        HUNTER_TASK_STATE,
        IE_CATEGORY,
        MESSAGE_STATE,
        MESSAGE_TYPE,
        MONEY_TYPE,
        OP_TYPE,
        TASK_STATE,
        TASK_TYPE,
        USER_CATEGORY,
        USER_CONTACT_NAME,
        USER_GENDER,
        USER_IMG_NAME,
        USER_STATE,
        WITHDRAW_STATE,
        WITHDRAW_TYPE
    }
}
