package com.tc.db.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 用户充值记录
 * @author Cyg
 */
@Entity
@Table(name = "user_pay", schema = "tc-company")
public class UserPay {
    private String id;
    private long money;
    private Timestamp createTime;
    private long userId;
    private User user;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "money")
    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
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
    @Column(name = "user_id")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserPay userPay = (UserPay) o;
        return money == userPay.money &&
                userId == userPay.userId &&
                Objects.equals(id, userPay.id) &&
                Objects.equals(createTime, userPay.createTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, money, createTime, userId);
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false,insertable = false,updatable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User userByUserId) {
        this.user = userByUserId;
    }
}
