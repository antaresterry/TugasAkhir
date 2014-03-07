package com.skripsi.atma;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class CookingGameHome extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_cooking_game_main); 

        Button btnNewGame = (Button)findViewById(R.id.new_button); 
        Button btnAbout = (Button)findViewById(R.id.about_button); 
        Button btnExit = (Button)findViewById(R.id.exit_button); 

        btnNewGame.setOnClickListener(new View.OnClickListener() { 

                       @Override 
                      public void onClick(View v) { 
                    	   Intent newGame = new Intent(CookingGameHome.this,CookingGame.class); 
                    	   CookingGameHome.this.startActivity(newGame); 
                                } 
               }); 

        btnAbout.setOnClickListener(new View.OnClickListener() { 

                       @Override 
                      public void onClick(View v) { 

                               Intent aboutIntent = new Intent(CookingGameHome.this,CookingGameAbout.class); 
                               CookingGameHome.this.startActivity(aboutIntent); 
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
