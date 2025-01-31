/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.account.contract;

/**
 *
 * @author user
 */
public interface TraderAccount extends TraderOperation, AccountContext{

    public void signUp(String email, byte[] hash_password, String first_name, String last_name);

    public void login(int account_number, byte[] hash_password);
    
    public void logout(int account_number);
    
    public int getAccountNumber();
    
    public String getAccountName();    
    
    public char[] getPassword();   

    public boolean isLoggedIn();

    public void setIsLoggeIn(boolean is_logged_in);

}
