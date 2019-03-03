package com.tc.dto.trans;

public class TransTaskBasic {
    private Trans state;
    private String id;
    private String name;
    private Trans creation;

    public TransTaskBasic() {
    }

    public TransTaskBasic(Trans state, String id, String name) {
        this.state = state;
        this.id = id;
        this.name = name;
    }

    public TransTaskBasic(Trans state, String id, String name, Trans creation) {
        this.state = state;
        this.id = id;
        this.name = name;
        this.creation = creation;
    }

    public Trans getState() {
        return state;
    }

    public void setState(Trans state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Trans getCreation() {
        return creation;
    }

    public void setCreation(Trans creation) {
        this.creation = creation;
    }
}
