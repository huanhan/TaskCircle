package com.tc.dto.authority;

import com.tc.db.entity.Authority;
import com.tc.db.entity.Resource;
import com.tc.service.AuthorityService;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Cyg
 * 修改权限的交互
 */
public class ModifyAuthority extends BasicAuthority {

    @NotNull(message = "标识不能为空")
    private Long id;

    @NotBlank(message = "资源标识不能为空")
    @Size(max = 30,message = "最大值30")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Authority toAuthority(BasicAuthority addResource) {
        Authority authority = super.toAuthority(addResource);
        authority.setId(id);
        authority.setName(name);
        return authority;
    }

    public static BasicAuthority newBasicResource(){
        return new ModifyAuthority();
    }
}
