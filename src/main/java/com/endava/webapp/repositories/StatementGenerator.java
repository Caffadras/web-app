package com.endava.webapp.repositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class StatementGenerator {
    @Value("${webapp.jdbc.url}")
    private String url;
    @Value("${webapp.jdbc.login}")
    private String login;
    @Value("${webapp.jdbc.password}")
    private String password;

    private Connection connection;

    public synchronized Connection getConnection(){
        if (connection == null){
            try{
                connection = DriverManager.getConnection(url, login, password);
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
        return connection;
    }
    public PreparedStatement createPrepared(String query, Object... params) throws SQLException {
        PreparedStatement preparedStatement = getConnection().prepareStatement(query);
        for(int i=1; i<=params.length; ++i){
            preparedStatement.setObject(i, params[i-1]);
        }
        return preparedStatement;
    }

    public PreparedStatement createPrepared(String query) throws SQLException {
        return getConnection().prepareStatement(query);
    }
}
