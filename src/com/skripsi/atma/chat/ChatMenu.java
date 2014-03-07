package com.skripsi.atma.chat;

import com.skripsi.atma.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class ChatMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_menu);
		Button olchat = (Button) findViewById(R.id.olchat);
		Button offchat = (Button) findViewById(R.id.offchat);
		View.OnClickListener offchatclick = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent c = new Intent(ChatMenu.this, ChatOffline.class);
				startActivity(c);
			}
		};
		offchat.setOnClickListener(offchatclick);

		View.OnClickListener olchatclick = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent c = new Intent(ChatMenu.this, ChatOnline.class);
				startActivity(c);
			}
		};
		olchat.setOnClickListener(olchatclick);
	}
}
