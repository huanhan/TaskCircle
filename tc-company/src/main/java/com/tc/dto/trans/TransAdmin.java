package com.tc.dto.trans;

import java.util.List;

public class TransAdmin extends Trans {

    private List<Trans> trans;
    private List<TransOP> admins;

    public TransAdmin() {
    }

    public TransAdmin(Object key, String value) {
        super(key, value);
    }

    public TransAdmin(Object key, String value, List<Trans> trans, List<TransOP> admins) {
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

    public List<TransOP> getAdmins() {
        return admins;
    }

    public void setAdmins(List<TransOP> admins) {
        this.admins = admins;
    }

}
