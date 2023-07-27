package com.example.proj.handler;

import com.example.proj.bean.UserBean;
import com.example.proj.controller.UsersController;
import com.example.proj.db.UserDB;
import com.example.proj.entity.UserData;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class UserHandler {

    private final String filepath = "D:\\AdventNet\\MickeyLite\\webapps\\Usermanagement\\usermanagementLog.properties";
    private Logger logger ;
    private Properties properties = new Properties();
    private UserDB userDB = new UserDB();
    public List<UserData> listUsers(){
        List<UserData> data = new ArrayList<>();
        data = userDB.listUsers();
        return data;
    }

    public UserData addUser(UserBean userBean){
        System.out.println(userBean.getName());
        UserData userData = new UserData();
        userData.setId(listUsers().size()+1);
        userData.setName(userBean.getName());
        userData.setEmail(userBean.getEmail());
        userData.setPassword(userBean.getPassword());
        userDB.addUser(userData);
        return userData;
    }

    public UserData deleteUser(UserBean userBean){
        UserData userData = new UserData(userBean.getId(),userBean.getName(),userBean.getName(),userBean.getPassword());
        userDB.deleteUser(userBean);
        return userData;
    }

    public UserData updateUser(UserBean userBean){
        UserData dataUpdate = listUsers().get(userBean.getId()-1);
        if (userBean.getName()!=null){
            dataUpdate.setName(userBean.getName());
        }
        if (userBean.getEmail()!=null){
            dataUpdate.setEmail(userBean.getEmail());
        }
        if (userBean.getPassword()!=null){
            dataUpdate.setPassword(userBean.getPassword());
        }
        System.out.println(dataUpdate.getEmail());
        userDB.updateUser(dataUpdate);
        return dataUpdate;
    }

    public Logger Logreport(){
        logger = Logger.getLogger(UsersController.class.getName());
        try{
            properties.load(new FileReader(filepath));
            FileHandler fileHandler = new FileHandler(properties.getProperty("Logpath"));
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        }catch (Exception e){

        }
        return logger;
    }

}
