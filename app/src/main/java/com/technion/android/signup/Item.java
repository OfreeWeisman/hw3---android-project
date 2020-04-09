package com.technion.android.signup;

public class Item {
    private String firstLetter;
    private String input;

    Item(){}

    Item(String input){
        this.input = input;
        firstLetter = input.substring(0,1);
    }

    public String getInput() {
        return input;
    }
    public String getFirstLetter(){
        return firstLetter;
    }
}
