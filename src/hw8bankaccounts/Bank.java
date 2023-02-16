package hw8bankaccounts;

/**
 *
 * @author andreatranchina
 */

import java.util.*;
import java.io.*;

public class Bank {
    private static double totalAmountInSavingsAccounts = 0.00;
    private static double totalAmountInCheckAccounts = 0.00;
    private static double totalAmountInCDAccounts = 0.00;
    private static double totalAmountInAllAccounts = 0.00;
    //private ArrayList<Account>accountArray;
    //int numAccounts = 0;
    
    //no arg constructor
    public Bank() throws IOException {
        //accountArray = new ArrayList<Account>();
        //numAccounts = 0;
        String fileName = "BankAccounts.dat";
        FileOutputStream fileOut = new FileOutputStream (fileName);
        DataOutputStream accountFile = new DataOutputStream (fileOut);
        accountFile.close();
    }
    
    /**readAnotherAccount() is called on in main to read in initial database
     * the method reads in each new account as a fixed length record to a file
     * containing the info on all bank accounts. It then opens a transaction
     * receipt file for each account*/
    public void readAnotherAccount(Account newAccount) throws IOException{
        //accountArray.add(newAccount);
        addToTotalAmount(newAccount, newAccount.getBalance()); 

        //accountArray[numAccounts] = newAccount;
        //numAccounts++;
        final int CHAR_SIZE = 2;
        final int ACCOUNT_BYTES = 82;
        final int ACCOUNT_RECORD_SIZE = CHAR_SIZE * ACCOUNT_BYTES;
        
        //write each new account as a fixed length record to the file
        //add to end of file using the seek method, get String containing account info and write to acctFile
        RandomAccessFile accountFile = new RandomAccessFile ("BankAccounts.dat", "rw");
        long fileLength = accountFile.length();
        accountFile.seek(fileLength);
        
        String accountStringForFile = newAccount.getAccountStringForFile();
        accountFile.writeChars(accountStringForFile);
        accountFile.close();
        
        String fileName = "rcpt" + newAccount.getAccountNum() + ".dat";
            FileOutputStream fileOut = new FileOutputStream(fileName);
            DataOutputStream dataOut = new DataOutputStream (fileOut);
            dataOut.close();
    }
    
    //getters (accessors)
    
    /**The method getAccount() retrieves the desired account from the file
     * "BankAccounts.dat", in which the info for al accounts is stored as fixed
     * length records */
    public Account getAccount(int index) throws IOException {
        //Account account = accountArray.get(index);
        //return account;
        
        String accountStringFromFile = getAccountStringFromFile(index);
        
        String firstName = accountStringFromFile.substring(0,10).trim();
        String lastName = accountStringFromFile.substring(10,20).trim();
        String ssn = accountStringFromFile.substring(20,30).trim();
        int accountNum = Integer.parseInt(accountStringFromFile.substring(30,40).trim());
        String status = accountStringFromFile.substring(40,50).trim();
        String type = accountStringFromFile.substring(50,60).trim();
        double balance = Double.parseDouble(accountStringFromFile.substring(60,70).trim());
        String maturityDateString = accountStringFromFile.substring(70).trim();
        
        Name name = new Name (firstName, lastName);
        Depositor depositor = new Depositor (name, ssn);
        
        Account account = null;
        switch (type) {
            case "Savings":
                account = new SavingsAccount(depositor, accountNum, status, type, balance);
                break;
            case "Checking":
                account = new CheckingAccount(depositor, accountNum, status, type, balance);
                break;
            case "CD":
               account = new CDAccount (depositor, accountNum, status, type, balance, maturityDateString);
               break;
            
        }
        return account;
    }    
    
    //called on by getAccount() to obtain the string with information on the
    //desired account from the file containing fixed length account records
    public String getAccountStringFromFile(int index) throws IOException {
            
        final int CHAR_SIZE = 2;
        final int ACCOUNT_BYTES = 82;
        final int ACCOUNT_RECORD_SIZE = ACCOUNT_BYTES * CHAR_SIZE;
        
        char [] accountCharArray = new char[ACCOUNT_BYTES];
           
        //read account string as a fixed length from file
        String fileName = "BankAccounts.dat";
        RandomAccessFile accountFile = new RandomAccessFile (fileName, "rw");
            
        //seek to record of interest
        long filePointer = index*ACCOUNT_RECORD_SIZE;
        accountFile.seek(filePointer);
        for (int i=0; i<ACCOUNT_BYTES; i++){
            accountCharArray[i] = accountFile.readChar();
        }
        String accountStringFromFile = String.valueOf(accountCharArray);
            
        accountFile.close();
            
        return accountStringFromFile;
        
        
        }
    
