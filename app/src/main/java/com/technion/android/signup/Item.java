package com.technion.android.signup;

public class Item {
    private String first_letter;
    private String text;

    public Item(String input){
        text = input;
        first_letter = input.substring(0,1);
    }

    public String getText() {
        return text;
    }
    public String getFirstLetter(){
        return first_letter;
    }
}
