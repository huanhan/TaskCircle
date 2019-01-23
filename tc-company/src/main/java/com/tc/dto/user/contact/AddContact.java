package com.tc.dto.user.contact;

import com.tc.db.entity.UserContact;
import com.tc.db.enums.UserContactName;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 添加/修改 联系方式
 * @author Cyg
 */
public class AddContact {

    @NotNull
    @Min(1)
    private Long userId;
    @NotNull
    private UserContactName contactName;
    @NotEmpty
    @Length(max = 30)
    private String contact;




    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserContactName getContactName() {
        return contactName;
    }

    public void setContactName(UserContactName contactName) {
        this.contactName = contactName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public static UserContact toContact(AddContact addContact) {
        UserContact userContact = new UserContact();
        userContact.setUserId(addContact.userId);
        userContact.setContact(addContact.contact);
        userContact.setContactName(addContact.getContactName());
        return userContact;
    }

    public static boolean toContact(UserContact query, AddContact addContact) {
        boolean isUpdate = false;
        if (!query.getContact().equals(addContact.contact)){
            query.setContact(addContact.contact);
            isUpdate = true;
        }
        return isUpdate;
    }
}
