/**
 * @author Andrea Tranchina
 * @since 12-04-2022
 * Description:
 */
package hw8bankaccounts;

import java.util.*;
import java.io.*;

public class Hw8BankAccounts{
    public static void main(String[]args) throws Exception {
        //variable declaration;
        Bank myBank = new Bank(); //database of bank accounts
        char choice; //user selection for type of transaction
        boolean notDone = true;
        
        //open input test cases file
        
        //create Scanner object
        Scanner sc = new Scanner (new File ("myTestCases.txt")); //test cases file
        //Scanner sc = new Scanner (System.in);
        
        //create PrintWriter
        PrintWriter pw = new PrintWriter ("Output.txt"); //output file
        //PrintWriter pw = new PrintWriter (System.out);
        
        //first part
        //fill and print initial database
        int numAccounts = readAccounts(myBank);
        printAccounts (myBank, pw);
        
        //second part
        //prompts for a transaction and calls function to process needed transaction
           
        do {
            menu();
            choice = sc.next().charAt(0);
            try{            
            switch(choice){
                case 'q':
                case 'Q':
                    notDone = false;
                    printAccounts(myBank, pw);
                    break;
                case 'b':
                case 'B':
                    balance(myBank, pw, sc);
                    break;
                case 'd':
                case 'D':
                    deposit(myBank, pw, sc);
                    break;
                case 'w':
                case 'W':
                    withdrawal (myBank, pw, sc);
                    break;
                case 'n':
                case 'N':
                    numAccounts = newAccount(myBank, pw, sc);
                    break;
                case 'x':
                case 'X':
                    numAccounts = deleteAccount(myBank, pw, sc);
                    break;
                case 'i':
                case 'I':
                    accountInfo(myBank, pw, sc);
                    break;
                case 'c':
                case 'C':
                    clearCheck(myBank, pw, sc);
                    break;
                case 'h':
                case 'H':
                    accountInfoHistory(myBank, pw, sc);
                    break;
                case 'r':
                case 'R':
                    reopenAccount(myBank, pw, sc);
                    break;
                case 's':
                case 'S':
                    closeAccount(myBank, pw , sc);
                    break;
                default:                    
                    throw new InvalidMenuSelectionException(choice);
            } //end switch        
            } //end try        
            catch (InvalidMenuSelectionException ex){
            pw.println(ex.getMessage());
            pw.println();
            pw.flush();
            //break - comment out otherwise would break do-while loop;    
            }//end catch
                                                    
            //give the user a chance to look at output before printing menu
            //pause(sc);
        }//end do
        while(notDone);
       
        //close the output file
        pw.close();
        
        //close the test cases input file
        sc.close();
        
        System.out.println();
        System.out.println("The program is terminating");    
    }//end main        
    
    /*1-Method readAccounts()
     * Input:
     *   mybank - database of bank accounts
     * Process:
     *   Reads in the initial database from the initial database text file
     * Output:
     *   Fills in the initial array and returns the count of active accounts 
     */
    public static int readAccounts(Bank myBank) throws IOException{
        //open input file, create scanner to read input databse file
        Scanner sc2 = new Scanner (new File ("InitDatabase.txt"));
        final int MAX_NUM = 50;
        Account[] myAccounts = new Account [MAX_NUM];
        int count = 0;
        
        while (sc2.hasNext() && count < MAX_NUM){
            
            String line = sc2.nextLine(); //read the next line of data
            String[] tokens = line.split(" "); //tokenize the line
            
            Name myName = new Name(tokens[4], tokens[5]);            
            Depositor myDepositor = new Depositor(myName, tokens[3]);
            
            if (tokens[2].equalsIgnoreCase("CD")){
                CDAccount cd = new CDAccount (myDepositor, Integer.parseInt(tokens[0]), tokens[7],
                        tokens[2], Double.parseDouble(tokens[1]), tokens[6]);
                
                myAccounts[count] = cd;
                myBank.readAnotherAccount(myAccounts[count]);
                
            } else if (tokens[2].equalsIgnoreCase("Checking")){
                CheckingAccount checking = new CheckingAccount (myDepositor, Integer.parseInt(tokens[0]), tokens[6],
                        tokens[2], Double.parseDouble(tokens[1]));
                
                myAccounts[count] = checking;
                myBank.readAnotherAccount(myAccounts[count]);
                
            } else {
                SavingsAccount savings = new SavingsAccount (myDepositor, Integer.parseInt(tokens[0]), tokens[6],
                        tokens[2], Double.parseDouble(tokens[1]));
                
                myAccounts[count] = savings;
                myBank.readAnotherAccount(myAccounts[count]);
            }
            count++;

        }//end while
        //close input file
        sc2.close();
        
        //return the account number count
        return count;
    }//end method readAccounts
        
