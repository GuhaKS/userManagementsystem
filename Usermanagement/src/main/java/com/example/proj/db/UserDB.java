package com.example.proj.db;

import com.example.proj.bean.UserBean;
import com.example.proj.entity.UserData;

import java.io.FileReader;
import java.sql.*;
import java.util.*;

public class UserDB {

    private Connection connection = null;
    private final String filepath = "D:\\AdventNet\\MickeyLite\\webapps\\Usermanagement\\usermanagementLog.properties";
    private List<UserData> users = new ArrayList<>();
    private Properties properties = new Properties();

    public void addUser(UserData userData){
        System.out.println(userData.getName());
        String sql = "INSERT INTO usermanagement (id, name, email, password) VALUES (?, ?, ?, ?)";
        try{
            connection = connectToDB();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userData.getId());
            statement.setString(2, userData.getName());
            statement.setString(3, userData.getEmail());
            statement.setString(4, userData.getPassword());
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<UserData> listUsers(){
        String sql = "SELECT * FROM usermanagement";
        connection = connectToDB();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                UserData userData = new UserData(resultSet.getInt("id"),resultSet.getString("name"),resultSet.getString("email"),resultSet.getString("password"));
                users.add(userData);
                Collections.sort(users, new Comparator<UserData>() {
                    public int compare(UserData o1, UserData o2) {
                        return Integer.compare(o1.getId(), o2.getId());
                    }
                });
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public void deleteUser(UserBean userBean){
        try {
            Statement statement = connectToDB().createStatement();

            statement.executeUpdate("DELETE FROM usermanagement WHERE id = "+userBean.getId());
            statement.close();
            connectToDB().close();
        }catch (Exception e){

        }
    }

    public void updateUser(UserData userData){
        try{
            PreparedStatement statement = connectToDB().prepareStatement("UPDATE usermanagement SET name = ?,email = ?,password = ? WHERE id = ?");
            statement.setString(1,userData.getName());
            statement.setString(2,userData.getEmail());
            statement.setString(3,userData.getPassword());
            statement.setInt(4,userData.getId());
            statement.executeUpdate();
        }catch (Exception e){

        }
    }
    public Connection connectToDB() {
        Connection connection = null;
        try {
            properties.load(new FileReader(filepath));
            Class.forName(properties.getProperty("driver"));
            connection = DriverManager.getConnection(properties.getProperty("jdbcurl"),properties.getProperty("user"),properties.getProperty("password"));
        } catch (Exception e) {
        }
        return connection;
    }

}
