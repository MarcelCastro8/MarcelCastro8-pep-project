package DAO;

import java.util.*;
import java.sql.*;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class AccountDAO {

/** Requirements
  * 1: Our API should be able to process new User registrations. 
  * As a user, I should be able to create a new Account on the endpoint POST localhost:8080/register. 
  * The body will contain a representation of a JSON Account, but will not contain an account_id.
  * The registration will be successful if and only if the username is not blank, the password is at least 4 characters long, 
  * and an Account with that username does not already exist. If all these conditions are met, the response body should contain a JSON of the Account, 
  * including its account_id. The response status should be 200 OK, which is the default. The new account should be persisted to the database.
  * If the registration is not successful, the response status should be 400. (Client error)
  */


    public Account insertNewAccount(Account acc){

        if (acc == null || acc.getUsername().isBlank() || acc.getPassword().length() < 4) {
            return null;
        }

        Connection connection = ConnectionUtil.getConnection();
        try{
            // CHECKING IF USERNAME ALREADY EXISTS
            String checksql = "SELECT * FROM account WHERE username = ?;";
            PreparedStatement checkps = connection.prepareStatement(checksql);

            checkps.setString(1, acc.getUsername());
            
            ResultSet checkrs = checkps.executeQuery();
            if(checkrs.next()) {
                return null; // Username already exists
            }



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

/** 
  * 2: Our API should be able to process User logins.
  * As a user, I should be able to verify my login on the endpoint POST localhost:8080/login. 
  * The request body will contain a JSON representation of an Account, not containing an account_id. 
  * In the future, this action may generate a Session token to allow the user to securely use the site. We will not worry about this for now.
  * The login will be successful if and only if the username and password provided in the request body JSON match a real account existing on the database.
  * If successful, the response body should contain a JSON of the account in the response body, including its account_id.
  * The response status should be 200 OK, which is the default.
  * If the login is not successful, the response status should be 401. (Unauthorized)
  */

    public Account userLogin(Account account){
        
        if (account == null) return null;

        Connection connection = ConnectionUtil.getConnection();

        try{
            String sql = "SELECT * FROM account WHERE username=? AND password=?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            ResultSet rs = ps.executeQuery();


            if (rs.next()){
                int accountId = rs.getInt("account_id");
                String username = rs.getString("username"); 
                String password = rs.getString("password");
                return new Account(accountId, username, password);
            }     
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    
}
