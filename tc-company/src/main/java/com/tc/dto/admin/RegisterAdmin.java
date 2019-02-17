package com.tc.dto.admin;

import com.tc.db.entity.Admin;
import com.tc.db.entity.User;
import com.tc.db.enums.AdminState;
import com.tc.db.enums.UserCategory;
import com.tc.db.enums.UserGender;
import com.tc.db.enums.UserState;
import com.tc.validator.Username;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

/**
 * 管理员注册
 * @author Cyg
 */
public class RegisterAdmin {
    @NotBlank
    @Size(min = 8,message = "账户最少8位")
    @Username(message = "账户已存在")
    private String username;

    @NotBlank
    @Size(min = 6,message = "密码最少为6位")
    private String password;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static Admin toAdmin(RegisterAdmin registerAdmin,Long id){
        Admin admin = new Admin();
        admin.setUser(toUser(registerAdmin));
        admin.setEntryTime(new Timestamp(System.currentTimeMillis()));
        admin.setCreateId(id);
        admin.setAdminState(AdminState.ON_GUARD);
        return admin;
    }

    public static User toUser(RegisterAdmin registerAdmin){
        User user = new User();
        user.setUsername(registerAdmin.getUsername());
        user.setEncoderPassword(registerAdmin.getPassword());
        user.setName(registerAdmin.getUsername().length() > 10 ? registerAdmin.getUsername().substring(0,9) : registerAdmin.getUsername());
        user.setCategory(UserCategory.ADMINISTRATOR);
        user.setGender(UserGender.MAN);
        user.setState(UserState.NORMAL);
        return user;
    }

}