    /* 2-method printAccounts:
     * Input:
     *  myBank - database of bank accounts
     *  pw - reference to PrintWriter writing to Output.txt output file
     * Process:
     *  Prints the database
     * Output: 
     *  Prints the database
    */
    public static void printAccounts(Bank myBank, PrintWriter pw) throws IOException{
        pw.println("\n\t   *********************************************************************************");
        pw.println("\t\t\t\t\tDatabase of Bank Accounts");
        pw.println("\t   *********************************************************************************");
        pw.println("Last Name  First Name         SSN   Account Number  "
                + "Account Status  Account Type    Balance  Maturity Date");
        
        for (int index = 0; index < myBank.getNumAccounts(); index++){
            Account myAccount = myBank.getAccountCopy(index);
            Depositor myDepositor = myAccount.getDepositorCopy();
            Name myName = myDepositor.getNameCopy();
            String myFirstName = myName.getFirstName();
            String myLastName = myName.getLastName();

            pw.print(myAccount.toString());
        }//end for loop
        
        pw.println("************************************************************"
                + "**********************************************");
        pw.printf("The total amount in all the checking accounts is $%.2f\n",
                myBank.getTotalAmountInCheckAccounts());
        pw.printf("The total amount in all the savings accounts is $%.2f\n",
                myBank.getTotalAmountInSavingsAccounts());
        pw.printf("The total amount in all the CD accounts is $%.2f\n",
                myBank.getTotalAmountInCDAccounts());
        pw.printf("The total amount in all types of accounts is $%.2f\n", 
                myBank.getTotalAmountInAllAccounts());
        pw.println("************************************************************"
                + "**********************************************\n");
        //flush the output file
        pw.flush();
    }//end method printAccounts
    
    /*3-method menu()
    *Input: none
    *Process: Prints the menu of transaction choices
    *Output: Prints the menu of transaction choices
    */
    public static void menu(){
        System.out.println("Select one of the following transactions");
        System.out.println("\t******************************");
        System.out.println("\t      List of choices");
        System.out.println("\t******************************");
        System.out.println("\t      W -- Withdrawal");
        System.out.println("\t      D -- Deposit");
        System.out.println("\t      C -- Clear Check");
        System.out.println("\t      N -- New Account");
        System.out.println("\t      B -- Balance Inquiry");
        System.out.println("\t      I -- Account Info (without transaction history)");
        System.out.println("\t      H -- Account Info plus Account Transaction History");
        System.out.println("\t      S -- Close Account (shut, but do not delete)");
        System.out.println("\t      R -- Reopen a closed account");
        System.out.println("\t      X -- Delete Account");
        System.out.println("\t      Q -- Quit");
        System.out.println();
        System.out.println("\tEnter your selection with a single letter: ");
    }//end menu method
    
