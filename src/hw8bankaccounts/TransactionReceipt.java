package hw8bankaccounts;

/**
 *
 * @author andreatranchina
 */

import java.util.Calendar;

public class TransactionReceipt {
    private TransactionTicket transactionTicket;
    private boolean successIndicatorFlag;
    private String reasonForFailureString;
    private double preTransactionBalance;
    private double postTransactionBalance;
    private Calendar postTransactionMaturityDate; //for CDs only
    
    //constructors    
    public TransactionReceipt(TransactionTicket ticket, boolean flag, String str){
        transactionTicket = ticket;
        successIndicatorFlag = flag;
        reasonForFailureString = str;
        postTransactionMaturityDate = Calendar.getInstance();
        postTransactionMaturityDate.clear();
    }
    
    public TransactionReceipt(TransactionTicket ticket, boolean flag, String str,
            double preBalance, double postBalance, Calendar matDate){
        transactionTicket = ticket;
        successIndicatorFlag = flag;
        reasonForFailureString = str;
        preTransactionBalance = preBalance;
        postTransactionBalance = postBalance;
        postTransactionMaturityDate = matDate;
    }
    
    public TransactionReceipt(TransactionTicket ticket, boolean flag, String str,
            double preBalance, double postBalance, String matDate){
        transactionTicket = ticket;
        successIndicatorFlag = flag;
        reasonForFailureString = str;
        preTransactionBalance = preBalance;
        postTransactionBalance = postBalance;
        String[]dateArray = matDate.split("/");
        postTransactionMaturityDate = Calendar.getInstance();
        postTransactionMaturityDate.clear();
        if (!matDate.equals("01/01/1970") && !matDate.equals("N/A")){
            postTransactionMaturityDate.set(Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[0])-1, 
                Integer.parseInt(dateArray[1]));
        }   
    }
    
    public TransactionReceipt(TransactionTicket ticket, boolean flag, String str,
            double preBalance, double postBalance){
        transactionTicket = ticket;
        successIndicatorFlag = flag;
        reasonForFailureString = str;
        preTransactionBalance = preBalance;
        postTransactionBalance = postBalance;
        postTransactionMaturityDate = Calendar.getInstance();
        postTransactionMaturityDate.clear();
    }
    
    //copy constructor
    public TransactionReceipt (TransactionReceipt myReceipt){
        transactionTicket = myReceipt.transactionTicket;
        successIndicatorFlag = myReceipt.successIndicatorFlag;
        reasonForFailureString = myReceipt.reasonForFailureString;
        preTransactionBalance = myReceipt.preTransactionBalance;
        postTransactionBalance = myReceipt.postTransactionBalance;
        postTransactionMaturityDate = myReceipt.postTransactionMaturityDate;
    }
    
    //getters (accesssors)
    public TransactionTicket getTransactionTicket(){
        return transactionTicket;
    }
    
    public TransactionTicket getTransactionTicketCopy(){
        TransactionTicket transactionTicketCopy = new TransactionTicket (transactionTicket);
        return transactionTicketCopy;
    }
    
    public boolean getSuccessIndicatorFlag(){
        return successIndicatorFlag;
    }
    public String getReasonForFailureString(){
        return reasonForFailureString;
    }
    public double getPreTransactionBalance(){
        return preTransactionBalance;
    }
    public double getPostTransactionBalance(){
        return postTransactionBalance;
    }
    public Calendar getPostTransactionMaturityDate(){
        return postTransactionMaturityDate;
    }
    
    public String getPostTransactionMaturityDateString(){        
        String str;
        str = String.format("%02d/%02d/%4d", postTransactionMaturityDate.get(Calendar.MONTH)+1, 
                postTransactionMaturityDate.get(Calendar.DAY_OF_MONTH), 
                postTransactionMaturityDate.get(Calendar.YEAR));
        return str;
    }
    
    public String toString(){
        String str = transactionTicket.toString();
        switch (transactionTicket.getTypeOfTransaction()){
            case "Balance Inquiry":
                if (successIndicatorFlag == true){
                    str += String.format("%s\n", reasonForFailureString);
                    str += String.format("Account Balance: $%.2f\n", postTransactionBalance);
                } else {
                    str += String.format("%s\n", reasonForFailureString);
                }
                break;
            case "Deposit":
                if (transactionTicket.getAmountOfTransaction() ==0){
                    str += String.format("%s\n", reasonForFailureString);
                } else {
                    str += String.format("%s\n", reasonForFailureString);
                    str += String.format("Old Balance: $%.2f\nNew Balance: $%.2f\n", 
                            preTransactionBalance, postTransactionBalance);
                }
                if (postTransactionMaturityDate != null && !getPostTransactionMaturityDateString().equals("01/01/1970")){
                    str += String.format("Post Transaction Maturity Date: %s\n", 
                            getPostTransactionMaturityDateString());
                }                
                break;
                
            case "Withdrawal":
                if (transactionTicket.getAmountOfTransaction() ==0){
                    str += String.format("%s\n", reasonForFailureString);
                } else {
                    str += String.format("%s\n", reasonForFailureString);
                    str += String.format("Old Balance: $%.2f\nNew Balance: $%.2f\n", 
                            preTransactionBalance, postTransactionBalance);
                }
                if (postTransactionMaturityDate != null && !getPostTransactionMaturityDateString().equals("01/01/1970")){
                    str += String.format("Post Transaction Maturity Date: %s\n", 
                            getPostTransactionMaturityDateString());
                }                
                break;
                
            case "New Account Creation":
                if (transactionTicket.getAmountOfTransaction() ==0){
                    str += String.format("%s\n", reasonForFailureString);
                } else {
                    str += String.format("%s\n", reasonForFailureString);
                    str += String.format("New Account Balance: $%.2f\n", 
                            postTransactionBalance);
                }
                if (postTransactionMaturityDate != null && !getPostTransactionMaturityDateString().equals("01/01/1970")){
                    str += String.format("Post Transaction Maturity Date: %s\n", 
                            getPostTransactionMaturityDateString());
                }                
                break;
                
            case "Account Deletion":
                if (reasonForFailureString.equals("Error: Account number " + 
                        transactionTicket.getAccountNum() + " does not exist")){
                    str += String.format("%s\n", reasonForFailureString);
                }
                else {
                    str+= String.format("Current account balance: $%.2f\n", preTransactionBalance);
                    str+= String.format("%s\n", reasonForFailureString);
                }
                break;
                
            case "Clear Check":
                str = "";
                //str += String.format("***********************************************\n");
                str += String.format("%s\n", reasonForFailureString);
               
                if (!reasonForFailureString.equals("Error: Account number " + 
                        transactionTicket.getAccountNum() + " does not exist")){
                    str += String.format("Old balance: $%.2f\n", preTransactionBalance);                
                    str += String.format("New Balance: $%.2f\n", postTransactionBalance);
                    //str += String.format("\n"); 
                }
                break;
                
            case "Close Account":
                str += String.format("%s\n", reasonForFailureString);                
                if (successIndicatorFlag == true){
                    str += String.format("Account Balance: $%.2f\n", postTransactionBalance);
                }                
                break;
                
            case "Reopen Account":
                str += String.format("%s\n", reasonForFailureString);                
                if (successIndicatorFlag == true){
                    str += String.format("Account Balance: $%.2f\n", postTransactionBalance);
                }                
                break; 
        }//end switch
        return str;
    }
    
    //used to create string needed to store information in transactionreceipt fixed length records file
    public String getTransactionReceiptStringForFile(){
        String str;
        if (postTransactionMaturityDate == null) {
            str = String.format("%-82s%-10s%-80s%-10.2f%-10.2f%-12s", 
                    transactionTicket.getTransactionTicketStringForFile(), successIndicatorFlag, reasonForFailureString, 
                    preTransactionBalance, postTransactionBalance, "N/A");
        } else {
            str = String.format("%-82s%-10s%-80s%-10.2f%-10.2f%-12s", 
                    transactionTicket.getTransactionTicketStringForFile(), successIndicatorFlag, reasonForFailureString, 
                    preTransactionBalance, postTransactionBalance, getPostTransactionMaturityDateString());
        }
        return str;
    }    
}
