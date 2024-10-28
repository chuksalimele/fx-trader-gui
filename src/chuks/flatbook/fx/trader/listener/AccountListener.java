/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package chuks.flatbook.fx.trader.listener;

/**
 *
 * @author user
 */
public interface AccountListener {
    void onLoggedIn();
    void onLogInFailed(String reason);
    void onLoggedOut();
    void onLogOutFailed(String reason);
    void onAccountOpen(int account_number);
    void onSignUpFail(String reason);
    void onAccountDisabled();
    void onAccountEnabled();
    void onAccountApproved();
    void onAccountClosed();
    void onPasswordChanged(char[] new_password);
}
