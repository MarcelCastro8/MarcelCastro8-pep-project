package DAO;

import java.util.*;
import java.sql.*;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

/** 3: Our API should be able to process the creation of new messages.
  * As a user, I should be able to submit a new post on the endpoint POST localhost:8080/messages. 
  * The request body will contain a JSON representation of a message, which should be persisted to the database, but will not contain a message_id.
  * The creation of the message will be successful if and only if the message_text is not blank, is not over 255 characters, and posted_by refers to a real,
  * existing user. If successful, the response body should contain a JSON of the message, including its message_id. 
  * The response status should be 200, which is the default. The new message should be persisted to the database.
  * If the creation of the message is not successful, the response status should be 400. (Client error)
  */

    public Message createNewMessage(Message mess){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?);";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, mess.getPosted_by());
            ps.setString(2, mess.getMessage_text());
            ps.setLong(3, mess.getTime_posted_epoch());

            ps.executeUpdate();

            return mess;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        return null;
    }


/** 4: Our API should be able to retrieve all messages.
  * As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.
  * The response body should contain a JSON representation of a list containing all messages retrieved from the database. 
  * It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default.
  */

    public List<Message> getAllMessages(){

        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try{
            String sql = "SELECT * FROM message;";
            PreparedStatement ps = connection.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message msg = new Message(rs.getInt("posted_by"), 
                                          rs.getString("message_text"),
                                          rs.getLong("time_posted_epoch"));
                messages.add(msg);                          
            }

        }
        catch(SQLException e){
            System.out.print(e.getMessage());
        }

        return messages;
    }


/**
  * 5: Our API should be able to retrieve a message by its ID.
  * As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages/{message_id}.
  * The response body should contain a JSON representation of the message identified by the message_id. 
  * It is expected for the response body to simply be empty if there is no such message. 
  * The response status should always be 200, which is the default. 
  */

    public Message getMessageByID(int id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM message WHERE message_id=?;";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message msg = new Message(rs.getInt("message_id"),
                                          rs.getInt("posted_by"),
                                          rs.getString("message_text"),
                                          rs.getLong("time_posted_epoch"));
                return msg;                              
            }
        }
        catch(SQLException e){
            System.out.print(e.getMessage());
        }

        return null;
    }





}
