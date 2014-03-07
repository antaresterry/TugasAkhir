package com.skripsi.atma;

import java.util.Random;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ItemFindingGame extends Activity {
	//declare all variable
	ProgressBar mProgressBar;
    CountDownTimer mCountDownTimer;
    public int i=0;
    public int n=77;
	public int s=0;
    public static int total=0;
    //ImageView BackGround;
    public int checkans=0;
    public int score=0;
	private SharedPreferences saveGame;
	private String prefName = "saveScoreItem";
	private String prefItem = "saveItem";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item__finding__game);
		//countdown timer and progress bar
		mProgressBar=(ProgressBar)findViewById(R.id.progressBar1);       
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
                    finish();
                }
            };
            mCountDownTimer.start();
            
            //random for set question
            Random generator = new Random();
            int random = generator.nextInt(3);
            if(random==0)
            {
            	set1();
            }
            else if(random==1)
            {
            	set2(); 	
            }
            else
            {
            	set3();
            }  
	}
	//set soal 1
	 public void set1()
	    {
		 final TextView list1 = (TextView)findViewById(R.id.list1); 
	     final TextView list2 = (TextView)findViewById(R.id.list2);
	     final TextView list3 = (TextView)findViewById(R.id.list3);
	     final TextView list4 = (TextView)findViewById(R.id.list4);
	     final TextView list5 = (TextView)findViewById(R.id.list5);
	     final TextView list6 = (TextView)findViewById(R.id.list6);
	     final ImageView Img1 = (ImageView)findViewById(R.id.item1);
	     final ImageView Img2 = (ImageView)findViewById(R.id.item2);
	     final ImageView Img3 = (ImageView)findViewById(R.id.item3);
	     final ImageView Img4 = (ImageView)findViewById(R.id.item4);
	     final ImageView Img5 = (ImageView)findViewById(R.id.item5);
	     final ImageView Img6 = (ImageView)findViewById(R.id.item6);
		 list1.setText("               Rope");
		 Img1.setClickable(true);
         Img1.setOnClickListener(new OnClickListener(){
 			@Override
 			public void onClick(View v) {
 				Img1.setClickable(false);
 				Img1.setVisibility(View.INVISIBLE);
 				list1.setPaintFlags(list1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
 				checkans++;
 				if(checkans==6)
 			 	{
 					score=750-(i*25);
 					chlayout();
 			 	}
 				mark(n);
 			};
 		});
         list2.setText("Key");
     	 Img2.setClickable(true);
     	 Img2.setOnClickListener(new OnClickListener(){
 			@Override
 			public void onClick(View v) {
 				Img2.setClickable(false);
 				Img2.setVisibility(View.INVISIBLE);
 				list2.setPaintFlags(list2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
 				checkans++;
				if(checkans==6)
			 	{
					score=750-(i*25);
					chlayout();
			 	}
				mark(n);
 			};
 		});
     	list3.setText("Cookie Cutter");
     	Img3.setClickable(true);
     	Img3.setOnClickListener(new OnClickListener(){
 			@Override
 			public void onClick(View v) {
 				Img3.setClickable(false);
 				Img3.setVisibility(View.INVISIBLE);
 				list3.setPaintFlags(list3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
 				checkans++;
				if(checkans==6)
			 	{
					score=750-(i*25);
					chlayout();
			 	}
				mark(n);
 			};
 		});
     	list4.setText("Feather");
     	Img4.setClickable(true);
     	Img4.setOnClickListener(new OnClickListener(){
 			@Override
 			public void onClick(View v) {
 				Img4.setClickable(false);
 				Img4.setVisibility(View.INVISIBLE);
 				list4.setPaintFlags(list4.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
 				checkans++;
				if(checkans==6)
			 	{
					score=750-(i*25);
					chlayout();
			 	}
				mark(n);
 			};
 		});
     	list5.setText("Cockroach");
     	Img5.setClickable(true);
     	Img5.setOnClickListener(new OnClickListener(){
 			@Override
 			public void onClick(View v) {
 				Img5.setClickable(false);
 				Img5.setVisibility(View.INVISIBLE);
 				list5.setPaintFlags(list5.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
 				checkans++;
				if(checkans==6)
			 	{
					score=750-(i*25);
					chlayout();
			 	}
				mark(n);
 			};
 		});
     	list6.setText("Knife");
     	Img6.setClickable(true);
     	Img6.setOnClickListener(new OnClickListener(){
 			@Override
 			public void onClick(View v) {
 				Img6.setClickable(false);
 				Img6.setVisibility(View.INVISIBLE);
 				list6.setPaintFlags(list6.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
 				checkans++;
				if(checkans==6)
			 	{
					score=750-(i*25);
					chlayout();
			 	}
				mark(n);
 			};
 		});    	
	    }
	 //set soal 2
	 public void set2()
	   {
		 final TextView list1 = (TextView)findViewById(R.id.list1); 
	     final TextView list2 = (TextView)findViewById(R.id.list2);
	     final TextView list3 = (TextView)findViewById(R.id.list3);
	     final TextView list4 = (TextView)findViewById(R.id.list4);
	     final TextView list5 = (TextView)findViewById(R.id.list5);
	     final TextView list6 = (TextView)findViewById(R.id.list6);
	     final ImageView Img7 = (ImageView)findViewById(R.id.Item7);
	     final ImageView Img8 = (ImageView)findViewById(R.id.Item8);
	     final ImageView Img9 = (ImageView)findViewById(R.id.Item9);
	     final ImageView Img10 = (ImageView)findViewById(R.id.Item10);
	     final ImageView Img11 = (ImageView)findViewById(R.id.Item11);
	     final ImageView Img12 = (ImageView)findViewById(R.id.Item12);
		list1.setText("  Wood Crate");
		Img7.setClickable(true);
     	Img7.setOnClickListener(new OnClickListener(){
 			@Override
 			public void onClick(View v) {
 				Img7.setClickable(false);
 				Img7.setVisibility(View.INVISIBLE);
 				list1.setPaintFlags(list1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
 				checkans++;
				if(checkans==6)
			 	{
					score=750-(i*25);
					chlayout();
			 	}
				mark(n);
 			};
 		});
     	list2.setText("Piolet Rope");
     	Img8.setClickable(true);
     	Img8.setOnClickListener(new OnClickListener(){
 			@Override
 			public void onClick(View v) {
 				Img8.setClickable(false);
 				Img8.setVisibility(View.INVISIBLE);
 				list2.setPaintFlags(list2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
 				checkans++;
				if(checkans==6)
			 	{
					score=750-(i*25);
					chlayout();
			 	}
				mark(n);
 			};
 		});
     	list3.setText("Bait");
     	Img9.setClickable(true);
     	Img9.setOnClickListener(new OnClickListener(){
 			@Override
 			public void onClick(View v) {
 				Img9.setClickable(false);
 				Img9.setVisibility(View.INVISIBLE);
 				list3.setPaintFlags(list3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
 				checkans++;
				if(checkans==6)
			 	{
					score=750-(i*25);
					chlayout();
			 	}
				mark(n);
 			};
 		});
     	list4.setText("Blood Bottle");
     	Img10.setClickable(true);
     	Img10.setOnClickListener(new OnClickListener(){
 			@Override
 			public void onClick(View v) {
 				Img10.setClickable(false);
 				Img10.setVisibility(View.INVISIBLE);
 				list4.setPaintFlags(list4.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
 				checkans++;
				if(checkans==6)
			 	{
					score=750-(i*25);
					chlayout();
			 	}
				mark(n);
 			};
 		});
     	list5.setText("Lyre");
     	Img11.setClickable(true);
     	Img11.setOnClickListener(new OnClickListener(){
 			@Override
 			public void onClick(View v) {
 				Img11.setClickable(false);
 				Img11.setVisibility(View.INVISIBLE);
 				list5.setPaintFlags(list5.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
 				checkans++;
				if(checkans==6)
			 	{
					score=750-(i*25);
					chlayout();
			 	}
				mark(n);
 			};
 		});
     	list6.setText("Hour Glass");
     	Img12.setClickable(true);
     	Img12.setOnClickListener(new OnClickListener(){
 			@Override
 			public void onClick(View v) {
 				Img12.setClickable(false);
 				Img12.setVisibility(View.INVISIBLE);
 				list6.setPaintFlags(list6.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
 				checkans++;
				if(checkans==6)
			 	{
					score=750-(i*25);
					chlayout();
			 	}
				mark(n);
 			};
 		});
	    }
	//set soal 3
	 public void set3()
	    {
		 final TextView list1 = (TextView)findViewById(R.id.list1); 
		 final TextView list2 = (TextView)findViewById(R.id.list2);
		 final TextView list3 = (TextView)findViewById(R.id.list3);
		 final TextView list4 = (TextView)findViewById(R.id.list4);
		 final TextView list5 = (TextView)findViewById(R.id.list5);
		 final TextView list6 = (TextView)findViewById(R.id.list6);
		 final ImageView Img13 = (ImageView)findViewById(R.id.Item13);
		 final ImageView Img14 = (ImageView)findViewById(R.id.Item14);
		 final ImageView Img15 = (ImageView)findViewById(R.id.Item15);
		 final ImageView Img16 = (ImageView)findViewById(R.id.Item16);
		 final ImageView Img17 = (ImageView)findViewById(R.id.Item17);
		 final ImageView Img18 = (ImageView)findViewById(R.id.Item18);
		 list1.setText("  Briefcase");
		 Img13.setClickable(true);
     	 Img13.setOnClickListener(new OnClickListener(){
 			@Override
 			public void onClick(View v) {
 				Img13.setClickable(false);
 				Img13.setVisibility(View.INVISIBLE);
 				list1.setPaintFlags(list1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
 				checkans++;
				if(checkans==6)
			 	{
					score=750-(i*25);
					chlayout();
			 	}
				mark(n);
 			};
 		});
     	list2.setText("Shipwheel");
     	Img14.setClickable(true);
     	Img14.setOnClickListener(new OnClickListener(){
 			@Override
 			public void onClick(View v) {
 				Img14.setClickable(false);
 				Img14.setVisibility(View.INVISIBLE);
 				list2.setPaintFlags(list2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
 				checkans++;
				if(checkans==6)
			 	{
					score=750-(i*25);
					chlayout();
			 	}
				mark(n);
 			};
 		});
     	list3.setText("Necklace");
     	Img15.setClickable(true);
     	Img15.setOnClickListener(new OnClickListener(){
 			@Override
 			public void onClick(View v) {
 				Img15.setClickable(false);
 				Img15.setVisibility(View.INVISIBLE);
 				list3.setPaintFlags(list3.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
 				checkans++;
				if(checkans==6)
			 	{
					score=750-(i*25);
					chlayout();
			 	}
				mark(n);
 			};
 		});
     	list4.setText("Spider");
     	Img16.setClickable(true);
     	Img16.setOnClickListener(new OnClickListener(){
 			@Override
 			public void onClick(View v) {
 				Img16.setClickable(false);
 				Img16.setVisibility(View.INVISIBLE);
 				list4.setPaintFlags(list4.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
 				checkans++;
				if(checkans==6)
			 	{
					score=750-(i*25);
					chlayout();
			 	}
				mark(n);
 			};
 		});
     	list5.setText("Globe");
     	Img17.setClickable(true);
     	Img17.setOnClickListener(new OnClickListener(){
 			@Override
 			public void onClick(View v) {
 				Img17.setClickable(false);
 				Img17.setVisibility(View.INVISIBLE);
 				list5.setPaintFlags(list5.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
 				checkans++;
				if(checkans==6)
			 	{
					score=750-(i*25);
					chlayout();
			 	}
				mark(n);
 			};
 		});
     	list6.setText("Magnifier");
     	Img18.setClickable(true);
     	Img18.setOnClickListener(new OnClickListener(){
 			@Override
 			public void onClick(View v) {
 				Img18.setClickable(false);
 				Img18.setVisibility(View.INVISIBLE);
 				list6.setPaintFlags(list6.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
 				checkans++;
				if(checkans==6)
			 	{
					score=750-(i*25);
					chlayout();
			 	}
				mark(n);
 			};
 		});	    
	    }
	 //marking for scoring
	 public void mark(int nilai)
	 {
		 s=s+n;
	 }
	 //change layout to display score
	 public void chlayout()
	 {
		 total=score+s;
		 saveGame = getSharedPreferences(prefName, MODE_PRIVATE);
		 SharedPreferences.Editor editor = saveGame.edit();
		 editor.putInt("lastScore", total);
		 editor.commit();
		 if(total>=800){
				saveGame = getSharedPreferences(prefItem, MODE_PRIVATE);
				editor = saveGame.edit();
				editor.putInt("ipa1", 30);
				editor.putInt("ipa2", 40);
				editor.putInt("ips1", 15);
				editor.putInt("ips2", 15);
				editor.commit();
			}
			else{
				saveGame = getSharedPreferences(prefItem, MODE_PRIVATE);
				editor = saveGame.edit();
				editor.putInt("ipa1", 30);
				editor.putInt("ipa2", 10);
				editor.putInt("ips1", 30);
				editor.putInt("ips2", 30);
				editor.commit();
			}
		 setContentView(R.layout.activity_item__finding__score);
		 TextView t=(TextView) findViewById(R.id.textView1);
		 t.setText(Integer.toString(total));
	 }
}
