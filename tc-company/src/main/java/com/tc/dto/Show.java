package com.tc.dto;

/**
 * @author Cyg
 *
 * {id:name}的记录信息
 */
public class Show {
    private Long id;
    private String name;

    public Show() {
    }

    public Show(Long id, String name) {
        this.id = id;
        this.name = name;
    }

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
}
