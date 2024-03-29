package com.tc.dto.user;

import com.tc.db.entity.User;
import com.tc.db.enums.UserCategory;
import com.tc.db.enums.UserGender;
import com.tc.db.enums.UserState;
import com.tc.validator.Username;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author Cyg
 * 用户注册DTO
 */
public class RegisterUser implements Serializable {

    @NotBlank
    @Size(min = 8,message = "账户最少8位")
    @Username(message = "账户已存在")
    private String username;

    @NotBlank
    @Size(min = 6,message = "密码最少为6位")
    private String password;

    public RegisterUser() {
    }

    public RegisterUser(String username, String password) {
        this.username = username;
        this.password = password;
    }


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

    public User toUser(){
        User user = new User();
        user.setUsername(username);
        user.setEncoderPassword(password);
        user.setName(username.length() > 10 ? username.substring(0,9) : username);
        user.setCategory(UserCategory.NORMAL);
        user.setGender(UserGender.MAN);
        user.setState(UserState.NORMAL);
        return user;
    }
}
