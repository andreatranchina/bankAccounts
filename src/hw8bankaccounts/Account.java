package hw8bankaccounts;

/**
 *
 * @author andreatranchina
 */

import java.util.*;
import java.io.*;

public class Account {
    private Depositor depositor;
    private int accountNum;
    private String type;
    private String status;
    //protected ArrayList<TransactionReceipt>transactionHistory;
    protected double balance;
    //private final String fileName = "rcpt" + accountNum + ".dat";    
        
    //constructors
    public Account() throws IOException{
        //transactionHistory = new ArrayList<TransactionReceipt>();
        depositor = new Depositor();
        accountNum = 000000;
        status = "";
        type = "";
        balance = 0.00;
        //maturityDate = Calendar.getInstance();      
    }
    
    public Account (Depositor d, int integer, String strStatus, String strType, 
            double doubleNum, String dateString) throws IOException{//for CDs
        //transactionHistory = new ArrayList<TransactionReceipt>();
        depositor = d;
        accountNum = integer;
        status = strStatus;
        type = strType;
        balance = doubleNum;
        /*maturityDate = Calendar.getInstance();
        maturityDate.clear();
        String[]dateArray = dateString.split("/");
        maturityDate.set(Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[0])-1,
                Integer.parseInt(dateArray[1]));     */
    }
    
    public Account (Depositor d, int integer, String strStatus, String strType, 
            double doubleNum, Calendar date) throws IOException{//for CDs
        //transactionHistory = new ArrayList<TransactionReceipt>();
        depositor = d;
        accountNum = integer;
        status = strStatus;
        type = strType;
        balance = doubleNum;
        //maturityDate = date; 
    }
    
    public Account (Depositor d, int integer, String strStatus, String strType, 
            double doubleNum) throws IOException{//for nonCDs
        //transactionHistory = new ArrayList<TransactionReceipt>();
        depositor = d;
        accountNum = integer;
        status = strStatus;
        type = strType;
        balance = doubleNum;
        //maturityDate = Calendar.getInstance();
        //maturityDate.clear();
    }
    
    //copy constructor
    public Account (Account myAccount){
       depositor = new Depositor (myAccount.depositor);
       accountNum = myAccount.accountNum;
       status = myAccount.status;
       type = myAccount.type;
       balance = myAccount.balance;
       //maturityDate = myAccount.maturityDate; 
       //transactionHistory = myAccount.transactionHistory;
       //transactionHistory = new ArrayList<TransactionReceipt>(myAccount.transactionHistory);
    }
    
    //getters (accessors)
    public Depositor getDepositor(){
        return depositor;
    }
    
    public Depositor getDepositorCopy(){
        Depositor depositorCopy = new Depositor (depositor);
        return depositorCopy;
    }
    
    public int getAccountNum(){
        return accountNum;
    }
    
    public String getType(){
        return type;
    }
    
    public double getBalance(){
        return balance;
    }
    
    public Calendar getMaturityDate (){
        return null;//METHOD IS OVERWRITTEN
    }
    
    /*public String getMaturityDateString(){ //now in CheckingAccount class
        String str;
        str = String.format("%02d/%02d/%4d", maturityDate.get(Calendar.MONTH)+1, 
                maturityDate.get(Calendar.DAY_OF_MONTH), maturityDate.get(Calendar.YEAR));
        return str;
    }*/
    
    public String getStatus(){
        return status;
    }
    