    //retrieves a copy of the deisred account
    public Account getAccountCopy (int index) throws IOException{
        Account account = getAccount(index);        
        switch (account.getType()){
            case "Savings":
                SavingsAccount savings = (SavingsAccount)account;
                account = new SavingsAccount(savings);
                break;
            case "Checking":
                CheckingAccount checking = (CheckingAccount)account;
                account = new CheckingAccount(checking);
                break;
            case "CD":
                CDAccount cd = (CDAccount)account;
                account = new CDAccount(cd);
                break;
        }
        return account;
    }    
    
    //returns the number of accounts in the file containing fixed length account records
    public int getNumAccounts() throws IOException {
        //return accountArray.size();
        //return numAccounts;
        final int CHAR_SIZE = 2;
        final int ACCOUNT_BYTES = 82;
        final int ACCOUNT_RECORD_SIZE = ACCOUNT_BYTES * CHAR_SIZE;
        
        String fileName = "BankAccounts.dat";
        RandomAccessFile accountFile = new RandomAccessFile (fileName, "rw");
        
        int numAccounts = Math.toIntExact(accountFile.length()) / ACCOUNT_RECORD_SIZE;
        //long numAccounts = accountFile.length() / ACCOUNT_RECORD_SIZE;
        return numAccounts;
    }
    /*(public int getNumAccounts()throws IOException {
        return numAccounts;
    }*/
    
    //modifies "BankAccounts.dat" fixed length records file after transaction is completed
    public void modifyAccountFile(Account account, int index) throws IOException {
        //replaces the account after a transaction modifies account info
        final int CHAR_SIZE = 2;
        final int ACCOUNT_BYTES = 82;
        final int ACCOUNT_RECORD_SIZE = ACCOUNT_BYTES * CHAR_SIZE;
        
        String fileName = "BankAccounts.dat";
        RandomAccessFile accountFile = new RandomAccessFile (fileName, "rw");
        
        String accountStringForFile = account.getAccountStringForFile();
        accountFile.seek(index*ACCOUNT_RECORD_SIZE);
        accountFile.writeChars(accountStringForFile);
        accountFile.close();                
    }
    
    //removes an account from "BankAccounts.dat", called on by deleteAccount()
    public void removeAccountFromFile (int index) throws IOException {
        final int CHAR_SIZE = 2;
        final int ACCOUNT_BYTES = 82;
        final int ACCOUNT_RECORD_SIZE = ACCOUNT_BYTES * CHAR_SIZE;
        
        char[] accountCharArray = new char [ACCOUNT_BYTES];
        
        String fileName = "BankAccounts.dat";
        RandomAccessFile accountFile = new RandomAccessFile (fileName, "rw");
        long fileLength = accountFile.length();
        accountFile.seek(fileLength - ACCOUNT_RECORD_SIZE);
        
        for (int i=0; i<ACCOUNT_BYTES; i++){
            accountCharArray[i] = accountFile.readChar();
        }
        
        String accountStringFromFile = String.valueOf(accountCharArray);
        
        long filePointer = index * ACCOUNT_RECORD_SIZE;
        accountFile.seek(filePointer);
        
        accountFile.writeChars(accountStringFromFile);
        accountFile.setLength(fileLength - ACCOUNT_RECORD_SIZE);
        accountFile.close();
    }
    
    public double getTotalAmountInSavingsAccounts(){
        return totalAmountInSavingsAccounts;
    }
    
    public double getTotalAmountInCheckAccounts(){
        return totalAmountInCheckAccounts;
    }
    
    public double getTotalAmountInCDAccounts(){
        return totalAmountInCDAccounts;
    }
    
    public double getTotalAmountInAllAccounts(){
        return totalAmountInAllAccounts;
    }
    
