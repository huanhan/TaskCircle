package com.tc.dto.trans;

import java.util.List;

public class TransAdmin extends Trans {

    private List<Trans> trans;
    private List<Trans> admins;

    public TransAdmin() {
    }

    public TransAdmin(Object key, String value) {
        super(key, value);
    }

    public TransAdmin(Object key, String value, List<Trans> trans, List<Trans> admins) {
        super(key, value);
        this.trans = trans;
        this.admins = admins;
    }


    public List<Trans> getTrans() {
        return trans;
    }

    public void setTrans(List<Trans> trans) {
        this.trans = trans;
    }

    public List<Trans> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Trans> admins) {
        this.admins = admins;
    }

}
