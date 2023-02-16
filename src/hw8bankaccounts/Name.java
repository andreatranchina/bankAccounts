package hw8bankaccounts;

/**
 *
 * @author andreatranchina
 */
public class Name {
    public String firstName;
    public String lastName;
    
    //constructors
    public Name(){
        firstName = "";
        lastName = "";        
    }
    public Name(String first, String last){
        firstName = first;
        lastName = last;
    }
    
    //copy constructor
    public Name (Name myName){
        firstName = myName.firstName;
        lastName = myName.lastName;
    }

    //getters
    public String getFirstName(){
        return firstName;
    }
    
    public String getLastName(){
        return lastName;
    }    
    
    //toString() method
    public String toString(){
        String str = String.format("%9s  %10s", firstName, lastName);
        return str;
    }
    
    //equals() method
    public boolean equals (Name myName){
        if (firstName.equals(myName.firstName) && lastName.equals(myName.lastName)){
            return true; //equals
        } else {
            return false;
        }
    }//end equals() method
    
    //used to create string needed to store information in bankaccount fixed length records file
    public String getNameStringForFile(){
        String str = String.format("%-10s%-10s", firstName, lastName);
        return str;
    }
}
