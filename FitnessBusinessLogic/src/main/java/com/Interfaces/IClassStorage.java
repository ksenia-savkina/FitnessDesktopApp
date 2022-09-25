package com.Interfaces;

import com.BindingModels.ClassBindingModel;
import com.ViewModels.ClassViewModel;

import java.util.List;

public interface IClassStorage {
    List<ClassViewModel> getFullList();

    List<ClassViewModel> getFilteredList(ClassBindingModel model);

    ClassViewModel getElement(ClassBindingModel model);

    void insert(ClassBindingModel model);

    void update(ClassBindingModel model) throws Exception;

    void delete(ClassBindingModel model) throws Exception;
}
