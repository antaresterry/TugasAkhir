package com.skripsi.atma;

import java.util.Random;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AnatomyGame extends Activity {
	//declare all variable
	ImageView Img;
	TextView Text;
	TextView Ans1;
	TextView Ans2;
	TextView Ans3;
	TextView Ans4;
	TextView Ans5;
	TextView nilai;
	Button Confirm;
	ProgressBar mProgressBar;
    CountDownTimer mCountDownTimer;
    public int i=0;
    public int a=0;
    public int total=0;
    public static int finalScore;
    public int count=4;
    public int n=0;
    public int rnd;
    private SharedPreferences saveGame;
	private String prefName = "saveScoreAnatomy";
	private String prefAnatomy = "saveAnatomy";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anatomy__game);
        
        //create all object
        Ans1=(TextView) findViewById(R.id.textView1);
        Ans2=(TextView) findViewById(R.id.textView2);
        Ans3=(TextView) findViewById(R.id.textView3);
        Ans4=(TextView) findViewById(R.id.textView4);
        Ans5=(TextView) findViewById(R.id.textView5);       
        nilai=(TextView) findViewById(R.id.textView6);
        Img = (ImageView) findViewById(R.id.imageView1);
        Confirm=(Button) findViewById(R.id.confirm);
        
        Random generator = new Random(); 
        rnd = generator.nextInt(3);
        
        mProgressBar=(ProgressBar)findViewById(R.id.progressBar1);
        //countdown timer and progress bar
        mProgressBar.setProgress(i);
           mCountDownTimer=new CountDownTimer(30000,1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
                    i++;
                    //TextView tv = (TextView) findViewById(R.id.textView1);
					//tv.setText(String.valueOf(i));
                    mProgressBar.setProgress(i);
                }

                @Override
                public void onFinish() {
                //Do what you want 
                    i++;
                    mProgressBar.setProgress(i);
                    finalScore=total+(750-(i*25));
					saveGame = getSharedPreferences(prefName, MODE_PRIVATE);
					SharedPreferences.Editor editor = saveGame.edit();
					editor.putInt("tempScore", finalScore);
					editor.commit();
                    finish();
                }
            };
            mCountDownTimer.start();
            
            changeImage(count);
            if(rnd==0)
            {
            	Ans1.setText("Zigometik");
            	Ans2.setText("Maksila");
            	Ans3.setText("Frontal");
            	Ans4.setText("Temporal");
            	Ans5.setText("Mandibula");
            }
            else if(rnd==1)
            {
            	Ans1.setText("Retina");
            	Ans2.setText("Pupil");
            	Ans3.setText("Cornea");
            	Ans4.setText("Iris");
            	Ans5.setText("Lens");
            }
            else if(rnd==2)
            {
            	Ans1.setText("Sebaceous");
            	Ans2.setText("Sweat gland");
            	Ans3.setText("Hair");
            	Ans4.setText("Pore");
            	Ans5.setText("Hair Follicle");
            }
        //marking answer
        Ans1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				a=1;
				Ans1.setBackgroundColor(Color.RED);
				Ans2.setBackgroundColor(Color.BLACK);
				Ans3.setBackgroundColor(Color.BLACK);
				Ans4.setBackgroundColor(Color.BLACK);
				Ans5.setBackgroundColor(Color.BLACK);
				Confirm.setClickable(true);
				mark(a);				
			};
		});
        Ans2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				a=2;
				Ans1.setBackgroundColor(Color.BLACK);
				Ans2.setBackgroundColor(Color.RED);
				Ans3.setBackgroundColor(Color.BLACK);
				Ans4.setBackgroundColor(Color.BLACK);
				Ans5.setBackgroundColor(Color.BLACK);
				Confirm.setClickable(true);
				mark(a);

			};
		});	
        Ans3.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				a=3;
				Ans1.setBackgroundColor(Color.BLACK);
				Ans2.setBackgroundColor(Color.BLACK);
				Ans3.setBackgroundColor(Color.RED);
				Ans4.setBackgroundColor(Color.BLACK);
				Ans5.setBackgroundColor(Color.BLACK);
				Confirm.setClickable(true);
				mark(a);
			};
		});	
        Ans4.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				a=4;
				Ans1.setBackgroundColor(Color.BLACK);
				Ans2.setBackgroundColor(Color.BLACK);
				Ans3.setBackgroundColor(Color.BLACK);
				Ans4.setBackgroundColor(Color.RED);
				Ans5.setBackgroundColor(Color.BLACK);
				Confirm.setClickable(true);
				mark(a);					
			};
		});	
        Ans5.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				a=5;
				Ans1.setBackgroundColor(Color.BLACK);
				Ans2.setBackgroundColor(Color.BLACK);
				Ans3.setBackgroundColor(Color.BLACK);
				Ans4.setBackgroundColor(Color.BLACK);
				Ans5.setBackgroundColor(Color.RED);
				Confirm.setClickable(true);
				mark(a);				
			};
		}); 
		//when confirm button clicked
		Confirm.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {				
				total=total+n;
				
				if(count > 0)
				count--;
				else
				{
					Confirm.setClickable(false);
					count=4;					
					finalScore=total+(750-(i*25));
					saveGame = getSharedPreferences(prefName, MODE_PRIVATE);
					SharedPreferences.Editor editor = saveGame.edit();
					editor.putInt("nilai", finalScore);
					editor.commit();
					nilai.setText(Integer.toString(finalScore));
					chlayout();
				}
				changeImage(count);
				if(a==1)
				{
					Ans1.setClickable(false);
					Ans1.setVisibility(View.INVISIBLE);
				}
				else if(a==2)
					{
					Ans2.setClickable(false);
					Ans2.setVisibility(View.INVISIBLE);
					}
				else if(a==3)
				{
					Ans3.setClickable(false);
					Ans3.setVisibility(View.INVISIBLE);
				}
				else if(a==4)
				{
					Ans4.setClickable(false);
					Ans4.setVisibility(View.INVISIBLE);
				}
				else if(a==5)
				{
					Ans5.setClickable(false);
					Ans5.setVisibility(View.INVISIBLE);
				}
				Confirm.setClickable(false);
			};
		});	
	}
	//checking answer, n=100 if correct
	public void mark(int A){
		switch (rnd){
		case 0:{
			if(A==1 && count==4)
				n=100;
			else
				if(A==2 && count==3)
					n=100;
				else
					if(A==3 && count ==2)
						n=100;
					else
						if(A==4 && count==1)
							n=100;
						else
							if(A==5 && count==0)
								n=100;
							else
								n=0;
			break;
		}
		case 1:{
			if(A==1 && count==4)
				n=100;
			else
				if(A==5 && count==3)
					n=100;
				else
					if(A==2 && count ==2)
						n=100;
					else
						if(A==4 && count==1)
							n=100;
						else
							if(A==3 && count==0)
								n=100;
							else
								n=0;
			break;
		}
		case 2:{
			if(A==3 && count==4)
				n=100;
			else
				if(A==5 && count==3)
					n=100;
				else
					if(A==1 && count ==2)
						n=100;
					else
						if(A==4 && count==1)
							n=100;
						else
							if(A==2 && count==0)
								n=100;
							else
								n=0;
			break;
		}
		}
	}	
	//change image
	public void changeImage(int count) {
		if(count==4) { 
        	if(rnd==0)
        		Img.setBackgroundResource(R.drawable.anatomy_head_zigometik);
        	else if(rnd==1)
        		Img.setBackgroundResource(R.drawable.retina);
        	else
        		Img.setBackgroundResource(R.drawable.hair);
        }
        else if(count==3)
        {
        	if(rnd==0)
        		Img.setBackgroundResource(R.drawable.anatomy_head_maksila);
        	else if(rnd==1)
        		Img.setBackgroundResource(R.drawable.lens);
        	else
        		Img.setBackgroundResource(R.drawable.hairfollicle); 
        } 
        else if(count==2){
        	if(rnd==0)
        		Img.setBackgroundResource(R.drawable.anatomy_head_frontal);
        	else if(rnd==1)
        		Img.setBackgroundResource(R.drawable.pupil);
        	else
        		Img.setBackgroundResource(R.drawable.sebaceous); 
        }
        else if(count==1)
        {
        	if(rnd==0)
        		Img.setBackgroundResource(R.drawable.anatomy_head_temporal);
        	else if(rnd==1)
        		Img.setBackgroundResource(R.drawable.iris);
        	else
        		Img.setBackgroundResource(R.drawable.pore); 
        } 
        else if(count==0)
        {
        	if(rnd==0)
        		Img.setBackgroundResource(R.drawable.anatomy_head_mandibula);
        	else if(rnd==1)
        		Img.setBackgroundResource(R.drawable.cornea);
        	else
        		Img.setBackgroundResource(R.drawable.sweatgland); 
        } 
        else
        {
        	if(rnd==0)
        		Img.setBackgroundResource(R.drawable.anatomy_head_zigometik);
        	else if(rnd==1)
        		Img.setBackgroundResource(R.drawable.retina);
        	else
        		Img.setBackgroundResource(R.drawable.hair);  			            
        }
	}
	public void chlayout()
	 {
		  setContentView(R.layout.activity_anatomy__score);
		  TextView t=(TextView) findViewById(R.id.textView5);
		  t.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					finish();
				};
			}); 
		  saveGame = getSharedPreferences(prefName, MODE_PRIVATE);
		  int highscore = saveGame.getInt("highscore", 0);
		  TextView FinalScore = (TextView) findViewById(R.id.textView1);
		  FinalScore.setText(Integer.toString(finalScore));
		  TextView HighScore = (TextView) findViewById(R.id.textView4);
		  HighScore.setText(Integer.toString(highscore));
		  if(highscore>=800){	
			  saveGame = getSharedPreferences(prefAnatomy, MODE_PRIVATE);
			  SharedPreferences.Editor editor = saveGame.edit();
			  editor.putInt("ipa1",10);
			  editor.putInt("ipa2",70);
			  editor.putInt("ips1",10);
			  editor.putInt("ips2",10);
			  editor.commit();
			}
			else{
				saveGame = getSharedPreferences(prefAnatomy, MODE_PRIVATE);
				SharedPreferences.Editor editor = saveGame.edit();
				editor.putInt("ipa1",30);
				editor.putInt("ipa2",10);
				editor.putInt("ips1",30);
				editor.putInt("ips2",30);
				editor.commit();
			}
	 }
}
