package Implements;

import DbUtil.Database;
import com.BindingModels.StatusBindingModel;
import com.Interfaces.IStatusStorage;
import com.ViewModels.StatusViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StatusStorage implements IStatusStorage {

    private final Connection connection;

    public StatusStorage() {
        this.connection = Database.getConnection();
    }

    @Override
    public List<StatusViewModel> getFullList() {
        List<StatusViewModel> statusViewModels = new ArrayList<>();
        String sql = "SELECT * FROM STATUS ORDER BY ID";
        Thread thread = new Thread(() -> {
            try (Statement statement = connection.createStatement()) {
                ResultSet set = statement.executeQuery(sql);
                while (set.next()) {
                    StatusViewModel statusViewModel = new StatusViewModel();
                    statusViewModel.id = set.getInt(1);
                    statusViewModel.name = set.getString(2);
                    statusViewModels.add(statusViewModel);
                }
            } catch (Exception e) {
                System.out.print(e.getMessage());
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusViewModels;
    }

    @Override
    public List<StatusViewModel> getFilteredList(StatusBindingModel model) {
        if (model == null) {
            return null;
        }
        List<StatusViewModel> statusViewModels = new ArrayList<>();
        String sql = "SELECT * FROM STATUS WHERE name = ?";
        Thread thread = new Thread(() -> {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, model.name);
                ResultSet set = statement.executeQuery();
                while (set.next()) {
                    StatusViewModel statusViewModel = new StatusViewModel();
                    statusViewModel.id = set.getInt(1);
                    statusViewModel.name = set.getString(2);
                    statusViewModels.add(statusViewModel);
                }
            } catch (Exception e) {
                System.out.print(e.getMessage());
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusViewModels;
    }

    @Override
    public StatusViewModel getElement(StatusBindingModel model) {
        if (model == null) {
            return null;
        }
        List<StatusViewModel> statusViewModels = new ArrayList<>();
        Thread thread = new Thread(() -> {
            String sql = "SELECT * FROM STATUS";
            if (model.id != null)
                sql += " WHERE id = ?";
            else if (model.name != null)
                sql += " WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                if (model.id != null)
                    statement.setInt(1, model.id);
                else if (model.name != null)
                    statement.setString(1, model.name);
                ResultSet set = statement.executeQuery();
                while (set.next()) {
                    StatusViewModel statusViewModel = new StatusViewModel();
                    statusViewModel.id = set.getInt(1);
                    statusViewModel.name = set.getString(2);
                    statusViewModels.add(statusViewModel);
                }
            } catch (Exception e) {
                System.out.print(e.getMessage());
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (statusViewModels.size() > 0)
            return statusViewModels.get(0);
        return null;
    }

    @Override
    public void insert(StatusBindingModel model) {
        String sql = "INSERT INTO STATUS VALUES (nextval('status_id_seq'), (?))";
        Thread thread = new Thread(() -> {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, model.name);
                statement.executeUpdate();
                connection.commit();
            } catch (Exception e) {
                System.out.print(e.getMessage());
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(StatusBindingModel model) throws Exception {
        StatusViewModel element = getElement(model);
        if (element == null) {
            throw new Exception("Элемент не найден");
        }
        String sql = "UPDATE STATUS SET name = (?) WHERE Id = ?";
        Thread thread = new Thread(() -> {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, model.name);
                statement.setInt(2, model.id);
                statement.executeUpdate();
                connection.commit();
            } catch (Exception e) {
                System.out.print(e.getMessage());
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(StatusBindingModel model) throws Exception {
        StatusViewModel element = getElement(model);
        if (element == null) {
            throw new Exception("Элемент не найден");
        }
        String sql = "DELETE FROM STATUS WHERE Id = ?";
        Thread thread = new Thread(() -> {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, model.id);
                statement.executeUpdate();
                connection.commit();
            } catch (Exception e) {
                System.out.print(e.getMessage());
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
