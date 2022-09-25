package com.BusinessLogic;

import com.BindingModels.TrainerBindingModel;
import com.Interfaces.ITrainerStorage;
import com.ViewModels.TrainerViewModel;

import java.util.Arrays;
import java.util.List;

public class TrainerLogic {
    private final ITrainerStorage _trainerStorage;

    public TrainerLogic(ITrainerStorage trainerStorage) {
        _trainerStorage = trainerStorage;
    }

    public List<TrainerViewModel> read(TrainerBindingModel model) {
        if (model == null) {
            return _trainerStorage.getFullList();
        }
        if (model.id != null) {
            return Arrays.asList(_trainerStorage.getElement(model));
        }
        return _trainerStorage.getFilteredList(model);
    }

    public void createOrUpdate(TrainerBindingModel model) throws Exception {
        TrainerBindingModel trainerBindingModel = new TrainerBindingModel();
        trainerBindingModel.id = null;
        trainerBindingModel.phone = model.phone;
        TrainerViewModel element = _trainerStorage.getElement(trainerBindingModel);
        if (element != null && element.id != model.id) {
            throw new Exception("Уже есть тренер с такими данными");
        }
        if (model.id != null) {
            _trainerStorage.update(model);
        } else {
            _trainerStorage.insert(model);
        }
    }

    public void delete(TrainerBindingModel model) throws Exception {
        TrainerBindingModel trainerBindingModel = new TrainerBindingModel();
        trainerBindingModel.id = model.id;
        TrainerViewModel element = _trainerStorage.getElement(trainerBindingModel);
        if (element == null) {
            throw new Exception("Тренер не найден");
        }
        _trainerStorage.delete(model);
    }
}