    /*4-Method findAcct
    *Input: myBank - database of bank accounts
    *       requestedAccount - requested account number to find
    *Process: Performs a linear search on the acctNumArray for requested account
    *Outut: If found, the index of the requested account will be returned.
            Otherwise, returns -1        
    */
    public static int findAcct(Bank myBank, int requestedAccount) throws IOException {
        
        for (int index = 0; index < myBank.getNumAccounts(); index++){
            if (myBank.getAccountCopy(index).getAccountNum() == requestedAccount){
                return index;
            }   
        }//end for
        return -1;
       
    }//end findAcct method
    
    
    /*5-Method balance:
    *Input: myBank - database of bank accounts
    *pw - reference to the PrintWriter writing to output file
    *sc - reference to the Scanner reading from test case input file
    *Process: Prompts for the requested amount, creates a ticket which is used
    *to call getBalance methods from Bank and Account classes. A receipt is
    *received from these classes and printed.
    *Output: If the account exists, the balance is printed. Otherwise, an error
    *message is printed
    */
    public static void balance (Bank myBank, PrintWriter pw, Scanner sc) throws IOException{        
        
        int requestedAccount;
        TransactionTicket ticket;
        TransactionReceipt receipt;
        
        System.out.println("\nEnter the account number: "); 
        requestedAccount = sc.nextInt(); //prompt for account number and read in
        
        ticket = new TransactionTicket(requestedAccount, "Balance Inquiry");
        receipt =  myBank.getBalance(ticket);         

        pw.print(receipt);
        pw.println();
        pw.flush(); //flush the output buffer         
    }//end method balance

    /*6-method deposit
    *Input: myBank - database of bank accounts
    *       pw - reference to PrintWriter writing to output file
    *       sc - reference to Scanner reading from test cases input file
    *Process: Prompts for requested account, amount to deposit and if needed
    *         new maturity date. Creates a ticket with the info received and uses it
    *         to call on makeDeposit methods from Bank and Account classes. These latter
    *         return an adequate receipt which is printed.
    *Output: For valid deposit, the deposit transaction is printed. Otherwise,
    *        an error message is printed
    */
    public static void deposit(Bank myBank, PrintWriter pw, Scanner sc) throws IOException{
        
        int requestedAccount;
        TransactionTicket ticket;
        TransactionReceipt receipt;
        double amountToDeposit;
        int index;
        int termOfCD = 0;
        
        System.out.println("\nEnter the account number: ");
        requestedAccount = sc.nextInt();
        
        //call findAcct to search if requestedAccount exists
        index = findAcct (myBank, requestedAccount);
        //ticket = new TransactionTicket(requestedAccount, "Deposit");
        //receipt = myBank.makeDeposit(ticket);
        
        if (index == -1){ //invalid account
            ticket = new TransactionTicket(requestedAccount, "Deposit");
            receipt = myBank.makeDeposit(ticket);
            //pw.println("Transaction Requested: " + ticket.getTypeOfTransaction());
            //pw.println("Transaction Date: " + ticket.getDateOfTransactionString());
            //pw.println(receipt.getReasonForFailureString());          
            pw.print(receipt);
            
        } else { // valid account
            System.out.println("Enter amount to deposit: ");            
            amountToDeposit = sc.nextDouble(); //read in amount to deposit
            
            if (amountToDeposit <= 0) {//invalid amount
                ticket = new TransactionTicket (requestedAccount, 
                        amountToDeposit, "Deposit");
                receipt = myBank.makeDeposit(ticket);
            
            } else {//valid amount                
                if (myBank.getAccount(index).getType().equals("CD") && amountToDeposit > 0){//CD acount              
                System.out.println("Enter in how many months you would like your "
                        + "new maturity date to be (6, 12, 18 or 24): ");
                termOfCD = sc.nextInt();
                ticket = new TransactionTicket (requestedAccount, amountToDeposit, 
                        "Deposit", termOfCD);            
                receipt = myBank.makeDeposit(ticket);
                }
                else { //nonCD account
                    ticket = new TransactionTicket (requestedAccount, amountToDeposit, 
                            "Deposit");
                    receipt = myBank.makeDeposit(ticket);
                }
            }
            pw.print(receipt);
        }
        pw.println();
        pw.flush(); // flush the output file
    }//end deposit method
    
    
    /*7-method withdrawal
    *Input: myBank - database of bank accounts
    *       pw - reference to PrintWriter writing to output file
    *       sc - reference to Scanner reading from test cases input file
    *Process: Prompts for requested account, amount to withdraw and if needed new
    *         maturity date. Uses this info to create a ticket which is used to
    *         call makeWithdrawal methods from Bank and Account classes. These
    *         these latter return an adequate receipt which is printed.
    *Output: For valid withdraw, the deposit transaction is printed. Otherwise,
    *        an error message is printed
    */
    public static void withdrawal(Bank myBank, PrintWriter pw, Scanner sc) throws IOException{
        
        int requestedAccount;
        TransactionTicket ticket;
        TransactionReceipt receipt;
        double amountToWithdraw;
        int index;
        
        System.out.println("\nEnter the account number: ");
        requestedAccount = sc.nextInt(); //prompt for account to withdraw from
        
        //call findAcct to search if requestedAccount exists
        index = findAcct (myBank, requestedAccount);
        
        if (index == -1){ //invalid account
            ticket = new TransactionTicket(requestedAccount, "Withdrawal");
            receipt = myBank.makeWithdrawal(ticket);
            //pw.println("Transaction Requested: " + ticket.getTypeOfTransaction());
            //pw.println("Transaction Date: " + ticket.getDateOfTransactionString());
            //pw.println(receipt.getReasonForFailureString());
            pw.print(receipt);
        
        } else { // valid account
            System.out.println("Enter amount to withdraw: ");            
            amountToWithdraw = sc.nextDouble(); //read in amount to deposit            
            int termOfCD = 0;
            
            if (myBank.getAccountCopy(index).getType().equals("CD")){
                System.out.println("Enter in how many months you would like your "
                        + "new maturity date to be (6, 12, 18 or 24): ");
                termOfCD = sc.nextInt();
            }
            ticket = new TransactionTicket (requestedAccount, amountToWithdraw, 
                    "Withdrawal", termOfCD);
            receipt = myBank.makeWithdrawal(ticket);

            pw.print(receipt);
        }
        pw.println();
        pw.flush(); // flush the output file
    }//end withdrawal method

