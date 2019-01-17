package com.tc.controller;

import com.google.gson.*;
import com.tc.db.entity.Condition;
import com.tc.db.entity.User;
import com.tc.dto.Result;
import com.tc.dto.condition.AddCondition;
import com.tc.dto.condition.ConditionKey;
import com.tc.dto.condition.ModifyCondition;
import com.tc.dto.condition.QueryCondition;
import com.tc.dto.user.QueryUser;
import com.tc.exception.DBException;
import com.tc.exception.ValidException;
import com.tc.service.ConditionService;
import com.tc.until.StringResourceCenter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 条件控制器
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/condition")
public class ConditionController {

    @Autowired
    private ConditionService conditionService;


    /**
     * 查询条件
     * @param queryCondition
     * @return
     */
    @PostMapping("/query")
    @ApiOperation("根据查询获取消息查看条件")
    public Result all(@RequestBody QueryCondition queryCondition){
        Page<Condition> result = conditionService.findByQuery(queryCondition);
        return Result.init(Condition.toListInIndex(result.getContent()),queryCondition);
    }

    /**
     * 获取消息查看条件详情
     * @param id
     * @return
     */
    @GetMapping("/detail/{id:\\d+}")
    @ApiOperation("根据编号获取条件详情")
    public Condition detail(@PathVariable("id") Long id){
        Condition condition = conditionService.findOne(id);
        return Condition.toDetail(condition);
    }

    /**
     * 获取用户条件的关键字
     * @return
     */
    @GetMapping("/key")
    @ApiOperation("获取用户条件的关键字")
    public List<ConditionKey> conditionKey(){
        return ConditionKey.init();
    }

    /**
     * 添加查询条件
     * @param addCondition
     * @param bindingResult
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("管理员添加消息的查询条件")
    public Condition add(@Valid @RequestBody AddCondition addCondition, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }

        //管理员选择根据用户ID的时候，需要判断value值是不是JSON对象，数据库中保存的是用户的查询条件
        if (addCondition.getName().equals(User.ID)){
            String json = addCondition.getValue();
            if (!hasJson(json)){
                throw new ValidException(StringResourceCenter.VALIDATOR_INSERT_FAILED);
            }
        }

        Condition condition = conditionService.save(AddCondition.toCondition(addCondition));
        if (condition == null){
            throw new DBException(StringResourceCenter.DB_INSERT_FAILED);
        }
        return Condition.toDetail(condition);
    }

    /**
     * 修改查询条件
     * @param modifyCondition
     * @param bindingResult
     * @return
     */
    @PutMapping("/update")
    @ApiOperation("修改消息的查询条件")
    public Condition update(@Valid @RequestBody ModifyCondition modifyCondition, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }

        //管理员选择根据用户ID的时候，需要判断value值是不是JSON对象，数据库中保存的是用户的查询条件
        if (modifyCondition.getName().equals(User.ID)){
            String json = modifyCondition.getValue();
            if (!hasJson(json)){
                throw new ValidException(StringResourceCenter.VALIDATOR_INSERT_FAILED);
            }
        }

        Condition old = conditionService.findOne(modifyCondition.getId());
        if (!ModifyCondition.toCondition(old,modifyCondition)){
            throw new ValidException(StringResourceCenter.VALIDATOR_UPDATE_ABNORMAL);
        }

        Condition condition = conditionService.save(old);
        if (condition == null){
            throw new DBException(StringResourceCenter.DB_INSERT_FAILED);
        }

        return Condition.toDetail(condition);
    }

    /**
     * 删除查询条件
     * @param id
     */
    @DeleteMapping("/delete/{id:\\d+}")
    @ApiOperation("删除消息的查询条件")
    public void delete(@PathVariable("id") Long id){
        boolean isSuccess = conditionService.deleteById(id);
        if (!isSuccess){
            throw new DBException(StringResourceCenter.DB_DELETE_ABNORMAL);
        }
    }

    private boolean hasJson(String value){
        try {
            new Gson().fromJson(value,QueryUser.class);
        }catch (Exception e){
            return false;
        }
        return true;
    }

}
