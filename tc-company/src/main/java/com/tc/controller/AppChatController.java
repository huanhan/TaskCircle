package com.tc.controller;

import com.tc.db.entity.Task;
import com.tc.db.entity.User;
import com.tc.db.entity.UserHunterInterflow;
import com.tc.dto.app.*;
import com.tc.dto.task.QueryTaskInterflow;
import com.tc.exception.ValidException;
import com.tc.service.PushMsgService;
import com.tc.service.UserHunterInterflowService;
import com.tc.service.UserService;
import com.tc.until.TimestampHelper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/app/chat")
public class AppChatController {

    @Autowired
    private UserHunterInterflowService userHunterInterflowService;

    @Autowired
    private PushMsgService pushMsgService;

    @Autowired
    private UserService userService;

    /**
     * 获取指定任务，指定用户，指定猎刃的聊天内容
     *
     * @return
     */
    @GetMapping("/{taskid:\\d+}/{hunterid:\\d+}/{userid:\\d+}/{page:\\d+}/{size:\\d+}")
    @ApiOperation(value = "获取指定任务，指定用户，指定猎刃的聊天内容")
    public AppPage chatDetail(@PathVariable("taskid") String taskid,
                              @PathVariable("hunterid") Long hunterid,
                              @PathVariable("userid") Long userid,
                              @PathVariable("page") Integer page,
                              @PathVariable("size") Integer size) {

        QueryTaskInterflow queryTaskInterflow = new QueryTaskInterflow(page, size);
        queryTaskInterflow.setSort(new Sort(Sort.Direction.DESC, Task.CREATE_TIME));
        queryTaskInterflow.setHunterId(hunterid);
        queryTaskInterflow.setUserId(userid);
        queryTaskInterflow.setTaskId(taskid);
        Page<UserHunterInterflow> query = userHunterInterflowService.findByQuery(queryTaskInterflow);
        List<UserHunterInterflow> content = query.getContent();
        List<ChatDto> data = ChatDto.initList(content);

        return AppPage.init(data, query);
    }

    @PostMapping("/{id:\\d+}")
    @ApiOperation(value = "保存聊天记录")
    public ChatDto saveChat(@PathVariable("id") Long id, @Valid @RequestBody ChatDtoReq addChat, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }
        UserHunterInterflow init = ChatDtoReq.init(addChat);
        init.setSender(id);
        UserHunterInterflow save = userHunterInterflowService.save(init);

        //发送给对方聊天信息
        ChatMsgDto chatMsgDto = new ChatMsgDto();
        BeanUtils.copyProperties(save, chatMsgDto);
        User user = userService.findOne(save.getUserId());
        User hunter = userService.findOne(save.getHunterId());

        chatMsgDto.setContent(save.getContext());
        chatMsgDto.setUserIcon(user.getHeadImg());
        chatMsgDto.setHunterIcon(hunter.getHeadImg());
        chatMsgDto.setCreateTime(TimestampHelper.today());

        if (save.getUserId() == save.getSender()) {//如果发送的信息是当前用户发送的
            chatMsgDto.setTitle("收到来自 " + user.getName() + " 的新消息");
            //发送给猎刃新新消息
            pushMsgService.pushNewChat(save.getHunterId(), chatMsgDto);
        } else {
            chatMsgDto.setTitle("收到来自 " + hunter.getName() + " 的新消息");
            //发送给用户新新消息
            pushMsgService.pushNewChat(save.getUserId(), chatMsgDto);
        }

        ChatDto result = ChatDto.init(save);
        result.setUserIcon(user.getHeadImg());
        result.setHunterIcon(hunter.getHeadImg());

        return result;
    }

}
