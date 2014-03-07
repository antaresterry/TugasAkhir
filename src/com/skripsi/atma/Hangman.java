package com.skripsi.atma;

import java.util.ArrayList;
import java.util.Random;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.*;
import android.view.View.OnKeyListener;
import android.widget.*;

@SuppressLint("DefaultLocale")
public class Hangman extends Activity {

    public static final String PREFS_NAME = "Hangman_pref"; 

     TextView hiddenText; 
     TextView hintText; 
     TextView enteredText; 
     EditText input; 
     ImageView hangpic;
     CountDownTimer mCountDownTimer;
     public int i=0;

    // text variable holds String representation of the hidden word 
     String text; 
    // hint 
     String hint; 
    // holds a String of all letters already inputed by user 
     String entered = ""; 
    //count of errors 
    int count; 

	@SuppressLint("DefaultLocale")
	@Override 
public void onCreate(Bundle savedInstanceState) { 
     super.onCreate(savedInstanceState); 
     setContentView(R.layout.activity_hangman); 
     
     //getting new hidden word 
     String texthint = getText(); 
     String[] pair = texthint.split("#"); 
     text = pair[0]; 
     hint = pair[1]; 

     //assembling UI 
     hintText = (TextView)findViewById(R.id.hint_txt); 
     hintText.setText(hint); 

     hiddenText = (TextView)findViewById(R.id.hidden_txt); 
     hiddenText.setText(encodeString(text, "*")); 

     enteredText = (TextView)findViewById(R.id.entered_txt); 
     enteredText.setText(entered); 

     hangpic = (ImageView)findViewById(R.id.hangpic); 
     count = 6; //setting count 
     changeImage(count); // changing background 

     //setting input 
     input = (EditText)findViewById(R.id.input_txt); 
     input.setFocusable(true); 
     input.setOnKeyListener(new OnKeyListener() { 

					@SuppressLint("DefaultLocale")
					@Override 
                    public boolean onKey(View v, int keyCode, KeyEvent event) { 
                            String temp; 
                            String newLetter = "&"; 

                            newLetter = input.getText().toString(); 

                            temp = (String)enteredText.getText(); 
                            if(temp.indexOf(newLetter.toUpperCase()) >= 0) { 
                                     input.setText(""); 
                                     return false; 
                            } 
                             input.setText(""); //clearing input 
                                 entered += newLetter.toUpperCase(); //adding inputed letter to the entered String 
                                 enteredText.setText(temp + newLetter.toUpperCase()); 
                                 hiddenText.setText(encodeString(text, newLetter.toUpperCase())); 

                                 //checking for win case 
                                 if(win(text)) { 
                                          Toast.makeText(getApplicationContext(), "Congratulations!\n\tYou won!", Toast.LENGTH_LONG).show(); 
                                          input.setFocusable(false);
                                          mCountDownTimer=new CountDownTimer(3000,1000) {

                                              @Override
                                              public void onTick(long millisUntilFinished) {
                                                  Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
                                                  i++;
                                              }

                                              @Override
                                              public void onFinish() {
                                              //Do what you want 
                                                  i++;
                                                  finish();
                                              }
                                          };
                                          mCountDownTimer.start();
                                } 
                                 //if not found in the hidden word decrement count 
                                 if(text.indexOf(newLetter.toUpperCase()) == -1) { 
                                          count--; 
                                          changeImage(count); //also change background (draw new part) 
                                } 
                                 //game over if there is no chances left 
                                 if(count == 0) { 
                                          Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_LONG).show(); 
                                          hiddenText.setText(encodeString(text, "#")); //display hidden word 
                                          input.setFocusable(false); //no more guesses
                                          finish();
                                } 

                                 return false; 
                       } 
              }); 
      } 

     private String encodeString( String target, String letter) { 
               String result = ""; 
               if(letter.contains("*")) { //this case will completely hide all letters 
                        for(int i = 0; i < target.length(); i++) { 
                                 result += "_ "; //there is a space bar between the letters 
                        } 
                        return result; 
               } 
               else if(letter.contains("#")) { //this case will open all letters (happens when user losses a game 
                        for(int i = 0; i < target.length(); i++) { 
                                 result += target.charAt(i) + " "; 
                        } 
                        return result; 
               } 
               else { //this case will open all instances of the correct letter 
                        for(int i = 0; i < target.length(); i++) { 
                                 if(target.charAt(i) == letter.charAt(0)) { 
                                           result += target.charAt(i) + " "; 
                                 } 
                                 else if(entered.indexOf(target.charAt(i)) != -1) { 
                                           result += target.charAt(i) + " "; 
                                 } 
                                 else 
                                           result += "_ "; 
                        } 
                        return result; 
               } 
      } 

     private boolean win(String text) { 
               if(text.length() > 0 && entered.length() > 0) { 
                        for(int i = 0; i < text.length(); i++) { 
                                 if(entered.indexOf(text.charAt(i)) == -1) { 
                                           return false; 
                                 } 
                        } 
                        return true; 
               } 
               return false; 
      } 

     private void changeImage(int count) { 
               switch (count) { 
               case 6: 
            	   hangpic.setBackgroundResource(R.drawable.hangman0); 
               break; 
               case 5: 
            	   hangpic.setBackgroundResource(R.drawable.hangman1); 
               break; 
               case 4: 
            	   hangpic.setBackgroundResource(R.drawable.hangman2); 
               break; 
               case 3: 
            	   hangpic.setBackgroundResource(R.drawable.hangman3); 
               break; 
               case 2: 
            	   hangpic.setBackgroundResource(R.drawable.hangman4); 
               break; 
               case 1: 
            	   hangpic.setBackgroundResource(R.drawable.hangman5); 
               break; 
               case 0: 
            	   hangpic.setBackgroundResource(R.drawable.hangman6); 
               break; 
               case 999: 
            	   hangpic.setBackgroundResource(R.drawable.ic_launcher); 
               break; 

     default: 
              hangpic.setBackgroundResource(R.drawable.hangman0); 
              break; 
     } 
} 

