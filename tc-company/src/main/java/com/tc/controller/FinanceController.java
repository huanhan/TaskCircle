package com.tc.controller;

import com.tc.dto.finance.CompanyFinance;
import com.tc.dto.finance.IESource;
import com.tc.dto.finance.QueryFinance;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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
     * 获取公司收入与支出列表
     * @param queryFinance 查询条件
     * @param result 异常结果
     * @return
     */
    @GetMapping("/all")
    @ApiOperation(value = "获取公司收入与支出列表")
    public List<CompanyFinance> all(@Valid @RequestBody QueryFinance queryFinance, BindingResult result){
        return new ArrayList<>();
    }

    /**
     * 获取收支来源
     * @return
     */
    @GetMapping("/source")
    @ApiOperation(value = "查看收入来源")
    public List<IESource> ieSources(){
        return new ArrayList<>();
    }

    /**
     * 获取收支详情
     * @return
     */
    @GetMapping("/source/detail")
    @ApiOperation(value = "查看收支来源详情")
    public IESource ieSource(){
        return new IESource();
    }


}
