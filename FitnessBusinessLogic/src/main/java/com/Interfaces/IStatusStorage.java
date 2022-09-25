package com.Interfaces;

import com.BindingModels.StatusBindingModel;
import com.ViewModels.StatusViewModel;

import java.util.List;

public interface IStatusStorage {
    List<StatusViewModel> getFullList();

    List<StatusViewModel> getFilteredList(StatusBindingModel model);

    StatusViewModel getElement(StatusBindingModel model);

    void insert(StatusBindingModel model);

    void update(StatusBindingModel model) throws Exception;

    void delete(StatusBindingModel model) throws Exception;
}