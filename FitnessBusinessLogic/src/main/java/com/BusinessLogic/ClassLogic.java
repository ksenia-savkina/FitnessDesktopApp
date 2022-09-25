package com.BusinessLogic;

import com.BindingModels.ClassBindingModel;
import com.Interfaces.IClassStorage;
import com.ViewModels.ClassViewModel;

import java.util.Arrays;
import java.util.List;

public class ClassLogic {
    private final IClassStorage _classStorage;

    public ClassLogic(IClassStorage classStorage) {
        _classStorage = classStorage;
    }

    public List<ClassViewModel> read(ClassBindingModel model) {
        if (model == null) {
            return _classStorage.getFullList();
        }
        if (model.id != null) {
            return Arrays.asList(_classStorage.getElement(model));
        }
        return _classStorage.getFilteredList(model);
    }

    public void createOrUpdate(ClassBindingModel model) throws Exception {
        ClassBindingModel classBindingModel = new ClassBindingModel();
        classBindingModel.id = null;
        classBindingModel.date = model.date;
        ClassViewModel element = _classStorage.getElement(classBindingModel);
        if (element != null && element.id != model.id) {
            throw new Exception("Уже существует занятие в данное время");
        }
        if (model.id != null) {
            _classStorage.update(model);
        } else {
            _classStorage.insert(model);
        }
    }

    public void delete(ClassBindingModel model) throws Exception {
        ClassBindingModel classBindingModel = new ClassBindingModel();
        classBindingModel.id = model.id;
        ClassViewModel element = _classStorage.getElement(classBindingModel);
        if (element == null) {
            throw new Exception("Занятие не найдено");
        }
        _classStorage.delete(model);
    }
}