    //to get a specific receipt from the accounts file containing transaction receipts
    //as fixed length records
    public TransactionReceipt getTransactionReceipt(int index) throws IOException{                
        String receiptStringFromFile = getTransactionReceiptStringFromFile(index);        
               
        int accountNum = Integer.parseInt(receiptStringFromFile.substring(0,10).trim());
        String dateOfTransactionString = receiptStringFromFile.substring(10,22).trim();
        String typeOfTransaction = receiptStringFromFile.substring(22,62).trim();
        double amountOfTransaction = Double.parseDouble(receiptStringFromFile.substring(62,72).trim());
        int termOfCD = Integer.parseInt(receiptStringFromFile.substring(72,82).trim());
        boolean successIndicatorFlag = Boolean.parseBoolean(receiptStringFromFile.substring(82,92).trim());
        String reasonForFailureString = receiptStringFromFile.substring(92,172).trim();
        double preTransactionBalance = Double.parseDouble(receiptStringFromFile.substring(172,182).trim());
        double postTransactionBalance = Double.parseDouble(receiptStringFromFile.substring(182,192).trim());
        String postTransactionMaturityDateString = receiptStringFromFile.substring(192).trim();        
                
        TransactionTicket ticket = new TransactionTicket (accountNum, dateOfTransactionString, 
                typeOfTransaction, amountOfTransaction, termOfCD);
        TransactionReceipt receipt = new TransactionReceipt (ticket, successIndicatorFlag, 
                reasonForFailureString, preTransactionBalance, postTransactionBalance, 
                postTransactionMaturityDateString);                       
        
        return receipt;    
    }
    
    //called on by getTransactionReceipt to obtain the string corresponding to
    //the desired receipt from the transactionReceipt file
    public String getTransactionReceiptStringFromFile(int index) throws IOException {
        final int CHAR_SIZE = 2;
        final int TRANS_TICKET_BYTES = 82;
        final int TRANS_RECEIPT_BYTES = 204;
        final int TRANS_RECEIPT_RECORD_SIZE = TRANS_RECEIPT_BYTES * CHAR_SIZE;
        
        char [] transactionReceiptCharArray = new char[TRANS_RECEIPT_BYTES];
        
        //read transaction string as fixed length
        String fileName = "rcpt" + accountNum + ".dat";
        RandomAccessFile receiptFile = new RandomAccessFile (fileName, "rw");
        
        //String receiptStringForFile = receipt.getTransactionReceiptStringForFile();
        
        long filePointer = index*TRANS_RECEIPT_RECORD_SIZE;
        receiptFile.seek(filePointer);
        //receiptFile.writeChars(receiptStringForFile);
        for (int i=0; i<TRANS_RECEIPT_BYTES; i++){
            transactionReceiptCharArray[i] = receiptFile.readChar();
        }
        String receiptStringFromFile = String.valueOf(transactionReceiptCharArray);
        
        receiptFile.close();
        return receiptStringFromFile;
    }
    
    //returns number of receipts in transactionReceipt file
    public int getNumOfReceipts() throws IOException {
        final int CHAR_SIZE = 2;
        final int TRANS_TICKET_BYTES = 82;
        final int TRANS_RECEIPT_BYTES = 204;
        final int TRANS_RECEIPT_RECORD_SIZE = TRANS_RECEIPT_BYTES * CHAR_SIZE;
        
        String fileName = "rcpt" + accountNum + ".dat";
        RandomAccessFile receiptFile = new RandomAccessFile (fileName, "rw");
        
        int numAccounts = Math.toIntExact(receiptFile.length()) / TRANS_RECEIPT_RECORD_SIZE;
        return numAccounts;
    }
    
    //addTransaction adds a receipt to the account's transactionReceipt file
    public void addTransaction(TransactionReceipt receipt) throws IOException {
        //transactionHistory.add(receipt);
        
        final int CHAR_SIZE = 2;
        final int TRANS_TICKET_BYTES = 82;
        final int TRANS_RECEIPT_BYTES = 204;
        final int TRANS_RECEIPT_RECORD_SIZE = TRANS_RECEIPT_BYTES * CHAR_SIZE;
        
        String fileName = "rcpt" + accountNum + ".dat";
        RandomAccessFile receiptFile = new RandomAccessFile (fileName, "rw");                        
        long fileLength = receiptFile.length();
        receiptFile.seek(fileLength);
        
        String receiptStringForFile = receipt.getTransactionReceiptStringForFile();
        receiptFile.writeChars(receiptStringForFile);
        
        receiptFile.close();
    }    
    
    //getBalance returns receipt with balance of existing account
    public TransactionReceipt getBalance(TransactionTicket transTicket) throws IOException{        
        TransactionReceipt transReceipt = new TransactionReceipt(transTicket, true, 
                "Transaction Successful", getBalance(), getBalance());
        addTransaction(transReceipt);
        return transReceipt;
    }//end getBalance    
    
