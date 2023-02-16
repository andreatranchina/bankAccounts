package hw8bankaccounts;

/**
 *
 * @author andreatranchina
 */
public class InvalidAccountException extends Exception {
    
    public InvalidAccountException(){
        super("Error: Account does not exist");
    }
    
    public InvalidAccountException(int accountNumber){
        super("Error: Account number " + accountNumber + " does not exist");
    }
}
