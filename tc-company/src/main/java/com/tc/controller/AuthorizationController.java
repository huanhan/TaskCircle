package com.tc.controller;

import com.tc.dto.LongIds;
import com.tc.dto.authorization.AdminAutRelation;
import com.tc.dto.authorization.UserAutRelation;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


/**
 * 授权控制器
 * @author Cyg
 */
@RestController
@ResponseStatus(code = HttpStatus.OK)
@RequestMapping(value = "/authorization")
public class AuthorizationController {


    /**
     * 获取所有用户类别，所有权限与两者之间的关联关系
     * @return
     */
    @GetMapping("/all/users")
    @ApiOperation(value = "在用户与权限关系管理界面中，一次性获取所有用户类别，所有权限与两者之间的关联关系")
    public UserAutRelation allByUser(){
        return new UserAutRelation();
    }

    /**
     * 通过勾选与取消选中的形式来设置用户类别与权限的映射关系
     * @param name 类别名
     * @param ids 权限列表
     */
    @PostMapping("/users/{name}")
    @ApiOperation(value = "设置用户类别对应的权限")
    public void setUserCategoryAuthority(@PathVariable("name") String name, @RequestBody LongIds ids){

    }

    /**
     * 获取所有管理员，所有权限与两者之间的关联关系
     * @return
     */
    @GetMapping("/all/admins")
    @ApiOperation(value = "在管理员与权限关系管理界面中，一次性获取所有管理员，所有权限与两者之间的关联关系")
    public AdminAutRelation allByAdmin(){
        return new AdminAutRelation();
    }

    /**
     * 通过勾选与取消选中的形式来设置管理员与权限的映射关系
     * @param id 管理员编号
     * @param ids 权限列表
     */
    @PostMapping("/admins/{id:\\d+}")
    @ApiOperation(value = "设置用户类别对应的权限")
    public void setAdminAuthority(@PathVariable("id") Long id, @RequestBody LongIds ids){

    }
}
