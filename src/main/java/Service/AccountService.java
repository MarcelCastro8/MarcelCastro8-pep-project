package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    
/** 
 *  no-args constructor for creating a new AccountService with a new AccountDAO
 */    
    public AccountService(){
        accountDAO = new AccountDAO();
    }

/*
 * Constructor for a AccountService when an accountDAO is provided
 */
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

/*
 * Method to add a new account using accountDAO object
 */
    public Account insertNewAccount(Account new_acc){
        
        return accountDAO.insertNewAccount(new_acc);

    }

/*
 * Method to provide user login using accountDAO object
 */
    public String userLogin(Account acc){

        return accountDAO.userLogin(acc);

    }


}
