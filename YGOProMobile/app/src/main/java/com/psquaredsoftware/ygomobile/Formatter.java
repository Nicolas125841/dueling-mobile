package com.psquaredsoftware.ygomobile;

public class Formatter {

    public String formatURL(String in){

         char[] array = in.toCharArray();
         String ret = "";
         for(int i = 0; i < array.length; i++){

             if(array[i] == ' '){
                ret += "%20";
             }else
                 ret += array[i];

         }
        return ret;
    }

    public String formatURLReverse(String in){

        char[] array = in.toCharArray();
        String ret = "";
        for(int i = 0; i < array.length; i++){

            if(i < array.length - 4 && array[i] == '%' && array[i + 1] == '2' && array[i + 2] == '0'){
                ret += " ";
                i+= 2;
            }else
                ret += array[i];

        }
        return ret;
    }

}
