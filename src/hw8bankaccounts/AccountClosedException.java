package hw8bankaccounts;

/**
 *
 * @author andreatranchina
 */
public class AccountClosedException extends Exception{
    
    public AccountClosedException(){
        super("Error: Account is currently closed - please reopen and try again");
    }
    
}
