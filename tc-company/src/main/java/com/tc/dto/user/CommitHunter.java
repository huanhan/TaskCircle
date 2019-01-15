package com.tc.dto.user;

import com.tc.db.entity.User;
import com.tc.db.enums.UserGender;
import com.tc.db.enums.UserIMGName;
import com.tc.db.enums.UserState;
import com.tc.until.ListUtils;
import com.tc.validator.IDCard;
import com.tc.validator.Phone;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 用户提交的成为猎刃的信息
 * @author Cyg
 */
public class CommitHunter {
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
    @NotEmpty
    @IDCard
    private String idCard;
    /**
     * 家庭住址
     */
    @NotEmpty
    @Length(max = 100,message = "家庭住址不能大于100个字")
    private String address;
    /**
     * 毕业学校
     */
    @NotEmpty
    @Length(max = 30,message = "学校名不能大于30个字")
    private String school;
    /**
     * 职业
     */
    @NotEmpty
    @Length(max = 20,message = "职业不能大于20个字")
    private String major;
    /**
     * 兴趣
     */
    @NotEmpty
    @Length(max = 100,message = "兴趣不能大于100个字")
    private String interest;
    /**
     * 简介
     */
    @NotEmpty
    @Length(max = 100,message = "简介不能大于100个字")
    private String intro;
    /**
     * 身高
     */
    @NotNull
    @Min(value = 10,message = "身高不能低于10厘米")
    @Max(value = 300,message = "身高不能高于300厘米")
    private Integer height;
    /**
     * 体重
     */
    @NotNull
    @Min(value = 10,message = "体重不能低于10斤")
    @Max(value = 300,message = "体重不能高于300斤")
    private Integer weight;
    /**
     * 生日
     */
    @NotNull
    @Past
    private Timestamp birthday;
    /**
     * 用户手机号码
     */
    @NotEmpty
    @Length(max = 11)
    @Phone
    private String phone;
    /**
     * 用户图片资料
     */
    @NotNull
    @Valid
    private List<CommitIMG> commitIMGS;



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

    public List<CommitIMG> getCommitIMGS() {
        return commitIMGS;
    }

    public void setCommitIMGS(List<CommitIMG> commitIMGS) {
        this.commitIMGS = commitIMGS;
    }

    public static User toUser(User user,CommitHunter commitHunter) {
        user.setId(commitHunter.getId());
        user.setName(commitHunter.getName());
        user.setGender(commitHunter.getGender());
        user.setIdCard(StringUtils.isEmpty(commitHunter.idCard) ? user.getIdCard() : commitHunter.idCard);
        user.setAddress(StringUtils.isEmpty(commitHunter.address) ? user.getAddress() : commitHunter.address);
        user.setSchool(StringUtils.isEmpty(commitHunter.school) ? user.getSchool() : commitHunter.school);
        user.setMajor(StringUtils.isEmpty(commitHunter.major) ? user.getMajor() : commitHunter.major);
        user.setInterest(StringUtils.isEmpty(commitHunter.interest) ? user.getInterest() : commitHunter.interest);
        user.setIntro(StringUtils.isEmpty(commitHunter.intro) ? user.getIntro() : commitHunter.intro);
        user.setHeight(commitHunter.height == null || commitHunter.height < 0 ? user.getHeight() : commitHunter.height);
        user.setWeight(commitHunter.weight == null || commitHunter.weight < 0 ? user.getWeight() : commitHunter.weight);
        user.setBirthday(commitHunter.birthday == null ? user.getBirthday() : commitHunter.birthday);
        user.setPhone(StringUtils.isEmpty(commitHunter.phone) ? user.getPhone() : commitHunter.phone);
        user.setState(UserState.AUDIT_HUNTER);
        if (!ListUtils.isEmpty(commitHunter.commitIMGS)){
            if (!ListUtils.isEmpty(user.getUserImgs())){
                //去掉相同(地址和图片类别相同)
                user.getUserImgs().forEach(userImg ->
                    commitHunter.commitIMGS.removeIf(
                            commitIMG ->
                                    commitIMG.getUserIMGName().equals(userImg.getImgName()) &&
                                            commitIMG.getUrl().equals(userImg.getUrlLocation()))
                );

                if (!ListUtils.isEmpty(commitHunter.commitIMGS)){
                    List<CommitIMG> news = new ArrayList<>();
                    //修改不同（图片类别相同，地址不同）
                    user.getUserImgs().forEach(userImg -> {
                        commitHunter.commitIMGS.forEach(commitIMG -> {
                            if (commitIMG.getUserIMGName().equals(userImg.getImgName()) && !commitIMG.getUrl().equals(userImg.getUrlLocation())){
                                userImg.setUrlLocation(commitIMG.getUrl());
                            }else if (!commitIMG.getUserIMGName().equals(userImg.getImgName())){
                                news.add(commitIMG);
                            }
                        });
                    });
                    //保存不同（图片类别不同，地址不同）
                    if (!ListUtils.isEmpty(news)) {
                        user.getUserImgs().addAll(CommitIMG.toList(user.getId(), news));
                    }
                }
            }else {
                user.setUserImgs(CommitIMG.toList(user.getId(), commitHunter.commitIMGS));
            }
        }
        return user;
    }
}
