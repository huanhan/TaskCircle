package com.tc.controller;

import com.tc.db.entity.Message;
import com.tc.dto.Result;
import com.tc.dto.message.AddMessage;
import com.tc.dto.message.ModifyMessage;
import com.tc.dto.message.QueryMessage;
import com.tc.service.MessageService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息控制器
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/message")
public class MessageController {

    @Autowired
    private MessageService messageService;



    /**
     * 根据查询条件，获取消息列表
     * @param queryMessage 查询条件
     * @return
     */
    @GetMapping("/all")
    @ApiOperation(value = "根据查询条件，获取消息列表")
    public Result all(QueryMessage queryMessage){
        Page<Message> query = messageService.findByQuery(queryMessage);
        return Result.init(Message.toListByIndex(query.getContent()),queryMessage);
    }

    /**
     * 获取消息详情
     * @param id 消息编号
     * @return
     */
    @GetMapping("/detail/{id:\\d+}")
    @ApiOperation(value = "获取消息详情")
    public Message detail(@PathVariable("id") Long id){
        Message result = messageService.findOne(id);
        return Message.toDetail(result);
    }

    /**
     * 添加新消息
     * @param addMessage 新的消息信息
     * @param result 检查异常信息
     * @return
     */
    @PostMapping
    @ApiOperation(value = "添加新消息")
    public Message add(@Valid @RequestBody AddMessage addMessage, BindingResult result){
        return new Message();
    }

    /**
     * 修改消息
     * @param modifyMessage 修改后的消息信息
     * @param result 检查异常信息
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改消息")
    public Message update(@Valid @RequestBody ModifyMessage modifyMessage, BindingResult result){
        return  new Message();
    }

    /**
     * 删除单个消息
     * @param id 消息编号
     */
    @DeleteMapping("/{id:\\d+}")
    @ApiOperation(value = "删除单个消息")
    public void delete(@PathVariable("id") Long id){

    }

    /**
     * 推送消息
     * @param id 消息编号
     */
    @GetMapping("/send/{id:\\d+}")
    @ApiOperation(value = "推送消息")
    public void sendMessage(@PathVariable("id") Long id){

    }

}
