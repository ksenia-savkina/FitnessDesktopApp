package com.Interfaces;

import com.BindingModels.TrainerBindingModel;
import com.ViewModels.TrainerViewModel;

import java.util.List;

public interface ITrainerStorage {
    List<TrainerViewModel> getFullList();

    List<TrainerViewModel> getFilteredList(TrainerBindingModel model);

    TrainerViewModel getElement(TrainerBindingModel model);

    void insert(TrainerBindingModel model);

    void update(TrainerBindingModel model) throws Exception;

    void delete(TrainerBindingModel model) throws Exception;
}