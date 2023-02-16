package hw8bankaccounts;

/**
 *
 * @author andreatranchina
 */
public class CheckTooOldException extends Exception{
    
    public CheckTooOldException(){
        super("Error: check too old - date of check must be within the past 6 months");
    }
}
