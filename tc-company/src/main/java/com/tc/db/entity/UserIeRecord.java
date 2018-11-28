package com.tc.db.entity;

import com.tc.db.enums.IECategory;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author Cyg
 * 用户收支记录实体
 */
@Entity
@Table(name = "user_ie_record", schema = "tc-company", catalog = "")
public class UserIeRecord implements Serializable {
    private String id;
    private Timestamp createTime;
    private Float money;
    private String context;
    private IECategory ieCategory;
    private User userByMe;
    private User userByTo;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "money")
    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    @Basic
    @Column(name = "context")
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "ie_category")
    public IECategory getIeCategory() {
        return ieCategory;
    }

    public void setIeCategory(IECategory ieCategory) {
        this.ieCategory = ieCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        UserIeRecord that = (UserIeRecord) o;
        return userByMe.getId().equals(that.getUserByMe().getId()) &&
                Float.compare(that.getMoney(), money) == 0 &&
                userByTo.getId().equals(that.getUserByTo().getId()) &&
                Objects.equals(id, that.getId()) &&
                Objects.equals(createTime, that.getCreateTime()) &&
                Objects.equals(context, that.getContext()) &&
                Objects.equals(ieCategory, that.getIeCategory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userByMe.getId(), createTime, money, context, userByTo.getId(), ieCategory);
    }

    @ManyToOne
    @JoinColumn(name = "me", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public User getUserByMe() {
        return userByMe;
    }

    public void setUserByMe(User userByMe) {
        this.userByMe = userByMe;
    }

    @ManyToOne
    @JoinColumn(name = "to", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public User getUserByTo() {
        return userByTo;
    }

    public void setUserByTo(User userByTo) {
        this.userByTo = userByTo;
    }
}
