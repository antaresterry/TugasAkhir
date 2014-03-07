package com.skripsi.atma;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TesMinatDisclaimer extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tes_minat_disclaimer);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tes_minat_disclaimer, menu);
		return true;
	}

}
