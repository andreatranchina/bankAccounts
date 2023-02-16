package hw8bankaccounts;

/**
 *
 * @author andreatranchina
 */
public class PostDatedCheckException extends Exception{
    
    public PostDatedCheckException(){
        super("Error: check has future date - date of check must be within the past 6 months");
    }

}
