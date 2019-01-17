package com.tc.dto.message;

import com.tc.db.entity.Admin;

import java.sql.Timestamp;

/**
 * 添加消息
 * @author Cyg
 */
public class AddMessage {

    private Long id;
    private Long creationId;
    private String context;
    private String title;
    private String type;
    private String state;

}
