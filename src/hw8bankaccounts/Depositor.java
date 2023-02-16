package hw8bankaccounts;

/**
 *
 * @author andreatranchina
 */
public class Depositor {
    private Name name;
    private String ssn;
    
    //constructors
    public Depositor(){
        name = new Name();
        ssn = "";    
    }
    public Depositor(Name nm, String str){
        name = nm;
        ssn = str;
    }
    
    //copy constructor
    public Depositor (Depositor myDepositor){
        name = new Name (myDepositor.name);
        ssn = myDepositor.ssn;
    }
   
    //getters (accessors)
    public String getSsn(){
        return ssn;
    }
   
    public Name getName(){
        return name;
    }        
    
    public Name getNameCopy(){
        Name nameCopy = new Name (name);
        return nameCopy;
    }
    
    //toString() method
    public String toString(){
        String str = String.format("%s  %10s", name, ssn);
        return str;
    }
    
    //equals() method
    public boolean equals (Depositor myDepositor){
        return name.equals(myDepositor.name) && ssn.equals(myDepositor.ssn);
    }// end equals() method    
    
    //used to create string needed to store information in bankaccount fixed length records file
    public String getDepositorStringForFile(){
        String str = String.format ("%-20s%-10s", name.getNameStringForFile(), ssn);
        return str;
    }
}
