package com.tc.controller;

import com.tc.db.entity.Message;
import com.tc.db.entity.User;
import com.tc.db.enums.MessageState;
import com.tc.dto.Result;
import com.tc.dto.app.AppPage;
import com.tc.dto.app.MessageAppDto;
import com.tc.dto.message.QueryMessage;
import com.tc.exception.DBException;
import com.tc.service.MessageService;
import com.tc.service.UserService;
import com.tc.until.StringResourceCenter;
import com.tc.until.TranstionHelper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * APP的消息控制器
 *
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/app/message")
public class AppMessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    //查询公告
    @GetMapping("/{id:\\d+}/{page:\\d+}/{size:\\d+}")
    @ApiOperation(value = "查询所有可见公告")
    public AppPage all(@PathVariable Long id, @PathVariable int page, @PathVariable int size) {
        User user = userService.findOne(id);
        if (user == null) {
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }

        QueryMessage queryMessage = new QueryMessage(page, size);
        queryMessage.setState(MessageState.NORMAL);
        Page<Message> query = messageService.findByQuery(queryMessage);
        List<Message> messageList = query.getContent();
        List<Message> result = Message.byUser(messageList, user);
        List<MessageAppDto> messageAppDtos = MessageAppDto.initList(result);
        return AppPage.init(messageAppDtos, query);
    }


}