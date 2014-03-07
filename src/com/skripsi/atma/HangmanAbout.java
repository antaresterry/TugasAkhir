package com.skripsi.atma; 

import android.app.Activity; 
import android.os.Bundle; 
import android.view.View; 
import android.widget.Button; 
import android.widget.TextView;

public class HangmanAbout extends Activity { 

         @Override 
     public void onCreate(Bundle savedInstanceState) { 
          super.onCreate(savedInstanceState); 
          setContentView(R.layout.activity_hangman__about); 

          Button btnExit = (Button)findViewById(R.id.exit_button); 
          btnExit.setOnClickListener(new View.OnClickListener() { 

                           @Override 
                           public void onClick(View v) { 
                                    finish(); 
                           } 
                  }); 
          TextView about = (TextView)findViewById(R.id.about); 
          about.setOnClickListener(new View.OnClickListener() { 

                           @Override 
                           public void onClick(View v) { 
                                    finish(); 
                           } 
                  }); 
          } 
} 