    //adds amount to static data members as needed
    public void addToTotalAmount(Account myAccount, double myAmount){
        totalAmountInAllAccounts += myAmount;
        
        if (myAccount.getType().equals("Checking")){
            totalAmountInCheckAccounts += myAmount;
        } else if (myAccount.getType().equals("Savings")) {
            totalAmountInSavingsAccounts += myAmount;
        } else {
            totalAmountInCDAccounts += myAmount;
        }
    }
    
    //subtracts amount from static data members as needed
    public void subtractFromTotalAmount(Account myAccount, double myAmount){
        totalAmountInAllAccounts -= myAmount;
        
        if (myAccount.getType().equals("Checking")){
            totalAmountInCheckAccounts -= myAmount;
            if (myAccount.getBalance() < 2498.50){
                totalAmountInCheckAccounts -= 1.50;
                totalAmountInAllAccounts -= 1.50;
            }
        } else if (myAccount.getType().equals("Savings")) {
            totalAmountInSavingsAccounts -= myAmount;
        } else {
            totalAmountInCDAccounts -= myAmount;
        }
    }    
    
    //subtracts amount from static data members as needed
    //this method is used for failed transactions leading to bounced checks
    public void subtractFromTotalAmount(Account myAccount, boolean myBool){
        if (myBool == false) {
            totalAmountInAllAccounts -= 2.50;
            totalAmountInCheckAccounts -= 2.50;
        }    
    }  
    
    //method findAccount searches for requested account in account array and returns index 
    private int findAccount (int requestedAccount) throws InvalidAccountException, IOException{
        for (int index=0; index<getNumAccounts(); index++){
            if (getAccount(index).getAccountNum() == requestedAccount){
                return index;
            }
        }//end for loop
        throw new InvalidAccountException(requestedAccount);
           
    }//end findAccount
    
    /*openNewAccount method calls findAccount to see if requested account exists.
    *If it does not exist returns an error receipt, else created new account 
    * and prints succesful transaction receipt.
    */
    public TransactionReceipt openNewAccount(TransactionTicket transTicket, Account newAccount) throws IOException{
        TransactionReceipt transReceipt;
        Account account;
        int index; 
        
        try {
            index = findAccount(transTicket.getAccountNum());
            account = getAccount(index);
        }
        
        catch (InvalidAccountException ex) { //in this case exception is required to make the new account           
            //accountArray.add(newAccount);
            //numAccounts++;
            readAnotherAccount(newAccount); //does the adding to total amount as well
            if (newAccount.getType().equals("CD")){
                transReceipt = new TransactionReceipt(transTicket, true,
                        "Account number " + transTicket.getAccountNum() + " successfully created", 
                        0 , newAccount.getBalance(), 
                        newAccount.getMaturityDate());
            } else {
                transReceipt = new TransactionReceipt(transTicket, true,
                        "Account number " + transTicket.getAccountNum() + " successfully created", 
                        0 , newAccount.getBalance());
            } 
            //create TransactionReceipt Binary File for the new account
            String fileName = "rcpt" + newAccount.getAccountNum() + ".dat";
            FileOutputStream fileOut = new FileOutputStream(fileName);
            DataOutputStream dataOut = new DataOutputStream (fileOut);
            dataOut.close();
            
            newAccount.addTransaction(transReceipt); //add transaction
            //addToTotalAmount(newAccount, newAccount.getBalance()); DONE IN readanotheraccount()

            return transReceipt;
        }
        
        //didnt go through catch block, transaction failed because account already exists
        if (newAccount.getType().equals("CD")) {            
            transReceipt = new TransactionReceipt(transTicket, false,
                    "Error: Account number " + transTicket.getAccountNum() + " already exists", 
                    account.getBalance() , account.getBalance(), 
                    account.getMaturityDate());
        } else {
            transReceipt = new TransactionReceipt(transTicket, false,
                    "Error: Account number " + transTicket.getAccountNum() + " already exists", 
                    account.getBalance() , account.getBalance());
        }     
        return transReceipt;             
    }//end openNewAccount
    
