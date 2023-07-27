package com.example.proj.controller;

import com.example.proj.bean.UserBean;
import com.example.proj.response.userResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opensymphony.xwork2.ModelDriven;
import com.example.proj.handler.UserHandler;
import org.apache.http.HttpStatus;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Properties;
import java.util.function.Supplier;
import java.util.logging.FileHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UsersController implements ServletRequestAware, ServletResponseAware,ModelDriven<UserBean> {
    private Properties properties;
    private List<UserBean> total = new ArrayList<>();
    private UserHandler userHandler = new UserHandler();
    private UserBean userBean = new UserBean();
    private HttpServletRequest request;
    private HttpServletResponse response;
    private static final Logger LOGGER = Logger.getLogger(UsersController.class.getName());

    @Override
    public UserBean getModel() {
        return userBean;
    }
    @Override
    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }
    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    public void index(){
        try{
            userResponse userResponse = new userResponse(userHandler.listUsers());
            userHandler.Logreport().info("method:GET\nApi call=> api\\account\n");
            WriteResponse(HttpStatus.SC_OK,userResponse);
        }catch (Exception e){
            userHandler.Logreport().warning(e.getLocalizedMessage());
        }
    }

    public void create(){
        try{
            userResponse userResponse = new userResponse(userHandler.addUser(userBean));
            userHandler.Logreport().info("method:POST\nApi call=> api\\account?query=param");
            WriteResponse(HttpStatus.SC_OK,userResponse);
        }catch (Exception e){
            userHandler.Logreport().warning(e.getMessage());
        }
    }

    public void show(){
        try{
            userResponse userResponse = new userResponse(userHandler.listUsers().get(userBean.getId()-1));
            userHandler.Logreport().info("method:GET\nApi call=> api\\account\\id");
            WriteResponse(HttpStatus.SC_OK,userResponse);
        }catch (Exception e){
            userHandler.Logreport().warning(e.getCause()==null?"Invalid ID entered.\n":"No Id Exists.\n");
        }
    }

    public void update() {
        try{
            userResponse userResponse = new userResponse(userHandler.updateUser(userBean));
            userHandler.Logreport().info("method:PUT\nApi call=> api\\account\\id?query=param");
            WriteResponse(HttpStatus.SC_OK,userResponse);
        }catch (Exception e){
            userHandler.Logreport().warning(e.getCause()==null?"Invalid ID entered.\n":"No Id Exists.\n");
        }
    }

    public void destroy(){
        try{
            userResponse userResponse = new userResponse(userHandler.deleteUser(userBean));
            userHandler.Logreport().info("method:DELETE\nApi call=> api\\account\\id");
            WriteResponse(HttpStatus.SC_OK,userResponse);
        }catch (Exception e){
            userHandler.Logreport().warning(e.getCause()==null?"Invalid ID entered.\n":"No Id Exists.\n");
        }
    }

    public void WriteResponse(int responsecode,userResponse userResponse){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try{
            if(responsecode==HttpStatus.SC_OK){
                response.setStatus(responsecode);
                response.getWriter().print(gson.toJson(userResponse));
                response.getWriter().flush();
                userHandler.Logreport().info(gson.toJson(userResponse));
                System.out.println(gson.toJson(userResponse));
            }else{
                response.setStatus(HttpStatus.SC_EXPECTATION_FAILED);
                response.getWriter().print(gson.toJson(null));
                response.getWriter().flush();
                System.out.println(gson.toJson(gson.toJson(null)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}