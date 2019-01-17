package com.tc.controller;

import com.tc.db.entity.UserWithdraw;
import com.tc.db.enums.WithdrawState;
import com.tc.dto.Result;
import com.tc.dto.finance.CompanyFinance;
import com.tc.dto.finance.IESource;
import com.tc.dto.finance.QueryFinance;
import com.tc.exception.ValidException;
import com.tc.service.UserWithdrawService;
import com.tc.until.FloatHelper;
import com.tc.until.TimestampHelper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 公司财务管理控制器
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/finance")
public class FinanceController {

    /**
     * 用户提现的提成
     */
    public static final Float VACUATE = 0.05F;


    @Autowired
    private UserWithdrawService userWithdrawService;


    /**
     * 获取公司收入与支出列表
     * 获取公司收入与支出时，查询方式不能空，审核通过时间不能空
     * @param queryFinance 查询条件
     * @return
     */
    @PostMapping("/all")
    @ApiOperation(value = "获取公司收入与支出列表")
    public Result all(@Valid @RequestBody QueryFinance queryFinance,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }

        //验证结束日期的有效性
        Timestamp end = TimestampHelper.endTimeByDateType(queryFinance.getDateType(),queryFinance.getAuditPassBegin(),queryFinance.getAuditPassEnd());

        //重新设置查询的结束日期与必要的查询内容
        queryFinance.setAuditPassEnd(end);
        queryFinance.setState(WithdrawState.SUCCESS);

        List<UserWithdraw> queryResult = userWithdrawService.findByQueryFinanceNotPage(queryFinance);
        List<CompanyFinance> result = CompanyFinance.getBy(queryResult,queryFinance.getDateType());
        result.sort(Comparator.comparing(CompanyFinance::getBegin));
        return Result.init(result,queryFinance);
    }

    /**
     * 获取收支来源
     * @return
     */
    @PostMapping("/source")
    @ApiOperation(value = "查看收支来源")
    public Result ieSources(@Valid @RequestBody QueryFinance queryFinance,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new ValidException(bindingResult.getFieldErrors());
        }
        //验证结束日期的有效性
        Timestamp end = TimestampHelper.endTimeByDateType(queryFinance.getDateType(),queryFinance.getAuditPassBegin(),queryFinance.getAuditPassEnd());

        //重新设置查询的结束日期与必要的查询内容
        queryFinance.setAuditPassEnd(end);
        queryFinance.setState(WithdrawState.SUCCESS);
        Page<UserWithdraw> result = userWithdrawService.findByQueryFinance(queryFinance);

        return Result.init(UserWithdraw.toIndexAsList(result.getContent()),queryFinance);
    }

    /**
     * 获取收支详情
     * @return
     */
    @GetMapping("/source/detail/{id:\\d+}")
    @ApiOperation(value = "查看收支来源详情")
    public UserWithdraw ieSource(@PathVariable("id") String id){
        UserWithdraw result = userWithdrawService.findOne(id);
        return UserWithdraw.toDetail(result);
    }


}
