package hw8bankaccounts;

/**
 *
 * @author andreatranchina
 */
public class InvalidAmountException extends Exception{
    
    public InvalidAmountException(){
        super("Error: invalid amount entered - amount must be positive");
    }
}
