package com.BusinessLogic;

import com.BindingModels.StatusBindingModel;
import com.Interfaces.IStatusStorage;
import com.ViewModels.StatusViewModel;

import java.util.Arrays;
import java.util.List;

public class StatusLogic {
    private final IStatusStorage _statusStorage;

    public StatusLogic(IStatusStorage statusStorage) {
        _statusStorage = statusStorage;
    }

    public List<StatusViewModel> read(StatusBindingModel model) {
        if (model == null) {
            return _statusStorage.getFullList();
        }
        if (model.id != null) {
            return Arrays.asList(_statusStorage.getElement(model));
        }
        return _statusStorage.getFilteredList(model);
    }

    public void createOrUpdate(StatusBindingModel model) throws Exception {
        StatusBindingModel statusBindingModel = new StatusBindingModel();
        statusBindingModel.id = null;
        statusBindingModel.name = model.name;
        StatusViewModel element = _statusStorage.getElement(statusBindingModel);
        if (element != null && element.id != model.id) {
            throw new Exception("Уже есть статус с таким названием");
        }
        if (model.id != null) {
            _statusStorage.update(model);
        } else {
            _statusStorage.insert(model);
        }
    }

    public void delete(StatusBindingModel model) throws Exception {
        StatusBindingModel statusBindingModel = new StatusBindingModel();
        statusBindingModel.id = model.id;
        StatusViewModel element = _statusStorage.getElement(statusBindingModel);
        if (element == null) {
            throw new Exception("Статус не найден");
        }
        _statusStorage.delete(model);
    }
}