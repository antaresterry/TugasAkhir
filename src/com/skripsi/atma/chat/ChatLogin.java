package com.skripsi.atma.chat;

import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.quickblox.core.QBCallback;
import com.quickblox.core.QBCallbackImpl;
import com.quickblox.core.result.Result;
import com.quickblox.module.auth.QBAuth;
import com.quickblox.module.users.QBUsers;
import com.quickblox.module.users.model.QBUser;
import com.skripsi.atma.R;

public class ChatLogin extends Activity implements QBCallback {

    private Button loginButton;
    private EditText loginEdit;
    private EditText passwordEdit;
    private TextView forgotPass;
    private CheckBox showPass;
    private String login;
    private String password;
    private QBUser user;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_login);

        // UI stuff
        loginEdit = (EditText) findViewById(R.id.loginEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        showPass = (CheckBox) findViewById(R.id.showPassword);
        showPass.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
				if(isChecked){
					passwordEdit.setTransformationMethod(null);
					passwordEdit.setSelection(passwordEdit.getText().length());
				}
				else{
					passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
					passwordEdit.setSelection(passwordEdit.getText().length());
				}
			}
        });
        forgotPass = (TextView) findViewById(R.id.forgotPass);
        forgotPass.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View vi) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alert = new AlertDialog.Builder(ChatLogin.this);

				alert.setTitle("Forgot Your Password?");
				alert.setMessage("Enter your email address :");

				// Set an EditText view to get user input 
				final EditText input = new EditText(ChatLogin.this);
				alert.setView(input);

				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						final String email = input.getText().toString();
						// Do something with value!
						if(validEmail(email)){
							QBUsers.resetPassword(email, new QBCallbackImpl() {
							    @Override
							    public void onComplete(Result result) {
							        if (result.isSuccess()) {
							             // Email with instruction was sent
							            Toast.makeText(ChatLogin.this, "Email with instruction has sent to your email address at " + email + ". Please check your email.",
							                    Toast.LENGTH_SHORT).show();
							        } else {
										Toast.makeText(ChatLogin.this, "Email is not valid. Please check your email.",
							                    Toast.LENGTH_SHORT).show();
							            Log.e("Errors",result.getErrors().toString()); 
							        }
							    }
							});
						}
						else{
							Toast.makeText(ChatLogin.this, "Email is not valid. Please check your email.",
				                    Toast.LENGTH_SHORT).show();
						}
					}
				});
				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});
				alert.show();
			}
        	
        });
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		        progressDialog.show();
				QBAuth.createSession(new QBCallbackImpl() {
					@Override
					public void onComplete(Result result) {
						// result comes here
						// check if result success
						if (result.isSuccess()) {
							// do stuff you need
							login = loginEdit.getText().toString();
					        password = passwordEdit.getText().toString();

					        user = new QBUser(login, password);
					        // ================= QuickBlox ===== Step 3 =================
			                // Login user into QuickBlox.
			                // Pass this activity , because it implements QBCallback interface.
			                // Callback result will come into onComplete method below.
			            	QBUsers.signIn(user, new QBCallbackImpl() {
							    @Override
							    public void onComplete(Result result) {
							        if (progressDialog != null) {
							            progressDialog.dismiss();
							        }
							    	if (result.isSuccess()) {
							            Intent intent = new Intent(ChatLogin.this, ChatModeratorsList.class);
							            intent.putExtra("myId", user.getId());
							            intent.putExtra("myLogin", user.getLogin());
							            intent.putExtra("myPassword", user.getPassword());
							            intent.putExtra("myFullName", user.getFullName());
							            startActivity(intent);
							            Toast.makeText(ChatLogin.this, "You've been successfully logged in application",
							                    Toast.LENGTH_SHORT).show();
							        } else {
							            AlertDialog.Builder dialog = new AlertDialog.Builder(ChatLogin.this);
							            dialog.setMessage("Error(s) occurred. Look into DDMS log for details, " +
							                    "please. Errors: " + result.getErrors()).create().show();
							        }
							    }
							});
						}
					}
				});
			}
        });
        QBAuth.createSession(this);
    }

	@Override
	public void onComplete(Result result) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onComplete(Result result, Object object) {
		// TODO Auto-generated method stub
		
	}
	
	private boolean validEmail(String email) {
	    Pattern pattern = Patterns.EMAIL_ADDRESS;
	    return pattern.matcher(email).matches();
	}
}