    /*9-method newAccount
    *Input: myBank - database of bank accounts
    *       pw - reference to PrintWriter writing to output file
    *       sc - reference to Scanner reading from test cases input file
    *Process: Prompts for requested account, calls findacct() to see if account
    *         already exists, if account number does already exist an error message is
    *         printed. If the account exists the method will prompt for additional
    *         new account info and a ticket is made to call on openNewAccount method
    *         in Bank class to make the new account.
    *Output: The number of active accounts is printed and returned. Adequate
    *        transaction receipts are printed for successful and failed transactions.
    */    
    public static int newAccount(Bank myBank, PrintWriter pw, Scanner sc) throws IOException{
        
        int requestedAccount;
        TransactionTicket ticket;
        TransactionReceipt receipt;
        Account myAccount = new Account();
        int index;    
        
        System.out.println("\nEnter the account number for new account: ");
        
        requestedAccount = sc.nextInt(); //prompt for new account number
        
        //call on findAcct to see if account already exists
        index = findAcct (myBank, requestedAccount);
        
        if (index != -1){ //account number already exists
            ticket = new TransactionTicket (requestedAccount, "New Account Creation");
            receipt = myBank.openNewAccount(ticket, myAccount);
            
            //pw.println("Transaction Requested: " + ticket.getTypeOfTransaction());
            //pw.println(receipt.getReasonForFailureString());
            pw.print(receipt);
            
        }
        else { //account number does not already exist
            System.out.println("Please enter the new depositor's first name followed " +
                    "by last name: ");
            String firstName = sc.next(), lastName = sc.next();
            System.out.println("Now please enter the depositor's SSN: ");
            String mySSN = sc.next();
            System.out.println("Now please enter account type (Checking/Savings/CD: ");
            String myType = sc.next();
            System.out.println("Please enter the initial opening deposit: ");
            Double myBalance = sc.nextDouble();
            
            Name myName = new Name (firstName, lastName);
            Depositor myDepositor = new Depositor (myName, mySSN);
            
            if (myType.equals("CD")){
                System.out.println("Please enter in how many months you would like " +
                        "your maturity date to be (6/12/18/24): ");
                int termOfCD = sc.nextInt();
                
                ticket = new TransactionTicket (requestedAccount, myBalance, 
                        "New Account Creation", termOfCD);
                
                Calendar today = ticket.getDateOfTransaction();
                Calendar newMaturity = Calendar.getInstance();                
                newMaturity.add(Calendar.MONTH, termOfCD);
                
                CDAccount cd = new CDAccount (myDepositor, requestedAccount, "open", myType, myBalance, newMaturity);
                //CDAccount cdAccount = new CDAccount (myAccount);
                //receipt = myBank.openNewAccount(ticket, cdAccount);
                myAccount = cd;
                
            } else if (myType.equalsIgnoreCase("Checking")){
                ticket = new TransactionTicket (requestedAccount, myBalance, "New Account Creation");
                myAccount = new CheckingAccount (myDepositor, requestedAccount, "open", myType, myBalance);

            } else {
                ticket = new TransactionTicket (requestedAccount, myBalance, "New Account Creation");
                myAccount = new SavingsAccount (myDepositor, requestedAccount, "open", myType, myBalance);
            }            
            
            receipt = myBank.openNewAccount(ticket, myAccount);
            
            pw.print(receipt);
      
        }
        pw.println("The number of active accounts is " + myBank.getNumAccounts());
        pw.println();
        pw.flush();
        return myBank.getNumAccounts();
    }//end newAccount method
    
    
    /*9-method deleteAccount
    *Input: myBank - database of bank accounts
    *       pw - reference to PrintWriter writing to output file
    *       sc - reference to Scanner reading from test cases input file
    *Process: Prompts for requested account, and creates a ticket which is used
    *         to call on deleteAccount method in bank class. This latter returns
    *         an adequate receipt which is printed.
    *
    *Output: The number of active accounts is printed and returned. Adequate
    *        transaction receipts are printed for successful and failed transactions.
    */
    public static int deleteAccount(Bank myBank, PrintWriter pw, Scanner sc) throws IOException{
        
        int requestedAccount;
        TransactionTicket ticket;
        TransactionReceipt receipt;   
        
        System.out.println("\nEnter the number of the account to be deleted: ");
        requestedAccount = sc.nextInt(); //ask for number of account to delete
        ticket = new TransactionTicket (requestedAccount, "Account Deletion");
        receipt = myBank.deleteAccount(ticket);        

        pw.print(receipt);
        pw.println("The number of active accounts is " + myBank.getNumAccounts());
        pw.println();
        pw.flush();
        return myBank.getNumAccounts();
    }//end deleteAccount method
    
