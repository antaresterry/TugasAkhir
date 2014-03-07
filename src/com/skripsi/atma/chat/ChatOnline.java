package com.skripsi.atma.chat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.quickblox.core.QBSettings;
import com.skripsi.atma.R;

public class ChatOnline extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_online);
		QBSettings.getInstance().fastConfigInit("2771", "qWFKadpr9PNyYF9", "UxrcjNejrVkqUas");
		Button signInButton = (Button) findViewById(R.id.signInButton);
		Button signUpButton = (Button) findViewById(R.id.signUpButton);
		ImageView qbLinkPanel = (ImageView) findViewById(R.id.splash_qb_link_view);
        qbLinkPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://quickblox.com"));
                startActivity(browserIntent);
            }
        });
		signInButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View vi) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ChatOnline.this, ChatLogin.class);
				startActivity(intent);
			}
		});
		signUpButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View vi) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ChatOnline.this, ChatSignUp.class);
				startActivity(intent);
			}
		});
	}
}