    /*makeDeposit throws exceptions if deposit amount is negative or CD is not mature.
    *Else the method will return a succesful transaction receipt, along with adjusting
    *account balances and maturity dates as needed.
    */
    public TransactionReceipt makeDeposit(TransactionTicket transTicket) 
            throws InvalidAmountException, AccountClosedException, 
            CDMaturityDateException, IOException{
        
        TransactionReceipt receipt = null;
        return receipt; //METHOD IS OVERWRITTEN BY SUBCLASSES
        /*if (getStatus().equals("closed")) { //closed account
            TransactionReceipt transReceipt = new TransactionReceipt(transTicket, false, 
                "Error: Account is currently closed - please reopen and try again", 
                getBalance(), getBalance(), getMaturityDate());
            addTransaction(transReceipt);
            return transReceipt;        
        }
        
        else if (getType().equals("CD") && transTicket.getAmountOfTransaction()<= 0) {//invalid amount CD account
            TransactionReceipt transReceipt = new TransactionReceipt(transTicket, false, 
                    ("Error: invalid amount entered - amount to deposit must be positive"), 
                    getBalance(), getBalance(), getMaturityDate());
            addTransaction(transReceipt);
            return transReceipt;
        
        } else if (transTicket.getAmountOfTransaction()<=0) {//invalid amount nonCD account   
            TransactionReceipt transReceipt = new TransactionReceipt(transTicket, false, 
                    ("Error: invalid amount entered - amount to deposit must be positive!!!!!!"), 
                    getBalance(), getBalance());
            addTransaction(transReceipt);
            return transReceipt;
            
        } else if (getType().equals("CD") && 
            transTicket.getDateOfTransaction().before(getMaturityDate())){ //valid amount, CD not mature
            
            TransactionReceipt transReceipt = new TransactionReceipt (transTicket, 
                    false, "Error: CD not matured", getBalance(), getBalance(),
                    getMaturityDate());
            addTransaction(transReceipt);
            return transReceipt;
        
        } else if (getType().equals("CD")){//valid amount, mature CD
            Calendar postMaturityDate = Calendar.getInstance();
            postMaturityDate.add(Calendar.MONTH, transTicket.getTermOfCD());
            maturityDate = postMaturityDate; //adjust to new maturity date           
            
            TransactionReceipt transReceipt = new TransactionReceipt (transTicket, 
                    true, "Transaction Successful", getBalance(), 
                    getBalance() + transTicket.getAmountOfTransaction(),
                    getMaturityDate());
            
            setBalance(getBalance() + transTicket.getAmountOfTransaction());
            
            addTransaction(transReceipt);
            //addToTotalAmount()
            return transReceipt;            
            
        } else {//valid amount, not a CD account
            TransactionReceipt transReceipt = new TransactionReceipt (transTicket, true,
                        "Transaction Successful", getBalance(), 
                        transTicket.getAmountOfTransaction() + getBalance());
            
            setBalance(getBalance() + transTicket.getAmountOfTransaction());
            addTransaction(transReceipt);
            return transReceipt;
        }*/        
    }//end makeDeposit
    
