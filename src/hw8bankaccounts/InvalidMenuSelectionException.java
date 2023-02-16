package hw8bankaccounts;

/**
 *
 * @author andreatranchina
 */
public class InvalidMenuSelectionException extends Exception{
    
    public InvalidMenuSelectionException(){
        super("Error: selection chosen is an invalid option - try again");
    }
    
    public InvalidMenuSelectionException(char choice){
        super("Error: " + choice + " is an invalid option - try again");
    }
    
}
