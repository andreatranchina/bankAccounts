package hw8bankaccounts;

/**
 *
 * @author andreatranchina
 */
public class InsufficientFundsException extends Exception{
    
    public InsufficientFundsException(){
        super("Error: Insufficient funds - transaction voided");
    }
    
    public InsufficientFundsException(double transactionFee){
         super(String.format("Error: Insufficient funds - transaction voided and "
                 + "$%.2f fee applied", transactionFee));
    }
    
}