    /*makeWithdrawal throws exceptions if amount to withdraw is not positive,
    *if the account has insufficient funds, if the CD account is not mature. Else
    *it will return successful transaction receipts and adjust balances and
    *maturity dates as needed.
    */
    public TransactionReceipt makeWithdrawal(TransactionTicket transTicket) 
            throws AccountClosedException, InvalidAmountException, 
            CDMaturityDateException, InsufficientFundsException, IOException{
        
        TransactionReceipt receipt = null;
        return receipt; //METHOD IS OVERWRITTEN IN SUBCLASSES
                
        /*if (getStatus().equals("closed")) {
            TransactionReceipt transReceipt = new TransactionReceipt(transTicket, false, 
                "Error: Account is currently closed - please reopen and try again", 
                getBalance(), getBalance(), getMaturityDate());
            addTransaction(transReceipt);
            return transReceipt;        
        }
        
        else if (transTicket.getAmountOfTransaction()<= 0) {//invalid amount 
            TransactionReceipt transReceipt = new TransactionReceipt(transTicket, false, 
                    ("Error: invalid amount entered - amount to withdraw must be positive"), 
                    getBalance(), getBalance(), getMaturityDate());
            
            addTransaction(transReceipt);
            return transReceipt;
            
        } else if (getBalance()<transTicket.getAmountOfTransaction()){//insufficient funds in account
            TransactionReceipt transReceipt = new TransactionReceipt(transTicket, false, 
                    ("Error: Insufficient Funds - Transaction Voided"), 
                    getBalance(), getBalance(), getMaturityDate());
            addTransaction(transReceipt);
            return transReceipt;
            
        } else if (getType().equals("CD") && 
            transTicket.getDateOfTransaction().before(getMaturityDate())){ //CD not mature
            
            TransactionReceipt transReceipt = new TransactionReceipt (transTicket, 
                    false, "Error: CD not matured", getBalance(), getBalance(),
                    getMaturityDate());
            addTransaction(transReceipt);
            return transReceipt;
        
        } else if (getType().equals("CD")){//mature CD
            Calendar postMaturityDate = Calendar.getInstance();
            postMaturityDate.add(Calendar.MONTH, transTicket.getTermOfCD());
            maturityDate = postMaturityDate; //adjust to new maturity date                   
            
            TransactionReceipt transReceipt = new TransactionReceipt (transTicket, 
                    true, "Transaction Successful", getBalance(), 
                    getBalance() - transTicket.getAmountOfTransaction(),
                    getMaturityDate());
            
            setBalance(getBalance() - transTicket.getAmountOfTransaction());
            addTransaction(transReceipt);
            return transReceipt;            
            
        } else {//valid amount, not CD account
            TransactionReceipt transReceipt = new TransactionReceipt (transTicket, true,
                    "Transaction Successful", getBalance(), 
                    getBalance() - transTicket.getAmountOfTransaction());
            
            setBalance(getBalance() - transTicket.getAmountOfTransaction());
            addTransaction(transReceipt);
            return transReceipt;

        }*/
    }//end makeWithdrawal
    
    /*clearCheck throws exceptions:
    *if amount of check is invalid (<0), if check date is not within the past 6
    *months, and if account has insufficient funds (in this latter case service 
    *fee will be applied and account balance is adjusted). Else succesfful transaction
    *receipt is returned and account balance is adjusted.
    */
    public TransactionReceipt clearCheck (Check check, TransactionTicket transTicket)
            throws AccountClosedException, InvalidAmountException, CheckTooOldException, 
            PostDatedCheckException, InsufficientFundsException, IOException{
        
        TransactionReceipt receipt = null;
        return receipt;
        /* METHOD IS OVERWRITTEN
        if (getStatus().equals("closed")) {
            TransactionReceipt transReceipt = new TransactionReceipt(transTicket, false, 
                "Error: Account is currently closed - please reopen and try again", 
                getBalance(), getBalance());
            addTransaction(transReceipt);
            return transReceipt;        
        }
        
        if (!(getType().equalsIgnoreCase("checking"))){//not a checking account
            
            TransactionReceipt transReceipt = new TransactionReceipt (transTicket, false,
            "Error: account entered must be a checking account", getBalance(), getBalance());
            addTransaction(transReceipt);
            return transReceipt;
        }
        
        if (check.getCheckAmount() < 0) {//check amount invalid
            
            TransactionReceipt transReceipt = new TransactionReceipt (transTicket, false,
            "Error: invalid amount of check entered - must be positive", getBalance(), getBalance());
            addTransaction(transReceipt);
            return transReceipt;                        
        }
        Calendar compareTime = Calendar.getInstance();
        compareTime.add(Calendar.MONTH, -6);
        
        if (check.getDateOfCheck().before(compareTime) || 
                check.getDateOfCheck().after(transTicket.getDateOfTransaction())){//check date not within past 6 months
            
            TransactionReceipt transReceipt = new TransactionReceipt (transTicket, false,
            "Error: date of check must be within the past 6 months", getBalance(), getBalance());
            addTransaction(transReceipt);
            return transReceipt;
        }
        
        if (getBalance() < check.getCheckAmount()){//insufficient funds
            
            TransactionReceipt transReceipt = new TransactionReceipt (transTicket, false,
            "Error: insufficient funds - check bounced and service fee applied", 
            getBalance(), getBalance() - 2.50);
            setBalance(getBalance()-2.50);
            addTransaction(transReceipt);
            return transReceipt;
        }
        else {//succesful transaction
            TransactionReceipt transReceipt = new TransactionReceipt (transTicket, true,
            "Transaction Successful", getBalance(), getBalance() - check.getCheckAmount());
            setBalance(getBalance()-check.getCheckAmount());
            addTransaction(transReceipt);
            return transReceipt;            
        }*/        
    }//end clearCheck
    
