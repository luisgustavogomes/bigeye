/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.tbsa.utl;

/**
 *
 * @author Luis Gustavo
 */
public class HelperType {

    public static boolean isNumericDouble(String s) throws Exception {
        try {
            double num = Double.parseDouble(s);
            return num > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNumericInteger(String s) throws Exception {
        try {
            int num = Integer.parseInt(s);
            return num > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static Byte converterBooleanToByte(Boolean bool) {
        if (bool) {
            return 1;
        }
        return 0;
    }

}
