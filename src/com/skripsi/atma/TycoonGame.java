package com.skripsi.atma;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class TycoonGame extends Activity {
	
	public SharedPreferences tg_data;
	public String tg_pref = "tg_data";
	public boolean backdisable = false;
	public int tempmoney;
	public int tempstock;
	public int temp;
	
	//player data
	public String char_name;
	public String boutique_name;
	public int location;
	public int reputation;
	public int money;
	
	//items' data
	public int[] i_stock;
	public String stock;
	public int[] i_sellp;
	public String sellp;
	
	//day's history
	public String[] day = new String[50];
	public int tempday;
	public String[] mpd = new String [50];
	public String mopd;
	public String[] rpd = new String[50];
	public String repd;
	
	//algorithm
	public int totalVisitor;
	public int totalSold;
	public int adv;
	
	//result
	public SharedPreferences tg_result;
	public String tg_pref_result = "tg_result";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		
		if (i.getData() != null) {
			if(i.getDataString().compareTo("Start Game") == 0){
				//check for saved game
				File f = new File(getApplicationContext().getFilesDir().getParentFile().getPath() + "/shared_prefs/tg_data.xml");
				
				if (f.exists()){
					Toast.makeText(TycoonGame.this, "Back to your last state...", Toast.LENGTH_SHORT).show();
					tg_play();
				}
				else {
					Toast.makeText(TycoonGame.this, "Welcome to your new boutique. Enjoy!", Toast.LENGTH_SHORT).show();
					tg_name_input();
				}
			}
			else if(i.getDataString().compareTo("Help") == 0) {
				tg_help();
			}
		}
	}

	public void tg_name_input(){
		setContentView(R.layout.activity_tg_name_input);
		
        final EditText inputname = (EditText) findViewById(R.id.name_input);
		TextView done = (TextView) findViewById(R.id.done);
		
		done.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(inputname.getText().toString().trim().length() == 0){
					Toast.makeText(TycoonGame.this, "Please give her a nice name...", Toast.LENGTH_SHORT).show();
				}
				else{
					char_name = inputname.getText().toString();
					money = 1000;
					reputation = 5;
					adv = 0;
					stock = "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0"; 
					sellp = "38,47,53,69,74,85,91,106,118,127,139,147,150,168,175";
					tempday = -1;
					mopd = "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
					repd = "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
					tg_data = getSharedPreferences(tg_pref, MODE_PRIVATE);
					SharedPreferences.Editor editor = tg_data.edit();
					editor.putString("Player", char_name);
					editor.putInt("Money", money);
					editor.putInt("Reputation", reputation);
					editor.putInt("Advertise", adv);
					editor.putString("Stock", stock);
					editor.putString("Sell Price", sellp);
					editor.putInt("Day", tempday);
					editor.putString("Money Per Day", mopd);
					editor.putString("Reputation Per Day", repd);
					editor.commit();
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(inputname.getWindowToken(), 0);
					tg_boutique_input();
				}
			}
		});
	}

	public void tg_boutique_input(){
		setContentView(R.layout.activity_tg_boutique_input);
        
        final EditText inputname = (EditText) findViewById(R.id.boutique_input);
		TextView done = (TextView) findViewById(R.id.done);
		
		done.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(inputname.getText().toString().trim().length() == 0){
					Toast.makeText(TycoonGame.this, "Please give an awesome name for your boutique...", Toast.LENGTH_SHORT).show();
				}
				else{
					boutique_name = inputname.getText().toString();
					tg_data = getSharedPreferences(tg_pref, MODE_PRIVATE);
					SharedPreferences.Editor editor = tg_data.edit();
					editor.putString("Boutique Name", boutique_name);
					editor.commit();
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(inputname.getWindowToken(), 0);
					tg_select_location();
				}	
			}		
		});
	}	
	
	public void tg_select_location(){
		setContentView(R.layout.activity_tg_select_location);

		GridView gridview = (GridView) findViewById(R.id.tg_location);	    
	    gridview.setAdapter(new ImageAdapter(this));

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	location = position;
	        	tg_data = getSharedPreferences(tg_pref, MODE_PRIVATE);
				SharedPreferences.Editor editor = tg_data.edit();
				editor.putInt("Location", location);
				editor.commit();
				tg_play();
	        }
	    });
	}
	
	    public class ImageAdapter extends BaseAdapter {
	    	private Context mContext;

	    	public ImageAdapter(Context c) {
	    		mContext = c;
	    	}

		    public int getCount() {
		        return mThumbIds.length;
		    }
	
		    public Object getItem(int position) {
		        return null;
		    }

		    public long getItemId(int position) {
		        return 0;
		    }
		    
		    // create a new ImageView for each item referenced by the Adapter
		    public View getView(int position, View convertView, ViewGroup parent) {
		        ImageView imageView;
		        if (convertView == null) {  // if it's not recycled, initialize some attributes
		            imageView = new ImageView(mContext);	
		            
		            DisplayMetrics metrics = new DisplayMetrics();
		            getWindowManager().getDefaultDisplay().getMetrics(metrics);
		            switch(metrics.densityDpi){
		                 case DisplayMetrics.DENSITY_LOW:
		                            imageView.setLayoutParams(new GridView.LayoutParams(93, 125));
		                            break;
		                 case DisplayMetrics.DENSITY_MEDIUM:
		                            imageView.setLayoutParams(new GridView.LayoutParams(150, 200));
		                            break;
		                 case DisplayMetrics.DENSITY_HIGH:
		                             imageView.setLayoutParams(new GridView.LayoutParams(206, 275));
		                             break;
		            }
		            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		            imageView.setPadding(3, 3, 3, 3);
		        } 
		        else {
		            imageView = (ImageView) convertView;
		        }
	
		        imageView.setImageResource(mThumbIds[position]);
		        return imageView;
		    }
	
		    	// references to images
		    	private Integer[] mThumbIds = {
		    		R.drawable.tg_amsterdam, R.drawable.tg_berlin,
		    		R.drawable.tg_budapest, R.drawable.tg_tokyo,
		            R.drawable.tg_london, R.drawable.tg_paris,
		            R.drawable.tg_sydney, R.drawable.tg_newyork
		    };
		}
	
	public void tg_play(){
		setContentView(R.layout.activity_tg_play_menu);
		backdisable = false;
		
		//retrieve player data
		tg_data = getSharedPreferences(tg_pref, MODE_PRIVATE);	
		char_name = tg_data.getString("Player", "Player");
		boutique_name = tg_data.getString("Boutique Name", "Boutique"); 
		location = tg_data.getInt("Location", 0);
		reputation = tg_data.getInt("Reputation", 0);
		money = tg_data.getInt("Money", 1000);
		adv = tg_data.getInt("Advertise", 0);
		stock = tg_data.getString("Stock", "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
		sellp = tg_data.getString("Sell Price", "38,47,53,69,74,85,91,106,118,127,139,147,150,168,175");
		tempday = tg_data.getInt("Day", -1);
		mopd = tg_data.getString("Money Per Day", "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
		repd = tg_data.getString("Reputation Per Day", "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0");
		
		String[] string = stock.split(",");
		i_stock = new int[string.length];
	    for (int i = 0; i < string.length; i++) {
	        i_stock[i] = Integer.parseInt(string[i]);
	    }
	    
	    String[] string1 = sellp.split(",");
	    i_sellp = new int[string1.length];
	    for (int i = 0; i < string1.length; i++) {
	    	i_sellp[i] = Integer.parseInt(string1[i]);
	    }
	    
	    mpd = mopd.split(",");
	    rpd = repd.split(",");
	    
		tg_background();
		Resources res = getResources();
		final int[] i_buyp = res.getIntArray(R.array.i_buyp);

		//print the status
		TextView name = (TextView)findViewById(R.id.tg_player);
		name.setText("Hi, " + char_name + " owner of " + boutique_name);

		TextView status = (TextView)findViewById(R.id.tg_status);
		status.setText("Money: $" + money + " Rep: " + reputation);
		
		//set the button
		Button BuyClothes = (Button) findViewById(R.id.tg_buyc);
	    Button SetPrice = (Button) findViewById(R.id.tg_setp);
	    Button Advertise = (Button) findViewById(R.id.tg_adv);
	    Button Journal = (Button) findViewById(R.id.tg_journal);
	    ImageView Start = (ImageView) findViewById(R.id.tg_start);
	    
	    BuyClothes.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				setContentView(R.layout.activity_tg_buy_clothes);
				backdisable = true;
				
				tg_background_scroll();
				tg_status_and_back();
				tg_stock();
				
				Button Button1 = (Button) findViewById(R.id.button1);
				Button Button2 = (Button) findViewById(R.id.button2);
				Button Button3 = (Button) findViewById(R.id.button3);
				Button Button4 = (Button) findViewById(R.id.button4);
				Button Button5 = (Button) findViewById(R.id.button5);
				Button Button6 = (Button) findViewById(R.id.button6);
				Button Button7 = (Button) findViewById(R.id.button7);
				Button Button8 = (Button) findViewById(R.id.button8);
				Button Button9 = (Button) findViewById(R.id.button9);
				Button Button10 = (Button) findViewById(R.id.button10);
				Button Button11 = (Button) findViewById(R.id.button11);
				Button Button12 = (Button) findViewById(R.id.button12);
				Button Button13 = (Button) findViewById(R.id.button13);
				Button Button14 = (Button) findViewById(R.id.button14);
				Button Button15 = (Button) findViewById(R.id.button15);
				
				final EditText Item1 = (EditText) findViewById(R.id.tg_edit1);
				final EditText Item2 = (EditText) findViewById(R.id.tg_edit2);
				final EditText Item3 = (EditText) findViewById(R.id.tg_edit3);
				final EditText Item4 = (EditText) findViewById(R.id.tg_edit4);
				final EditText Item5 = (EditText) findViewById(R.id.tg_edit5);
				final EditText Item6 = (EditText) findViewById(R.id.tg_edit6);
				final EditText Item7 = (EditText) findViewById(R.id.tg_edit7);
				final EditText Item8 = (EditText) findViewById(R.id.tg_edit8);
				final EditText Item9 = (EditText) findViewById(R.id.tg_edit9);
				final EditText Item10 = (EditText) findViewById(R.id.tg_edit10);
				final EditText Item11 = (EditText) findViewById(R.id.tg_edit11);
				final EditText Item12 = (EditText) findViewById(R.id.tg_edit12);
				final EditText Item13 = (EditText) findViewById(R.id.tg_edit13);
				final EditText Item14 = (EditText) findViewById(R.id.tg_edit14);
				final EditText Item15 = (EditText) findViewById(R.id.tg_edit15);
				
				Button1.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item1.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							tempstock = Integer.parseInt(Item1.getText().toString());
							tempmoney = i_buyp[0] * tempstock;
							tg_purchasing(0);
						}
						Item1.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item1.getWindowToken(), 0);
						tg_status_and_back();
						tg_stock();
					};
				});
				
				Button2.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item2.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							tempstock = Integer.parseInt(Item2.getText().toString());
							tempmoney = i_buyp[1] * tempstock;
							tg_purchasing(1);
						}
						Item2.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item2.getWindowToken(), 0);
						tg_status_and_back();
						tg_stock();
					};
				});
				
				Button3.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item3.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							tempstock = Integer.parseInt(Item3.getText().toString());
							tempmoney = i_buyp[2] * tempstock;
							tg_purchasing(2);
						}
						Item3.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item3.getWindowToken(), 0);
						tg_status_and_back();
						tg_stock();
					};
				});
				
				Button4.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item4.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							tempstock = Integer.parseInt(Item4.getText().toString());
							tempmoney = i_buyp[3] * tempstock;
							tg_purchasing(3);
						}
						Item4.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item4.getWindowToken(), 0);
						tg_status_and_back();
						tg_stock();
					};
				});
				
				Button5.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item5.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							tempstock = Integer.parseInt(Item5.getText().toString());
							tempmoney = i_buyp[4] * tempstock;
							tg_purchasing(4);
						}
						Item5.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item5.getWindowToken(), 0);
						tg_status_and_back();
						tg_stock();
					};
				});
				
				Button6.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item6.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							tempstock = Integer.parseInt(Item6.getText().toString());
							tempmoney = i_buyp[5] * tempstock;
							tg_purchasing(5);
						}
						Item6.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item6.getWindowToken(), 0);
						tg_status_and_back();
						tg_stock();
					};
				});
				
				Button7.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item7.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							tempstock = Integer.parseInt(Item7.getText().toString());
							tempmoney = i_buyp[6] * tempstock;
							tg_purchasing(6);
						}
						Item7.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item7.getWindowToken(), 0);
						tg_status_and_back();
						tg_stock();
					};
				});
				
				Button8.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item8.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							tempstock = Integer.parseInt(Item8.getText().toString());
							tempmoney = i_buyp[7] * tempstock;
							tg_purchasing(7);
						}
						Item8.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item8.getWindowToken(), 0);
						tg_status_and_back();
						tg_stock();
					};
				});
				
				Button9.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item9.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							tempstock = Integer.parseInt(Item9.getText().toString());
							tempmoney = i_buyp[8] * tempstock;
							tg_purchasing(8);
						}
						Item9.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item9.getWindowToken(), 0);
						tg_status_and_back();
						tg_stock();
					};
				});
				
				Button10.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item10.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							tempstock = Integer.parseInt(Item10.getText().toString());
							tempmoney = i_buyp[9] * tempstock;
							tg_purchasing(9);
						}
						Item10.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item10.getWindowToken(), 0);
						tg_status_and_back();
						tg_stock();
					};
				});
				
				Button11.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item11.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							tempstock = Integer.parseInt(Item11.getText().toString());
							tempmoney = i_buyp[10] * tempstock;
							tg_purchasing(10);
						}
						Item11.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item11.getWindowToken(), 0);
						tg_status_and_back();
						tg_stock();
					};
				});
				
				Button12.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item12.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							tempstock = Integer.parseInt(Item12.getText().toString());
							tempmoney = i_buyp[11] * tempstock;
							tg_purchasing(11);
						}
						Item12.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item12.getWindowToken(), 0);
						tg_status_and_back();
						tg_stock();
					};
				});
				
				Button13.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item13.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							tempstock = Integer.parseInt(Item13.getText().toString());
							tempmoney = i_buyp[12] * tempstock;
							tg_purchasing(12);
						}
						Item13.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item13.getWindowToken(), 0);
						tg_status_and_back();
						tg_stock();
					};
				});
				
				Button14.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item14.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							tempstock = Integer.parseInt(Item14.getText().toString());
							tempmoney = i_buyp[13] * tempstock;
							tg_purchasing(13);
						}
						Item14.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item14.getWindowToken(), 0);
						tg_status_and_back();
						tg_stock();
					};
				});
				
				Button15.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item15.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							tempstock = Integer.parseInt(Item15.getText().toString());
							tempmoney = i_buyp[14] * tempstock;
							tg_purchasing(14);
						}
						Item15.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item15.getWindowToken(), 0);
						tg_status_and_back();
						tg_stock();
					};
				});
			};
		});
	    
	    SetPrice.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				setContentView(R.layout.activity_tg_set_price);
				backdisable = true;
				
				tg_background_scroll();
				tg_status_and_back();
				tg_price();
				
				Button Button1 = (Button) findViewById(R.id.button1);
				Button Button2 = (Button) findViewById(R.id.button2);
				Button Button3 = (Button) findViewById(R.id.button3);
				Button Button4 = (Button) findViewById(R.id.button4);
				Button Button5 = (Button) findViewById(R.id.button5);
				Button Button6 = (Button) findViewById(R.id.button6);
				Button Button7 = (Button) findViewById(R.id.button7);
				Button Button8 = (Button) findViewById(R.id.button8);
				Button Button9 = (Button) findViewById(R.id.button9);
				Button Button10 = (Button) findViewById(R.id.button10);
				Button Button11 = (Button) findViewById(R.id.button11);
				Button Button12 = (Button) findViewById(R.id.button12);
				Button Button13 = (Button) findViewById(R.id.button13);
				Button Button14 = (Button) findViewById(R.id.button14);
				Button Button15 = (Button) findViewById(R.id.button15);
				
				final EditText Item1 = (EditText) findViewById(R.id.tg_edit1);
				final EditText Item2 = (EditText) findViewById(R.id.tg_edit2);
				final EditText Item3 = (EditText) findViewById(R.id.tg_edit3);
				final EditText Item4 = (EditText) findViewById(R.id.tg_edit4);
				final EditText Item5 = (EditText) findViewById(R.id.tg_edit5);
				final EditText Item6 = (EditText) findViewById(R.id.tg_edit6);
				final EditText Item7 = (EditText) findViewById(R.id.tg_edit7);
				final EditText Item8 = (EditText) findViewById(R.id.tg_edit8);
				final EditText Item9 = (EditText) findViewById(R.id.tg_edit9);
				final EditText Item10 = (EditText) findViewById(R.id.tg_edit10);
				final EditText Item11 = (EditText) findViewById(R.id.tg_edit11);
				final EditText Item12 = (EditText) findViewById(R.id.tg_edit12);
				final EditText Item13 = (EditText) findViewById(R.id.tg_edit13);
				final EditText Item14 = (EditText) findViewById(R.id.tg_edit14);
				final EditText Item15 = (EditText) findViewById(R.id.tg_edit15);
				
				Button1.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item1.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your custom price.", Toast.LENGTH_SHORT).show();
						else {
							i_sellp[0] = Integer.parseInt(Item1.getText().toString());
							tg_save_price();
						}
						Item1.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item1.getWindowToken(), 0);
						tg_price();
					};
				});
				
				Button2.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item2.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							i_sellp[1] = Integer.parseInt(Item2.getText().toString());
							tg_save_price();
						}
						Item2.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item2.getWindowToken(), 0);
						tg_price();
					};
				});
				
				Button3.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item3.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							i_sellp[2] = Integer.parseInt(Item3.getText().toString());
							tg_save_price();
						}
						Item3.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item3.getWindowToken(), 0);
						tg_price();
					};
				});
				
				Button4.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item4.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							i_sellp[3] = Integer.parseInt(Item4.getText().toString());
							tg_save_price();
						}
						Item4.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item4.getWindowToken(), 0);
						tg_price();
					};
				});
				
				Button5.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item5.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							i_sellp[4] = Integer.parseInt(Item5.getText().toString());
							tg_save_price();
						}
						Item5.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item5.getWindowToken(), 0);
						tg_price();
					};
				});
				
				Button6.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item6.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							i_sellp[5] = Integer.parseInt(Item6.getText().toString());
							tg_save_price();
						}
						Item6.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item6.getWindowToken(), 0);
						tg_price();
					};
				});
				
				Button7.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item7.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							i_sellp[6] = Integer.parseInt(Item7.getText().toString());
							tg_save_price();
						}
						Item7.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item7.getWindowToken(), 0);
						tg_price();
					};
				});
				
				Button8.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item8.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							i_sellp[7] = Integer.parseInt(Item8.getText().toString());
							tg_save_price();
						}
						Item8.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item8.getWindowToken(), 0);
						tg_price();
					};
				});
				
				Button9.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item9.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							i_sellp[8] = Integer.parseInt(Item9.getText().toString());
							tg_save_price();
						}
						Item9.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item9.getWindowToken(), 0);
						tg_price();
					};
				});
				
				Button10.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item10.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							i_sellp[9] = Integer.parseInt(Item10.getText().toString());
							tg_save_price();
						}
						Item10.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item10.getWindowToken(), 0);
						tg_price();
					};
				});
				
				Button11.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item11.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							i_sellp[10] = Integer.parseInt(Item11.getText().toString());
							tg_save_price();
						}
						Item11.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item11.getWindowToken(), 0);
						tg_price();
					};
				});
				
				Button12.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item12.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							i_sellp[11] = Integer.parseInt(Item12.getText().toString());
							tg_save_price();
						}
						Item12.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item12.getWindowToken(), 0);
						tg_price();
					};
				});
				
				Button13.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item13.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							i_sellp[12] = Integer.parseInt(Item13.getText().toString());
							tg_save_price();
						}
						Item13.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item13.getWindowToken(), 0);
						tg_price();
					};
				});
				
				Button14.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item14.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							i_sellp[13] = Integer.parseInt(Item14.getText().toString());
							tg_save_price();
						}
						Item14.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item14.getWindowToken(), 0);
						tg_price();
					};
				});
				
				Button15.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						if(Item15.getText().toString().trim().length() == 0)
							Toast.makeText(TycoonGame.this, "Please input your amount", Toast.LENGTH_SHORT).show();
						else {
							i_sellp[14] = Integer.parseInt(Item15.getText().toString());
							tg_save_price();
						}
						Item15.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(Item15.getWindowToken(), 0);
						tg_price();
					};
				});
			};
		});
	    
	    Advertise.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				setContentView(R.layout.activity_tg_advertise);
				backdisable = true;
				tempmoney = 0;
				
				tg_background();
				tg_status_and_back();
				
				final CheckBox flyer = (CheckBox) findViewById(R.id.tg_flyer);
				final CheckBox newspaper = (CheckBox) findViewById(R.id.tg_newspaper);
				final CheckBox radio = (CheckBox) findViewById(R.id.tg_radio);
				final CheckBox tv = (CheckBox) findViewById(R.id.tg_tv);
				TextView Done = (TextView) findViewById(R.id.tg_done);
				
				Done.setOnClickListener(new OnClickListener() {	
					@Override
					public void onClick(View v) {
						if(adv != 0){
							Toast.makeText(TycoonGame.this, "You only can choose media once in a day", Toast.LENGTH_SHORT).show();
						}
						else {
							if(flyer.isChecked()) {
								tempmoney += 300;
								adv += 10;
							}
							if(newspaper.isChecked()) {
								tempmoney += 550;
								adv += 20;
							}
							if(radio.isChecked()) {
								tempmoney += 800;
								adv += 30;
							}
							if(tv.isChecked()) {
								tempmoney += 1050;
								adv += 40;
							}
							
							if(adv == 0){
								Toast.makeText(TycoonGame.this, "Please choose at least one media.", Toast.LENGTH_SHORT).show();
							}
							else if(tempmoney > money){
								Toast.makeText(TycoonGame.this, "You don't have enough money.", Toast.LENGTH_SHORT).show();
								tempmoney = 0;
								adv = 0;
							}
							else {
								money -= tempmoney;
								tg_data = getSharedPreferences(tg_pref, MODE_PRIVATE);
								SharedPreferences.Editor editor = tg_data.edit();
								editor.putInt("Money", money);
								editor.putInt("Advertise", adv);
								editor.commit();
								Toast.makeText(TycoonGame.this, "Your store will be advertised on these media.", Toast.LENGTH_SHORT).show();
								tg_status_and_back();
							}
						}
					}
				});
			};
		});
	    
	    Journal.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				setContentView(R.layout.activity_tg_journal);
				backdisable = true;
				
				tg_background();				
				tg_status_and_back();
				
				//set list view
				ListView listv = (ListView) findViewById(R.id.listv);	
				
				//set string for day
				for(int i = 0; i < 30; i++){
					day[i] = "Day " + Integer.toString(i + 1);
				}
				
				//retrieve string
				ArrayList<ListItem> ListItem = GetSearchResults(day, mpd, rpd);		
				listv.setAdapter(new ListItemAdapter(TycoonGame.this, ListItem));
			};
		});
	    
	    Start.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				setContentView(R.layout.activity_tg_end_day);
				Random r = new Random();
				
				tg_background();
				
				TextView status = (TextView)findViewById(R.id.tg_status);
				status.setText("TODAY'S RESULT");
				
				TextView countday = (TextView)findViewById(R.id.tg_day);
				TextView visitor = (TextView)findViewById(R.id.tg_visitor);
				TextView sold = (TextView)findViewById(R.id.tg_sold);
				TextView mmoney = (TextView)findViewById(R.id.tg_money);
				
				Button Back = (Button) findViewById(R.id.tg_back_button);
				Back.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						tg_play();
					};
				});
				
				//start day
				tempstock = 0;
				tempday++;
				countday.setText("DAY - " + (tempday + 1));
				
				//count total visitor
				totalVisitor = (r.nextInt(10) + 1) + ((reputation + adv) /2);
				visitor.setText("Total visitor: " + Integer.toString(totalVisitor));
				
				//count items sold 
				for(int i = 0; i < 15; i++){
					tempmoney = 0;
					if (i_stock[i] != 0){
						double x = (int) (0.5 * i_buyp[i]);
						double y = (int) (0.5 * i_buyp[i]);
						
						if (i_sellp[i] < y){
							tempmoney = i_stock[i] * i_sellp[i];
							tempstock += i_stock[i];
							i_stock[i] = 0;
						}
						else {
							double i1 = (x + y)/((double)(i_sellp[i]-i_buyp[i]) + y);
							double i2 = (double) (r.nextInt(26) + 5) / 100;
							temp = (int) ((i1 * i2 * (double)(totalVisitor)));
							
							if (temp <= i_stock[i]){
								tempmoney = temp * i_sellp[i];
								tempstock += temp;
								i_stock[i] -= temp;
							}
							else {
								tempmoney = i_stock[i] * i_sellp[i];
								tempstock += i_stock[i];
								i_stock[i] = 0;
							}
						}
					}
					money += tempmoney;
				}
				totalSold = tempstock;
				sold.setText("Total sold: " + Integer.toString(totalSold));
				
				//count reputation
				if(tempday == 0)
					temp = (int) (((totalVisitor * 0.7 + totalSold * 0.3) + reputation) /2);
				else
					temp = (int) (((totalVisitor * 0.7 + totalSold * 0.3) + Integer.parseInt(rpd[tempday-1])) /2);
				if (temp > 100) 
					temp = 100;
				reputation = temp;
				rpd[tempday] = Integer.toString(reputation);
				mpd[tempday] = Integer.toString(money);
				
				//reset day
				adv = 0;
				tempstock = 0;
				
				tg_data = getSharedPreferences(tg_pref, MODE_PRIVATE);
				SharedPreferences.Editor editor = tg_data.edit();
				StringBuilder sb1 = new StringBuilder();
				StringBuilder sb2 = new StringBuilder();
				StringBuilder sb3 = new StringBuilder();
				for (int i = 0; i < i_stock.length; i++) {
				    sb1.append(i_stock[i]).append(",");
				}
				for (int i = 0; i < rpd.length; i++) {
					sb2.append(rpd[i]).append(",");
					sb3.append(mpd[i]).append(",");
				}
				editor.putString("Stock", sb1.toString());
				editor.putString("Reputation Per Day", sb2.toString());
				editor.putString("Money Per Day", sb3.toString());
				editor.putInt("Money", money);
				editor.putInt("Reputation", reputation);
				editor.putInt("Day", tempday);
				editor.putInt("Advertise", adv);
				editor.commit();
				
				mmoney.setText("Current money: " + Integer.toString(money));
				tempstock =0;
				for(int i = 0; i < i_stock.length; i++){
					tempstock += i_stock[i];
				}
				
				if(money == 0 || (tempstock == 0 && money < 38)){
					Back.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v) {
							setContentView(R.layout.activity_tg_bankrupt);
							backdisable = false;
							tg_background();
							
							TextView title = (TextView)findViewById(R.id.tg_title);
							TextView details = (TextView)findViewById(R.id.tg_details);
							TextView mmmoney = (TextView)findViewById(R.id.tg_money);
							TextView rrep = (TextView)findViewById(R.id.tg_reputation);
							
							title.setText("GAME OVER");
							details.setText("I'm sorry. But you don't have enough money to continue day. Your boutique has been bankrupt :(");
							mmmoney.setText("Your end money: " + money);
							rrep.setText("Your end reputation: " + reputation);
							
							tg_data.edit().clear().commit(); 
							
							File f = new File(getApplicationContext().getFilesDir().getParentFile().getPath() + "/shared_prefs/tg_data.xml"); 
							f.delete();
						};
					});
				}
				
				if(tempday == 29){
					Back.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v) {
							setContentView(R.layout.activity_tg_end);
							backdisable = false;
							tg_background();
							temp = 0;
							
							TextView title = (TextView)findViewById(R.id.tg_title);
							TextView details = (TextView)findViewById(R.id.tg_details);
							TextView mmmoney = (TextView)findViewById(R.id.tg_money);
							TextView asset = (TextView)findViewById(R.id.tg_asset);
							TextView endm = (TextView)findViewById(R.id.tg_end_money);
							TextView rrep = (TextView)findViewById(R.id.tg_reputation);
							
							for(int i = 0; i < 15; i++)
								temp += (i_stock[i] * i_sellp[i]);
							
							title.setText("YOUR GAME ENDS HERE");
							details.setText("YAY! Your boutique has grown and managed to survive through the first month. We believe that " + boutique_name + " will continue to grow in the future. Thank you for playing, " + char_name + "!");
							mmmoney.setText("Your money: " + money);
							asset.setText("Your asset: " + temp);
							temp += money;
							endm.setText("Your end money: " + temp);
							rrep.setText("Your end reputation: " + reputation);
							tempmoney = temp;
							temp = reputation;
							
							tg_data.edit().clear().commit(); 
							File f = new File(getApplicationContext().getFilesDir().getParentFile().getPath() + "/shared_prefs/tg_data.xml"); 
							f.delete();
							
							if(tempmoney >= 10000 && temp > 50){
								tg_result = getSharedPreferences(tg_pref_result, MODE_PRIVATE);
								SharedPreferences.Editor editor = tg_result.edit();
								editor.putInt("tg_ipa1", 30);
								editor.putInt("tg_ipa2", 5);
								editor.putInt("tg_ips1", 10);
								editor.putInt("tg_ips2", 55);
								editor.commit();
							}
							else{
								tg_result = getSharedPreferences(tg_pref_result, MODE_PRIVATE);
								SharedPreferences.Editor editor = tg_result.edit();
								editor.putInt("tg_ipa1", 5);
								editor.putInt("tg_ipa2", 30);
								editor.putInt("tg_ips1", 55);
								editor.putInt("tg_ips2", 10);
								editor.commit();
							}
						};
					});
				}
			};
		});
	}
	
	public void tg_help(){
		setContentView(R.layout.activity_tg_help);
		backdisable = false;
		
		TextView title = (TextView)findViewById(R.id.tg_title);
		TextView details1 = (TextView)findViewById(R.id.tg_details1);
		TextView details2 = (TextView)findViewById(R.id.tg_details2);
		TextView details3 = (TextView)findViewById(R.id.tg_details3);
		TextView details4 = (TextView)findViewById(R.id.tg_details4);
		TextView details5 = (TextView)findViewById(R.id.tg_details5);
		TextView details6 = (TextView)findViewById(R.id.tg_details6);
		
		title.setText("WELCOME!");
		details1.setText("We’re really happy to know that you’re opening a new boutique in here. But of course, we have to explain how the game works so you can run your business smoothly ;)");
		details2.setText("First of all you will have to BUY CLOTHES to fill your boutique. We’re not mad if you only buy some pieces, but we hope you can buy more pieces in future. Remember to watch your money when you are buying something. It’s not a bank, it’s your wallet!");
		details3.setText("After that, you have to SET PRICE for every item that you already bought. This is not charity, so we have to take more profit than we can imagine! ");
		details4.setText("Remember that your boutique will only crawl and eventually go bankrupt if you don’t let people know about it. ADVERTISE in media, so more people will know about your boutique and you will get more money! Don’t forget that you can only choose media once a day. And your media will only last for one day, so you have to advertise again in the next day.");
		details5.setText("We’re collecting the data of how your days went and we put in JOURNAL. Don’t forget to take a look so you can make a better progress in the upcoming days.");
		details6.setText("And then finally you can open your boutique’s front door and START A DAY. Hope you get more money from that! We will only watch your progress in first month, so make a good business in a month at least! After that, we’ll see if you’re qualified to run your boutique ;) But remember, we have zero tolerance if you don’t have money or stock to run your next day. We will close your boutique fiercely! So please try your best ;)");
	}
	
	//*********methods to help the code**********
	
	//arraylist for journal
	public ArrayList<ListItem> GetSearchResults(String string1[], String string2[], String string3[]){
	     ArrayList<ListItem> results = new ArrayList<ListItem>();
	 
	     for(int i = 0; i < tempday+1; ++i){
	    	 ListItem sr = new ListItem();
		     sr.setcol1(string1[i]);
		     sr.setcol2("Money: " + string2[i]);
		     sr.setcol3("Reputation: " + string3[i]);
		     results.add(sr);
	     }
	     return results;
	}
	
	//method for saved price
	public void tg_save_price(){
		tg_data = getSharedPreferences(tg_pref, MODE_PRIVATE);
		SharedPreferences.Editor editor = tg_data.edit();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < i_sellp.length; i++) {
		    sb.append(i_sellp[i]).append(",");
		}
		editor.putString("Sell Price", sb.toString());
		editor.commit();
		Toast.makeText(TycoonGame.this, "Price has been changed successfully.", Toast.LENGTH_SHORT).show();
	}
	
	//method to show custom price
	public void tg_price(){
		TextView Text1 = (TextView) findViewById(R.id.tg_owned1);
		Text1.setText("Your price: " + i_sellp[0]);
		TextView Text2 = (TextView) findViewById(R.id.tg_owned2);
		Text2.setText("Your price: " + i_sellp[1]);
		TextView Text3 = (TextView) findViewById(R.id.tg_owned3);
		Text3.setText("Your price: " + i_sellp[2]);
		TextView Text4 = (TextView) findViewById(R.id.tg_owned4);
		Text4.setText("Your price: " + i_sellp[3]);
		TextView Text5 = (TextView) findViewById(R.id.tg_owned5);
		Text5.setText("Your price: " + i_sellp[4]);
		TextView Text6 = (TextView) findViewById(R.id.tg_owned6);
		Text6.setText("Your price: " + i_sellp[5]);
		TextView Text7 = (TextView) findViewById(R.id.tg_owned7);
		Text7.setText("Your price: " + i_sellp[6]);
		TextView Text8 = (TextView) findViewById(R.id.tg_owned8);
		Text8.setText("Your price: " + i_sellp[7]);
		TextView Text9 = (TextView) findViewById(R.id.tg_owned9);
		Text9.setText("Your price: " + i_sellp[8]);
		TextView Text10 = (TextView) findViewById(R.id.tg_owned10);
		Text10.setText("Your price: " + i_sellp[9]);
		TextView Text11 = (TextView) findViewById(R.id.tg_owned11);
		Text11.setText("Your price: " + i_sellp[10]);
		TextView Text12 = (TextView) findViewById(R.id.tg_owned12);
		Text12.setText("Your price: " + i_sellp[11]);
		TextView Text13 = (TextView) findViewById(R.id.tg_owned13);
		Text13.setText("Your price: " + i_sellp[12]);
		TextView Text14 = (TextView) findViewById(R.id.tg_owned14);
		Text14.setText("Your price: " + i_sellp[13]);
		TextView Text15 = (TextView) findViewById(R.id.tg_owned15);
		Text15.setText("Your price: " + i_sellp[14]);
	}
	
	//method for purchasing
	public void tg_purchasing(int j){
		if(tempmoney > money)
			Toast.makeText(TycoonGame.this, "You don't have enough money to buy.", Toast.LENGTH_SHORT).show();
		else {
			money -= tempmoney;
			i_stock[j] += tempstock;
			tg_data = getSharedPreferences(tg_pref, MODE_PRIVATE);
			SharedPreferences.Editor editor = tg_data.edit();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < i_stock.length; i++) {
			    sb.append(i_stock[i]).append(",");
			}
			editor.putString("Stock", sb.toString());
			editor.putInt("Money", money);
			editor.commit();
			Toast.makeText(TycoonGame.this, "Purchase successful.", Toast.LENGTH_SHORT).show();
		}
	}
	
	//method to show stock
	public void tg_stock(){
		TextView Text1 = (TextView) findViewById(R.id.tg_owned1);
		Text1.setText("You own: " + i_stock[0]);
		TextView Text2 = (TextView) findViewById(R.id.tg_owned2);
		Text2.setText("You own: " + i_stock[1]);
		TextView Text3 = (TextView) findViewById(R.id.tg_owned3);
		Text3.setText("You own: " + i_stock[2]);
		TextView Text4 = (TextView) findViewById(R.id.tg_owned4);
		Text4.setText("You own: " + i_stock[3]);
		TextView Text5 = (TextView) findViewById(R.id.tg_owned5);
		Text5.setText("You own: " + i_stock[4]);
		TextView Text6 = (TextView) findViewById(R.id.tg_owned6);
		Text6.setText("You own: " + i_stock[5]);
		TextView Text7 = (TextView) findViewById(R.id.tg_owned7);
		Text7.setText("You own: " + i_stock[6]);
		TextView Text8 = (TextView) findViewById(R.id.tg_owned8);
		Text8.setText("You own: " + i_stock[7]);
		TextView Text9 = (TextView) findViewById(R.id.tg_owned9);
		Text9.setText("You own: " + i_stock[8]);
		TextView Text10 = (TextView) findViewById(R.id.tg_owned10);
		Text10.setText("You own: " + i_stock[9]);
		TextView Text11 = (TextView) findViewById(R.id.tg_owned11);
		Text11.setText("You own: " + i_stock[10]);
		TextView Text12 = (TextView) findViewById(R.id.tg_owned12);
		Text12.setText("You own: " + i_stock[11]);
		TextView Text13 = (TextView) findViewById(R.id.tg_owned13);
		Text13.setText("You own: " + i_stock[12]);
		TextView Text14 = (TextView) findViewById(R.id.tg_owned14);
		Text14.setText("You own: " + i_stock[13]);
		TextView Text15 = (TextView) findViewById(R.id.tg_owned15);
		Text15.setText("You own: " + i_stock[14]);
	}

	//method to select background
	public void tg_background(){
		RelativeLayout layout =(RelativeLayout)findViewById(R.id.tg_background);	
		if (location == 0)
			layout.setBackgroundResource(R.drawable.tg_amsterdam);
    	else if (location == 1)
    		layout.setBackgroundResource(R.drawable.tg_berlin);    		
    	else if (location == 2)
    		layout.setBackgroundResource(R.drawable.tg_budapest);		
    	else if (location == 3)
    		layout.setBackgroundResource(R.drawable.tg_tokyo);	
    	else if (location == 4)
    		layout.setBackgroundResource(R.drawable.tg_london);
    	else if (location == 5)
    		layout.setBackgroundResource(R.drawable.tg_paris);
    	else if (location == 6)
    		layout.setBackgroundResource(R.drawable.tg_sydney);
    	else if (location == 7)
    		layout.setBackgroundResource(R.drawable.tg_newyork);
	}
	
	//method to select background2
	public void tg_background_scroll(){
		ScrollView layout =(ScrollView)findViewById(R.id.scrollv);
		if (location == 0)
			layout.setBackgroundResource(R.drawable.tg_amsterdam);
    	else if (location == 1)
    		layout.setBackgroundResource(R.drawable.tg_berlin);    		
    	else if (location == 2)
    		layout.setBackgroundResource(R.drawable.tg_budapest);		
    	else if (location == 3)
    		layout.setBackgroundResource(R.drawable.tg_tokyo);	
    	else if (location == 4)
    		layout.setBackgroundResource(R.drawable.tg_london);
    	else if (location == 5)
    		layout.setBackgroundResource(R.drawable.tg_paris);
    	else if (location == 6)
    		layout.setBackgroundResource(R.drawable.tg_sydney);
    	else if (location == 7)
    		layout.setBackgroundResource(R.drawable.tg_newyork);
	}
	
	//method to print status and do the back button
	public void tg_status_and_back(){
		TextView status = (TextView)findViewById(R.id.tg_status);
		status.setText(char_name + ", your money is $" + money);
		
		Button Back = (Button) findViewById(R.id.tg_back_button);
		Back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				tg_play();
			};
		});
	}
	
	//method to disable or enable back button.
	@Override
	public void onBackPressed() {
	    if(backdisable == false){
	        super.onBackPressed();
	    }
	}	
}