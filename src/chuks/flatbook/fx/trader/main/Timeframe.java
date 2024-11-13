/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package chuks.flatbook.fx.trader.main;

/**
 *
 * @author user
 */
public class Timeframe {

    final public static int M1 = 60;
    final public static int M5 = M1 * 5;
    final public static int M15 = M1 * 15;
    final public static int M30 = M1 * 30;
    final public static int H1 = M1 * 60;
    final public static int H4 = H1 * 4;
    final public static int D1 = H1 * 24;
    final public static int W1 = D1 * 7;
    final public static int MN1 = D1 * 30;
    private int tf;
    private String strTf;
    public Timeframe(int tf) {
        strTf = toString(tf);
    }

    public Timeframe(String strTf) {
        tf = toInt(strTf);
    }
    
    public int getInt() {
        return tf;
    }
    
    public String getString() {
        return strTf;
    }    

    public static String toString(int tf) {
        String strTf = "";
        switch (tf) {
            case Timeframe.M1 ->
                strTf = "M1";
            case Timeframe.M5 ->
                strTf = "M5";
            case Timeframe.M15 ->
                strTf = "M15";
            case Timeframe.M30 ->
                strTf = "M30";
            case Timeframe.H1 ->
                strTf = "H1";
            case Timeframe.H4 ->
                strTf = "H4";
            case Timeframe.D1 ->
                strTf = "D1";
            case Timeframe.W1 ->
                strTf = "W1";
            case Timeframe.MN1 ->
                strTf = "MN1";
            default -> {
            }
        }
        return strTf;
    }

    public static int toInt(String strTf) {
        int tf = -1;
        switch (strTf) {
            case "M1" ->
                tf = Timeframe.M1;
            case "M5" ->
                tf = Timeframe.M5;
            case "M15" ->
                tf = Timeframe.M15;
            case "M30" ->
                tf = Timeframe.M30;
            case "H1" ->
                tf = Timeframe.H1;
            case "H4" ->
                tf = Timeframe.H4;
            case "D1" ->
                tf = Timeframe.D1;
            case "W1" ->
                tf = Timeframe.W1;
            case "MN1" ->
                tf = Timeframe.MN1;
            default -> {
            }
        }
        return tf;
    }
}
