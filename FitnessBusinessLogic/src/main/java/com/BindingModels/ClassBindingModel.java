package com.BindingModels;

import javafx.util.Pair;

import java.sql.Timestamp;
import java.util.TreeMap;

public class ClassBindingModel {
    public Integer id;

    public String name;

    public Timestamp date;

    public int trainerId;

    public TreeMap<Integer, Pair<String, String>> classClients;

    public TreeMap<Integer, String> classStatuses;
}