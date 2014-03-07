package com.skripsi.atma; 

import android.app.Activity; 
import android.content.Intent; 
import android.os.Bundle; 
import android.view.View; 
import android.widget.Button; 

public class HangmanHome extends Activity { 

    @Override 
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_hangman__home); 

        Button btnNewGame = (Button)findViewById(R.id.new_button); 
        Button btnAbout = (Button)findViewById(R.id.about_button); 
        Button btnExit = (Button)findViewById(R.id.exit_button); 

        btnNewGame.setOnClickListener(new View.OnClickListener() { 

                       @Override 
                      public void onClick(View v) { 
                    	   Intent newGame = new Intent(HangmanHome.this,Hangman.class); 
                           HangmanHome.this.startActivity(newGame); 
                                } 
               }); 

        btnAbout.setOnClickListener(new View.OnClickListener() { 

                       @Override 
                      public void onClick(View v) { 

                               Intent aboutIntent = new Intent(HangmanHome.this,HangmanAbout.class); 
                               HangmanHome.this.startActivity(aboutIntent); 
                        } 
                });

        btnExit.setOnClickListener(new View.OnClickListener() { 

                        @Override 
                        public void onClick(View v) { 
                               finish(); 
                        } 
                }); 
    } 
} 