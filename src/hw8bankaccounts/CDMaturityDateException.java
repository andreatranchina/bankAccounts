package hw8bankaccounts;

/**
 *
 * @author andreatranchina
 */
public class CDMaturityDateException extends Exception{
    
    public CDMaturityDateException(){
        super("Error: CD not matured");
    }
}
