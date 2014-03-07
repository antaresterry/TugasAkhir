package com.skripsi.atma; 

import android.app.Activity; 
import android.content.Intent; 
import android.os.Bundle; 
import android.view.View; 
import android.widget.Button; 

public class EngineHome extends Activity { 

    @Override 
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_engine_main); 

        Button btnNewGame = (Button)findViewById(R.id.new_button); 
        Button btnExit = (Button)findViewById(R.id.exit_button); 
        Button btnAbout = (Button)findViewById(R.id.about_button); 

        btnNewGame.setOnClickListener(new View.OnClickListener() { 

                       @Override 
                      public void onClick(View v) { 
                    	   Intent newGame = new Intent(EngineHome.this,EngineGame.class); 
                           EngineHome.this.startActivity(newGame); 
                                } 
               });
        
        btnAbout.setOnClickListener(new View.OnClickListener() { 

            @Override 
           public void onClick(View v) { 
         	   Intent tts = new Intent(EngineHome.this,EngineTips.class);
                EngineHome.this.startActivity(tts); 
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