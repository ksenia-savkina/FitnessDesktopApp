package com.ViewModels;

import lombok.Data;
import org.postgresql.util.PGInterval;

@Data
public class TrainerViewModel {
    public Integer id;

    public String name;

    public String surname;

    public String phone;

    public PGInterval experience;

    public String experienceStr;
}