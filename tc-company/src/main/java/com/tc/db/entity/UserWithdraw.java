package com.tc.db.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Cyg
 * 用户提现记录实体
 */
@Entity
@Table(name = "user_withdraw", schema = "tc-company")
public class UserWithdraw implements Serializable {
    private String id;
    private Long userId;
    private Float money;
    private String state;
    private Collection<AuditWithdraw> auditWithdraws;
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
    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
    @Column(name = "state")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        UserWithdraw that = (UserWithdraw) o;
        return user.getId().equals(that.getUser().getId()) &&
                Double.compare(that.money, money) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(state, that.state);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, user.getId(), money, state);
    }

    @OneToMany(mappedBy = "userWithdraw")
    public Collection<AuditWithdraw> getAuditWithdraws() {
        return auditWithdraws;
    }

    public void setAuditWithdraws(Collection<AuditWithdraw> auditWithdrawsById) {
        this.auditWithdraws = auditWithdrawsById;
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
