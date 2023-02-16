package hw8bankaccounts;

/**
 *
 * @author andreatranchina
 */
import java.util.Calendar;

public class TransactionTicket {
   private int accountNum;
   private Calendar dateOfTransaction;
   private String typeOfTransaction;
   private double amountOfTransaction;
   private int termOfCD;
   
   public TransactionTicket (int integer, String type) {//nonCD accounts getBalance method
       accountNum = integer;
       typeOfTransaction = type;
       dateOfTransaction = Calendar.getInstance();
       //dateOfTransaction.clear();
       //String[]dateArray = date.toString().split("/");
       //dateOfTransaction.set(Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[0])-1,
                //Integer.parseInt(dateArray[1]));
   }
   
   public TransactionTicket(int integer, double amount, String type){//for non CD accounts
       accountNum = integer;
       amountOfTransaction = amount;
       typeOfTransaction = type;
       dateOfTransaction = Calendar.getInstance();
      
   }
      
   public TransactionTicket(int integer, double amount, String type, int CDterm){//for CD accounts
       accountNum = integer;
       typeOfTransaction = type;
       amountOfTransaction = amount;
       termOfCD = CDterm;
       //for the date of check
       dateOfTransaction = Calendar.getInstance();
       /*dateOfTransaction.clear();
       String[]docArray = date.split("/");
       dateOfTransaction.set(Integer.parseInt(docArray[2]), Integer.parseInt(docArray[0])-1,
               Integer.parseInt(docArray[1]));    */
   }
   
   public TransactionTicket(int num, String cal, String type, double amount, int term){
       accountNum = num;
       typeOfTransaction = type;
       amountOfTransaction = amount;
       termOfCD = term;
       String[]dateArray = cal.split("/");
       dateOfTransaction = Calendar.getInstance();
       dateOfTransaction.clear();
       dateOfTransaction.set(Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[0])-1, 
               Integer.parseInt(dateArray[1]));
   }
   
   //copy constructor
   public TransactionTicket (TransactionTicket myTicket){
       accountNum = myTicket.accountNum;
       dateOfTransaction = myTicket.dateOfTransaction;
       typeOfTransaction = myTicket.typeOfTransaction;
       amountOfTransaction = myTicket.amountOfTransaction;
       termOfCD = myTicket.termOfCD;
   }
   
   //getters (accessors)
   public int getAccountNum(){
        return accountNum;
   }
   public Calendar getDateOfTransaction(){
        return dateOfTransaction;
   }
   
   public String getDateOfTransactionString(){
       String str;
        str = String.format("%02d/%02d/%4d", dateOfTransaction.get(Calendar.MONTH)+1, 
                dateOfTransaction.get(Calendar.DAY_OF_MONTH), dateOfTransaction.get(Calendar.YEAR));
        return str;
   }
   
   public String getTypeOfTransaction(){
       return typeOfTransaction;
   }
   public double getAmountOfTransaction(){
       return amountOfTransaction;
   }
   public int getTermOfCD(){
       return termOfCD;
   }
   
   public String toString(){
       String str1 = String.format("Account Number: %d \nTransaction Date: %s "
               + "\nTransaction Requested: %s \n", accountNum, getDateOfTransactionString(), 
               typeOfTransaction); 
       
       switch (typeOfTransaction){
           case "Deposit":
           case "Withdrawal":
               if (amountOfTransaction != 0){
                    String str2 = String.format("Amount of Transaction: $%.2f\n", amountOfTransaction);
                    str1 = str1 + str2;
                    break;
               }     
           default:
               break;
       }
       return str1;
   }
    
    //used to create string needed to store information in transactionreceipt fixed length records file
    public String getTransactionTicketStringForFile(){
        String str = String.format("%-10d%-12s%-40s%-10.2f%-10d", accountNum, 
               getDateOfTransactionString(), typeOfTransaction, amountOfTransaction, termOfCD);
        return str;
    }
}
