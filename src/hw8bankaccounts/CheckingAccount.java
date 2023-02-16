package hw8bankaccounts;

/**
 *
 * @author andreatranchina
 */
import java.util.Calendar;
import java.io.*;

public class CheckingAccount extends Account{
    
    //null constructor
    public CheckingAccount() throws IOException{
        super();
    }
    
    //parametrized constructors
    public CheckingAccount(Depositor d, int integer, String strStatus, String strType,
            double doubleNum) throws IOException{
        super (d, integer, strStatus, strType, doubleNum);
    }
    
    public CheckingAccount(Account account) throws IOException{
        super(account.getDepositor(), account.getAccountNum(), account.getStatus(),
                account.getType(), account.getBalance());
    }
    
    //copy constructor
    public CheckingAccount(CheckingAccount checkAccount){
        //super (checkAccount.getDepositor(), checkAccount.getAccountNum(), 
                //checkAccount.getStatus(), checkAccount.getType(), checkAccount.balance);
        //transactionHistory = new ArrayList<TransactionReceipt>(checkAccount.transactionHistory);//also protected 
        super(checkAccount);
    }
    
    //overrides makeDeposit in Account superclass
    public TransactionReceipt makeDeposit (TransactionTicket transTicket) 
            throws AccountClosedException, InvalidAmountException, IOException{
        //TransactionReceipt receipt = super.makeDeposit(ticket);
        if (getStatus().equals("closed")) { //closed account
            /*TransactionReceipt transReceipt = new TransactionReceipt(transTicket, false, 
                "Error: Account is currently closed - please reopen and try again", 
                getBalance(), getBalance());
            addTransaction(transReceipt);
            return transReceipt;*/
            throw new AccountClosedException();
            
        }else if (transTicket.getAmountOfTransaction()<=0) {//invalid amount nonCD account   
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
    
    //overrides makeWithdrawal in Account superclass
    public TransactionReceipt makeWithdrawal (TransactionTicket transTicket)
            throws AccountClosedException, InvalidAmountException, InsufficientFundsException, IOException{
        
        if (getStatus().equals("closed")) {
            /*TransactionReceipt transReceipt = new TransactionReceipt(transTicket, false, 
                "Error: Account is currently closed - please reopen and try again", 
                getBalance(), getBalance());
            addTransaction(transReceipt);
            return transReceipt;*/
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
            
            if (getBalance()<2500){//need to apply $1.50 fee
                TransactionReceipt transReceipt = new TransactionReceipt (transTicket, true, 
                "Transaction Successful (balance less than $2500.00, additional $1.50 charged)", 
                getBalance(), getBalance() - transTicket.getAmountOfTransaction() - 1.50);
            
            balance -= (transTicket.getAmountOfTransaction() + 1.50);
            addTransaction(transReceipt);
            return transReceipt;
            
            } else {            
            TransactionReceipt transReceipt = new TransactionReceipt (transTicket, true,
            "Transaction Successful", getBalance(), getBalance() - transTicket.getAmountOfTransaction());
            
            balance -= transTicket.getAmountOfTransaction();
            addTransaction(transReceipt);
            return transReceipt; 
            }
        }
    }
    
    //overrides clearCheck in Account superclass
    public TransactionReceipt clearCheck (Check check, TransactionTicket transTicket)
            throws AccountClosedException, InvalidAmountException, CheckTooOldException, 
            PostDatedCheckException, InsufficientFundsException, IOException{
        
        if (getStatus().equals("closed")) {
            /*TransactionReceipt transReceipt = new TransactionReceipt(transTicket, false, 
                "Error: Account is currently closed - please reopen and try again", 
                getBalance(), getBalance());
            addTransaction(transReceipt);
            return transReceipt;*/
            throw new AccountClosedException();
        }
        
        /*if (!(getType().equalsIgnoreCase("checking"))){//not a checking account DONE IN ACCOUNT
            
            TransactionReceipt transReceipt = new TransactionReceipt (transTicket, false,
            "Error: account entered must be a checking account", getBalance(), getBalance());
            addTransaction(transReceipt);
            return transReceipt;
        }*/ 
        
        if (check.getCheckAmount() < 0) {//check amount invalid
            
            /*TransactionReceipt transReceipt = new TransactionReceipt (transTicket, false,
            "Error: invalid amount of check entered - must be positive", getBalance(), getBalance());
            addTransaction(transReceipt);
            return transReceipt; */
            throw new InvalidAmountException();
        }
        
        Calendar compareTime = Calendar.getInstance();
        compareTime.add(Calendar.MONTH, -6);
        
        if (check.getDateOfCheck().before(compareTime)){//check date not within past 6 months
            
            /*TransactionReceipt transReceipt = new TransactionReceipt (transTicket, false,
            "Error: date of check must be within the past 6 months", getBalance(), getBalance());
            addTransaction(transReceipt);
            return transReceipt;*/
            throw new CheckTooOldException();
        }
        
        if (check.getDateOfCheck().after(transTicket.getDateOfTransaction())){//check date not within past 6 months        
            throw new PostDatedCheckException();
        }    
            
        if (getBalance() < check.getCheckAmount()){//insufficient funds
            
            /*TransactionReceipt transReceipt = new TransactionReceipt (transTicket, false,
            "Error: insufficient funds - check bounced and service fee applied", 
            getBalance(), getBalance() - 2.50);

            balance -= 2.50;
            addTransaction(transReceipt);
            return transReceipt;*/
            balance -= 2.50;
            throw new InsufficientFundsException(2.50);
        }
        else {//successful transaction
            if (getBalance()<2500){//need to apply 1.50 fee
                TransactionReceipt transReceipt = new TransactionReceipt (transTicket, true, 
                "Transaction Successful (balance less than $2500.00, additional $1.50 charged)", 
                getBalance(), getBalance() - check.getCheckAmount() - 1.50);
                
                balance -= (check.getCheckAmount() + 1.50);
                addTransaction(transReceipt);
                return transReceipt;
            
            } else {
                TransactionReceipt transReceipt = new TransactionReceipt (transTicket, true,
                "Transaction Successful", getBalance(), getBalance() - check.getCheckAmount());
             
                balance -= check.getCheckAmount();
                addTransaction(transReceipt);
                return transReceipt;
            }           
        }        
    }//end clearCheck
    
    //overrides toString in Account superClass
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
