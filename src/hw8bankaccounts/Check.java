package hw8bankaccounts;

/**
 *
 * @author andreatranchina
 */
import java.util.Calendar;

public class Check {
    private int accountNum;
    private double checkAmount;
    private Calendar dateOfCheck;
        
    //constructors
    public Check(){
        accountNum = 0;
        checkAmount = 0.00;
        dateOfCheck = Calendar.getInstance();        
    }
    public Check (int account, double amount, String date){
        accountNum = account;
        checkAmount = amount;
        
        dateOfCheck = Calendar.getInstance();
        dateOfCheck.clear();
        String[]dateArray = date.split("/");
        dateOfCheck.set(Integer.parseInt(dateArray[2]), Integer.parseInt(dateArray[0])-1,
                Integer.parseInt(dateArray[1]));   
    }
    
    //copy constructor
    public Check (Check myCheck){
        accountNum = myCheck.accountNum;
        checkAmount = myCheck.checkAmount;
        dateOfCheck = myCheck.dateOfCheck;
    }
    
    //getters
    public int getAccountNum(){
        return accountNum;
    }
    public double getCheckAmount(){
        return checkAmount;
    }
    public Calendar getDateOfCheck(){
        return dateOfCheck;        
    }
    public String getDateOfCheckString(){
        String str;
        str = String.format("%02d/%02d/%4d", dateOfCheck.get(Calendar.MONTH)+1, 
                dateOfCheck.get(Calendar.DAY_OF_MONTH), dateOfCheck.get(Calendar.YEAR));
        return str;
    }
    
    public String toString(){
        String str = String.format("Check Amount: $%.2f\nCheck Date: %s\n",
                checkAmount, getDateOfCheckString()); 
        return str;
    }    
}
