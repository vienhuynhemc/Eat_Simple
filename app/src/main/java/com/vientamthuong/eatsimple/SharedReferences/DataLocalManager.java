package com.vientamthuong.eatsimple.SharedReferences;

import android.content.Context;

import com.google.gson.Gson;

public class DataLocalManager {
    private static final String REF_FIRST = "REF_FIRST";
    private static final String REF_VALID = "REF_VALID";
    private static final String REF_ACCOUNT = "REF_ACCOUNT";
    private static DataLocalManager instance;
    private MyShareReferences myShareReferences;

    public static void init(Context context){
        instance = new DataLocalManager();
        instance.myShareReferences = new MyShareReferences(context);
    }
    public static DataLocalManager getInstance(){
        if(instance == null){
            instance = new DataLocalManager();
        }
        return instance;
    }
    public static void setFirstInstalled(boolean isFirst){
        DataLocalManager.getInstance().myShareReferences.putBooleanValue(REF_FIRST,isFirst);
    }
    public static boolean getFirstInstalled(){
        return DataLocalManager.getInstance().myShareReferences.getBooleanValue(REF_FIRST);
    }
//    public static void setAccount(Customer account){
//        Gson gson = new Gson();
//        String stringJSONAccount = gson.toJson(account);
//        DataLocalManager.getInstance().myShareReferences.putStringValue(REF_ACCOUNT,stringJSONAccount);
//    }
//    public static Customer getAccount(){
//        Gson gson = new Gson();
//        String stringJSONAccount = DataLocalManager.getInstance().myShareReferences.getStringValue(REF_ACCOUNT);
//        Customer account = gson.fromJson(stringJSONAccount,Customer.class);
//        return account;
//    }
    public static void setValid(boolean input){
        DataLocalManager.getInstance().myShareReferences.putBooleanValue(REF_VALID,input);
    }
    public static boolean isValid(){
        return DataLocalManager.getInstance().myShareReferences.getBooleanValue(REF_VALID);
    }

}
