package com.skripsi.atma;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DetectiveGame extends Activity {

	private SharedPreferences gameData;
	private String prefName = "DetectiveGameData";
	private SharedPreferences gameSettings;
	private String prefName2 = "DetectiveGameSettings";
	private SharedPreferences gameResult;
	private String prefName3 = "DetectiveGameResult";
	private String gender;
	private String tempGender;
	private String tempGender2;
	private String tempGender3;
	private String tempGender4;
	private String charName;
	private String charNickname;
	private String cityName;
	private String schoolName;
	private String friendName1;
	private String friendName2;
	private String friendNickname1;
	private String friendNickname2;
	private String selectedItem;
	private String[] stories;
	private String[] chapters;
	private String[] decisions;
	private int story;
	private int plot;
	private int chapter;
	private int branch;
	private int branchStory;
	private int tempDecision;
	private int socialPoints;
	private int analyticalPoints;
	private int inputMode = 0;
	private int textSpeed = 0;
	private int tempLoc;
	private boolean branch01 = false;
	private boolean branch02 = false;
	private boolean branch03 = false;
	private boolean dec01 = false;
	private boolean dec02 = false;
	private boolean dec03 = false;
	private boolean disableEvent = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (intent.getData() != null) {
			File gameSetting = new File(getApplicationContext().getFilesDir().getParentFile().getPath() + "/shared_prefs/" + prefName2 + ".xml");
			
			if(gameSetting.exists()){
				try{
					gameSettings = getSharedPreferences(prefName2, MODE_PRIVATE);
					textSpeed = gameSettings.getInt("TextSpeed", 10);
				}
				catch(NullPointerException e){
					e.printStackTrace();
				}
			}
			if(intent.getDataString().compareTo("New Game") == 0){
				chooseHero();
			}
			else if(intent.getDataString().compareTo("Load Game") == 0){
				File savedGame = new File(getApplicationContext().getFilesDir().getParentFile().getPath() + "/shared_prefs/" + prefName + ".xml");
				
				if(savedGame.exists()){
					try{
						loadGame();
						if(plot == 0 && story == 0){
							newChapter();
						}
						else{
							conversation();
						}
					}
					catch(NullPointerException e){
						e.printStackTrace();
					}
				}
				else{
					//set to landscape and fullscreen mode.
					getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
				    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				    setContentView(R.layout.activity_detective_game_conversation);
				    View vi = new View(this);
					vi.setBackgroundResource(R.drawable.dg_apartment_front_day);
					Toast.makeText(this, "There is no saved game. Returning to Main Menu..", Toast.LENGTH_LONG).show();
					this.finish();
				}
				
			}
			else if(intent.getDataString().compareTo("Options") == 0){
				option();
			}
		}	
	}

	public void loadGame(){
		gameData = getSharedPreferences(prefName, MODE_PRIVATE);
		gender = gameData.getString("Gender", "Male");
		charName = gameData.getString("Name", "Default Name");
		charNickname = gameData.getString("Nickname", "Default Nickname");
		cityName = gameData.getString("CityName", "Default City");
		schoolName = gameData.getString("SchoolName", "Default School");
		story = gameData.getInt("Story", 0);
		plot = gameData.getInt("Plot", 0);
		chapter = gameData.getInt("Chapter", 0);
		branch = gameData.getInt("Branch", 0);
		branchStory = gameData.getInt("BranchStory", 0);
		tempDecision = gameData.getInt("TempDecision", 0);
		tempLoc = gameData.getInt("TempLoc", 0);
		socialPoints = gameData.getInt("SocialPoints", 0);
		analyticalPoints = gameData.getInt("AnalyticalPoints", 11);
		inputMode = gameData.getInt("InputMode", 0);
		Toast.makeText(this, "Saved game found. Loading saved game..", Toast.LENGTH_SHORT).show();
	}
	
	public void chooseHero(){
		setContentView(R.layout.activity_detective_game_choose_hero);
		final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
		final Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
		RelativeLayout currentView = (RelativeLayout) findViewById(R.id.chooseHero);
		currentView.startAnimation(fadeIn);
		TextView maleHero = (TextView) findViewById(R.id.maleHero);
		TextView femaleHero = (TextView) findViewById(R.id.femaleHero);
		maleHero.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gender = "Male";
				RelativeLayout currentView = (RelativeLayout) findViewById(R.id.chooseHero);
				currentView.startAnimation(fadeOut);
				setContentView(R.layout.activity_detective_game_name_input);
				nameInput();
			}
		});
		femaleHero.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gender = "Female";
				RelativeLayout currentView = (RelativeLayout) findViewById(R.id.chooseHero);
				currentView.startAnimation(fadeOut);
				setContentView(R.layout.activity_detective_game_name_input);
				nameInput();
			}
		});
	}
	public void nameInput(){
		final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
		final Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
		setContentView(R.layout.activity_detective_game_name_input);
	
		final EditText inputName = (EditText) findViewById(R.id.nameInput);
		ImageView avatar = (ImageView) findViewById(R.id.avatar);
		TextView done = (TextView) findViewById(R.id.done);
		
		if(gender.equals("Male")){
			avatar.setImageResource(R.drawable.dgmalehero001);
		}
		else if(gender.equals("Female")){
			avatar.setImageResource(R.drawable.dgfemalehero001);
		}
		avatar.startAnimation(fadeIn);
		done.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RelativeLayout currentView = (RelativeLayout) findViewById(R.id.backg);
				TextView nameText = (TextView) findViewById(R.id.nameText);
				TextView status = (TextView) findViewById(R.id.status);
				if(inputMode == 0){
					charName = inputName.getText().toString();
					if(charName.equals(null) || charName.equals("")){
						Toast.makeText(DetectiveGame.this, "Your name cannot be empty.", Toast.LENGTH_SHORT).show();
					}
					else{
						Toast.makeText(DetectiveGame.this, "Congratulations! Your name is " + charName + ". Now please input your nickname.", Toast.LENGTH_SHORT).show();
						currentView.startAnimation(fadeOut);
						nameText.setText("Please input your Nick Name :");
						inputName.setText("");
						status.setText("Name : " + charName);
						status.setVisibility(View.VISIBLE);
						currentView.startAnimation(fadeIn);
						inputMode = 1;
					}
					
				}
				else if(inputMode == 1){
					charNickname = inputName.getText().toString();
					if(charNickname.equals(null) || charNickname.equals("")){
						Toast.makeText(DetectiveGame.this, "Your nickname cannot be empty.", Toast.LENGTH_SHORT).show();
					}
					else{
						Toast.makeText(DetectiveGame.this, "Congratulations! Your nickname is " + charNickname + ". Now please input the city you came from.", Toast.LENGTH_SHORT).show();
						currentView.startAnimation(fadeOut);
						nameText.setText("Please input your City Name :");
						inputName.setText("");
						status.setText(status.getText().toString() + "\nNickname : " + charNickname);
						currentView.startAnimation(fadeIn);
						inputMode = 2;
					}
				}
				else if(inputMode == 2){
					cityName = inputName.getText().toString();
					if(cityName.equals(null) || cityName.equals("")){
						Toast.makeText(DetectiveGame.this, "Your city name cannot be empty.", Toast.LENGTH_SHORT).show();
					}
					else{
						Toast.makeText(DetectiveGame.this, "Congratulations! Your city name is " + cityName + ". Now please input the school you came from.", Toast.LENGTH_SHORT).show();
						currentView.startAnimation(fadeOut);
						nameText.setText("Please input your School Name :");
						inputName.setText("");
						status.setText(status.getText().toString() + "\nCity Name : " + cityName);
						currentView.startAnimation(fadeIn);
						inputMode = 3;
					}
				}
				else if(inputMode == 3){
					schoolName = inputName.getText().toString();
					if(schoolName.equals(null) || schoolName.equals("")){
						Toast.makeText(DetectiveGame.this, "Your school name cannot be empty.", Toast.LENGTH_SHORT).show();
					}
					else{
						currentView.startAnimation(fadeOut);
						status.setText(status.getText().toString() + "\nSchool Name : " + schoolName);
						currentView.startAnimation(fadeIn);
						DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
						    @Override
						    public void onClick(DialogInterface dialog, int which) {
						        switch (which){
						        case DialogInterface.BUTTON_POSITIVE:
						        	story = 0;
									plot = 0;
									chapter = 0;
									branch = 0;
									branchStory = 0;
									socialPoints = 0;
									analyticalPoints = 11;
									tempDecision = 0;
									tempLoc = 0;
									inputMode = 0;
									savePref("Name", charName);
									savePref("Nickname", charNickname);
									savePref("Gender", gender);
									savePref("CityName", cityName);
									savePref("SchoolName", schoolName);
									savePrefInt("Branch", branch);
									savePrefInt("BranchStory", branchStory);
									savePrefInt("TempDecision", tempDecision);
									savePrefInt("TempLoc", tempLoc);
									savePrefInt("SocialPoints", socialPoints);
									savePrefInt("AnalyticalPoints", analyticalPoints);
									savePrefInt("InputMode", inputMode);
									saveStory(story, plot, chapter);
									newChapter();
						            break;

						        case DialogInterface.BUTTON_NEGATIVE:
									inputMode = 0;
									nameInput();
						            break;
						        }
						    }
						};

						AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this);
						builder.setMessage("This is your decisions :\n" + status.getText().toString() + "\nAre you sure?").setPositiveButton("Yes, I want to proceed", dialogClickListener)
						    .setNegativeButton("No, I want to input again", dialogClickListener).show();
					}
				}
			}
		});
	}
	
	public void newChapter(){
		final Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
		setContentView(R.layout.activity_detective_game_chapter);
		TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
		setSituation();
		textBox.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RelativeLayout currentView = (RelativeLayout) findViewById(R.id.backg);
				currentView.startAnimation(fadeOut);
				conversation();
			}
		});
	}
	public void conversation(){
		setContentView(R.layout.activity_detective_game_conversation);
		TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
		setSituation();
		textBox.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(plot == 0 && story == 0){
					newChapter();
				}
				else{
					setSituation();
				}
			}
		});
	}
	
	public void setSituation(){
		TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
		RelativeLayout currentView = (RelativeLayout) findViewById(R.id.backg);
		final Resources res = getResources();
		final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
		final Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
		chapters = res.getStringArray(R.array.DG_Chapters);
		if(gender.equals("Male")){
			friendName1 = "Kaoru Kail";
			friendName2 = "Rena Ohgami";
			friendNickname1 = "Kail";
			friendNickname2 = "Rena";
			tempGender = "boy";
			tempGender2 = "his";
			tempGender3 = "him";
			tempGender4 = "her";
		}
		else if(gender.equals("Female")){
			friendName1 = "Mika Ohgami";
			friendName2 = "Kaoru Karl";
			friendNickname1 = "Mika";
			friendNickname2 = "Karl";
			tempGender = "girl";
			tempGender2 = "her";
			tempGender3 = "her";
			tempGender4 = "him";
		}
		if(chapter == 0){
			if(plot == 0){
				stories = res.getStringArray(R.array.DG_Story_01);
				
				if(story == 0){
					textBox.startAnimation(fadeIn);
					textBox.setCharacterDelay(100);
					textBox.animateText("Chapter " + String.valueOf(chapter + 1) + "\n" + chapters[chapter]);
					saveStory(story, plot, chapter);
					story = 1;
				}
				else{
					ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
					ImageView avatarCenter = (ImageView) findViewById(R.id.avatarCenter);
					ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
					TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
					if(story == 1){
						nameBox.setText(charName);
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.LEFT);
						changeBackground(Color.BLACK);
						setCharVisibility(false, true, false);
						if(gender.equals("Male")){
							avatarCenter.setImageResource(R.drawable.dgmalehero001);
						}
						else if(gender.equals("Female")){
							avatarCenter.setImageResource(R.drawable.dgfemalehero001);
						}
						avatarCenter.startAnimation(fadeIn);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story - 1].replace("char_name", charName));
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story - 1].replace("char_name", charName));
						}
						saveStory(story, plot, chapter);
						story = 2;
					}
					else if(story == 2){
						nameBox.setText(charName);
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.LEFT);
						avatarCenter.startAnimation(fadeOut);
						setCharVisibility(false, true, false);
						currentView.setBackgroundColor(Color.BLACK);
						if(gender.equals("Male")){
							avatarCenter.setImageResource(R.drawable.dgmalehero002);
						}
						else if(gender.equals("Female")){
							avatarCenter.setImageResource(R.drawable.dgfemalehero002);
						}
						avatarCenter.startAnimation(fadeIn);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story - 1].replace("city_name", cityName));
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story - 1].replace("city_name", cityName));
						}
						saveStory(story, plot, chapter);
						story = 3;
					}
					else if(story == 3){
						nameBox.setText(charName);
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.LEFT);
						avatarCenter.startAnimation(fadeOut);
						setCharVisibility(true, false, false);
						changeBackground(R.drawable.dg_apartment_front_day);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero003);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero003);
						}
						avatarLeft.startAnimation(fadeIn);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story - 1]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story - 1]);
						}
						saveStory(story, plot, chapter);
						story = 4;
					}
					else if(story == 4){
						nameBox.setText("Mom");
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.RIGHT);
						avatarRight.setImageResource(R.drawable.dgmom02);
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_apartment_front_day);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero001);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero001);
						}
						avatarLeft.setColorFilter(R.color.TransBlack);
						avatarRight.clearColorFilter();
						avatarLeft.startAnimation(fadeIn);
						avatarRight.startAnimation(fadeIn);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story - 1].replace("char_nickname", charNickname));
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story - 1].replace("char_nickname", charNickname));
						}
						saveStory(story, plot, chapter);
						story = 5;
					}
					else if(story == 5){
						nameBox.setText(charName);
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.LEFT);
						avatarLeft.startAnimation(fadeOut);
						avatarRight.startAnimation(fadeOut);
						setCharVisibility(false, true, false);
						currentView.setBackgroundResource(R.drawable.dg_apartment_front_day);
						if(gender.equals("Male")){
							avatarCenter.setImageResource(R.drawable.dgmalehero002);
						}
						else if(gender.equals("Female")){
							avatarCenter.setImageResource(R.drawable.dgfemalehero002);
						}
						avatarCenter.clearColorFilter();
						avatarCenter.startAnimation(fadeIn);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story - 1]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story - 1]);
						}
						saveStory(story, plot, chapter);
						story = 6;
					}
					else if(story == 6){
						nameBox.setText(charName);
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.LEFT);
						avatarCenter.startAnimation(fadeOut);
						setCharVisibility(false, true, false);
						currentView.setBackgroundResource(R.drawable.dg_apartment_front_day);
						if(gender.equals("Male")){
							avatarCenter.setImageResource(R.drawable.dgmalehero001);
						}
						else if(gender.equals("Female")){
							avatarCenter.setImageResource(R.drawable.dgfemalehero001);
						}
						avatarCenter.startAnimation(fadeIn);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story - 1]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story - 1]);
						}
						saveStory(story, plot, chapter);
						story = 7;
					}
					else if(story == 7){
						nameBox.setText(charName);
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.LEFT);
						avatarCenter.startAnimation(fadeOut);
						setCharVisibility(false, true, false);
						currentView.setBackgroundResource(R.drawable.dg_apartment_front_day);
						if(gender.equals("Male")){
							avatarCenter.setImageResource(R.drawable.dgmalehero002);
						}
						else if(gender.equals("Female")){
							avatarCenter.setImageResource(R.drawable.dgfemalehero002);
						}
						avatarCenter.startAnimation(fadeIn);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story - 1]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story - 1]);
						}
						saveStory(story, plot, chapter);
						story = 8;
					}
					else if(story == 8){
						nameBox.setText("Dad");
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.RIGHT);
						avatarCenter.startAnimation(fadeOut);
						setCharVisibility(true, false, true);
						avatarRight.setImageResource(R.drawable.dgdad02);
						currentView.setBackgroundResource(R.drawable.dg_apartment_front_day);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero001);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero001);
						}
						avatarLeft.setColorFilter(R.color.TransBlack);
						avatarRight.clearColorFilter();
						avatarLeft.startAnimation(fadeIn);
						avatarRight.startAnimation(fadeIn);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story - 1].replace("char_nickname", charNickname));
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story - 1].replace("char_nickname", charNickname));
						}
						saveStory(story, plot, chapter);
						story = 9;
					}	
					else if(story == 9){
						nameBox.setText(charName);
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.LEFT);
						avatarLeft.startAnimation(fadeOut);
						avatarRight.startAnimation(fadeOut);
						setCharVisibility(true, false, true);
						avatarRight.setImageResource(R.drawable.dgdad01);
						currentView.setBackgroundResource(R.drawable.dg_apartment_front_day);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero002);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero002);
						}
						avatarRight.setColorFilter(R.color.TransBlack);
						avatarLeft.clearColorFilter();
						avatarLeft.startAnimation(fadeIn);
						avatarRight.startAnimation(fadeIn);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story - 1]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story - 1]);
						}
						saveStory(story, plot, chapter);
						story = 10;
					}	
					else if(story == 10){
						avatarLeft.startAnimation(fadeOut);
						avatarRight.startAnimation(fadeOut);
						setCharVisibility(false, false, false);
						changeBackground(Color.BLACK);
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story - 1].replace("char_name", charName).replace("gender_2", tempGender2));
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story - 1].replace("char_name", charName).replace("gender_2", tempGender2));
						}
						saveStory(story, plot, chapter);
						plot = 1;
						story = 0;
					}
				}
			}
			else{
				ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
				ImageView avatarCenter = (ImageView) findViewById(R.id.avatarCenter);
				ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
				TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
				if(plot == 1){
					stories = res.getStringArray(R.array.DG_Story_02);
					
					if(story == 0){
						setCharVisibility(false, false, false);
						changeBackground(R.drawable.dg_home_entrance_evening);
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 1;
					}
					else if(story == 1){
						nameBox.clearBuffer();
						nameBox.setText(charName);
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.LEFT);
						setCharVisibility(false, true, false);
						currentView.setBackgroundResource(R.drawable.dg_home_entrance_evening);
						if(gender.equals("Male")){
							avatarCenter.setImageResource(R.drawable.dgmalehero002);
						}
						else if(gender.equals("Female")){
							avatarCenter.setImageResource(R.drawable.dgfemalehero002);
						}
						avatarCenter.startAnimation(fadeIn);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 2;
					}
					else if(story == 2){
						nameBox.setText("Mom");
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.RIGHT);
						avatarCenter.startAnimation(fadeOut);
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_home_entrance_evening);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero001);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero001);
						}
						avatarRight.setImageResource(R.drawable.dgmom02);
						avatarRight.clearColorFilter();
						avatarLeft.setColorFilter(R.color.TransBlack);
						avatarLeft.startAnimation(fadeIn);
						avatarRight.startAnimation(fadeIn);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 3;
					}
					else if(story == 3){
						nameBox.setText("Dad");
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.LEFT);
						avatarLeft.startAnimation(fadeOut);
						avatarRight.startAnimation(fadeOut);
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_home_entrance_evening);
						avatarRight.setImageResource(R.drawable.dgmom01);
						avatarLeft.setImageResource(R.drawable.dgdad02);
						avatarRight.setColorFilter(R.color.TransBlack);
						avatarLeft.clearColorFilter();
						avatarLeft.startAnimation(fadeIn);
						avatarRight.startAnimation(fadeIn);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 4;
					}
					else if(story == 4){
						avatarLeft.startAnimation(fadeOut);
						avatarRight.startAnimation(fadeOut);
						setCharVisibility(false, false, false);
						changeBackground(R.drawable.dg_home_corridor_evening);
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 5;
					}
					else if(story == 5){
						nameBox.clearBuffer();
						nameBox.setText(charName);
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.LEFT);
						setCharVisibility(false, true, false);
						currentView.setBackgroundResource(R.drawable.dg_home_corridor_evening);
						if(gender.equals("Male")){
							avatarCenter.setImageResource(R.drawable.dgmalehero003);
						}
						else if(gender.equals("Female")){
							avatarCenter.setImageResource(R.drawable.dgfemalehero003);
						}
						avatarCenter.startAnimation(fadeIn);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 6;
					}
					else if(story == 6){
						nameBox.setText("Dad");
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.RIGHT);
						avatarCenter.startAnimation(fadeOut);
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_home_corridor_evening);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero001);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero001);
						}
						avatarRight.setImageResource(R.drawable.dgdad02);
						avatarLeft.setColorFilter(R.color.TransBlack);
						avatarRight.clearColorFilter();
						avatarLeft.startAnimation(fadeIn);
						avatarRight.startAnimation(fadeIn);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 7;
					}
					else if(story == 7){
						avatarLeft.startAnimation(fadeOut);
						avatarRight.startAnimation(fadeOut);
						setCharVisibility(false, false, false);
						changeBackground(R.drawable.dg_home_hero_room_empty_evening);
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 8;
					}
					else if(story == 8){
						nameBox.clearBuffer();
						nameBox.setText(charName);
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.LEFT);
						setCharVisibility(false, true, false);
						changeBackground(R.drawable.dg_home_hero_room_empty_with_cardboard_evening);
						if(gender.equals("Male")){
							avatarCenter.setImageResource(R.drawable.dgmalehero002);
						}
						else if(gender.equals("Female")){
							avatarCenter.setImageResource(R.drawable.dgfemalehero002);
						}
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 9;
					}
					else if(story == 9){
						setCharVisibility(false, false, false);
						changeBackground(R.drawable.dg_home_hero_room_night_with_lamp);
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 10;
					}
					else if(story == 10){
						setCharVisibility(false, false, false);
						changeBackground(R.drawable.dg_home_hero_room_night);
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						plot = 2;
						story = 0;
					}
				}
				else if(plot == 2){
					stories = res.getStringArray(R.array.DG_Story_03);
					if(story == 0){
						setCharVisibility(false, false, false);
						changeBackground(R.drawable.dg_home_entrance_day);
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 1;
					}
					else if(story == 1){
						setCharVisibility(false, false, false);
						changeBackground(R.drawable.dg_home_hero_room_day);
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 2;
					}
					else if(story == 2){
						setCharVisibility(false, false, false);
						changeBackground(R.drawable.dg_school_entrance_day);
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story].replace("school_name", schoolName));
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story].replace("school_name", schoolName));
						}
						saveStory(story, plot, chapter);
						story = 3;
					}
					else if(story == 3){
						setCharVisibility(false, false, false);
						changeBackground(R.drawable.dg_school_classroom_day);
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 4;
					}
					else if(story == 4){
						nameBox.clearBuffer();
						nameBox.setText("Mr. Harada");
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.LEFT);
						setCharVisibility(false, true, false);
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story].replace("city_name", cityName).replace("gender", tempGender).replace("char_nickname", charNickname));
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story].replace("city_name", cityName).replace("gender", tempGender).replace("char_nickname", charNickname));
						}
						avatarCenter.setImageResource(R.drawable.dgteacher02);
						saveStory(story, plot, chapter);
						story = 5;
					}
					else if(story == 5){
						nameBox.setText(charName);
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.RIGHT);
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
						avatarLeft.setImageResource(R.drawable.dgteacher01);
						avatarLeft.setColorFilter(R.color.TransBlack);
						if(gender.equals("Male")){
							avatarRight.setImageResource(R.drawable.dgmalehero103);
						}
						else if(gender.equals("Female")){
							avatarRight.setImageResource(R.drawable.dgfemalehero103);
						}
						avatarRight.clearColorFilter();
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story].replace("char_name", charName).replace("city_name", cityName));
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story].replace("char_name", charName).replace("city_name", cityName));
						}
						saveStory(story, plot, chapter);
						story = 6;
					}
					else if(story == 6){
						nameBox.setText("Mr. Harada");
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.LEFT);
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
						avatarLeft.setImageResource(R.drawable.dgteacher02);
						avatarRight.setColorFilter(R.color.TransBlack);
						if(gender.equals("Male")){
							avatarRight.setImageResource(R.drawable.dgmalehero101);
						}
						else if(gender.equals("Female")){
							avatarRight.setImageResource(R.drawable.dgfemalehero101);
						}
						avatarLeft.clearColorFilter();
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story].replace("friend_name_1", friendName1));
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story].replace("friend_name_1", friendName1));
						}
						saveStory(story, plot, chapter);
						story = 7;
					}
					else if(story == 7){
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero111);
							avatarRight.setImageResource(R.drawable.dgkaorukail101);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero111);
							avatarRight.setImageResource(R.drawable.dgmikaohgami101);
						}
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story].replace("friend_name_1", friendName1));
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story].replace("friend_name_1", friendName1));
						}
						saveStory(story, plot, chapter);
						story = 8;
					}
					else if(story == 8){
						disableEvent = true;
						nameBox.setGravity(Gravity.LEFT);
						nameBox.setTypeface(null, Typeface.NORMAL);
						setCharVisibility(true, false, true);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story]);
						}
						textBox.animateText("");
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero111);
							avatarRight.setImageResource(R.drawable.dgkaorukail101);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero111);
							avatarRight.setImageResource(R.drawable.dgmikaohgami101);
						}
						avatarLeft.clearColorFilter();
						avatarRight.clearColorFilter();
						AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[story])
						.setItems(R.array.DG_decision_01, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								decisions = res.getStringArray(R.array.DG_decision_result_01);
								tempDecision = which;
								TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
								ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
								ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
								TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
								avatarRight.setColorFilter(R.color.TransBlack);
								if(tempDecision == 1){
									textBox.animateText("");
									nameBox.setTypeface(null, Typeface.NORMAL);
									if(textSpeed == 0){
										nameBox.clearBuffer();
										nameBox.setText(decisions[tempDecision].replace("gender_3", tempGender3));
									}
									else{
										nameBox.setCharacterDelay(textSpeed);
										nameBox.animateText(decisions[tempDecision].replace("gender_3", tempGender3));
									}
									if(gender.equals("Male")){
										avatarLeft.setImageResource(R.drawable.dgmalehero101);
									}
									else if(gender.equals("Female")){
										avatarLeft.setImageResource(R.drawable.dgfemalehero101);
									}
									socialPoints += 1;
								}
								else{
									if(tempDecision == 0){
										if(gender.equals("Male")){
											avatarLeft.setImageResource(R.drawable.dgmalehero109);
										}
										else if(gender.equals("Female")){
											avatarLeft.setImageResource(R.drawable.dgfemalehero109);
										}
										socialPoints += 0;
									}
									else{
										if(gender.equals("Male")){
											avatarLeft.setImageResource(R.drawable.dgmalehero102);
										}
										else if(gender.equals("Female")){
											avatarLeft.setImageResource(R.drawable.dgfemalehero102);
										}
										socialPoints += 2;
									}
									nameBox.clearBuffer();
									nameBox.setText(charName);
									nameBox.setTypeface(null, Typeface.BOLD);
									if(textSpeed == 0){
										textBox.clearBuffer();
										textBox.setText(decisions[tempDecision]);
									}
									else{
										textBox.setCharacterDelay(textSpeed);
										textBox.animateText(decisions[tempDecision]);
									}
								}
								savePrefInt("SocialPoints", socialPoints);
								inputMode = 1;
								savePrefInt("InputMode", inputMode);
								savePrefInt("TempDecision", tempDecision);
								saveStory(story, plot, chapter);
								story = 9;
							}
						});
						if(inputMode == 0){
							AlertDialog decision = builder.create();
							decision.show();
						}
						else{
							avatarRight.setColorFilter(R.color.TransBlack);
							decisions = res.getStringArray(R.array.DG_decision_result_01);
							
							if(tempDecision == 1){
								textBox.animateText("");
								nameBox.setTypeface(null, Typeface.NORMAL);
								if(textSpeed == 0){
									nameBox.clearBuffer();
									nameBox.setText(decisions[tempDecision].replace("gender_3", tempGender3));
								}
								else{
									nameBox.setCharacterDelay(textSpeed);
									nameBox.animateText(decisions[tempDecision].replace("gender_3", tempGender3));
								}
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero101);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero101);
								}
							}
							else{
								if(tempDecision == 0){
									if(gender.equals("Male")){
										avatarLeft.setImageResource(R.drawable.dgmalehero109);
									}
									else if(gender.equals("Female")){
										avatarLeft.setImageResource(R.drawable.dgfemalehero109);
									}
								}
								else{
									if(gender.equals("Male")){
										avatarLeft.setImageResource(R.drawable.dgmalehero102);
									}
									else if(gender.equals("Female")){
										avatarLeft.setImageResource(R.drawable.dgfemalehero102);
									}
								}
								nameBox.clearBuffer();
								nameBox.setText(charName);
								nameBox.setTypeface(null, Typeface.BOLD);
								if(textSpeed == 0){
									textBox.clearBuffer();
									textBox.setText(decisions[tempDecision]);
								}
								else{
									textBox.setCharacterDelay(textSpeed);
									textBox.animateText(decisions[tempDecision]);
								}
							}
							saveStory(story, plot, chapter);
							story = 9;
						}
					}
					else if(story == 9){
						disableEvent = false;
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
						if(tempDecision == 0){
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero109);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero109);
							}
						}
						else if(tempDecision == 1){
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero101);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero101);
							}
						}
						else if(tempDecision == 2){
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero102);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero102);
							}
						}
						if(gender.equals("Male")){
							avatarRight.setImageResource(R.drawable.dgkaorukail102);
						}
						else if(gender.equals("Female")){
							avatarRight.setImageResource(R.drawable.dgmikaohgami102);
						}
						avatarRight.clearColorFilter();
						avatarLeft.setColorFilter(R.color.TransBlack);
						nameBox.clearBuffer();
						nameBox.setText(friendName1);
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.RIGHT);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story].replace("char_name", charName).replace("friend_name_1", friendName1));
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story].replace("char_name", charName).replace("friend_name_1", friendName1));
						}
						saveStory(story, plot, chapter);
						story = 10;
						inputMode = 0;
						savePrefInt("InputMode", inputMode);
					}
					else if(story == 10){
						setCharVisibility(false, true, false);
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
						avatarCenter.setImageResource(R.drawable.dgteacher02);
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.LEFT);
						nameBox.setText("Mr. Harada");
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 11;
					}
					else if(story == 11){
						setCharVisibility(false, false, false);
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 0;
						chapter = 0;
						plot = 3;
					}
				}
				else if(plot == 3){
					stories = res.getStringArray(R.array.DG_Story_04);
					
					if(story == 0){
						setCharVisibility(false, false, false);
						currentView.setBackgroundColor(Color.BLACK);
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 1;
					}
					else if(story == 1){
						setCharVisibility(true, false, true);
						changeBackground(R.drawable.dg_school_classroom_evening);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero111);
							avatarRight.setImageResource(R.drawable.dgkaorukail102);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero111);
							avatarRight.setImageResource(R.drawable.dgmikaohgami102);
						}
						avatarLeft.setColorFilter(R.color.TransBlack);
						avatarRight.clearColorFilter();
						nameBox.clearBuffer();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.RIGHT);
						nameBox.setText(friendName1);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story].replace("char_nickname", charNickname));
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story].replace("char_nickname", charNickname));
						}
						saveStory(story, plot, chapter);
						story = 2;
					}
					else if(story == 2){
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_evening);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero111);
							avatarRight.setImageResource(R.drawable.dgrenaohgami102);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero111);
							avatarRight.setImageResource(R.drawable.dgkaorukarl102);
						}
						avatarLeft.setColorFilter(R.color.TransBlack);
						avatarRight.clearColorFilter();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.RIGHT);
						nameBox.setText(friendName2);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story].replace("friend_name_2", friendName2).replace("friend_nickname_2", friendNickname2));
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story].replace("friend_name_2", friendName2).replace("friend_nickname_2", friendNickname2));
						}
						saveStory(story, plot, chapter);
						story = 3;
					}
					else if(story == 3){
						disableEvent = true;
						nameBox.setGravity(Gravity.LEFT);
						nameBox.setTypeface(null, Typeface.NORMAL);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story]);
						}
						textBox.animateText("");
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_evening);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero111);
							avatarRight.setImageResource(R.drawable.dgrenaohgami102);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero111);
							avatarRight.setImageResource(R.drawable.dgkaorukarl102);
						}
						avatarLeft.clearColorFilter();
						avatarRight.clearColorFilter();
						AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[story])
						.setItems(R.array.DG_decision_02, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								decisions = res.getStringArray(R.array.DG_decision_result_02);
								tempDecision = which;
								TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
								ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
								ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
								TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
								avatarRight.setColorFilter(R.color.TransBlack);
								if(tempDecision == 0){
									textBox.animateText("");
									nameBox.setTypeface(null, Typeface.NORMAL);
									if(textSpeed == 0){
										nameBox.clearBuffer();
										nameBox.setText(decisions[tempDecision].replace("gender_3", tempGender4));
									}
									else{
										nameBox.setCharacterDelay(textSpeed);
										nameBox.animateText(decisions[tempDecision].replace("gender_3", tempGender4));
									}
									if(gender.equals("Male")){
										avatarLeft.setImageResource(R.drawable.dgmalehero101);
									}
									else if(gender.equals("Female")){
										avatarLeft.setImageResource(R.drawable.dgfemalehero101);
									}
									socialPoints += 1;
								}
								else{
									if(tempDecision == 2){
										if(gender.equals("Male")){
											avatarLeft.setImageResource(R.drawable.dgmalehero109);
										}
										else if(gender.equals("Female")){
											avatarLeft.setImageResource(R.drawable.dgfemalehero109);
										}
										socialPoints += 0;
									}
									else{
										if(gender.equals("Male")){
											avatarLeft.setImageResource(R.drawable.dgmalehero102);
										}
										else if(gender.equals("Female")){
											avatarLeft.setImageResource(R.drawable.dgfemalehero102);
										}
										socialPoints += 2;
									}
									nameBox.clearBuffer();
									nameBox.setText(charName);
									nameBox.setTypeface(null, Typeface.BOLD);
									if(textSpeed == 0){
										textBox.clearBuffer();
										textBox.setText(decisions[tempDecision].replace("friend_nickname_2", friendNickname2));
									}
									else{
										textBox.setCharacterDelay(textSpeed);
										textBox.animateText(decisions[tempDecision].replace("friend_nickname_2", friendNickname2));
									}
								}
								savePrefInt("SocialPoints", socialPoints);
								inputMode = 1;
								savePrefInt("InputMode", inputMode);
								savePrefInt("TempDecision", tempDecision);
								saveStory(story, plot, chapter);
								story = 4;
							}
						});
						if(inputMode == 0){
							AlertDialog decision = builder.create();
							decision.show();
						}
						else{
							avatarRight.setColorFilter(R.color.TransBlack);
							decisions = res.getStringArray(R.array.DG_decision_result_02);
							
							if(tempDecision == 0){
								textBox.animateText("");
								nameBox.setTypeface(null, Typeface.NORMAL);
								if(textSpeed == 0){
									nameBox.clearBuffer();
									nameBox.setText(decisions[tempDecision].replace("gender_3", tempGender3));
								}
								else{
									nameBox.setCharacterDelay(textSpeed);
									nameBox.animateText(decisions[tempDecision].replace("gender_3", tempGender3));
								}
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero101);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero101);
								}
							}
							else{
								if(tempDecision == 2){
									if(gender.equals("Male")){
										avatarLeft.setImageResource(R.drawable.dgmalehero109);
									}
									else if(gender.equals("Female")){
										avatarLeft.setImageResource(R.drawable.dgfemalehero109);
									}
								}
								else{
									if(gender.equals("Male")){
										avatarLeft.setImageResource(R.drawable.dgmalehero102);
									}
									else if(gender.equals("Female")){
										avatarLeft.setImageResource(R.drawable.dgfemalehero102);
									}
								}
								nameBox.clearBuffer();
								nameBox.setText(charName);
								nameBox.setTypeface(null, Typeface.BOLD);
								if(textSpeed == 0){
									textBox.clearBuffer();
									textBox.setText(decisions[tempDecision].replace("friend_nickname_2", friendNickname2));
								}
								else{
									textBox.setCharacterDelay(textSpeed);
									textBox.animateText(decisions[tempDecision].replace("friend_nickname_2", friendNickname2));
								}
							}
							saveStory(story, plot, chapter);
							story = 4;
						}
					}
					else if(story == 4){
						disableEvent = false;
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_evening);
						if(tempDecision == 2){
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero109);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero109);
							}
						}
						else if(tempDecision == 0){
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero101);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero101);
							}
						}
						else if(tempDecision == 1){
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero102);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero102);
							}
						}
						if(gender.equals("Male")){
							avatarRight.setImageResource(R.drawable.dgrenaohgami102);
						}
						else if(gender.equals("Female")){
							avatarRight.setImageResource(R.drawable.dgkaorukarl102);
						}
						avatarRight.clearColorFilter();
						avatarLeft.setColorFilter(R.color.TransBlack);
						nameBox.clearBuffer();
						nameBox.setText(friendName2);
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.RIGHT);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 5;
						inputMode = 0;
						savePrefInt("InputMode", inputMode);
					}
					else if(story == 5){
						setCharVisibility(false, true, false);
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_evening);
						if(gender.equals("Male")){
							avatarCenter.setImageResource(R.drawable.dgmalehero101);
						}
						else if(gender.equals("Female")){
							avatarCenter.setImageResource(R.drawable.dgfemalehero101);
						}
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 6;
					}
					else if(story == 6){
						setCharVisibility(false, true, false);
						changeBackground(R.drawable.dg_city_shop_evening);
						if(gender.equals("Male")){
							avatarCenter.setImageResource(R.drawable.dgmalehero101);
						}
						else if(gender.equals("Female")){
							avatarCenter.setImageResource(R.drawable.dgfemalehero101);
						}
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 7;
					}
					else if(story == 7){
						setCharVisibility(false, true, false);
						changeBackground(R.drawable.dg_city_park_entrance_evening);
						if(gender.equals("Male")){
							avatarCenter.setImageResource(R.drawable.dgmalehero101);
						}
						else if(gender.equals("Female")){
							avatarCenter.setImageResource(R.drawable.dgfemalehero101);
						}
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 8;
					}
					else if(story == 8){
						setCharVisibility(true, false, true);
						changeBackground(R.drawable.dg_city_park_bench);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero111);
							avatarRight.setImageResource(R.drawable.dgkaorukail103);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero111);
							avatarRight.setImageResource(R.drawable.dgmikaohgami103);
						}
						avatarLeft.clearColorFilter();
						avatarRight.clearColorFilter();
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story].replace("friend_name_1", friendName1));
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story].replace("friend_name_1", friendName1));
						}
						saveStory(story, plot, chapter);
						story = 9;
						inputMode = 0;
						savePrefInt("InputMode", inputMode);
					}
					else if(story == 9){
						disableEvent = true;
						nameBox.clearBuffer();
						nameBox.setGravity(Gravity.LEFT);
						nameBox.setTypeface(null, Typeface.NORMAL);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story]);
						}
						textBox.animateText("");
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_city_park_bench);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero111);
							avatarRight.setImageResource(R.drawable.dgkaorukail103);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero111);
							avatarRight.setImageResource(R.drawable.dgmikaohgami103);
						}
						String[] tempDes = res.getStringArray(R.array.DG_decision_03);
						tempDes[0] = tempDes[0].replace("gender_3", tempGender3);
						avatarLeft.clearColorFilter();
						avatarRight.clearColorFilter();
						AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[story])
						.setItems(tempDes, new DialogInterface.OnClickListener() {
								
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								decisions = res.getStringArray(R.array.DG_decision_result_03);
								tempDecision = which;
								TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
								ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
								ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
								TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
								if(tempDecision == 0){
									if(gender.equals("Male")){
										avatarLeft.setImageResource(R.drawable.dgmalehero102);
									}
									else if(gender.equals("Female")){
										avatarLeft.setImageResource(R.drawable.dgfemalehero102);
									}
									socialPoints += 1;
								}
								else if(tempDecision == 1){
									if(gender.equals("Male")){
										avatarLeft.setImageResource(R.drawable.dgmalehero103);
									}
									else if(gender.equals("Female")){
										avatarLeft.setImageResource(R.drawable.dgfemalehero103);
									}
									socialPoints += 2;
								}
								else if(tempDecision == 2){
									if(gender.equals("Male")){
										avatarLeft.setImageResource(R.drawable.dgmalehero109);
									}
									else if(gender.equals("Female")){
										avatarLeft.setImageResource(R.drawable.dgfemalehero109);
									}
									socialPoints += 0;
								}
								avatarRight.setColorFilter(R.color.TransBlack);
								nameBox.clearBuffer();
								nameBox.setTypeface(null, Typeface.BOLD);
								nameBox.setText(charName);
								if(textSpeed == 0){
									textBox.clearBuffer();
									textBox.setText(decisions[tempDecision]);
								}
								else{
									textBox.setCharacterDelay(textSpeed);
									textBox.animateText(decisions[tempDecision]);
								}
								savePrefInt("SocialPoints", socialPoints);
								inputMode = 1;
								savePrefInt("InputMode", inputMode);
								savePrefInt("TempDecision", tempDecision);
								saveStory(story, plot, chapter);
								story = 10;
							}
						});
						if(inputMode == 0){
							AlertDialog decision = builder.create();
							decision.show();
						}
						else{
							decisions = res.getStringArray(R.array.DG_decision_result_03);
							nameBox.clearBuffer();
							nameBox.setTypeface(null, Typeface.BOLD);
							nameBox.setText(charName);
							if(textSpeed == 0){
								textBox.clearBuffer();
								textBox.setText(decisions[tempDecision]);
							}
							else{
								textBox.setCharacterDelay(textSpeed);
								textBox.animateText(decisions[tempDecision]);
							}
							if(tempDecision == 0){
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero102);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero102);
								}
							}
							else if(tempDecision == 1){
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero103);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero103);
								}
							}
							else if(tempDecision == 2){
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero109);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero109);
								}
							}
							avatarRight.setColorFilter(R.color.TransBlack);
							saveStory(story, plot, chapter);
							story = 10;
						}
					}
					else if(story == 10){
						disableEvent = false;
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_city_park_bench);
						if(gender.equals("Male")){
							if(tempDecision == 0){
								avatarLeft.setImageResource(R.drawable.dgmalehero102);
							}
							else if(tempDecision == 1){
								avatarLeft.setImageResource(R.drawable.dgmalehero103);
							}
							else if(tempDecision == 2){
								avatarLeft.setImageResource(R.drawable.dgmalehero109);
							}
							avatarRight.setImageResource(R.drawable.dgkaorukail104);
						}
						else if(gender.equals("Female")){
							if(tempDecision == 0){
								avatarLeft.setImageResource(R.drawable.dgfemalehero102);
							}
							else if(tempDecision == 1){
								avatarLeft.setImageResource(R.drawable.dgfemalehero103);
							}
							else if(tempDecision == 2){
								avatarLeft.setImageResource(R.drawable.dgfemalehero109);
							}
							avatarRight.setImageResource(R.drawable.dgmikaohgami104);
						}
						avatarLeft.setColorFilter(R.color.TransBlack);
						avatarRight.clearColorFilter();
						nameBox.clearBuffer();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.RIGHT);
						nameBox.setText(friendName1);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 11;
						inputMode = 0;
						savePrefInt("InputMode", inputMode);
					}
					else if(story == 11){
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_city_park_bench);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero102);
							avatarRight.setImageResource(R.drawable.dgkaorukail103);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero102);
							avatarRight.setImageResource(R.drawable.dgmikaohgami103);
						}
						avatarRight.setColorFilter(R.color.TransBlack);
						avatarLeft.clearColorFilter();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.LEFT);
						nameBox.setText(charName);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 12;
					}
					else if(story == 12){
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_city_park_bench);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgrenaohgami102);
							avatarRight.setImageResource(R.drawable.dgkaorukail103);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgkaorukarl102);
							avatarRight.setImageResource(R.drawable.dgmikaohgami103);
						}
						avatarRight.setColorFilter(R.color.TransBlack);
						avatarLeft.clearColorFilter();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.LEFT);
						nameBox.setText(friendName2);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story].replace("char_name", charName));
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story].replace("char_name", charName));
						}
						saveStory(story, plot, chapter);
						story = 13;
					}
					else if(story == 13){
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_city_park_bench);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgrenaohgami101);
							avatarRight.setImageResource(R.drawable.dgkaorukail104);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgkaorukarl101);
							avatarRight.setImageResource(R.drawable.dgmikaohgami104);
						}
						avatarLeft.setColorFilter(R.color.TransBlack);
						avatarRight.clearColorFilter();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.RIGHT);
						nameBox.setText(friendName1);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story].replace("char_nickname", charNickname));
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story].replace("char_nickname", charNickname));
						}
						saveStory(story, plot, chapter);
						story = 14;
					}
					else if(story == 14){
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_city_park_bench);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgrenaohgami102);
							avatarRight.setImageResource(R.drawable.dgkaorukail103);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgkaorukarl102);
							avatarRight.setImageResource(R.drawable.dgmikaohgami103);
						}
						avatarRight.setColorFilter(R.color.TransBlack);
						avatarLeft.clearColorFilter();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.LEFT);
						nameBox.setText(friendName2);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story].replace("char_nickname", charNickname));
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story].replace("char_nickname", charNickname));
						}
						saveStory(story, plot, chapter);
						story = 15;
					}
					else if(story == 15){
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_city_park_bench);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero103);
							avatarRight.setImageResource(R.drawable.dgrenaohgami101);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero103);
							avatarRight.setImageResource(R.drawable.dgkaorukarl101);
						}
						avatarRight.setColorFilter(R.color.TransBlack);
						avatarLeft.clearColorFilter();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.LEFT);
						nameBox.setText(charName);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 16;
					}
					else if(story == 16){
						setCharVisibility(false, false, false);
						changeBackground(R.drawable.dg_home_entrance_night_with_lamp);
						textBox.animateText("");
						nameBox.setCharacterDelay(textSpeed);
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						nameBox.animateText(stories[story]);
						saveStory(story, plot, chapter);
						story = 17;
					}
					else if(story == 17){
						setCharVisibility(false, false, false);
						changeBackground(R.drawable.dg_home_hero_room_night);
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 18;
					}
					else if(story == 18){
						setCharVisibility(false, false, false);
						changeBackground(R.drawable.dg_school_classroom_day);
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 19;
					}
					else if(story == 19){
						setCharVisibility(false, false, false);
						changeBackground(R.drawable.dg_school_entrance_day);
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story]);
						}
						saveStory(story, plot, chapter);
						story = 0;
						plot = 0;
						chapter = 1;
					}
				}
			}
		}
		else if(chapter == 1){
			if(plot == 0){
				stories = res.getStringArray(R.array.DG_Story_05);
				
				if(story == 0){
					textBox.setCharacterDelay(100);
					textBox.animateText("Chapter " + String.valueOf(chapter + 1) + "\n" + chapters[chapter]);
					saveStory(story, plot, chapter);
					story = 1;
				}
				else{
					ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
					ImageView avatarCenter = (ImageView) findViewById(R.id.avatarCenter);
					ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
					TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
					if(story == 1){
						nameBox.setGravity(Gravity.LEFT);
						setCharVisibility(false, false, false);
						changeBackground(R.drawable.dg_school_entrance_day);
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story - 1]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story - 1]);
						}
						saveStory(story, plot, chapter);
						story = 2;
					}
					else if(story == 2){
						nameBox.setGravity(Gravity.LEFT);
						setCharVisibility(false, false, false);
						changeBackground(R.drawable.dg_school_main_hall_day);
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story - 1]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story - 1]);
						}
						saveStory(story, plot, chapter);
						story = 3;
					}
					else if(story == 3){
						nameBox.setGravity(Gravity.LEFT);
						setCharVisibility(false, true, false);
						currentView.setBackgroundResource(R.drawable.dg_school_main_hall_day);
						avatarCenter.setImageResource(R.drawable.dgheadmaster02);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story - 1]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story - 1]);
						}
						nameBox.clearBuffer();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setText("Headmaster");
						saveStory(story, plot, chapter);
						story = 4;
					}
					else if(story == 4){
						nameBox.setGravity(Gravity.LEFT);
						setCharVisibility(false, true, false);
						currentView.setBackgroundResource(R.drawable.dg_school_main_hall_day);
						avatarCenter.setImageResource(R.drawable.dgheadmaster06);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story - 1]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story - 1]);
						}
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setText("Headmaster");
						saveStory(story, plot, chapter);
						story = 5;
					}
					else if(story == 5){
						nameBox.setGravity(Gravity.LEFT);
						setCharVisibility(false, true, false);
						currentView.setBackgroundResource(R.drawable.dg_school_main_hall_day);
						avatarCenter.setImageResource(R.drawable.dgheadmaster05);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story - 1]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story - 1]);
						}
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setText("Headmaster");
						saveStory(story, plot, chapter);
						story = 6;
					}
					else if(story == 6){
						nameBox.setGravity(Gravity.LEFT);
						setCharVisibility(false, false, false);
						currentView.setBackgroundResource(R.drawable.dg_school_main_hall_day);
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story - 1]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story - 1]);
						}
						saveStory(story, plot, chapter);
						story = 7;
					}
					else if(story == 7){
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_school_main_hall_day);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgrenaohgami103);
							avatarRight.setImageResource(R.drawable.dgkaorukail103);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgkaorukarl103);
							avatarRight.setImageResource(R.drawable.dgmikaohgami103);
						}
						avatarRight.setColorFilter(R.color.TransBlack);
						avatarLeft.clearColorFilter();
						nameBox.clearBuffer();
						nameBox.setGravity(Gravity.LEFT);
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setText(friendName2);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story - 1]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story - 1]);
						}
						saveStory(story, plot, chapter);
						story = 8;
					}
					else if(story == 8){
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_school_main_hall_day);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgrenaohgami104);
							avatarRight.setImageResource(R.drawable.dgkaorukail105);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgkaorukarl104);
							avatarRight.setImageResource(R.drawable.dgmikaohgami105);
						}
						avatarLeft.setColorFilter(R.color.TransBlack);
						avatarRight.clearColorFilter();
						nameBox.setGravity(Gravity.RIGHT);
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setText(friendName1);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story - 1].replace("friend_nickname_2", friendNickname2));
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story - 1].replace("friend_nickname_2", friendNickname2));
						}
						saveStory(story, plot, chapter);
						story = 9;
					}
					else if(story == 9){
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_school_main_hall_day);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero110);
							avatarRight.setImageResource(R.drawable.dgkaorukail103);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero110);
							avatarRight.setImageResource(R.drawable.dgmikaohgami103);
						}
						avatarLeft.clearColorFilter();
						avatarRight.setColorFilter(R.color.TransBlack);
						nameBox.setGravity(Gravity.LEFT);
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setText(charName);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story - 1]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story - 1]);
						}
						saveStory(story, plot, chapter);
						story = 10;
					}
					else if(story == 10){
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_school_main_hall_day);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero102);
							avatarRight.setImageResource(R.drawable.dgkaorukail101);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero102);
							avatarRight.setImageResource(R.drawable.dgmikaohgami101);
						}
						avatarLeft.clearColorFilter();
						avatarRight.setColorFilter(R.color.TransBlack);
						nameBox.setGravity(Gravity.LEFT);
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setText(charName);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story - 1]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story - 1]);
						}
						saveStory(story, plot, chapter);
						story = 11;
					}
					else if(story == 11){
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_school_main_hall_day);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero101);
							avatarRight.setImageResource(R.drawable.dgkaorukail102);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero101);
							avatarRight.setImageResource(R.drawable.dgmikaohgami102);
						}
						avatarLeft.setColorFilter(R.color.TransBlack);
						avatarRight.clearColorFilter();
						nameBox.setGravity(Gravity.RIGHT);
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setText(friendName1);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story - 1].replace("char_nickname", charNickname));
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story - 1].replace("char_nickname", charNickname));
						}
						saveStory(story, plot, chapter);
						story = 12;
					}
					else if(story == 12){
						disableEvent = true;
						nameBox.clearBuffer();
						nameBox.setGravity(Gravity.LEFT);
						nameBox.setTypeface(null, Typeface.NORMAL);
						textBox.animateText("");
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(stories[story - 1]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(stories[story - 1]);
						}
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_school_main_hall_day);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero111);
							avatarRight.setImageResource(R.drawable.dgkaorukail101);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero111);
							avatarRight.setImageResource(R.drawable.dgmikaohgami101);
						}
						avatarLeft.clearColorFilter();
						avatarRight.clearColorFilter();
						AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[story - 1])
						.setItems(R.array.DG_decision_04, new DialogInterface.OnClickListener() {
									
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								decisions = res.getStringArray(R.array.DG_decision_result_04);
								tempDecision = which;
								TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
								ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
								ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
								TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
								if(tempDecision == 0){
									if(gender.equals("Male")){
										avatarLeft.setImageResource(R.drawable.dgmalehero102);
										avatarRight.setImageResource(R.drawable.dgkaorukail101);
									}
									else if(gender.equals("Female")){
										avatarLeft.setImageResource(R.drawable.dgfemalehero102);
										avatarRight.setImageResource(R.drawable.dgmikaohgami101);
									}
									socialPoints += 1;
								}
								else if(tempDecision == 1){
									if(gender.equals("Male")){
										avatarLeft.setImageResource(R.drawable.dgmalehero109);
										avatarRight.setImageResource(R.drawable.dgkaorukail101);
									}
									else if(gender.equals("Female")){
										avatarLeft.setImageResource(R.drawable.dgfemalehero109);
										avatarRight.setImageResource(R.drawable.dgmikaohgami101);
									}
									socialPoints += 0;
								}
								avatarRight.setColorFilter(R.color.TransBlack);
								nameBox.clearBuffer();
								nameBox.setTypeface(null, Typeface.BOLD);
								nameBox.setText(charName);
								if(textSpeed == 0){
									textBox.clearBuffer();
									textBox.setText(decisions[tempDecision]);
								}
								else{
									textBox.setCharacterDelay(textSpeed);
									textBox.animateText(decisions[tempDecision]);
								}
								savePrefInt("SocialPoints", socialPoints);
								inputMode = 1;
								savePrefInt("InputMode", inputMode);
								savePrefInt("TempDecision", tempDecision);
								saveStory(story, plot, chapter);
								story = 13;
								}
							}
						);
						if(inputMode == 0){
							AlertDialog decision = builder.create();
							decision.show();
						}
						else{
							decisions = res.getStringArray(R.array.DG_decision_result_04);
							nameBox.clearBuffer();
							nameBox.setTypeface(null, Typeface.BOLD);
							nameBox.setText(charName);
							if(textSpeed == 0){
								textBox.clearBuffer();
								textBox.setText(decisions[tempDecision]);
							}
							else{
								textBox.setCharacterDelay(textSpeed);
								textBox.animateText(decisions[tempDecision]);
							}
							if(tempDecision == 0){
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero102);
									avatarRight.setImageResource(R.drawable.dgkaorukail101);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero102);
									avatarRight.setImageResource(R.drawable.dgmikaohgami101);
								}
							}
							else if(tempDecision == 1){
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero109);
									avatarRight.setImageResource(R.drawable.dgkaorukail101);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero109);
									avatarRight.setImageResource(R.drawable.dgmikaohgami101);
								}
							}
							avatarRight.setColorFilter(R.color.TransBlack);
							saveStory(story, plot, chapter);
							story = 13;
						}
					}
					else if(story == 13){
						disableEvent = false;
						decisions = res.getStringArray(R.array.DG_decision_result_04);
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_school_main_hall_day);
						if(tempDecision == 0){
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero102);
								avatarRight.setImageResource(R.drawable.dgkaorukail101);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero102);
								avatarRight.setImageResource(R.drawable.dgmikaohgami101);
							}
							nameBox.setText(charName);
						}
						else if(tempDecision == 1){
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgrenaohgami102);
								avatarRight.setImageResource(R.drawable.dgkaorukail101);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgkaorukarl102);
								avatarRight.setImageResource(R.drawable.dgmikaohgami101);
							}
							nameBox.setText(friendName2);
						}
						avatarRight.setColorFilter(R.color.TransBlack);
						avatarLeft.clearColorFilter();
						nameBox.clearBuffer();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(decisions[tempDecision + 2]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(decisions[tempDecision + 2]);
						}
						saveStory(story, plot, chapter);
						story = 14;
						inputMode = 0;
						savePrefInt("InputMode", inputMode);
					}
					else if(story == 14){
						setCharVisibility(true, false, true);
						currentView.setBackgroundResource(R.drawable.dg_school_main_hall_day);
						if(gender.equals("Male")){
							if(tempDecision == 0){
								avatarRight.setImageResource(R.drawable.dgrenaohgami102);
								avatarLeft.setImageResource(R.drawable.dgmalehero101);
								nameBox.setText(friendName2);
							}
							else{
								avatarLeft.setImageResource(R.drawable.dgrenaohgami101);
								avatarRight.setImageResource(R.drawable.dgkaorukail102);
								nameBox.setText(friendName1);
							}
						}
						else if(gender.equals("Female")){
							if(tempDecision == 0){
								avatarRight.setImageResource(R.drawable.dgkaorukarl102);
								avatarLeft.setImageResource(R.drawable.dgfemalehero101);
								nameBox.setText(friendName2);
							}
							else{
								avatarLeft.setImageResource(R.drawable.dgkaorukarl101);
								avatarRight.setImageResource(R.drawable.dgmikaohgami102);
								nameBox.setText(friendName1);
							}
						}
						avatarLeft.setColorFilter(R.color.TransBlack);
						avatarRight.clearColorFilter();
						nameBox.setGravity(Gravity.RIGHT);
						nameBox.setTypeface(null, Typeface.BOLD);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(stories[story - 2]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(stories[story - 2]);
						}
						saveStory(story, plot, chapter);
						story = 0;
						plot = 1;
					}
				}
			}
			else if(plot == 1){
				ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
				ImageView avatarCenter = (ImageView) findViewById(R.id.avatarCenter);
				ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
				TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
				stories = res.getStringArray(R.array.DG_Story_06);
				if(story == 0){
					setCharVisibility(false, true, false);
					if(gender.equals("Male")){
						avatarCenter.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarCenter.setImageResource(R.drawable.dgfemalehero101);
					}
					currentView.setBackgroundResource(R.drawable.dg_school_main_hall_day);
					avatarLeft.clearColorFilter();
					avatarRight.clearColorFilter();
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setTypeface(null, Typeface.NORMAL);
					textBox.animateText("");
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[story]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[story]);
					}
					saveStory(story, plot, chapter);
					story = 1;
				}
				else if(story == 1){
					disableEvent = true;
					nameBox.clearBuffer();
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setTypeface(null, Typeface.NORMAL);
					textBox.animateText("");
					setCharVisibility(false, true, false);
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[story]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[story]);
					}
					currentView.setBackgroundResource(R.drawable.dg_school_main_hall_day);
					if(gender.equals("Male")){
						avatarCenter.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarCenter.setImageResource(R.drawable.dgfemalehero101);
					}
					AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[story])
					.setItems(R.array.DG_decision_05, new DialogInterface.OnClickListener() {
								
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							decisions = res.getStringArray(R.array.DG_decision_result_05);
							tempDecision = which;
							branch = which;
							TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
							ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
							ImageView avatarCenter = (ImageView) findViewById(R.id.avatarCenter);
							ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
							TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
							if(tempDecision == 0){
								setCharVisibility(true, false, true);
								avatarRight.setImageResource(R.drawable.dgteacher01);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero101);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero101);
								}
								changeBackground(R.drawable.dg_school_faculty_room);
							}
							else if(tempDecision == 1){
								setCharVisibility(false, true, false);
								if(gender.equals("Male")){
									avatarCenter.setImageResource(R.drawable.dgmalehero101);
								}
								else if(gender.equals("Female")){
									avatarCenter.setImageResource(R.drawable.dgfemalehero101);
								}
								changeBackground(R.drawable.dg_school_classroom_day);
							}
							else if(tempDecision == 2){
								setCharVisibility(true, false, true);
								avatarRight.setImageResource(R.drawable.dgjanitor01);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero101);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero101);
								}
								changeBackground(R.drawable.dg_school_library_day);
							}
							avatarLeft.clearColorFilter();
							avatarRight.clearColorFilter();
							nameBox.setTypeface(null, Typeface.NORMAL);
							textBox.animateText("");
							if(textSpeed == 0){
								nameBox.clearBuffer();
								nameBox.setText(decisions[tempDecision].replace("school_name", schoolName));
							}
							else{
								nameBox.setCharacterDelay(textSpeed);
								nameBox.animateText(decisions[tempDecision].replace("school_name", schoolName));
							}
							inputMode = 1;
							savePrefInt("Branch", branch);
							savePrefInt("InputMode", inputMode);
							savePrefInt("TempDecision", tempDecision);
							saveStory(story, plot, chapter);
							story = 2;
							inputMode = 0;
							}
						}
					);
					if(inputMode == 0){
						AlertDialog decision = builder.create();
						decision.show();
					}
					else{
						decisions = res.getStringArray(R.array.DG_decision_result_05);
						if(tempDecision == 0){
							setCharVisibility(true, false, true);
							avatarRight.setImageResource(R.drawable.dgteacher01);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero101);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero101);
							}
							changeBackground(R.drawable.dg_school_faculty_room);
						}
						else if(tempDecision == 1){
							setCharVisibility(false, true, false);
							if(gender.equals("Male")){
								avatarCenter.setImageResource(R.drawable.dgmalehero101);
							}
							else if(gender.equals("Female")){
								avatarCenter.setImageResource(R.drawable.dgfemalehero101);
							}
							changeBackground(R.drawable.dg_school_classroom_day);
						}
						else if(tempDecision == 2){
							setCharVisibility(true, false, true);
							avatarRight.setImageResource(R.drawable.dgjanitor01);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero101);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero101);
							}
							changeBackground(R.drawable.dg_school_library_day);
						}
						avatarLeft.clearColorFilter();
						avatarRight.clearColorFilter();
						nameBox.setTypeface(null, Typeface.NORMAL);
						textBox.animateText("");
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(decisions[tempDecision].replace("school_name", schoolName));
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(decisions[tempDecision].replace("school_name", schoolName));
						}
						saveStory(story, plot, chapter);
						story = 2;
					}
				}
				else if(story >= 2 && story <= 5){
					setSituations2();
				}
				else if(story == 6){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText(charName);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					setCharVisibility(false, true, false);
					currentView.setBackgroundResource(R.drawable.dg_school_rooftop_day);
					if(gender.equals("Male")){
						avatarCenter.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarCenter.setImageResource(R.drawable.dgfemalehero101);
					}
					avatarCenter.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 7;
				}
				else if(story == 7){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.setText(friendName1);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarCenter.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_rooftop_day);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
						avatarRight.setImageResource(R.drawable.dgkaorukail102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						avatarRight.setImageResource(R.drawable.dgmikaohgami102);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 8;
				}
				else if(story == 8){
					stories = res.getStringArray(R.array.DG_Story_07);
					textBox.animateText("");
					nameBox.setTypeface(null, Typeface.NORMAL);
					nameBox.setGravity(Gravity.LEFT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_rooftop_day);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
						avatarRight.setImageResource(R.drawable.dgkaorukail102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						avatarRight.setImageResource(R.drawable.dgmikaohgami102);
					}
					avatarRight.setColorFilter(R.color.TransBlack);
					avatarLeft.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[story - 6]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 9;
				}
				else if(story == 9){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.setText(friendName1);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarCenter.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_rooftop_day);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
						avatarRight.setImageResource(R.drawable.dgkaorukail102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						avatarRight.setImageResource(R.drawable.dgmikaohgami102);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 10;
				}
				else if(story == 10){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.setText(friendName2);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					avatarCenter.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_rooftop_day);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgrenaohgami102);
						avatarRight.setImageResource(R.drawable.dgkaorukail101);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgkaorukarl102);
						avatarRight.setImageResource(R.drawable.dgmikaohgami101);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 11;
				}
				else if(story == 11){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.setText(friendName1);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarCenter.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_rooftop_day);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgrenaohgami101);
						avatarRight.setImageResource(R.drawable.dgkaorukail102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgkaorukarl101);
						avatarRight.setImageResource(R.drawable.dgmikaohgami102);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 12;
				}
				else if(story == 12){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText(charName);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_rooftop_day);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
						avatarRight.setImageResource(R.drawable.dgkaorukail101);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
						avatarRight.setImageResource(R.drawable.dgmikaohgami101);
					}
					avatarRight.setColorFilter(R.color.TransBlack);
					avatarLeft.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 13;
				}
				else if(story == 13){
					disableEvent = true;
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText(friendName1);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_rooftop_day);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
						avatarRight.setImageResource(R.drawable.dgkaorukail102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						avatarRight.setImageResource(R.drawable.dgmikaohgami102);
					}
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarRight.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					String[] tempDec = res.getStringArray(R.array.DG_decision_06);
					List<String> tempList = new ArrayList<String>();
					tempList.add(tempDec[0]);
					tempList.add(tempDec[1]);
					tempList.add(tempDec[2]);
					final String[] tempDes = new String[3];
					if(dec01){
						tempList.remove("Mr. Harada");
					}
					else if(dec02){
						tempList.remove("Akiyama");
					}
					else if(dec03){
						story = 17;
					}
					if(tempList.size() == 3){
						tempDes[0] = tempList.get(0);
						tempDes[1] = tempList.get(1);
						tempDes[2] = tempList.get(2);
					}
					else if(tempList.size() == 2){
						tempDes[0] = tempList.get(0);
						tempDes[1] = tempList.get(1);
					}
					else if(tempList.size() == 1){
						tempDes[0] = tempList.get(0);
					}
					AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[story - 6])
					.setItems(tempDes, new DialogInterface.OnClickListener() {
								
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							decisions = res.getStringArray(R.array.DG_decision_06);
							selectedItem = tempDes[which];
							TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
							ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
							ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
							TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
							if(selectedItem.equals("Mr. Harada")){
								branch = 0;
								analyticalPoints -= 4;
							}
							else if(selectedItem.equals("Akiyama")){
								branch = 1;
								analyticalPoints -= 4;
							}
							else if(selectedItem.equals("Janitor")){
								branch = 2;
								analyticalPoints -= 0;
							}
							nameBox.clearBuffer();
							nameBox.setText(charName);
							nameBox.setTypeface(null, Typeface.BOLD);
							nameBox.setGravity(Gravity.LEFT);
							avatarLeft.startAnimation(fadeOut);
							avatarRight.startAnimation(fadeOut);
							setCharVisibility(true, false, true);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero102);
								avatarRight.setImageResource(R.drawable.dgkaorukail101);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero102);
								avatarRight.setImageResource(R.drawable.dgmikaohgami101);
							}
							avatarRight.setColorFilter(R.color.TransBlack);
							avatarLeft.clearColorFilter();
							avatarLeft.startAnimation(fadeIn);
							avatarRight.startAnimation(fadeIn);
							if(textSpeed == 0){
								textBox.clearBuffer();
								textBox.setText(decisions[branch]);
							}
							else{
								textBox.setCharacterDelay(textSpeed);
								textBox.animateText(decisions[branch]);
							}
							inputMode = 1;
							savePrefInt("InputMode", inputMode);
							savePrefInt("Branch", branch);
							saveStory(story, plot, chapter);
							story = 14;
							inputMode = 0;
							}
						}
					);
					if(inputMode == 0){
						AlertDialog decision = builder.create();
						decision.show();
					}
					else{
						decisions = res.getStringArray(R.array.DG_decision_06);
						nameBox.clearBuffer();
						nameBox.setText(charName);
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.LEFT);
						avatarLeft.startAnimation(fadeOut);
						avatarRight.startAnimation(fadeOut);
						setCharVisibility(true, false, true);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero102);
							avatarRight.setImageResource(R.drawable.dgkaorukail101);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero102);
							avatarRight.setImageResource(R.drawable.dgmikaohgami101);
						}
						avatarRight.setColorFilter(R.color.TransBlack);
						avatarLeft.clearColorFilter();
						avatarLeft.startAnimation(fadeIn);
						avatarRight.startAnimation(fadeIn);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(decisions[branch]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(decisions[branch]);
						}
						saveStory(story, plot, chapter);
						story = 14;
					}
				}
				else if(story == 14){
					savePrefInt("InputMode", inputMode);
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText(friendName1);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_rooftop_day);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
						avatarRight.setImageResource(R.drawable.dgkaorukail102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						avatarRight.setImageResource(R.drawable.dgmikaohgami102);
					}
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarRight.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					if(branch == 0){
						AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[story - 6])
								.setItems(R.array.DG_decision_07_01, new DialogInterface.OnClickListener() {
											
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										tempDecision = which;
										decisions = res.getStringArray(R.array.DG_decision_07_01);
										TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
										ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
										ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
										TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
										if(tempDecision == 0){
											analyticalPoints -= 2;
										}
										else if(tempDecision == 1){
											analyticalPoints -= 0;
										}
										else if(tempDecision == 2){
											analyticalPoints -= 2;
										}
										nameBox.clearBuffer();
										nameBox.setText(charName);
										nameBox.setTypeface(null, Typeface.BOLD);
										nameBox.setGravity(Gravity.LEFT);
										avatarLeft.startAnimation(fadeOut);
										avatarRight.startAnimation(fadeOut);
										setCharVisibility(true, false, true);
										if(gender.equals("Male")){
											avatarLeft.setImageResource(R.drawable.dgmalehero102);
											avatarRight.setImageResource(R.drawable.dgkaorukail101);
										}
										else if(gender.equals("Female")){
											avatarLeft.setImageResource(R.drawable.dgfemalehero102);
											avatarRight.setImageResource(R.drawable.dgmikaohgami101);
										}
										avatarRight.setColorFilter(R.color.TransBlack);
										avatarLeft.clearColorFilter();
										avatarLeft.startAnimation(fadeIn);
										avatarRight.startAnimation(fadeIn);
										if(textSpeed == 0){
											textBox.clearBuffer();
											textBox.setText(decisions[tempDecision]);
										}
										else{
											textBox.setCharacterDelay(textSpeed);
											textBox.animateText(decisions[tempDecision]);
										}
										inputMode = 1;
										savePrefInt("InputMode", inputMode);
										savePrefInt("Branch", branch);
										saveStory(story, plot, chapter);
										story = 15;
										inputMode = 0;
										}
									}
								);
								if(inputMode == 0){
									AlertDialog decision = builder.create();
									decision.show();
								}
								else{
									decisions = res.getStringArray(R.array.DG_decision_07_01);
									nameBox.clearBuffer();
									nameBox.setText(charName);
									nameBox.setTypeface(null, Typeface.BOLD);
									nameBox.setGravity(Gravity.LEFT);
									avatarLeft.startAnimation(fadeOut);
									avatarRight.startAnimation(fadeOut);
									setCharVisibility(true, false, true);
									if(gender.equals("Male")){
										avatarLeft.setImageResource(R.drawable.dgmalehero102);
										avatarRight.setImageResource(R.drawable.dgkaorukail101);
									}
									else if(gender.equals("Female")){
										avatarLeft.setImageResource(R.drawable.dgfemalehero102);
										avatarRight.setImageResource(R.drawable.dgmikaohgami101);
									}
									avatarRight.setColorFilter(R.color.TransBlack);
									avatarLeft.clearColorFilter();
									avatarLeft.startAnimation(fadeIn);
									avatarRight.startAnimation(fadeIn);
									if(textSpeed == 0){
										textBox.clearBuffer();
										textBox.setText(decisions[tempDecision]);
									}
									else{
										textBox.setCharacterDelay(textSpeed);
										textBox.animateText(decisions[tempDecision]);
									}
									saveStory(story, plot, chapter);
									story = 15;
								}
					}
					else if(branch == 1){
						AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[story - 6])
								.setItems(R.array.DG_decision_07_02, new DialogInterface.OnClickListener() {
											
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										tempDecision = which;
										decisions = res.getStringArray(R.array.DG_decision_07_02);
										TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
										ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
										ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
										TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
										if(tempDecision == 0){
											analyticalPoints -= 0;
										}
										else if(tempDecision == 1){
											analyticalPoints -= 2;
										}
										else if(tempDecision == 2){
											analyticalPoints -= 2;
										}
										nameBox.clearBuffer();
										nameBox.setText(charName);
										nameBox.setTypeface(null, Typeface.BOLD);
										nameBox.setGravity(Gravity.LEFT);
										avatarLeft.startAnimation(fadeOut);
										avatarRight.startAnimation(fadeOut);
										setCharVisibility(true, false, true);
										if(gender.equals("Male")){
											avatarLeft.setImageResource(R.drawable.dgmalehero102);
											avatarRight.setImageResource(R.drawable.dgkaorukail101);
										}
										else if(gender.equals("Female")){
											avatarLeft.setImageResource(R.drawable.dgfemalehero102);
											avatarRight.setImageResource(R.drawable.dgmikaohgami101);
										}
										avatarRight.setColorFilter(R.color.TransBlack);
										avatarLeft.clearColorFilter();
										avatarLeft.startAnimation(fadeIn);
										avatarRight.startAnimation(fadeIn);
										if(textSpeed == 0){
											textBox.clearBuffer();
											textBox.setText(decisions[tempDecision]);
										}
										else{
											textBox.setCharacterDelay(textSpeed);
											textBox.animateText(decisions[tempDecision]);
										}
										inputMode = 1;
										savePrefInt("InputMode", inputMode);
										savePrefInt("Branch", branch);
										saveStory(story, plot, chapter);
										story = 15;
										inputMode = 0;
										}
									}
								);
								if(inputMode == 0){
									AlertDialog decision = builder.create();
									decision.show();
								}
								else{
									decisions = res.getStringArray(R.array.DG_decision_07_02);
									nameBox.clearBuffer();
									nameBox.setText(charName);
									nameBox.setTypeface(null, Typeface.BOLD);
									nameBox.setGravity(Gravity.LEFT);
									avatarLeft.startAnimation(fadeOut);
									avatarRight.startAnimation(fadeOut);
									setCharVisibility(true, false, true);
									if(gender.equals("Male")){
										avatarLeft.setImageResource(R.drawable.dgmalehero102);
										avatarRight.setImageResource(R.drawable.dgkaorukail101);
									}
									else if(gender.equals("Female")){
										avatarLeft.setImageResource(R.drawable.dgfemalehero102);
										avatarRight.setImageResource(R.drawable.dgmikaohgami101);
									}
									avatarRight.setColorFilter(R.color.TransBlack);
									avatarLeft.clearColorFilter();
									avatarLeft.startAnimation(fadeIn);
									avatarRight.startAnimation(fadeIn);
									if(textSpeed == 0){
										textBox.clearBuffer();
										textBox.setText(decisions[tempDecision]);
									}
									else{
										textBox.setCharacterDelay(textSpeed);
										textBox.animateText(decisions[tempDecision]);
									}
									saveStory(story, plot, chapter);
									story = 15;
								}
					}
					else if(branch == 2){
						AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[story - 6])
								.setItems(R.array.DG_decision_07_03, new DialogInterface.OnClickListener() {
											
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										tempDecision = which;
										decisions = res.getStringArray(R.array.DG_decision_07_03);
										TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
										ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
										ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
										TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
										if(tempDecision == 0){
											analyticalPoints -= 0;
										}
										else if(tempDecision == 1){
											analyticalPoints -= 2;
										}
										else if(tempDecision == 2){
											analyticalPoints -= 2;
										}
										nameBox.clearBuffer();
										nameBox.setText(charName);
										nameBox.setTypeface(null, Typeface.BOLD);
										nameBox.setGravity(Gravity.LEFT);
										avatarLeft.startAnimation(fadeOut);
										avatarRight.startAnimation(fadeOut);
										setCharVisibility(true, false, true);
										if(gender.equals("Male")){
											avatarLeft.setImageResource(R.drawable.dgmalehero102);
											avatarRight.setImageResource(R.drawable.dgkaorukail101);
										}
										else if(gender.equals("Female")){
											avatarLeft.setImageResource(R.drawable.dgfemalehero102);
											avatarRight.setImageResource(R.drawable.dgmikaohgami101);
										}
										avatarRight.setColorFilter(R.color.TransBlack);
										avatarLeft.clearColorFilter();
										avatarLeft.startAnimation(fadeIn);
										avatarRight.startAnimation(fadeIn);
										if(textSpeed == 0){
											textBox.clearBuffer();
											textBox.setText(decisions[tempDecision]);
										}
										else{
											textBox.setCharacterDelay(textSpeed);
											textBox.animateText(decisions[tempDecision]);
										}
										inputMode = 1;
										savePrefInt("InputMode", inputMode);
										savePrefInt("Branch", branch);
										saveStory(story, plot, chapter);
										story = 15;
										inputMode = 0;
										}
									}
								);
								if(inputMode == 0){
									AlertDialog decision = builder.create();
									decision.show();
								}
								else{
									decisions = res.getStringArray(R.array.DG_decision_07_03);
									nameBox.clearBuffer();
									nameBox.setText(charName);
									nameBox.setTypeface(null, Typeface.BOLD);
									nameBox.setGravity(Gravity.LEFT);
									avatarLeft.startAnimation(fadeOut);
									avatarRight.startAnimation(fadeOut);
									setCharVisibility(true, false, true);
									if(gender.equals("Male")){
										avatarLeft.setImageResource(R.drawable.dgmalehero102);
										avatarRight.setImageResource(R.drawable.dgkaorukail101);
									}
									else if(gender.equals("Female")){
										avatarLeft.setImageResource(R.drawable.dgfemalehero102);
										avatarRight.setImageResource(R.drawable.dgmikaohgami101);
									}
									avatarRight.setColorFilter(R.color.TransBlack);
									avatarLeft.clearColorFilter();
									avatarLeft.startAnimation(fadeIn);
									avatarRight.startAnimation(fadeIn);
									if(textSpeed == 0){
										textBox.clearBuffer();
										textBox.setText(decisions[tempDecision]);
									}
									else{
										textBox.setCharacterDelay(textSpeed);
										textBox.animateText(decisions[tempDecision]);
									}
									saveStory(story, plot, chapter);
									story = 15;
								}
					}
				}
				else if(story == 15){
					savePrefInt("InputMode", inputMode);
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText(friendName1);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_rooftop_day);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
						avatarRight.setImageResource(R.drawable.dgkaorukail102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						avatarRight.setImageResource(R.drawable.dgmikaohgami102);
					}
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarRight.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					if(branch == 0){
						AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[story - 6])
								.setItems(R.array.DG_decision_08_01, new DialogInterface.OnClickListener() {
											
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										tempDecision = which;
										decisions = res.getStringArray(R.array.DG_decision_08_01);
										TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
										ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
										ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
										TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
										if(tempDecision == 0){
											analyticalPoints -= 2;
										}
										else if(tempDecision == 1){
											analyticalPoints -= 0;
										}
										else if(tempDecision == 2){
											analyticalPoints -= 2;
										}
										nameBox.clearBuffer();
										nameBox.setText(charName);
										nameBox.setTypeface(null, Typeface.BOLD);
										nameBox.setGravity(Gravity.LEFT);
										avatarLeft.startAnimation(fadeOut);
										avatarRight.startAnimation(fadeOut);
										setCharVisibility(true, false, true);
										if(gender.equals("Male")){
											avatarLeft.setImageResource(R.drawable.dgmalehero102);
											avatarRight.setImageResource(R.drawable.dgkaorukail101);
										}
										else if(gender.equals("Female")){
											avatarLeft.setImageResource(R.drawable.dgfemalehero102);
											avatarRight.setImageResource(R.drawable.dgmikaohgami101);
										}
										avatarRight.setColorFilter(R.color.TransBlack);
										avatarLeft.clearColorFilter();
										avatarLeft.startAnimation(fadeIn);
										avatarRight.startAnimation(fadeIn);
										if(textSpeed == 0){
											textBox.clearBuffer();
											textBox.setText(decisions[tempDecision]);
										}
										else{
											textBox.setCharacterDelay(textSpeed);
											textBox.animateText(decisions[tempDecision]);
										}
										inputMode = 1;
										savePrefInt("InputMode", inputMode);
										savePrefInt("Branch", branch);
										saveStory(story, plot, chapter);
										story = 16;
										inputMode = 0;
										}
									}
								);
								if(inputMode == 0){
									AlertDialog decision = builder.create();
									decision.show();
								}
								else{
									decisions = res.getStringArray(R.array.DG_decision_08_01);
									nameBox.clearBuffer();
									nameBox.setText(charName);
									nameBox.setTypeface(null, Typeface.BOLD);
									nameBox.setGravity(Gravity.LEFT);
									avatarLeft.startAnimation(fadeOut);
									avatarRight.startAnimation(fadeOut);
									setCharVisibility(true, false, true);
									if(gender.equals("Male")){
										avatarLeft.setImageResource(R.drawable.dgmalehero102);
										avatarRight.setImageResource(R.drawable.dgkaorukail101);
									}
									else if(gender.equals("Female")){
										avatarLeft.setImageResource(R.drawable.dgfemalehero102);
										avatarRight.setImageResource(R.drawable.dgmikaohgami101);
									}
									avatarRight.setColorFilter(R.color.TransBlack);
									avatarLeft.clearColorFilter();
									avatarLeft.startAnimation(fadeIn);
									avatarRight.startAnimation(fadeIn);
									if(textSpeed == 0){
										textBox.clearBuffer();
										textBox.setText(decisions[tempDecision]);
									}
									else{
										textBox.setCharacterDelay(textSpeed);
										textBox.animateText(decisions[tempDecision]);
									}
									saveStory(story, plot, chapter);
									story = 16;
								}
					}
					else if(branch == 1){
						AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[story - 6])
								.setItems(R.array.DG_decision_08_02, new DialogInterface.OnClickListener() {
											
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										tempDecision = which;
										decisions = res.getStringArray(R.array.DG_decision_08_02);
										TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
										ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
										ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
										TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
										if(tempDecision == 0){
											analyticalPoints -= 0;
										}
										else if(tempDecision == 1){
											analyticalPoints -= 2;
										}
										else if(tempDecision == 2){
											analyticalPoints -= 2;
										}
										nameBox.clearBuffer();
										nameBox.setText(charName);
										nameBox.setTypeface(null, Typeface.BOLD);
										nameBox.setGravity(Gravity.LEFT);
										avatarLeft.startAnimation(fadeOut);
										avatarRight.startAnimation(fadeOut);
										setCharVisibility(true, false, true);
										if(gender.equals("Male")){
											avatarLeft.setImageResource(R.drawable.dgmalehero102);
											avatarRight.setImageResource(R.drawable.dgkaorukail101);
										}
										else if(gender.equals("Female")){
											avatarLeft.setImageResource(R.drawable.dgfemalehero102);
											avatarRight.setImageResource(R.drawable.dgmikaohgami101);
										}
										avatarRight.setColorFilter(R.color.TransBlack);
										avatarLeft.clearColorFilter();
										avatarLeft.startAnimation(fadeIn);
										avatarRight.startAnimation(fadeIn);
										if(textSpeed == 0){
											textBox.clearBuffer();
											textBox.setText(decisions[tempDecision]);
										}
										else{
											textBox.setCharacterDelay(textSpeed);
											textBox.animateText(decisions[tempDecision]);
										}
										inputMode = 1;
										savePrefInt("InputMode", inputMode);
										savePrefInt("Branch", branch);
										saveStory(story, plot, chapter);
										story = 16;
										inputMode = 0;
										}
									}
								);
								if(inputMode == 0){
									AlertDialog decision = builder.create();
									decision.show();
								}
								else{
									decisions = res.getStringArray(R.array.DG_decision_08_02);
									nameBox.clearBuffer();
									nameBox.setText(charName);
									nameBox.setTypeface(null, Typeface.BOLD);
									nameBox.setGravity(Gravity.LEFT);
									avatarLeft.startAnimation(fadeOut);
									avatarRight.startAnimation(fadeOut);
									setCharVisibility(true, false, true);
									if(gender.equals("Male")){
										avatarLeft.setImageResource(R.drawable.dgmalehero102);
										avatarRight.setImageResource(R.drawable.dgkaorukail101);
									}
									else if(gender.equals("Female")){
										avatarLeft.setImageResource(R.drawable.dgfemalehero102);
										avatarRight.setImageResource(R.drawable.dgmikaohgami101);
									}
									avatarRight.setColorFilter(R.color.TransBlack);
									avatarLeft.clearColorFilter();
									avatarLeft.startAnimation(fadeIn);
									avatarRight.startAnimation(fadeIn);
									if(textSpeed == 0){
										textBox.clearBuffer();
										textBox.setText(decisions[tempDecision]);
									}
									else{
										textBox.setCharacterDelay(textSpeed);
										textBox.animateText(decisions[tempDecision]);
									}
									saveStory(story, plot, chapter);
									story = 16;
								}
					}
					else if(branch == 2){
						AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[story - 6])
								.setItems(R.array.DG_decision_08_03, new DialogInterface.OnClickListener() {
											
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										tempDecision = which;
										decisions = res.getStringArray(R.array.DG_decision_08_03);
										TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
										ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
										ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
										TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
										if(tempDecision == 0){
											analyticalPoints -= 0;
										}
										else if(tempDecision == 1){
											analyticalPoints -= 2;
										}
										else if(tempDecision == 2){
											analyticalPoints -= 2;
										}
										nameBox.clearBuffer();
										nameBox.setText(charName);
										nameBox.setTypeface(null, Typeface.BOLD);
										nameBox.setGravity(Gravity.LEFT);
										avatarLeft.startAnimation(fadeOut);
										avatarRight.startAnimation(fadeOut);
										setCharVisibility(true, false, true);
										if(gender.equals("Male")){
											avatarLeft.setImageResource(R.drawable.dgmalehero102);
											avatarRight.setImageResource(R.drawable.dgkaorukail101);
										}
										else if(gender.equals("Female")){
											avatarLeft.setImageResource(R.drawable.dgfemalehero102);
											avatarRight.setImageResource(R.drawable.dgmikaohgami101);
										}
										avatarRight.setColorFilter(R.color.TransBlack);
										avatarLeft.clearColorFilter();
										avatarLeft.startAnimation(fadeIn);
										avatarRight.startAnimation(fadeIn);
										if(textSpeed == 0){
											textBox.clearBuffer();
											textBox.setText(decisions[tempDecision]);
										}
										else{
											textBox.setCharacterDelay(textSpeed);
											textBox.animateText(decisions[tempDecision]);
										}
										inputMode = 1;
										savePrefInt("InputMode", inputMode);
										savePrefInt("Branch", branch);
										saveStory(story, plot, chapter);
										story = 16;
										inputMode = 0;
										}
									}
								);
								if(inputMode == 0){
									AlertDialog decision = builder.create();
									decision.show();
								}
								else{
									decisions = res.getStringArray(R.array.DG_decision_08_03);
									nameBox.clearBuffer();
									nameBox.setText(charName);
									nameBox.setTypeface(null, Typeface.BOLD);
									nameBox.setGravity(Gravity.LEFT);
									avatarLeft.startAnimation(fadeOut);
									avatarRight.startAnimation(fadeOut);
									setCharVisibility(true, false, true);
									if(gender.equals("Male")){
										avatarLeft.setImageResource(R.drawable.dgmalehero102);
										avatarRight.setImageResource(R.drawable.dgkaorukail101);
									}
									else if(gender.equals("Female")){
										avatarLeft.setImageResource(R.drawable.dgfemalehero102);
										avatarRight.setImageResource(R.drawable.dgmikaohgami101);
									}
									avatarRight.setColorFilter(R.color.TransBlack);
									avatarLeft.clearColorFilter();
									avatarLeft.startAnimation(fadeIn);
									avatarRight.startAnimation(fadeIn);
									if(textSpeed == 0){
										textBox.clearBuffer();
										textBox.setText(decisions[tempDecision]);
									}
									else{
										textBox.setCharacterDelay(textSpeed);
										textBox.animateText(decisions[tempDecision]);
									}
									saveStory(story, plot, chapter);
									story = 16;
								}
					}
				}
				else if(story == 16){
					savePrefInt("InputMode", inputMode);
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText(friendName1);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_rooftop_day);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
						avatarRight.setImageResource(R.drawable.dgkaorukail102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						avatarRight.setImageResource(R.drawable.dgmikaohgami102);
					}
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarRight.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					if(branch == 0){
						AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[story - 6])
								.setItems(R.array.DG_decision_09_01, new DialogInterface.OnClickListener() {
											
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										tempDecision = which;
										decisions = res.getStringArray(R.array.DG_decision_09_01);
										TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
										ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
										ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
										TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
										if(tempDecision == 0){
											analyticalPoints -= 2;
										}
										else if(tempDecision == 1){
											analyticalPoints -= 0;
										}
										else if(tempDecision == 2){
											analyticalPoints -= 2;
										}
										nameBox.clearBuffer();
										nameBox.setText(charName);
										nameBox.setTypeface(null, Typeface.BOLD);
										nameBox.setGravity(Gravity.LEFT);
										avatarLeft.startAnimation(fadeOut);
										avatarRight.startAnimation(fadeOut);
										setCharVisibility(true, false, true);
										if(gender.equals("Male")){
											avatarLeft.setImageResource(R.drawable.dgmalehero102);
											avatarRight.setImageResource(R.drawable.dgkaorukail101);
										}
										else if(gender.equals("Female")){
											avatarLeft.setImageResource(R.drawable.dgfemalehero102);
											avatarRight.setImageResource(R.drawable.dgmikaohgami101);
										}
										avatarRight.setColorFilter(R.color.TransBlack);
										avatarLeft.clearColorFilter();
										avatarLeft.startAnimation(fadeIn);
										avatarRight.startAnimation(fadeIn);
										if(textSpeed == 0){
											textBox.clearBuffer();
											textBox.setText(decisions[tempDecision]);
										}
										else{
											textBox.setCharacterDelay(textSpeed);
											textBox.animateText(decisions[tempDecision]);
										}
										inputMode = 1;
										savePrefInt("InputMode", inputMode);
										savePrefInt("Branch", branch);
										saveStory(story, plot, chapter);
										story = 17;
										dec01 = true;
										inputMode = 0;
										}
									}
								);
								if(inputMode == 0){
									AlertDialog decision = builder.create();
									decision.show();
								}
								else{
									decisions = res.getStringArray(R.array.DG_decision_09_01);
									nameBox.clearBuffer();
									nameBox.setText(charName);
									nameBox.setTypeface(null, Typeface.BOLD);
									nameBox.setGravity(Gravity.LEFT);
									avatarLeft.startAnimation(fadeOut);
									avatarRight.startAnimation(fadeOut);
									setCharVisibility(true, false, true);
									if(gender.equals("Male")){
										avatarLeft.setImageResource(R.drawable.dgmalehero102);
										avatarRight.setImageResource(R.drawable.dgkaorukail101);
									}
									else if(gender.equals("Female")){
										avatarLeft.setImageResource(R.drawable.dgfemalehero102);
										avatarRight.setImageResource(R.drawable.dgmikaohgami101);
									}
									avatarRight.setColorFilter(R.color.TransBlack);
									avatarLeft.clearColorFilter();
									avatarLeft.startAnimation(fadeIn);
									avatarRight.startAnimation(fadeIn);
									if(textSpeed == 0){
										textBox.clearBuffer();
										textBox.setText(decisions[tempDecision]);
									}
									else{
										textBox.setCharacterDelay(textSpeed);
										textBox.animateText(decisions[tempDecision]);
									}
									saveStory(story, plot, chapter);
									story = 17;
									dec01 = true;
								}
					}
					else if(branch == 1){
						AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[story - 6])
								.setItems(R.array.DG_decision_09_02, new DialogInterface.OnClickListener() {
											
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										tempDecision = which;
										decisions = res.getStringArray(R.array.DG_decision_09_02);
										TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
										ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
										ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
										TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
										if(tempDecision == 0){
											analyticalPoints -= 0;
										}
										else if(tempDecision == 1){
											analyticalPoints -= 2;
										}
										else if(tempDecision == 2){
											analyticalPoints -= 2;
										}
										nameBox.clearBuffer();
										nameBox.setText(charName);
										nameBox.setTypeface(null, Typeface.BOLD);
										nameBox.setGravity(Gravity.LEFT);
										avatarLeft.startAnimation(fadeOut);
										avatarRight.startAnimation(fadeOut);
										setCharVisibility(true, false, true);
										if(gender.equals("Male")){
											avatarLeft.setImageResource(R.drawable.dgmalehero102);
											avatarRight.setImageResource(R.drawable.dgkaorukail101);
										}
										else if(gender.equals("Female")){
											avatarLeft.setImageResource(R.drawable.dgfemalehero102);
											avatarRight.setImageResource(R.drawable.dgmikaohgami101);
										}
										avatarRight.setColorFilter(R.color.TransBlack);
										avatarLeft.clearColorFilter();
										avatarLeft.startAnimation(fadeIn);
										avatarRight.startAnimation(fadeIn);
										if(textSpeed == 0){
											textBox.clearBuffer();
											textBox.setText(decisions[tempDecision]);
										}
										else{
											textBox.setCharacterDelay(textSpeed);
											textBox.animateText(decisions[tempDecision]);
										}
										inputMode = 1;
										savePrefInt("InputMode", inputMode);
										savePrefInt("Branch", branch);
										saveStory(story, plot, chapter);
										story = 17;
										inputMode = 0;
										dec02 = true;
										}
									}
								);
								if(inputMode == 0){
									AlertDialog decision = builder.create();
									decision.show();
								}
								else{
									decisions = res.getStringArray(R.array.DG_decision_09_02);
									nameBox.clearBuffer();
									nameBox.setText(charName);
									nameBox.setTypeface(null, Typeface.BOLD);
									nameBox.setGravity(Gravity.LEFT);
									avatarLeft.startAnimation(fadeOut);
									avatarRight.startAnimation(fadeOut);
									setCharVisibility(true, false, true);
									if(gender.equals("Male")){
										avatarLeft.setImageResource(R.drawable.dgmalehero102);
										avatarRight.setImageResource(R.drawable.dgkaorukail101);
									}
									else if(gender.equals("Female")){
										avatarLeft.setImageResource(R.drawable.dgfemalehero102);
										avatarRight.setImageResource(R.drawable.dgmikaohgami101);
									}
									avatarRight.setColorFilter(R.color.TransBlack);
									avatarLeft.clearColorFilter();
									avatarLeft.startAnimation(fadeIn);
									avatarRight.startAnimation(fadeIn);
									if(textSpeed == 0){
										textBox.clearBuffer();
										textBox.setText(decisions[tempDecision]);
									}
									else{
										textBox.setCharacterDelay(textSpeed);
										textBox.animateText(decisions[tempDecision]);
									}
									saveStory(story, plot, chapter);
									story = 17;
									dec02 = true;
								}
					}
					else if(branch == 2){
						AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[story - 6])
								.setItems(R.array.DG_decision_09_03, new DialogInterface.OnClickListener() {
											
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										tempDecision = which;
										decisions = res.getStringArray(R.array.DG_decision_09_03);
										TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
										ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
										ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
										TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
										if(tempDecision == 0){
											analyticalPoints -= 0;
										}
										else if(tempDecision == 1){
											analyticalPoints -= 2;
										}
										else if(tempDecision == 2){
											analyticalPoints -= 2;
										}
										nameBox.clearBuffer();
										nameBox.setText(charName);
										nameBox.setTypeface(null, Typeface.BOLD);
										nameBox.setGravity(Gravity.LEFT);
										avatarLeft.startAnimation(fadeOut);
										avatarRight.startAnimation(fadeOut);
										setCharVisibility(true, false, true);
										if(gender.equals("Male")){
											avatarLeft.setImageResource(R.drawable.dgmalehero102);
											avatarRight.setImageResource(R.drawable.dgkaorukail101);
										}
										else if(gender.equals("Female")){
											avatarLeft.setImageResource(R.drawable.dgfemalehero102);
											avatarRight.setImageResource(R.drawable.dgmikaohgami101);
										}
										avatarRight.setColorFilter(R.color.TransBlack);
										avatarLeft.clearColorFilter();
										avatarLeft.startAnimation(fadeIn);
										avatarRight.startAnimation(fadeIn);
										if(textSpeed == 0){
											textBox.clearBuffer();
											textBox.setText(decisions[tempDecision]);
										}
										else{
											textBox.setCharacterDelay(textSpeed);
											textBox.animateText(decisions[tempDecision]);
										}
										inputMode = 1;
										savePrefInt("InputMode", inputMode);
										savePrefInt("Branch", branch);
										saveStory(story, plot, chapter);
										story = 17;
										inputMode = 0;
										}
									}
								);
								if(inputMode == 0){
									AlertDialog decision = builder.create();
									decision.show();
								}
								else{
									decisions = res.getStringArray(R.array.DG_decision_09_03);
									nameBox.clearBuffer();
									nameBox.setText(charName);
									nameBox.setTypeface(null, Typeface.BOLD);
									nameBox.setGravity(Gravity.LEFT);
									avatarLeft.startAnimation(fadeOut);
									avatarRight.startAnimation(fadeOut);
									setCharVisibility(true, false, true);
									if(gender.equals("Male")){
										avatarLeft.setImageResource(R.drawable.dgmalehero102);
										avatarRight.setImageResource(R.drawable.dgkaorukail101);
									}
									else if(gender.equals("Female")){
										avatarLeft.setImageResource(R.drawable.dgfemalehero102);
										avatarRight.setImageResource(R.drawable.dgmikaohgami101);
									}
									avatarRight.setColorFilter(R.color.TransBlack);
									avatarLeft.clearColorFilter();
									avatarLeft.startAnimation(fadeIn);
									avatarRight.startAnimation(fadeIn);
									if(textSpeed == 0){
										textBox.clearBuffer();
										textBox.setText(decisions[tempDecision]);
									}
									else{
										textBox.setCharacterDelay(textSpeed);
										textBox.animateText(decisions[tempDecision]);
									}
									saveStory(story, plot, chapter);
									story = 17;
								}
					}
				}
				else if(story == 17){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText(friendName1);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(false, true, false);
					currentView.setBackgroundResource(R.drawable.dg_school_rooftop_day);
					if(gender.equals("Male")){
						avatarCenter.setImageResource(R.drawable.dgkaorukail101);
					}
					else if(gender.equals("Female")){
						avatarCenter.setImageResource(R.drawable.dgmikaohgami101);
					}
					avatarCenter.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 18;
				}
				else if(story == 18){
					stories = res.getStringArray(R.array.DG_Story_07);
					textBox.animateText("");
					nameBox.setTypeface(null, Typeface.NORMAL);
					nameBox.setGravity(Gravity.LEFT);
					avatarCenter.startAnimation(fadeOut);
					setCharVisibility(false, false, false);
					currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[story - 6]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 19;
				}
				else if(story == 19){
					stories = res.getStringArray(R.array.DG_Story_07);
					textBox.animateText("");
					nameBox.setTypeface(null, Typeface.NORMAL);
					nameBox.setGravity(Gravity.LEFT);
					setCharVisibility(false, false, false);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[story - 6]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 20;
				}
				else if(story == 20){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText(charName);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					setCharVisibility(true, false, true);
					avatarRight.setImageResource(R.drawable.dgheadmaster01);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarRight.setColorFilter(R.color.TransBlack);
					avatarLeft.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 21;
				}	
				else if(story == 21){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText("Headmaster");
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					avatarRight.setImageResource(R.drawable.dgheadmaster02);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
					}
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarRight.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 22;
				}	
				else if(story == 22){
					stories = res.getStringArray(R.array.DG_Story_07);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					nameBox.clearBuffer();
					nameBox.setText(friendName2);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					avatarRight.setImageResource(R.drawable.dgheadmaster01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgrenaohgami102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgkaorukarl102);
					}
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					avatarRight.setColorFilter(R.color.TransBlack);
					avatarLeft.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					saveStory(story, plot, chapter);
					story = 23;
				}	
				else if(story == 23){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText("Headmaster");
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					avatarRight.setImageResource(R.drawable.dgheadmaster02);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgrenaohgami101);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgkaorukarl101);
					}
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarRight.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 24;
				}	
				else if(story == 24){
					stories = res.getStringArray(R.array.DG_Story_07);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					nameBox.clearBuffer();
					nameBox.setText(friendName2);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					avatarRight.setImageResource(R.drawable.dgheadmaster01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgrenaohgami102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgkaorukarl102);
					}
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					avatarRight.setColorFilter(R.color.TransBlack);
					avatarLeft.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					saveStory(story, plot, chapter);
					story = 25;
				}	
				else if(story == 25){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.setText("Headmaster");
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					avatarRight.setImageResource(R.drawable.dgheadmaster02);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgrenaohgami101);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgkaorukarl101);
					}
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarRight.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 26;
				}
				else if(story == 26){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText(charName);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					avatarCenter.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					avatarRight.setImageResource(R.drawable.dgheadmaster01);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarRight.setColorFilter(R.color.TransBlack);
					avatarLeft.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 27;
				}	
				else if(story == 27){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText("Headmaster");
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					avatarRight.setImageResource(R.drawable.dgheadmaster02);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
					}
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarRight.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 28;
				}
				else if(story == 28){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText(charName);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					avatarCenter.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					avatarRight.setImageResource(R.drawable.dgheadmaster01);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarRight.setColorFilter(R.color.TransBlack);
					avatarLeft.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 29;
				}	
				else if(story == 29){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText("Headmaster");
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					avatarRight.setImageResource(R.drawable.dgheadmaster02);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
					}
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarRight.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 30;
				}
				else if(story == 30){
					stories = res.getStringArray(R.array.DG_Story_07);
					textBox.animateText("");
					nameBox.setTypeface(null, Typeface.NORMAL);
					nameBox.setGravity(Gravity.LEFT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					avatarCenter.setImageResource(R.drawable.dgjanitor01);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					avatarCenter.startAnimation(fadeIn);
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[story - 6]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 31;
				}
				else if(story == 31){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText("Janitor");
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					avatarCenter.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					avatarRight.setImageResource(R.drawable.dgheadmaster01);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					avatarLeft.setImageResource(R.drawable.dgjanitor02);
					avatarRight.setColorFilter(R.color.TransBlack);
					avatarLeft.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 32;
				}	
				else if(story == 32){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText("Headmaster");
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					avatarRight.setImageResource(R.drawable.dgheadmaster02);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
					}
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarRight.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 33;
				}
				else if(story == 33){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText("Janitor");
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					avatarCenter.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					avatarRight.setImageResource(R.drawable.dgheadmaster01);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					avatarLeft.setImageResource(R.drawable.dgjanitor03);
					avatarRight.setColorFilter(R.color.TransBlack);
					avatarLeft.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 34;
				}	
				else if(story == 34){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText(friendName1);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarCenter.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					if(gender.equals("Male")){
						avatarRight.setImageResource(R.drawable.dgkaorukail102);
					}
					else if(gender.equals("Female")){
						avatarRight.setImageResource(R.drawable.dgmikaohgami102);
					}
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					avatarLeft.setImageResource(R.drawable.dgjanitor02);
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarRight.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 35;
				}	
				else if(story == 35){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText(friendName2);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarCenter.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					if(gender.equals("Male")){
						avatarRight.setImageResource(R.drawable.dgrenaohgami102);
					}
					else if(gender.equals("Female")){
						avatarRight.setImageResource(R.drawable.dgkaorukarl102);
					}
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					avatarLeft.setImageResource(R.drawable.dgjanitor02);
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarRight.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 36;
				}
				else if(story == 36){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText("Headmaster");
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					avatarLeft.setImageResource(R.drawable.dgheadmaster02);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					if(gender.equals("Male")){
						avatarRight.setImageResource(R.drawable.dgrenaohgami101);
					}
					else if(gender.equals("Female")){
						avatarRight.setImageResource(R.drawable.dgkaorukarl101);
					}
					avatarRight.setColorFilter(R.color.TransBlack);
					avatarLeft.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 37;
				}
				else if(story == 37){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText("Janitor");
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					avatarLeft.setImageResource(R.drawable.dgheadmaster01);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					avatarRight.setImageResource(R.drawable.dgjanitor03);
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarRight.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 38;
				}
				else if(story == 38){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText(charName);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					avatarRight.setImageResource(R.drawable.dgjanitor04);
					avatarRight.setColorFilter(R.color.TransBlack);
					avatarLeft.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 39;
				}
				else if(story == 39){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText("Janitor");
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
					}
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					avatarRight.setImageResource(R.drawable.dgjanitor02);
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarRight.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 40;
				}
				else if(story == 40){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText(charName);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					avatarRight.setImageResource(R.drawable.dgjanitor06);
					avatarRight.setColorFilter(R.color.TransBlack);
					avatarLeft.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 41;
				}
				else if(story == 41){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText(charName);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero106);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero106);
					}
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					avatarRight.setImageResource(R.drawable.dgjanitor06);
					avatarRight.setColorFilter(R.color.TransBlack);
					avatarLeft.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 42;
				}
				else if(story == 42){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText("Janitor");
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
					}
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					avatarRight.setImageResource(R.drawable.dgjanitor02);
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarRight.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 43;
				}
				else if(story == 43){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText(friendName1);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgkaorukail102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgmikaohgami102);
					}
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					avatarRight.setImageResource(R.drawable.dgjanitor06);
					avatarRight.setColorFilter(R.color.TransBlack);
					avatarLeft.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 44;
				}
				else if(story == 44){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText(friendName2);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgrenaohgami102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgkaorukarl102);
					}
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					avatarRight.setImageResource(R.drawable.dgjanitor06);
					avatarRight.setColorFilter(R.color.TransBlack);
					avatarLeft.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 45;
				}
				else if(story == 45){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText("Janitor");
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgrenaohgami105);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgkaorukarl105);
					}
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					avatarRight.setImageResource(R.drawable.dgjanitor03);
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarRight.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 46;
				}
				else if(story == 46){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText("Headmaster");
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					avatarLeft.setImageResource(R.drawable.dgheadmaster04);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					avatarRight.setImageResource(R.drawable.dgjanitor06);
					avatarRight.setColorFilter(R.color.TransBlack);
					avatarLeft.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 47;
				}
				else if(story == 47){
					stories = res.getStringArray(R.array.DG_Story_07);
					textBox.animateText("");
					nameBox.setTypeface(null, Typeface.NORMAL);
					nameBox.setGravity(Gravity.LEFT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(false, true, false);
					avatarCenter.setImageResource(R.drawable.dgakiyama03);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					avatarCenter.startAnimation(fadeIn);
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[story - 6]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 48;
				}
				else if(story == 48){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText("Akiyama");
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					avatarCenter.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					avatarLeft.setImageResource(R.drawable.dgakiyama07);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					avatarRight.setImageResource(R.drawable.dgjanitor06);
					avatarRight.setColorFilter(R.color.TransBlack);
					avatarLeft.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 49;
				}
				else if(story == 49){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText("Janitor");
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					avatarLeft.setImageResource(R.drawable.dgakiyama03);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					avatarRight.setImageResource(R.drawable.dgjanitor02);
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarRight.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 50;
				}
				else if(story == 50){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText("Akiyama");
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					avatarLeft.setImageResource(R.drawable.dgakiyama06);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					avatarRight.setImageResource(R.drawable.dgjanitor06);
					avatarRight.setColorFilter(R.color.TransBlack);
					avatarLeft.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 51;
				}
				else if(story == 51){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText("Janitor");
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					avatarLeft.setImageResource(R.drawable.dgakiyama03);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					avatarRight.setImageResource(R.drawable.dgjanitor02);
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarRight.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 52;
				}
				else if(story == 52){
					stories = res.getStringArray(R.array.DG_Story_07);
					textBox.animateText("");
					nameBox.setTypeface(null, Typeface.NORMAL);
					nameBox.setGravity(Gravity.LEFT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(false, true, false);
					avatarCenter.setImageResource(R.drawable.dgheadmaster05);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					avatarCenter.startAnimation(fadeIn);
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[story - 6]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 53;
				}
				else if(story == 53){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText("Headmaster");
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					avatarCenter.startAnimation(fadeOut);
					setCharVisibility(false, true, false);
					avatarCenter.setImageResource(R.drawable.dgheadmaster02);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					avatarCenter.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 54;
				}
				else if(story == 54){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText("Janitor");
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarCenter.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					avatarLeft.setImageResource(R.drawable.dgheadmaster01);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					avatarRight.setImageResource(R.drawable.dgjanitor05);
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarRight.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 55;
				}
				else if(story == 55){
					stories = res.getStringArray(R.array.DG_Story_07);
					textBox.animateText("");
					nameBox.setTypeface(null, Typeface.NORMAL);
					nameBox.setGravity(Gravity.LEFT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(false, false, false);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[story - 6]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 56;
				}
				else if(story == 56){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText("Headmaster");
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					setCharVisibility(true, false, true);
					avatarLeft.setImageResource(R.drawable.dgheadmaster02);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					if(gender.equals("Male")){
						avatarRight.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarRight.setImageResource(R.drawable.dgfemalehero101);
					}
					avatarRight.setColorFilter(R.color.TransBlack);
					avatarLeft.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 57;
				}
				else if(story == 57){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText(charName);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					avatarLeft.setImageResource(R.drawable.dgheadmaster01);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					if(gender.equals("Male")){
						avatarRight.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarRight.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarRight.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 58;
				}
				else if(story == 58){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText("Headmaster");
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					avatarLeft.setImageResource(R.drawable.dgheadmaster02);
					currentView.setBackgroundResource(R.drawable.dg_school_headmaster_office_day);
					if(gender.equals("Male")){
						avatarRight.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarRight.setImageResource(R.drawable.dgfemalehero101);
					}
					avatarRight.setColorFilter(R.color.TransBlack);
					avatarLeft.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 59;
				}
				else if(story == 59){
					stories = res.getStringArray(R.array.DG_Story_07);
					textBox.animateText("");
					nameBox.setTypeface(null, Typeface.NORMAL);
					nameBox.setGravity(Gravity.LEFT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(false, false, false);
					changeBackground(R.drawable.dg_school_corridor_day);
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[story - 6]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 60;
				}
				else if(story == 60){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText(friendName2);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
						avatarRight.setImageResource(R.drawable.dgrenaohgami102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						avatarRight.setImageResource(R.drawable.dgkaorukarl102);
					}
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarRight.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6].replace("char_nickname", charNickname));
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6].replace("char_nickname", charNickname));
					}
					saveStory(story, plot, chapter);
					story = 61;
				}
				else if(story == 61){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText(friendName1);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
						avatarRight.setImageResource(R.drawable.dgkaorukail102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						avatarRight.setImageResource(R.drawable.dgmikaohgami102);
					}
					avatarLeft.setColorFilter(R.color.TransBlack);
					avatarRight.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 62;
				}
				else if(story == 62){
					stories = res.getStringArray(R.array.DG_Story_07);
					nameBox.clearBuffer();
					nameBox.setText(charName);
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
						avatarRight.setImageResource(R.drawable.dgkaorukail101);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
						avatarRight.setImageResource(R.drawable.dgmikaohgami101);
					}
					avatarRight.setColorFilter(R.color.TransBlack);
					avatarLeft.clearColorFilter();
					avatarLeft.startAnimation(fadeIn);
					avatarRight.startAnimation(fadeIn);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[story - 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 63;
				}
				else if(story == 63){
					stories = res.getStringArray(R.array.DG_Story_07);
					textBox.animateText("");
					nameBox.setTypeface(null, Typeface.NORMAL);
					nameBox.setGravity(Gravity.LEFT);
					avatarLeft.startAnimation(fadeOut);
					avatarRight.startAnimation(fadeOut);
					setCharVisibility(false, false, false);
					currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[story - 6]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 64;
				}
				else if(story == 64){
					stories = res.getStringArray(R.array.DG_Story_07);
					textBox.animateText("");
					nameBox.setTypeface(null, Typeface.NORMAL);
					nameBox.setGravity(Gravity.LEFT);
					setCharVisibility(false, false, false);
					currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[story - 6]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[story - 6]);
					}
					saveStory(story, plot, chapter);
					story = 65;
				}
				else if(story == 65){
					result();
				}
			}
		}
	}
	public void setSituations2(){
		TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
		RelativeLayout currentView = (RelativeLayout) findViewById(R.id.backg);
		final Resources res = getResources();
		final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
		final Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
		ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
		ImageView avatarCenter = (ImageView) findViewById(R.id.avatarCenter);
		ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
		TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
		stories = res.getStringArray(R.array.DG_Story_06);
		if(story == 2){
			if(branch == 0){
				stories = res.getStringArray(R.array.DG_Story_Branch_01);
				if(branchStory == 0){
					disableEvent = true;
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					nameBox.clearBuffer();
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setTypeface(null, Typeface.NORMAL);
					textBox.animateText("");
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[branchStory]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[branchStory]);
					}
					setCharVisibility(true, false, true);
					avatarRight.setImageResource(R.drawable.dgteacher01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
					}
					avatarLeft.clearColorFilter();
					avatarRight.clearColorFilter();
					
					AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[branchStory])
					.setItems(R.array.DG_decision_branch_01, new DialogInterface.OnClickListener() {
								
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							decisions = res.getStringArray(R.array.DG_decision_branch_01_result);
							tempDecision = which;
							TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
							ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
							ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
							TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
							if(tempDecision == 0){
								avatarRight.setImageResource(R.drawable.dgteacher01);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero102);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero102);
								}
								socialPoints += 1;
							}
							else if(tempDecision == 1){
								avatarRight.setImageResource(R.drawable.dgteacher08);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero105);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero105);
								}
								socialPoints += 0;
							}
							avatarLeft.clearColorFilter();
							avatarRight.setColorFilter(R.color.TransBlack);
							nameBox.clearBuffer();
							nameBox.setTypeface(null, Typeface.BOLD);
							nameBox.setText(charName);
							if(textSpeed == 0){
								textBox.clearBuffer();
								textBox.setText(decisions[tempDecision]);
							}
							else{
								textBox.setCharacterDelay(textSpeed);
								textBox.animateText(decisions[tempDecision]);
							}
							savePrefInt("SocialPoints", socialPoints);
							inputMode = 1;
							savePrefInt("InputMode", inputMode);
							savePrefInt("TempDecision", tempDecision);
							savePrefInt("BranchStory", branchStory);
							branchStory = 1;
							}
						}
					);
					if(inputMode == 0){
						AlertDialog decision = builder.create();
						decision.show();
					}
					else{
						decisions = res.getStringArray(R.array.DG_decision_branch_01_result);
						if(tempDecision == 0){
							avatarRight.setImageResource(R.drawable.dgteacher01);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero102);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero102);
							}
						}
						else if(tempDecision == 1){
							avatarRight.setImageResource(R.drawable.dgteacher08);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero105);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero105);
							}
						}
						avatarLeft.clearColorFilter();
						avatarRight.setColorFilter(R.color.TransBlack);
						nameBox.clearBuffer();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setText(charName);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(decisions[tempDecision]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(decisions[tempDecision]);
						}
						savePrefInt("BranchStory", branchStory);
						branchStory = 1;
					}
					saveStory(story, plot, chapter);
				}
				else if(branchStory == 1){
					disableEvent = false;
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					decisions = res.getStringArray(R.array.DG_decision_branch_01_result);
					if(tempDecision == 0){
						avatarRight.setImageResource(R.drawable.dgteacher02);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero101);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						}
					}
					else if(tempDecision == 1){
						avatarRight.setImageResource(R.drawable.dgteacher03);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero111);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero111);
						}
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Mr. Harada");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(decisions[tempDecision + 2].replace("char_name", charName));
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(decisions[tempDecision + 2].replace("char_name", charName));
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 2;
				}
				else if(branchStory == 2){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					decisions = res.getStringArray(R.array.DG_decision_branch_01_result);
					if(tempDecision == 0){
						avatarRight.setImageResource(R.drawable.dgteacher01);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero102);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero102);
						}
					}
					else if(tempDecision == 1){
						avatarRight.setImageResource(R.drawable.dgteacher05);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero110);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero110);
						}
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(decisions[tempDecision + 4]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(decisions[tempDecision + 4]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 3;
				}
				else if(branchStory == 3){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					decisions = res.getStringArray(R.array.DG_decision_branch_01_result);
					if(tempDecision == 0){
						avatarRight.setImageResource(R.drawable.dgteacher02);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero101);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						}
					}
					else if(tempDecision == 1){
						avatarRight.setImageResource(R.drawable.dgteacher03);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero111);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero111);
						}
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Mr. Harada");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(decisions[tempDecision + 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(decisions[tempDecision + 6]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 4;
				}
				else if(branchStory == 4){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 5;
				}
				else if(branchStory == 5){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher04);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Mr. Harada");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 6;
				}
				else if(branchStory == 6){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher06);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Mr. Harada");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 7;
				}
				else if(branchStory == 7){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher08);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 8;
				}
				else if(branchStory == 8){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher07);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Mr. Harada");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 9;
				}
				else if(branchStory == 9){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher08);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 10;
				}
				else if(branchStory == 10){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher07);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Mr. Harada");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 11;
				}
				else if(branchStory == 11){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 12;
				}
				else if(branchStory == 12){
					setCharVisibility(false, true, false);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					if(gender.equals("Male")){
						avatarCenter.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarCenter.setImageResource(R.drawable.dgfemalehero101);
					}
					textBox.animateText("");
					nameBox.setTypeface(null, Typeface.NORMAL);
					nameBox.setGravity(Gravity.LEFT);
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[branchStory - 3]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 0;
					branch01 = true;
					story = 3;
					inputMode = 0;
					savePrefInt("InputMode", inputMode);
				}
			}
			else if(branch == 1){
				stories = res.getStringArray(R.array.DG_Story_Branch_02);
				if(branchStory == 0){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama03);
					avatarLeft.setImageResource(R.drawable.dgstudent101);
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText("Student 1");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 1;
					inputMode = 0;
					savePrefInt("InputMode", inputMode);
				}
				else if(branchStory == 1){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama03);
					avatarLeft.setImageResource(R.drawable.dgstudent201);
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText("Student 2");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 2;
				}
				else if(branchStory == 2){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama07);
					avatarLeft.setImageResource(R.drawable.dgstudent201);
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Akiyama");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 3;
				}
				else if(branchStory == 3){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama07);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarLeft.clearColorFilter();
					avatarRight.clearColorFilter();
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 4;
				}
				else if(branchStory == 4){
					disableEvent = true;
					setCharVisibility(false, true, false);
					currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					nameBox.clearBuffer();
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setTypeface(null, Typeface.NORMAL);
					textBox.animateText("");
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[branchStory]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[branchStory]);
					}
					if(gender.equals("Male")){
						avatarCenter.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarCenter.setImageResource(R.drawable.dgfemalehero101);
					}
					
					AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[branchStory])
					.setItems(R.array.DG_decision_branch_02_01, new DialogInterface.OnClickListener() {
								
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							decisions = res.getStringArray(R.array.DG_decision_branch_02_result_01);
							tempDecision = which;
							tempLoc = which;
							TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
							ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
							ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
							TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
							if(tempDecision == 0){
								avatarRight.setImageResource(R.drawable.dgakiyama05);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero105);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero105);
								}
								socialPoints += 1;
							}
							else if(tempDecision == 1){
								avatarRight.setImageResource(R.drawable.dgakiyama05);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero109);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero109);
								}
								socialPoints += 0;
							}
							avatarLeft.clearColorFilter();
							avatarRight.setColorFilter(R.color.TransBlack);
							nameBox.clearBuffer();
							nameBox.setTypeface(null, Typeface.BOLD);
							nameBox.setText(charName);
							if(textSpeed == 0){
								textBox.clearBuffer();
								textBox.setText(decisions[tempDecision]);
							}
							else{
								textBox.setCharacterDelay(textSpeed);
								textBox.animateText(decisions[tempDecision]);
							}
							savePrefInt("SocialPoints", socialPoints);
							inputMode = 1;
							savePrefInt("InputMode", inputMode);
							savePrefInt("TempDecision", tempDecision);
							savePrefInt("TempLoc", tempLoc);
							savePrefInt("BranchStory", branchStory);
							branchStory = 5;
							}
						}
					);
					if(inputMode == 0){
						AlertDialog decision = builder.create();
						decision.show();
					}
					else{
						decisions = res.getStringArray(R.array.DG_decision_branch_02_result_01);
						if(tempDecision == 0){
							avatarRight.setImageResource(R.drawable.dgakiyama05);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero105);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero105);
							}
						}
						else if(tempDecision == 1){
							avatarRight.setImageResource(R.drawable.dgakiyama05);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero109);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero109);
							}
						}
						avatarLeft.clearColorFilter();
						avatarRight.setColorFilter(R.color.TransBlack);
						nameBox.clearBuffer();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setText(charName);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(decisions[tempDecision]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(decisions[tempDecision]);
						}
						savePrefInt("BranchStory", branchStory);
						branchStory = 5;
					}
					saveStory(story, plot, chapter);
				}
				else if(branchStory == 5){
					disableEvent = false;
					setCharVisibility(true, false, true);
					decisions = res.getStringArray(R.array.DG_decision_branch_02_result_01);
					if(tempLoc == 0){
						avatarRight.setImageResource(R.drawable.dgakiyama05);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero101);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						}
						changeBackground(R.drawable.dg_school_corridor_day);
						avatarRight.clearColorFilter();
						avatarLeft.clearColorFilter();
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(decisions[tempDecision + 2]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(decisions[tempDecision + 2]);
						}
					}
					else if(tempLoc == 1){
						avatarRight.setImageResource(R.drawable.dgakiyama03);
						avatarLeft.setImageResource(R.drawable.dgstudent101);
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
						avatarLeft.clearColorFilter();
						avatarRight.setColorFilter(R.color.TransBlack);
						nameBox.clearBuffer();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.LEFT);
						nameBox.setText("Student 1");
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(decisions[tempDecision + 2]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(decisions[tempDecision + 2]);
						}
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 6;
				}
				else if(branchStory == 6){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					decisions = res.getStringArray(R.array.DG_decision_branch_02_result_01);
					if(tempLoc == 0){
						avatarRight.setImageResource(R.drawable.dgakiyama05);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero101);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						}
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
						avatarRight.clearColorFilter();
						avatarLeft.setColorFilter(R.color.TransBlack);
						nameBox.clearBuffer();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.RIGHT);
						nameBox.setText("Akiyama");
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(decisions[tempDecision + 4]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(decisions[tempDecision + 4]);
						}
					}
					else if(tempLoc == 1){
						avatarRight.setImageResource(R.drawable.dgakiyama03);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero109);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero109);
						}
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
						avatarLeft.clearColorFilter();
						avatarRight.setColorFilter(R.color.TransBlack);
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(decisions[tempDecision + 4]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(decisions[tempDecision + 4]);
						}
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 7;
				}
				else if(branchStory == 7){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama03);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 8;
					inputMode = 0;
					savePrefInt("InputMode", inputMode);
				}
				else if(branchStory == 8){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama06);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero109);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero109);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Akiyama");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 9;
				}
				else if(branchStory == 9){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama03);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 10;
				}
				else if(branchStory == 10){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama02);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero103);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero103);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Akiyama");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 11;
				}
				else if(branchStory == 11){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama04);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 12;
				}
				else if(branchStory == 12){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama05);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero109);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero109);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Akiyama");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 13;
				}
				else if(branchStory == 13){
					disableEvent = true;
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					nameBox.clearBuffer();
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setTypeface(null, Typeface.NORMAL);
					textBox.animateText("");
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[branchStory - 2]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[branchStory - 2]);
					}
					avatarRight.setImageResource(R.drawable.dgakiyama04);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero109);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero109);
					}
					avatarRight.clearColorFilter();
					avatarLeft.clearColorFilter();
					AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[branchStory - 2])
					.setItems(R.array.DG_decision_branch_02_02, new DialogInterface.OnClickListener() {
								
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							decisions = res.getStringArray(R.array.DG_decision_branch_02_result_02);
							tempDecision = which;
							TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
							ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
							ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
							TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
							if(tempDecision == 0){
								avatarRight.setImageResource(R.drawable.dgakiyama04);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero105);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero105);
								}
								socialPoints += 0;
							}
							else if(tempDecision == 1){
								avatarRight.setImageResource(R.drawable.dgakiyama04);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero102);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero102);
								}
								socialPoints += 1;
							}
							avatarLeft.clearColorFilter();
							avatarRight.setColorFilter(R.color.TransBlack);
							nameBox.clearBuffer();
							nameBox.setTypeface(null, Typeface.BOLD);
							nameBox.setText(charName);
							if(textSpeed == 0){
								textBox.clearBuffer();
								textBox.setText(decisions[tempDecision]);
							}
							else{
								textBox.setCharacterDelay(textSpeed);
								textBox.animateText(decisions[tempDecision]);
							}
							savePrefInt("SocialPoints", socialPoints);
							inputMode = 1;
							savePrefInt("InputMode", inputMode);
							savePrefInt("TempDecision", tempDecision);
							savePrefInt("BranchStory", branchStory);
							branchStory = 14;
							}
						}
					);
					if(inputMode == 0){
						AlertDialog decision = builder.create();
						decision.show();
					}
					else{
						decisions = res.getStringArray(R.array.DG_decision_branch_02_result_02);
						if(tempDecision == 0){
							avatarRight.setImageResource(R.drawable.dgakiyama04);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero105);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero105);
							}
						}
						else if(tempDecision == 1){
							avatarRight.setImageResource(R.drawable.dgakiyama04);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero102);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero102);
							}
						}
						avatarLeft.clearColorFilter();
						avatarRight.setColorFilter(R.color.TransBlack);
						nameBox.clearBuffer();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setText(charName);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(decisions[tempDecision]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(decisions[tempDecision]);
						}
						savePrefInt("BranchStory", branchStory);
						branchStory = 14;
					}
					saveStory(story, plot, chapter);
				}
				else if(branchStory == 14){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					decisions = res.getStringArray(R.array.DG_decision_branch_02_result_02);
					if(tempDecision == 0){
						avatarRight.setImageResource(R.drawable.dgakiyama07);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero111);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero111);
						}
					}
					else if(tempDecision == 1){
						avatarRight.setImageResource(R.drawable.dgakiyama02);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero101);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						}
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Akiyama");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(decisions[tempDecision + 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(decisions[tempDecision + 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 15;
				}
				else if(branchStory == 15){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					decisions = res.getStringArray(R.array.DG_decision_branch_02_result_02);
					if(tempDecision == 0){
						avatarRight.setImageResource(R.drawable.dgakiyama03);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero104);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero104);
						}
					}
					else if(tempDecision == 1){
						avatarRight.setImageResource(R.drawable.dgakiyama01);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero102);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero102);
						}
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(decisions[tempDecision + 4]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(decisions[tempDecision + 4]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 16;
				}

				else if(branchStory == 16){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 4]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 4]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 17;
				}
				else if(branchStory == 17){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama06);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Akiyama");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 4]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 4]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 18;
				}
				else if(branchStory == 18){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama03);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 4]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 4]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 19;
				}
				else if(branchStory == 19){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama02);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero103);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero103);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Akiyama");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 4]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 4]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 20;
				}
				else if(branchStory == 20){
					setCharVisibility(false, true, false);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					if(gender.equals("Male")){
						avatarCenter.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarCenter.setImageResource(R.drawable.dgfemalehero101);
					}
					textBox.animateText("");
					nameBox.setTypeface(null, Typeface.NORMAL);
					nameBox.setGravity(Gravity.LEFT);
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[branchStory - 4]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[branchStory - 4]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 0;
					branch02 = true;
					story = 3;
					inputMode = 0;
					savePrefInt("InputMode", inputMode);
				}
			}
			else if(branch == 2){
				stories = res.getStringArray(R.array.DG_Story_Branch_03);
				if(branchStory == 0){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor03);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 1;
				}
				else if(branchStory == 1){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor04);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero104);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero104);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 2;
					inputMode = 0;
					savePrefInt("InputMode", inputMode);
				}
				else if(branchStory == 2){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor05);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 3;
				}
				else if(branchStory == 3){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 4;
				}
				else if(branchStory == 4){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor05);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 5;
				}
				else if(branchStory == 5){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 6;
				}
				else if(branchStory == 6){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor02);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 7;
					inputMode = 0;
					savePrefInt("InputMode", inputMode);
				}
				else if(branchStory == 7){
					disableEvent = true;
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					nameBox.clearBuffer();
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setTypeface(null, Typeface.NORMAL);
					textBox.animateText("");
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[branchStory]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[branchStory]);
					}
					setCharVisibility(true, false, true);
					avatarRight.setImageResource(R.drawable.dgjanitor02);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarLeft.clearColorFilter();
					avatarRight.clearColorFilter();
					
					AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[branchStory])
					.setItems(R.array.DG_decision_branch_03, new DialogInterface.OnClickListener() {
								
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							decisions = res.getStringArray(R.array.DG_decision_branch_03_result);
							tempDecision = which;
							TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
							ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
							ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
							TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
							if(tempDecision == 0){
								avatarRight.setImageResource(R.drawable.dgjanitor02);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero102);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero102);
								}
								socialPoints += 1;
							}
							else if(tempDecision == 1){
								avatarRight.setImageResource(R.drawable.dgjanitor02);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero109);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero109);
								}
								socialPoints += 0;
							}
							avatarLeft.clearColorFilter();
							avatarRight.setColorFilter(R.color.TransBlack);
							nameBox.clearBuffer();
							nameBox.setTypeface(null, Typeface.BOLD);
							nameBox.setText(charName);
							if(textSpeed == 0){
								textBox.clearBuffer();
								textBox.setText(decisions[tempDecision]);
							}
							else{
								textBox.setCharacterDelay(textSpeed);
								textBox.animateText(decisions[tempDecision]);
							}
							savePrefInt("SocialPoints", socialPoints);
							inputMode = 1;
							savePrefInt("InputMode", inputMode);
							savePrefInt("TempDecision", tempDecision);
							savePrefInt("BranchStory", branchStory);
							branchStory = 8;
							}
						}
					);
					if(inputMode == 0){
						AlertDialog decision = builder.create();
						decision.show();
					}
					else{
						decisions = res.getStringArray(R.array.DG_decision_branch_03_result);
						if(tempDecision == 0){
							avatarRight.setImageResource(R.drawable.dgjanitor01);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero102);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero102);
							}
						}
						else if(tempDecision == 1){
							avatarRight.setImageResource(R.drawable.dgjanitor06);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero109);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero109);
							}
						}
						avatarLeft.clearColorFilter();
						avatarRight.setColorFilter(R.color.TransBlack);
						nameBox.clearBuffer();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setText(charName);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(decisions[tempDecision]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(decisions[tempDecision]);
						}
						savePrefInt("BranchStory", branchStory);
						branchStory = 8;
					}
					saveStory(story, plot, chapter);
				}
				else if(branchStory == 8){
					disableEvent = false;
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					decisions = res.getStringArray(R.array.DG_decision_branch_03_result);
					if(tempDecision == 0){
						avatarRight.setImageResource(R.drawable.dgjanitor05);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero102);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero102);
						}
					}
					else if(tempDecision == 1){
						avatarRight.setImageResource(R.drawable.dgjanitor02);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero109);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero109);
						}
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(decisions[tempDecision + 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(decisions[tempDecision + 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 9;
				}
				else if(branchStory == 9){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 1]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 10;
				}
				else if(branchStory == 10){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor05);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 1]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 11;
				}
				else if(branchStory == 11){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 1]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 12;
				}
				else if(branchStory == 12){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor02);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 1]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 13;
				}
				else if(branchStory == 13){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor06);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 1]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 14;
				}
				else if(branchStory == 14){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor02);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 1]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 15;
				}
				else if(branchStory == 15){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 1]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 16;
				}
				else if(branchStory == 16){
					setCharVisibility(false, true, false);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					if(gender.equals("Male")){
						avatarCenter.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarCenter.setImageResource(R.drawable.dgfemalehero101);
					}
					textBox.animateText("");
					nameBox.setTypeface(null, Typeface.NORMAL);
					nameBox.setGravity(Gravity.LEFT);
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[branchStory - 1]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 0;
					branch03 = true;
					story = 3;
					inputMode = 0;
					savePrefInt("InputMode", inputMode);
				}
			}
		}
		else if(story == 3){
			disableEvent = true;
			setCharVisibility(true, false, true);
			stories = res.getStringArray(R.array.DG_Story_06);
			nameBox.clearBuffer();
			nameBox.setGravity(Gravity.LEFT);
			nameBox.setTypeface(null, Typeface.NORMAL);
			textBox.animateText("");
			setCharVisibility(false, true, false);
			if(textSpeed == 0){
				nameBox.clearBuffer();
				nameBox.setText(stories[story - 2]);
			}
			else{
				nameBox.setCharacterDelay(textSpeed);
				nameBox.animateText(stories[story - 2]);
			}
			currentView.setBackgroundResource(R.drawable.dg_school_main_hall_day);
			if(gender.equals("Male")){
				avatarCenter.setImageResource(R.drawable.dgmalehero101);
			}
			else if(gender.equals("Female")){
				avatarCenter.setImageResource(R.drawable.dgfemalehero101);
			}
			String[] tempDec = res.getStringArray(R.array.DG_decision_05);
			List<String> tempList = new ArrayList<String>();
			tempList.add(tempDec[0]);
			tempList.add(tempDec[1]);
			tempList.add(tempDec[2]);
			if(branch01){
				tempList.remove("Faculty Room");
			}
			else if(branch02){
				tempList.remove("Classroom");
			}
			else if(branch03){
				tempList.remove("Library");
			}
			final String[] tempDes = new String[2];
			tempDes[0] = tempList.get(0);
			tempDes[1] = tempList.get(1);
			AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[story - 2])
			.setItems(tempDes, new DialogInterface.OnClickListener() {
						
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					decisions = res.getStringArray(R.array.DG_decision_result_05);
					selectedItem = tempDes[which];
					TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
					ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
					ImageView avatarCenter = (ImageView) findViewById(R.id.avatarCenter);
					ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
					TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
					if(selectedItem.equals("Faculty Room")){
						setCharVisibility(true, false, true);
						avatarRight.setImageResource(R.drawable.dgteacher01);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero101);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						}
						changeBackground(R.drawable.dg_school_faculty_room);
						branch = 0;
					}
					else if(selectedItem.equals("Classroom")){
						setCharVisibility(false, true, false);
						if(gender.equals("Male")){
							avatarCenter.setImageResource(R.drawable.dgmalehero101);
						}
						else if(gender.equals("Female")){
							avatarCenter.setImageResource(R.drawable.dgfemalehero101);
						}
						changeBackground(R.drawable.dg_school_classroom_day);
						branch = 1;
					}
					else if(selectedItem.equals("Library")){
						setCharVisibility(true, false, true);
						avatarRight.setImageResource(R.drawable.dgjanitor01);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero101);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						}
						changeBackground(R.drawable.dg_school_library_day);
						branch = 2;
					}
					avatarLeft.clearColorFilter();
					avatarRight.clearColorFilter();
					nameBox.setTypeface(null, Typeface.NORMAL);
					textBox.animateText("");
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(decisions[branch].replace("schoolName", schoolName));
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(decisions[branch].replace("schoolName", schoolName));
					}
					inputMode = 1;
					savePrefInt("InputMode", inputMode);
					savePrefInt("Branch", branch);
					saveStory(story, plot, chapter);
					story = 4;
					inputMode = 0;
					}
				}
			);
			if(inputMode == 0){
				AlertDialog decision = builder.create();
				decision.show();
			}
			else{
				decisions = res.getStringArray(R.array.DG_decision_result_05);
				if(selectedItem.equals("Faculty Room")){
					setCharVisibility(true, false, true);
					avatarRight.setImageResource(R.drawable.dgteacher01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
					}
					changeBackground(R.drawable.dg_school_faculty_room);
					branch = 0;
				}
				else if(selectedItem.equals("Classroom")){
					setCharVisibility(false, true, false);
					if(gender.equals("Male")){
						avatarCenter.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarCenter.setImageResource(R.drawable.dgfemalehero101);
					}
					changeBackground(R.drawable.dg_school_classroom_day);
					branch = 1;
				}
				else if(selectedItem.equals("Library")){
					setCharVisibility(true, false, true);
					avatarRight.setImageResource(R.drawable.dgjanitor01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
					}
					changeBackground(R.drawable.dg_school_library_day);
					branch = 2;
				}
				avatarLeft.clearColorFilter();
				avatarRight.clearColorFilter();
				nameBox.setTypeface(null, Typeface.NORMAL);
				textBox.animateText("");
				if(textSpeed == 0){
					nameBox.clearBuffer();
					nameBox.setText(decisions[branch].replace("schoolName", schoolName));
				}
				else{
					nameBox.setCharacterDelay(textSpeed);
					nameBox.animateText(decisions[branch].replace("schoolName", schoolName));
				}
				saveStory(story, plot, chapter);
				story = 4;
			}
		}
		else if(story == 4){
			if(branch == 0){
				stories = res.getStringArray(R.array.DG_Story_Branch_01);
				if(branchStory == 0){
					disableEvent = true;
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					nameBox.clearBuffer();
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setTypeface(null, Typeface.NORMAL);
					textBox.animateText("");
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[branchStory]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[branchStory]);
					}
					setCharVisibility(true, false, true);
					avatarRight.setImageResource(R.drawable.dgteacher01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
					}
					avatarLeft.clearColorFilter();
					avatarRight.clearColorFilter();
					
					AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[branchStory])
					.setItems(R.array.DG_decision_branch_01, new DialogInterface.OnClickListener() {
								
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							decisions = res.getStringArray(R.array.DG_decision_branch_01_result);
							tempDecision = which;
							TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
							ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
							ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
							TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
							if(tempDecision == 0){
								avatarRight.setImageResource(R.drawable.dgteacher01);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero102);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero102);
								}
								socialPoints += 1;
							}
							else if(tempDecision == 1){
								avatarRight.setImageResource(R.drawable.dgteacher08);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero105);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero105);
								}
								socialPoints += 0;
							}
							avatarLeft.clearColorFilter();
							avatarRight.setColorFilter(R.color.TransBlack);
							nameBox.clearBuffer();
							nameBox.setTypeface(null, Typeface.BOLD);
							nameBox.setText(charName);
							if(textSpeed == 0){
								textBox.clearBuffer();
								textBox.setText(decisions[tempDecision]);
							}
							else{
								textBox.setCharacterDelay(textSpeed);
								textBox.animateText(decisions[tempDecision]);
							}
							savePrefInt("SocialPoints", socialPoints);
							inputMode = 1;
							savePrefInt("InputMode", inputMode);
							savePrefInt("TempDecision", tempDecision);
							savePrefInt("BranchStory", branchStory);
							branchStory = 1;
							}
						}
					);
					if(inputMode == 0){
						AlertDialog decision = builder.create();
						decision.show();
					}
					else{
						decisions = res.getStringArray(R.array.DG_decision_branch_01_result);
						if(tempDecision == 0){
							avatarRight.setImageResource(R.drawable.dgteacher01);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero102);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero102);
							}
						}
						else if(tempDecision == 1){
							avatarRight.setImageResource(R.drawable.dgteacher08);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero105);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero105);
							}
						}
						avatarLeft.clearColorFilter();
						avatarRight.setColorFilter(R.color.TransBlack);
						nameBox.clearBuffer();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setText(charName);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(decisions[tempDecision]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(decisions[tempDecision]);
						}
						savePrefInt("BranchStory", branchStory);
						branchStory = 1;
					}
					saveStory(story, plot, chapter);
				}
				else if(branchStory == 1){
					disableEvent = false;
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					decisions = res.getStringArray(R.array.DG_decision_branch_01_result);
					if(tempDecision == 0){
						avatarRight.setImageResource(R.drawable.dgteacher02);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero101);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						}
					}
					else if(tempDecision == 1){
						avatarRight.setImageResource(R.drawable.dgteacher03);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero111);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero111);
						}
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Mr. Harada");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(decisions[tempDecision + 2].replace("char_name", charName));
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(decisions[tempDecision + 2].replace("char_name", charName));
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 2;
				}
				else if(branchStory == 2){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					decisions = res.getStringArray(R.array.DG_decision_branch_01_result);
					if(tempDecision == 0){
						avatarRight.setImageResource(R.drawable.dgteacher01);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero102);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero102);
						}
					}
					else if(tempDecision == 1){
						avatarRight.setImageResource(R.drawable.dgteacher05);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero110);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero110);
						}
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(decisions[tempDecision + 4]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(decisions[tempDecision + 4]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 3;
				}
				else if(branchStory == 3){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					decisions = res.getStringArray(R.array.DG_decision_branch_01_result);
					if(tempDecision == 0){
						avatarRight.setImageResource(R.drawable.dgteacher02);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero101);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						}
					}
					else if(tempDecision == 1){
						avatarRight.setImageResource(R.drawable.dgteacher03);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero111);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero111);
						}
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Mr. Harada");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(decisions[tempDecision + 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(decisions[tempDecision + 6]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 4;
				}
				else if(branchStory == 4){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 5;
				}
				else if(branchStory == 5){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher04);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Mr. Harada");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 6;
				}
				else if(branchStory == 6){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher06);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Mr. Harada");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 7;
				}
				else if(branchStory == 7){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher08);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 8;
				}
				else if(branchStory == 8){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher07);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Mr. Harada");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 9;
				}
				else if(branchStory == 9){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher08);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 10;
				}
				else if(branchStory == 10){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher07);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Mr. Harada");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 11;
				}
				else if(branchStory == 11){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 12;
				}
				else if(branchStory == 12){
					setCharVisibility(false, true, false);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					if(gender.equals("Male")){
						avatarCenter.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarCenter.setImageResource(R.drawable.dgfemalehero101);
					}
					textBox.animateText("");
					nameBox.setTypeface(null, Typeface.NORMAL);
					nameBox.setGravity(Gravity.LEFT);
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[branchStory - 3]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 0;
					branch01 = true;
					story = 5;
					inputMode = 0;
					savePrefInt("InputMode", inputMode);
				}
			}
			else if(branch == 1){
				stories = res.getStringArray(R.array.DG_Story_Branch_02);
				if(branchStory == 0){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama03);
					avatarLeft.setImageResource(R.drawable.dgstudent101);
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText("Student 1");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 1;
					inputMode = 0;
					savePrefInt("InputMode", inputMode);
				}
				else if(branchStory == 1){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama03);
					avatarLeft.setImageResource(R.drawable.dgstudent201);
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText("Student 2");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 2;
				}
				else if(branchStory == 2){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama07);
					avatarLeft.setImageResource(R.drawable.dgstudent201);
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Akiyama");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 3;
				}
				else if(branchStory == 3){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama07);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarLeft.clearColorFilter();
					avatarRight.clearColorFilter();
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 4;
				}
				else if(branchStory == 4){
					disableEvent = true;
					setCharVisibility(false, true, false);
					currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					nameBox.clearBuffer();
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setTypeface(null, Typeface.NORMAL);
					textBox.animateText("");
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[branchStory]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[branchStory]);
					}
					if(gender.equals("Male")){
						avatarCenter.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarCenter.setImageResource(R.drawable.dgfemalehero101);
					}
					
					AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[branchStory])
					.setItems(R.array.DG_decision_branch_02_01, new DialogInterface.OnClickListener() {
								
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							decisions = res.getStringArray(R.array.DG_decision_branch_02_result_01);
							tempDecision = which;
							tempLoc = which;
							TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
							ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
							ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
							TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
							if(tempDecision == 0){
								avatarRight.setImageResource(R.drawable.dgakiyama05);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero105);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero105);
								}
								socialPoints += 1;
							}
							else if(tempDecision == 1){
								avatarRight.setImageResource(R.drawable.dgakiyama05);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero109);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero109);
								}
								socialPoints += 0;
							}
							avatarLeft.clearColorFilter();
							avatarRight.setColorFilter(R.color.TransBlack);
							nameBox.clearBuffer();
							nameBox.setTypeface(null, Typeface.BOLD);
							nameBox.setText(charName);
							if(textSpeed == 0){
								textBox.clearBuffer();
								textBox.setText(decisions[tempDecision]);
							}
							else{
								textBox.setCharacterDelay(textSpeed);
								textBox.animateText(decisions[tempDecision]);
							}
							savePrefInt("SocialPoints", socialPoints);
							inputMode = 1;
							savePrefInt("InputMode", inputMode);
							savePrefInt("TempDecision", tempDecision);
							savePrefInt("TempLoc", tempLoc);
							savePrefInt("BranchStory", branchStory);
							branchStory = 5;
							}
						}
					);
					if(inputMode == 0){
						AlertDialog decision = builder.create();
						decision.show();
					}
					else{
						decisions = res.getStringArray(R.array.DG_decision_branch_02_result_01);
						if(tempDecision == 0){
							avatarRight.setImageResource(R.drawable.dgakiyama05);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero105);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero105);
							}
						}
						else if(tempDecision == 1){
							avatarRight.setImageResource(R.drawable.dgakiyama05);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero109);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero109);
							}
						}
						avatarLeft.clearColorFilter();
						avatarRight.setColorFilter(R.color.TransBlack);
						nameBox.clearBuffer();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setText(charName);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(decisions[tempDecision]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(decisions[tempDecision]);
						}
						savePrefInt("BranchStory", branchStory);
						branchStory = 5;
					}
					saveStory(story, plot, chapter);
				}
				else if(branchStory == 5){
					disableEvent = false;
					setCharVisibility(true, false, true);
					decisions = res.getStringArray(R.array.DG_decision_branch_02_result_01);
					if(tempLoc == 0){
						avatarRight.setImageResource(R.drawable.dgakiyama05);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero101);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						}
						changeBackground(R.drawable.dg_school_corridor_day);
						avatarRight.clearColorFilter();
						avatarLeft.clearColorFilter();
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(decisions[tempDecision + 2]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(decisions[tempDecision + 2]);
						}
					}
					else if(tempLoc == 1){
						avatarRight.setImageResource(R.drawable.dgakiyama03);
						avatarLeft.setImageResource(R.drawable.dgstudent101);
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
						avatarLeft.clearColorFilter();
						avatarRight.setColorFilter(R.color.TransBlack);
						nameBox.clearBuffer();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.LEFT);
						nameBox.setText("Student 1");
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(decisions[tempDecision + 2]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(decisions[tempDecision + 2]);
						}
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 6;
				}
				else if(branchStory == 6){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					decisions = res.getStringArray(R.array.DG_decision_branch_02_result_01);
					if(tempLoc == 0){
						avatarRight.setImageResource(R.drawable.dgakiyama05);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero101);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						}
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
						avatarRight.clearColorFilter();
						avatarLeft.setColorFilter(R.color.TransBlack);
						nameBox.clearBuffer();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.RIGHT);
						nameBox.setText("Akiyama");
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(decisions[tempDecision + 4]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(decisions[tempDecision + 4]);
						}
					}
					else if(tempLoc == 1){
						avatarRight.setImageResource(R.drawable.dgakiyama03);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero109);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero109);
						}
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
						avatarLeft.clearColorFilter();
						avatarRight.setColorFilter(R.color.TransBlack);
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(decisions[tempDecision + 4]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(decisions[tempDecision + 4]);
						}
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 7;
				}
				else if(branchStory == 7){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama03);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 8;
					inputMode = 0;
					savePrefInt("InputMode", inputMode);
				}
				else if(branchStory == 8){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama06);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero109);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero109);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Akiyama");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 9;
				}
				else if(branchStory == 9){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama03);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 10;
				}
				else if(branchStory == 10){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama02);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero103);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero103);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Akiyama");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 11;
				}
				else if(branchStory == 11){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama04);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 12;
				}
				else if(branchStory == 12){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama05);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero109);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero109);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Akiyama");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 13;
				}
				else if(branchStory == 13){
					disableEvent = true;
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					nameBox.clearBuffer();
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setTypeface(null, Typeface.NORMAL);
					textBox.animateText("");
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[branchStory - 2]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[branchStory - 2]);
					}
					avatarRight.setImageResource(R.drawable.dgakiyama04);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero109);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero109);
					}
					avatarRight.clearColorFilter();
					avatarLeft.clearColorFilter();
					AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[branchStory - 2])
					.setItems(R.array.DG_decision_branch_02_02, new DialogInterface.OnClickListener() {
								
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							decisions = res.getStringArray(R.array.DG_decision_branch_02_result_02);
							tempDecision = which;
							TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
							ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
							ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
							TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
							if(tempDecision == 0){
								avatarRight.setImageResource(R.drawable.dgakiyama04);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero105);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero105);
								}
								socialPoints += 0;
							}
							else if(tempDecision == 1){
								avatarRight.setImageResource(R.drawable.dgakiyama04);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero102);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero102);
								}
								socialPoints += 1;
							}
							avatarLeft.clearColorFilter();
							avatarRight.setColorFilter(R.color.TransBlack);
							nameBox.clearBuffer();
							nameBox.setTypeface(null, Typeface.BOLD);
							nameBox.setText(charName);
							if(textSpeed == 0){
								textBox.clearBuffer();
								textBox.setText(decisions[tempDecision]);
							}
							else{
								textBox.setCharacterDelay(textSpeed);
								textBox.animateText(decisions[tempDecision]);
							}
							savePrefInt("SocialPoints", socialPoints);
							inputMode = 1;
							savePrefInt("InputMode", inputMode);
							savePrefInt("TempDecision", tempDecision);
							savePrefInt("BranchStory", branchStory);
							branchStory = 14;
							}
						}
					);
					if(inputMode == 0){
						AlertDialog decision = builder.create();
						decision.show();
					}
					else{
						decisions = res.getStringArray(R.array.DG_decision_branch_02_result_02);
						if(tempDecision == 0){
							avatarRight.setImageResource(R.drawable.dgakiyama04);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero105);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero105);
							}
						}
						else if(tempDecision == 1){
							avatarRight.setImageResource(R.drawable.dgakiyama04);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero102);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero102);
							}
						}
						avatarLeft.clearColorFilter();
						avatarRight.setColorFilter(R.color.TransBlack);
						nameBox.clearBuffer();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setText(charName);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(decisions[tempDecision]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(decisions[tempDecision]);
						}
						savePrefInt("BranchStory", branchStory);
						branchStory = 14;
					}
					saveStory(story, plot, chapter);
				}
				else if(branchStory == 14){
					disableEvent = false;
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					decisions = res.getStringArray(R.array.DG_decision_branch_02_result_02);
					if(tempDecision == 0){
						avatarRight.setImageResource(R.drawable.dgakiyama07);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero111);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero111);
						}
					}
					else if(tempDecision == 1){
						avatarRight.setImageResource(R.drawable.dgakiyama02);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero101);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						}
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Akiyama");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(decisions[tempDecision + 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(decisions[tempDecision + 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 15;
				}
				else if(branchStory == 15){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					decisions = res.getStringArray(R.array.DG_decision_branch_02_result_02);
					if(tempDecision == 0){
						avatarRight.setImageResource(R.drawable.dgakiyama03);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero104);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero104);
						}
					}
					else if(tempDecision == 1){
						avatarRight.setImageResource(R.drawable.dgakiyama01);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero102);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero102);
						}
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(decisions[tempDecision + 4]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(decisions[tempDecision + 4]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 16;
				}

				else if(branchStory == 16){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 4]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 4]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 17;
				}
				else if(branchStory == 17){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama06);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Akiyama");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 4]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 4]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 18;
				}
				else if(branchStory == 18){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama03);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 4]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 4]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 19;
				}
				else if(branchStory == 19){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama02);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero103);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero103);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Akiyama");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 4]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 4]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 20;
				}
				else if(branchStory == 20){
					setCharVisibility(false, true, false);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					if(gender.equals("Male")){
						avatarCenter.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarCenter.setImageResource(R.drawable.dgfemalehero101);
					}
					textBox.animateText("");
					nameBox.setTypeface(null, Typeface.NORMAL);
					nameBox.setGravity(Gravity.LEFT);
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[branchStory - 4]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[branchStory - 4]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 0;
					branch02 = true;
					story = 5;
					inputMode = 0;
					savePrefInt("InputMode", inputMode);
				}
			}
			else if(branch == 2){
				stories = res.getStringArray(R.array.DG_Story_Branch_03);
				if(branchStory == 0){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor03);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 1;
					inputMode = 0;
					savePrefInt("InputMode", inputMode);
				}
				else if(branchStory == 1){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor04);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero104);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero104);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 2;
				}
				else if(branchStory == 2){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor05);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 3;
				}
				else if(branchStory == 3){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 4;
				}
				else if(branchStory == 4){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor05);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 5;
				}
				else if(branchStory == 5){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 6;
				}
				else if(branchStory == 6){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor02);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 7;
					inputMode = 0;
					savePrefInt("InputMode", inputMode);
				}
				else if(branchStory == 7){
					disableEvent = true;
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					nameBox.clearBuffer();
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setTypeface(null, Typeface.NORMAL);
					textBox.animateText("");
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[branchStory]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[branchStory]);
					}
					setCharVisibility(true, false, true);
					avatarRight.setImageResource(R.drawable.dgjanitor02);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarLeft.clearColorFilter();
					avatarRight.clearColorFilter();
					
					AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[branchStory])
					.setItems(R.array.DG_decision_branch_03, new DialogInterface.OnClickListener() {
								
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							decisions = res.getStringArray(R.array.DG_decision_branch_03_result);
							tempDecision = which;
							TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
							ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
							ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
							TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
							if(tempDecision == 0){
								avatarRight.setImageResource(R.drawable.dgjanitor02);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero102);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero102);
								}
								socialPoints += 1;
							}
							else if(tempDecision == 1){
								avatarRight.setImageResource(R.drawable.dgjanitor02);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero109);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero109);
								}
								socialPoints += 0;
							}
							avatarLeft.clearColorFilter();
							avatarRight.setColorFilter(R.color.TransBlack);
							nameBox.clearBuffer();
							nameBox.setTypeface(null, Typeface.BOLD);
							nameBox.setText(charName);
							if(textSpeed == 0){
								textBox.clearBuffer();
								textBox.setText(decisions[tempDecision]);
							}
							else{
								textBox.setCharacterDelay(textSpeed);
								textBox.animateText(decisions[tempDecision]);
							}
							savePrefInt("SocialPoints", socialPoints);
							inputMode = 1;
							savePrefInt("InputMode", inputMode);
							savePrefInt("TempDecision", tempDecision);
							savePrefInt("BranchStory", branchStory);
							branchStory = 8;
							}
						}
					);
					if(inputMode == 0){
						AlertDialog decision = builder.create();
						decision.show();
					}
					else{
						decisions = res.getStringArray(R.array.DG_decision_branch_03_result);
						if(tempDecision == 0){
							avatarRight.setImageResource(R.drawable.dgjanitor01);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero102);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero102);
							}
						}
						else if(tempDecision == 1){
							avatarRight.setImageResource(R.drawable.dgjanitor06);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero109);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero109);
							}
						}
						avatarLeft.clearColorFilter();
						avatarRight.setColorFilter(R.color.TransBlack);
						nameBox.clearBuffer();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setText(charName);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(decisions[tempDecision]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(decisions[tempDecision]);
						}
						savePrefInt("BranchStory", branchStory);
						branchStory = 8;
					}
					saveStory(story, plot, chapter);
				}
				else if(branchStory == 8){
					disableEvent = false;
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					decisions = res.getStringArray(R.array.DG_decision_branch_03_result);
					if(tempDecision == 0){
						avatarRight.setImageResource(R.drawable.dgjanitor05);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero102);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero102);
						}
					}
					else if(tempDecision == 1){
						avatarRight.setImageResource(R.drawable.dgjanitor02);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero109);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero109);
						}
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(decisions[tempDecision + 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(decisions[tempDecision + 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 9;
				}
				else if(branchStory == 9){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 1]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 10;
				}
				else if(branchStory == 10){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor05);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 1]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 11;
				}
				else if(branchStory == 11){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 1]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 12;
				}
				else if(branchStory == 12){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor02);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 1]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 13;
				}
				else if(branchStory == 13){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor06);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 1]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 14;
				}
				else if(branchStory == 14){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor02);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 1]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 15;
				}
				else if(branchStory == 15){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 1]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 16;
				}
				else if(branchStory == 16){
					setCharVisibility(false, true, false);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					if(gender.equals("Male")){
						avatarCenter.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarCenter.setImageResource(R.drawable.dgfemalehero101);
					}
					textBox.animateText("");
					nameBox.setTypeface(null, Typeface.NORMAL);
					nameBox.setGravity(Gravity.LEFT);
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[branchStory - 1]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 0;
					branch03 = true;
					story = 5;
					inputMode = 0;
					savePrefInt("InputMode", inputMode);
				}
			}
		}
		else if(story == 5){
			if(branch01 && branch02){
				branch = 2;
			}
			else if(branch01 && branch03){
				branch = 1;
			}
			else if(branch02 && branch03){
				branch = 0;
			}
			if(branch == 0){
				stories = res.getStringArray(R.array.DG_Story_Branch_01);
				if(branchStory == 0){
					disableEvent = true;
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					nameBox.clearBuffer();
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setTypeface(null, Typeface.NORMAL);
					textBox.animateText("");
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[branchStory]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[branchStory]);
					}
					setCharVisibility(true, false, true);
					avatarRight.setImageResource(R.drawable.dgteacher01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
					}
					avatarLeft.clearColorFilter();
					avatarRight.clearColorFilter();
					
					AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[branchStory])
					.setItems(R.array.DG_decision_branch_01, new DialogInterface.OnClickListener() {
								
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							decisions = res.getStringArray(R.array.DG_decision_branch_01_result);
							tempDecision = which;
							TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
							ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
							ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
							TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
							if(tempDecision == 0){
								avatarRight.setImageResource(R.drawable.dgteacher01);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero102);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero102);
								}
								socialPoints += 1;
							}
							else if(tempDecision == 1){
								avatarRight.setImageResource(R.drawable.dgteacher08);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero105);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero105);
								}
								socialPoints += 0;
							}
							avatarLeft.clearColorFilter();
							avatarRight.setColorFilter(R.color.TransBlack);
							nameBox.clearBuffer();
							nameBox.setTypeface(null, Typeface.BOLD);
							nameBox.setText(charName);
							if(textSpeed == 0){
								textBox.clearBuffer();
								textBox.setText(decisions[tempDecision]);
							}
							else{
								textBox.setCharacterDelay(textSpeed);
								textBox.animateText(decisions[tempDecision]);
							}
							savePrefInt("SocialPoints", socialPoints);
							inputMode = 1;
							savePrefInt("InputMode", inputMode);
							savePrefInt("TempDecision", tempDecision);
							savePrefInt("BranchStory", branchStory);
							branchStory = 1;
							}
						}
					);
					if(inputMode == 0){
						AlertDialog decision = builder.create();
						decision.show();
					}
					else{
						decisions = res.getStringArray(R.array.DG_decision_branch_01_result);
						if(tempDecision == 0){
							avatarRight.setImageResource(R.drawable.dgteacher01);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero102);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero102);
							}
						}
						else if(tempDecision == 1){
							avatarRight.setImageResource(R.drawable.dgteacher08);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero105);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero105);
							}
						}
						avatarLeft.clearColorFilter();
						avatarRight.setColorFilter(R.color.TransBlack);
						nameBox.clearBuffer();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setText(charName);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(decisions[tempDecision]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(decisions[tempDecision]);
						}
						savePrefInt("BranchStory", branchStory);
						branchStory = 1;
					}
					saveStory(story, plot, chapter);
				}
				else if(branchStory == 1){
					disableEvent = false;
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					decisions = res.getStringArray(R.array.DG_decision_branch_01_result);
					if(tempDecision == 0){
						avatarRight.setImageResource(R.drawable.dgteacher02);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero101);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						}
					}
					else if(tempDecision == 1){
						avatarRight.setImageResource(R.drawable.dgteacher03);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero111);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero111);
						}
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Mr. Harada");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(decisions[tempDecision + 2].replace("char_name", charName));
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(decisions[tempDecision + 2].replace("char_name", charName));
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 2;
				}
				else if(branchStory == 2){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					decisions = res.getStringArray(R.array.DG_decision_branch_01_result);
					if(tempDecision == 0){
						avatarRight.setImageResource(R.drawable.dgteacher01);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero102);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero102);
						}
					}
					else if(tempDecision == 1){
						avatarRight.setImageResource(R.drawable.dgteacher05);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero110);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero110);
						}
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(decisions[tempDecision + 4]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(decisions[tempDecision + 4]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 3;
				}
				else if(branchStory == 3){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					decisions = res.getStringArray(R.array.DG_decision_branch_01_result);
					if(tempDecision == 0){
						avatarRight.setImageResource(R.drawable.dgteacher02);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero101);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						}
					}
					else if(tempDecision == 1){
						avatarRight.setImageResource(R.drawable.dgteacher03);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero111);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero111);
						}
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Mr. Harada");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(decisions[tempDecision + 6]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(decisions[tempDecision + 6]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 4;
				}
				else if(branchStory == 4){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 5;
				}
				else if(branchStory == 5){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher04);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Mr. Harada");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 6;
				}
				else if(branchStory == 6){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher06);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Mr. Harada");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 7;
				}
				else if(branchStory == 7){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher08);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 8;
				}
				else if(branchStory == 8){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher07);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Mr. Harada");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 9;
				}
				else if(branchStory == 9){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher08);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 10;
				}
				else if(branchStory == 10){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher07);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Mr. Harada");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 11;
				}
				else if(branchStory == 11){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					avatarRight.setImageResource(R.drawable.dgteacher01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 3]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 12;
				}
				else if(branchStory == 12){
					setCharVisibility(false, true, false);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					if(gender.equals("Male")){
						avatarCenter.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarCenter.setImageResource(R.drawable.dgfemalehero101);
					}
					textBox.animateText("");
					nameBox.setTypeface(null, Typeface.NORMAL);
					nameBox.setGravity(Gravity.LEFT);
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[branchStory - 3]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[branchStory - 3]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 0;
					story = 6;
					inputMode = 0;
					savePrefInt("InputMode", inputMode);
				}
			}
			else if(branch == 1){
				stories = res.getStringArray(R.array.DG_Story_Branch_02);
				if(branchStory == 0){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama03);
					avatarLeft.setImageResource(R.drawable.dgstudent101);
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText("Student 1");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 1;
					inputMode = 0;
					savePrefInt("InputMode", inputMode);
				}
				else if(branchStory == 1){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama03);
					avatarLeft.setImageResource(R.drawable.dgstudent201);
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText("Student 2");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 2;
				}
				else if(branchStory == 2){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama07);
					avatarLeft.setImageResource(R.drawable.dgstudent201);
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Akiyama");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 3;
				}
				else if(branchStory == 3){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama07);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarLeft.clearColorFilter();
					avatarRight.clearColorFilter();
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 4;
				}
				else if(branchStory == 4){
					disableEvent = true;
					setCharVisibility(false, true, false);
					currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					nameBox.clearBuffer();
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setTypeface(null, Typeface.NORMAL);
					textBox.animateText("");
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[branchStory]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[branchStory]);
					}
					if(gender.equals("Male")){
						avatarCenter.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarCenter.setImageResource(R.drawable.dgfemalehero101);
					}
					
					AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[branchStory])
					.setItems(R.array.DG_decision_branch_02_01, new DialogInterface.OnClickListener() {
								
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							decisions = res.getStringArray(R.array.DG_decision_branch_02_result_01);
							tempDecision = which;
							tempLoc = which;
							TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
							ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
							ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
							TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
							if(tempDecision == 0){
								avatarRight.setImageResource(R.drawable.dgakiyama05);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero105);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero105);
								}
								socialPoints += 1;
							}
							else if(tempDecision == 1){
								avatarRight.setImageResource(R.drawable.dgakiyama05);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero109);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero109);
								}
								socialPoints += 0;
							}
							avatarLeft.clearColorFilter();
							avatarRight.setColorFilter(R.color.TransBlack);
							nameBox.clearBuffer();
							nameBox.setTypeface(null, Typeface.BOLD);
							nameBox.setText(charName);
							if(textSpeed == 0){
								textBox.clearBuffer();
								textBox.setText(decisions[tempDecision]);
							}
							else{
								textBox.setCharacterDelay(textSpeed);
								textBox.animateText(decisions[tempDecision]);
							}
							savePrefInt("SocialPoints", socialPoints);
							inputMode = 1;
							savePrefInt("InputMode", inputMode);
							savePrefInt("TempDecision", tempDecision);
							savePrefInt("TempLoc", tempLoc);
							savePrefInt("BranchStory", branchStory);
							branchStory = 5;
							}
						}
					);
					if(inputMode == 0){
						AlertDialog decision = builder.create();
						decision.show();
					}
					else{
						decisions = res.getStringArray(R.array.DG_decision_branch_02_result_01);
						if(tempDecision == 0){
							avatarRight.setImageResource(R.drawable.dgakiyama05);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero105);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero105);
							}
						}
						else if(tempDecision == 1){
							avatarRight.setImageResource(R.drawable.dgakiyama05);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero109);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero109);
							}
						}
						avatarLeft.clearColorFilter();
						avatarRight.setColorFilter(R.color.TransBlack);
						nameBox.clearBuffer();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setText(charName);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(decisions[tempDecision]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(decisions[tempDecision]);
						}
						savePrefInt("BranchStory", branchStory);
						branchStory = 5;
					}
					saveStory(story, plot, chapter);
				}
				else if(branchStory == 5){
					disableEvent = false;
					setCharVisibility(true, false, true);
					decisions = res.getStringArray(R.array.DG_decision_branch_02_result_01);
					if(tempLoc == 0){
						avatarRight.setImageResource(R.drawable.dgakiyama05);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero101);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						}
						changeBackground(R.drawable.dg_school_corridor_day);
						avatarRight.clearColorFilter();
						avatarLeft.clearColorFilter();
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(decisions[tempDecision + 2]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(decisions[tempDecision + 2]);
						}
					}
					else if(tempLoc == 1){
						avatarRight.setImageResource(R.drawable.dgakiyama03);
						avatarLeft.setImageResource(R.drawable.dgstudent101);
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
						avatarLeft.clearColorFilter();
						avatarRight.setColorFilter(R.color.TransBlack);
						nameBox.clearBuffer();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.LEFT);
						nameBox.setText("Student 1");
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(decisions[tempDecision + 2]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(decisions[tempDecision + 2]);
						}
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 6;
				}
				else if(branchStory == 6){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_faculty_room);
					decisions = res.getStringArray(R.array.DG_decision_branch_02_result_01);
					if(tempLoc == 0){
						avatarRight.setImageResource(R.drawable.dgakiyama05);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero101);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						}
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
						avatarRight.clearColorFilter();
						avatarLeft.setColorFilter(R.color.TransBlack);
						nameBox.clearBuffer();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setGravity(Gravity.RIGHT);
						nameBox.setText("Akiyama");
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(decisions[tempDecision + 4]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(decisions[tempDecision + 4]);
						}
					}
					else if(tempLoc == 1){
						avatarRight.setImageResource(R.drawable.dgakiyama03);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero109);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero109);
						}
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
						avatarLeft.clearColorFilter();
						avatarRight.setColorFilter(R.color.TransBlack);
						textBox.animateText("");
						nameBox.setTypeface(null, Typeface.NORMAL);
						nameBox.setGravity(Gravity.LEFT);
						if(textSpeed == 0){
							nameBox.clearBuffer();
							nameBox.setText(decisions[tempDecision + 4]);
						}
						else{
							nameBox.setCharacterDelay(textSpeed);
							nameBox.animateText(decisions[tempDecision + 4]);
						}
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 7;
				}
				else if(branchStory == 7){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama03);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 8;
					inputMode = 0;
					savePrefInt("InputMode", inputMode);
				}
				else if(branchStory == 8){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama06);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero109);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero109);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Akiyama");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 9;
				}
				else if(branchStory == 9){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama03);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 10;
				}
				else if(branchStory == 10){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama02);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero103);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero103);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Akiyama");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 11;
				}
				else if(branchStory == 11){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama04);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 12;
				}
				else if(branchStory == 12){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama05);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero109);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero109);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Akiyama");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 13;
				}
				else if(branchStory == 13){
					disableEvent = true;
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					nameBox.clearBuffer();
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setTypeface(null, Typeface.NORMAL);
					textBox.animateText("");
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[branchStory - 2]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[branchStory - 2]);
					}
					avatarRight.setImageResource(R.drawable.dgakiyama04);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero109);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero109);
					}
					avatarRight.clearColorFilter();
					avatarLeft.clearColorFilter();
					AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[branchStory - 2])
					.setItems(R.array.DG_decision_branch_02_02, new DialogInterface.OnClickListener() {
								
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							decisions = res.getStringArray(R.array.DG_decision_branch_02_result_02);
							tempDecision = which;
							TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
							ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
							ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
							TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
							if(tempDecision == 0){
								avatarRight.setImageResource(R.drawable.dgakiyama04);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero105);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero105);
								}
								socialPoints += 0;
							}
							else if(tempDecision == 1){
								avatarRight.setImageResource(R.drawable.dgakiyama04);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero102);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero102);
								}
								socialPoints += 1;
							}
							avatarLeft.clearColorFilter();
							avatarRight.setColorFilter(R.color.TransBlack);
							nameBox.clearBuffer();
							nameBox.setTypeface(null, Typeface.BOLD);
							nameBox.setText(charName);
							if(textSpeed == 0){
								textBox.clearBuffer();
								textBox.setText(decisions[tempDecision]);
							}
							else{
								textBox.setCharacterDelay(textSpeed);
								textBox.animateText(decisions[tempDecision]);
							}
							savePrefInt("SocialPoints", socialPoints);
							inputMode = 1;
							savePrefInt("InputMode", inputMode);
							savePrefInt("TempDecision", tempDecision);
							savePrefInt("BranchStory", branchStory);
							branchStory = 14;
							}
						}
					);
					if(inputMode == 0){
						AlertDialog decision = builder.create();
						decision.show();
					}
					else{
						decisions = res.getStringArray(R.array.DG_decision_branch_02_result_02);
						if(tempDecision == 0){
							avatarRight.setImageResource(R.drawable.dgakiyama04);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero105);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero105);
							}
						}
						else if(tempDecision == 1){
							avatarRight.setImageResource(R.drawable.dgakiyama04);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero102);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero102);
							}
						}
						avatarLeft.clearColorFilter();
						avatarRight.setColorFilter(R.color.TransBlack);
						nameBox.clearBuffer();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setText(charName);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(decisions[tempDecision]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(decisions[tempDecision]);
						}
						savePrefInt("BranchStory", branchStory);
						branchStory = 14;
					}
					saveStory(story, plot, chapter);
				}
				else if(branchStory == 14){
					disableEvent = false;
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					decisions = res.getStringArray(R.array.DG_decision_branch_02_result_02);
					if(tempDecision == 0){
						avatarRight.setImageResource(R.drawable.dgakiyama07);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero111);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero111);
						}
					}
					else if(tempDecision == 1){
						avatarRight.setImageResource(R.drawable.dgakiyama02);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero101);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero101);
						}
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Akiyama");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(decisions[tempDecision + 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(decisions[tempDecision + 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 15;
				}
				else if(branchStory == 15){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					decisions = res.getStringArray(R.array.DG_decision_branch_02_result_02);
					if(tempDecision == 0){
						avatarRight.setImageResource(R.drawable.dgakiyama03);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero104);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero104);
						}
					}
					else if(tempDecision == 1){
						avatarRight.setImageResource(R.drawable.dgakiyama01);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero102);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero102);
						}
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(decisions[tempDecision + 4]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(decisions[tempDecision + 4]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 16;
				}

				else if(branchStory == 16){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 4]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 4]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 17;
				}
				else if(branchStory == 17){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama06);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Akiyama");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 4]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 4]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 18;
				}
				else if(branchStory == 18){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama03);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 4]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 4]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 19;
				}
				else if(branchStory == 19){
					setCharVisibility(true, false, true);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					avatarRight.setImageResource(R.drawable.dgakiyama02);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero103);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero103);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Akiyama");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 4]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 4]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 20;
				}
				else if(branchStory == 20){
					setCharVisibility(false, true, false);
					if(tempLoc == 0)
						currentView.setBackgroundResource(R.drawable.dg_school_corridor_day);
					else if(tempLoc == 1)
						currentView.setBackgroundResource(R.drawable.dg_school_classroom_day);
					if(gender.equals("Male")){
						avatarCenter.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarCenter.setImageResource(R.drawable.dgfemalehero101);
					}
					textBox.animateText("");
					nameBox.setTypeface(null, Typeface.NORMAL);
					nameBox.setGravity(Gravity.LEFT);
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[branchStory - 4]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[branchStory - 4]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 0;
					branch02 = true;
					story = 6;
					inputMode = 0;
					savePrefInt("InputMode", inputMode);
				}
			}
			else if(branch == 2){
				stories = res.getStringArray(R.array.DG_Story_Branch_03);
				if(branchStory == 0){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor03);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 1;
					inputMode = 0;
					savePrefInt("InputMode", inputMode);
				}
				else if(branchStory == 1){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor04);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero104);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero104);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 2;
				}
				else if(branchStory == 2){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor05);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 3;
				}
				else if(branchStory == 3){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 4;
				}
				else if(branchStory == 4){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor05);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 5;
				}
				else if(branchStory == 5){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 6;
				}
				else if(branchStory == 6){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor02);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 7;
				}
				else if(branchStory == 7){
					disableEvent = true;
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					nameBox.clearBuffer();
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setTypeface(null, Typeface.NORMAL);
					textBox.animateText("");
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[branchStory]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[branchStory]);
					}
					setCharVisibility(true, false, true);
					avatarRight.setImageResource(R.drawable.dgjanitor02);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarLeft.clearColorFilter();
					avatarRight.clearColorFilter();
					
					AlertDialog.Builder builder = new AlertDialog.Builder(DetectiveGame.this).setTitle(stories[branchStory])
					.setItems(R.array.DG_decision_branch_03, new DialogInterface.OnClickListener() {
								
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							decisions = res.getStringArray(R.array.DG_decision_branch_03_result);
							tempDecision = which;
							TypeWriter nameBox = (TypeWriter) findViewById(R.id.textBox);
							ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
							ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
							TypeWriter textBox = (TypeWriter) findViewById(R.id.conversation);
							if(tempDecision == 0){
								avatarRight.setImageResource(R.drawable.dgjanitor02);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero102);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero102);
								}
								socialPoints += 1;
							}
							else if(tempDecision == 1){
								avatarRight.setImageResource(R.drawable.dgjanitor02);
								if(gender.equals("Male")){
									avatarLeft.setImageResource(R.drawable.dgmalehero109);
								}
								else if(gender.equals("Female")){
									avatarLeft.setImageResource(R.drawable.dgfemalehero109);
								}
								socialPoints += 0;
							}
							avatarLeft.clearColorFilter();
							avatarRight.setColorFilter(R.color.TransBlack);
							nameBox.clearBuffer();
							nameBox.setTypeface(null, Typeface.BOLD);
							nameBox.setText(charName);
							if(textSpeed == 0){
								textBox.clearBuffer();
								textBox.setText(decisions[tempDecision]);
							}
							else{
								textBox.setCharacterDelay(textSpeed);
								textBox.animateText(decisions[tempDecision]);
							}
							inputMode = 1;
							savePrefInt("InputMode", inputMode);
							savePrefInt("TempDecision", tempDecision);
							savePrefInt("BranchStory", branchStory);
							branchStory = 8;
							}
						}
					);
					if(inputMode == 0){
						AlertDialog decision = builder.create();
						decision.show();
					}
					else{
						decisions = res.getStringArray(R.array.DG_decision_branch_03_result);
						if(tempDecision == 0){
							avatarRight.setImageResource(R.drawable.dgjanitor01);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero102);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero102);
							}
						}
						else if(tempDecision == 1){
							avatarRight.setImageResource(R.drawable.dgjanitor06);
							if(gender.equals("Male")){
								avatarLeft.setImageResource(R.drawable.dgmalehero109);
							}
							else if(gender.equals("Female")){
								avatarLeft.setImageResource(R.drawable.dgfemalehero109);
							}
						}
						avatarLeft.clearColorFilter();
						avatarRight.setColorFilter(R.color.TransBlack);
						nameBox.clearBuffer();
						nameBox.setTypeface(null, Typeface.BOLD);
						nameBox.setText(charName);
						if(textSpeed == 0){
							textBox.clearBuffer();
							textBox.setText(decisions[tempDecision]);
						}
						else{
							textBox.setCharacterDelay(textSpeed);
							textBox.animateText(decisions[tempDecision]);
						}
						savePrefInt("BranchStory", branchStory);
						branchStory = 8;
					}
					saveStory(story, plot, chapter);
				}
				else if(branchStory == 8){
					disableEvent = false;
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					decisions = res.getStringArray(R.array.DG_decision_branch_03_result);
					if(tempDecision == 0){
						avatarRight.setImageResource(R.drawable.dgjanitor05);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero102);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero102);
						}
					}
					else if(tempDecision == 1){
						avatarRight.setImageResource(R.drawable.dgjanitor02);
						if(gender.equals("Male")){
							avatarLeft.setImageResource(R.drawable.dgmalehero109);
						}
						else if(gender.equals("Female")){
							avatarLeft.setImageResource(R.drawable.dgfemalehero109);
						}
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(decisions[tempDecision + 2]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(decisions[tempDecision + 2]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 9;
				}
				else if(branchStory == 9){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 1]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 10;
				}
				else if(branchStory == 10){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor05);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero101);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 1]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 11;
				}
				else if(branchStory == 11){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 1]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 12;
				}
				else if(branchStory == 12){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor02);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 1]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 13;
				}
				else if(branchStory == 13){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor06);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 1]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 14;
				}
				else if(branchStory == 14){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor02);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero111);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero111);
					}
					avatarRight.clearColorFilter();
					avatarLeft.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.RIGHT);
					nameBox.setText("Janitor");
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 1]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 15;
				}
				else if(branchStory == 15){
					setCharVisibility(true, false, true);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					avatarRight.setImageResource(R.drawable.dgjanitor01);
					if(gender.equals("Male")){
						avatarLeft.setImageResource(R.drawable.dgmalehero102);
					}
					else if(gender.equals("Female")){
						avatarLeft.setImageResource(R.drawable.dgfemalehero102);
					}
					avatarLeft.clearColorFilter();
					avatarRight.setColorFilter(R.color.TransBlack);
					nameBox.clearBuffer();
					nameBox.setTypeface(null, Typeface.BOLD);
					nameBox.setGravity(Gravity.LEFT);
					nameBox.setText(charName);
					if(textSpeed == 0){
						textBox.clearBuffer();
						textBox.setText(stories[branchStory - 1]);
					}
					else{
						textBox.setCharacterDelay(textSpeed);
						textBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 16;
				}
				else if(branchStory == 16){
					setCharVisibility(false, true, false);
					currentView.setBackgroundResource(R.drawable.dg_school_library_day);
					if(gender.equals("Male")){
						avatarCenter.setImageResource(R.drawable.dgmalehero101);
					}
					else if(gender.equals("Female")){
						avatarCenter.setImageResource(R.drawable.dgfemalehero101);
					}
					textBox.animateText("");
					nameBox.setTypeface(null, Typeface.NORMAL);
					nameBox.setGravity(Gravity.LEFT);
					if(textSpeed == 0){
						nameBox.clearBuffer();
						nameBox.setText(stories[branchStory - 1]);
					}
					else{
						nameBox.setCharacterDelay(textSpeed);
						nameBox.animateText(stories[branchStory - 1]);
					}
					saveStory(story, plot, chapter);
					savePrefInt("BranchStory", branchStory);
					branchStory = 0;
					branch03 = true;
					story = 6;
					inputMode = 0;
					savePrefInt("InputMode", inputMode);
				}
			}
		}
	}
	public void setCharVisibility(boolean left, boolean center, boolean right){
		final ImageView avatarLeft = (ImageView) findViewById(R.id.avatarLeft);
		final ImageView avatarCenter = (ImageView) findViewById(R.id.avatarCenter);
		final ImageView avatarRight = (ImageView) findViewById(R.id.avatarRight);
		if(left){
			avatarLeft.setVisibility(View.VISIBLE);
		}
		else{
			avatarLeft.setVisibility(View.INVISIBLE);
		}
		if(center){
			avatarCenter.setVisibility(View.VISIBLE);
		}
		else{
			avatarCenter.setVisibility(View.INVISIBLE);
		}
		if(right){
			avatarRight.setVisibility(View.VISIBLE);
		}
		else{
			avatarRight.setVisibility(View.INVISIBLE);
		}
	}
	
	public void savePref(String pref, String value){
		gameData = getSharedPreferences(prefName, MODE_PRIVATE);
		SharedPreferences.Editor editor = gameData.edit();
		editor.putString(pref, value);
		editor.commit();
	}
	
	public void savePrefInt(String pref, int value){
		gameData = getSharedPreferences(prefName, MODE_PRIVATE);
		SharedPreferences.Editor editor = gameData.edit();
		editor.putInt(pref, value);
		editor.commit();
	}
	
	public void saveStory(int story, int plot, int chapter){
		gameData = getSharedPreferences(prefName, MODE_PRIVATE);
		SharedPreferences.Editor editor = gameData.edit();
		editor.putInt("Story", story);
		editor.putInt("Plot", plot);
		editor.putInt("Chapter", chapter);
		editor.commit();
	}
	
	public void changeBackground(int id){
		View currentView = (View) findViewById(R.id.backg);
		final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
		final Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);

		currentView.startAnimation(fadeOut);
		if (id == Color.BLACK)
			currentView.setBackgroundColor(id);
		else
			currentView.setBackgroundResource(id);
		currentView.startAnimation(fadeIn);
	}
	
	public void option(){
		setContentView(R.layout.activity_detective_game_options);
		RadioGroup textSpd = (RadioGroup) findViewById(R.id.textSpeed);
		RadioButton slow = (RadioButton) findViewById(R.id.slow);
		RadioButton fast = (RadioButton) findViewById(R.id.fast);
		RadioButton instant = (RadioButton) findViewById(R.id.instant);
		if(textSpeed == 50){
			slow.setChecked(true);
		}
		else if(textSpeed == 10){
			fast.setChecked(true);
		}
		else if(textSpeed == 0){
			instant.setChecked(true);
		}
		textSpd.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(RadioGroup txtSpd, int which) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor;
				switch(which){
				case R.id.slow:{
					textSpeed = 50;
					gameSettings = getSharedPreferences(prefName2, MODE_PRIVATE);
					editor = gameSettings.edit();
					editor.putInt("TextSpeed", textSpeed);
					editor.commit();
					break;
					}
				case R.id.fast:{
					textSpeed = 10;
					gameSettings = getSharedPreferences(prefName2, MODE_PRIVATE);
					editor = gameSettings.edit();
					editor.putInt("TextSpeed", textSpeed);
					editor.commit();
					break;
					}
				case R.id.instant:{
					textSpeed = 0;
					gameSettings = getSharedPreferences(prefName2, MODE_PRIVATE);
					editor = gameSettings.edit();
					editor.putInt("TextSpeed", textSpeed);
					editor.commit();
					break;
					}
				}
			}
		});
		Button delButton = (Button) findViewById(R.id.delButton);
		delButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View vi) {
				// TODO Auto-generated method stub
				File savedGame = new File(getApplicationContext().getFilesDir().getParentFile().getPath() + "/shared_prefs/" + prefName + ".xml");
				boolean deleted = savedGame.delete();
				if(deleted){
					Toast.makeText(DetectiveGame.this, "Saved data deleted. Returning to Menu..", Toast.LENGTH_LONG).show();
					finish();
				}
				else{
					Toast.makeText(DetectiveGame.this, "Saved data not found.", Toast.LENGTH_LONG).show();
				}
			}
			
		});
		
		Button aboutGame = (Button) findViewById(R.id.aboutButton);
		aboutGame.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View vi) {
				// TODO Auto-generated method stub
				setContentView(R.layout.activity_detective_game_about);
			}
			
		});
	}
	public void result(){
		int ipa1 = 0;
		int ipa2 = 0;
		int ips1 = 0;
		int ips2 = 0;
		setContentView(R.layout.activity_detective_game_result);
		TextView resultView = (TextView) findViewById(R.id.resultView);
		resultView.setText("Social Skills:\n");
		if(socialPoints >= 0 && socialPoints < 4){
			ips1 = 40;
			ips2 = 10;
			resultView.setText(resultView.getText() + "It seems you have a difficulty in your social life. Though it is quite bad, you can still improve it. You have low social skills.\n\n");
		}
		else if(socialPoints >= 4 && socialPoints < 8){
			ips1 = 25;
			ips2 = 25;
			resultView.setText(resultView.getText() + "You are quite sociable. You can get along with others well. You have average social skills.\n\n");
		}
		else if(socialPoints >= 8 && socialPoints < 11){
			ips1 = 10;
			ips2 = 40;
			resultView.setText(resultView.getText() + "You can easily adept to any social circumstances. You have high social skills.\n\n");
		}
		resultView.setText(resultView.getText() + "Analytical Skills:\n");
		if(analyticalPoints >= 0 && analyticalPoints < 4){
			ipa1 = 10;
			ipa2 = 40;
			resultView.setText(resultView.getText() + "You seem to have difficulty in analyzing a situation. You have low analytical skills.");
		}
		else if(analyticalPoints >= 4 && analyticalPoints < 8){
			ipa1 = 25;
			ipa2 = 25;
			resultView.setText(resultView.getText() + "You have good thinking ability and you can assess your surrounding quickly, as well as taking good decision from it. You have average analytical skills.");
		}
		else if(analyticalPoints >= 8 && analyticalPoints < 12){
			ipa1 = 40;
			ipa2 = 10;
			resultView.setText(resultView.getText() + "You can analyze things well, and you have a sharp mind. You have high analytical skills.");
		}
		gameResult = getSharedPreferences(prefName3, MODE_PRIVATE);
		SharedPreferences.Editor editor = gameResult.edit();
		editor.putInt("ipa1", ipa1);
		editor.putInt("ipa2", ipa2);
		editor.putInt("ips1", ips1);
		editor.putInt("ips2", ips2);
		editor.commit();
		resultView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View vi) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
	}
	

	@Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (disableEvent)
        {
        	Toast.makeText(DetectiveGame.this, "You cannot go back at this point.", Toast.LENGTH_SHORT).show();
            // do nothing
        }
        else
        {
            super.onBackPressed();
        }
    }


}