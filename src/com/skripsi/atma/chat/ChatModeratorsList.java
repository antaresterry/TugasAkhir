package com.skripsi.atma.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.quickblox.core.QBCallback;
import com.quickblox.core.QBCallbackImpl;
import com.quickblox.core.result.Result;
import com.quickblox.module.chat.QBChat;
import com.quickblox.module.users.QBUsers;
import com.quickblox.module.users.model.QBUser;
import com.quickblox.module.users.result.QBUserPagedResult;
import com.skripsi.atma.R;

public class ChatModeratorsList extends Activity implements QBCallback {

    private ListView moderatorsList;
    private ProgressDialog progressDialog;
    private Button delButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_moderators_list);

        moderatorsList = (ListView) findViewById(R.id.moderatorsList);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading moderator list");
        progressDialog.show();
        
		ImageView qbLinkPanel = (ImageView) findViewById(R.id.splash_qb_link_view);
        qbLinkPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://quickblox.com"));
                startActivity(browserIntent);
            }
        });
        
        delButton = (Button) findViewById(R.id.delButton);
        delButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(ChatModeratorsList.this)
				.setTitle("Delete Account")
				.setMessage("Do you really want to delete your account?")
				.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int whichButton) {

				        // Load QBUser objects from bundle (passed from previous activity).
				        Bundle extras = getIntent().getExtras();

				        QBUser me = new QBUser();
				        me.setId(extras.getInt("myId"));
				        me.setLogin(extras.getString("myLogin"));
				        me.setPassword(extras.getString("myPassword"));
				        me.setFullName(extras.getString("myFullName"));
				    	QBUsers.deleteUser(me.getId(), new QBCallbackImpl() {
				    	    @Override
				    	    public void onComplete(Result result) {
				    	        if (result.isSuccess()) {
				    	            // User was deleted
				    	        	Toast.makeText(ChatModeratorsList.this, "User was deleted. Logging out..", Toast.LENGTH_SHORT).show();
				    	        	finish();
				    	        } else {
				    	            Log.e("Errors",result.getErrors().toString()); 
				    	        }
				    	    } 
				    	});
				    }
				 })
				 .setNegativeButton(android.R.string.no, null).show();
			}
        	
        });

        ArrayList<String> userTags = new ArrayList<String>();
        userTags.add("moderator");
        QBUsers.getUsersByTags(userTags, this);
        
    }
    
    @Override
    public void onComplete(Result result) {
        if (result.isSuccess()) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            // Cast 'result' to specific result class QBUserPagedResult.
            QBUserPagedResult pagedResult = (QBUserPagedResult) result;
            final ArrayList<QBUser> users = pagedResult.getUsers();

            // Prepare users list for simple adapter.
            ArrayList<Map<String, String>> usersListForAdapter = new ArrayList<Map<String, String>>();
            for (QBUser u : users) {
                Map<String, String> umap = new HashMap<String, String>();
                umap.put("userLogin", u.getLogin());
                umap.put("chatLogin", QBChat.getChatLoginFull(u));
                long currentTime = System.currentTimeMillis();
                try{
                	long userLastRequestAtTime = u.getLastRequestAt().getTime();
                    // if user didn't do anything last 5 minutes (5*60*1000 milliseconds)    
                    if((currentTime - userLastRequestAtTime) > 5*60*1000){ 
                        // user is offline now
                    	umap.put("status", "Offline");
                    }
                    else{
                    	umap.put("status", "Online");
                    }
                }
                catch(NullPointerException e){
                	umap.put("status", "Offline");
                    usersListForAdapter.add(umap);
                    continue;
                }
                umap.put("fullName", u.getFullName());
                usersListForAdapter.add(umap);
            }

            // Put users list into adapter.
            SimpleAdapter usersAdapter = new SimpleAdapter(this, usersListForAdapter,
                    android.R.layout.simple_list_item_2,
                    new String[]{"userLogin", "fullName"},
                    new int[]{android.R.id.text1, android.R.id.text2});

            moderatorsList.setAdapter(usersAdapter);
            moderatorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    // Prepare QBUser objects to pass it into next activities using bundle.
                    QBUser friendUser = users.get(i);

                    Intent intent = new Intent(ChatModeratorsList.this, ChatActivity.class);
                    Bundle extras = getIntent().getExtras();
                    intent.putExtra("friendId", friendUser.getId());
                    intent.putExtra("friendLogin", friendUser.getLogin());
                    intent.putExtra("friendPassword", friendUser.getPassword());
                    intent.putExtra("friendFullName", friendUser.getFullName());
                    // Add extras from previous activity.
                    intent.putExtras(extras);

                    startActivity(intent);
                }
            });
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Error(s) occurred. Look into DDMS log for details, " +
                    "please. Errors: " + result.getErrors()).create().show();
        }
    }

    @Override
    public void onComplete(Result result, Object context) { }
}