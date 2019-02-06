package com.tc.dto.comment;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * 用户评论猎刃
 * @author Cyg
 */
public class HunterComment extends BasicComment{

    //评价用户的内容
    @NotEmpty(message = "评价用户内容不能为空")
    private String content;

    //用户的星星数
    @NotNull(message = "评分不能为空")
    private Float start;

    //被评价的猎刃任务id
    @NotEmpty(message = "猎刃任务id不能为空")
    private String hunterTaskId;

    //被评价的猎刃
    @NotNull(message = "猎刃id不能为空")
    private Long evaHunterId;

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

    public Long getEvaHunterId() {
        return evaHunterId;
    }

    public void setEvaHunterId(Long evaHunterId) {
        this.evaHunterId = evaHunterId;
    }
}
