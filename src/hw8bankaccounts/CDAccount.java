package hw8bankaccounts;

/**
 *
 * @author andreatranchina
 */
import java.util.Calendar;
import java.io.*;

public class CDAccount extends SavingsAccount{
    private Calendar maturityDate;
    
    //null constructor
    public CDAccount() throws IOException{
        super();
    }
    
    //parametrized constructors
    public CDAccount(Depositor d, int integer, String strStatus, String strType, 
            double doubleNum, String dateString) throws IOException{
        super(d, integer, strStatus, strType, doubleNum);
        maturityDate = Calendar.getInstance();
        maturityDate.clear();
        String[]dateArray = dateString.split("/");
        maturityDate.set(Integer.parseInt(dateArray[2]), 
                Integer.parseInt(dateArray[0])-1, Integer.parseInt(dateArray[1]));
    }
    
    public CDAccount(Depositor d, int integer, String strStatus, String strType, 
            double doubleNum, Calendar date) throws IOException{
        super(d, integer, strStatus, strType, doubleNum);
        maturityDate = date; 
    }
    
    public CDAccount (Account account, String dateString) throws IOException{
        super(account.getDepositor(), account.getAccountNum(), account.getStatus(),
                account.getType(), account.getBalance());
        maturityDate = Calendar.getInstance();
        maturityDate.clear();
        String[]dateArray = dateString.split("/");
        maturityDate.set(Integer.parseInt(dateArray[2]), 
                Integer.parseInt(dateArray[0])-1, Integer.parseInt(dateArray[1]));
    }
    
    //copy constructor
    public CDAccount (CDAccount cdAccount){
        super(cdAccount);
        maturityDate = cdAccount.getMaturityDate();
    }
    
    public Calendar getMaturityDate(){
        return maturityDate;
    }
    
    public String getMaturityDateString(){
        String str;
        str = String.format("%02d/%02d/%4d", maturityDate.get(Calendar.MONTH)+1, 
                maturityDate.get(Calendar.DAY_OF_MONTH), maturityDate.get(Calendar.YEAR));
        return str;
    }
    
    //overrides makeDeposit in SavingsAccount superclass
    public TransactionReceipt makeDeposit(TransactionTicket transTicket) 
            throws AccountClosedException, InvalidAmountException, CDMaturityDateException, IOException{        
        if (getStatus().equals("closed")) { //closed account
            /*TransactionReceipt transReceipt = new TransactionReceipt(transTicket, false, 
                "Error: Account is currently closed - please reopen and try again", 
                getBalance(), getBalance(), getMaturityDate());
            addTransaction(transReceipt);
            return transReceipt;*/
            throw new AccountClosedException();
        
        }else if (transTicket.getAmountOfTransaction()<= 0) {//invalid amount CD account
            /*TransactionReceipt transReceipt = new TransactionReceipt(transTicket, false, 
                    ("Error: invalid amount entered - amount to deposit must be positive"), 
                    getBalance(), getBalance(), getMaturityDate());
            addTransaction(transReceipt);
            return transReceipt;*/
            throw new InvalidAmountException();
        
        }else if (transTicket.getDateOfTransaction().before(getMaturityDate())){ //valid amount, CD not mature
            
            /*TransactionReceipt transReceipt = new TransactionReceipt (transTicket, 
                    false, "Error: CD not matured", getBalance(), getBalance(),
                    getMaturityDate());
            addTransaction(transReceipt);
            return transReceipt;*/
            throw new CDMaturityDateException();
        
        } else {//valid amount, mature CD
            Calendar postMaturityDate = Calendar.getInstance();
            postMaturityDate.add(Calendar.MONTH, transTicket.getTermOfCD());
            maturityDate = postMaturityDate; //adjust to new maturity date           
            
            TransactionReceipt transReceipt = new TransactionReceipt (transTicket, 
                    true, "Transaction Successful", getBalance(), 
                    getBalance() + transTicket.getAmountOfTransaction(),
                    getMaturityDate());
            
            balance = getBalance() + transTicket.getAmountOfTransaction();
            
            addTransaction(transReceipt);
            //addToTotalAmount()
            return transReceipt;  
        }
    }
    
    //overrides makeWithdrawal in SavingsAccount superclass
    public TransactionReceipt makeWithdrawal (TransactionTicket transTicket) 
            throws AccountClosedException, InvalidAmountException, 
            CDMaturityDateException, InsufficientFundsException, IOException{
        
        if (getStatus().equals("closed")) {//account is closed
            /*TransactionReceipt transReceipt = new TransactionReceipt(transTicket, false, 
                "Error: Account is currently closed - please reopen and try again", 
                getBalance(), getBalance(), getMaturityDate());
            addTransaction(transReceipt);
            return transReceipt;*/
            throw new AccountClosedException();
        }
        
        else if (transTicket.getAmountOfTransaction()<= 0) {//invalid amount 
            /*TransactionReceipt transReceipt = new TransactionReceipt(transTicket, false, 
                    ("Error: invalid amount entered - amount to withdraw must be positive"), 
                    getBalance(), getBalance(), getMaturityDate());
            
            addTransaction(transReceipt);
            return transReceipt;*/
            throw new InvalidAmountException();
            
        } else if (getBalance()<transTicket.getAmountOfTransaction()){//insufficient funds in account
            /*TransactionReceipt transReceipt = new TransactionReceipt(transTicket, false, 
                    ("Error: Insufficient Funds - Transaction Voided"), 
                    getBalance(), getBalance(), getMaturityDate());
            addTransaction(transReceipt);
            return transReceipt;*/
            throw new InsufficientFundsException();
            
        } else if (getType().equals("CD") && 
            transTicket.getDateOfTransaction().before(getMaturityDate())){ //CD not mature
            
            /*TransactionReceipt transReceipt = new TransactionReceipt (transTicket, 
                    false, "Error: CD not matured", getBalance(), getBalance(),
                    getMaturityDate());
            addTransaction(transReceipt);
            return transReceipt;*/
            throw new CDMaturityDateException();
                
        
        } else {//(getType().equals("CD")) --> mature CD
            Calendar postMaturityDate = Calendar.getInstance();
            postMaturityDate.add(Calendar.MONTH, transTicket.getTermOfCD());
            maturityDate = postMaturityDate; //adjust to new maturity date                   
            
            TransactionReceipt transReceipt = new TransactionReceipt (transTicket, 
                    true, "Transaction Successful", getBalance(), 
                    getBalance() - transTicket.getAmountOfTransaction(),
                    getMaturityDate());
            
            balance -= transTicket.getAmountOfTransaction();
            addTransaction(transReceipt);
            return transReceipt;
        }
    }    
    
    //toString() method overrides toString() in SavingsAccount superclass
    public String toString(){
            String str = String.format("%s  %15d  %14s  %12s   $%7.2f  %13s\n", 
                    getDepositor(), getAccountNum(), getStatus(), getType(), getBalance(), getMaturityDateString());
            return str;
    }//end toString()
    
    public String getAccountStringForFile() {
        //String string = "";
        String str = String.format("%-30s%-10d%-10s%-10s%-10.2f%-12s", 
                    getDepositor().getDepositorStringForFile(), getAccountNum(), getStatus(), getType(), 
                    getBalance(), getMaturityDateString());
        return str;
    }
    
}
