package com.skripsi.atma;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TycoonGameMenu extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//set to full screen mode
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_tycoon_game);
	    
	    //create button objects
	    Button StartGame = (Button) findViewById(R.id.tg_start);
	    Button Help = (Button) findViewById(R.id.tg_help);
		
	    //set on click listener
	    StartGame.setOnClickListener(new OnClickListener(){	
			@Override
			public void onClick(View v) {
				Intent i = new Intent(TycoonGameMenu.this, TycoonGame.class);
				i.setData(Uri.parse("Start Game"));
				startActivity(i);
			};
		});
		
		Help.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent i = new Intent(TycoonGameMenu.this, TycoonGame.class);
				i.setData(Uri.parse("Help"));
				startActivity(i);
			}
			
		});
	}
}
