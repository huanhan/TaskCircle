package com.tc.dto.user;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 修改密码
 * @author Cyg
 */
public class ModifyPassword {

    @NotNull
    @Min(1)
    private Long id;
    @NotEmpty
    @Length(max = 16)
    private String oldPassword;
    @NotEmpty
    @Length(max = 16)
    private String newPassword;
    @NotEmpty
    @Length(max = 16)
    private String queryPassword;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getQueryPassword() {
        return queryPassword;
    }

    public void setQueryPassword(String queryPassword) {
        this.queryPassword = queryPassword;
    }

    public void setEncoderPassword(String password) {
        this.newPassword = new BCryptPasswordEncoder().encode(password);
    }

}
