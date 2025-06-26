/**
 * You will need to design and create your own DAO classes from scratch. 
 * You should refer to prior mini-project lab examples and course material for guidance.
 * 
 * Please refrain from using a 'try-with-resources' block when connecting to your database. 
 * The ConnectionUtil provided uses a singleton, and using a try-with-resources will cause issues in the tests.
 */

package DAO;

import java.util.*;
import java.sql.*

import java.Model.Account;
import java.Model.Message;
import java.Util.ConnectionUtil;


public class SocialMediaAppDAO{


    public Account registerNewAccount(Account acc){

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

}