    /*deleteAccount method calls findAccount to see if requested account exists.
    *If it does not exist returns an error receipt, else if account exists but
    *balance is nonzero, returns an error receipt. Else deleted account and 
    *prints successful transaction receipt
    */
    public TransactionReceipt deleteAccount(TransactionTicket transTicket) throws IOException{
        TransactionReceipt transReceipt;
        Account account;
        int index; 
        
        try {//try to find account
            index = findAccount(transTicket.getAccountNum());
            account = getAccount(index);
        }
        catch (InvalidAccountException ex) {//read if account does not exist            
            transReceipt = new TransactionReceipt(transTicket, false, ex.getMessage(), 0.00, 0.00);      
            return transReceipt;
        }
        
        if (getAccount(index).getBalance() != 0) {//balance is not zero
            transReceipt = new TransactionReceipt (transTicket, false, 
                "Error - Cannot delete account with nonzero balance", getAccount(index).getBalance(), 
                getAccount(index).getBalance());
                
            getAccount(index).addTransaction(transReceipt);
            return transReceipt;
            
        } else {//account exists and balance is zero
            //put active account in highest index in place of deleted account
            //then set highest index active account number and balance to zero
            transReceipt = new TransactionReceipt (transTicket, true, 
                "Transaction Successful - Account Deleted", getAccount(index).getBalance(), 
                0);
                
            account.addTransaction(transReceipt);
            //accountArray.remove(index);
            removeAccountFromFile(index);

            return transReceipt;
            }        
    }//end deleteAccount
    
    //getBalance method calls findAccount to see if requested account exists.
    //If it does not exist returns an error receipt, else calls on getBalance in Account class 
    public TransactionReceipt getBalance(TransactionTicket transTicket) throws IOException{
        TransactionReceipt transReceipt;
        Account account;
        int index; 
        
        try {
            index = findAccount(transTicket.getAccountNum());
            account = getAccount(index);
        }
        catch (InvalidAccountException ex) {            
            transReceipt = new TransactionReceipt(transTicket, false, ex.getMessage());      
            return transReceipt;
        }
        
        transReceipt = account.getBalance(transTicket);
        //getAccount(index).addTransaction(transReceipt); done in getBalance of account class        
        return transReceipt;
    }//end getBalance
    
    //makeDeposit method calls findAccount to see if requested account exists.
    //If it does not exist returns an error receipt, else calls on makeDeposit in Account class 
    public TransactionReceipt makeDeposit(TransactionTicket transTicket) throws IOException {
        TransactionReceipt transReceipt;
        Account account;
        int index; 
        
        try {//try to find account
            index = findAccount(transTicket.getAccountNum());
            account = getAccount(index);
        }
        catch (InvalidAccountException ex) {//catch block is read if account does not exist            
            transReceipt = new TransactionReceipt(transTicket, false, ex.getMessage());
            return transReceipt;
        }

        try {//try to make deposit
            transReceipt = account.makeDeposit(transTicket);       
            //account.addTransaction(transReceipt); done in Account subclasses for successful transactions            
            if (transReceipt.getSuccessIndicatorFlag() == true) {
                    addToTotalAmount(account, transTicket.getAmountOfTransaction());
                    modifyAccountFile(account, index);
            }            
            return transReceipt;
            
        } 
            
        catch (InvalidAmountException | AccountClosedException | CDMaturityDateException ex){//read if deposit failed                
            transReceipt = new TransactionReceipt(transTicket, false, ex.getMessage(), 
                    account.getBalance(), account.getBalance(), account.getMaturityDate());
            account.addTransaction(transReceipt);
            return transReceipt;            
        }                  
    }//end makeDeposit
    
    //makeWithdrawal method calls findAccount to see if requested account exists.
    //If it does not exist returns an error receipt, else calls on makeWithdrawal in Account class 
    public TransactionReceipt makeWithdrawal(TransactionTicket transTicket) throws IOException{        
        TransactionReceipt transReceipt;
        int index;
        Account account;

        try {//try to find account
            index = findAccount(transTicket.getAccountNum());
            account = getAccount(index);                                
        }
            
        catch (InvalidAccountException ex) {//catch block read if account not found
            transReceipt = new TransactionReceipt(transTicket, false, ex.getMessage());
            //index = -1;
            return transReceipt;
        }
            
        try {//try to make withdrawal
            transReceipt = account.makeWithdrawal(transTicket);
            //account.addTransaction(transReceipt); done in Account subclasses for successful transactions
            if (transReceipt.getSuccessIndicatorFlag() == true) {
                subtractFromTotalAmount(account, transTicket.getAmountOfTransaction());
                modifyAccountFile(account, index);
            }
            return transReceipt;                
        }            
           
        catch (AccountClosedException | InvalidAmountException | InsufficientFundsException | 
                CDMaturityDateException ex){//read if withdrawal transaction failed
            transReceipt = new TransactionReceipt(transTicket, false, ex.getMessage(), 
                    account.getBalance(), account.getBalance(), account.getMaturityDate());
            account.addTransaction(transReceipt);
            return transReceipt;
        }
            
    }//end makeWithdrawal
    
