package com.tc.dto.admin;

import com.tc.db.entity.Admin;
import com.tc.db.entity.User;
import com.tc.db.entity.UserContact;
import com.tc.db.enums.AdminState;
import com.tc.db.enums.UserContactName;
import com.tc.db.enums.UserGender;
import com.tc.until.ListUtils;
import com.tc.validator.IDCard;
import com.tc.validator.NoSpecial;
import com.tc.validator.Phone;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 修改管理员信息需要的内容
 * @author Cyg
 */
public class ModifyAdmin {
    /**
     * 用户编号
     */
    @NotNull
    @Min(value = 1)
    private Long id;
    /**
     * 用户姓名
     */
    @NotBlank(message = "不能空")
    @Length(max = 10)
    private String name;
    /**
     * 用户性别
     */
    @NotNull(message = "不能空")
    private UserGender gender;
    /**
     * 身份证号码
     */
    @Length(max = 18)
    @IDCard
    private String idCard;
    /**
     * 家庭住址
     */
    @NoSpecial
    @Length(max = 100)
    private String address;
    /**
     * 毕业学校
     */
    @NoSpecial
    @Length(max = 30)
    private String school;
    /**
     * 职业
     */
    @NoSpecial
    @Length(max = 20)
    private String major;
    /**
     * 兴趣
     */
    @NoSpecial
    @Length(max = 100)
    private String interest;
    /**
     * 简介
     */
    @NoSpecial
    @Length(max = 100)
    private String intro;
    /**
     * 身高
     */
    @Max(999)
    private Integer height;
    /**
     * 体重
     */
    @Max(999)
    private Integer weight;
    /**
     * 生日
     */
    @Past
    private Timestamp birthday;
    /**
     * 用户手机号码
     */
    @Length(max = 16)
    @Phone
    private String phone;
    /**
     * 管理员状态
     */
    private AdminState adminState;
    /**
     * 管理员入职时间
     */
    private Timestamp entryTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserGender getGender() {
        return gender;
    }

    public void setGender(UserGender gender) {
        this.gender = gender;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public AdminState getAdminState() {
        return adminState;
    }

    public void setAdminState(AdminState adminState) {
        this.adminState = adminState;
    }

    public Timestamp getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Timestamp entryTime) {
        this.entryTime = entryTime;
    }

    public static Admin toAdmin(Admin admin,ModifyAdmin modifyAdmin){
        admin.setUser(toUser(admin.getUser(),modifyAdmin));
        admin.setUserId(modifyAdmin.getId());
        admin.setAdminState(modifyAdmin.adminState == null ? admin.getAdminState() : modifyAdmin.adminState);
        admin.setEntryTime(modifyAdmin.entryTime == null ? admin.getEntryTime() : modifyAdmin.entryTime);
        return admin;
    }

    public static User toUser(User user,ModifyAdmin modifyAdmin){
        user.setId(modifyAdmin.getId());
        user.setName(modifyAdmin.getName());
        user.setGender(modifyAdmin.getGender());
        user.setIdCard(StringUtils.isEmpty(modifyAdmin.idCard) ? user.getIdCard() : modifyAdmin.idCard);
        user.setAddress(StringUtils.isEmpty(modifyAdmin.address) ? user.getAddress() : modifyAdmin.address);
        user.setSchool(StringUtils.isEmpty(modifyAdmin.school) ? user.getSchool() : modifyAdmin.school);
        user.setMajor(StringUtils.isEmpty(modifyAdmin.major) ? user.getMajor() : modifyAdmin.major);
        user.setInterest(StringUtils.isEmpty(modifyAdmin.interest) ? user.getInterest() : modifyAdmin.interest);
        user.setIntro(StringUtils.isEmpty(modifyAdmin.intro) ? user.getIntro() : modifyAdmin.intro);
        user.setHeight(modifyAdmin.height == null || modifyAdmin.height < 0 ? user.getHeight() : modifyAdmin.height);
        user.setWeight(modifyAdmin.weight == null || modifyAdmin.weight < 0 ? user.getWeight() : modifyAdmin.weight);
        user.setBirthday(modifyAdmin.birthday == null ? user.getBirthday() : modifyAdmin.birthday);
        if (StringUtils.isNotEmpty(modifyAdmin.phone)){
            user.setPhone(modifyAdmin.getPhone());
            if (ListUtils.isNotEmpty(user.getUserContacts())){
                AtomicBoolean isSelect = new AtomicBoolean(false);
                user.getUserContacts().forEach(userContact -> {
                    if (userContact.getContactName().equals(UserContactName.PHONT)){
                        isSelect.set(true);
                        userContact.setContact(user.getPhone());
                    }
                });
                if (!isSelect.get()){
                    List<UserContact> list = new ArrayList<>();
                    list.add(new UserContact(user.getId(),UserContactName.PHONT,user.getPhone()));
                    user.setUserContacts(list);
                }
            }else {
                List<UserContact> list = new ArrayList<>();
                list.add(new UserContact(user.getId(),UserContactName.PHONT,user.getPhone()));
                user.setUserContacts(list);
            }
        }
        return user;
    }
}
