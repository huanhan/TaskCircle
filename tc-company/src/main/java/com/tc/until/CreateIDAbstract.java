package com.tc.until;

public abstract class CreateIDAbstract {

    protected String id;

    public void createID(){
       id = IdGenerator.INSTANCE.nextId();
       setUUID(id);
    }

    /**
     *
     * @param id
     */
    public abstract void setUUID(String id);


}
