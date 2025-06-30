package Controller;

import Model.*;
import Service.AccountService;
import Service.MessageService;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }


    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        //app.get("example-endpoint", this::exampleHandler);

        //1. POST localhost:8080/register
        app.post("/register", this::postInsertNewAccountHandler);

        //2. POST localhost:8080/login
        app.post("/login", this::postUserLoginHandler);

        //3. POST localhost:8080/messages
        app.post("/messages", this::postAddNewMessageHandler);

        //4. GET localhost:8080/messages
        app.get("/messages", this::getAllMessagesHandler);

        //5. GET localhost:8080/messages/{message_id}
        app.get("/{message_id}", this::getMsgByIdHandler);

        //6. DELETE localhost:8080/messages/{message_id}
        app.delete("/{message_id}", this::deleteMessageByIdHandler);

        //7. PATCH localhost:8080/messages/{message_id}
        app.patch("/{message_id}", this::updateMessageByIdHandler);

        //8. GET localhost:8080/accounts/{account_id}/messages
        app.get("/{account_id}/messages", this::getMessagesByUserHandler);

        

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     *
     * private void exampleHandler(Context context) {
     *     context.json("sample text");
     * }   
     */

    private void postInsertNewAccountHandler (Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.insertNewAccount(account);

        if(addedAccount!=null){
            ctx.json(addedAccount);
        }else{
            ctx.status(400);
        }
    }    

    private void postUserLoginHandler (Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loginAccount = accountService.userLogin(account);

        if(loginAccount!=null){
            ctx.json(loginAccount);
        }
        else{
            ctx.status(401);
        }
    }

    private void postAddNewMessageHandler (Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addNewMessage(message);

        if(addedMessage!=null){
            ctx.json(addedMessage);
        }
        else{
            ctx.status(400);
        }
    }

    
    private void getAllMessagesHandler (Context ctx) throws JsonProcessingException {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }


    private void getMsgByIdHandler (Context ctx) throws JsonProcessingException{
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMsgById(messageId);

        if(message != null){
            ctx.json(message);
        } 
        else{
            ctx.status(200);
        }
    }


    private void deleteMessageByIdHandler (Context ctx) throws JsonProcessingException{
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageById(messageId);

        if(deletedMessage!=null){
            ctx.json(deletedMessage);
        }
        else{
            ctx.status(200);
        }
    }


    private void updateMessageByIdHandler(Context ctx) throws JsonProcessingException{
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));

        ObjectMapper mapper = new ObjectMapper();
        Message updated_message = mapper.readValue(ctx.body(), Message.class);
        Message newMessage = messageService.updateMessageById(updated_message, messageId);

        if(newMessage!=null){
            ctx.json(newMessage);
        }
        else{
            ctx.status(400);
        }

    }
    
    
    private void getMessagesByUserHandler(Context ctx) throws JsonProcessingException{
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> userMessages = messageService.getMessagesByUser(accountId);

        ctx.json(userMessages);  // returns [] if list is empty
    }


}