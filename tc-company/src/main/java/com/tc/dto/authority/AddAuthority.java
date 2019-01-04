package com.tc.dto.authority;

import com.tc.db.entity.Authority;
import com.tc.db.entity.Resource;
import com.tc.service.impl.AuthorityServiceImpl;
import com.tc.service.impl.ResourceServiceImpl;
import com.tc.validator.Name;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Cyg
 * 添加资源交互
 */
public class AddAuthority extends BasicAuthority {

    @NotNull
    @Min(value = 1)
    private Long creation;

    @NotBlank(message = "权限名不能为空")
    @Size(max = 20,message = "最多20个字符")
    @Name(service = AuthorityServiceImpl.class,message = "已存在相同名称的权限")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreation() {
        return creation;
    }

    public void setCreation(Long creation) {
        this.creation = creation;
    }

    @Override
    public Authority toAuthority(BasicAuthority authority) {
        Authority result = super.toAuthority(authority);
        result.setName(name);
        return result;
    }

    public static BasicAuthority newBasicResource(){
        return new AddAuthority();
    }
}
