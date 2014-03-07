package com.skripsi.atma;

import java.io.File;

import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;
import android.content.SharedPreferences;

public class AnatomyScore extends Activity {

	private SharedPreferences saveGame;
	private String prefName = "saveScoreAnatomy";
	private SharedPreferences saveGame2;
	private String prefAnatomy = "saveAnatomy";
	int highscore;
	int nilai;
	TextView FinalScore;
	TextView HighScore;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anatomy__score);			
		//load nilai from shared preferences
		File f = new File(getApplicationContext().getFilesDir().getParentFile().getPath() + "/shared_prefs/"+ prefName +".xml");
		if (f.exists()){
			try{
				saveGame = getSharedPreferences(prefName, MODE_PRIVATE);
				nilai = saveGame.getInt("nilai", 0);
				highscore = saveGame.getInt("highscore", 0);
			}
			catch(NullPointerException e){
				e.printStackTrace();			
			}
		}
		if(highscore==0)
			highscore=nilai;
		if(nilai>highscore){
			saveGame = getSharedPreferences(prefName, MODE_PRIVATE);
			SharedPreferences.Editor editor = saveGame.edit();
			editor.putInt("highscore", nilai);
			editor.commit();
			highscore = nilai;
		}
		highScore();
		
		FinalScore=(TextView) findViewById(R.id.textView1);
		FinalScore.setText(Integer.toString(nilai));
		HighScore=(TextView) findViewById(R.id.textView4);
		HighScore.setText(Integer.toString(highscore));
	}
	
	public void highScore(){
		if(highscore>=800){	
			saveGame2 = getSharedPreferences(prefAnatomy, MODE_PRIVATE);
			SharedPreferences.Editor editor = saveGame2.edit();
			editor.putInt("ipa1",10);
			editor.putInt("ipa2",70);
			editor.putInt("ips1",10);
			editor.putInt("ips2",10);
			editor.commit();
		}
		else{
			saveGame2 = getSharedPreferences(prefAnatomy, MODE_PRIVATE);
			SharedPreferences.Editor editor = saveGame2.edit();
			editor.putInt("ipa1",30);
			editor.putInt("ipa2",10);
			editor.putInt("ips1",30);
			editor.putInt("ips2",30);
			editor.commit();
		}
	}	
}
