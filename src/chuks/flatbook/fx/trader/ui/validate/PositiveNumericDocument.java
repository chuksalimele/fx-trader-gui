/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.trader.ui.validate;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

/**
 *
 * @author user
 */
public class PositiveNumericDocument extends PlainDocument {

    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str != null && isValidInput(str)) {
            if(!str.equals("."))
            super.insertString(offset, str, attr);
        }
    }

    @Override
    public void replace(int offset, int length, String str, AttributeSet attrs) throws BadLocationException {
        if (str != null && isValidInput(str)) {
            
            //String allStr = this.getText(0, this.getLength());
            //if(allStr.contains(".") && str.contains(".") && offset != 0){
            //    return;
            //}
            super.replace(offset, length, str, attrs);
        }
    }

    private boolean isValidInput(String text) throws BadLocationException {
        // Regex to allow only valid positive decimal numbers
        
        

        String regex = "^\\d*(\\.\\d*)?$";
        return text.matches(regex);
    }

}