    /*closeAccount receives a ticket and checks to see if the requested account
    *is already closed. If it is closed, it will return an error receipt, else
    *it will change the account status from opened to close and return a 
    *successful transaction receipt
    */
    public TransactionReceipt closeAccount (TransactionTicket ticket) throws IOException{
        if (getStatus().equals("closed")) {//account is already closed
            TransactionReceipt receipt = new TransactionReceipt (ticket, false, 
            "Error: account is already closed", getBalance(), getBalance());
            addTransaction(receipt);
            return receipt;
            
        } else {//account is open
            TransactionReceipt receipt = new TransactionReceipt (ticket, true, 
            "Transaction Successful - Account Closed", getBalance(), getBalance());
            addTransaction(receipt);
            status = "closed";
            return receipt;
        }
    }//end closeAccount method

    /*reopenAccount receives a ticket and checks to see if the requested account
    *is already open. If it is open, it will return an error receipt, else
    *it will change the account status from close to open and return a 
    *successful transaction receipt
    */    
    public TransactionReceipt reopenAccount (TransactionTicket ticket) throws IOException{
        if (getStatus().equals("open")) {//account is already open
            TransactionReceipt receipt = new TransactionReceipt (ticket, false, 
            "Error: account is already open", getBalance(), getBalance());
            addTransaction(receipt);
            return receipt;
            
        } else {//account is closed
            TransactionReceipt receipt = new TransactionReceipt (ticket, true, 
            "Transaction Successful - Account Reopened", getBalance(), getBalance());
            addTransaction(receipt);
            status = "open";
            return receipt;
        }
    }//end reopenAccount method
    
    //toString() method OVERWRITTEN
    public String toString(){
        String string = "";
        return string;
        /*if (type.equals("CD")){
            String str = String.format("%s  %15d  %14s  %12s   $%7.2f  %13s\n", 
                    depositor, accountNum, status, type, balance, getMaturityDateString());
            return str;
        }
        else {
            String str = String.format("%s  %15d  %14s  %12s   $%7.2f  %13s\n", 
                        depositor, accountNum, status, type, balance, "N/A");
            return str;
        }*/
    }//end toString()  
    
    //.equals() method
    public boolean equals (Account myAccount){
        return depositor.equals(myAccount.depositor) && accountNum == myAccount.accountNum 
                && type.equals(myAccount.type) && status.equals(myAccount.status) && 
                balance == myAccount.balance; 
                //&& transactionHistory.equals(myAccount.transactionHistory)
                
    }
    
    //OVERRIDDEN
    //gets the string of information on the account, this string is used to
    //store in the file containing the fixed length account records
    public String getAccountStringForFile(){
        String string = "";
        return string;
        /*if (type.equals("CD")){
            String str = String.format("%-30s%-6d%-5s%-10s%-8.2f%-10s", 
                    depositor, accountNum, status, type, balance, getMaturityDateString());
            return str;
        }
        else {
            String str = String.format("%-30s%-6d%-5s%-10s%-8.2f%", 
                        depositor, accountNum, status, type, balance);
            return str;
        }*/
    }
    
}        
