package com.tc.controller;

import com.tc.db.entity.UserIeRecord;
import com.tc.db.entity.UserWithdraw;
import com.tc.dto.Result;
import com.tc.dto.TimeScope;
import com.tc.dto.app.AppPage;
import com.tc.dto.app.CashPledgeAppDto;
import com.tc.dto.app.TransferAppDto;
import com.tc.dto.app.UserWithdrawAppDto;
import com.tc.dto.finance.AddFinance;
import com.tc.dto.finance.Money;
import com.tc.dto.finance.QueryFinance;
import com.tc.dto.finance.QueryIE;
import com.tc.dto.user.CashPledge;
import com.tc.exception.ValidException;
import com.tc.service.UserIeRecordService;
import com.tc.service.UserService;
import com.tc.service.UserWithdrawService;
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
 * APP用户资金控制器
 *
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/app/finance")
public class AppFinanceController {

    @Autowired
    private UserIeRecordService userIeRecordService;

    @Autowired
    private UserWithdrawService userWithdrawService;

    @Autowired
    private UserService userService;

    /**
     * 获取我的转账记录
     * 根据UserIeRecord来
     *
     * @return
     */
    @GetMapping("/transfer/{page:\\d+}/{size:\\d+}/{id:\\d+}")
    @ApiOperation("获取我的转账记录")
    public AppPage allByTransfer(
            @PathVariable("page") int page,
            @PathVariable("size") int size,
            @PathVariable("id") Long id) {
        QueryIE queryIE = new QueryIE(page, size);
        queryIE.setMe(id);
        queryIE.setTo(id);
        Page<UserIeRecord> userIeRecords = userIeRecordService.findByQuery(queryIE);
        List<UserIeRecord> content = userIeRecords.getContent();
        return AppPage.init(TransferAppDto.toList(content), userIeRecords);
    }

    /**
     * 获取我的充值或者提现记录
     * 根据状态判断充值还是提现
     * 根据UserWithdraw
     *
     * @return
     */
    @GetMapping("/{page:\\d+}/{size:\\d+}/{id:\\d+}")
    @ApiOperation("获取我的充值或者提现记录")
    public AppPage allByFinance(
            @PathVariable("page") int page,
            @PathVariable("size") int size,
            @PathVariable("id") Long id) {
        QueryFinance queryFinance = new QueryFinance(page, size);
        queryFinance.setUserId(id);
        Page<UserWithdraw> userWithdrawPage = userWithdrawService.findByQueryFinance(queryFinance);
        List<UserWithdraw> content = userWithdrawPage.getContent();
        return AppPage.init(UserWithdrawAppDto.toList(content), userWithdrawPage);
    }

    /**
     * 获取我的当前的所有押金情况
     * 押金没有记录，只有当前正在使用的押金
     * 押金主要是发布任务时候需要使用的，所以只要把该用户的正在发布中的任务的押金取出来就可以了
     * 编号使用任务编号
     * 金额使用任务的押金
     * 创建时间使用任务的发布时间
     *
     * @param id
     * @return
     */
    @GetMapping("/money/{id:\\d+}")
    @ApiOperation("获取我的押金列表")
    public List<CashPledgeAppDto> allByMoney(@PathVariable("id") Long id) {

        TimeScope timeScope = new TimeScope();
        timeScope.setPageSize(30);
        timeScope.setId(id);
        List<CashPledge> result = userService.findByCashPledgeAndUser(timeScope);

        return CashPledgeAppDto.toList(result);
    }

    /**
     * 我的转账记录详情
     * 不需要，因为列表会展示
     * @param tid
     * @param id
     */
    /*@GetMapping("/transfer/detail/{tid:\\d+}/{id:\\d+}")
    @ApiOperation(value = "获取我的转账记录详情")
    public UserIeRecord transferDetail(@PathVariable("tid") String tid, @PathVariable("id") Long id) {

        return new UserIeRecord();
    }*/

    /**
     * 我的充值与提现记录详情
     *不需要，因为列表会展示
     * @param tid
     * @param id
     * @return
     */
    /*@GetMapping("/detail/{tid:\\d+}/{id:\\d+}")
    @ApiOperation(value = "获取我的充值与提现记录详情")
    public UserWithdraw financeDetail(@PathVariable("tid") String tid, @PathVariable("id") Long id) {
        return new UserWithdraw();
    }*/

    /**
     * 我的押金详情
     *不需要，因为列表会展示
     * @param tid
     * @param id
     * @return
     */
   /* @GetMapping("/money/detail/{tid:\\d+}/{id:\\d+}")
    @ApiOperation(value = "获取我的充值与提现记录详情")
    public Money moneyDetail(@PathVariable("tid") String tid, @PathVariable("id") Long id) {
        return new Money();
    }*/

    /**
     * 新增提现记录，并提交审核
     *
     * @param id
     * @param addFinance
     * @param bindingResult
     */
    @PostMapping("/add/{id:\\d+}")
    @ApiOperation(value = "新增一条提现记录，并提交审核")
    public void add(@PathVariable("id") Long id, @Valid @RequestBody AddFinance addFinance, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidException(bindingResult.getFieldErrors());
        }

        //保存提现记录时，需要扣除用户资金，并设置提现状态为提交审核状态
    }
}
