package com.tc.controller;

import com.google.gson.Gson;
import com.tc.db.entity.Message;
import com.tc.db.entity.User;
import com.tc.db.enums.MessageState;
import com.tc.db.enums.UserCategory;
import com.tc.db.enums.UserState;
import com.tc.dto.LongIds;
import com.tc.dto.Result;
import com.tc.dto.message.AddMessage;
import com.tc.dto.message.ModifyMessage;
import com.tc.dto.message.MyCondition;
import com.tc.dto.message.QueryMessage;
import com.tc.dto.trans.TransStates;
import com.tc.dto.user.QueryUser;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.AdminService;
import com.tc.service.MessageService;
import com.tc.service.UserService;
import com.tc.until.ListUtils;
import com.tc.until.PageRequest;
import com.tc.until.StringResourceCenter;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * 消息控制器
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/message")
public class MessageController {

    /**
     * 指定分割符
     */
    public static final String DEFAULT_SPLIT = ",";
    /**
     * 指定查看型允许的最大查看人数
     */
    public static final int MAX_LOOK = 100;


    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    /**
     * 根据查询条件，获取消息列表
     * @param queryMessage 查询条件
     * @return
     */
    @PostMapping("/query")
    @ApiOperation(value = "根据查询条件，获取消息列表")
    public Result all(HttpServletRequest request, @RequestBody QueryMessage queryMessage){
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        queryMessage.setSort(new Sort(new Sort.Order(Sort.Direction.DESC,Message.STATE),new Sort.Order(Sort.Direction.DESC,Message.CREATE_TIME)));
        Page<Message> query = messageService.findByQuery(queryMessage);
        return Result.init(Message.toListByIndex(query.getContent(),me),queryMessage.append(query.getTotalElements(),(long)query.getTotalPages()).clearSort());
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
     * @param bindingResult 检查异常信息
     * @return
     */
    @PostMapping
    @ApiOperation(value = "添加新消息")
    public Message add(HttpServletRequest request, @Valid @RequestBody AddMessage addMessage, BindingResult bindingResult){
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Message message = messageService.save(AddMessage.toMessage(addMessage,me));
        message.setCreation(adminService.findOne(me));
        return Message.toDetail(message);
    }

    /**
     * 修改消息
     * @param modifyMessage 修改后的消息信息
     * @param bindingResult 检查异常信息
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改消息")
    public Message update(HttpServletRequest request, @Valid @RequestBody ModifyMessage modifyMessage, BindingResult bindingResult){
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        Message old = messageService.findOne(modifyMessage.getId());
        if (old == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        if (!old.getCreationId().equals(me)){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        if (!ModifyMessage.isUpdate(old,modifyMessage)){
            throw new ValidException(StringResourceCenter.VALIDATOR_UPDATE_ABNORMAL);
        }
        Message message = messageService.save(old);
        return Message.toDetail(message);
    }

    /**
     * 发布消息
     * @param id
     */
    @GetMapping("/issue/{id:\\d+}")
    @ApiOperation(value = "管理员发布消息")
    public void issueMessage(HttpServletRequest request, @PathVariable("id") Long id){
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        Message query = messageService.findOne(id);
        if (query == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        if (!query.getCreationId().equals(me)){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        if (StringUtils.isEmpty(query.getLookCondition()) || query.getType() == null){
            throw new ValidException("请设置查看条件后，在发布消息");
        }
        boolean isSuccess = messageService.updateState(id,MessageState.NORMAL);
        if (!isSuccess){
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
    }

    /**
     * 撤回消息
     * @param id
     */
    @GetMapping("/out/{id:\\d+}")
    @ApiOperation(value = "管理员撤回消息")
    public void outMessage(HttpServletRequest request, @PathVariable("id") Long id){
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        Message query = messageService.findOne(id);
        if (query == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        if (StringUtils.isEmpty(query.getLookCondition()) || query.getType() == null){
            throw new ValidException("请设置查看条件后，在发布消息");
        }
        if (!query.getCreationId().equals(me)){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        boolean isSuccess = messageService.updateState(id,MessageState.STOP);
        if (!isSuccess){
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
    }

    /**
     * 删除单个消息
     * @param id 消息编号
     */
    @DeleteMapping("/{id:\\d+}")
    @ApiOperation(value = "删除单个消息")
    public void delete(HttpServletRequest request, @PathVariable("id") Long id){
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        Message query = messageService.findOne(id);
        if (query == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        if (!query.getCreationId().equals(me)){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        boolean isSuccess = messageService.deleteById(id);
        if (!isSuccess){
            throw new DBException(StringResourceCenter.DB_DELETE_ABNORMAL);
        }
    }

    /**
     * 设置消息可查看人的条件
     * @param condition
     * @param bindingResult
     */
    @PutMapping("/condition")
    @ApiOperation(value = "设置消息可查看人的条件")
    public Message addCondition(HttpServletRequest request,@Valid @RequestBody MyCondition condition, BindingResult bindingResult){
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }

        Message message = messageService.findOne(condition.getId());
        if (message == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        if (!message.getCreationId().equals(me)){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        String jsonContext = null;
        long count = 0;
        //对条件进行验证
        switch (condition.getMessageType()){
            case APPOINT:
                List<String> sIds = Arrays.asList(condition.getContext().split(DEFAULT_SPLIT));
                List<Long> lIds = ListUtils.to(sIds);
                if (ListUtils.isEmpty(lIds)){
                    throw new ValidException("指定人编号异常");
                }
                if (lIds.size() > MAX_LOOK){
                    throw new ValidException("超出限制，最大" + MAX_LOOK + "人");
                }
                jsonContext = new Gson().toJson(new LongIds(0L,lIds));
                count = userService.countByIds(lIds);
                break;
            case ALL:
                jsonContext = "{'str':'所有人均可查看'}";
                count = userService.count();
                break;
            case CONDITION:
                QueryUser queryUser;
                try{
                    queryUser = new Gson().fromJson(condition.getContext(),QueryUser.class);
                }catch (Exception ex){
                    throw new ValidException("Json格式异常");
                }
                jsonContext = condition.getContext();
                count = userService.countByQuery(queryUser);
                break;
            default:
                break;
        }

        if (jsonContext == null){
            throw new ValidException("没有要添加的条件");
        }
        condition.setContext(jsonContext);
        Message result = messageService.save(MyCondition.toMessage(message,condition));
        if (result == null){
            throw new DBException(StringResourceCenter.DB_UPDATE_ABNORMAL);
        }
        result.setSendCount(count);
        return Message.toDetail(result);
    }


    /**
     * 根据查询条件获取用户列表
     *
     * @param queryUser 用户查询条件
     * @return
     */
    @PostMapping("/condition/user")
    @ApiOperation(value = "根据查询条件获取创建消息时需要的用户列表")
    public Result all(@RequestBody QueryUser queryUser) {

        if (queryUser.getCategory() == null){
            queryUser.setCategories(UserCategory.byQueryUsers());
        }

        Page<User> queryUsers = userService.findByQueryUser(queryUser);
        return Result.init(User.toIndexAsList(queryUsers.getContent()), queryUser.append(queryUsers.getTotalElements(), (long) queryUsers.getTotalPages()));
    }


    /**
     * 获取可查看指定消息的用户列表
     * @param id
     * @return
     */
    @PostMapping("/condition/user/{id:\\d+}")
    @ApiOperation(value = "获取可查看指定消息的用户列表")
    public Result conditionUser(@PathVariable("id") Long id,@RequestBody PageRequest page){
        Message result = messageService.findOne(id);
        if (result == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        if (!StringUtils.isEmpty(result.getLookCondition()) && result.getType() != null){
            switch (result.getType()){
                case CONDITION:
                    QueryUser queryUser;
                    try {
                        queryUser = new Gson().fromJson(result.getLookCondition(),QueryUser.class);
                    }catch (Exception e){
                        throw new DBException("JSON转换异常");
                    }
                    queryUser.setPageSize(page.getPageSize());
                    queryUser.setPageNumber(page.getPageNumber());
                    Page<User> queryUsers = userService.findByQueryUser(queryUser);
                    return Result.init(User.toIndexAsList(queryUsers.getContent()),page.append(queryUsers.getTotalElements(),(long)queryUsers.getTotalPages()));
                case APPOINT:
                    LongIds longIds;
                    try {
                        longIds = new Gson().fromJson(result.getLookCondition(),LongIds.class);
                    }catch (Exception e){
                        throw new DBException("JSON转换异常");
                    }
                    Page<User> idsUsers = userService.findByIdsAndPage(longIds.getIds(),page);
                    return Result.init(User.toIndexAsList(idsUsers.getContent()),page.append(idsUsers.getTotalElements(),(long)idsUsers.getTotalPages()));
                case ALL:
                    throw new ValidException("所有人都可以看到该消息");
                case IS_NULL:
                    throw new ValidException("还未添加条件");
                default:
                    throw new ValidException("消息分类信息异常");
            }
        }else {
            throw new DBException("没有添加任何条件");
        }
    }

    /**
     * 查看管理员设置的条件
     * @param id
     * @return
     */
    @GetMapping("/condition/{id:\\d+}")
    @ApiOperation(value = "查看消息查看的条件")
    public Result conditionDetail(@PathVariable("id") Long id){
        Message result = messageService.findOne(id);
        if(result == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        if (!StringUtils.isEmpty(result.getLookCondition()) && result.getType() != null){
            switch (result.getType()){
                case CONDITION:
                    QueryUser queryUser;
                    try {
                        queryUser = new Gson().fromJson(result.getLookCondition(),QueryUser.class);
                    }catch (Exception e){
                        throw new DBException("JSON转换异常");
                    }
                    return Result.init(queryUser);
                case ALL:
                    return Result.init(result.getLookCondition());
                case APPOINT:
                    LongIds longIds;
                    try {
                        longIds = new Gson().fromJson(result.getLookCondition(),LongIds.class);
                    }catch (Exception e){
                        throw new DBException("JSON转换异常");
                    }
                    return Result.init(longIds);
                default:
                    break;
            }
        }else {
            throw new DBException("没有添加任何条件");
        }
        return null;
    }

    /**
     * 删除条件
     * @param id
     */
    @DeleteMapping("/condition/{id:\\d+}")
    @ApiOperation(value = "删除条件")
    public void deleteCondition(HttpServletRequest request,@PathVariable("id") Long id){
        Long me = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        Message message = messageService.findOne(id);
        if (message == null){
            throw new DBException(StringResourceCenter.DB_QUERY_FAILED);
        }
        if (!message.getCreationId().equals(me)){
            throw new ValidException(StringResourceCenter.VALIDATOR_AUTHORITY_FAILED);
        }
        boolean isSuccess = messageService.deleteCondition(id);
        if (!isSuccess){
            throw new DBException(StringResourceCenter.DB_DELETE_ABNORMAL);
        }
    }
}
