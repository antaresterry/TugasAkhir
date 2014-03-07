package com.skripsi.atma;

import android.app.Activity; 
import android.content.Intent; 
import android.os.Bundle; 
import android.view.View; 
import android.widget.Button; 

public class ItemFindingHome extends Activity {
    @Override 
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_item__finding__home); 

        Button btnNewGame = (Button)findViewById(R.id.new_button); 
        Button btnScore = (Button)findViewById(R.id.score_button); 
        Button btnExit = (Button)findViewById(R.id.exit_button); 

        btnNewGame.setOnClickListener(new View.OnClickListener() { 

                       @Override 
                      public void onClick(View v) { 
                    	   Intent newGame = new Intent(ItemFindingHome.this,ItemFindingGame.class); 
                           ItemFindingHome.this.startActivity(newGame); 
                                } 
               }); 

        btnScore.setOnClickListener(new View.OnClickListener() { 

                       @Override 
                      public void onClick(View v) {
                    	   Intent ScoreIntent = new Intent(ItemFindingHome.this,ItemFindingScore.class);
                    	   ItemFindingHome.this.startActivity(ScoreIntent);                                                                           
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