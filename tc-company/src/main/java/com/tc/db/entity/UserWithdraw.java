package com.tc.db.entity;

import com.tc.db.enums.WithdrawState;
import com.tc.db.enums.WithdrawType;
import com.tc.dto.trans.Trans;
import com.tc.dto.trans.TransEnum;
import com.tc.until.ListUtils;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author Cyg
 * 用户提现记录实体
 */
@Entity
@Table(name = "user_withdraw", schema = "tc-company")
public class UserWithdraw implements Serializable {

    public static final String ID = "id";
    public static final String USER_ID = "userId";
    public static final String MONEY = "money";
    public static final String REALITY_MONEY = "realityMoney";
    public static final String CONTEXT = "context";
    public static final String STATE = "state";
    public static final String TYPE = "type";
    public static final String CREATE_TIME = "createTime";
    public static final String AUDIT_WITHDRAWS = "auditWithdraws";
    public static final String ADMIN_AUDIT_TIME = "adminAuditTime";
    public static final String AUDIT_PASS_TIME = "auditPassTime";
    public static final String USER = "user";

    private String id;
    private Long userId;
    private Long adminId;
    private Float money;
    private Float realityMoney;
    private String context;
    private WithdrawState state;
    private WithdrawType type;
    private Timestamp createTime;
    private Timestamp adminAuditTime;
    private Timestamp auditPassTime;
    private Collection<AuditWithdraw> auditWithdraws;
    private User user;
    private Admin admin;

    private Trans transState;
    private Trans transType;

    private List<TransEnum> transStates;
    private List<TransEnum> transTypes;
    private Boolean isAudit = false;

    public UserWithdraw() {
    }

    public UserWithdraw(Long userId) {
        this.userId = userId;
    }

    public static List<UserWithdraw> toIndexAsList(List<UserWithdraw> content, Long me) {
        if (!ListUtils.isEmpty(content)){
            content.forEach(userWithdraw -> {
                userWithdraw.setAuditWithdraws(null);
                if (userWithdraw.getUser()!= null){
                    userWithdraw.setUser(new User(userWithdraw.userId,userWithdraw.getUser().getName(),userWithdraw.getUser().getUsername()));
                }
                if (userWithdraw.getAdmin() != null){
                    if (me.equals(userWithdraw.getAdminId())){
                        userWithdraw.isAudit = true;
                    }
                    userWithdraw.setAdmin(new Admin(userWithdraw.adminId,userWithdraw.getAdmin().getUser()));
                }
                userWithdraw.setTransState(new Trans(userWithdraw.state.name(),userWithdraw.state.getState()));
                userWithdraw.setTransType(new Trans(userWithdraw.type.name(),userWithdraw.type.getType()));
            });
        }
        return content;
    }

    public static List<UserWithdraw> toIndexAsList(List<UserWithdraw> content) {
        if (!ListUtils.isEmpty(content)){
            content.forEach(userWithdraw -> {
                userWithdraw.setAuditWithdraws(null);
                if (userWithdraw.getUser()!= null){
                    userWithdraw.setUser(new User(
                            userWithdraw.userId,
                            userWithdraw.getUser().getName(),
                            userWithdraw.getUser().getUsername(),
                            userWithdraw.getUser().getCategory()));
                }
                if (userWithdraw.getAdmin() != null){
                    userWithdraw.setAdmin(new Admin(userWithdraw.adminId,userWithdraw.getAdmin().getUser()));
                }
                userWithdraw.setTransState(new Trans(userWithdraw.state.name(),userWithdraw.state.getState()));
                userWithdraw.setTransType(new Trans(userWithdraw.type.name(),userWithdraw.type.getType()));
            });
        }
        return content;
    }

    public static List<String> toIds(List<UserWithdraw> uws) {
        List<String> result = new ArrayList<>();
        if (!ListUtils.isEmpty(uws)){
            uws.forEach(task -> result.add(task.id));
        }
        return result;
    }

    public static UserWithdraw toDetail(UserWithdraw result) {
        if (result != null){
            if (result.getUser() != null){
                result.setUser(new User(result.getUserId(),result.getUser().getName(),result.getUser().getUsername()));
            }
            if (result.getAdmin() != null){
                result.setAdmin(new Admin(result.adminId,result.getAdmin().getUser()));
            }
            result.setAuditWithdraws(null);
            result.setTransState(new Trans(result.state.name(),result.state.getState()));
            result.setTransType(new Trans(result.type.name(),result.type.getType()));
        }

        return result;
    }



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
    @Column(name = "admin_id")
    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
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
    @Column(name = "reality_money")
    public Float getRealityMoney() {
        return realityMoney;
    }

    public void setRealityMoney(Float realityMoney) {
        this.realityMoney = realityMoney;
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
    @Column(name = "state")
    public WithdrawState getState() {
        return state;
    }

    public void setState(WithdrawState state) {
        this.state = state;
    }

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    public WithdrawType getType() {
        return type;
    }

    public void setType(WithdrawType type) {
        this.type = type;
    }

    @Basic
    @CreationTimestamp
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp timestamp) {
        this.createTime = timestamp;
    }

    @Basic
    @Column(name = "admin_audit_time")
    public Timestamp getAdminAuditTime() {
        return adminAuditTime;
    }

    public void setAdminAuditTime(Timestamp adminAuditTime) {
        this.adminAuditTime = adminAuditTime;
    }

    @Basic
    @Column(name = "audit_pass_time")
    public Timestamp getAuditPassTime() {
        return auditPassTime;
    }

    public void setAuditPassTime(Timestamp auditPassTime) {
        this.auditPassTime = auditPassTime;
    }

    @Transient
    public Trans getTransState() {
        return transState;
    }

    public void setTransState(Trans transState) {
        this.transState = transState;
    }

    @Transient
    public Trans getTransType() {
        return transType;
    }

    public void setTransType(Trans transType) {
        this.transType = transType;
    }

    @Transient
    public List<TransEnum> getTransStates() {
        return transStates;
    }

    public void setTransStates(List<TransEnum> transStates) {
        this.transStates = transStates;
    }

    @Transient
    public List<TransEnum> getTransTypes() {
        return transTypes;
    }

    public void setTransTypes(List<TransEnum> transTypes) {
        this.transTypes = transTypes;
    }

    @Transient
    public Boolean getAudit() {
        return isAudit;
    }

    public void setAudit(Boolean audit) {
        isAudit = audit;
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

    @ManyToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "user_id",insertable = false,updatable = false)
    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public UserWithdraw append(List<TransEnum> transStates, List<TransEnum> transTypes){
        this.setTransStates(transStates);
        this.setTransTypes(transTypes);
        return this;
    }

    public UserWithdraw append(List<AuditWithdraw> list){
        this.setAuditWithdraws(list);
        return this;
    }


}
