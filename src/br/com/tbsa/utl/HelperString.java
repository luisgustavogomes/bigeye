package br.com.tbsa.utl;

import org.apache.commons.codec.binary.Base64;

public class HelperString {

    public static String cript(String senha) {
        byte[] bytes = Base64.encodeBase64(senha.getBytes());
        String retorno = new String(bytes);
        return retorno;
    }

    public static String decript(String senha) {
        byte[] bytes = Base64.decodeBase64(senha.getBytes());
        String retorno = new String(bytes);
        return retorno;
    }

    public static Boolean is(String msg) {
        return msg.equals("true");
    }

}
