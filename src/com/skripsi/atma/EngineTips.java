package com.skripsi.atma;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.app.Activity;

public class EngineTips extends Activity {

	public int n=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tips_and_tricks);
		
		final TextView tts = (TextView) findViewById(R.id.textView1);	
		tts.setText(R.string.tips1);
		tts.setOnClickListener(new View.OnClickListener() { 
           @Override 
           public void onClick(View v) {
        	   switch(n){
        	   case 1 :
        		   tts.setText(R.string.tips2);
        		   n++;
        		   break;
        	   case 2 :
        		   tts.setText(R.string.tips3);
        		   n++;
        		   break;
        	   case 3 :
        		   tts.setText(R.string.tips4);
        		   n++;
        		   break;
        	   case 4 :
        		   finish();
        	   }
           } 
		});
	}
}
