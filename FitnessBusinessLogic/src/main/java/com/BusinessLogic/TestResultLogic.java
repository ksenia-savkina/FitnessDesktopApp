package com.BusinessLogic;

import com.BindingModels.TestResultBindingModel;
import com.Interfaces.ITestResultStorage;
import com.ViewModels.TestResultViewModel;

import java.util.Arrays;
import java.util.List;

public class TestResultLogic {
    private final ITestResultStorage _testResultStorage;

    public TestResultLogic(ITestResultStorage testResultStorage) {
        _testResultStorage = testResultStorage;
    }

    public List<TestResultViewModel> read(TestResultBindingModel model) {
        if (model == null) {
            return _testResultStorage.getFullList();
        }
        if (model.id != null) {
            return Arrays.asList(_testResultStorage.getElement(model));
        }
        return _testResultStorage.getFilteredList(model);
    }

    public void createOrUpdate(TestResultBindingModel model) throws Exception {
        TestResultBindingModel testResultBindingModel = new TestResultBindingModel();
        testResultBindingModel.id = null;
        testResultBindingModel.date = model.date;
        TestResultViewModel element = _testResultStorage.getElement(testResultBindingModel);
        if (element != null && element.id != model.id) {
            throw new Exception("Уже существует результат в данное время");
        }
        if (model.id != null) {
            _testResultStorage.update(model);
        } else {
            _testResultStorage.insert(model);
        }
    }

    public void delete(TestResultBindingModel model) throws Exception {
        TestResultBindingModel testResultBindingModel = new TestResultBindingModel();
        testResultBindingModel.id = model.id;
        TestResultViewModel element = _testResultStorage.getElement(testResultBindingModel);
        if (element == null) {
            throw new Exception("Результат не найден");
        }
        _testResultStorage.delete(model);
    }
}