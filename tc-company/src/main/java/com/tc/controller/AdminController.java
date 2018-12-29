package com.tc.controller;

import com.tc.db.entity.Admin;
import com.tc.dto.Ids;
import com.tc.dto.Show;
import com.tc.dto.admin.AdminDetail;
import com.tc.dto.admin.ModifyAdmin;
import com.tc.dto.admin.QueryAdmin;
import com.tc.dto.admin.RegisterAdmin;
import com.tc.dto.authority.AuthorityDetail;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理员控制器
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/admin")
public class AdminController {
    /**
     * 查询管理员详情
     * @param id 管理员
     * @return
     */
    @GetMapping(value = "{id:\\d+}")
    @ApiOperation(value = "查看管理员的详情")
    public AdminDetail detail(@PathVariable("id") Long id){
        return new AdminDetail();
    }

    /**
     * 获取管理员列表，根据查询条件
     * @param queryAdmin 查询条件
     * @return
     */
    @GetMapping
    @ApiOperation(value = "获取管理员列表，根据查询条件")
    public List<Admin> all(@Valid @RequestBody QueryAdmin queryAdmin){
        return new ArrayList<>();
    }

    /**
     * 添加管理员
     * @param registerAdmin 管理员信息
     */
    @PostMapping
    @ApiOperation(value = "添加管理员")
    public Admin add(@Valid @RequestBody RegisterAdmin registerAdmin){
        return new Admin();
    }

    /**
     * 查看管理员拥有的所有权限
     * @param id 管理员编号
     * @return
     */
    @GetMapping("/authority/{id:\\d+}")
    @ApiOperation(value = "管理员权限列表")
    public List<Show> adminAuthority(@PathVariable("id") Long id){
        return new ArrayList<>();
    }


    /**
     * 移除管理员权限
     * @param id 管理员编号
     * @param ids 管理员权限列表
     */
    @DeleteMapping("/authority/{id:\\d+}")
    @ApiOperation(value = "移除管理员权限")
    public void removeAdminAuthority(@PathVariable("id") Long id, @RequestBody Ids ids){

    }

    /**
     * 删除管理员
     * @param id 管理员编号
     */
    @DeleteMapping("/{id:\\d+}")
    @ApiOperation(value = "删除管理员")
    public void removeAdmin(@PathVariable("id") Long id){

    }

    /**
     * 删除多个管理员
     * @param ids 管理员编号列表
     */
    @DeleteMapping
    @ApiOperation(value = "删除管理员")
    public void removeAdmin(@Valid @RequestBody Ids ids, BindingResult result){

    }

    /**
     * 修改管理员信息
     * @param modifyAdmin 管理员信息
     * @param result
     * @return
     */
    @PutMapping
    @ApiOperation(value = "修改管理员信息")
    public Admin update(@Valid @RequestBody ModifyAdmin modifyAdmin,BindingResult result){
        return new Admin();
    }
}
