package DAO;

import java.util.*;
import java.sql.*;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

/** 
  * 3: Our API should be able to process the creation of new messages.
  * As a user, I should be able to submit a new post on the endpoint POST localhost:8080/messages. 
  * The request body will contain a JSON representation of a message, which should be persisted to the database, but will not contain a message_id.
  * The creation of the message will be successful if and only if the message_text is not blank, is not over 255 characters, and posted_by refers to a real,
  * existing user. If successful, the response body should contain a JSON of the message, including its message_id. 
  * The response status should be 200, which is the default. The new message should be persisted to the database.
  * If the creation of the message is not successful, the response status should be 400. (Client error)
  */

    public Message createNewMessage(Message msg){
        
        if(msg == null || msg.getMessage_text().isBlank() || msg.getMessage_text().length() > 255) {
            return null;
        }
        
        Connection connection = ConnectionUtil.getConnection();
        try{
            // Check if posted_by account exists
            String sql = "SELECT * FROM account WHERE account_id = ?;";
            PreparedStatement Ps = connection.prepareStatement(sql);

            Ps.setInt(1, msg.getPosted_by());
            ResultSet rsCheck = Ps.executeQuery();
            if (!rsCheck.next()) {
                return null;
            }

            String sql2 = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?);";
            PreparedStatement ps = connection.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, msg.getPosted_by());
            ps.setString(2, msg.getMessage_text());
            ps.setLong(3, msg.getTime_posted_epoch());

            ps.executeUpdate();

            ResultSet pkeys = ps.getGeneratedKeys();
            if (pkeys.next()) {
                msg.setMessage_id(pkeys.getInt(1));
            }

            return msg;
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        return null;
    }


/** 
  * 4: Our API should be able to retrieve all messages.
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
                Message msg = new Message(rs.getInt("message_id"),
                                          rs.getInt("posted_by"), 
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


/** 
  * 6: Our API should be able to delete a message identified by a message ID.
  * As a User, I should be able to submit a DELETE request on the endpoint DELETE localhost:8080/messages/{message_id}.
  * The deletion of an existing message should remove an existing message from the database. If the message existed, 
  * the response body should contain the now-deleted message. The response status should be 200, which is the default.
  * If the message did not exist, the response status should be 200, but the response body should be empty. 
  * This is because the DELETE verb is intended to be idempotent, ie, multiple calls to the DELETE endpoint should respond with the same type of response.
  */

    public Message deleteMessageById(int id){
        
        Connection connection = ConnectionUtil.getConnection();
        Message msg = getMessageByID(id);
       
        try{
            String sql = "DELETE FROM message WHERE message_id=?;";    
            PreparedStatement ps = connection.prepareStatement(sql);
           
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        catch(SQLException e){
            System.out.print(e.getMessage());
        }

        return msg;
    }

    /** 
      * 7: Our API should be able to update a message text identified by a message ID.
      * As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{message_id}.
      * The request body should contain a new message_text values to replace the message identified by message_id.
      * The request body can not be guaranteed to contain any other information.
      * The update of a message should be successful if and only if the message id already exists and the new message_text
      * is not blank and is not over 255 characters. If the update is successful, the response body should contain 
      * the full updated message (including message_id, posted_by, message_text, and time_posted_epoch), and the response status should be 200,
      * which is the default. The message existing on the database should have the updated message_text.
      * If the update of the message is not successful for any reason, the response status should be 400. (Client error)
      */

    public Message updateMessageById(Message msg, int id){
        
        if(msg == null || msg.getMessage_text().isBlank() || msg.getMessage_text().length() > 255) {
            return null;
        }

        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "UPDATE message SET message_text=? WHERE message_id=?;";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, msg.getMessage_text());
            ps.setInt(2, id);
            ps.executeUpdate();

            Message updated_msg = getMessageByID(id);
            return updated_msg;

        }
        catch(SQLException e){
            System.out.print(e.getMessage());
        }

        return null;
    }


    /**
      * 8: Our API should be able to retrieve all messages written by a particular user.
      * As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{account_id}/messages.
      * The response body should contain a JSON representation of a list containing all messages posted by a particular user, 
      * which is retrieved from the database. It is expected for the list to simply be empty if there are no messages. 
      * The response status should always be 200, which is the default.
      */

    public List<Message> getMessagesByUser(int userid){

        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message WHERE posted_by=?;";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, userid);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Message msg = new Message(rs.getInt("message_id"),
                                          rs.getInt("posted_by"),
                                          rs.getString("message_text"),
                                          rs.getLong("time_posted_epoch"));
                messages.add(msg);                          
            }

            return messages;

        }
        catch(SQLException e){
            System.out.print(e.getMessage());
        }

        return messages;
    }
}
