package com.skripsi.atma;

import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class About extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		TextView abt = (TextView)findViewById(R.id.AboutText);
		abt.setText(Html.fromHtml(getString(R.string.AboutText)));
		
		Button Back = (Button) findViewById(R.id.BackButton);
		Back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
				System.exit(0);
			};
		});
		
	}

}