    //clearCheck method calls findAccount to see if requested account exists and is CheckingAccount.
    //If it does not exist returns an error receipt, else calls on clearCheck in Account class 
    public TransactionReceipt clearCheck(Check check, TransactionTicket transTicket) throws IOException{
        TransactionReceipt transReceipt;
        int index;
        Account account;
        
        try {//try to find account
            index = findAccount(check.getAccountNum());        
            account = getAccount(index);
        }
        catch (InvalidAccountException ex) {//catch read if account not found           
            transReceipt = new TransactionReceipt(transTicket, false, ex.getMessage());            
            return transReceipt;
        }            
        
        if (!(account.getType().equalsIgnoreCase("checking"))){//not a checking account
            
            transReceipt = new TransactionReceipt (transTicket, false,
            "Error: account entered must be a checking account", account.getBalance(), 
                    account.getBalance());
            account.addTransaction(transReceipt);
            return transReceipt;        
        
        } else {//account is checking account            
            try {//try to clear check
                transReceipt = account.clearCheck(check, transTicket);                
                //account.addTransaction(transReceipt); done in Account subclasses for successful transactions
                if (transReceipt.getSuccessIndicatorFlag() == true) {
                    subtractFromTotalAmount(getAccount(index), transTicket.getAmountOfTransaction());
                    modifyAccountFile(account, index);
                }                                
                return transReceipt;
            }
            catch (InvalidAmountException | AccountClosedException |
                    PostDatedCheckException | CheckTooOldException ex){//read if clear check transaction failed
                transReceipt = new TransactionReceipt(transTicket, false, ex.getMessage(), 
                        account.getBalance(), account.getBalance());
                account.addTransaction(transReceipt);
                return transReceipt;
            }
            catch (InsufficientFundsException ex) {
                transReceipt = new TransactionReceipt(transTicket, false, ex.getMessage(), 
                        account.getBalance()+2.50, account.getBalance());
                modifyAccountFile(account, index); //even if failed transaction $2.50 fee applied for bounced check
                subtractFromTotalAmount(getAccount(index), transReceipt.getSuccessIndicatorFlag());
                account.addTransaction(transReceipt);
                return transReceipt;
            }
        }               
    }//end clearCheck
    
    
    /*closeAccount method receives a ticket and checks to see if the account
    * number requested in the ticket it exists. If account does not exist it
    *returns an error receipt, else it returns a receipt by calling on closeAccount
    *in the Account class
    */
    public TransactionReceipt closeAccount(TransactionTicket ticket) throws IOException{
        TransactionReceipt transReceipt;
        int index;
        Account account;
        
        try {//try to find account
            index = findAccount(ticket.getAccountNum());        
            account = getAccount(index);
        }
        catch (InvalidAccountException ex) {//read if account not found            
            transReceipt = new TransactionReceipt(ticket, false, ex.getMessage());            
            return transReceipt;
        }        
        
        transReceipt = account.closeAccount(ticket);
        
        if (transReceipt.getSuccessIndicatorFlag() == true) {
            modifyAccountFile(account, index);
        }
        
        return transReceipt;
    }//end closeAccount method

    /*reopenAccount method receives a ticket and checks to see if the account
    * number requested in the ticket it exists. If account does not exist it
    *returns an error receipt, else it returns a receipt by calling on reopen
    *in the Account class
    */
    public TransactionReceipt reopenAccount(TransactionTicket ticket) throws IOException{
        TransactionReceipt transReceipt;
        int index;
        Account account;
        
        try {
            index = findAccount(ticket.getAccountNum());        
            account = getAccount(index);
        }
        catch (InvalidAccountException ex) {            
            transReceipt = new TransactionReceipt(ticket, false, ex.getMessage());            
            return transReceipt;
        }        
        
        transReceipt = account.reopenAccount(ticket);
        
        if (transReceipt.getSuccessIndicatorFlag() == true) {
            modifyAccountFile(account, index);
        }
        return transReceipt;
    }//end reopenAccount method    
        
}
