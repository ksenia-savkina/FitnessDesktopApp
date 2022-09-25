package Implements;

import DbUtil.Database;
import com.BindingModels.ClientBindingModel;
import com.Interfaces.IClientStorage;
import com.ViewModels.ClientViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ClientStorage implements IClientStorage {
    private final Connection connection;

    public ClientStorage() {
        this.connection = Database.getConnection();
    }

    @Override
    public List<ClientViewModel> getFullList() {
        List<ClientViewModel> clientViewModels = new ArrayList<>();
        String sql = "SELECT CL.id, CL.name, CL.surname, CL.birthdate, CL.testRequest, CL.phone, CL.Login, " +
                " CL.Password, CL.StatusId, S.name AS StatusName" +
                " FROM CLIENT CL JOIN Status S on S.id = CL.StatusId" +
                " ORDER BY CL.testRequest";
        Thread thread = new Thread(() -> {
            try (Statement statement = connection.createStatement()) {
                ResultSet set = statement.executeQuery(sql);
                while (set.next()) {
                    ClientViewModel clientViewModel = new ClientViewModel();
                    clientViewModel.id = set.getInt(1);
                    clientViewModel.name = set.getString(2);
                    clientViewModel.surname = set.getString(3);
                    clientViewModel.birthdate = set.getDate(4);
                    clientViewModel.testRequest = set.getBoolean(5);
                    clientViewModel.phone = set.getString(6);
                    clientViewModel.login = set.getString(7);
                    clientViewModel.password = set.getString(8);
                    clientViewModel.statusId = set.getInt(9);
                    clientViewModel.statusName = set.getString(10);
                    clientViewModels.add(clientViewModel);
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
        return clientViewModels;
    }

    @Override
    public List<ClientViewModel> getFilteredList(ClientBindingModel model) {
        if (model == null) {
            return null;
        }
        List<ClientViewModel> clientViewModels = new ArrayList<>();
        String sql = "SELECT CL.id, CL.name, CL.surname, CL.birthdate, CL.testRequest, CL.phone, CL.Login, " +
                " CL.Password, CL.StatusId, S.name AS StatusName" +
                " FROM CLIENT CL JOIN Status S on S.id = CL.StatusId" +
                " WHERE CL.login = ?" +
                " ORDER BY CL.testRequest";
        Thread thread = new Thread(() -> {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, model.login);
                ResultSet set = statement.executeQuery();
                while (set.next()) {
                    ClientViewModel clientViewModel = new ClientViewModel();
                    clientViewModel.id = set.getInt(1);
                    clientViewModel.name = set.getString(2);
                    clientViewModel.surname = set.getString(3);
                    clientViewModel.birthdate = set.getDate(4);
                    clientViewModel.testRequest = set.getBoolean(5);
                    clientViewModel.phone = set.getString(6);
                    clientViewModel.login = set.getString(7);
                    clientViewModel.password = set.getString(8);
                    clientViewModel.statusId = set.getInt(9);
                    clientViewModel.statusName = set.getString(10);
                    clientViewModels.add(clientViewModel);
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
        return clientViewModels;
    }

    @Override
    public ClientViewModel getElement(ClientBindingModel model) {
        if (model == null) {
            return null;
        }
        List<ClientViewModel> clientViewModels = new ArrayList<>();
        Thread thread = new Thread(() -> {
            String sql = "SELECT CL.id, CL.name, CL.surname, CL.birthdate, CL.testRequest, CL.phone, CL.Login, " +
                    " CL.Password, CL.StatusId, S.name AS StatusName" +
                    " FROM CLIENT CL JOIN Status S on S.id = CL.StatusId";
            if (model.id != null)
                sql += " WHERE CL.id = ?";
            else if (model.login != null)
                sql += " WHERE CL.login = ? or CL.phone = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                if (model.id != null)
                    statement.setInt(1, model.id);
                else if (model.login != null) {
                    statement.setString(1, model.login);
                    statement.setString(2, model.phone);
                }
                ResultSet set = statement.executeQuery();
                while (set.next()) {
                    ClientViewModel clientViewModel = new ClientViewModel();
                    clientViewModel.id = set.getInt(1);
                    clientViewModel.name = set.getString(2);
                    clientViewModel.surname = set.getString(3);
                    clientViewModel.birthdate = set.getDate(4);
                    clientViewModel.testRequest = set.getBoolean(5);
                    clientViewModel.phone = set.getString(6);
                    clientViewModel.login = set.getString(7);
                    clientViewModel.password = set.getString(8);
                    clientViewModel.statusId = set.getInt(9);
                    clientViewModel.statusName = set.getString(10);
                    clientViewModels.add(clientViewModel);
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
        if (clientViewModels.size() > 0)
            return clientViewModels.get(0);
        return null;
    }

    @Override
    public void insert(ClientBindingModel model) {
        String sql = "INSERT INTO CLIENT VALUES (nextval('client_id_seq'), (?), (?), (?), (?), (?), (?), (?), (?))";
        Thread thread = new Thread(() -> {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, model.name);
                statement.setString(2, model.surname);
                statement.setDate(3, model.birthdate);
                statement.setBoolean(4, model.testRequest);
                statement.setString(5, model.phone);
                statement.setString(6, model.login);
                statement.setString(7, model.password);
                statement.setInt(8, model.statusId);
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
    public void update(ClientBindingModel model) throws Exception {
        ClientViewModel element = getElement(model);
        if (element == null) {
            throw new Exception("Клиент не найден");
        }
        String sql = "UPDATE CLIENT SET name = (?), surname = (?), birthdate = (?), testRequest = (?), phone = (?), login = (?), password = (?), statusId = (?) WHERE Id = ?";
        Thread thread = new Thread(() -> {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, model.name);
                statement.setString(2, model.surname);
                statement.setDate(3, model.birthdate);
                statement.setBoolean(4, model.testRequest);
                statement.setString(5, model.phone);
                statement.setString(6, model.login);
                statement.setString(7, model.password);
                statement.setInt(8, model.statusId);
                statement.setInt(9, model.id);
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
    public void delete(ClientBindingModel model) throws Exception {
        ClientViewModel element = getElement(model);
        if (element == null) {
            throw new Exception("Клиент не найден");
        }
        Thread thread = new Thread(() -> {
            try {
                String sql1 = "DELETE FROM CLIENT WHERE Id = ?";
                PreparedStatement statement = connection.prepareStatement(sql1);
                statement.setInt(1, model.id);
                statement.executeUpdate();
                String sql2 = "DELETE FROM TestResult WHERE ClientId = ?";
                statement = connection.prepareStatement(sql2);
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
