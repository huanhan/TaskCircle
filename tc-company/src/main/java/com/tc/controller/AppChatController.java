package com.tc.controller;

import com.tc.db.entity.Task;
import com.tc.db.entity.User;
import com.tc.db.entity.UserHunterInterflow;
import com.tc.dto.Result;
import com.tc.dto.app.*;
import com.tc.dto.task.QueryTaskInterflow;
import com.tc.exception.ValidException;
import com.tc.service.PushMsgService;
import com.tc.service.UserHunterInterflowService;
import com.tc.service.UserService;
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
        List<ChatDto> data = ChatDto.init(content);

        return AppPage.init(data, query);
    }

    @PostMapping("/{id:\\d+}")
    @ApiOperation(value = "保存聊天记录")
    public ResultApp saveChat(@PathVariable("id") Long id, @Valid @RequestBody ChatDtoReq addChat, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }
        UserHunterInterflow save = userHunterInterflowService.save(ChatDtoReq.init(addChat));
        ChatMsgDto chatMsgDto = new ChatMsgDto();
        BeanUtils.copyProperties(save, chatMsgDto);
        if (id == save.getUserId()) {
            User one = userService.findOne(save.getUserId());
            chatMsgDto.setIcon(one.getHeadImg());
        } else {
            User one = userService.findOne(save.getHunterId());
            chatMsgDto.setIcon(one.getHeadImg());
        }
//        pushMsgService.pushNewChat("" + save.getHunterId(), chatMsgDto);
        pushMsgService.pushNotice("收到了聊天信息", chatMsgDto.getContext());
        return ResultApp.init("增加成功");
    }

}
