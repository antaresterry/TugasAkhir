package com.skripsi.atma;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;

public class EngineGame extends Activity {
	//declare all variable
	private SharedPreferences engine;
	private String prefEngine = "saveEngine";
	ImageView Img;
	TextView Text;
	TextView Ans1;
	TextView Ans2;
	TextView Ans3;
	TextView Ans4;
	TextView Ans5;
	TextView Quest;
	EditText input;
	Button Confirm;
    public int nilai=0;
    public int confirm1=0;
    public double rpm=0;
    public static int finalScore;
    int count=1;
    int c=0;
    public int save=0;
    public int piston=0;
    public String carb;
    public int carbSize=0;
    int ans=1;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engine__design__game);
        
        //assembling UI
        Ans1=(TextView) findViewById(R.id.textView1);
        Ans2=(TextView) findViewById(R.id.textView2);
        Ans3=(TextView) findViewById(R.id.textView3);
        Ans4=(TextView) findViewById(R.id.textView4);
        Ans5=(TextView) findViewById(R.id.textView5);
        Quest=(TextView) findViewById(R.id.textView6);
        Img = (ImageView) findViewById(R.id.imageView1);
        Confirm=(Button) findViewById(R.id.confirm);
        input=(EditText) findViewById(R.id.editText1);
        
        changeImage(count);
        //option for choice1
        choice1();
        //marking answer
        Ans1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				int A1=1;
				if(c==0 && A1==1){
				Ans1.setBackgroundColor(Color.RED);
				Ans2.setClickable(false);
				Ans3.setClickable(false);
				Ans4.setClickable(false);
				Ans5.setClickable(false);
				Confirm.setClickable(true);
				mark1(A1);
				A1=11;
				c=1;
				}
				else
				{
					Ans1.setBackgroundColor(Color.BLACK);
					Ans2.setClickable(true);
					Ans3.setClickable(true);
					Ans4.setClickable(true);
					Ans5.setClickable(true);
					c=0;
					Confirm.setClickable(false);
					A1=1;
				}
			};
		});
        Ans2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				int A2=2;
				if(c==0 && A2==2){
					Ans2.setBackgroundColor(Color.RED);
					Ans1.setClickable(false);
					Ans3.setClickable(false);
					Ans4.setClickable(false);
					Ans5.setClickable(false);
					c=1;
					Confirm.setClickable(true);
					mark1(A2);
					A2=12;
					}
					else
						if(c==1)
					{
						Ans2.setBackgroundColor(Color.BLACK);
						Ans1.setClickable(true);
						Ans3.setClickable(true);
						Ans4.setClickable(true);
						Ans5.setClickable(true);
						c=0;
						Confirm.setClickable(false);
						A2=2;
					}
			};
		});	
        Ans3.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				int A3=3;
				if(c==0 && A3==3){
				Ans3.setBackgroundColor(Color.RED);
				Ans2.setClickable(false);
				Ans1.setClickable(false);
				Ans4.setClickable(false);
				Ans5.setClickable(false);
				c=1;
				Confirm.setClickable(true);
				mark1(A3);
				A3=13;
				}
				else
					if(c==1){
						Ans3.setBackgroundColor(Color.BLACK);
						Ans2.setClickable(true);
						Ans1.setClickable(true);
						Ans4.setClickable(true);
						Ans5.setClickable(true);
						c=0;
						Confirm.setClickable(false);
						A3=3;
					}
			};
		});	
        Ans4.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				int A4=4;
				if(c==0 && A4==4){
				Ans4.setBackgroundColor(Color.RED);
				Ans2.setClickable(false);
				Ans3.setClickable(false);
				Ans1.setClickable(false);
				Ans5.setClickable(false);
				c=1;
				Confirm.setClickable(true);
				mark1(A4);
				A4=14;
				}
				else
					if(c==1){
						Ans4.setBackgroundColor(Color.BLACK);
						Ans2.setClickable(true);
						Ans3.setClickable(true);
						Ans1.setClickable(true);
						Ans5.setClickable(true);
						c=0;
						Confirm.setClickable(false);
						A4=4;
					}					
			};
		});	
        Ans5.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				int A5=5;
				if(c==0 && A5==5){
				Ans5.setBackgroundColor(Color.RED);
				Ans2.setClickable(false);
				Ans3.setClickable(false);
				Ans4.setClickable(false);
				Ans1.setClickable(false);
				c=1;
				Confirm.setClickable(true);
				mark1(A5);
				A5=15;
				}
				else
					if(c==1){
						Ans5.setBackgroundColor(Color.BLACK);
						Ans2.setClickable(true);
						Ans3.setClickable(true);
						Ans4.setClickable(true);
						Ans1.setClickable(true);
						c=0;
						Confirm.setClickable(false);
						A5=5;
					}
			};
		});     
	}
	//checking answer
	private void mark1(int A){
		if(A==1)
			piston=250;
		else
			if(A==2)
				piston=225;
			else
				if(A==3)
					piston=200;
				else
					if(A==4)
						piston=180;
					else
						if(A==5)
							piston=150;
						else
							piston=0;
		//when confirm button clicked
		Confirm.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {						
				if(confirm1==1)					
					carbSize();
				else if(count > 0)
				{
					choice2();
					count--;
				}
				else
				{
					Confirm.setClickable(false);
					count=1;
					confirm1=0;
					
				}
				confirm1=1;
				changeImage(count);
			};
		});		
	}
	//get carburetor size
	public void carbSize()
	{
			carbSize=Integer.parseInt(input.getText().toString());
			//Quest.setText(String.valueOf(carbSize));
			if(carbSize<=25 || carbSize>=35)
				Toast.makeText(getApplicationContext(), "The carburetor size must be 26-34", Toast.LENGTH_LONG).show();
			else
				result();
	}
	public void choice1()
	{
		input.setVisibility(View.INVISIBLE);
		input.setClickable(false);
		Ans1.setText("75x56.5");
		Ans2.setText("70x58");
		Ans3.setText("63.5x62.2");
		Ans4.setText("63.5x56.4");
		Ans5.setText("57x58.7");
		Quest.setText("Choose Piston Size : ");
		
	}
	public void choice2()
	{
		Ans1.setVisibility(View.INVISIBLE);
		Ans2.setVisibility(View.INVISIBLE);
		Ans3.setVisibility(View.INVISIBLE);
		Ans4.setVisibility(View.INVISIBLE);
		Ans5.setVisibility(View.INVISIBLE);
		Ans1.setClickable(false);
		Ans2.setClickable(false);
		Ans3.setClickable(false);
		Ans4.setClickable(false);
		Ans5.setClickable(false);
		input.setVisibility(View.VISIBLE);
		input.setClickable(true);
		input.setFocusable(true); 
		Quest.setText("Input Carburetor Venturi : ");
	}
	//result max power @ rpm x
	public void result()
	{		
			double c=0.65;
			setContentView(R.layout.activity_engine__design__game_2);
			rpm=((carbSize/c)*(carbSize/c)/piston)*1000;
			TextView maxrpm = (TextView) findViewById(R.id.textView3);
			TextView maxhp = (TextView) findViewById(R.id.textView4);
			maxrpm.setText(new DecimalFormat("##.##").format(rpm)+"RPM");
			if(piston==250)
			{
				if(rpm>=10000 && rpm<=12000)
					save=1;
				else 
					save=0;
				maxhp.setText("25 HP");
			}
			else if(piston==225)
			{
				if(rpm>=9000 && rpm<=10500)
					save=1;				
				else
					save=0;
				maxhp.setText(" 21 HP");
			}
			else if(piston==200)
			{
				if(rpm>=8000 && rpm<=11000)
					save=1;
				else
					save=0;
				maxhp.setText(" 19 HP");
			}
			else if(piston==180)
			{
				if(rpm>=8000 && rpm<=9500)
					save=1;
				else
					save=0;
				maxhp.setText(" 17 HP");
			}
			else
			{
				if(rpm>=10500 && rpm<=13000)
					save=1;
				else
					save=0;
				maxhp.setText(" 15 HP");
			}
			save();
	}
	public void save()
	{
		TextView result = (TextView) findViewById(R.id.textView7);
		if(save==1){
			result.setText("Your combination is optimal.");
			engine = getSharedPreferences(prefEngine, MODE_PRIVATE);
			SharedPreferences.Editor editor = engine.edit();
			editor.putInt("ipa1", 70);
			editor.putInt("ipa2", 10);
			editor.putInt("ips1", 10);
			editor.putInt("ips2", 10);
			editor.commit();
		}
		else{
			result.setText("The size of your piston or carburetor is not appropriate. So, your engine is not working properly.");
			engine = getSharedPreferences(prefEngine, MODE_PRIVATE);
			SharedPreferences.Editor editor = engine.edit();
			editor.putInt("ipa1", 40);
			editor.putInt("ipa2", 20);
			editor.putInt("ips1", 20);
			editor.putInt("ips2", 20);
			editor.commit();
		}
	}
	//change image
	private void changeImage(int count) {
            switch (count) { 
            case 1: 
         	   Img.setBackgroundResource(R.drawable.piston); 
            break; 
            case 0: 
         	   Img.setBackgroundResource(R.drawable.keihin); 
            break; 
            default: 
               Img.setBackgroundResource(R.drawable.ic_launcher); 
            break; 			            
    }	
	}
}