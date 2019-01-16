package com.tc.dto.user;


import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 用户头像
 * @author Cyg
 */
public class ModifyUserHeader {

    @NotEmpty
    @URL
    private String header;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
