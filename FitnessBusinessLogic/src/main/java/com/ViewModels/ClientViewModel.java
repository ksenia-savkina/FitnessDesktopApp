package com.ViewModels;

import lombok.Data;

import java.sql.Date;

@Data
public class ClientViewModel {
    public Integer id;

    public String name;

    public String surname;

    public Date birthdate;

    public boolean testRequest;

    public String phone;

    public String login;

    public String password;

    public String statusName;

    public int statusId;

    public String testRequestStr;

    //public TreeMap<Integer, String> clientClasses;
}