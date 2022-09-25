package com.Interfaces;

import com.BindingModels.TestResultBindingModel;
import com.ViewModels.TestResultViewModel;

import java.util.List;

public interface ITestResultStorage {
    List<TestResultViewModel> getFullList();

    List<TestResultViewModel> getFilteredList(TestResultBindingModel model);

    TestResultViewModel getElement(TestResultBindingModel model);

    void insert(TestResultBindingModel model);

    void update(TestResultBindingModel model) throws Exception;

    void delete(TestResultBindingModel model) throws Exception;
}
