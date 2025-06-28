package DAO;

import java.util.*;
import java.sql.*;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class AuthorDAO {

// Requirements
// 1: Our API should be able to process new User registrations. 

    public Account insertNewAccount(Account acc){

        Connection connection = ConnectionUtil.getConnection();
        try{
            //SQL statement and logic
            String sql = "INSERT INTO account (username, password) VALUES (?, ?);";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //preparedStatements's setString methods
            ps.setString(1, acc.getUsername());
            ps.setString(2, acc.getPassword());

            ps.executeUpdate();

            ResultSet pkeyResultSet = ps.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);                
                return new Account(generated_account_id, acc.getUsername(), acc.getPassword());            
            }

        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

// 2: Our API should be able to process User logins.


    
}