    /*10- method AccountInfo
    *Input: myBank - database of bank accounts
    *pw - reference to the PrintWriter writing to output file
    *sc - reference to the Scanner reading from test case input file
    *Process: Prompts user for a social security number. If no account exists
    *         with the requested SSN then an error message is printed. Otherwise prints
    *         complete account info for all accounts with this SSN.
    *Output: For SSN associated with active accounts, prints account info for
    *        all accounts with that SSN. Otherwise, error message is printed
    */
    public static void accountInfo(Bank myBank, PrintWriter pw, Scanner sc) throws IOException{
        int accountsWithSSN = 0;
        
        String requestedSSN;        
        
        System.out.println("Enter the SSN for which you want account info: ");
        requestedSSN = sc.next();
        
        pw.println("Transaction Requested: Account Info");
        pw.println("Requested SSN: " + requestedSSN);
        
        for (int index = 0; index< myBank.getNumAccounts(); index++){
            if (myBank.getAccount(index).getDepositor().getSsn().equalsIgnoreCase(requestedSSN)){
                accountsWithSSN++;
            }//end if
        }//end for
        
        if (accountsWithSSN == 0){
            pw.println("Error: no accounts found with requested SSN!\n");
        } else {
            pw.println("\t**************************************************************************************");
            pw.println("Last Name  First Name         SSN   Account Number  "
                + "Account Status  Account Type    Balance  Maturity Date");
        
            for (int index = 0; index < myBank.getNumAccounts(); index++){
                Account myAccount = myBank.getAccountCopy(index);
                Depositor myDepositor = myAccount.getDepositorCopy();
                Name myName = myDepositor.getNameCopy();
                if (myDepositor.getSsn().equalsIgnoreCase(requestedSSN)){

                    pw.print(myAccount);

                }//end if 
            }//end for
            pw.println("\t**************************************************************************************");
            pw.println(accountsWithSSN + " accounts found with requested "
                    + "SSN\n");
        }    
    }//end accountInfo

    
    /*11- method clearCheck
    *Input: myBank - database of bank accounts
    *pw - reference to the PrintWriter writing to output file
    *sc - reference to the Scanner reading from test case input file
    *Process: Prompts user for account number, amount of check and date of check.
    *         The info received is used to created a check and ticket object which
    *         are then used to call on clearCheck methods in Bank and Account classes.
    *         these latter return an adequate receipt which is printed.
    *Output: Adequate receipts are printed for failed and successful transactions.
    *        Therefore,if the account number does not exists, or the account is not
    *        a checkings account or the amount of check is invalid(<0), or if the
    *        date of check is not within the past 6 months an error message is printed.
    */
    public static void clearCheck (Bank myBank, PrintWriter pw, Scanner sc) throws IOException{

        TransactionReceipt receipt;
        
        System.out.println("\nEnter the account number: ");
        int requestedAccount = sc.nextInt(); //prompt for account to withdraw from
        System.out.println("Enter the amount of the check: ");
        double checkAmount = sc.nextDouble();
        System.out.println("Enter the date of the check (in format xx/xx/xxxx): ");
        String checkDate = sc.next();
        
        Check check = new Check (requestedAccount, checkAmount, checkDate);
        TransactionTicket ticket = new TransactionTicket (requestedAccount, checkAmount, "Clear Check");     
        receipt = myBank.clearCheck(check, ticket);

        pw.print(ticket);
        pw.println("***********************************************");        
        pw.print(check);
        pw.println("***********************************************");

        pw.print(receipt);
        pw.println();        
    }//end clearCheck method
    
    
    /*12- method closeAccount
    *Input: myBank - database of bank accounts
    *pw - reference to the PrintWriter writing to output file
    *sc - reference to the Scanner reading from test case input file
    *Process: Prompts user for account number. The info received is used to 
    *         create a ticket object which is used to call on closeAccount methods in
    *         Bank and Account classes. These give an error if account is already closed, else change
    *         the account status to closed. They then return an adequate receipt which
    *         is printed by this method.
    *Output: Adequate receipts are printed for failed and successful transactions.
    *        Therefore, if the account is already closed, an error message is printed.
    *        If the account is currently opened, a successful receipt is printed.
    */    
    public static void closeAccount (Bank myBank, PrintWriter pw, Scanner sc) throws IOException{        
        
        int requestedAccount;
        TransactionTicket ticket;
        TransactionReceipt receipt;
        
        System.out.println("\nEnter the Number of the Account you would like to close: "); 
        requestedAccount = sc.nextInt(); //prompt for account number and read in
        
        ticket = new TransactionTicket(requestedAccount, "Close Account");
        receipt =  myBank.closeAccount(ticket);         

        pw.print(receipt);
        pw.println();
        pw.flush(); //flush the output buffer   
    }//end closeAccount method
    
    
    /*13- method reopenAccount
    *Input: myBank - database of bank accounts
    *pw - reference to the PrintWriter writing to output file
    *sc - reference to the Scanner reading from test case input file
    *Process: Prompts user for account number. The info received is used to 
    *         create a ticket object which is used to call on reopenAccount methods in
    *         Bank and Account classes. These give an error if account is already open, else change
    *         the account status to open. They then return an adequate receipt which
    *         is printed by this method.
    *Output: Adequate receipts are printed for failed and successful transactions.
    *        Therefore, if the account is already open, an error message is printed.
    *        If the account is currently closed, a successful receipt is printed.
    */        
    public static void reopenAccount (Bank myBank, PrintWriter pw, Scanner sc) throws IOException{
        
        int requestedAccount;
        TransactionTicket ticket;
        TransactionReceipt receipt;
        
        System.out.println("\nEnter the Number of the Account you would like to reopen: "); 
        requestedAccount = sc.nextInt(); //prompt for account number and read in
        
        ticket = new TransactionTicket(requestedAccount, "Reopen Account");
        receipt =  myBank.reopenAccount(ticket);         

        pw.print(receipt);
        pw.println();
        pw.flush(); //flush the output buffer        
    }//end reopenAccount method
    
