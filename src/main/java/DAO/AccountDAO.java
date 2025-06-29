package DAO;

import java.util.*;
import java.sql.*;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class AccountDAO {

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

/** 2: Our API should be able to process User logins.
 As a user, I should be able to verify my login on the endpoint POST localhost:8080/login. 
 The request body will contain a JSON representation of an Account, not containing an account_id. 
 In the future, this action may generate a Session token to allow the user to securely use the site. We will not worry about this for now.
 The login will be successful if and only if the username and password provided in the request body JSON match a real account existing on the database.
 If successful, the response body should contain a JSON of the account in the response body, including its account_id.
 The response status should be 200 OK, which is the default.
 If the login is not successful, the response status should be 401. (Unauthorized)
*/

    public String userLogin(Account account){
        Connection connection = ConnectionUtil.getConnection();
        Account acc2 = null;

        try{
            String sql = "SELECT * FROM account WHERE username=?, password=?;" ;
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                acc2 = new Account(rs.getString("username"), rs.getString("password"));
            }

            if(account.username.equals(acc2.getUsername()) && account.password.equals(acc2.getPassword())){
                return "User found, login succesful!";
            }
            else{
                return "User not found!";
            }
        }
        
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    
}
