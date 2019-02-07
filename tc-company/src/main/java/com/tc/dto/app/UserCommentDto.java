package com.tc.dto.app;

import com.tc.dto.comment.BasicComment;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * 用户评论猎刃
 */
public class UserCommentDto extends BasicComment {

    //评价用户的内容
    @NotEmpty(message = "评价用户内容不能为空")
    private String content;

    //用户的星星数
    @NotNull(message = "评分不能为空")
    private Float start;

    //被评价的猎刃任务id
    @NotEmpty(message = "猎刃任务id不能为空")
    private String hunterTaskId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Float getStart() {
        return start;
    }

    public void setStart(Float start) {
        this.start = start;
    }

    public String getHunterTaskId() {
        return hunterTaskId;
    }

    public void setHunterTaskId(String hunterTaskId) {
        this.hunterTaskId = hunterTaskId;
    }
}
