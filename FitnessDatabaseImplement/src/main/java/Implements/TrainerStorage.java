package Implements;

import DbUtil.Database;
import com.BindingModels.TrainerBindingModel;
import com.Interfaces.ITrainerStorage;
import com.ViewModels.TrainerViewModel;
import org.postgresql.util.PGInterval;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TrainerStorage implements ITrainerStorage {

    private final Connection connection;

    public TrainerStorage() {
        this.connection = Database.getConnection();
    }

    @Override
    public List<TrainerViewModel> getFullList() {
        List<TrainerViewModel> trainerViewModels = new ArrayList<>();
        String sql = "SELECT * FROM TRAINER ORDER BY ID";
        Thread thread = new Thread(() -> {
            try (Statement statement = connection.createStatement()) {
                ResultSet set = statement.executeQuery(sql);
                while (set.next()) {
                    TrainerViewModel trainerViewModel = new TrainerViewModel();
                    trainerViewModel.id = set.getInt(1);
                    trainerViewModel.name = set.getString(2);
                    trainerViewModel.surname = set.getString(3);
                    trainerViewModel.phone = set.getString(4);
                    trainerViewModel.experience = (PGInterval) set.getObject(5);
                    trainerViewModels.add(trainerViewModel);
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
        return trainerViewModels;
    }

    @Override
    public List<TrainerViewModel> getFilteredList(TrainerBindingModel model) {
        if (model == null) {
            return null;
        }
        List<TrainerViewModel> trainerViewModels = new ArrayList<>();
        String sql = "SELECT * FROM TRAINER WHERE surname = ? and name = ?";
        Thread thread = new Thread(() -> {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, model.name);
                statement.setString(2, model.surname);
                ResultSet set = statement.executeQuery();
                while (set.next()) {
                    TrainerViewModel trainerViewModel = new TrainerViewModel();
                    trainerViewModel.id = set.getInt(1);
                    trainerViewModel.name = set.getString(2);
                    trainerViewModel.surname = set.getString(3);
                    trainerViewModel.phone = set.getString(4);
                    trainerViewModel.experience = (PGInterval) set.getObject(5);
                    trainerViewModels.add(trainerViewModel);
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
        return trainerViewModels;
    }

    @Override
    public TrainerViewModel getElement(TrainerBindingModel model) {
        if (model == null) {
            return null;
        }
        List<TrainerViewModel> trainerViewModels = new ArrayList<>();
        Thread thread = new Thread(() -> {
            String sql = "SELECT * FROM TRAINER";
            if (model.id != null)
                sql += " WHERE id = ?";
            else if (model.phone != null)
                sql += " WHERE phone = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                if (model.id != null)
                    statement.setInt(1, model.id);
                else if (model.phone != null)
                    statement.setString(1, model.phone);
                ResultSet set = statement.executeQuery();
                while (set.next()) {
                    TrainerViewModel trainerViewModel = new TrainerViewModel();
                    trainerViewModel.id = set.getInt(1);
                    trainerViewModel.name = set.getString(2);
                    trainerViewModel.surname = set.getString(3);
                    trainerViewModel.phone = set.getString(4);
                    trainerViewModel.experience = (PGInterval) set.getObject(5);
                    trainerViewModels.add(trainerViewModel);
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
        if (trainerViewModels.size() > 0)
            return trainerViewModels.get(0);
        return null;
    }

    @Override
    public void insert(TrainerBindingModel model) {
        String sql = "INSERT INTO TRAINER VALUES (nextval('trainer_id_seq'), (?), (?), (?), (?))";
        Thread thread = new Thread(() -> {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, model.name);
                statement.setString(2, model.surname);
                statement.setString(3, model.phone);
                statement.setObject(4, model.experience);
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
    public void update(TrainerBindingModel model) throws Exception {
        TrainerViewModel element = getElement(model);
        if (element == null) {
            throw new Exception("Тренер не найден");
        }
        String sql = "UPDATE TRAINER SET name = (?), surname = (?), phone = (?), experience = (?) WHERE Id = ?";
        Thread thread = new Thread(() -> {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, model.name);
                statement.setString(2, model.surname);
                statement.setString(3, model.phone);
                statement.setObject(4, model.experience);
                statement.setInt(5, model.id);
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
    public void delete(TrainerBindingModel model) throws Exception {
        TrainerViewModel element = getElement(model);
        if (element == null) {
            throw new Exception("Тренер не найден");
        }
        String sql = "DELETE FROM TRAINER WHERE Id = ?";
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
