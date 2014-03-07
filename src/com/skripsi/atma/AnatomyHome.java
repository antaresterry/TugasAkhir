package com.skripsi.atma; 

import android.app.Activity; 
import android.content.Intent; 
import android.os.Bundle; 
import android.view.View; 
import android.widget.Button; 

public class AnatomyHome extends Activity { 
    @Override 
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_anatomy__home); 

        Button btnNewGame = (Button)findViewById(R.id.new_button); 
        Button btnAbout = (Button)findViewById(R.id.about_button); 
        Button btnExit = (Button)findViewById(R.id.exit_button); 

        btnNewGame.setOnClickListener(new View.OnClickListener() { 
        	@Override 
        	public void onClick(View v) { 
        		Intent newGame = new Intent(AnatomyHome.this,AnatomyGame.class); 
        		AnatomyHome.this.startActivity(newGame); 
        	} 
        }); 

        btnAbout.setOnClickListener(new View.OnClickListener() { 
        	@Override 
        	public void onClick(View v) { 
        		Intent ScoreIntent = new Intent(AnatomyHome.this,AnatomyScore.class);
        		AnatomyHome.this.startActivity(ScoreIntent); 
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