package com.tc.until;


import com.tc.dto.user.contact.AddContact;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtil {

    public static final String QQ = "[1-9][0-9]{4,14}";
    public static final String EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
    public static final String WEICHAT = "^[a-zA-Z0-9_-]{5,19}$";

    public static boolean isQQ(String context) {
        return context.matches(QQ);
    }

    public static boolean isMobile(String context) {
        return TelephoneUtil.isAlpha(context, TelephoneUtil.TelephoneType.Mobile);
    }

    public static boolean isTelphone(String context) {
        return TelephoneUtil.isAlpha(context, TelephoneUtil.TelephoneType.Telephone);
    }

    public static boolean isEmail(String context) {
        return context.matches(EMAIL);
    }

    public static boolean isWeiChat(String context) {
        return context.matches(WEICHAT);
    }

    public static boolean isAdd(AddContact addContact) {
        switch (addContact.getContactName()) {
            case QQ:
                return ValidateUtil.isQQ(addContact.getContact());
            case WCHART:
                return ValidateUtil.isWeiChat(addContact.getContact());
            case PHONT:
                return ValidateUtil.isMobile(addContact.getContact());
            case FL_PHONT:
                return ValidateUtil.isTelphone(addContact.getContact());
            case EMAIL:
                return ValidateUtil.isEmail(addContact.getContact());
            default:
                return false;
        }
    }

    /**
     * 判断字符串中是否包含特殊字符
     * @param str
     * @return
     */
    public static boolean isSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

}