private String getText() { 
     String result = "CAPASITOR#ELEKTRO"; 

     // all hidden words 
     ArrayList<String> words = new ArrayList<String>(); 
 words.add("CAPASITOR#ELEKTRO"); 
 words.add("TRANSFORMATOR#ELEKTRO"); 
 words.add("AMPERE#ELEKTRO"); 
 words.add("CACHE#ELEKTRO"); 
 words.add("INTERFERENCY#ELEKTRO"); 
 words.add("TRAFFIC#GENERAL"); 
 words.add("FLOAT#GENERAL"); 
 words.add("CORRUPTION#GENERAL"); 
 words.add("CULTURE#GENERAL"); 
 words.add("GLOBALIZATION#GENERAL"); 
 words.add("INFLATION#ECONOMY"); 
 words.add("DEMAND#ECONOMY"); 
 words.add("PRODUCTIVITY#ECONOMY"); 
 words.add("QUANTITY#ECONOMY"); 
 words.add("OPPORTUNITY#ECONOMY"); 
 words.add("INTERPERSONAL#COMMUNICATION"); 
 words.add("INTERACTION#COMMUNICATION"); 
 words.add("VIRTUAL#COMMUNICATION"); 
 words.add("STRATEGY#COMMUNICATION"); 
 words.add("BRANDING#COMMUNICATION"); 
 words.add("DOCTRINE#THEOLOGY"); 
 words.add("TEACHER#EDUCATION"); 
 words.add("PARADIGMA#THEOLOGY"); 
 words.add("IDEOLOGY#THEOLOGY"); 
 words.add("METAPHYSICS#THEOLOGY"); 
 words.add("EXECUTIVE#LAW"); 
 words.add("LAWYER#LAW"); 
 words.add("RULES#LAW"); 
 words.add("PUNISHMENT#LAW"); 
 words.add("JURISDICTION#LAW"); 
 words.add("INFLUENZA#MEDICAL"); 
 words.add("DIAGNOSE#MEDICAL"); 
 words.add("ANATOMY#MEDICAL"); 
 words.add("ORTHOPEDY#MEDICAL"); 
 words.add("GENETIC#MEDICAL"); 
 words.add("RONTGEN#MEDICAL"); 
 words.add("STIMULUS#PHSYCOLOGY"); 
 words.add("APTITUDE#PHSYCOLOGY"); 
 words.add("COGNITIVE#PHSYCOLOGY"); 
 words.add("EMOTIONAL#PHSYCOLOGY"); 
 words.add("HABIT#PHSYCOLOGY"); 
 words.add("CATALYST#BIOLOGY"); 
 words.add("INCUBATOR#BIOLOGY"); 
 words.add("BIOREMEDIATION#BIOLOGY"); 
 words.add("FOSSIL#BIOLOGY"); 
 words.add("HOLTICULTURE#BIOLOGY"); 
 words.add("CONTROLLER#ENGINEERING"); 
 words.add("IGNITION#ENGINEERING"); 
 words.add("AUTOMATION#ENGINEERING"); 
 words.add("GENERATOR#ENGINEERING"); 
 words.add("STORM#WEATHER"); 
 words.add("WINTER#WEATHER"); 
 words.add("SUMMER#WEATHER"); 
 words.add("RAINY#WEATHER"); 
 words.add("SPRING#WEATHER"); 
 words.add("AUTUMN#WEATHER"); 

 Random generator = new Random(); 
 int randomIndex = generator.nextInt(words.size());
 result = words.get(randomIndex); 
 return result; 
 } 
}