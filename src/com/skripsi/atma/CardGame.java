package com.skripsi.atma;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;


public class CardGame extends Activity {
	//check is used to determine whether there is a saved game or not.
	//backdisable is used to determine whether the back button can be pressed or not.
	//pturncheck is used to check if it's player's turn, eturncheck is for enemy.
	public boolean check, tournamentcheck = false, backdisable = false, pturncheck = false, eturncheck = false;
	//myDbHelper is the instance of DatabaseHelper class.
	public DatabaseHelper myDbHelper = new DatabaseHelper(this);
	//k is used to determine the saved town.
	//ID is used to determine the player's ID.
	//cardorder is used to determine the order of card's in deck (1st, 2nd or 3rd).
	public int k = 0, ID = 1, cardorder = 0;
	//String containing the card's details.
	public String cname, cdesc, crarity, cabilityname, cabilitydesc, cabilitytype, cfilename;
	//Integer containing the card's details.
	public int chp, cap, cabilityid, cquantity;
	//Integer containing each card's id in deck and the dummies.
	public int first, second, third, firstcard, secondcard, thirdcard;
	//Integer as temporary variables for each card's id and quantity.
	public int temp1, temp2, temp3, tempquantity1, tempquantity2, tempquantity3, temp;
	//Integer to keep track the flow of the battle.
	public int phase = 1, turn;
    //integer containing the player's stats.
	public int menang1 = 0, kalah1 = 0, seri1 = 0, story;
	//integer containing the turn determining result and determining win or lose.
	public int pturnnum = 1, eturnnum = 1, plose = 0, elose = 0;
	//first index	: player and enemy.
	//second index	: 
	//[0]-[2]	: id kartu
	//[3]-[5]	: nyawa kartu
	//[6]-[8]	: serangan kartu
	//[9]-[11]	: kemampuan kartu
	public int[][] p = new int[2][12];
	//first index	: player and enemy.
	//second index	:
	//[0]-[2]	: gambar kartu
	//[3]-[5]	: nama kartu
	//[6]-[8]	: nama kemampuan
	//[9]-[11]  : deskripsi kemampuan
	public String[][] pcard = new String[2][12];
	//first index	: player and enemy.
	//second index	:
	//[0]-[2]	: nama pemain
	public String[] pnm = new String[2];
	public String pname;
	//integer for looping to display the card inventory listview.
	public int inventory;
	//integer to store ability's id and active ability's usage.
	public int abi, puse = 0, euse = 0;
	//boolean to check double damage or not.
	public boolean doubledmg = false;
	//integer to check whether there is anything to check or not
	public int tapthedialog;
	//boolean to check whether the battle is from arena or tournament
	public boolean fromarena;
	//boolean to check whether the battle ends in win, lose, or draw
	public boolean win = false, lose = false, draw = false;
	//String to store the new card name
	public String newcardname, newcardname1, newcardname2;
	//integer to check the tutorial page
	public int tutpage;
	//integer to check the tournament page
	public int tournamentpage;
	//integer to store the enemy id and his cards.
	public int enemyid = 1, ecardid1 = 1, ecardid2 = 1, ecardid3 = 1;
	//shared preferences for summary.
	private SharedPreferences result;
	private String prefName = "CardGameResult";
	//float to store player's win, lose, draw percentage.
	public float mngpersen, klhpersen, srpersen;
	//boolean to check whether the game has completed or not.
	public boolean endgame = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_game_menu);
		
		//set to landscape and fullscreen mode.
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

	    //call the main menu
	    cardgamemainmenu();
	}

	public void cardgamemainmenu(){
		//set the layout to card_game_menu.xml.
		setContentView(R.layout.activity_card_game_menu);
	
		//access the database. 
	    try {   
	    	myDbHelper.createDataBase(); 
	    } 
	    catch (IOException ioe) {
	        throw new Error("Unable to create database");
	    }
	    try {
	        myDbHelper.openDataBase();
	    }
	    catch(SQLException sqle){
	        throw sqle;
	    }
	    
	    //create button objects.
	    Button storymode = (Button) findViewById(R.id.button1);
	    Button tournamentmode = (Button) findViewById(R.id.button2);
	    Button tutorial = (Button) findViewById(R.id.button4);
	    
	    //check whether there is already a record inserted before.
		String[] pilihtabel = {"nama", "cerita"};
	    Cursor c = myDbHelper.selectrecord("PEMAIN", pilihtabel, null, null, null, null, null);
	    if(c!=null){
	    	if(c.moveToFirst()){
	    		check = true;
	    		story = c.getInt(c.getColumnIndex("cerita"));
	    		if(story >= 43)
	    			tournamentcheck = true;
	    		else
	    			tournamentcheck = false;
	    	}
	    	else{
	    		check = false;
	    		tournamentcheck = false;
	    	}
	    }
	    c.close();
	    
	    //set on click listener for storymode.
	    storymode.setOnClickListener(new OnClickListener(){	
			@Override
			public void onClick(View v) {
			    //if there is no record, go to name input for new story; if there is any, go to town to continue story.
			    if(check == false){
			    	Toast.makeText(CardGame.this, "No saved game found. Creating a new story..", Toast.LENGTH_SHORT).show();
			    	cardgamenameinput();
			    }
			    else{
			    	Toast.makeText(CardGame.this, "Saved game found. Loading saved game..", Toast.LENGTH_SHORT).show();
			    	cardgametown();
			    }
			};
		});		
	    
	    //set on click listener for tournamentmode.
	    tournamentmode.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(tournamentcheck == false)
					Toast.makeText(CardGame.this, "No playable deck is present. Please choose the story mode first instead.", Toast.LENGTH_SHORT).show();
				else{
					tournamentpage = 0;
					tournament();
				}	
			};
		});
	    
	    //set on click listener for tutorial.
	    tutorial.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				tutpage = 0;
				tutorial();
			};
		});
	    
	}
	
	public void tournament(){
		//disable back button.
		backdisable=true;		
						
		//set the layout to card_game_name_conversation.xml.
		setContentView(R.layout.activity_card_game_conversation);
		
		//create objects for search.
		ImageView bg = (ImageView) findViewById(R.id.imageView1);
		ImageView avatar = (ImageView) findViewById(R.id.avatarCenter);
		TextView tstory = (TextView) findViewById(R.id.textView1);
		TextView tplace = (TextView) findViewById(R.id.textView2);
		TextView ttown = (TextView) findViewById(R.id.textView3);
		final TextView dialog = (TextView) findViewById(R.id.textView4);
				
		bg.setImageResource(R.drawable.tournament);
		avatar.setVisibility(View.INVISIBLE);
		tplace.setText("Tournament");
		ttown.setText("");
		tstory.setText("");
		
		tournamentchat();

		//set on click listener for dialog in tournament
		//set on click listener for tutorial.
	    dialog.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				tournamentchat();
			};
		});
	}
	
	
	public void tournamentchat(){
		TextView dialog = (TextView) findViewById(R.id.textView4);
		Random rnd = new Random();
		switch(tournamentpage){
		case 0:{
			dialog.setText("  Welcome to Baratayuda tournament!");
			tournamentpage++;
			break;
		}
		case 1:{
			dialog.setText("  This place will offer you the most exciting battle\n  in the game!");
			tournamentpage++;
			break;
		}
		case 2:{
			dialog.setText("  You will get rare or even legendary cards by winning\n  the tournament!");
			tournamentpage++;
			break;
		}
		case 3:{
			dialog.setText("  There will be 5 consecutive battles...\n  so are you ready?");
			tournamentpage++;
			break;
		}
		case 4:{
			dialog.setText("  Let's begin the first round!");
			tournamentpage++;
			break;
		}
		case 5:{
			fromarena = false;
			enemyid = 27;
			if(story < 83){
				ecardid1 = rnd.nextInt(12) + 19;
				ecardid2 = rnd.nextInt(12) + 19;
				ecardid3 = rnd.nextInt(12) + 19;
			}
			else if(story >= 83 && story < 154){
				ecardid1 = rnd.nextInt(20) + 11;
				ecardid2 = rnd.nextInt(20) + 11;
				ecardid3 = rnd.nextInt(20) + 11;
			}
			else if(story >= 154 && story < 216){
				ecardid1 = rnd.nextInt(26) + 5;
				ecardid2 = rnd.nextInt(26) + 5;
				ecardid3 = rnd.nextInt(26) + 5;
			}
			else if(story >= 216){
				ecardid1 = rnd.nextInt(30) + 1;
				ecardid2 = rnd.nextInt(30) + 1;
				ecardid3 = rnd.nextInt(30) + 1;
			}
			ContentValues updatevalues = new ContentValues();
		    updatevalues.put("kartu1", ecardid1);
		    updatevalues.put("kartu2", ecardid2);
		    updatevalues.put("kartu3", ecardid3);
		    long n = DatabaseHelper.updatetable("LAWAN", updatevalues, "_id="+enemyid);
		    if(n!=-1){
	        	System.out.println("UPDATE SUCCESS");
	        }	
	        else
	        	System.out.println("UPDATE FAILED");
			cardbattle();
			break;
		}
		case 6:{
			if(win == true){
				dialog.setText("  Wonderful fight!");
			}
			else
				if(draw == true){
					dialog.setText("  Well... this tournament won't accept\n  any draw. So that means you're out!\n  But don't give up and come back again!");
				}
				else
					if(lose == true){
						dialog.setText("  Don't give up and come back again if you've gotten stronger!");
					}
			tournamentpage++;
			break;
		}
		case 7:{
			if(win == true){
				dialog.setText("  Alright, let's move on to the next battle!");
				tournamentpage++;
			}
			else{
				cardgamemainmenu();
			}		
			break;
		}
		case 8:{
			dialog.setText("  Are you ready?\n  Let's begin the fight!");
			tournamentpage++;
			break;
		}
		case 9:{
			fromarena = false;
			enemyid = 28;
			if(story < 83){
				ecardid1 = rnd.nextInt(12) + 19;
				ecardid2 = rnd.nextInt(8) + 11;
				ecardid3 = rnd.nextInt(12) + 19;
			}
			else if(story >= 83 && story < 154){
				ecardid1 = rnd.nextInt(8) + 11;
				ecardid2 = rnd.nextInt(8) + 11;
				ecardid3 = rnd.nextInt(20) + 11;
			}
			else if(story >= 154 && story < 216){
				ecardid1 = rnd.nextInt(8) + 11;
				ecardid2 = rnd.nextInt(8) + 11;
				ecardid3 = rnd.nextInt(8) + 11;
			}
			else if(story >= 216){
				ecardid1 = rnd.nextInt(6) + 5;
				ecardid2 = rnd.nextInt(8) + 11;
				ecardid3 = rnd.nextInt(8) + 11;
			}
			ContentValues updatevalues = new ContentValues();
		    updatevalues.put("kartu1", ecardid1);
		    updatevalues.put("kartu2", ecardid2);
		    updatevalues.put("kartu3", ecardid3);
		    long n = DatabaseHelper.updatetable("LAWAN", updatevalues, "_id="+enemyid);
		    if(n!=-1){
	        	System.out.println("UPDATE SUCCESS");
	        }	
	        else
	        	System.out.println("UPDATE FAILED");
			cardbattle();
			break;
		}
		case 10:{
			if(win == true){
				dialog.setText("  Amazing!");
			}
			else
				if(draw == true){
					dialog.setText("  Well... this tournament won't accept\n  any draw. So that means you're out!\n  But don't give up and come back again!");
				}
				else
					if(lose == true){
						dialog.setText("  Don't give up and come back again if you've gotten stronger!");
					}
			tournamentpage++;
			break;
		}
		case 11:{
			if(win == true){
				dialog.setText("  The battle has just begun!");
				tournamentpage++;
			}
			else{
				cardgamemainmenu();
			}		
			break;
		}
		case 12:{
			dialog.setText("  We move on to the third battle!\n  Ready or not, here it comes!");
			tournamentpage++;
			break;
		}
		case 13:{
			fromarena = false;
			enemyid = 29;
			if(story < 83){
				ecardid1 = rnd.nextInt(12) + 19;
				ecardid2 = rnd.nextInt(8) + 11;
				ecardid3 = rnd.nextInt(8) + 11;
			}
			else if(story >= 83 && story < 154){
				ecardid1 = rnd.nextInt(8) + 11;
				ecardid2 = rnd.nextInt(8) + 11;
				ecardid3 = rnd.nextInt(8) + 11;
			}
			else if(story >= 154 && story < 216){
				ecardid1 = rnd.nextInt(6) + 5;
				ecardid2 = rnd.nextInt(8) + 11;
				ecardid3 = rnd.nextInt(6) + 5;
			}
			else if(story >= 216){
				ecardid1 = rnd.nextInt(4) + 1;
				ecardid2 = rnd.nextInt(6) + 5;
				ecardid3 = rnd.nextInt(6) + 5;
			}
			ContentValues updatevalues = new ContentValues();
		    updatevalues.put("kartu1", ecardid1);
		    updatevalues.put("kartu2", ecardid2);
		    updatevalues.put("kartu3", ecardid3);
		    long n = DatabaseHelper.updatetable("LAWAN", updatevalues, "_id="+enemyid);
		    if(n!=-1){
	        	System.out.println("UPDATE SUCCESS");
	        }	
	        else
	        	System.out.println("UPDATE FAILED");
			cardbattle();
			break;
		}
		case 14:{
			if(win == true){
				dialog.setText("  Simply magnificent!");
			}
			else
				if(draw == true){
					dialog.setText("  Well... this tournament won't accept\n  any draw. So that means you're out!\n  But don't give up and come back again!");
				}
				else
					if(lose == true){
						dialog.setText("  Don't give up and come back again if you've gotten stronger!");
					}
			tournamentpage++;
			break;
		}
		case 15:{
			if(win == true){
				dialog.setText("  But there are still two battles left!");
				tournamentpage++;
			}
			else{
				cardgamemainmenu();
			}		
			break;
		}
		case 16:{
			dialog.setText("  Can you handle the next battle?\n  Let's begin the fourth battle!");
			tournamentpage++;
			break;
		}
		case 17:{
			fromarena = false;
			enemyid = 30;
			if(story < 83){
				ecardid1 = rnd.nextInt(12) + 19;
				ecardid2 = rnd.nextInt(12) + 19;
				ecardid3 = rnd.nextInt(6) + 5;
			}
			else if(story >= 83 && story < 154){
				ecardid1 = rnd.nextInt(8) + 11;
				ecardid2 = rnd.nextInt(6) + 5;
				ecardid3 = rnd.nextInt(12) + 19;
			}
			else if(story >= 154 && story < 216){
				ecardid1 = rnd.nextInt(6) + 5;
				ecardid2 = rnd.nextInt(6) + 5;
				ecardid3 = rnd.nextInt(6) + 5;
			}
			else if(story >= 216){
				ecardid1 = rnd.nextInt(4) + 1;
				ecardid2 = rnd.nextInt(4) + 1;
				ecardid3 = rnd.nextInt(6) + 5;
			}
			ContentValues updatevalues = new ContentValues();
		    updatevalues.put("kartu1", ecardid1);
		    updatevalues.put("kartu2", ecardid2);
		    updatevalues.put("kartu3", ecardid3);
		    long n = DatabaseHelper.updatetable("LAWAN", updatevalues, "_id="+enemyid);
		    if(n!=-1){
	        	System.out.println("UPDATE SUCCESS");
	        }	
	        else
	        	System.out.println("UPDATE FAILED");
			cardbattle();
			break;
		}
		case 18:{
			if(win == true){
				dialog.setText("  Wow! Just WOW!");
			}
			else
				if(draw == true){
					dialog.setText("  Well... this tournament won't accept\n  any draw. So that means you're out!\n  But don't give up and come back again!");
				}
				else
					if(lose == true){
						dialog.setText("  Don't give up and come back again if you've gotten stronger!");
					}
			tournamentpage++;
			break;
		}
		case 19:{
			if(win == true){
				dialog.setText("  This is it!\n  This is the final battle for this tournament!");
				tournamentpage++;
			}
			else{
				cardgamemainmenu();
			}		
			break;
		}
		case 20:{
			dialog.setText("  Are you ready for it?\n  Let the final battle begins!");
			tournamentpage++;
			break;
		}
		case 21:{
			fromarena = false;
			enemyid = 31;
			if(story < 83){
				ecardid1 = rnd.nextInt(8) + 11;
				ecardid2 = rnd.nextInt(8) + 11;
				ecardid3 = rnd.nextInt(8) + 11;
			}
			else if(story >= 83 && story < 154){
				ecardid1 = rnd.nextInt(6) + 5;
				ecardid2 = rnd.nextInt(6) + 5;
				ecardid3 = rnd.nextInt(6) + 5;
			}
			else if(story >= 154 && story < 216){
				ecardid1 = rnd.nextInt(4) + 1;
				ecardid2 = rnd.nextInt(6) + 5;
				ecardid3 = rnd.nextInt(6) + 5;
			}
			else if(story >= 216){
				ecardid1 = rnd.nextInt(4) + 1;
				ecardid2 = rnd.nextInt(4) + 1;
				ecardid3 = rnd.nextInt(4) + 1;
			}
			ContentValues updatevalues = new ContentValues();
		    updatevalues.put("kartu1", ecardid1);
		    updatevalues.put("kartu2", ecardid2);
		    updatevalues.put("kartu3", ecardid3);
		    long n = DatabaseHelper.updatetable("LAWAN", updatevalues, "_id="+enemyid);
		    if(n!=-1){
	        	System.out.println("UPDATE SUCCESS");
	        }	
	        else
	        	System.out.println("UPDATE FAILED");
			cardbattle();
			break;
		}
		case 22:{
			if(win == true){
				dialog.setText("  Congratulations!");
			}
			else
				if(draw == true){
					dialog.setText("  Well... this tournament won't accept\n  any draw. So that means you're out!\n  But don't give up and come back again!");
				}
				else
					if(lose == true){
						dialog.setText("  Don't give up and come back again if you've gotten stronger!");
					}
			tournamentpage++;
			break;
		}
		case 23:{
			if(win == true){
				dialog.setText("  We have a new winner of this tournament!");
				tournamentpage++;
			}
			else{
				cardgamemainmenu();
			}		
			break;
		}
		case 24:{
			dialog.setText("  With this, you'll be rewarded 3 rare cards!\n  But there's also a chance that you'll get\n  legendary cards!");
			tournamentpage++;
			break;
		}
		case 25:{
			dialog.setText("  So, what would they be?");
			tournamentpage++;
			break;
		}
		case 26:{
			int newcardid = getnewcard();
			int newcardid1 = getnewcard();
			int newcardid2 = getnewcard();
	        String[] pilihtabel = {"nama"};
			String[] where = {""};
			where[0] = Integer.toString(newcardid);
			Cursor c = myDbHelper.selectrecord("KARTU", pilihtabel, "_id" + "=?", where, null, null, null);
			if(c!=null){
				if(c.moveToFirst()){
		    		do{
		    			newcardname = c.getString(c.getColumnIndex("nama"));
		    		}
		    		while(c.moveToNext());
				}
			}
			c.close();
			where[0] = Integer.toString(newcardid1);
			c = myDbHelper.selectrecord("KARTU", pilihtabel, "_id" + "=?", where, null, null, null);
			if(c!=null){
				if(c.moveToFirst()){
		    		do{
		    			newcardname1 = c.getString(c.getColumnIndex("nama"));
		    		}
		    		while(c.moveToNext());
				}
			}
			c.close();
			where[0] = Integer.toString(newcardid2);
			c = myDbHelper.selectrecord("KARTU", pilihtabel, "_id" + "=?", where, null, null, null);
			if(c!=null){
				if(c.moveToFirst()){
		    		do{
		    			newcardname2 = c.getString(c.getColumnIndex("nama"));
		    		}
		    		while(c.moveToNext());
				}
			}
			c.close();
			dialog.setText("  Congratulations! You got " + newcardname + ", " + newcardname1 + ", and " + newcardname2 + "!");
			tournamentpage++;
			break;
		}
		case 27:{
			dialog.setText("  Thanks for participating!\n  We hope to see you again soon!");
			tournamentpage++;
			break;
		}
		case 28:{
			cardgamemainmenu();
		}
		}
	}
	
	
	public void tutorial(){
		setContentView(R.layout.activity_card_game_tutorial);
		final ImageView tut = (ImageView) findViewById(R.id.imageView1);
		tut.setImageResource(R.drawable.help1);
		
		//set on click listener for tutorial.
	    tut.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				switch(tutpage){
				case 0:{
					tut.setImageResource(R.drawable.help2);
					tutpage++;
					break;
				}
				case 1:{
					tut.setImageResource(R.drawable.help3);
					tutpage++;
					break;
				}
				case 2:{
					tut.setImageResource(R.drawable.help4);
					tutpage++;
					break;
				}
				case 3:{
					tut.setImageResource(R.drawable.help5);
					tutpage++;
					break;
				}
				case 4:{
					tut.setImageResource(R.drawable.help6);
					tutpage++;
					break;
				}
				case 5:{
					tut.setImageResource(R.drawable.help7);
					tutpage++;
					break;
				}
				case 6:{
					tut.setImageResource(R.drawable.help8);
					tutpage++;
					break;
				}
				case 7:{
					tut.setImageResource(R.drawable.help9);
					tutpage++;
					break;
				}
				case 8:{
					cardgamemainmenu();
					break;
				}
				}
			};
		});
	}
	
	public void disclaimer(){
		setContentView(R.layout.activity_card_game_tutorial);
		final ImageView dis = (ImageView) findViewById(R.id.imageView1);
		TextView dicla = (TextView) findViewById(R.id.textView3);
		dis.setImageResource(R.drawable.cgdisclaimer);
		
		dicla.setText("");
		//set on click listener for tutorial.
	    dis.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				search();
			};
		});
	}
	
	public void cardgamenameinput(){
		//set the layout to card_game_name_input.xml.
		setContentView(R.layout.activity_card_game_name_input);
        
		//create objects for name input.
        final EditText inputname = (EditText) findViewById(R.id.nameInput);
		TextView done = (TextView) findViewById(R.id.done);
		
		//set on click listener for done button.
		done.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(inputname.getText().toString().trim().length() == 0){
					Toast.makeText(CardGame.this, "Please enter the character's name..", Toast.LENGTH_SHORT).show();
				}
				else{
					//insert the name from user with the default values for other columns to table "PEMAIN".
					String name = inputname.getText().toString();
					ContentValues initialValues = new ContentValues();
			        initialValues.put("nama", name);
			        initialValues.put("jlh_menang", 0);
			        initialValues.put("jlh_kalah", 0);
			        initialValues.put("jlh_seri", 0);
			        initialValues.put("kota", k);
			        initialValues.put("cerita", 0);
			        long n = myDbHelper.insertrecord("PEMAIN", null, initialValues);
			        //if the insert is successful, go to town to begin the story; if it isn't, print an error message.
			        if(n!=-1)
			        	System.out.println("INSERT SUCCESS");
			        else
			        	System.out.println("INSERT FAILED");
			        disclaimer();			        
				}
			}		
		});
	}
	
	public void cardgametown(){
		//set the layout to card_game_name_input.xml.
		setContentView(R.layout.activity_card_game_town);
		
		//create objects for cardgametown.
		ImageView bg = (ImageView) findViewById(R.id.imageView1);
		TextView search = (TextView) findViewById(R.id.textView5);
		TextView arena = (TextView) findViewById(R.id.textView6);
		TextView map = (TextView) findViewById(R.id.textView7);
		TextView deck = (TextView) findViewById(R.id.textView8);
		TextView stats = (TextView) findViewById(R.id.textView9);
		TextView back = (TextView) findViewById(R.id.textView10);
		TextView town = (TextView) findViewById(R.id.textView3);
		Resources res = getResources();
		String[] kota = res.getStringArray(R.array.Kota);
		
		//set k to the current town from the database.
		String[] pilihtabel = {"kota", "cerita"};
	    Cursor c = myDbHelper.selectrecord("PEMAIN", pilihtabel, null, null, null, null, null);
	    if(c!=null){
	    	if(c.moveToFirst()){
	    		do{
	    			k = c.getInt(c.getColumnIndex("kota"));
	    			story = c.getInt(c.getColumnIndex("cerita"));
	    		}
	    		while(c.moveToNext());
	    	}
	    }
	    c.close();
		town.setText(kota[k]);
		
	    //set the town background based on k.
		if(k == 0)
			bg.setImageResource(R.drawable.cg_town1_kurukshetra);
		else if(k==1)
			bg.setImageResource(R.drawable.cg_town2_indraprastha);
		else if(k==2)
			bg.setImageResource(R.drawable.cg_town3_hastinapura);
		else if(k==3)
			bg.setImageResource(R.drawable.cg_town4_wardamana);
		else if(k==4)
			bg.setImageResource(R.drawable.cg_town5_pramanakati);
		else if(k==5)
			bg.setImageResource(R.drawable.cg_town6_waranawati);
		else if(k==6)
			bg.setImageResource(R.drawable.cg_town7_wrekastali);
		else if(k==7)
			bg.setImageResource(R.drawable.cg_town8_menara);
		else if(k==8)
			bg.setImageResource(R.drawable.cg_town9_nirvana);
		
		//set on click listener for search.
	    search.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				search();
			};
		});	
	    
	    //set on click listener for arena.
	    arena.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				arena();
			};
		});
	    
	    //set on click listener for map.
	    map.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(story == 130 || story == 298 || story >= 329)
					Toast.makeText(CardGame.this, "You cannot access the map for now...", Toast.LENGTH_SHORT).show();
				else
					map();
			};
		});
	    
	    //set on click listener for deck.
	    deck.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(story < 40)
					Toast.makeText(CardGame.this, "You haven't got any cards...", Toast.LENGTH_SHORT).show();
				else
					deck();
			};
		});
	    
	    //set on click listener for stats.
	    stats.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				stats();
			};
		});	
	    
	    //set on click listener for back.
	    back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				cardgamemainmenu();
			};
		});	
	}
	
	public void search(){		
		//disable back button.
		backdisable=true;		
		
		//set the layout to card_game_name_conversation.xml.
		setContentView(R.layout.activity_card_game_conversation);
		
		//create objects for search.
		ImageView bg = (ImageView) findViewById(R.id.imageView1);
		TextView place = (TextView) findViewById(R.id.textView2);
		TextView town = (TextView) findViewById(R.id.textView3);
		TextView dialog = (TextView) findViewById(R.id.textView4);
		Resources res = getResources();
		String[] kota = res.getStringArray(R.array.Kota);
		
	    //set the town background based on k.
		if(k == 0)
			bg.setImageResource(R.drawable.cg_town1_kurukshetra);
		else if(k==1)
			bg.setImageResource(R.drawable.cg_town2_indraprastha);
		else if(k==2)
			bg.setImageResource(R.drawable.cg_town3_hastinapura);
		else if(k==3)
			bg.setImageResource(R.drawable.cg_town4_wardamana);
		else if(k==4)
			bg.setImageResource(R.drawable.cg_town5_pramanakati);
		else if(k==5)
			bg.setImageResource(R.drawable.cg_town6_waranawati);
		else if(k==6)
			bg.setImageResource(R.drawable.cg_town7_wrekastali);
		else if(k==7)
			bg.setImageResource(R.drawable.cg_town8_menara);
		else if(k==8)
			bg.setImageResource(R.drawable.cg_town9_nirvana);
		
		//set the place to search and the town to the corresponding one.
		place.setText("Search");
		town.setText(kota[k]);
		String[] pilihtabel = {"nama"};
	    Cursor c = myDbHelper.selectrecord("PEMAIN", pilihtabel, null, null, null, null, null);
	    if(c!=null){
	    	if(c.moveToFirst()){
	    		do{
	    			pname = c.getString(c.getColumnIndex("nama"));
	    		}
	    		while(c.moveToNext());
	    	}
	    }
	    c.close();
	    
	    //k=1 s=31-42 a=43-58
	    //k=2 s=59-82 a=83-109
	    //k=3 s=110-129 a=130-153
	    //k=4 s=154-167 a=168-196
	    //k=5 s=197-209 a=210-234
	    //k=6 s=235-254 a=255-281
	    //k=7 s=282-297 a=298-328
	    //k=8 s=329-338 a=339-379
	    if(story >= 31 && k == 0)
	    	nothingtosearch();
		else if (story >= 43 && k <= 1)
			nothingtosearch();
		else if (story >= 83 && k <= 2)
			nothingtosearch();
		else if (story >= 130 && k <= 3)
			nothingtosearch();
		else if (story >= 168 && k <= 4)
			nothingtosearch();
		else if (story >= 210 && k <= 5)
			nothingtosearch();
		else if (story >= 255 && k <= 6)
			nothingtosearch();
		else if (story >= 298 && k <= 7)
			nothingtosearch();
		else if (story >= 339 && k <= 8)
			nothingtosearch();
	    else{
	    	searchconversation();
	    }
	    
		//set on click listener for dialog.
	    dialog.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(story >= 31 && k == 0)
			    	nothingtosearch();
				else if (story >= 43 && k <= 1)
					nothingtosearch();
				else if (story >= 83 && k <= 2)
					nothingtosearch();
				else if (story >= 130 && k <= 3)
					nothingtosearch();
				else if (story >= 168 && k <= 4)
					nothingtosearch();
				else if (story >= 210 && k <= 5)
					nothingtosearch();
				else if (story >= 255 && k <= 6)
					nothingtosearch();
				else if (story >= 298 && k <= 7)
					nothingtosearch();
				else if (story >= 339 && k <= 8)
					nothingtosearch();
			    else{
			    	searchconversation();
			    }
			};
		});			
	}
	

	public void nothingtosearch(){
		ImageView avatar = (ImageView) findViewById(R.id.avatarCenter);
		TextView dialog = (TextView) findViewById(R.id.textView4);
		switch(tapthedialog){
		case 0:{
			avatar.setVisibility(View.INVISIBLE);
			dialog.setText("  There is nothing to search for now..");
			tapthedialog = 1;
			break;
		}
		case 1:{
			tapthedialog = 0;
			cardgametown();
			break;
		}
		}	
	}
	

	public void searchconversation(){
		ImageView bg = (ImageView) findViewById(R.id.imageView1);
		ImageView avatar = (ImageView) findViewById(R.id.avatarCenter);
		TextView dialogtown = (TextView) findViewById(R.id.textView3);
		TextView dialog = (TextView) findViewById(R.id.textView4);
		switch(story){
			case 0: {
				dialogtown.setVisibility(View.INVISIBLE);
				bg.setImageResource(R.drawable.cg_school);
				avatar.setVisibility(View.INVISIBLE);
				dialog.setText("  Inside your classroom...");
				story++;
				break;
			}		
			case 1:{
				avatar.setImageResource(R.drawable.cgmainchar6);
				avatar.setVisibility(View.VISIBLE);
				dialog.setText("  You are not feeling really well today. You feel like your body is\n  ache all the way.");
				story++;
				break;
			}
			case 2:{
				avatar.setVisibility(View.INVISIBLE);
				dialog.setText("  You didn't notice it that you actually fell asleep...");
				story++;
				break;
			}
			case 3:{
				bg.setVisibility(View.INVISIBLE);
				dialog.setText("  ... You are feeling dizzy...");
				story++;
				break;
			}
			case 4:{
				avatar.setImageResource(R.drawable.cgmainchar6);
				avatar.setVisibility(View.VISIBLE);
				dialog.setText("  You try to open your eyes...");
				story++;
				break;
			}
			case 5:{
				avatar.setImageResource(R.drawable.cgmainchar3);
				bg.setImageResource(R.drawable.cg_town1_kurukshetra);
				bg.setVisibility(View.VISIBLE);
				dialog.setText("  You found yourself on a vast grassland...\n  you wondered if this is a dream.");
				story++;
				break;
			}
			case 6:{
				avatar.setImageResource(R.drawable.cgmainchar2);
				dialog.setText("  You decided to take a look around.");
				story++;
				break;
			}
			case 7:{
				avatar.setImageResource(R.drawable.cgmainchar3);
				dialog.setText("  You are surprised that this is not an ordinary grassland...");
				story++;
				break;
			}
			case 8:{
				avatar.setImageResource(R.drawable.cgmainchar2);
				dialog.setText("  You recall a place taught at the Indonesian history subject... \n  Suddenly you realize that this is a battlefield.");
				story++;
				break;
			}
			case 9:{
				dialogtown.setVisibility(View.VISIBLE);
				dialog.setText("  This grassland is Kurukshetra, the battlefield between\n  Pandavas and Kauravas in the Baratayuda story.");
				story++;
				break;
			}
			case 10:{
				dialog.setText("  Soon after remembering it, you witness a lot of soldiers die \n  on the ground... But there is something wrong with it.");
				story++;
				break;
			}
			case 11:{
				avatar.setImageResource(R.drawable.cgmainchar3);
				dialog.setText("  The war is completely frozen... Everyone in the war was just \n  looked like statues.");
				story++;
				break;
			}
			case 12:{
				dialog.setText("  Suddenly, a bright light flashes into your face. When you \n  realize it, everyone on the battlefield is gone into thin air.");
				story++;
				break;
			}
			case 13:{
				bg.setImageResource(R.drawable.cg_town1_kurukshetra);
				avatar.setImageResource(R.drawable.cgmainchar8);
				dialog.setText("  As you don't know what's going on, you decided to take a \n  look around.");
				story++;
				break;
			}
			case 14:{
				avatar.setImageResource(R.drawable.cglady);
				dialog.setText("  Apparently, you meet a strange woman. It seems that she is\n  not from this period of time either.");
				story++;
				break;
			}
			case 15:{
				dialog.setText("  Hmm? What is a high school student doing here?");
				story++;
				break;
			}
			case 16:{
				dialog.setText("  ... don't tell me you are " + pname + "! Well it seems that \n  your arrival was a little bit late..");
				story++;
				break;
			}
			case 17:{
				avatar.setImageResource(R.drawable.cglady1);
				dialog.setText("  Oh sorry for my rudeness that I've forgot to introduce myself.");
				story++;
				break;
			}
			case 18:{
				avatar.setImageResource(R.drawable.cglady2);
				dialog.setText("  My name is Lady. Pretty strange name huh?");
				story++;
				break;
			}
			case 19:{
				avatar.setImageResource(R.drawable.cglady);
				dialog.setText("  I am from the future just like you. I arrive at this age to stop \n  a crazy man who wants to change the history. If you think that \n  this is a dream then you are wrong.");
				story++;
				break;
			}
			case 20:{
				dialog.setText("  I can not do this mission alone that I really need your help \n  so I summoned you to this time.");
				story++;
				break;
			}
			case 21:{
				dialog.setText("  Why me you ask? Well... From my future database, it seems\n  that you are trully excellent in Indonesian History\n  and Culture subject.");
				story++;
				break;
			}
			case 22:{
				dialog.setText("  That crazy man I told you wants to trap every Wayang\n  characters into cards so that this Baratayuda won't\n  ever happen.\n  The cruel part is that he will use those card as a game.");
				story++;
				break;
			}
			case 23:{
				avatar.setImageResource(R.drawable.cglady6);
				dialog.setText("  Unfortunately, he was faster than I thought. \n  Those soldiers you saw earlier have been turned into cards. \n  I just happened to collect only 5 of them.");
				story++;
				break;
			}
			case 24:{
				dialog.setText("  I really think that we need to go through his game.\n  If we can get to him and defeat him in this cruel game,\n  then I can arrest him for good.");
				story++;
				break;
			}
			case 25:{
				avatar.setImageResource(R.drawable.cglady4);
				dialog.setText("  So what do you think? Pretty crazy huh? \n  I know how you feel but you and I are the only hope here.");
				story++;
				break;
			}
			case 26:{
				avatar.setImageResource(R.drawable.cgmainchar6);
				dialog.setText("  It seems that she entrusted you with a heavy mission. \n  You have no choice but to agree to help her out.");
				story++;
				break;
			}
			case 27:{
				avatar.setImageResource(R.drawable.cglady1);
				dialog.setText("  You will do? Thank you so much!");
				story++;
				break;
			}
			case 28:{
				avatar.setImageResource(R.drawable.cglady2);
				dialog.setText("  Well now... why don't we go to the nearest town from here to\n  gather information?");
				story++;
				break;
			}
			case 29:{
				avatar.setVisibility(View.INVISIBLE);
				dialog.setText("  The two of you start walking to search the nearest town.");
				story++;
				break;
			}
			case 30:{
				story++;
				tapthedialog = 0;
				updatestory();
				cardgametown();
				break;
			}
			case 31:{
				avatar.setImageResource(R.drawable.cglady2);
				avatar.setVisibility(View.VISIBLE);
				dialog.setText("  Well here we are.. Indraprastha, the city of Pandavas.\n  You sure know about it right?");
				story++;
				break;
			}
			case 32:{
				avatar.setImageResource(R.drawable.cglady6);
				dialog.setText("  We should check around the town first.\n  I have a feeling that something bad might gonna happen..");
				story++;
				break;
			}
			case 33:{
				avatar.setImageResource(R.drawable.cgfoe11);
				dialog.setText("  Well? Who do we have here?");
				story++;
				break;
			}
			case 34:{
				avatar.setImageResource(R.drawable.cglady8);
				dialog.setText("  That outfit..\n  You are one of them!?");
				story++;
				break;
			}
			case 35:{
				avatar.setImageResource(R.drawable.cgfoe11);
				dialog.setText("  Oh so you know us huh?\n  Then you must be from future too.");
				story++;
				break;
			}
			case 36:{
				dialog.setText("  Well.. I, Kraus of the HISTORY challenge you!\n  You should've known the game right?\n  In that case, I'll be waiting for you at the arena of this city!");
				story++;
				break;
			}
			case 37:{
				avatar.setVisibility(View.INVISIBLE);
				dialog.setText("  Kraus has disappeared...");
				story++;
				break;
			}
			case 38:{
				avatar.setImageResource(R.drawable.cglady8);
				avatar.setVisibility(View.VISIBLE);
				dialog.setText("  We should hurry to the arena!\n  We can trace him from his underlings.");
				story++;
				break;
			}
			case 39:{
				avatar.setImageResource(R.drawable.cglady);
				dialog.setText("  Oh before I forgot, you need these to fight him..");
				story++;
				break;
			}
			case 40:{
				avatar.setVisibility(View.INVISIBLE);
				dialog.setText("  You got Pratiwindya, Sutasoma, Srutakarma, Satanika,\n  and Srutakirti cards!");
				ContentValues updatevalues = new ContentValues();
			    updatevalues.put("jumlah", 1);
			    long n = DatabaseHelper.updatetable("KARTU", updatevalues, "_id="+26);
			    n = DatabaseHelper.updatetable("KARTU", updatevalues, "_id="+27);
			    ContentValues updatevalues1 = new ContentValues();
			    updatevalues1.put("kartu1", 23);
			    updatevalues1.put("kartu2", 24);
			    updatevalues1.put("kartu3", 25);
			    n = DatabaseHelper.updatetable("PEMAIN", updatevalues1, "_id="+ID);
			    if(n!=-1){
		        	System.out.println("UPDATE SUCCESS");
		        }	
		        else
		        	System.out.println("UPDATE FAILED");
			    tournamentcheck = true;
				story++;
				break;
			}
			case 41:{
				avatar.setImageResource(R.drawable.cglady4);
				avatar.setVisibility(View.VISIBLE);
				dialog.setText("  Don't forget to edit your deck at town.\n  We need to win this no matter what.");
				story++;
				break;
			}
			case 42:{
				story++;
				tapthedialog = 0;
				updatestory();
				cardgametown();
				break;
			}
			case 59:{
				avatar.setImageResource(R.drawable.cglady8);
				avatar.setVisibility(View.VISIBLE);
				dialog.setText("  This town really looks a fort for me...\n  But we should hurry to find him!");
				story++;
				break;
			}
			case 60:{
				avatar.setImageResource(R.drawable.cgfoe2);
				dialog.setText("  Well why are you so in a hurry?");
				story++;
				break;
			}
			case 61:{
				avatar.setImageResource(R.drawable.cglady8);
				dialog.setText("  Get out of our way!\n  W.wait... don't tell me you're also his underling!?");
				story++;
				break;
			}
			case 62:{
				avatar.setImageResource(R.drawable.cgfoe22);
				dialog.setText("  His underling?\n  What are you talking about girl?");
				story++;
				break;
			}
			case 63:{
				avatar.setImageResource(R.drawable.cgfoe21);
				dialog.setText("  Oh now I get it!!\n  You must be the one my boss mentioned!");
				story++;
				break;
			}
			case 64:{
				dialog.setText("  Hahaha how fortunate!\n  I, Edmund of HISTORY, has been ordered\n  to stop you two here no matter what.");
				story++;
				break;
			}
			case 65:{
				dialog.setText("  My boss has gone somewhere and you\n  won't go anywhere! hahaha...");
				story++;
				break;
			}
			case 66:{
				dialog.setText("  But I don't want to play with you in the town..\n  How about at the arena I specially created\n  for you?");
				story++;
				break;
			}
			case 67:{
				avatar.setImageResource(R.drawable.cglady8);
				dialog.setText("  Wait!\n  You and the other guy named Kraus keep on\n  mentioning about HISTORY..\n  Tell me about that!");
				story++;
				break;
			}
			case 68:{
				avatar.setImageResource(R.drawable.cgfoe21);
				dialog.setText("  Huh? So you're after someone but you\n  don't even know where does he come from?");
				story++;
				break;
			}
			case 69:{
				dialog.setText("  Hahaha... how amusing!\n  Well... how about if I tell you if you\n  win at my arena?");
				story++;
				break;
			}
			case 70:{
				dialog.setText("  I am a member of HISTORY and\n  I will not lie even to my enemy!\n  You should be grateful!");
				story++;
				break;
			}
			case 71:{
				dialog.setText("  Well enough talk and prepare your best deck\n  I'm really hoping a great fight later!");
				story++;
				break;
			}
			case 72:{
				avatar.setVisibility(View.INVISIBLE);
				dialog.setText("  Edmund has disappeared...");
				story++;
				break;
			}
			case 73:{
				avatar.setImageResource(R.drawable.cglady3);
				avatar.setVisibility(View.VISIBLE);
				dialog.setText("  Just what is he thinking!?\n  And what's with that stupid organization!");
				story++;
				break;
			}
			case 74:{
				avatar.setImageResource(R.drawable.cgmainchar3);
				dialog.setText("  You are asking about the boss of the organization\n  called the HISTORY...");
				story++;
				break;
			}
			case 75:{
				avatar.setImageResource(R.drawable.cglady5);
				dialog.setText("  He is called Andalucia.\n  Actually, he is a time criminal.\n  He's the man behind many time terrorism.");
				story++;
				break;
			}
			case 76:{
				dialog.setText("  He is known as a lone wolf...always acting alone.\n  But this time, it seems that he created\n  some kind of organization.");
				story++;
				break;
			}
			case 77:{
				avatar.setImageResource(R.drawable.cglady6);
				dialog.setText("  If this organization keeps on growing,\n  it will be hard for us, the time police\n  to handle...");
				story++;
				break;
			}
			case 78:{
				avatar.setImageResource(R.drawable.cglady8);
				dialog.setText("  So that's why I really need a lot of information\n  about him and his organization.\n  Seems like we need to take a detour from now...");
				story++;
				break;
			}
			case 79:{
				dialog.setText("  We have no choice but to follow their game\n  and beat them for good using their\n  own stupid game!");
				story++;
				break;
			}
			case 80:{
				dialog.setText("  To start with, let's defeat this Edmund guy first,\n  shall we?");
				story++;
				break;
			}
			case 81:{
				avatar.setVisibility(View.INVISIBLE);
				dialog.setText("  The two of you start walking to the arena...");
				story++;
				break;
			}
			case 82:{
				story++;
				tapthedialog = 0;
				updatestory();
				cardgametown();
				break;
			}
			case 110:{
				avatar.setVisibility(View.INVISIBLE);
				dialog.setText("  The town is really quiet...");
				story++;
				break;
			}
			case 111:{
				avatar.setImageResource(R.drawable.cglady);
				avatar.setVisibility(View.VISIBLE);
				dialog.setText("  This town is really quiet...\n  Almost like a ghost town...");
				story++;
				break;
			}
			case 112:{
				dialog.setText("  Something is not right here...");
				story++;
				break;
			}
			case 113:{
				avatar.setImageResource(R.drawable.cgmainchar3);
				dialog.setText("  As you search around the town,\n  you realize that the town's people have vanished\n  into the air just like the soldiers\n  at Kurukshetra...");
				story++;
				break;
			}
			case 114:{
				avatar.setImageResource(R.drawable.cglady4);
				dialog.setText("  There is someone over there...");
				story++;
				break;
			}
			case 115:{
				avatar.setVisibility(View.INVISIBLE);
				dialog.setText("  The two of you approach...");
				story++;
				break;
			}
			case 116:{
				avatar.setImageResource(R.drawable.cgfoe3);
				avatar.setVisibility(View.VISIBLE);
				dialog.setText("  Oh..? So I'm detected, huh?");
				story++;
				break;
			}
			case 117:{
				dialog.setText("  Hahaha... If you can find me, then\n  can you find the other people?");
				story++;
				break;
			}
			case 118:{
				dialog.setText("  I really doubt it hahaha...\n  Those people vanish as sacrifices to awaken Nirvana!");
				story++;
				break;
			}
			case 119:{
				avatar.setImageResource(R.drawable.cglady8);
				dialog.setText("  What do you mean!?\n  Do you kill them!?");
				story++;
				break;
			}
			case 120:{
				avatar.setImageResource(R.drawable.cgfoe3);
				dialog.setText("  Kill? We are not that evil...\n  We simply erase them from the history! Hahaha...");
				story++;
				break;
			}
			case 121:{
				avatar.setImageResource(R.drawable.cglady8);
				dialog.setText("  How dare you!? I'll arrest you right here right now!");
				story++;
				break;
			}
			case 122:{
				avatar.setImageResource(R.drawable.cgfoe3);
				dialog.setText("  That's not my fault! No! NO!\n  I'm just ordered to push this button and everything\n  just... BOOM!");
				story++;
				break;
			}
			case 123:{
				dialog.setText("  Everyone vanish! I guess those at the other town\n  disappeared as well...");
				story++;
				break;
			}
			case 124:{
				dialog.setText("  This era must be eradicated in order to achieve Nirvana!\n  That's what our mission is!\n  Do you get it? I doubt it...");
				story++;
				break;
			}
			case 125:{
				avatar.setImageResource(R.drawable.cglady8);
				dialog.setText("  Just what the hell is this Nirvana thing!?\n  You guys should pay for this!");
				story++;
				break;
			}
			case 126:{
				avatar.setImageResource(R.drawable.cgfoe3);
				dialog.setText("  Ooo... scary... but unfortunately, you guys\n  can't go anywhere if haven't defeat me!\n  Oh by the way, the name's Dartz.\n  I'll be waiting for you at the arena! Hahaha...");
				story++;
				break;
			}
			case 127:{
				avatar.setImageResource(R.drawable.cglady8);
				dialog.setText("  Wait you! I said wait!\n  ... guess we don't have a choice...");
				story++;
				break;
			}
			case 128:{
				avatar.setImageResource(R.drawable.cglady5);
				dialog.setText("  We have to defeat him and all of HISTORY!\n  This is going too far!\n  I can't let them do anything they want!");
				story++;
				break;
			}
			case 129:{
				story++;
				tapthedialog = 0;
				updatestory();
				cardgametown();
				break;
			}
			case 154:{
				avatar.setImageResource(R.drawable.cgfoe41);
				avatar.setVisibility(View.VISIBLE);
				dialog.setText("  Oh my! I've never imagined that you could go this far!\n  My name is Blue just like the color of my hair.");
				story++;
				break;
			}
			case 155:{
				avatar.setImageResource(R.drawable.cglady8);
				dialog.setText("  Another one!? Just how many of you out here!?");
				story++;
				break;
			}
			case 156:{
				avatar.setImageResource(R.drawable.cgfoe42);
				dialog.setText("  Woah! Relax!\n  I imagined that you met Dartz before me...\n  If that's the case, no wonder that you're angry..");
				story++;
				break;
			}
			case 157:{
				avatar.setImageResource(R.drawable.cgfoe4);
				dialog.setText("  I don't like him either.\n  I first thought that it was only personal.\n  However, now I've got companions! haha..");
				story++;
				break;
			}
			case 158:{
				avatar.setImageResource(R.drawable.cglady4);
				dialog.setText("  Then are you willing to share some information?\n  We need to find your boss, Andalucia and put an end to this.");
				story++;
				break;
			}
			case 159:{
				avatar.setImageResource(R.drawable.cgfoe43);
				dialog.setText("  Then I assumed that you don't even care of the villagers?");
				story++;
				break;
			}
			case 160:{
				avatar.setImageResource(R.drawable.cglady4);
				dialog.setText("  We believe that by defeating him, everything will go back to normal.");
				story++;
				break;
			}
			case 161:{
				avatar.setImageResource(R.drawable.cgfoe41);
				dialog.setText("  Oh man...\n  I really don't want to do this anyway...\n  But I guess we are in the same spot, aren't we?");
				story++;
				break;
			}
			case 162:{
				avatar.setImageResource(R.drawable.cgfoe42);
				dialog.setText("  I'll be waiting for you in the arena!\n  I promise to tell you everything I know about Nirvana.");
				story++;
				break;
			}
			case 163:{
				avatar.setVisibility(View.INVISIBLE);
				dialog.setText("  Blue leaves for the arena...");
				story++;
				break;
			}
			case 164:{
				avatar.setImageResource(R.drawable.cglady6);
				avatar.setVisibility(View.VISIBLE);
				dialog.setText("  Yeah... it seems that we are at a tight spot...");
				story++;
				break;
			}
			case 165:{
				avatar.setImageResource(R.drawable.cglady4);
				dialog.setText("  Let's go, " + pname + "!\n  We need every information about him and his plan.");
				story++;
				break;
			}
			case 166:{
				avatar.setVisibility(View.INVISIBLE);
				dialog.setText("  The two of you leaves for the arena...");
				story++;
				break;
			}
			case 167:{
				story++;
				tapthedialog = 0;
				updatestory();
				cardgametown();
				break;
			}
			case 197:{
				avatar.setImageResource(R.drawable.cgfoe5);
				avatar.setVisibility(View.VISIBLE);
				dialog.setText("  I see that stupid Blue has spoiled everything on you.");
				story++;
				break;
			}
			case 198:{
				avatar.setImageResource(R.drawable.cglady8);
				dialog.setText("  A woman?\n  So that Andalucia also employed woman, huh?");
				story++;
				break;
			}
			case 199:{
				avatar.setImageResource(R.drawable.cgfoe53);
				dialog.setText("  How dare you!\n  I'm Tremmy, his wife!");
				story++;
				break;
			}
			case 200:{
				avatar.setImageResource(R.drawable.cglady7);
				dialog.setText("  Wife!? So he's married, huh?");
				story++;
				break;
			}
			case 201:{
				avatar.setImageResource(R.drawable.cgfoe51);
				dialog.setText("  How about you? Is that boy over there your boyfriend?");
				story++;
				break;
			}
			case 202:{
				avatar.setImageResource(R.drawable.cglady7);
				dialog.setText("  I-it's n-not as you think!");
				story++;
				break;
			}
			case 203:{
				avatar.setImageResource(R.drawable.cgfoe51);
				dialog.setText("  Hmmm... you're blushing.\n  Anyway, the manifestation engine is sealed by me.\n  If you want me to stop it, you must defeat me first!");
				story++;
				break;
			}
			case 204:{
				dialog.setText("  I'll be waiting for you and your boyfriend at the arena.");
				story++;
				break;
			}
			case 205:{
				avatar.setVisibility(View.INVISIBLE);
				dialog.setText("  Tremmy leaves for the arena...");
				story++;
				break;
			}
			case 206:{
				avatar.setImageResource(R.drawable.cglady7);
				avatar.setVisibility(View.VISIBLE);
				dialog.setText("  I s-said it's n-not as you think!\n  Geez...");
				story++;
				break;
			}
			case 207:{
				dialog.setText("  A-anyhow, let's defeat her.\n  W-we need to clarify this thing!");
				story++;
				break;
			}
			case 208:{
				avatar.setImageResource(R.drawable.cgmainchar6);
				dialog.setText("  It seems that you're stuck between the girls' fight...\n  You don't have a choice but to do the usual...");
				story++;
				break;
			}
			case 209:{
				story++;
				tapthedialog = 0;
				updatestory();
				cardgametown();
				break;
			}
			case 235:{
				avatar.setImageResource(R.drawable.cgfoe6);
				avatar.setVisibility(View.VISIBLE);
				dialog.setText("  Welcome to Wrekastali!\n  I, Flame must admit that you are exceptional.");
				story++;
				break;
			}
			case 236:{
				avatar.setImageResource(R.drawable.cglady8);
				dialog.setText("  Just stop this Nirvana thing!\n  You're nothing, but his pawns!");
				story++;
				break;
			}
			case 237:{
				avatar.setImageResource(R.drawable.cgfoe61);
				dialog.setText("  Just what are you saying?\n  I'm his right hand.. and I won't abandon him.");
				story++;
				break;
			}
			case 238:{
				avatar.setImageResource(R.drawable.cgfoe62);
				dialog.setText("  I don't care about history, about this era, \n  about the people living there, and\n  about my comrades!");
				story++;
				break;
			}
			case 239:{
				avatar.setImageResource(R.drawable.cglady8);
				dialog.setText("  You were used by him!\n  Just don't let him fool you!");
				story++;
				break;
			}
			case 240:{
				avatar.setImageResource(R.drawable.cgfoe62);
				dialog.setText("  No he doesn't fool me.\n  He even helped me with my poor family in the past.\n  It's just the right thing to do to help him back.");
				story++;
				break;
			}
			case 241:{
				avatar.setImageResource(R.drawable.cglady);
				dialog.setText("  He did that?");
				story++;
				break;
			}
			case 242:{
				avatar.setImageResource(R.drawable.cgfoe61);
				dialog.setText("  He was from the future and he told me\n  everything he knew about me.\n  He even saved my family's economy crisis.");
				story++;
				break;
			}
			case 243:{
				dialog.setText("  He did the same to the rest of the HISTORY.\n  So I volunteered to join his cause.");
				story++;
				break;
			}
			case 244:{
				dialog.setText("  Do you know what happened to him actually?\n  He suffered a very serious disease.\n  To continue to live longer, he needs hourly medicine.");
				story++;
				break;
			}
			case 245:{
				dialog.setText("  That's when he found an old manuscript of Nirvana.\n  It will be bad to lose a good person...");
				story++;
				break;
			}
			case 246:{
				avatar.setImageResource(R.drawable.cglady);
				dialog.setText("  No way...\n  There's no way he did those things!\n  You must be deceived!");
				story++;
				break;
			}
			case 247:{
				avatar.setImageResource(R.drawable.cgfoe61);
				dialog.setText("  If you're unsure, why don't you try to talk to him?\n  I bet that he will gladly answer your questions.");
				story++;
				break;
			}
			case 248:{
				avatar.setImageResource(R.drawable.cgfoe62);
				dialog.setText("  However, you need to pass me first!\n  You know where to find me, right?\n  I'll be waiting!");
				story++;
				break;
			}
			case 249:{
				avatar.setVisibility(View.INVISIBLE);
				dialog.setText("  Flame leaves for the arena...");
				story++;
				break;
			}
			case 250:{
				avatar.setImageResource(R.drawable.cglady);
				avatar.setVisibility(View.VISIBLE);
				dialog.setText("  I just can't believe it...\n  We need to find him immediately.");
				story++;
				break;
			}
			case 251:{
				avatar.setVisibility(View.INVISIBLE);
				dialog.setText("  It seems that you're stuck in a dilemma...\n  However, you should fight your way to know the truth.");
				story++;
				break;
			}
			case 253:{
				dialog.setText("  The two of you rush to the arena...");
				story++;
				break;
			}
			case 254:{
				story++;
				tapthedialog = 0;
				updatestory();
				cardgametown();
				break;
			}
			case 282:{
				avatar.setImageResource(R.drawable.cglady);
				avatar.setVisibility(View.VISIBLE);
				dialog.setText("  This tower is really huge...\n  Anyway, let's go inside!");
				story++;
				break;
			}
			case 283:{
				avatar.setVisibility(View.INVISIBLE);
				dialog.setText("  You enter the Menara...");
				story++;
				break;
			}
			case 284:{
				avatar.setImageResource(R.drawable.cglady1);
				avatar.setVisibility(View.VISIBLE);
				dialog.setText("  Look at that!\n  There is an elevator!");
				story++;
				break;
			}
			case 285:{
				avatar.setVisibility(View.INVISIBLE);
				dialog.setText("  The two of you start to ride the elevator...");
				story++;
				break;
			}
			case 286:{
				avatar.setImageResource(R.drawable.cglady3);
				avatar.setVisibility(View.VISIBLE);
				dialog.setText("  But, how come that there is an elevator at this era?\n  This must be a work from the future...\n  Or perhaps, he made this too...!?");
				story++;
				break;
			}
			case 287:{
				avatar.setVisibility(View.INVISIBLE);
				dialog.setText("  The elevator has stopped...");
				story++;
				break;
			}
			case 288:{
				avatar.setImageResource(R.drawable.cglady5);
				avatar.setVisibility(View.VISIBLE);
				dialog.setText("  What now?\n  Is it broken?");
				story++;
				break;
			}
			case 289:{
				avatar.setImageResource(R.drawable.cgfoe11);
				dialog.setText("  It's not.\n  I just stopped it for a while.");
				story++;
				break;
			}
			case 290:{
				avatar.setImageResource(R.drawable.cglady8);
				dialog.setText("  Kraus?\n  I thought that you've given up on us already...");
				story++;
				break;
			}
			case 291:{
				avatar.setImageResource(R.drawable.cgfoe11);
				dialog.setText("  I'm very sorry lady, but I'm not the Kraus you knew.\n  I'm just his image... his illusion made by this tower.");
				story++;
				break;
			}
			case 292:{
				avatar.setImageResource(R.drawable.cglady8);
				dialog.setText("  Illusion?\n  I don't understand...");
				story++;
				break;
			}
			case 293:{
				avatar.setImageResource(R.drawable.cgfoe11);
				dialog.setText("  This tower can create a copy of people in your mind and events around them.");
				story++;
				break;
			}
			case 294:{
				dialog.setText("  From what I get, you need to defeat this person\n  in a card game to continue, right?\n  How about calling this floor where we stop an arena?");
				story++;
				break;
			}
			case 295:{
				dialog.setText("  You will surely love the exciting battle no?\n  hahaha...");
				story++;
				break;
			}
			case 296:{
				avatar.setVisibility(View.INVISIBLE);
				dialog.setText("  It seems that this floor has become an arena.\n  You have no choice but to go through it...");
				story++;
				break;
			}
			case 297:{
				story++;
				tapthedialog = 0;
				updatestory();
				cardgametown();
				break;
			}
			case 329:{
				avatar.setImageResource(R.drawable.cglady1);
				avatar.setVisibility(View.VISIBLE);
				dialog.setText("  This place is so pretty!\n  It really looks like a heaven...");
				story++;
				break;
			}
			case 330:{
				avatar.setVisibility(View.INVISIBLE);
				dialog.setText("  Someone is approaching from the Menara...");
				story++;
				break;
			}
			case 331:{
				avatar.setImageResource(R.drawable.cgfoe61);
				avatar.setVisibility(View.VISIBLE);
				dialog.setText("  How is it?\n  It's beautiful, isn't it?");
				story++;
				break;
			}
			case 332:{
				avatar.setImageResource(R.drawable.cglady4);
				dialog.setText("  Flame?");
				story++;
				break;
			}
			case 333:{
				avatar.setImageResource(R.drawable.cgfoe61);
				dialog.setText("  Nope. I'm also an image just like what you've fought earlier...");
				story++;
				break;
			}
			case 334:{
				dialog.setText("  I was created a little bit late...");
				story++;
				break;
			}
			case 335:{
				avatar.setImageResource(R.drawable.cglady8);
				dialog.setText("  Then what do you want?\n  If you want to stop us, then we'll be glad to defeat you!");
				story++;
				break;
			}
			case 336:{
				avatar.setImageResource(R.drawable.cgfoe61);
				dialog.setText("  Well, actually yeah.\n  However, I want to be in the same arena as him.\n  It would be the last arena so I'll be waiting!");
				story++;
				break;
			}
			case 337:{
				avatar.setVisibility(View.INVISIBLE);
				dialog.setText("  You are going to enter the very last arena...\n  You should prepare yourself...");
				story++;
				break;
			}
			case 338:{
				story++;
				tapthedialog = 0;
				updatestory();
				cardgametown();
				break;
			}
		}
	}
	
	public void updatestorytodb(){
		ContentValues updatevalues = new ContentValues();
	    updatevalues.put("cerita", story);
	    long n = DatabaseHelper.updatetable("PEMAIN", updatevalues, "_id="+ID);
	    if(n!=-1){
        	System.out.println("UPDATE SUCCESS");
        }	
        else
        	System.out.println("UPDATE FAILED");
	}
	
	public void arena(){
		//set the layout to card_game_name_conversation.xml.
		setContentView(R.layout.activity_card_game_conversation);
		
		//create objects for arena.
		ImageView bg = (ImageView) findViewById(R.id.imageView1);
		TextView place = (TextView) findViewById(R.id.textView2);
		TextView town = (TextView) findViewById(R.id.textView3);
		final TextView dialog = (TextView) findViewById(R.id.textView4);
		Resources res = getResources();
		String[] kota = res.getStringArray(R.array.Kota);
		
	    //set the town background based on k.
		if(k == 0)
			bg.setImageResource(R.drawable.cg_town1_kurukshetra);
		else if(k==1)
			bg.setImageResource(R.drawable.cg_town2_indraprastha);
		else if(k==2)
			bg.setImageResource(R.drawable.cg_town3_hastinapura);
		else if(k==3)
			bg.setImageResource(R.drawable.cg_town4_wardamana);
		else if(k==4)
			bg.setImageResource(R.drawable.cg_town5_pramanakati);
		else if(k==5)
			bg.setImageResource(R.drawable.cg_town6_waranawati);
		else if(k==6)
			bg.setImageResource(R.drawable.cg_town7_wrekastali);
		else if(k==7)
			bg.setImageResource(R.drawable.cg_town8_menara);
		else if(k==8)
			bg.setImageResource(R.drawable.cg_town9_nirvana);
		
		//set the place to search and the town to the corresponding one.
		place.setText("Arena");
		town.setText(kota[k]);
		
		if(story == 31 && k <= 1)
	    	arenaclosed();
		else if(story == 43 && k <= 0)
			arenaclosed();
		else if(story == 59 && k <= 2)
			arenaclosed();
		else if(story == 83 && k <= 1)
			arenaclosed();
		else if(story == 110 && k <= 3)
			arenaclosed();
		else if(story == 130 && k <= 2)
			arenaclosed();
		else if(story == 154 && k <= 4)
			arenaclosed();
		else if(story == 168 && k <= 3)
			arenaclosed();
		else if(story == 197 && k <= 5)
			arenaclosed();
		else if(story == 210 && k <= 4)
			arenaclosed();
		else if(story == 235 && k <= 6)
			arenaclosed();
		else if(story == 255 && k <= 5)
			arenaclosed();
		else if(story == 282 && k <= 7)
			arenaclosed();
		else if(story == 298 && k <= 6)
			arenaclosed();
		else if(story == 329 && k <= 8)
			arenaclosed();
		else if(story == 339 && k <= 7)
			arenaclosed();
		else
	    	arenaconversation();
		
		//set on click listener for dialog.
	    dialog.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(story == 31 && k <= 1)
			    	arenaclosed();
				else if(story == 43 && k <= 0)
					arenaclosed();
				else if(story == 59 && k <= 2)
					arenaclosed();
				else if(story == 83 && k <= 1)
					arenaclosed();
				else if(story == 110 && k <= 3)
					arenaclosed();
				else if(story == 130 && k <= 2)
					arenaclosed();
				else if(story == 154 && k <= 4)
					arenaclosed();
				else if(story == 168 && k <= 3)
					arenaclosed();
				else if(story == 197 && k <= 5)
					arenaclosed();
				else if(story == 210 && k <= 4)
					arenaclosed();
				else if(story == 235 && k <= 6)
					arenaclosed();
				else if(story == 255 && k <= 5)
					arenaclosed();
				else if(story == 282 && k <= 7)
					arenaclosed();
				else if(story == 298 && k <= 6)
					arenaclosed();
				else if(story == 329 && k <= 8)
					arenaclosed();
				else if(story == 339 && k <= 7)
					arenaclosed();
				else
			    	arenaconversation();
			};
		});	
		
	}
	
	public void arenaconversation(){
		ImageView avatar = (ImageView) findViewById(R.id.avatarCenter);
		ImageView bg = (ImageView) findViewById(R.id.imageView1);
		TextView dialogtown = (TextView) findViewById(R.id.textView3);
		TextView dialog = (TextView) findViewById(R.id.textView4);
		switch(story){
		case 43:{
			avatar.setImageResource(R.drawable.cgfoe11);
			dialog.setText("  Welcome to the arena of Indraprastha!\n  If you win in this arena, I will tell\n  you where our boss is. How about that?");
			story++;
			break;
		}
		case 44:{
			dialog.setText("  But unfortunately, you need to pass the preliminary battles!");
			story++;
			break;
		}
		case 45:{
			dialog.setText("  Are you ready? Here we go for the first round!!");
			story++;
			break;
		}
		case 46:{
			fromarena = true;
			enemyid = 1;
			cardbattle();
			break;
		}
		case 47:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe11);
				dialog.setText("  Well... what a surprise...\n  I didn't think that you can beat me easily");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgfoe11);
					dialog.setText("  Well... a draw is a draw...\n  But for me, a draw means lose!\n  Come back again if you've gotten stronger!");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgfoe11);
						dialog.setText("  Hahaha... how's that? I'm pretty good, huh?\n  Come back again if you've gotten stronger!");
					}
			story++;
			break;
		}
		case 48:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe13);
				dialog.setText("  This is so thrilling!! I won't accept losing!\n  This time is for real!");
				story++;
			}
			else{
				story = 43;
				cardgametown();
			}			
			break;
		}
		case 49:{
			fromarena = true;
			enemyid = 2;
			cardbattle();
			break;
		}
		case 50:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe12);
				dialog.setText("  H...How could this be happening!?");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgfoe11);
					dialog.setText("  Well... a draw is a draw...\n  But for me, a draw means lose!\n  Come back again if you've gotten stronger!");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgfoe11);
						dialog.setText("  Hahaha... how's that? I'm pretty good, huh?\n  Come back again if you've gotten stronger!");
					}
			story++;
			break;
		}
		case 51:{
			if(win == true){
				dialog.setText("  Alright... as promise I will tell you where he is..");
				story++;
			}
			else{
				story = 43;
				cardgametown();
			}		
			break;
		}
		case 52:{
			dialog.setText("  He has moved to Hastinapura, the city of Kauravas.\n  But I'm sure that you can't defeat him!\n  HAHAHAHA...");
			story++;
			break;
		}
		case 53:{
			avatar.setVisibility(View.INVISIBLE);
			dialog.setText("  Kraus has disappeared...");
			story++;
			break;
		}
		case 54:{
			avatar.setImageResource(R.drawable.cglady);
			avatar.setVisibility(View.VISIBLE);
			dialog.setText("  Well done, " + pname + "!\n  I knew that I can count on you!");
			story++;
			break;
		}
		case 55:{
			dialog.setText("  But this is just a beginning..\n  We need to go to Hastinapura immediately.");
			story++;
			break;
		}
		case 56:{
			dialog.setText("  I really hope that we can catch him there.");
			story++;
			break;
		}
		case 57:{
			avatar.setVisibility(View.INVISIBLE);
			dialog.setText("  The two of you leave the arena...");
			story++;
			break;
		}
		case 58:{
			story++;
			tapthedialog = 0;
			updatestory();
			cardgametown();
			break;
		}
		case 83:{
			avatar.setImageResource(R.drawable.cgfoe22);
			avatar.setVisibility(View.VISIBLE);
			dialog.setText("  What took you so long to get here?\n  I've been waiting for quite some time...");
			story++;
			break;
		}
		case 84:{
			avatar.setImageResource(R.drawable.cgfoe21);
			dialog.setText("  Anyhow... let's get this going!\n  Don't worry.. I'll keep my promise!");
			story++;
			break;
		}
		case 85:{
			dialog.setText("  Ready or not here I come!");
			story++;
			break;
		}
		case 86:{
			fromarena = true;
			enemyid = 4;
			cardbattle();
			break;
		}
		case 87:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe22);
				dialog.setText("  Hahaha... well done...\n  Now I will tell you that...");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgfoe21);
					dialog.setText("  Unfortunately, I set up a special rule in my arena!\n  A draw result means lose! Hahaha\n  Oops.. I didn't tell you?\n  Well just get out of here! Hahaha...");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgfoe21);
						dialog.setText("  Unfortunately, you're not skilled enough to\n  defeat me... A lack in experience perhaps?\n  Hahaha... just get out of here now!");
					}
			story++;
			break;
		}
		case 88:{
			if(win == true){
				dialog.setText("  the next round is coming!! Hahaha\n  Huh? I didn't tell you?\n  How absurd! Enough with that and start the battle!");
				story++;
			}
			else{
				story = 83;
				cardgametown();
			}		
			break;
		}
		case 89:{
			fromarena = true;
			enemyid = 5;
			cardbattle();
			break;
		}
		case 90:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe22);
				dialog.setText("  Hahaha... well done...\n  Now I will tell you that...");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgfoe21);
					dialog.setText("  Unfortunately, I set up a special rule in my arena!\n  A draw result means lose! Hahaha\n  Oops.. I didn't tell you?\n  Well just get out of here! Hahaha...");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgfoe21);
						dialog.setText("  Unfortunately, you're not skilled enough to\n  defeat me... A lack in experience perhaps?\n  Hahaha... just get out of here now!");
					}
			story++;
			break;
		}
		case 91:{
			if(win == true){
				dialog.setText("  the real battle just starts now! This time is for real!");
				story++;
			}
			else{
				story = 83;
				cardgametown();
			}		
			break;
		}
		case 92:{
			fromarena = true;
			enemyid = 6;
			cardbattle();
			break;
		}
		case 93:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe22);
				dialog.setText("  Hahaha...");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgfoe21);
					dialog.setText("  Unfortunately, I set up a special rule in my arena!\n  A draw result means lose! Hahaha\n  Oops.. I didn't tell you?\n  Well just get out of here! Hahaha...");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgfoe21);
						dialog.setText("  Unfortunately, you're not skilled enough to\n  defeat me... A lack in experience perhaps?\n  Hahaha... just get out of here now!");
					}
			story++;
			break;
		}
		case 94:{
			if(win == true){
				dialog.setText("  Just how come...\n  Oh well... this game is stupid at the first place...\n  But you seem to be pretty smart for it...");
				story++;
			}
			else{
				story = 83;
				cardgametown();
			}		
			break;
		}
		case 95:{
			dialog.setText("  Alright... I'll keep my promise...\n  I'll tell you about HISTORY...");
			story++;
			break;
		}
		case 96:{
			dialog.setText("  I joined HISTORY since the boss promised me\n  that I could turn back to the past...\n  I've got to... but I won't tell you my reason.");
			story++;
			break;
		}
		case 97:{
			dialog.setText("  The boss' vision is clear to me and in return,\n  I agreed to help him...");
			story++;
			break;
		}
		case 98:{
			dialog.setText("  And here I am... delaying you to my best!\n  HAHAHA! While you're here, the boss has made his move...");
			story++;
			break;
		}
		case 99:{
			avatar.setImageResource(R.drawable.cglady8);
			dialog.setText("  What do you mean!?");
			story++;
			break;
		}
		case 100:{
			avatar.setImageResource(R.drawable.cgfoe21);
			dialog.setText("  Four of us have spread into four towns to destroy\n   the heaven's pillars!\n  There's nothing you can do now! HAHAHA!");
			story++;
			break;
		}
		case 101:{
			avatar.setImageResource(R.drawable.cglady8);
			dialog.setText("  What!?\n  What happened if you destroy the heaven's pillars!?");
			story++;
			break;
		}
		case 102:{
			avatar.setImageResource(R.drawable.cgfoe21);
			dialog.setText("  HAHAHA!\n  Do you think that I, your enemy will tell you that!?\n  The pillar of light to the NIrvana will appear!");
			story++;
			break;
		}
		case 103:{
			dialog.setText("  Oops... that bad mouth of mine...\n  Just forget about that!\n  I'm leaving! Hahaha just try to stop us now!");
			story++;
			break;
		}
		case 104:{
			avatar.setVisibility(View.INVISIBLE);
			dialog.setText("  Edmund has disappeared...");
			story++;
			break;
		}
		case 105:{
			avatar.setImageResource(R.drawable.cglady8);
			avatar.setVisibility(View.VISIBLE);
			dialog.setText("  This is no good...\n  the pillar of light to Nirvana...");
			story++;
			break;
		}
		case 106:{
			dialog.setText("  We need to immediately stop them!\n  Our best bet is that they target four of\n  the nearest towns around here...");
			story++;
			break;
		}
		case 107:{
			dialog.setText("  Let's search the towns near here!");
			story++;
			break;
		}
		case 108:{
			avatar.setVisibility(View.INVISIBLE);
			dialog.setText("  The two of you leave the arena...");
			story++;
			break;
		}
		case 109:{
			story++;
			tapthedialog = 0;
			updatestory();
			cardgametown();
			break;
		}
		case 130:{
			avatar.setImageResource(R.drawable.cgfoe3);
			avatar.setVisibility(View.VISIBLE);
			dialog.setText("  So you two actually come here...\n  Angry, huh?\n  Well... I can't blame you for that...");
			story++;
			break;
		}
		case 131:{
			avatar.setImageResource(R.drawable.cgfoe3);
			dialog.setText("  If you really hate me that much,\n  why don't we battle?\n  You could go berserk however you want!");
			story++;
			break;
		}
		case 132:{
			avatar.setImageResource(R.drawable.cglady8);
			dialog.setText("  Do not insult us or you will get hurt!");
			story++;
			break;
		}
		case 133:{
			avatar.setImageResource(R.drawable.cgfoe3);
			dialog.setText("  Hahaha... this just makes me work up!\n  Alright! Let's do the battle now!");
			story++;
			break;
		}
		case 134:{
			fromarena = true;
			enemyid = 8;
			cardbattle();
			break;
		}
		case 135:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe3);
				dialog.setText("  Hahaha... well done...");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgfoe3);
					dialog.setText("  Draw is unacceptable...\n  You know what it means, right?\n  Now just cry out there...");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgfoe3);
						dialog.setText("  See? Anger won't solve anything...\n  You lose because of it and I win\n  because I can control myself..\n  Now just cry out there...");
					}
			story++;
			break;
		}
		case 136:{
			if(win == true){
				dialog.setText("  But will you go berserk if I demand a rematch?");
				story++;
			}
			else{
				story = 130;
				cardgametown();
			}		
			break;
		}
		case 137:{
			avatar.setImageResource(R.drawable.cgfoe3);
			dialog.setText("  I guess that doesn't matter anymore, huh?\n  Well let's get this started!");
			story++;
			break;
		}
		case 138:{
			fromarena = true;
			enemyid = 9;
			cardbattle();
			break;
		}
		case 139:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe3);
				dialog.setText("  Hahaha... well done...");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgfoe3);
					dialog.setText("  Draw is unacceptable...\n  You know what it means, right?\n  Now just cry out there...");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgfoe3);
						dialog.setText("  See? Anger won't solve anything...\n  You lose because of it and I win\n  because I can control myself..\n  Now just cry out there...");
					}
			story++;
			break;
		}
		case 140:{
			if(win == true){
				dialog.setText("  But will you go berserk if I demand a last fight?");
				story++;
			}
			else{
				story = 130;
				cardgametown();
			}		
			break;
		}
		case 141:{
			avatar.setImageResource(R.drawable.cgfoe3);
			dialog.setText("  I guess that doesn't matter anymore, huh?\n  Well let's get this started!");
			story++;
			break;
		}
		case 142:{
			fromarena = true;
			enemyid = 10;
			cardbattle();
			break;
		}
		case 143:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe3);
				dialog.setText("  Hahaha... well done...");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgfoe3);
					dialog.setText("  Draw is unacceptable...\n  You know what it means, right?\n  Now just cry out there...");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgfoe3);
						dialog.setText("  See? Anger won't solve anything...\n  You lose because of it and I win\n  because I can control myself..\n  Now just cry out there...");
					}
			story++;
			break;
		}
		case 144:{
			if(win == true){
				dialog.setText("  I guess that your anger really helps you this time...");
				story++;
			}
			else{
				story = 130;
				cardgametown();
			}		
			break;
		}
		case 145:{
			avatar.setImageResource(R.drawable.cgfoe3);
			dialog.setText("  But I warned you as a friend...\n  You won't go any further if you keep\n  that attitude of yours...");
			story++;
			break;
		}
		case 146:{
			dialog.setText("  Just one more friendly advice,\n  you should check out Pramanakati.\n  Maybe you can learn something about Nirvana.");
			story++;
			break;
		}
		case 147:{
			dialog.setText("  I can't tell you about it for now.\n  You should just find it yourself,\n  just like you found me out at the town.");
			story++;
			break;
		}
		case 148:{
			dialog.setText("  As for now, I bid you farewell...");
			story++;
			break;
		}
		case 149:{
			avatar.setVisibility(View.INVISIBLE);
			dialog.setText("  Dartz left the arena...");
			story++;
			break;
		}
		case 150:{
			avatar.setImageResource(R.drawable.cglady6);
			avatar.setVisibility(View.VISIBLE);
			dialog.setText("  Just what the hell with that guy?\n  Geez... maybe we should follow his advice.");
			story++;
			break;
		}
		case 151:{
			dialog.setText("  I still just can't accept it...\n  Argh!! let's just go out from here...");
			story++;
			break;
		}
		case 152:{
			avatar.setVisibility(View.INVISIBLE);
			dialog.setText("  The two of you left the arena...");
			story++;
			break;
		}
		case 153:{
			story++;
			tapthedialog = 0;
			updatestory();
			cardgametown();
			break;
		}
		case 168:{
			avatar.setImageResource(R.drawable.cgfoe41);
			avatar.setVisibility(View.VISIBLE);
			dialog.setText("  Alright here we are!\n  I guess you've become familiar with his game right?");
			story++;
			break;
		}
		case 169:{
			dialog.setText("  I'm not good at this kind of game.\n  But I always try to do my best.");
			story++;
			break;
		}
		case 170:{
			dialog.setText("  So don't be angry to me if I won, ok?");
			story++;
			break;
		}
		case 171:{
			avatar.setImageResource(R.drawable.cglady6);
			dialog.setText("  I can assure you that my friend here won't go easy either.");
			story++;
			break;
		}
		case 172:{
			avatar.setImageResource(R.drawable.cgfoe42);
			dialog.setText("  Alright.\n  Then shall we begin?");
			story++;
			break;
		}
		case 173:{
			fromarena = true;
			enemyid = 12;
			cardbattle();
			break;
		}
		case 174:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe41);
				dialog.setText("  Yeah, I can see it that you're really serious on this.");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgfoe4);
					dialog.setText("  I hate to say this, but a draw is not permitted.\n  I'm not planning to lower my guard though.\n  So be sure to defeat me next time.");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgfoe4);
						dialog.setText("  I hate to say this, but a lose is a lose.\n  I'm not planning to lower my guard though.\n  So be sure to defeat me next time.");
					}
			story++;
			break;
		}
		case 175:{
			if(win == true){
				dialog.setText("  I think that I need to be more serious...");
				story++;
			}
			else{
				story = 168;
				cardgametown();
			}		
			break;
		}
		case 176:{
			avatar.setImageResource(R.drawable.cgfoe42);
			dialog.setText("  I really hope that you also do the same 'cause here I come!");
			story++;
			break;
		}
		case 177:{
			fromarena = true;
			enemyid = 13;
			cardbattle();
			break;
		}
		case 178:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe41);
				dialog.setText("  Yeah, I can see it that you're getting more serious...");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgfoe4);
					dialog.setText("  I hate to say this, but a draw is not permitted.\n  I'm not planning to lower my guard though.\n  So be sure to defeat me next time.");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgfoe4);
						dialog.setText("  I hate to say this, but a lose is a lose.\n  I'm not planning to lower my guard though.\n  So be sure to defeat me next time.");
					}
			story++;
			break;
		}
		case 179:{
			if(win == true){
				dialog.setText("  I really need to increase my concentration for the final...");
				story++;
			}
			else{
				story = 168;
				cardgametown();
			}		
			break;
		}
		case 180:{
			avatar.setImageResource(R.drawable.cgfoe42);
			dialog.setText("  This time, I won't be easy to be beaten!\n  Let our final battle begins!");
			story++;
			break;
		}
		case 181:{
			fromarena = true;
			enemyid = 14;
			cardbattle();
			break;
		}
		case 182:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe4);
				dialog.setText("  I give up!");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgfoe4);
					dialog.setText("  I hate to say this, but a draw is not permitted.\n  I'm not planning to lower my guard though.\n  So be sure to defeat me next time.");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgfoe4);
						dialog.setText("  I hate to say this, but a lose is a lose.\n  I'm not planning to lower my guard though.\n  So be sure to defeat me next time.");
					}
			story++;
			break;
		}
		case 183:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe41);
				dialog.setText("  You really have a high determination.\n  I really admire it.");
				story++;
			}
			else{
				story = 168;
				cardgametown();
			}		
			break;
		}
		case 184:{
			avatar.setImageResource(R.drawable.cgfoe43);
			dialog.setText("  I'll tell you about Nirvana just as I promise.");
			story++;
			break;
		}
		case 185:{
			dialog.setText("  3 years ago, Andalucia found a way to achieve immortality.\n  One should open the door to the heaven.\n  That heaven is called the Nirvana.");
			story++;
			break;
		}
		case 186:{
			dialog.setText("  The door to the heaven is the Menara.\n  Only by collecting godly power, one shall reveal the Menara.");
			story++;
			break;
		}
		case 187:{
			dialog.setText("  And the choice to collect that power is to steal from\n  this era, where its people possessed some kind of magical power..");
			story++;
			break;
		}
		case 188:{
			dialog.setText("  And thus, as you can see, the soldiers in Kurukshetra\n  and also the town's people vanished.\n  They were manifested as that godly power.");
			story++;
			break;
		}
		case 189:{
			dialog.setText("  I know that it's kind of horrible.\n  But I can't do much to stop him.\n  What I can do is just delaying his plan.");
			story++;
			break;
		}
		case 190:{
			dialog.setText("  To make the town's people vanish,\n  we need to start the manifestation engine one by one,\n  starts from Wardamana, Pramanakati, Waranawati, and Wrekastali.");
			story++;
			break;
		}
		case 191:{
			dialog.setText("  The engine needs time to operate since the previous one.\n  I just activated it an hour ago, so there's still time.\n  You should go to Waranawati and Wrekastali immediately.");
			story++;
			break;
		}
		case 192:{
			avatar.setImageResource(R.drawable.cglady8);
			dialog.setText("  Alright, thanks for your information.\n  But I'm afraid that we need to rush.");
			story++;
			break;
		}
		case 193:{
			avatar.setImageResource(R.drawable.cgfoe41);
			dialog.setText("  Yeah, I really hope that you could stop them.");
			story++;
			break;
		}
		case 194:{
			avatar.setImageResource(R.drawable.cglady4);
			dialog.setText("  Alright, see you later, Blue.");
			story++;
			break;
		}
		case 195:{
			avatar.setVisibility(View.INVISIBLE);
			dialog.setText("  The two of you rush to the next town...");
			story++;
			break;
		}
		case 196:{
			story++;
			tapthedialog = 0;
			updatestory();
			cardgametown();
			break;
		}
		case 210:{
			avatar.setImageResource(R.drawable.cgfoe51);
			avatar.setVisibility(View.VISIBLE);
			dialog.setText("  Well, if it isn't the happy couple from back then..");
			story++;
			break;
		}
		case 211:{
			avatar.setImageResource(R.drawable.cglady7);
			dialog.setText("  Geez... h-how many times should I tell you that\n  we are partners to stop your husband!?");
			story++;
			break;
		}
		case 212:{
			avatar.setImageResource(R.drawable.cgfoe51);
			dialog.setText("  Yeah yeah...\n  I don't care much of your explanation.\n  All I care is how to stop people like you from\n  intervening with my husband's work!");
			story++;
			break;
		}
		case 213:{
			avatar.setImageResource(R.drawable.cgfoe53);
			dialog.setText("  You may be able to defeat the others.\n  But you won't get away from me easily!");
			story++;
			break;
		}
		case 214:{
			fromarena = true;
			enemyid = 16;
			cardbattle();
			break;
		}
		case 215:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe51);
				dialog.setText("  Not bad...");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgfoe52);
					dialog.setText("  You lacks love for each other.\n  That's why you only can get a draw with me.\n  Go out there and try to increase your love first!");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgfoe52);
						dialog.setText("  You lacks love for each other.\n  That's why you can't defeat me.\n  Go out there and try to increase your love first!");
					}
			story++;
			break;
		}
		case 216:{
			if(win == true){
				dialog.setText("  But can your love exceed mine?");
				story++;
			}
			else{
				story = 210;
				cardgametown();
			}		
			break;
		}
		case 217:{
			avatar.setImageResource(R.drawable.cgfoe53);
			dialog.setText("  If not, you will get hurt on this next fight!");
			story++;
			break;
		}
		case 218:{
			fromarena = true;
			enemyid = 17;
			cardbattle();
			break;
		}
		case 219:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe51);
				dialog.setText("  Not bad...");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgfoe52);
					dialog.setText("  You lacks love for each other.\n  That's why you only can get a draw with me.\n  Go out there and try to increase your love first!");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgfoe52);
						dialog.setText("  You lacks love for each other.\n  That's why you can't defeat me.\n  Go out there and try to increase your love first!");
					}
			story++;
			break;
		}
		case 220:{
			if(win == true){
				dialog.setText("  You may love each other more now...");
				story++;
			}
			else{
				story = 210;
				cardgametown();
			}		
			break;
		}
		case 221:{
			avatar.setImageResource(R.drawable.cgfoe53);
			dialog.setText("  But I can assure you that my love is undefeatable!\n  I'll prove it at our final fight!");
			story++;
			break;
		}
		case 222:{
			fromarena = true;
			enemyid = 18;
			cardbattle();
			break;
		}
		case 223:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe53);
				dialog.setText("  How...");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgfoe52);
					dialog.setText("  You lacks love for each other.\n  That's why you only can get a draw with me.\n  Go out there and try to increase your love first!");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgfoe52);
						dialog.setText("  You lacks love for each other.\n  That's why you can't defeat me.\n  Go out there and try to increase your love first!");
					}
			story++;
			break;
		}
		case 224:{
			if(win == true){
				dialog.setText("  How could you defeat my love?");
				story++;
			}
			else{
				story = 210;
				cardgametown();
			}		
			break;
		}
		case 225:{
			avatar.setImageResource(R.drawable.cglady7);
			dialog.setText("  E-enough with that love thing!");
			story++;
			break;
		}
		case 226:{
			avatar.setImageResource(R.drawable.cglady8);
			dialog.setText("  We are partners and we trust each other.\n  That's why we can defeat you easily.\n  You may love your husband, but it seems that you lacks trust.");
			story++;
			break;
		}
		case 227:{
			avatar.setImageResource(R.drawable.cgfoe53);
			dialog.setText("  I don't need your advice.\n  I just love him and I'll do anything to love him!\n  That's why I won't let him down!\n  I'll bring out this mission without fail!");
			story++;
			break;
		}
		case 228:{
			avatar.setVisibility(View.INVISIBLE);
			dialog.setText("  Tremmy activated the manifestation engine.\n  The town's people around you suddenly vanish to thin air.");
			story++;
			break;
		}
		case 229:{
			avatar.setImageResource(R.drawable.cglady8);
			avatar.setVisibility(View.VISIBLE);
			dialog.setText("  What have you done!\n  You promise that you'll stop it if we defeat you!");
			story++;
			break;
		}
		case 230:{
			avatar.setImageResource(R.drawable.cgfoe53);
			dialog.setText("  I said that I'll do anything to love him!\n  That's why I don't care whether you defeat me or not!\n hahaha...");
			story++;
			break;
		}
		case 231:{
			avatar.setVisibility(View.INVISIBLE);
			dialog.setText("  Tremmy leaves the arena...");
			story++;
			break;
		}
		case 232:{
			avatar.setImageResource(R.drawable.cglady8);
			avatar.setVisibility(View.VISIBLE);
			dialog.setText("  This is bad!\n  Our last hope is at Wrekastali...\n  Come on! We need to go there in instant!");
			story++;
			break;
		}
		case 233:{
			avatar.setVisibility(View.INVISIBLE);
			dialog.setText("  The two of you rush into the last town, Wrekastali...");
			story++;
			break;
		}
		case 234:{
			story++;
			tapthedialog = 0;
			updatestory();
			cardgametown();
			break;
		}
		case 255:{
			avatar.setImageResource(R.drawable.cgfoe6);
			avatar.setVisibility(View.VISIBLE);
			dialog.setText("  I'm glad that you choose to go to the arena.\n  I was worried that you held yourselves back...");
			story++;
			break;
		}
		case 256:{
			avatar.setImageResource(R.drawable.cglady6);
			dialog.setText("  We won't.\n  We only need to go to him to find out the truth.");
			story++;
			break;
		}
		case 257:{
			avatar.setImageResource(R.drawable.cgfoe61);
			dialog.setText("  Even if what I said earlier was true...\n  what would you do to him and to us?\n  Will you arrest us like your original plan?");
			story++;
			break;
		}
		case 258:{
			avatar.setImageResource(R.drawable.cglady4);
			dialog.setText("  No matter what, all of you have done\n  a big time and space crime.\n  The court will decide the actions for him and the rest of HISTORY.");
			story++;
			break;
		}
		case 259:{
			avatar.setImageResource(R.drawable.cgfoe6);
			dialog.setText("  I see.\n  If that's the case, then there is no need for me to hold back either.\n  Get your deck ready for battle!");
			story++;
			break;
		}
		case 260:{
			fromarena = true;
			enemyid = 20;
			cardbattle();
			break;
		}
		case 261:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe61);
				dialog.setText("  Guess I was not so serious on you...");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgfoe61);
					dialog.setText("  Are you really serious on taking him down with this?\n  If you only can make a draw, then it will be difficult.\n  You should head back and rethink this.");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgfoe61);
						dialog.setText("  Are you really serious on taking him down with this?\n  If you can not defeat me, then it will be difficult.\n  You should head back and rethink this.");
					}
			story++;
			break;
		}
		case 262:{
			if(win == true){
				dialog.setText("  But this time, you better watch your back!");
				story++;
			}
			else{
				story = 255;
				cardgametown();
			}		
			break;
		}
		case 263:{
			avatar.setImageResource(R.drawable.cgfoe62);
			dialog.setText("  I won't be going easily like last time!");
			story++;
			break;
		}
		case 264:{
			fromarena = true;
			enemyid = 21;
			cardbattle();
			break;
		}
		case 265:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe61);
				dialog.setText("  You really aren't holding back, huh?");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgfoe61);
					dialog.setText("  Are you really serious on taking him down with this?\n  If you only can make a draw, then it will be difficult.\n  You should head back and rethink this.");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgfoe61);
						dialog.setText("  Are you really serious on taking him down with this?\n  If you can not defeat me, then it will be difficult.\n  You should head back and rethink this.");
					}
			story++;
			break;
		}
		case 266:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe62);
				dialog.setText("  This time, my iron will is going to crush you!");
				story++;
			}
			else{
				story = 255;
				cardgametown();
			}		
			break;
		}
		case 267:{
			dialog.setText("  You better get ready for our final fight!");
			story++;
			break;
		}
		case 268:{
			fromarena = true;
			enemyid = 22;
			cardbattle();
			break;
		}
		case 269:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe61);
				dialog.setText("  Hahaha...\n  Guess your will power exceed mine, huh?");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgfoe61);
					dialog.setText("  Are you really serious on taking him down with this?\n  If you only can make a draw, then it will be difficult.\n  You should head back and rethink this.");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgfoe61);
						dialog.setText("  Are you really serious on taking him down with this?\n  If you can not defeat me, then it will be difficult.\n  You should head back and rethink this.");
					}
			story++;
			break;
		}
		case 270:{
			if(win == true){
				dialog.setText("  So... are you really going to Nirvana?");
				story++;
			}
			else{
				story = 255;
				cardgametown();
			}		
			break;
		}
		case 271:{
			avatar.setImageResource(R.drawable.cglady6);
			dialog.setText("  We need to.\n  There's nothing that can stop us now.");
			story++;
			break;
		}
		case 272:{
			avatar.setImageResource(R.drawable.cgfoe6);
			dialog.setText("  I see.\n  Then, go to Menara.\n  It is the only way to go to the Nirvana.");
			story++;
			break;
		}
		case 273:{
			avatar.setImageResource(R.drawable.cgfoe61);
			dialog.setText("  You better prepare yourselves first.\n  There's no telling who will you meet there.");
			story++;
			break;
		}
		case 274:{
			dialog.setText("  I will be waiting for your response after meeting him.\n  Oh by the way, I have to activate the engine\n  so that the Menara to the Nirvana can appear.");
			story++;
			break;
		}
		case 275:{
			avatar.setVisibility(View.INVISIBLE);
			dialog.setText("  Flame leaves the arena...\n  The manifestation engine has started...\n  Suddenly you see a great pillar shows up at a distance.");
			story++;
			break;
		}
		case 276:{
			avatar.setImageResource(R.drawable.cglady4);
			avatar.setVisibility(View.VISIBLE);
			dialog.setText("  All of our efforts to stop the engine have gone waste, huh?");
			story++;
			break;
		}
		case 277:{
			avatar.setImageResource(R.drawable.cgmainchar2);
			dialog.setText("  You try to cheer her up...");
			story++;
			break;
		}
		case 278:{
			avatar.setImageResource(R.drawable.cglady2);
			dialog.setText("  Yeah you're right.\n  There's no time to think about it...\n  Right now, the way to meet him has finally opened.");
			story++;
			break;
		}
		case 279:{
			avatar.setImageResource(R.drawable.cglady2);
			dialog.setText("  It can be our last fight.\n  Once we go, there's no way back.\n  Come on let's go " + pname + "!");
			story++;
			break;
		}
		case 280:{
			avatar.setVisibility(View.INVISIBLE);
			dialog.setText("  The two of you rush into the Menara...");
			story++;
			break;
		}
		case 281:{
			story++;
			tapthedialog = 0;
			updatestory();
			cardgametown();
			break;
		}		
		case 298:{
			avatar.setImageResource(R.drawable.cgfoe11);
			avatar.setVisibility(View.VISIBLE);
			dialog.setText("  Welcome!\n  Your first opponent will be me!");
			story++;
			break;
		}
		case 299:{
			dialog.setText("  Are you ready?\n  Let's begin the battle!");
			story++;
			break;
		}
		case 300:{
			fromarena = true;
			enemyid = 3;
			cardbattle();
			break;
		}
		case 301:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe11);
				dialog.setText("  Hahaha... you're good!");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgfoe11);
					dialog.setText("  I'm pretty good, huh? Hahaha... take your time to prepare!");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgfoe11);
						dialog.setText("  I'm pretty good, huh? Hahaha... take your time to prepare!");
					}
			story++;
			break;
		}
		case 302:{
			if(win == true){
				dialog.setText("  But let's see how you do at the next battle!");
				story++;
			}
			else{
				story = 298;
				cardgametown();
			}		
			break;
		}
		case 303:{
			avatar.setVisibility(View.INVISIBLE);
			dialog.setText("  The image changed into Edmund...");
			story++;
			break;
		}
		case 304:{
			avatar.setImageResource(R.drawable.cgfoe21);
			avatar.setVisibility(View.VISIBLE);
			dialog.setText("  This time I, Edmund will knock you down! Hahaha...");
			story++;
			break;
		}
		case 305:{
			fromarena = true;
			enemyid = 7;
			cardbattle();
			break;
		}
		case 306:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe21);
				dialog.setText("  Hahaha... you're good!");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgfoe21);
					dialog.setText("  I'm pretty good, huh? Hahaha... take your time to prepare!");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgfoe21);
						dialog.setText("  I'm pretty good, huh? Hahaha... take your time to prepare!");
					}
			story++;
			break;
		}
		case 307:{
			if(win == true){
				dialog.setText("  But let's see how you do at the next battle!");
				story++;
			}
			else{
				story = 298;
				cardgametown();
			}		
			break;
		}
		case 308:{
			avatar.setVisibility(View.INVISIBLE);
			dialog.setText("  The image changed into Dartz...");
			story++;
			break;
		}
		case 309:{
			avatar.setImageResource(R.drawable.cgfoe3);
			avatar.setVisibility(View.VISIBLE);
			dialog.setText("  How about the Dartz here who will bring you despair? Hahaha...");
			story++;
			break;
		}
		case 310:{
			fromarena = true;
			enemyid = 11;
			cardbattle();
			break;
		}
		case 311:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe3);
				dialog.setText("  Hahaha... you're good!");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgfoe3);
					dialog.setText("  I'm pretty good, huh? Hahaha... take your time to prepare!");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgfoe3);
						dialog.setText("  I'm pretty good, huh? Hahaha... take your time to prepare!");
					}
			story++;
			break;
		}
		case 312:{
			if(win == true){
				dialog.setText("  But let's see how you do at the next battle!");
				story++;
			}
			else{
				story = 298;
				cardgametown();
			}		
			break;
		}
		case 313:{
			avatar.setVisibility(View.INVISIBLE);
			dialog.setText("  The image changed into Blue...");
			story++;
			break;
		}
		case 314:{
			avatar.setImageResource(R.drawable.cgfoe41);
			avatar.setVisibility(View.VISIBLE);
			dialog.setText("  Blue is here to defeat you!\n  Are you ready!?");
			story++;
			break;
		}
		case 315:{
			fromarena = true;
			enemyid = 15;
			cardbattle();
			break;
		}
		case 316:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe41);
				dialog.setText("  Hahaha... you're good!");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgfoe41);
					dialog.setText("  I'm pretty good, huh? Hahaha... take your time to prepare!");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgfoe41);
						dialog.setText("  I'm pretty good, huh? Hahaha... take your time to prepare!");
					}
			story++;
			break;
		}
		case 317:{
			if(win == true){
				dialog.setText("  But let's see how you do at the next battle!");
				story++;
			}
			else{
				story = 298;
				cardgametown();
			}		
			break;
		}
		case 318:{
			avatar.setVisibility(View.INVISIBLE);
			dialog.setText("  The image changed into Tremmy...");
			story++;
			break;
		}
		case 319:{
			avatar.setImageResource(R.drawable.cgfoe5);
			avatar.setVisibility(View.VISIBLE);
			dialog.setText("  What a cute and strong couple!\n  However, can you even defeat my love!?");
			story++;
			break;
		}
		case 320:{
			fromarena = true;
			enemyid = 19;
			cardbattle();
			break;
		}
		case 321:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe5);
				dialog.setText("  Hahaha... you're good!");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgfoe5);
					dialog.setText("  I'm pretty good, huh? Hahaha... take your time to prepare!");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgfoe5);
						dialog.setText("  I'm pretty good, huh? Hahaha... take your time to prepare!");
					}
			story++;
			break;
		}
		case 322:{
			if(win == true){
				dialog.setText("  You sure are great...\n  This time, I'll let you pass...");
				story++;
			}
			else{
				story = 298;
				cardgametown();
			}		
			break;
		}
		case 323:{
			avatar.setVisibility(View.INVISIBLE);
			dialog.setText("  The image has disappeared...");
			story++;
			break;
		}
		case 324:{
			avatar.setImageResource(R.drawable.cglady3);
			avatar.setVisibility(View.VISIBLE);
			dialog.setText("  Those were tough...\n  If this tower can create those images,\n  then it's no doubt a product from the future.");
			story++;
			break;
		}
		case 325:{
			avatar.setImageResource(R.drawable.cglady);
			dialog.setText("  But the image of Flame didn't show up...\n  I wonder why...");
			story++;
			break;
		}
		case 326:{
			avatar.setImageResource(R.drawable.cglady6);
			dialog.setText("  Hmmm whatever...\n  Let's continue on...");
			story++;
			break;
		}
		case 327:{
			avatar.setVisibility(View.INVISIBLE);
			dialog.setText("  The elevator has gone back to work...\n  The two of you have finally reached the top...");
			story++;
			break;
		}
		case 328:{
			story++;
			tapthedialog = 0;
			updatestory();
			k = 8;
			updatekota();
			cardgametown();
			break;
		}
		case 339:{
			avatar.setImageResource(R.drawable.cgfoe6);
			avatar.setVisibility(View.VISIBLE);
			dialog.setText("  Welcome to the very last arena!\n  I hope that you prepare yourself as there is no way back.");
			story++;
			break;
		}
		case 340:{
			avatar.setImageResource(R.drawable.cglady8);
			dialog.setText("  We won't turn back after all we've done these times!\n  And we will show you right here right now!");
			story++;
			break;
		}
		case 341:{
			avatar.setImageResource(R.drawable.cgfoe61);
			dialog.setText("  That's the spirit!\n  Even if I'm just an image, I won't hold myself!\n  You better get ready!");
			story++;
			break;
		}
		case 342:{
			fromarena = true;
			enemyid = 23;
			cardbattle();
			break;
		}
		case 343:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgfoe6);
				dialog.setText("  Guess I was not able to defeat you...");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgfoe61);
					dialog.setText("  Are you really serious on taking him down with this?\n  If you only can make a draw, then it will be difficult.\n  You should head back and rethink this.");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgfoe61);
						dialog.setText("  Are you really serious on taking him down with this?\n  If you can not defeat me, then it will be difficult.\n  You should head back and rethink this.");
					}
			story++;
			break;
		}
		case 344:{
			if(win == true){
				dialog.setText("  Go ahead and see what you can do!");
				story++;
			}
			else{
				story = 339;
				cardgametown();
			}		
			break;
		}
		case 345:{
			avatar.setVisibility(View.INVISIBLE);
			dialog.setText("  The two of you finally meet Andalucia...");
			story++;
			break;
		}
		case 346:{
			avatar.setImageResource(R.drawable.cgandalucia);
			avatar.setVisibility(View.VISIBLE);
			dialog.setText("  Well done...\n  I've been watching you all this time.\n  I know what you wanted to ask.\n  But can we save it for later?");
			story++;
			break;
		}
		case 347:{
			avatar.setImageResource(R.drawable.cglady8);
			dialog.setText("  Just stop this already!\n  You just make a big mistake here!");
			story++;
			break;
		}
		case 348:{
			avatar.setImageResource(R.drawable.cgandalucia3);
			dialog.setText("  I told you, didn't I?\n  Let's talk after you defeated me!\n  Ready or not, here I come!");
			story++;
			break;
		}
		case 349:{
			fromarena = true;
			enemyid = 24;
			cardbattle();
			break;
		}
		case 350:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgandalucia);
				dialog.setText("  Perfect!\n  That was perfect!");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgandalucia5);
					dialog.setText("  Perhaps that I'm too strong for you?\n  I won't talk if you haven't defeat me.\n  So prepare yourself better!");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgandalucia5);
						dialog.setText("  Perhaps that I'm too strong for you?\n  I won't talk if you haven't defeat me.\n  So prepare yourself better!");
					}
			story++;
			break;
		}
		case 351:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgandalucia);
				dialog.setText("  I just love this game!\n  How about a rematch?");
				story++;
			}
			else{
				story = 339;
				cardgametown();
			}		
			break;
		}
		case 352:{
			dialog.setText("  You better get ready for the next fight!");
			story++;
			break;
		}
		case 353:{
			fromarena = true;
			enemyid = 25;
			cardbattle();
			break;
		}
		case 354:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgandalucia);
				dialog.setText("  Perfect!\n  That was perfect!");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgandalucia5);
					dialog.setText("  Perhaps that I'm too strong for you?\n  I won't talk if you haven't defeat me.\n  So prepare yourself better!");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgandalucia5);
						dialog.setText("  Perhaps that I'm too strong for you?\n  I won't talk if you haven't defeat me.\n  So prepare yourself better!");
					}
			story++;
			break;
		}
		case 355:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgandalucia);
				dialog.setText("  I don't know why, but I feel thrilled!\n  How about a last fight?");
				story++;
			}
			else{
				story = 339;
				cardgametown();
			}		
			break;
		}
		case 356:{
			dialog.setText("  This time, I will be using my super powerful deck!\n  Are you ready?");
			story++;
			break;
		}
		case 357:{
			fromarena = true;
			enemyid = 26;
			cardbattle();
			break;
		}
		case 358:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgandalucia);
				dialog.setText("  Perfect!\n  That was absolutely perfect!");
			}
			else
				if(draw == true){
					avatar.setImageResource(R.drawable.cgandalucia5);
					dialog.setText("  Perhaps that I'm too strong for you?\n  I won't talk if you haven't defeat me.\n  So prepare yourself better!");
				}
				else
					if(lose == true){
						avatar.setImageResource(R.drawable.cgandalucia5);
						dialog.setText("  Perhaps that I'm too strong for you?\n  I won't talk if you haven't defeat me.\n  So prepare yourself better!");
					}
			story++;
			break;
		}
		case 359:{
			if(win == true){
				avatar.setImageResource(R.drawable.cgandalucia);
				dialog.setText("  Alright.\n  As I promise, I will talk about everything to you.");
				story++;
			}
			else{
				story = 339;
				cardgametown();
			}		
			break;
		}
		case 360:{
			avatar.setImageResource(R.drawable.cgandalucia2);
			dialog.setText("  You must be knowing me from Flame...\n  And I will say that those are true.");
			story++;
			break;
		}
		case 361:{
			avatar.setImageResource(R.drawable.cgandalucia1);
			dialog.setText("  I'm just an unlucky man with an uncureable disease.\n  That's why I thought that why don't I help others for good?\n  That's why I created this organization and helped people.");
			story++;
			break;
		}
		case 362:{
			dialog.setText("  Those helped by me actually joined my cause.\n  They agreed to help me back.");
			story++;
			break;
		}
		case 363:{
			dialog.setText("  It was a really wonderful time...\n  until one day, I found this ancient writings.\n  It says that I can be cured...");
			story++;
			break;
		}
		case 364:{
			dialog.setText("  However, it costs other people's blood...\n  I didn't want to spoil out blood, so I created engine\n  to eradicate people from an era.");
			story++;
			break;
		}
		case 365:{
			dialog.setText("  I didn't want to eradicate my era, so I decided to do it to the past.\n  I randomly picked an era and this is what happen.");
			story++;
			break;
		}
		case 366:{
			dialog.setText("  I really didn't want to do it, but somehow,\n  those ancient writings echoe in my head...");
			story++;
			break;
		}
		case 367:{
			dialog.setText("  And actually I managed to get to here.\n  But can you guess what I get?\n  The Nirvana means heaven.\n  To continue to live forever, you need to die.");
			story++;
			break;
		}
		case 368:{
			dialog.setText("  Yes, to live forever in the afterlife... you need to die.\n  So basically I've come here to be fooled...");
			story++;
			break;
		}
		case 369:{
			avatar.setImageResource(R.drawable.cgandalucia5);
			dialog.setText("  So now, I surrender myself...\n  I have set the system that if I lose to you, then everything will return to normal.");
			story++;
			break;
		}
		case 370:{
			dialog.setText("  The people at the towns, the soldiers in Kurukshetra,\n  everyone will return and the history will be restored.");
			story++;
			break;
		}
		case 371:{
			avatar.setImageResource(R.drawable.cglady2);
			dialog.setText("  Thank you.\n  I'm just glad that you've realized your mistake.\n  I've contacted the hq and they will pick you up soon.");
			story++;
			break;
		}
		case 372:{
			avatar.setImageResource(R.drawable.cgandalucia5);
			dialog.setText("  I'm just glad that I didn't end up being a mass murderer...");
			story++;
			break;
		}
		case 373:{
			avatar.setVisibility(View.INVISIBLE);
			dialog.setText("  The police from the future arrived and all members of HISTORY were arrested...");
			story++;
			break;
		}
		case 374:{
			avatar.setImageResource(R.drawable.cglady2);
			avatar.setVisibility(View.VISIBLE);
			dialog.setText("  Thank you," + pname + ".\n  I'm not sure that I can get through this without your help.");
			story++;
			break;
		}
		case 375:{
			avatar.setImageResource(R.drawable.cglady7);
			dialog.setText("  I..I really appreciate your help... really...");
			story++;
			break;
		}
		case 376:{
			avatar.setImageResource(R.drawable.cglady2);
			dialog.setText("  This is where we part...\n  But please don't forget about me, okay?");
			story++;
			break;
		}
		case 377:{
			bg.setVisibility(View.INVISIBLE);
			avatar.setVisibility(View.INVISIBLE);
			dialogtown.setVisibility(View.INVISIBLE);
			dialog.setText("  The two of you say goodbye to each other...\n  You were teleported back to your era...");
			story++;
			break;
		}
		case 378:{
			bg.setImageResource(R.drawable.cg_school);
			bg.setVisibility(View.VISIBLE);
			dialog.setText("  When you open uo your eyes, you found yourself in your class...\n  Was it a dream...?");
			story++;
			break;
		}
		case 379:{
			story = 0;
			tapthedialog = 0;
			updatestory();
			endgame = true;
			stats();
			break;
		}
		}
	}
	
	public void arenaclosed(){
		ImageView avatar = (ImageView) findViewById(R.id.avatarCenter);
		TextView dialog = (TextView) findViewById(R.id.textView4);
		switch(tapthedialog){
		case 0:{
			avatar.setVisibility(View.INVISIBLE);
			dialog.setText("  The arena is closed for now..");
			tapthedialog = 1;
			break;
		}
		case 1:{
			tapthedialog = 0;
			cardgametown();
			break;
		}
		}	
	}
	
	public void map(){
		//set the layout to card_game_name_world_map.xml.
		setContentView(R.layout.activity_card_game_world_map);
		
		//create objects for search.
		TextView town1 = (TextView) findViewById(R.id.textView4);
		TextView town2 = (TextView) findViewById(R.id.textView5);
		TextView town3 = (TextView) findViewById(R.id.textView6);
		TextView town4 = (TextView) findViewById(R.id.textView7);
		TextView town5 = (TextView) findViewById(R.id.textView8);
		TextView town6 = (TextView) findViewById(R.id.textView9);
		TextView town7 = (TextView) findViewById(R.id.textView10);
		TextView town8 = (TextView) findViewById(R.id.textView11);
		TextView town9 = (TextView) findViewById(R.id.textView12);
		TextView back = (TextView) findViewById(R.id.textView13);
		Resources res = getResources();
		String[] kota = res.getStringArray(R.array.Kota);
		
		//set the text for each TextView
		town1.setText("  " + kota[0] + "  ");
		town2.setText("  " + kota[1] + "  ");
		town3.setText("  " + kota[2] + "  ");
		town4.setText("  " + kota[3] + "  ");
		town5.setText("  " + kota[4] + "  ");
		town6.setText("  " + kota[5] + "  ");
		town7.setText("  " + kota[6] + "  ");
		town8.setText("  " + kota[7] + "  ");
		town9.setText("  " + kota[8] + "  ");
		back.setText("  " + "Back" + "  ");
		
		if(story >= 59){
			town3.setVisibility(View.VISIBLE);
			if(story >= 110){
				town4.setVisibility(View.VISIBLE);
				if(story >= 154)
					town5.setVisibility(View.VISIBLE);
				else{
					if(story >= 197)
						town6.setVisibility(View.VISIBLE);
					else{
						if(story >= 235)
							town7.setVisibility(View.VISIBLE);
						else{
							if(story >= 282)
								town8.setVisibility(View.VISIBLE);
							else{
								if(story >= 329)
									town9.setVisibility(View.VISIBLE);
								else{
									town9.setVisibility(View.INVISIBLE);
								}
								town8.setVisibility(View.INVISIBLE);
								town9.setVisibility(View.INVISIBLE);
							}
							town7.setVisibility(View.INVISIBLE);
							town8.setVisibility(View.INVISIBLE);
							town9.setVisibility(View.INVISIBLE);
						}
						town6.setVisibility(View.INVISIBLE);
						town7.setVisibility(View.INVISIBLE);
						town8.setVisibility(View.INVISIBLE);
						town9.setVisibility(View.INVISIBLE);
					}
					town5.setVisibility(View.INVISIBLE);
					town6.setVisibility(View.INVISIBLE);
					town7.setVisibility(View.INVISIBLE);
					town8.setVisibility(View.INVISIBLE);
					town9.setVisibility(View.INVISIBLE);
				}
			}
			else{
				town4.setVisibility(View.INVISIBLE);
				town5.setVisibility(View.INVISIBLE);
				town6.setVisibility(View.INVISIBLE);
				town7.setVisibility(View.INVISIBLE);
				town8.setVisibility(View.INVISIBLE);
				town9.setVisibility(View.INVISIBLE);
			}
		}
		else{
			town3.setVisibility(View.INVISIBLE);
			town4.setVisibility(View.INVISIBLE);
			town5.setVisibility(View.INVISIBLE);
			town6.setVisibility(View.INVISIBLE);
			town7.setVisibility(View.INVISIBLE);
			town8.setVisibility(View.INVISIBLE);
			town9.setVisibility(View.INVISIBLE);
		}
		//set on click listener for town1.
	    town1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				k = 0;
				updatekota();
				cardgametown();
			};
		});	
		
	    //set on click listener for town2.
	    town2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				k = 1;
				updatekota();
				cardgametown();
			};
		});	
	    

	    //set on click listener for town3.
	    town3.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				k = 2;
				updatekota();
				cardgametown();
			};
		});

	    //set on click listener for town4.
	    town4.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				k = 3;
				updatekota();
				cardgametown();
			};
		});

	    //set on click listener for town5.
	    town5.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				k = 4;
				updatekota();
				cardgametown();
			};
		});

	    //set on click listener for town6.
	    town6.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				k = 5;
				updatekota();
				cardgametown();
			};
		});

	    //set on click listener for town7.
	    town7.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				k = 6;
				updatekota();
				cardgametown();
			};
		});

	    //set on click listener for town8.
	    town8.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				k = 7;
				updatekota();
				cardgametown();
			};
		});

	    //set on click listener for town9.
	    town9.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				k = 8;
				updatekota();
				cardgametown();
			};
		});
	    
	    //set on click listener for back.
	    back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				cardgametown();
			};
		});
	}
	
	//method to update player's current town.
	public void updatekota(){
		ContentValues updatevalues = new ContentValues();
	    updatevalues.put("kota", k);
	    long n = DatabaseHelper.updatetable("PEMAIN", updatevalues, "_id="+ID);
	    if(n!=-1){
        	System.out.println("UPDATE SUCCESS");
        }	
        else
        	System.out.println("UPDATE FAILED");
	}
	
	//method to update player's current story.
		public void updatestory(){
			ContentValues updatevalues = new ContentValues();
		    updatevalues.put("cerita", story);
		    long n = DatabaseHelper.updatetable("PEMAIN", updatevalues, "_id="+ID);
		    if(n!=-1){
	        	System.out.println("UPDATE SUCCESS");
	        }	
	        else
	        	System.out.println("UPDATE FAILED");
		}
	
	public void deck(){
		//set the layout to card_game_name_conversation.xml.
		setContentView(R.layout.activity_card_game_deck);
		final TextView kartu1 = (TextView) findViewById(R.id.textView4);
		final TextView kartu2 = (TextView) findViewById(R.id.textView6);
		final TextView kartu3 = (TextView) findViewById(R.id.textView8);
		TextView back = (TextView) findViewById(R.id.textView20);
		
		//set the text of each card in deck.
		String[] pilihtabel1 = {"kartu1", "kartu2", "kartu3"};
		Cursor c1 = myDbHelper.selectrecord("PEMAIN", pilihtabel1, null, null, null, null, null);		
		if(c1!=null){
	    	if(c1.moveToFirst()){
	    		do{
	    			first = c1.getInt(c1.getColumnIndex("kartu1"));
	    			second = c1.getInt(c1.getColumnIndex("kartu2"));
	    			third = c1.getInt(c1.getColumnIndex("kartu3"));
	    		}
	    		while(c1.moveToNext());
	    	}
	    }
		c1.close();
		//store each card's id in temporary variables.
		temp1 = first;
		temp2 = second;
		temp3 = third;
		String[] pilihtabel2 = {"nama", "jumlah"};
		String[] where1 = {Integer.toString(first)};
		String[] where2 = {Integer.toString(second)};
		String[] where3 = {Integer.toString(third)};
		//store each card's quantity in temporary variables.
		Cursor c2 = myDbHelper.selectrecord("KARTU", pilihtabel2, "_id" + "=?", where1, null, null, null);
		if(c2!=null){
			if(c2.moveToFirst()){
	    		do{
	    			kartu1.setText(" " + c2.getString(c2.getColumnIndex("nama")));
	    			tempquantity1 = c2.getInt(c2.getColumnIndex("jumlah"));
	    		}
	    		while(c2.moveToNext());
	    	}
		}
		c2.close();
		Cursor c3 = myDbHelper.selectrecord("KARTU", pilihtabel2, "_id" + "=?", where2, null, null, null);
		if(c3!=null){
			if(c3.moveToFirst()){
	    		do{
	    			kartu2.setText(" " + c3.getString(c3.getColumnIndex("nama")));
	    			tempquantity2 = c3.getInt(c2.getColumnIndex("jumlah"));
	    		}
	    		while(c3.moveToNext());
	    	}
		}
		c3.close();
		Cursor c4 = myDbHelper.selectrecord("KARTU", pilihtabel2, "_id" + "=?", where3, null, null, null);
		if(c4!=null){
			if(c4.moveToFirst()){
	    		do{
	    			kartu3.setText(" " + c4.getString(c4.getColumnIndex("nama")));
	    			tempquantity3 = c4.getInt(c2.getColumnIndex("jumlah"));
	    		}
	    		while(c4.moveToNext());
	    	}
		}
		c4.close();
		
		//set on click listener for kartu1.
	    kartu1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				cardorder = 1;
				editdeck();
			};
		});
	    
	    //set on click listener for kartu2.
	    kartu2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				cardorder = 2;
				editdeck();
			};
		});
	    
	    //set on click listener for kartu3.
	    kartu3.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				cardorder = 3;
				editdeck();
			};
		});
		
		//set on click listener for back.
	    back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Resources res = getResources();
				if(kartu1.getText() == res.getString(R.string.card) || kartu2.getText() == res.getString(R.string.card) || kartu3.getText() == res.getString(R.string.card)){
					Toast.makeText(CardGame.this, "The deck is still not playable. Please make a deck of 3 cards.", Toast.LENGTH_SHORT).show();
					tournamentcheck = false;
				}		
				else{
					tournamentcheck = true;
					cardgametown();
				}					
			};
		});
		
	}
	
	public void editdeck(){
		//set the layout to card_game_name_conversation.xml.
		setContentView(R.layout.activity_card_game_deck_edit);
		
		//create objects for editdeck.
		TextView back = (TextView) findViewById(R.id.textView20);
		final ListView lv = (ListView) findViewById(R.id.listView1);
		
		//fetch the cardlist from database and return it to listview.
		String[] pilihtabel = {"nama", "jumlah"};
		Cursor c = myDbHelper.selectrecord("KARTU", pilihtabel, "jumlah>0", null, null, null, null);	
		final String[] cardlist = new String[c.getCount()];
		final String[] cardlistquantity = new String[c.getCount()];
		if(c!=null){
	    	if(c.moveToFirst()){
	    		for(int i = 0; i < c.getCount(); i++){
	    			cardlist[i] = c.getString(c.getColumnIndex("nama"));
	    			cardlistquantity[i] = "Own : " + Integer.toString(c.getInt(c.getColumnIndex("jumlah")));
	    			c.moveToNext();
	    		}		
	    	}
		}
		c.close();
		inventory = cardlist.length;
		ArrayList<ListItem> ListItem = GetSearchResults(cardlist, cardlistquantity);		
		lv.setAdapter(new ListItemAdapter(this, ListItem));
		
		//the method run when an item on listview is clicked.
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//get the card's index including the card's image and show its details by calling choosecard().
				String[] pilihtabel1 = {"nama","deskripsi","nyawa","serangan","kemampuan","kelangkaan","gambar","jumlah"};
				String[] wherearg = {cardlist[position]};
				Cursor c1 = myDbHelper.selectrecord("KARTU", pilihtabel1, "nama" + "=?", wherearg, null, null, null);		
				if(c1!=null){
			    	if(c1.moveToFirst()){
			    		do{
			    			cname = c1.getString(c1.getColumnIndex("nama"));
			    			cdesc = c1.getString(c1.getColumnIndex("deskripsi"));
			    			chp = c1.getInt(c1.getColumnIndex("nyawa"));
			    			cap = c1.getInt(c1.getColumnIndex("serangan"));
			    			cabilityid = c1.getInt(c1.getColumnIndex("kemampuan"));
			    			crarity = c1.getString(c1.getColumnIndex("kelangkaan"));			    			
			    			cfilename = c1.getString(c1.getColumnIndex("gambar"));
			    			cquantity = c1.getInt(c1.getColumnIndex("jumlah"));
			    		}
			    		while(c1.moveToNext());
			    	}
			    }
				c1.close();
				String[] pilihtabel2 = {"nama", "deskripsi", "jenis"};
				String[] wherearg1 = {Integer.toString(cabilityid)};
				Cursor c2 = myDbHelper.selectrecord("KEMAMPUAN", pilihtabel2, "_id" + "=?", wherearg1, null, null, null);
				if(c2!=null){
					if(c2.moveToFirst()){
			    		do{
			    			cabilityname = c2.getString(c2.getColumnIndex("nama"));
			    			cabilitydesc = c2.getString(c2.getColumnIndex("deskripsi"));
			    			cabilitytype = c2.getString(c2.getColumnIndex("jenis"));
			    		}
			    		while(c2.moveToNext());
			    	}
				}
				c2.close();
				choosecard();
			}
		});		
		
		//set on click listener for back.
	    back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				deck();
			};
		});
		
	}
	
	private ArrayList<ListItem> GetSearchResults(String cardlist[], String cardlistquantity[]){
	     ArrayList<ListItem> results = new ArrayList<ListItem>();
	 
	     for(int i = 0; i < inventory; ++i){
	    	 ListItem sr = new ListItem();
		     sr.setcol1(cardlist[i]);
		     sr.setcol2(cardlistquantity[i]);
		     sr.setcol3(" ");
		     results.add(sr);
	     }
	     
	     return results;
	    }
	
	public void choosecard(){
		//set the layout to card_game_name_stats.xml.
		setContentView(R.layout.activity_card_game_deck_choose);

		//set the card's image based on the fileName passed by edittext().
		ImageView gbrkartu = (ImageView) findViewById(R.id.imageView1);
		int image = getApplicationContext().getResources().getIdentifier(cfilename, "drawable", getApplicationContext().getPackageName());
        gbrkartu.setImageResource(image);
        
		//create objects for cardgamestats.
        TextView cardName = (TextView) findViewById(R.id.textView4);
        TextView cardHP = (TextView) findViewById(R.id.textView9);
        TextView cardAP = (TextView) findViewById(R.id.textView11);
        TextView cardRarity = (TextView) findViewById(R.id.textView13);
        TextView cardAbilityName = (TextView) findViewById(R.id.textView15);
        TextView cardAbilityType = (TextView) findViewById(R.id.textView17);
        TextView cardAbilityDesc = (TextView) findViewById(R.id.textView19);
        TextView cardDesc = (TextView) findViewById(R.id.textView20);    
		TextView choose = (TextView) findViewById(R.id.textView5);
		TextView cancel = (TextView) findViewById(R.id.textView7);
		
		//set the card's details.
		cardName.setText(cname);
		cardHP.setText(Integer.toString(chp));
		cardAP.setText(Integer.toString(cap));
		cardRarity.setText(crarity);
		cardAbilityName.setText(cabilityname);
		cardAbilityType.setText(cabilitytype);
		cardAbilityDesc.setText(cabilitydesc);
		cardDesc.setText(cdesc);
		
		//set on click listener for choose.
	    choose.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//set the card into the database based on the order it's chosen (1st card, 2nd card, or 3rd card).
				int idkartu = 0, jlhkartu = 0;
				String[] pilihtabel = {"_id", "jumlah"};
				String[] wherearg = {cname};
				Cursor c2 = myDbHelper.selectrecord("KARTU", pilihtabel, "nama" + "=?", wherearg, null, null, null);
				if(c2!=null){
					if(c2.moveToFirst()){
			    		do{
			    			jlhkartu = c2.getInt(c2.getColumnIndex("jumlah"));
			    			idkartu = c2.getInt(c2.getColumnIndex("_id"));
			    		}
			    		while(c2.moveToNext());
			    	}
				}
				c2.close();
				//if the same card is chosen again, prevent it to reduce its quantity, reduce it otherwise.
				ContentValues updatevalues = new ContentValues();
				ContentValues updatevalues1 = new ContentValues();
				if(cardorder==1){
					updatevalues.put("kartu1", idkartu);
					if(idkartu!=temp1)
						updatevalues1.put("jumlah", jlhkartu - 1);
					else
						updatevalues1.put("jumlah", jlhkartu);
					Toast.makeText(CardGame.this, "Your 1st card is " + cname, Toast.LENGTH_SHORT).show();
				}
				else
					if(cardorder==2){
						updatevalues.put("kartu2", idkartu);
						if(idkartu!=temp2)
							updatevalues1.put("jumlah", jlhkartu - 1);
						else
							updatevalues1.put("jumlah", jlhkartu);
						Toast.makeText(CardGame.this, "Your 2nd card is " + cname, Toast.LENGTH_SHORT).show();
					}
					else
						if(cardorder==3){
							updatevalues.put("kartu3", idkartu);
							if(idkartu!=temp3)
								updatevalues1.put("jumlah", jlhkartu - 1);
							else
								updatevalues1.put("jumlah", jlhkartu);
							Toast.makeText(CardGame.this, "Your 3rd card is " + cname, Toast.LENGTH_SHORT).show();
						}
			    long m = DatabaseHelper.updatetable("PEMAIN", updatevalues, "_id="+ID);
			    if(m!=-1){
		        	System.out.println("UPDATE SUCCESS");
		        }	
		        else
		        	System.out.println("UPDATE FAILED");
			    long n = DatabaseHelper.updatetable("KARTU", updatevalues1,"_id="+idkartu);
			    if(n!=-1){
		        	System.out.println("UPDATE SUCCESS");
		        }	
		        else
		        	System.out.println("UPDATE FAILED");
			    //if the id of cards in temporary is not the same with the the current one, it means the card in that order
			    //is changed into the new one.
			    //the quantity reduced before must be restored to the one stored in the temporary.
			    String[] pilihtabel1 = {"kartu1", "kartu2", "kartu3"};
				Cursor c1 = myDbHelper.selectrecord("PEMAIN", pilihtabel1, null, null, null, null, null);		
				if(c1!=null){
			    	if(c1.moveToFirst()){
			    		do{
			    			firstcard = c1.getInt(c1.getColumnIndex("kartu1"));
			    			secondcard = c1.getInt(c1.getColumnIndex("kartu2"));
			    			thirdcard = c1.getInt(c1.getColumnIndex("kartu3"));
			    		}
			    		while(c1.moveToNext());
			    	}
			    }
				c1.close();
				ContentValues updatecv1 = new ContentValues();
				ContentValues updatecv2 = new ContentValues();
				ContentValues updatecv3 = new ContentValues();
				if(firstcard!=temp1){
					updatecv1.put("jumlah", tempquantity1 + 1);
					long o = DatabaseHelper.updatetable("KARTU", updatecv1,"_id="+temp1);
					if(o!=-1){
						System.out.println("UPDATE SUCCESS");
					}	
					else
						System.out.println("UPDATE FAILED");
					}
				if(secondcard!=temp2){
					updatecv2.put("jumlah", tempquantity2 + 1);
					long p = DatabaseHelper.updatetable("KARTU", updatecv2,"_id="+temp2);
					if(p!=-1){
						System.out.println("UPDATE SUCCESS");
					}	
					else
						System.out.println("UPDATE FAILED");
					}
				if(thirdcard!=temp3){
					updatecv3.put("jumlah", tempquantity3 + 1);
					long q = DatabaseHelper.updatetable("KARTU", updatecv3,"_id="+temp3);
					if(q!=-1){
						System.out.println("UPDATE SUCCESS");
					}	
					else
						System.out.println("UPDATE FAILED");
					}
				
			    deck();
			};
		});

		//set on click listener for cancel.
	    cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				editdeck();
			};
		});
	}
	
	
	public void stats(){
		//set the layout to card_game_name_stats.xml.
		setContentView(R.layout.activity_card_game_stats);
		backdisable = true;
		
		//create objects for cardgamestats.
		TextView namap = (TextView) findViewById(R.id.textView4);
		TextView jlh = (TextView) findViewById(R.id.textView6);
		TextView menang = (TextView) findViewById(R.id.textView8);
		TextView menangpersen = (TextView) findViewById(R.id.textView10);
		TextView kalah = (TextView) findViewById(R.id.textView12);
		TextView kalahpersen = (TextView) findViewById(R.id.textView14);
		TextView seri = (TextView) findViewById(R.id.textView16);
		TextView seripersen = (TextView) findViewById(R.id.textView18);	
		TextView back = (TextView) findViewById(R.id.textView20);
		String nama = new String();
		int menang1 = 0, kalah1 = 0, seri1 = 0;
		
		if(endgame == true)
			back.setText("End Game and Return to Main Menu");
		
		//fetch the record in table "PEMAIN".
		String[] pilihtabel = {"nama", "jlh_menang", "jlh_kalah", "jlh_seri"};
	    Cursor c = myDbHelper.selectrecord("PEMAIN", pilihtabel, null, null, null, null, null);
	    if(c!=null){
	    	if(c.moveToFirst()){
	    		do{
	    			nama = c.getString(c.getColumnIndex("nama"));
	    			menang1 = c.getInt(c.getColumnIndex("jlh_menang"));
	    			kalah1 = c.getInt(c.getColumnIndex("jlh_kalah"));
	    			seri1 = c.getInt(c.getColumnIndex("jlh_seri"));
	    		}
	    		while(c.moveToNext());
	    	}
	    }
	    c.close();
	    
	    //calculate the no.of battle and percentage of win, lose, draw.
	    int jumlah = menang1 + kalah1 + seri1;
	    if(menang1==0)
	    	mngpersen = 0;
	    else
	    	mngpersen = ((float) (menang1) / (float) (jumlah)) * 100;
	    if(kalah1==0)
	    	klhpersen = 0;
	    else
	    	klhpersen = ((float) (kalah1) / (float) (jumlah)) * 100;
	    if(seri1==0)
	    	srpersen = 0;
	    else
	    	srpersen = ((float) (seri1) / (float) (jumlah)) * 100;
	    
	    //set the TextView to the fetched data.
	    namap.setText(nama.toString());
	    menang.setText(Integer.toString(menang1));
	    kalah.setText(Integer.toString(kalah1));
	    seri.setText(Integer.toString(seri1));
	    jlh.setText(Integer.toString(jumlah));
	    menangpersen.setText(String.format("%.02f", mngpersen));
	    kalahpersen.setText(String.format("%.02f", klhpersen));
	    seripersen.setText(String.format("%.02f", srpersen));
	    
	    //set on click listener for back.
	    back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(endgame == true){
					cardgameresult();
					endgame = false;
					backdisable = false;
					cardgamemainmenu();
				}
				else{
					backdisable = false;
					cardgametown();
				}
			};
		});	
	    
	}
	
	public void cardbattle(){
		//set the layout to card_game_battle.xml.
		setContentView(R.layout.activity_card_game_battle);
		
		//disable the back button.
		backdisable = true;
		
		//initialize variables
		phase = 1; pturnnum = 1; eturnnum = 1; plose = 0; elose = 0; puse = 0; euse = 0; doubledmg = false; pturncheck = false; eturncheck = false; win = false; lose = false; draw = false;
		
		//create objects for cardbattle.
		TextView pnama = (TextView) findViewById(R.id.textView1);	
		TextView enama = (TextView) findViewById(R.id.textView3);	
		final TextView pcardhp = (TextView) findViewById(R.id.textView6);
		final TextView pcardap = (TextView) findViewById(R.id.textView8);
		final TextView ecardhp = (TextView) findViewById(R.id.textView10);
		final TextView ecardap = (TextView) findViewById(R.id.textView12);
	    final ImageView pcard1 = (ImageView) findViewById(R.id.imageView1);
	    final ImageView pcard2 = (ImageView) findViewById(R.id.imageView3);
	    final ImageView pcard3 = (ImageView) findViewById(R.id.imageView5);
	    final ImageView ecard1 = (ImageView) findViewById(R.id.imageView2);
	    final ImageView ecard2 = (ImageView) findViewById(R.id.imageView4);
	    final ImageView ecard3 = (ImageView) findViewById(R.id.imageView6);
	    final TextView message = (TextView) findViewById(R.id.textView4);
	    final TextView pturn = (TextView) findViewById(R.id.textView9);
	    final TextView eturn = (TextView) findViewById(R.id.textView11);
	    final Button yes = (Button) findViewById(R.id.button1);
	    final Button nextorno = (Button) findViewById(R.id.button2);
	    pturn.setVisibility(View.INVISIBLE);
	    eturn.setVisibility(View.INVISIBLE);
	    yes.setVisibility(View.INVISIBLE);
	    yes.setEnabled(false);
	    nextorno.setText("Next");
	    
		//fetch the id of each card in deck.
		String[] pilihtabel1 = {"kartu1", "kartu2", "kartu3"};
		Cursor c = myDbHelper.selectrecord("PEMAIN", pilihtabel1, null, null, null, null, null);		
		if(c!=null){
	    	if(c.moveToFirst()){
	    		do{
	    			p[0][0] = c.getInt(c.getColumnIndex("kartu1"));
	    			p[0][1] = c.getInt(c.getColumnIndex("kartu2"));
	    			p[0][2] = c.getInt(c.getColumnIndex("kartu3"));
	    		}
	    		while(c.moveToNext());
	    	}
	    }
		c.close();
		String[] pilihtabel3 = {"kartu1", "kartu2", "kartu3"};
		String[] whereenemyid = {""};
		whereenemyid[0] = Integer.toString(enemyid);
		c = myDbHelper.selectrecord("LAWAN", pilihtabel3, "_id" + "=?", whereenemyid, null, null, null);		
		if(c!=null){
	    	if(c.moveToFirst()){
	    		do{
	    			p[1][0] = c.getInt(c.getColumnIndex("kartu1"));
	    			p[1][1] = c.getInt(c.getColumnIndex("kartu2"));
	    			p[1][2] = c.getInt(c.getColumnIndex("kartu3"));
	    		}
	    		while(c.moveToNext());
	    	}
	    }
		c.close();
		//fetch the details of each card in deck.
		String[] pilihtabel2 = {"nama","nyawa","serangan","kemampuan","gambar"};
		String[] where1 = {""}, where2 = {""}, where3 = {""};
		for(int i = 0; i < 2; ++i){
			where1[0] = Integer.toString(p[i][0]);
			where2[0] = Integer.toString(p[i][1]);
			where3[0] = Integer.toString(p[i][2]);
			c = myDbHelper.selectrecord("KARTU", pilihtabel2, "_id" + "=?", where1, null, null, null);
			if(c!=null){
				if(c.moveToFirst()){
		    		do{
		    			p[i][3] = c.getInt(c.getColumnIndex("nyawa"));
		    			p[i][6] = c.getInt(c.getColumnIndex("serangan"));
		    			p[i][9] = c.getInt(c.getColumnIndex("kemampuan"));
		    			pcard[i][0] = c.getString(c.getColumnIndex("gambar"));
		    			pcard[i][3] = c.getString(c.getColumnIndex("nama"));
		    		}
		    		while(c.moveToNext());
				}
			}
			c.close();
			c = myDbHelper.selectrecord("KARTU", pilihtabel2, "_id" + "=?", where2, null, null, null);
			if(c!=null){
				if(c.moveToFirst()){
		    		do{
		    			p[i][4] = c.getInt(c.getColumnIndex("nyawa"));
		    			p[i][7] = c.getInt(c.getColumnIndex("serangan"));
		    			p[i][10] = c.getInt(c.getColumnIndex("kemampuan"));
		    			pcard[i][1] = c.getString(c.getColumnIndex("gambar"));
		    			pcard[i][4] = c.getString(c.getColumnIndex("nama"));
		    		}
		    		while(c.moveToNext());
				}
			}
			c.close();
			c = myDbHelper.selectrecord("KARTU", pilihtabel2, "_id" + "=?", where3, null, null, null);
			if(c!=null){
				if(c.moveToFirst()){
		    		do{
		    			p[i][5] = c.getInt(c.getColumnIndex("nyawa"));
		    			p[i][8] = c.getInt(c.getColumnIndex("serangan"));
		    			p[i][11] = c.getInt(c.getColumnIndex("kemampuan"));
		    			pcard[i][2] = c.getString(c.getColumnIndex("gambar"));
		    			pcard[i][5] = c.getString(c.getColumnIndex("nama"));
		    		}
		    		while(c.moveToNext());
				}
			}
			c.close();
			String[] where4 = {Integer.toString(p[i][9])}, where5 = {Integer.toString(p[i][10])}, where6 = {Integer.toString(p[i][11])};
			String[] pilihtabel4 = {"nama", "deskripsi"};
			c = myDbHelper.selectrecord("KEMAMPUAN", pilihtabel4, "_id" + "=?", where4, null, null, null);
			if(c!=null){
				if(c.moveToFirst()){
		    		do{
		    			pcard[i][6] = c.getString(c.getColumnIndex("nama"));
		    			pcard[i][9] = c.getString(c.getColumnIndex("deskripsi"));
		    		}
		    		while(c.moveToNext());
				}
			}
			c.close();
			c = myDbHelper.selectrecord("KEMAMPUAN", pilihtabel4, "_id" + "=?", where5, null, null, null);
			if(c!=null){
				if(c.moveToFirst()){
		    		do{
		    			pcard[i][7] = c.getString(c.getColumnIndex("nama"));
		    			pcard[i][10] = c.getString(c.getColumnIndex("deskripsi"));
		    		}
		    		while(c.moveToNext());
				}
			}
			c.close();
			c = myDbHelper.selectrecord("KEMAMPUAN", pilihtabel4, "_id" + "=?", where6, null, null, null);
			if(c!=null){
				if(c.moveToFirst()){
		    		do{
		    			pcard[i][8] = c.getString(c.getColumnIndex("nama"));
		    			pcard[i][11] = c.getString(c.getColumnIndex("deskripsi"));
		    		}
		    		while(c.moveToNext());
				}
			}
			c.close();
		}

		//set each card's images, life, and attack; if a card is defeated, it is darkened.
		if(plose == 0){
			pcard1.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[0][0], "drawable", getApplicationContext().getPackageName()));
			pcard2.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[0][1], "drawable", getApplicationContext().getPackageName()));
			pcard3.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[0][2], "drawable", getApplicationContext().getPackageName()));
			pcardhp.setText(Integer.toString(p[0][3]));
			pcardap.setText(Integer.toString(p[0][6]));
		}
		else
			if(plose == 1){
				pcard1.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[0][1], "drawable", getApplicationContext().getPackageName()));
				pcard2.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[0][2], "drawable", getApplicationContext().getPackageName()));
				pcard3.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[0][0], "drawable", getApplicationContext().getPackageName()));
				pcard3.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
				pcardhp.setText(Integer.toString(p[0][4]));
				pcardap.setText(Integer.toString(p[0][7]));
			}
			else
				if(plose == 2){
					pcard1.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[0][2], "drawable", getApplicationContext().getPackageName()));
					pcard2.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[0][0], "drawable", getApplicationContext().getPackageName()));
					pcard3.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[0][1], "drawable", getApplicationContext().getPackageName()));
					pcard2.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
					pcard3.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
					pcardhp.setText(Integer.toString(p[0][5]));
					pcardap.setText(Integer.toString(p[0][8]));
				}
				else{
					pcard1.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
					pcard2.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
					pcard3.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
					pcardhp.setText(Integer.toString(p[0][5]));
					pcardap.setText(Integer.toString(p[0][8]));
				}
					
		if(elose == 0){
			ecard1.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[1][0], "drawable", getApplicationContext().getPackageName()));
			ecard2.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[1][1], "drawable", getApplicationContext().getPackageName()));
			ecard3.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[1][2], "drawable", getApplicationContext().getPackageName()));
			ecardhp.setText(Integer.toString(p[1][3]));
			ecardap.setText(Integer.toString(p[1][6]));
		}
		else
			if(elose == 1){
				ecard1.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[1][1], "drawable", getApplicationContext().getPackageName()));
				ecard2.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[1][2], "drawable", getApplicationContext().getPackageName()));
				ecard3.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[1][0], "drawable", getApplicationContext().getPackageName()));
				ecard3.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
				ecardhp.setText(Integer.toString(p[1][4]));
				ecardap.setText(Integer.toString(p[1][7]));
			}
			else
				if(elose == 2){
					ecard1.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[1][2], "drawable", getApplicationContext().getPackageName()));
					ecard2.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[1][0], "drawable", getApplicationContext().getPackageName()));
					ecard3.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[1][1], "drawable", getApplicationContext().getPackageName()));
					ecard2.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
					ecard3.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
					ecardhp.setText(Integer.toString(p[1][5]));
					ecardap.setText(Integer.toString(p[1][8]));
				}
				else{
					ecard1.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
					ecard2.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
					ecard3.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
					ecardhp.setText(Integer.toString(p[1][5]));
					ecardap.setText(Integer.toString(p[1][8]));
				}
		
		//fetch the player's data from table "PEMAIN".
		String[] pilihtabel = {"nama", "jlh_menang", "jlh_kalah", "jlh_seri"};
	    c = myDbHelper.selectrecord("PEMAIN", pilihtabel, null, null, null, null, null);
	    if(c!=null){
	    	if(c.moveToFirst()){
	    		do{
	    			pnm[0] = c.getString(c.getColumnIndex("nama"));
	    			pnama.setText(c.getString(c.getColumnIndex("nama")));
	    			menang1 = c.getInt(c.getColumnIndex("jlh_menang"));
	    			kalah1 = c.getInt(c.getColumnIndex("jlh_kalah"));
	    			seri1 = c.getInt(c.getColumnIndex("jlh_seri"));
	    		}
	    		while(c.moveToNext());
	    	}
	    }
	    c.close();
	    
		//fetch the enemy's data from table "LAWAN".
		String[] pilihtabel6 = {"nama"};
	    c = myDbHelper.selectrecord("LAWAN", pilihtabel6, "_id" + "=?", whereenemyid, null, null, null);
	    if(c!=null){
	    	if(c.moveToFirst()){
	    		do{
	    			pnm[1] = c.getString(c.getColumnIndex("nama"));
	    			enama.setText(c.getString(c.getColumnIndex("nama")));
	    		}
	    		while(c.moveToNext());
	    	}
	    } 
	    c.close();
	    
	    //the method to determine who gets the first turn.
	    //turn=0 -> the player gets the first turn.
	    //turn=1 -> the opponent gets the first turn.
	    Random rnd = new Random(); 
	    turn = rnd.nextInt(2); 
	    
	    //if player gets the first turn, pturncheck becomes true.
	    //otherwise, eturncheck becomes true.
	    //turn=0 -> player.
	    //turn=1 -> enemy.
	    //set the text <<Turn or Turn>> respctively. (See the layout.)
	    if(turn == 0){
	    	message.setText(" " + pnm[0] + " gets the first turn!");
	    	pturn.setVisibility(View.VISIBLE);
	    	pturncheck = true;
	    }
	    else
	    	if(turn == 1){
	    		message.setText(" " + pnm[1] + " gets the first turn!");
	    		eturn.setVisibility(View.VISIBLE);
	    		eturncheck = true;
	    	}
	    		
	    //the battle process (each click on button alter the value of phase)
	    
	    pcard1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(plose == 0)
					Toast.makeText(CardGame.this, pcard[0][6] + " : " + pcard[0][9], Toast.LENGTH_SHORT).show();
				else if(plose == 1)
					Toast.makeText(CardGame.this, pcard[0][7] + " : " + pcard[0][10], Toast.LENGTH_SHORT).show();
				else if(plose == 2 || plose == 3)
					Toast.makeText(CardGame.this, pcard[0][8] + " : " + pcard[0][11], Toast.LENGTH_SHORT).show();		
			};
		});	
	    
	    pcard2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(plose == 0)
					Toast.makeText(CardGame.this, pcard[0][7] + " : " + pcard[0][10], Toast.LENGTH_SHORT).show();
				else if(plose == 1)
					Toast.makeText(CardGame.this, pcard[0][8] + " : " + pcard[0][11], Toast.LENGTH_SHORT).show();
				else if(plose == 2 || plose == 3)
					Toast.makeText(CardGame.this, pcard[0][6] + " : " + pcard[0][9], Toast.LENGTH_SHORT).show();
			};
		});	
	    
	    pcard3.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(plose == 0)
					Toast.makeText(CardGame.this, pcard[0][8] + " : " + pcard[0][11], Toast.LENGTH_SHORT).show();
				else if(plose == 1)
					Toast.makeText(CardGame.this, pcard[0][6] + " : " + pcard[0][9], Toast.LENGTH_SHORT).show();
				else if(plose == 2 || plose == 3)
					Toast.makeText(CardGame.this, pcard[0][7] + " : " + pcard[0][10], Toast.LENGTH_SHORT).show();
			};
		});	
	    
	    ecard1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(elose == 0)
					Toast.makeText(CardGame.this, pcard[1][6] + " : " + pcard[1][9], Toast.LENGTH_SHORT).show();
				else if(elose == 1)
					Toast.makeText(CardGame.this, pcard[1][7] + " : " + pcard[1][10], Toast.LENGTH_SHORT).show();
				else if(elose == 2 || elose == 3)
					Toast.makeText(CardGame.this, pcard[1][8] + " : " + pcard[1][11], Toast.LENGTH_SHORT).show();
			};
		});	
	    
	    ecard2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(elose == 0)
					Toast.makeText(CardGame.this, pcard[1][7] + " : " + pcard[1][10], Toast.LENGTH_SHORT).show();
				else if(elose == 1)
					Toast.makeText(CardGame.this, pcard[1][8] + " : " + pcard[1][11], Toast.LENGTH_SHORT).show();
				else if(elose == 2 || elose == 3)
					Toast.makeText(CardGame.this, pcard[1][6] + " : " + pcard[1][9], Toast.LENGTH_SHORT).show();
			};
		});	
	    
	    ecard3.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(elose == 0)
					Toast.makeText(CardGame.this, pcard[1][8] + " : " + pcard[1][11], Toast.LENGTH_SHORT).show();
				else if(elose == 1)
					Toast.makeText(CardGame.this, pcard[1][6] + " : " + pcard[1][9], Toast.LENGTH_SHORT).show();
				else if(elose == 2 || elose == 3)
					Toast.makeText(CardGame.this, pcard[1][7] + " : " + pcard[1][10], Toast.LENGTH_SHORT).show();
			};
		});	
	    
	    
	    //this method will only run when the yes button is visible and enabled.
	    //that means, this method will only run at phase2: deciding to use an active ability or not.
	    yes.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				yes.setVisibility(View.INVISIBLE);
			    yes.setEnabled(false);
			    nextorno.setText("Next");
				message.setText(" " + pnm[0] + "'s " + pcard[0][plose + 3] + " uses " + pcard[0][plose + 6] + "!");
				phase = 4;
			};
		});	
	    
	    //this is the main method to manage the flow of the battle.
	    //the text of the button will change into no only at phase2: deciding to use an active ability or not.
	    //if the user decides to use ability, then that means after the last next, the yes onclicklistener will run.
	    //else this onclicklistener will run.
	    nextorno.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(pturncheck == true)
					turn = 0;
				else
					turn = 1;
				switch(phase){
				case 1:{
					if(pturnnum > 8 && eturnnum > 8){
						plose++;
						elose++;
						message.setText(" Both cards have reached maximum turn!");
						if(pturncheck == true && eturncheck == false){
							pturncheck = false;
							eturncheck = true;
						}
						else
							if(pturncheck == false && eturncheck == true){
								pturncheck = true;
								eturncheck = false;
							}
						phase = 8;
						break;
					}	
					if(pturncheck == true){
						pturn.setVisibility(View.VISIBLE);
						eturn.setVisibility(View.INVISIBLE);
						message.setText(" Turn " + pturnnum + " of " + pnm[0] + "!");
						phase = 2;
						break;
					}
						else
						if(eturncheck == true){
							pturn.setVisibility(View.INVISIBLE);
							eturn.setVisibility(View.VISIBLE);
							message.setText(" Turn " + eturnnum + " of " + pnm[1] + "!");
							phase = 2;
							break;
						}
				}
				case 2:{
					if(eturncheck == true){
						int euseability;
						abi = p[1][elose + 9];
						//if enemy's ability is active, randomize whether to use it or not.
						//if it's passive, immediately use it.
						if(abi==1 || abi==2 || abi==5 || abi==6 || abi==7 || abi==11 || abi==12 || abi==13 || abi==14 || abi==19 || abi==20 || abi==21 || abi==22 || abi==23 || abi==24){
							Random rdm = new Random();
							euseability = rdm.nextInt(2);
						}
						else{
							euseability = 0;
						}	
						if(euseability == 0){ //enemy use skill.
							message.setText(" " + pnm[1] + "'s " + pcard[1][elose + 3] + " uses " + pcard[1][elose + 6] + "!");
							phase = 4;
						}
						else{ //enemy doesn't use skill.
							message.setText(" " + pnm[1] + "'s " + pcard[1][elose + 3] + " doesn't use ability!");
							phase = 5; //skip to phase 5.
						}
					}
					else
						if(pturncheck == true){
							abi = p[0][plose + 9];
							//if player's ability is active, ask player to use it or not.
							//if it is passive, immediately use it.
							if(abi==1 || abi==2 || abi==5 || abi==6 || abi==7 || abi==11 || abi==12 || abi==13 || abi==14 || abi==19 || abi==20 || abi==21 || abi==22 || abi==23 || abi==24){
								message.setText(" Do you want to use ability?");
								yes.setVisibility(View.VISIBLE);
								yes.setEnabled(true);
								nextorno.setText("No");
								phase = 3;
							}
							else{
								yes.setVisibility(View.INVISIBLE);
							    yes.setEnabled(false);
							    nextorno.setText("Next");
								message.setText(" " + pnm[0] + "'s " + pcard[0][plose + 3] + " uses " + pcard[0][plose + 6] + "!");
								phase = 4;
							}	
						}
					break;
				}
				case 3:{
					yes.setVisibility(View.INVISIBLE);
				    yes.setEnabled(false);
				    nextorno.setText("Next");
					if(pturncheck == true){ //show this message just to the player since the message for enemy has been displayed.
						message.setText(" " + pnm[0] + "'s " + pcard[0][plose + 3] + " doesn't use ability!");
						phase = 5; //skip to phase 5.
					}
					break;
				}
				case 4:{
					message.setText(resolveability(abi));
					//if a card is defeated from an ability effect, then go to check hp and skip the attack phase.
					if(p[0][plose + 3] <= 0){
						plose++;
						pturnnum = 1;
						eturnnum = 1;
						puse = 0;
						phase = 7;
					}	
						else
							if(p[1][elose + 3] <= 0){
								elose++;
								pturnnum = 1;
								eturnnum = 1;
								euse = 0;
								phase = 7;
							}
							else{
								//update the display.
								pcardhp.setText(Integer.toString(p[0][plose + 3]));
								pcardap.setText(Integer.toString(p[0][plose + 6]));
								if(p[0][plose + 6] <= 0){
									p[0][plose + 6] = 1;
									pcardap.setText("1");
								}
								ecardhp.setText(Integer.toString(p[1][elose + 3]));
								ecardap.setText(Integer.toString(p[1][elose + 6]));
								if(p[1][elose + 6] <= 0){
									p[1][elose + 6] = 1;
									ecardap.setText("1");
								}
								phase = 5;
							}		
					break;
				}
				case 5:{
					if(pturnnum > 8 && eturnnum > 8){
						plose++;
						elose++;
						message.setText(" Both cards have reached maximum turn!");
						phase = 8;
						break;
					}
					if(pturncheck == true)
						message.setText(" " + pnm[turn] + "'s " + pcard[turn][plose + 3] + " attacks!");
					else
						message.setText(" " + pnm[turn] + "'s " + pcard[turn][elose + 3] + " attacks!");
					phase = 6;
					break;
				}
				case 6:{
					if(turn == 0){
						if(doubledmg == false){
							p[1][elose + 3] -= p[0][plose + 6];
							message.setText(" " + pnm[turn] + "'s " + pcard[turn][plose + 3] + " deals " + Integer.toString(p[turn][plose + 6]) + " damage!");
						}
						else{
							p[1][elose + 3] -= (p[0][plose + 6] * 2);
							message.setText(" " + pnm[turn] + "'s " + pcard[turn][plose + 3] + " deals " + Integer.toString(p[turn][plose + 6] * 2) + " damage!");
							doubledmg = false;
						}
						
					}
						else{
							if(doubledmg == false){
								p[0][plose + 3] -= p[1][elose + 6];
								message.setText(" " + pnm[turn] + "'s " + pcard[turn][elose + 3] + " deals " + Integer.toString(p[turn][elose + 6]) + " damage!");
							}
								
							else{
								p[0][plose + 3] -= (p[1][elose + 6] * 2);
								message.setText(" " + pnm[turn] + "'s " + pcard[turn][elose + 3] + " deals " + Integer.toString(p[turn][elose + 6] * 2) + " damage!");
								doubledmg = false;
							}	
						}
					if(p[0][plose + 3] <= 0){
						plose++;
						pturnnum = 1;
						eturnnum = 1;
						puse = 0;
						phase = 7;
					}	
						else
							if(p[1][elose + 3] <= 0){
								elose++;
								pturnnum = 1;
								eturnnum = 1;
								euse = 0;
								phase = 7;
							}
							else
								phase = 8;
					break;
				}
				case 7:{
					if(pturncheck == true)
						message.setText(" " + pnm[1] + "'s " + pcard[1][elose + 2] + " is defeated!");
					else
						if(eturncheck == true)
							message.setText(" " + pnm[0] + "'s " + pcard[0][plose + 2] + " is defeated!");
					phase = 8;
				}
				case 8:{
					if(plose == 0){
						pcard1.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[0][0], "drawable", getApplicationContext().getPackageName()));
						pcard2.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[0][1], "drawable", getApplicationContext().getPackageName()));
						pcard3.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[0][2], "drawable", getApplicationContext().getPackageName()));
						pcardhp.setText(Integer.toString(p[0][3]));
						pcardap.setText(Integer.toString(p[0][6]));
						phase = 9;
					}
					else
						if(plose == 1){
							pcard1.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[0][1], "drawable", getApplicationContext().getPackageName()));
							pcard2.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[0][2], "drawable", getApplicationContext().getPackageName()));
							pcard3.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[0][0], "drawable", getApplicationContext().getPackageName()));
							pcard3.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
							pcardhp.setText(Integer.toString(p[0][4]));
							pcardap.setText(Integer.toString(p[0][7]));
							phase = 9;
						}
						else
							if(plose == 2){
								pcard1.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[0][2], "drawable", getApplicationContext().getPackageName()));
								pcard2.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[0][0], "drawable", getApplicationContext().getPackageName()));
								pcard3.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[0][1], "drawable", getApplicationContext().getPackageName()));
								pcard2.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
								pcard3.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
								pcardhp.setText(Integer.toString(p[0][5]));
								pcardap.setText(Integer.toString(p[0][8]));
								phase = 9;
							}
							else{
								pcard1.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
								pcard2.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
								pcard3.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
								if(p[0][5] < 0)
									pcardhp.setText("0");
								else
									pcardhp.setText(Integer.toString(p[0][5]));
								if(p[0][8] <= 0)
									pcardap.setText("1");
								else
									pcardap.setText(Integer.toString(p[0][8]));
								phase = 10;
								break;
							}
								
					if(elose == 0){
						ecard1.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[1][0], "drawable", getApplicationContext().getPackageName()));
						ecard2.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[1][1], "drawable", getApplicationContext().getPackageName()));
						ecard3.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[1][2], "drawable", getApplicationContext().getPackageName()));
						ecardhp.setText(Integer.toString(p[1][3]));
						ecardap.setText(Integer.toString(p[1][6]));
						phase = 9;
					}
					else
						if(elose == 1){
							ecard1.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[1][1], "drawable", getApplicationContext().getPackageName()));
							ecard2.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[1][2], "drawable", getApplicationContext().getPackageName()));
							ecard3.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[1][0], "drawable", getApplicationContext().getPackageName()));
							ecard3.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
							ecardhp.setText(Integer.toString(p[1][4]));
							ecardap.setText(Integer.toString(p[1][7]));
							phase = 9;
						}
						else
							if(elose == 2){
								ecard1.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[1][2], "drawable", getApplicationContext().getPackageName()));
								ecard2.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[1][0], "drawable", getApplicationContext().getPackageName()));
								ecard3.setImageResource(getApplicationContext().getResources().getIdentifier(pcard[1][1], "drawable", getApplicationContext().getPackageName()));
								ecard2.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
								ecard3.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
								ecardhp.setText(Integer.toString(p[1][5]));
								ecardap.setText(Integer.toString(p[1][8]));
								phase = 9;
							}
							else{
								ecard1.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
								ecard2.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
								ecard3.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
								if(p[1][5] < 0)
									ecardhp.setText("0");
								else
									ecardhp.setText(Integer.toString(p[1][5]));
								if(p[1][8] <= 1)
									ecardap.setText("1");
								else
									ecardap.setText(Integer.toString(p[1][8]));
								phase = 10;
								break;
							}
				}
				case 9:{
					if(pturnnum > 8 && eturnnum > 8){
						message.setText(" Both cards are defeated!");
						pturnnum = 1;
						eturnnum = 1;
						if(pturncheck == true && eturncheck == false){
							pturncheck = false;
							eturncheck = true;
						}
						else
							if(pturncheck == false && eturncheck == true){
								pturncheck = true;
								eturncheck = false;
							}
						puse = 0;
						euse = 0;
						phase = 1;
					}
					else
						if(pturncheck == true && eturncheck == false){
							pturncheck = false;
							eturncheck = true;
							pturnnum++;
							phase = 1;	
						}
						else
							if(pturncheck == false && eturncheck == true){
								pturncheck = true;
								eturncheck = false;
								eturnnum++;
								phase = 1;
							}
					break;
				}
				case 10:{
					if(pturnnum > 8 && eturnnum > 8){
						pcard1.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
						pcard2.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
						pcard3.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
						ecard1.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
						ecard2.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
						ecard3.setColorFilter(Color.rgb(123, 123, 123), android.graphics.PorterDuff.Mode.MULTIPLY);
						message.setText(" It's a draw!");
						ContentValues updatevalues = new ContentValues();
						updatevalues.put("jlh_seri", seri1 + 1);
					    long n = DatabaseHelper.updatetable("PEMAIN", updatevalues, "_id="+ID);
				        if(n!=-1){
				        	System.out.println("UPDATE SUCCESS");
				        }	
				        else
				        	System.out.println("UPDATE FAILED");
				        draw = true;
				        phase = 13;
					}
					else
					if(plose == 3){
						message.setText(" " + pnm[0] + " has been defeated!");
						ContentValues updatevalues = new ContentValues();
					    updatevalues.put("jlh_kalah", kalah1 + 1);
					    long n = DatabaseHelper.updatetable("PEMAIN", updatevalues, "_id="+ID);
				        if(n!=-1){
				        	System.out.println("UPDATE SUCCESS");
				        }	
				        else
				        	System.out.println("UPDATE FAILED");
				        lose = true;
				        phase = 13;
					}
					else
						if(elose == 3){
							message.setText(" " + pnm[1] + " has been defeated!");
							ContentValues updatevalues = new ContentValues();
							updatevalues.put("jlh_menang", menang1 + 1);
						    long n = DatabaseHelper.updatetable("PEMAIN", updatevalues, "_id="+ID);
					        if(n!=-1){
					        	System.out.println("UPDATE SUCCESS");
					        }	
					        else
					        	System.out.println("UPDATE FAILED");
					        win = true;
					        
					        if(fromarena == true){
					        	int newcardid = getnewcard();
						        String[] pilihtabel = {"nama"};
								String[] where = {""};
								where[0] = Integer.toString(newcardid);
								Cursor c = myDbHelper.selectrecord("KARTU", pilihtabel, "_id" + "=?", where, null, null, null);
								if(c!=null){
									if(c.moveToFirst()){
							    		do{
							    			newcardname = c.getString(c.getColumnIndex("nama"));
							    		}
							    		while(c.moveToNext());
									}
								}
								c.close();
								phase = 11;
					        }
							else{
								phase = 13;
							}
						}
					break;
				}
				case 11:{
					message.setText(" You obtained a new card!");
					phase = 12;
					break;
				}
				case 12:{
					message.setText(" You got " + newcardname + "!");
					phase = 13;
					break;
				}
				case 13:{
					//enable the back button.
					backdisable = false;
					if(fromarena == true){
						story++;
						arena();						
					}
					else{
						tournamentpage++;
						tournament();
					}
				}
				}
			};
		});	
	}
	
	public int getnewcard(){
		String[] pilihtabel = {"jumlah"};
		String[] where = {""};
		Random rnd = new Random();
		int rarity, tmpquantity = 0, cardid = 0;
		
		if(fromarena == true)
			if(story >= 83)
				rarity = rnd.nextInt(3);
			else
				if(story >= 210)
					rarity = rnd.nextInt(4);
			else
				rarity = rnd.nextInt(2);
		else
			rarity = rnd.nextInt(3) + 1;
		
		switch (rarity){
		case 0:{
			cardid = rnd.nextInt(12) + 19;	//common
			break;
		}
		case 1:{
			cardid = rnd.nextInt(8) + 11;	//uncommon
			break;
		}
		case 2:{
			cardid = rnd.nextInt(6) + 5;	//rare
			break;
		}
		case 3:{
			cardid = rnd.nextInt(4) + 1;	//legendary
			break;
		}
		}
		where[0] = Integer.toString(cardid);
		Cursor c = myDbHelper.selectrecord("KARTU", pilihtabel, "_id" + "=?", where, null, null, null);
		if(c!=null){
			if(c.moveToFirst()){
	    		do{
	    			tmpquantity = c.getInt(c.getColumnIndex("jumlah"));
	    		}
	    		while(c.moveToNext());
			}
		}
		c.close();
		ContentValues updatevalues = new ContentValues();
		updatevalues.put("jumlah", tmpquantity + 1);
	    long n = DatabaseHelper.updatetable("KARTU", updatevalues, "_id="+cardid);
        if(n!=-1){
        	System.out.println("UPDATE SUCCESS");
        }	
        else
        	System.out.println("UPDATE FAILED");
        return cardid;
	}
	
	//method to resolve the ability used.
	public String resolveability(int abi){
		String message = "";
		switch (abi){
		case 1:{
			if(pturncheck == true && puse < 3){
				puse++;
				p[1][elose + 3] -= 3;
				p[1][elose + 6] -= 1;
				message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " got HP - 3 and AP - 1!";
			}
			else 
				if(eturncheck == true && euse < 3){
					euse++;
					p[0][plose + 3] -= 3;
					p[0][plose + 6] -= 1;
					message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " got HP - 3 and AP - 1!";
				}
				else
					if(puse >= 3)
						message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " can not use that ability anymore!";
					else
						if(euse >= 3)
							message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " can not use that ability anymore!";
			break;
		}
		case 2:{
			if(pturncheck == true && puse < 2){
				puse++;
				doubledmg = true;
				message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " gains double damage this turn!";
			}
			else 
				if(eturncheck == true && euse < 2){
					euse++;
					doubledmg = true;
					message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " gains double damage this turn!";
				}
				else
					if(puse >= 2)
						message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " can not use that ability anymore!";
					else
						if(euse >= 2)
							message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " can not use that ability anymore!";
			break;
		}
		case 3:{
			if(pturncheck == true && p[0][plose + 3] <= p[1][elose + 3]){
				p[0][plose + 6] += p[1][elose + 6];
				if(p[0][plose + 6] > 8){
					p[0][plose + 6] = 8;
					message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " has reached maximum AP!";
				}
				else
					message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " got AP + " + p[1][elose + 6] + "!";
			}
			else 
				if(eturncheck == true && p[1][elose + 3] <= p[0][plose + 3]){
					p[1][elose + 6] += p[0][plose + 6];
					if(p[1][elose + 6] > 8){
						p[1][elose + 6] = 8;
						message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " has reached maximum AP!";
					}
					else
						message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " got AP + " + p[0][plose + 6] + "!";
				}
				else
					message = " It doesn't seem to be working!";
			break;
		}
		case 4:{
			if(pturnnum > 2 && eturnnum > 2){
				pturnnum += 4;
				eturnnum += 4;
				message = " Both card's turns have been advanced by 4!";
			}
			else
				message = " It doesn't seem to be working!";
			break;
		}
		case 5:{
			if(pturncheck == true && puse < 2){
				puse++;
				int tmp = p[0][plose + 3];
				p[0][plose + 3] = p[1][elose + 3];
				p[1][elose + 3] = tmp;
				message = " Both card's HP were swapped!";
			}
			else 
				if(eturncheck == true && euse < 2){
					euse++;
					int tmp = p[0][plose + 3];
					p[0][plose + 3] = p[1][elose + 3];
					p[1][elose + 3] = tmp;
					message = " Both card's HP were swapped!";
				}
				else
					if(puse >= 2)
						message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " can not use that ability anymore!";
					else
						if(euse >= 2)
							message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " can not use that ability anymore!";
			break;
		}
		case 6:{
			if(pturncheck == true && puse < 2){
				puse++;
				int tmp = p[0][plose + 6];
				p[0][plose + 6] = p[1][elose + 6];
				p[1][elose + 6] = tmp;
				message = " Both card's AP were swapped!";
			}
			else 
				if(eturncheck == true && euse < 2){
					euse++;
					int tmp = p[0][plose + 6];
					p[0][plose + 6] = p[1][elose + 6];
					p[1][elose + 6] = tmp;
					message = " Both card's AP were swapped!";
				}
				else
					if(puse >= 2)
						message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " can not use that ability anymore!";
					else
						if(euse >= 2)
							message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " can not use that ability anymore!";
			break;
		}
		case 7:{
			if(pturncheck == true && puse < 3){
				puse++;
				pturnnum += 2;
				eturnnum += 2;
				message = " Both card's turns have been advanced by 2!";
			}
			else 
				if(eturncheck == true && euse < 3){
					euse++;
					pturnnum += 2;
					eturnnum += 2;
					message = " Both card's turns have been advanced by 2!";
				}
				else
					if(puse >= 3)
						message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " can not use that ability anymore!";
					else
						if(euse >= 3)
							message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " can not use that ability anymore!";
			break;
		}
		case 8:{
			if(pturncheck == true && p[0][plose + 3] >= 13 && p[1][elose + 3] >= 13){
				p[0][plose + 3] = 1;
				p[1][elose + 3] = 1;
				message = " Both card's HP becomes 1!";
			}
			else 
				if(eturncheck == true && p[0][plose + 3] >= 13 && p[1][elose + 3] >= 13){
					p[0][plose + 3] = 1;
					p[1][elose + 3] = 1;
					message = " Both card's HP becomes 1!";
				}
				else
					message = " It doesn't seem to be working!";
			break;
		}
		case 9:{
			if(pturncheck == true && p[0][plose + 6] >= 1 && p[1][elose + 6] >= 1){
				p[0][plose + 6] = 1;
				p[1][elose + 6] = 1;
				message = " Both card's AP becomes 1!";
			}
			else 
				if(eturncheck == true && p[0][plose + 6] >= 1 && p[1][elose + 6] >= 1){
					p[0][plose + 6] = 1;
					p[1][elose + 6] = 1;
					message = " Both card's AP becomes 1!";
				}
				else
					message = " It doesn't seem to be working!";
			break;
		}
		case 10:{
			if(pturncheck == true && pturnnum >= 4){
				p[0][plose + 3] += 2;
				p[0][plose + 6] += 1;
				if(p[0][plose + 6] > 8){
					p[0][plose + 6] = 8;
					message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " got HP + 2 and AP + 1, but it has reached max AP!";
				}
				else
					message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " got HP + 2 and AP + 1!";
			}
			else 
				if(eturncheck == true && eturnnum >= 4){
					p[1][elose + 3] += 2;
					p[1][elose + 6] += 1;
					if(p[1][elose + 6] > 8){
						p[1][elose + 6] = 8;
						message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " got HP + 2 and AP + 1, but it has reached max AP!";
					}
					else
						message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " got HP + 2 and AP + 1!";
				}
				else
					message = " It doesn't seem to be working!";
			break;
		}
		case 11:{
			if(pturncheck == true && puse < 2){
				puse++;
				p[0][plose + 3] += p[1][elose + 6];
				message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " got HP + " + p[1][elose + 6] + "!";
			}
			else 
				if(eturncheck == true && euse < 2){
					euse++;
					p[1][elose + 3] += p[0][plose + 6];
					message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " got HP + " + p[0][plose + 6] + "!";
				}
				else
					if(puse >= 2)
						message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " can not use that ability anymore!";
					else
						if(euse >= 2)
							message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " can not use that ability anymore!";
			break;
		}
		case 12:{
			if(pturncheck == true && puse < 3 && p[0][plose + 3] > 1 && p[1][elose + 3] > 1){
				puse++;
				p[0][plose + 3] /= 2;
				p[1][elose + 3] /= 2;
				message = " Both cards' HP were halved!";
			}
			else 
				if(eturncheck == true && euse < 3 && p[0][plose + 3] > 1 && p[1][elose + 3] > 1){
					euse++;
					p[0][plose + 3] /= 2;
					p[1][elose + 3] /= 2;
					message = " Both cards' HP were halved!";
				}
				else
					if(puse >= 3)
						message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " can not use that ability anymore!";
					else
						if(euse >= 3)
							message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " can not use that ability anymore!";
			break;
		}
		case 13:{
			if(pturncheck == true && puse < 1){
				puse++;
				p[1][elose + 3] -= 8;
				message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " got HP - 8!";
			}
			else 
				if(eturncheck == true && euse < 1){
					euse++;
					p[0][plose + 3] -= 8;
					message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " got HP - 8!";
				}
				else
					if(puse >= 1)
						message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " can not use that ability anymore!";
					else
						if(euse >= 1)
							message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " can not use that ability anymore!";
			break;
		}
		case 14:{
			if(pturncheck == true && puse < 1){
				puse++;
				p[1][elose + 6] = 1;
				message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " 's AP becomes 1!";
			}
			else 
				if(eturncheck == true && euse < 1){
					euse++;
					p[0][plose + 6] = 1;
					message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " 's AP becomes 1!";
				}
				else
					if(puse >= 1)
						message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " can not use that ability anymore!";
					else
						if(euse >= 1)
							message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " can not use that ability anymore!";
			break;
		}
		case 15:{
			if(pturncheck == true && p[0][plose + 3] <= 4){
				p[1][elose + 3] = p[0][plose + 3];
				message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " 's HP becomes " + p[0][plose + 3] + "!";
			}
			else 
				if(eturncheck == true && p[1][elose + 3] <= 4){
					p[0][plose + 3] = p[1][elose + 3];
					message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " 's HP becomes " + p[1][elose + 3] + "!";
				}
				else
					message = " It doesn't seem to be working!";
			break;
		}
		case 16:{
			if(pturncheck == true && p[0][plose + 3] >= 14){
				p[0][plose + 6]++;
				message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " got AP + 1!";
			}
			else 
				if(eturncheck == true && p[1][elose + 3] >= 14){
					p[1][elose + 6]++;
					message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " got AP + 1!";
				}
				else
					message = " It doesn't seem to be working!";
			break;
		}
		case 17:{
			if(pturncheck == true && p[0][plose + 3] <= 3){
				p[0][plose + 6] += 4;
				if(p[0][plose + 6] > 8){
					p[0][plose + 6] = 8;
					message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " got AP + 4, but it has reached max AP!";
				}
				else
					message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " got AP + 4!";
			}
			else 
				if(eturncheck == true && p[1][elose + 3] <= 3){
					p[1][elose + 6] += 4;
					if(p[1][elose + 6] > 8){
						p[1][elose + 6] = 8;
						message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " got AP + 4, but it has reached max AP!";
					}
					else
						message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " got AP + 4!";
				}
				else
					message = " It doesn't seem to be working!";
			break;
		}
		case 18:{
			if(pturncheck == true && p[1][elose + 6] > p[0][plose + 6]){
				p[1][elose + 3] -= 4;
				message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " got HP - 4!";
			}
			else 
				if(eturncheck == true && p[0][plose + 6] > p[1][elose + 6]){
					p[0][plose + 3] -= 4;
					message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " got HP - 4!";
				}
				else
					message = " It doesn't seem to be working!";
			break;
		}
		case 19:{
			if(pturncheck == true && puse < 2){
				puse++;
				p[0][plose + 6] += 2;
				message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " got AP + 2!";
			}
			else 
				if(eturncheck == true && euse < 2){
					euse++;
					p[1][elose + 6] += 2;
					message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " got AP + 2!";
				}
				else
					if(puse >= 2)
						message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " can not use that ability anymore!";
					else
						if(euse >= 2)
							message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " can not use that ability anymore!";
			break;
		}
		case 20:{
			if(pturncheck == true && puse < 4){
				puse++;
				p[0][plose + 3] += 2;
				message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " got HP + 2!";
			}
			else 
				if(eturncheck == true && euse < 4){
					euse++;
					p[1][elose + 3] += 2;
					message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " got HP + 2!";
				}
				else
					if(puse >= 4)
						message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " can not use that ability anymore!";
					else
						if(euse >= 4)
							message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " can not use that ability anymore!";
			break;
		}
		case 21:{
			if(pturncheck == true && puse < 2 && p[0][plose + 3] > 4){
				puse++;
				p[0][plose + 3] -= 2;
				doubledmg = true;
				message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " loses 2 HP to gain double damage this turn!";
			}
			else 
				if(eturncheck == true && euse < 2 && p[1][elose + 3] > 4){
					euse++;
					p[1][elose + 3] -= 2;
					doubledmg = true;
					message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " loses 2 HP to gain double damage this turn!";
				}
				else
					if(puse >= 2)
						message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " can not use that ability anymore!";
					else
						if(euse >= 2)
							message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " can not use that ability anymore!";
						else
							message = " It doesn't seem to be working!";
			break;
		}
		case 22:{
			if(pturncheck == true && puse < 2 && p[0][plose + 3] > 4 && p[1][elose + 3] > 4){
				puse++;
				p[0][plose + 3] -= 4;
				p[1][elose + 3] -= 4;
				message = " Both cards got HP - 4!";
			}
			else 
				if(eturncheck == true && euse < 2 && p[0][plose + 3] > 4 && p[1][elose + 3] > 4){
					euse++;
					p[0][plose + 3] -= 4;
					p[1][elose + 3] -= 4;
					message = " Both cards got HP - 4!";
				}
				else
					if(puse >= 2)
						message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " can not use that ability anymore!";
					else
						if(euse >= 2)
							message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " can not use that ability anymore!";
						else
							message = " It doesn't seem to be working!";
			break;
		}
		case 23:{
			if(pturncheck == true && puse < 3){
				puse++;
				p[0][plose + 3]++;
				p[1][elose + 3]--;
				message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " got HP + 1, " + pnm[1] + "'s " + pcard[1][elose + 3] + " got HP - 1!";
			}
			else 
				if(eturncheck == true && euse < 3){
					euse++;
					p[1][elose + 3]++;
					p[0][plose + 3]--;
					message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " got HP + 1, " + pnm[0] + "'s " + pcard[0][plose + 3] + " got HP - 1!";
				}
				else
					if(puse >= 3)
						message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " can not use that ability anymore!";
					else
						if(euse >= 3)
							message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " can not use that ability anymore!";
			break;
		}
		case 24:{
			if(pturncheck == true && puse < 2){
				puse++;
				p[0][plose + 6]++;
				p[1][elose + 6]--;
				message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " got AP + 1, " + pnm[1] + "'s " + pcard[1][elose + 3] + " got AP - 1!";
			}
			else 
				if(eturncheck == true && euse < 2){
					euse++;
					p[1][elose + 6]++;
					p[0][plose + 6]--;
					message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " got AP + 1, " + pnm[0] + "'s " + pcard[0][plose + 3] + " got AP - 1!";
				}
				else
					if(puse >= 2)
						message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " can not use that ability anymore!";
					else
						if(euse >= 2)
							message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " can not use that ability anymore!";
			break;
		}
		case 25:{
			if(pturncheck == true && p[0][plose + 3] <= 3){
				p[0][plose + 6]++;
				message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " got AP + 1!";
			}
			else 
				if(eturncheck == true && p[1][elose + 3] <= 3){
					p[1][elose + 6]++;
					message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " got AP + 1!";
				}
				else
					message = " It doesn't seem to be working!";
			break;
		}
		case 26:{
			if(pturncheck == true && p[0][plose + 3] <= 4){
				p[0][plose + 3]++;
				message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " got HP + 1!";
			}
			else 
				if(eturncheck == true && p[1][elose + 3] <= 4){
					p[1][elose + 3]++;
					message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " got HP + 1!";
				}
				else
					message = " It doesn't seem to be working!";
			break;
		}
		case 27:{
			if(pturncheck == true && p[0][plose + 3] > 12){
				p[0][plose + 6]++;
				message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " got AP + 1!";
			}
			else 
				if(eturncheck == true && p[1][elose + 3] > 12){
					p[1][elose + 6]++;
					message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " got AP + 1!";
				}
				else
					message = " It doesn't seem to be working!";
			break;
		}
		case 28:{
			if(pturncheck == true && p[0][plose + 6] < 5){
				p[0][plose + 6]++;
				message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " got AP + 1!";
			}
			else 
				if(eturncheck == true && p[1][elose + 6] < 5){
					p[1][elose + 6]++;
					message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " got AP + 1!";
				}
				else
					message = " It doesn't seem to be working!";
			break;
		}
		case 29:{
			if(pturncheck == true && p[0][plose + 3] > p[1][elose + 3]){
				p[0][plose + 6]++;
				p[1][elose + 3] += 2;
				message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " got AP + 1, " + pnm[1] + "'s " + pcard[1][elose + 3] + " got HP + 2!";
			}
			else 
				if(eturncheck == true && p[1][elose + 3] > p[0][plose + 3]){
					p[1][elose + 6]++;
					p[0][plose + 3] += 2;
					message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " got AP + 1, " + pnm[0] + "'s " + pcard[0][plose + 3] + " got HP + 2!";
				}
				else
					message = " It doesn't seem to be working!";
			break;
		}
		case 30:{
			if(pturncheck == true && p[0][plose + 6] > p[1][elose + 6]){
				p[1][elose + 6]++;
				p[0][plose + 3] += 2;
				message = " " + pnm[0] + "'s " + pcard[0][plose + 3] + " got HP + 2, " + pnm[1] + "'s " + pcard[1][elose + 3] + " got AP + 1!";
			}
			else 
				if(eturncheck == true && p[1][elose + 6] > p[0][plose + 6]){
					p[0][plose + 6]++;
					p[1][elose + 3] += 2;
					message = " " + pnm[1] + "'s " + pcard[1][elose + 3] + " got HP + 2,  " + pnm[0] + "'s " + pcard[0][plose + 3] + " got AP + 1!";
				}
				else
					message = " It doesn't seem to be working!";
			break;
		}
		}
		return message;
	}
	
	public void cardgameresult(){
		result = getSharedPreferences(prefName, MODE_PRIVATE);
		SharedPreferences.Editor editor = result.edit();
		if(mngpersen > 70.0f){
			editor.putInt("cg_ipa1", 50);
			editor.putInt("cg_ipa2", 30);
			editor.putInt("cg_ips1", 5);
			editor.putInt("cg_ips2", 15);
		}
		else
			if(mngpersen > 50.0f){
				editor.putInt("cg_ipa1", 30);
				editor.putInt("cg_ipa2", 50);
				editor.putInt("cg_ips1", 5);
				editor.putInt("cg_ips2", 15);
			}
			else{
				editor.putInt("cg_ipa1", 15);
				editor.putInt("cg_ipa2", 5);
				editor.putInt("cg_ips1", 30);
				editor.putInt("cg_ips2", 50);
			}
		editor.commit();
	}
		
	//method to disable or enable back button.
	@Override
	public void onBackPressed() {
	    if(backdisable == false){
	        super.onBackPressed();
	    }
	}
	
	//method invoked when back to main menu button is pressed.
	public void back_to_main_menu(View v){
		myDbHelper.close();
		finish();
		System.exit(0);
	}
	
	//method invoked when the activity is destroyed.
	public void onDestroy(){
		super.onDestroy();
		myDbHelper.close();
	}
}