    /*14- method AccountInfoHistory
    *Input: myBank - database of bank accounts
    *pw - reference to the PrintWriter writing to output file
    *sc - reference to the Scanner reading from test case input file
    *Process: Prompts user for a social security number. If no account exists
    *         with the requested SSN then an error message is printed. Otherwise prints
    *         complete account info for all accounts with this SSN. The method then
    *         additionally printed the entire transaction history for each account
    *         number associaed with the requested SSN
    *Output: For SSN associated with active accounts, prints account info for
    *        all accounts with that SSN. Otherwise, error message is printed
    */
    public static void accountInfoHistory(Bank myBank, PrintWriter pw, Scanner sc) throws IOException{
        int accountsWithSSN = 0;
        
        String requestedSSN;        
        
        System.out.println("Enter the SSN for which you want account info: ");
        requestedSSN = sc.next();
        
        pw.println("Transaction Requested: Account Info Plus Transaction History");
        pw.println("Requested SSN: " + requestedSSN);
        
        for (int index = 0; index< myBank.getNumAccounts(); index++){
            if (myBank.getAccountCopy(index).getDepositor().getSsn().equalsIgnoreCase(requestedSSN)){
                accountsWithSSN++;
            }//end if
        }//end for
        
        if (accountsWithSSN == 0){
            pw.println("Error: no accounts found with requested SSN!\n");
        } else {
            pw.println("\t**************************************************************************************");
            pw.println("Last Name  First Name         SSN   Account Number  "
                + "Account Status  Account Type    Balance  Maturity Date");
        
            for (int index = 0; index < myBank.getNumAccounts(); index++){
                Account myAccount = myBank.getAccountCopy(index);
                Depositor myDepositor = myAccount.getDepositorCopy();
                Name myName = myDepositor.getNameCopy();
                if (myDepositor.getSsn().equalsIgnoreCase(requestedSSN)){

                    pw.print(myAccount);
                }//end if 
            }//end for
            pw.println("\t**************************************************************************************");
            pw.println(accountsWithSSN + " accounts found with requested "
                    + "SSN\n");
        }
        
        pw.println("Transaction History");
        pw.println("*******************************");
        for (int index = 0; index < myBank.getNumAccounts(); index++){               
            if (myBank.getAccountCopy(index).getDepositor().getSsn().equalsIgnoreCase(requestedSSN)){
                TransactionTicket ticket = new TransactionTicket (myBank.getAccountCopy(index).getAccountNum(), 
                        "Account Info History");
                //ArrayList <TransactionReceipt> receiptArray = 
                        //myBank.getAccountCopy(index).getTransactionReceipt(ticket);
                Account account = myBank.getAccount(index);
                int numOfReceipts = account.getNumOfReceipts();
                pw.println("Account Number: " + ticket.getAccountNum());
                pw.println("------------------------------");
                
                if (numOfReceipts == 0){
                    pw.println("No transactions found under this account number");
                }
                
                for (int receiptNum=0; receiptNum < numOfReceipts; receiptNum++) {
                    TransactionReceipt receipt = account.getTransactionReceipt(receiptNum);
                
                    int transactionNum = receiptNum + 1;
                    pw.println("Transaction #" + transactionNum);
                    
                    if (receipt.getTransactionTicketCopy().getTypeOfTransaction().equals("Clear Check")){
                        pw.print(receipt.getTransactionTicketCopy().toString());
                        pw.printf("Amount of Check: $%.2f\n", receipt.getTransactionTicketCopy().getAmountOfTransaction());
                        //pw.println("***********************************************");        
                        //pw.print(check);
                    }
                    pw.print(receipt);

                    pw.println("------------------------------");
                }//end for
                pw.println();
            }//end if
        }//end for
        pw.println();
    }//end accountInfoHistory
    
    
    /*Method pause90 */
    public static void pause (Scanner sc){
        String tempstring;
        System.out.println("%npress Enter to continue");
        tempstring = sc.nextLine(); //flush previous enter
        tempstring = sc.nextLine(); //wait for ENTER
    }
        
    
}
        
