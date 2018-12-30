package com.tc.controller;

import com.tc.db.entity.UserWithdraw;
import com.tc.dto.withdraw.QueryWithdraw;
import com.tc.dto.withdraw.WithdrawStatistics;
import com.tc.dto.withdraw.WithdrawStatisticsCondition;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户提现控制器
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/withdraw")
public class WithdrawController {


    /**
     * 提现的基本信息
     * @param id 提现编号
     * @return
     */
    @GetMapping("/detail/{id:\\d+}")
    @ApiOperation(value = "提现的基本信息")
    public UserWithdraw detail(@PathVariable("id") Long id){
        return new UserWithdraw();
    }


    /**
     * 获取收支统计
     * @param withdrawStatisticsCondition 统计条件
     * @param result 异常结果
     * @return
     */
    @GetMapping("/statistics")
    @ApiOperation(value = "所有用户收支统计信息")
    public WithdrawStatistics getWithdrawStatistics(@Valid @RequestBody WithdrawStatisticsCondition withdrawStatisticsCondition,
                                                    BindingResult result){
        return new WithdrawStatistics();
    }

    /**
     * 获取收入列表记录
     * @param queryWithdraw 查询条件
     * @param result 异常结果
     * @return
     */
    @GetMapping("/all")
    @ApiOperation(value = "所有用户的收支列表")
    public List<UserWithdraw> all(@Valid @RequestBody QueryWithdraw queryWithdraw,BindingResult result){
        return new ArrayList<>();
    }
}
