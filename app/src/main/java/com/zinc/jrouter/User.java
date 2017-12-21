package com.zinc.jrouter;

/**
 * @author Jiang zinc
 * @date 创建时间：2017/12/17
 * @description
 */

public class User {

    private String name;
    private String pwd;
    private String mail;

    public User(String name, String pwd, String mail) {
        this.name = name;
        this.pwd = pwd;
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
