package hw8bankaccounts;

/**
 *
 * @author andreatranchina
 */
import java.io.*;

public class SavingsAccount extends Account{
    //no-arg construct
    public SavingsAccount() throws IOException{
        super();
    }
   
    //parametrized constructors
    public SavingsAccount (Depositor d, int integer, String strStatus, String strType,
            double doubleNum) throws IOException {
        super (d, integer, strStatus, strType, doubleNum);
    }
    
    public SavingsAccount (Account account) throws IOException{
        super(account.getDepositor(), account.getAccountNum(), account.getStatus(),
                account.getType(), account.getBalance());
    }
    
    //copy constructor
    public SavingsAccount(SavingsAccount saveAccount) {
        //super (saveAccount.getDepositor(), saveAccount.getAccountNum(), 
                //saveAccount.getStatus(), saveAccount.getType(), saveAccount.balance);
        //transactionHistory = new ArrayList<TransactionReceipt>(saveAccount.transactionHistory);//also protected
        super(saveAccount);
    }

    //overrides makeDeposit in account class
    public TransactionReceipt makeDeposit (TransactionTicket transTicket) 
            throws InvalidAmountException, AccountClosedException, CDMaturityDateException, IOException{
        //TransactionReceipt receipt = super.makeDeposit(ticket);
        if (getStatus().equals("closed")) { //closed account
            /*TransactionReceipt transReceipt = new TransactionReceipt(transTicket, false, 
                "Error: Account is currently closed - please reopen and try again", 
                getBalance(), getBalance());
            addTransaction(transReceipt);
            return transReceipt;*/
            throw new AccountClosedException();
            
        }else if (transTicket.getAmountOfTransaction()<=0) {//invalid amount  
            /*TransactionReceipt transReceipt = new TransactionReceipt(transTicket, false, 
                    ("Error: invalid amount entered - amount to deposit must be positive"), 
                    getBalance(), getBalance());
            addTransaction(transReceipt);
            return transReceipt;*/
            throw new InvalidAmountException();
        
        }else {//valid amount, not a CD account
            TransactionReceipt transReceipt = new TransactionReceipt (transTicket, true,
                        "Transaction Successful", getBalance(), 
                        transTicket.getAmountOfTransaction() + getBalance());
            
            balance = getBalance() + transTicket.getAmountOfTransaction();
            addTransaction(transReceipt);
            return transReceipt;    
        }
    }
    
    //overrides makeWithdrawal in Account class
    public TransactionReceipt makeWithdrawal (TransactionTicket transTicket) 
            throws AccountClosedException, InvalidAmountException, 
            CDMaturityDateException, InsufficientFundsException, IOException{
        if (getStatus().equals("closed")) {
            /*TransactionReceipt transReceipt = new TransactionReceipt(transTicket, false, 
                "Error: Account is currently closed - please reopen and try again", 
                getBalance(), getBalance());
            addTransaction(transReceipt);
            return transReceipt;  */
            throw new AccountClosedException();
        }
        
        else if (transTicket.getAmountOfTransaction()<= 0) {//invalid amount 
            /*TransactionReceipt transReceipt = new TransactionReceipt(transTicket, false, 
                    ("Error: invalid amount entered - amount to withdraw must be positive"), 
                    getBalance(), getBalance());
            
            addTransaction(transReceipt);
            return transReceipt;*/
            throw new InvalidAmountException();
            
        } else if (getBalance()<transTicket.getAmountOfTransaction()){//insufficient funds in account
            /*TransactionReceipt transReceipt = new TransactionReceipt(transTicket, false, 
                    ("Error: Insufficient Funds - Transaction Voided"), 
                    getBalance(), getBalance());
            addTransaction(transReceipt);
            return transReceipt;*/
            throw new InsufficientFundsException();
            
        } else {//valid amount
            TransactionReceipt transReceipt = new TransactionReceipt (transTicket, true,
                    "Transaction Successful", getBalance(), 
                    getBalance() - transTicket.getAmountOfTransaction());
            
            balance -= transTicket.getAmountOfTransaction();
            addTransaction(transReceipt);
            return transReceipt;    
        }
    }    
    
    //overrides toString in Account class
    public String toString (){
        String str = String.format("%s  %15d  %14s  %12s   $%7.2f  %13s\n", 
                    getDepositor(), getAccountNum(), getStatus(), getType(), getBalance(), "N/A");
        return str;
    }
    
    public String getAccountStringForFile() {
        //String string = "";
        String str = String.format("%-30s%-10d%-10s%-10s%-10.2f%-12s", 
                    getDepositor().getDepositorStringForFile(), getAccountNum(), getStatus(), getType(), 
                    getBalance(), "N/A");
        return str;
    }
}
