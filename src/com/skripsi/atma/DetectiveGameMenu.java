package com.skripsi.atma;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DetectiveGameMenu extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detective_game_menu);
		
		Button newgame = (Button) findViewById(R.id.DGnewGame);
		Button loadgame = (Button) findViewById(R.id.DGloadGame);
		Button options = (Button) findViewById(R.id.DGoptions);
		
		newgame.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v1) {
				// TODO Auto-generated method stub
				Intent i = new Intent(DetectiveGameMenu.this,DetectiveGame.class);
				i.setData(Uri.parse("New Game"));
				startActivity(i);
			};
		});
		
		loadgame.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v1) {
				// TODO Auto-generated method stub
				Intent i = new Intent(DetectiveGameMenu.this,DetectiveGame.class);
				i.setData(Uri.parse("Load Game"));
				startActivity(i);
			}
		});
		
		options.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v1) {
				// TODO Auto-generated method stub
				Intent i = new Intent(DetectiveGameMenu.this,DetectiveGame.class);
				i.setData(Uri.parse("Options"));
				startActivity(i);
			}
			
		});
	}
}
