package com.skripsi.atma.chat;

import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.quickblox.core.QBCallbackImpl;
import com.quickblox.core.result.Result;
import com.quickblox.module.auth.QBAuth;
import com.quickblox.module.users.QBUsers;
import com.quickblox.module.users.model.QBUser;
import com.quickblox.module.users.result.QBUserResult;
import com.skripsi.atma.R;

public class ChatSignUp extends Activity {
	
	private Button signUpButton;
	private EditText emailInput;
	private EditText loginInput;
	private EditText fullNameInput;
	private EditText passwordInput;
	private EditText confPasswordInput;
	private TextView emailError;
	private TextView loginError;
	private TextView fullNameError;
	private TextView passError;
	private TextView confPassError;
    private ProgressDialog progressDialog;
	private boolean emailIsValid;
	private boolean loginIsValid;
	private boolean fullNameIsValid;
	private boolean passIsValid;
	private boolean confPassIsValid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_sign_up);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
		emailError = (TextView) findViewById(R.id.emailError);
		loginError = (TextView) findViewById(R.id.loginError);
		fullNameError = (TextView) findViewById(R.id.fullNameError);
		passError = (TextView) findViewById(R.id.passError);
		confPassError = (TextView) findViewById(R.id.confPassError);
		emailInput = (EditText) findViewById(R.id.emailInput);
		emailInput.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable edit) {
				// TODO Auto-generated method stub
				if(validEmail(emailInput.getText().toString())){
					emailError.setVisibility(View.GONE);
					emailIsValid = true;
				}
				else{
					emailError.setText("Email is not valid. Please check again.");
					emailError.setVisibility(View.VISIBLE);
					emailIsValid = false;
				}
			}

			@Override
			public void beforeTextChanged(CharSequence string, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence string, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}
			
		});
		loginInput = (EditText) findViewById(R.id.loginInput);
		loginInput.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable edit) {
				// TODO Auto-generated method stub
				if(loginInput.getText() != null && loginInput.getText().length() >= 8){
					loginError.setVisibility(View.GONE);
					loginIsValid = true;
				}
				else{
					loginError.setText("Login is not valid. Please check again.");
					loginError.setVisibility(View.VISIBLE);
					loginIsValid = false;
				}
			}

			@Override
			public void beforeTextChanged(CharSequence string, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence string, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}
			
		});
		fullNameInput = (EditText) findViewById(R.id.fullNameInput);
		fullNameInput.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable edit) {
				// TODO Auto-generated method stub
				if(fullNameInput.getText() != null){
					fullNameError.setVisibility(View.GONE);
					fullNameIsValid = true;
				}
				else{
					fullNameError.setText("Full Name is not valid. Please check again.");
					fullNameError.setVisibility(View.VISIBLE);
					fullNameIsValid = false;
				}
			}

			@Override
			public void beforeTextChanged(CharSequence string, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence string, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}
			
		});
		passwordInput = (EditText) findViewById(R.id.passwordInput);
		passwordInput.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable edit) {
				// TODO Auto-generated method stub
				if(passwordInput.getText() != null && passwordInput.getText().length() >= 8){
					passError.setVisibility(View.GONE);
					passIsValid = true;
					if(confPasswordInput.getText().toString().equals(passwordInput.getText().toString())){
						confPassError.setVisibility(View.GONE);
						confPassIsValid = true;
					}
				}
				else{
					passError.setText("Password is not valid. Please check again.");
					passError.setVisibility(View.VISIBLE);
					passIsValid = false;
				}
			}

			@Override
			public void beforeTextChanged(CharSequence string, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence string, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}
			
		});
		confPasswordInput = (EditText) findViewById(R.id.confPasswordInput);
		confPasswordInput.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable edit) {
				// TODO Auto-generated method stub
				if(confPasswordInput.getText() != null && confPasswordInput.getText().length() >= 8){
					if(confPasswordInput.getText().toString().equals(passwordInput.getText().toString())){
						passError.setVisibility(View.GONE);
						confPassError.setVisibility(View.GONE);
						passIsValid = true;
						confPassIsValid = true;
					}
					else{
						passError.setText("Password and Confirmation Password does not match. Please check again.");
						passError.setVisibility(View.VISIBLE);
						confPassError.setText("Password and Confirmation Password does not match. Please check again.");
						confPassError.setVisibility(View.VISIBLE);
						passIsValid = false;
						confPassIsValid = false;
					}
				}
				else{
					confPassError.setText("Confirmation Password is not valid. Please check again.");
					confPassError.setVisibility(View.VISIBLE);
					confPassIsValid = false;
				}
			}

			@Override
			public void beforeTextChanged(CharSequence string, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence string, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}
			
		});
		signUpButton = (Button) findViewById(R.id.signUpButton);
		signUpButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View vi) {
				// TODO Auto-generated method stub
		        progressDialog.show();
				if(emailInput.getText() != null && loginInput.getText() != null && fullNameInput.getText() != null && passwordInput.getText() != null && confPasswordInput.getText() != null){
					if(emailIsValid && loginIsValid && fullNameIsValid && passIsValid && confPassIsValid){
						
						QBAuth.createSession(new QBCallbackImpl() {
							@Override
							public void onComplete(Result result) {
								// result comes here
								// check if result success
								if (result.isSuccess()) {
									// do stuff you need
									QBUser user = new QBUser();
									user.setEmail(emailInput.getText().toString());
									user.setLogin(loginInput.getText().toString());
									user.setFullName(fullNameInput.getText().toString());
									user.setPassword(passwordInput.getText().toString());
									QBUsers.signUpSignInTask(user, new QBCallbackImpl() {
										@Override
										public void onComplete(Result result) {
									        if (progressDialog != null) {
									            progressDialog.dismiss();
									        }
											if (result.isSuccess()) {
												QBUserResult qbUserResult = (QBUserResult) result;
												Log.d("Registration was successful","user: " + qbUserResult.getUser().toString());
												QBUser user;
												user = qbUserResult.getUser();
												Intent intent = new Intent(ChatSignUp.this, ChatModeratorsList.class);
												intent.putExtra("myId", user.getId());
												intent.putExtra("myLogin", user.getLogin());
												intent.putExtra("myPassword", user.getPassword());
												intent.putExtra("myFullName", user.getFullName());
												startActivity(intent);
											} 
											else {
												Log.e("Errors",result.getErrors().toString()); 
											}
										}
									});
								}
								else{
									Log.e("Errors",result.getErrors().toString()); 
								}
							}
						});
					}
					else{
						
					}
				}
				else{
					
				}
			}
		});
	}
	private boolean validEmail(String email) {
	    Pattern pattern = Patterns.EMAIL_ADDRESS;
	    return pattern.matcher(email).matches();
	}
}
