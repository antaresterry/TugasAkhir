package com.skripsi.atma;

import java.io.File;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.app.Activity;
import android.content.SharedPreferences;

public class ItemFindingScore extends Activity {

	private SharedPreferences saveGame;
	private String prefName = "saveScoreItem";
	private String prefItem = "saveItem";
	private int lastScore;
	TextView FinalScore;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item__finding__score);			
		//check for saved score
		File f = new File(getApplicationContext().getFilesDir().getParentFile().getPath() + "/shared_prefs/" + prefName + ".xml");
		
		if (f.exists()){
			try{
				saveGame = getSharedPreferences(prefName, MODE_PRIVATE);
				lastScore = saveGame.getInt("lastScore", 0);
				highScore();
			}
			catch(NullPointerException e){
				e.printStackTrace();			
			}
		} 
		FinalScore=(TextView) findViewById(R.id.textView1);
		FinalScore.setText(Integer.toString(lastScore));
		TextView s=(TextView) findViewById(R.id.textView3);
		s.setOnClickListener(new OnClickListener(){
	 		@Override
	 		public void onClick(View v) {
	 			finish();
	 		};
	 	});	    
	}
	public void highScore(){
		if(lastScore>=800){
			saveGame = getSharedPreferences(prefItem, MODE_PRIVATE);
			SharedPreferences.Editor editor = saveGame.edit();
			editor.putInt("ipa1",30);
			editor.putInt("ipa2",40);
			editor.putInt("ips1",15);
			editor.putInt("ips2",15);
			editor.commit();
		}
		else{
			saveGame = getSharedPreferences(prefItem, MODE_PRIVATE);
			SharedPreferences.Editor editor = saveGame.edit();
			editor.putInt("ipa1",30);
			editor.putInt("ipa2",10);
			editor.putInt("ips1",30);
			editor.putInt("ips2",30);
			editor.commit();
		}
	}
}
