package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO msgDAO;  

/** 
 *  no-args constructor for creating a new MessageService with a new MessageDAO
 */     
    public MessageService(){
        msgDAO = new MessageDAO();
    }

/*
 * Constructor for a MessageService when an messageDAO is provided
 */    
    public MessageService(MessageDAO messageDAO){
        this.msgDAO = messageDAO;
    }

/*
 * Method to create a new message using MessageDAO object
 */
    public Message addNewMessage(Message msg){
        return msgDAO.createNewMessage(msg);
    }    


/*
 * Method to retrieve all messages persisted in the database using MessageDAO object
 */
    public List<Message> getAllMessages(){
        return msgDAO.getAllMessages();
    }


/*
 * Method to retrieve a particular message from the database related to an id
 * using MessageDAO object 
 */
    public Message getMsgById(int id){
        return msgDAO.getMessageByID(id);
    }

/*
 * Method to delete a particular message from the database related to an id
 * using MessageDAO object
 */
    public Message deleteMessageById(int id){
        return msgDAO.deleteMessageById(id);
    }


/*
 * Method to update a particular message from the database (if exist) related to an id
 * using MessageDAO object
 */    
    public Message updateMessageById(Message msg, int id){

        if(msgDAO.getMessageByID(id) != null){
             return msgDAO.updateMessageById(msg, id);
        }

        return null;
    }


/*
 * Method to retrieve all messages from a particular user according to his/her user id
 */
    public List<Message> getMessagesByUser(int user_id){
        return msgDAO.getMessagesByUser(user_id);
    }    

}
