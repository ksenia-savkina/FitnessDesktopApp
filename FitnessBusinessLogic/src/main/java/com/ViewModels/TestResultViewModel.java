package com.ViewModels;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TestResultViewModel {
    public Integer id;

    public boolean result;

    public Timestamp date;

    public int clientId;

    public String resultStr;
}
