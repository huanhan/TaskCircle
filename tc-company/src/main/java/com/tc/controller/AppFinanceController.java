package com.tc.controller;

import com.tc.db.entity.UserIeRecord;
import com.tc.db.entity.UserWithdraw;
import com.tc.dto.TimeScope;
import com.tc.dto.app.*;
import com.tc.dto.finance.QueryFinance;
import com.tc.dto.finance.QueryIE;
import com.tc.dto.user.CashPledge;
import com.tc.exception.DBException;
import com.tc.service.UserIeRecordService;
import com.tc.service.UserService;
import com.tc.service.UserWithdrawService;
import com.tc.until.StringResourceCenter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    @GetMapping("/transfer/{page:\\d+}/{size:\\d+}")
    @ApiOperation("获取我的转账记录")
    public AppPage allByTransfer(
            @PathVariable("page") int page,
            @PathVariable("size") int size,
            HttpServletRequest request) {
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        QueryIE queryIE = new QueryIE(page, size,new Sort(Sort.Direction.DESC,UserIeRecord.CREATE_TIME));
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
    @GetMapping("/{page:\\d+}/{size:\\d+}")
    @ApiOperation("获取我的充值或者提现记录")
    public AppPage allByFinance(
            @PathVariable("page") int page,
            @PathVariable("size") int size,
            HttpServletRequest request) {
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        QueryFinance queryFinance = new QueryFinance(page, size,new Sort(Sort.Direction.DESC,UserWithdraw.CREATE_TIME));
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
     * @return
     */
    @GetMapping("/money")
    @ApiOperation("获取我的押金列表")
    public List<CashPledgeAppDto> allByMoney(HttpServletRequest request) {
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        TimeScope timeScope = new TimeScope();
        timeScope.setPageSize(30);
        timeScope.setId(id);
        List<CashPledge> result = userService.findByCashPledgeAndUser(timeScope);

        return CashPledgeAppDto.toList(result);
    }

    /**
     * 新增提现记录，并提交审核
     */
    @PostMapping("/withdraw/add")
    @ApiOperation(value = "新增一条提现记录，并提交审核")
    public ResultApp add(HttpServletRequest request) {
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        UserWithdraw result = userWithdrawService.save(UserWithdraw.createWDraw(id));
        if (result == null) {
            throw new DBException("添加提现记录失败");
        }
        //保存提现记录时，需要扣除用户资金，并设置提现状态为提交审核状态
        return ResultApp.init("已通知管理员，管理员将售后与您联系");
    }

    @GetMapping("/pay/add")
    @ApiOperation(value = "新增一条充值记录，并提交审核")
    public ResultApp addPay(HttpServletRequest request){
        Long id = Long.parseLong(request.getAttribute(StringResourceCenter.USER_ID).toString());
        UserWithdraw result = userWithdrawService.save(UserWithdraw.createPay(id));
        if (result == null) {
            throw new DBException("添加充值记录失败");
        }
        return ResultApp.init("已通知管理员，管理员将售后与您联系");
    }
}
