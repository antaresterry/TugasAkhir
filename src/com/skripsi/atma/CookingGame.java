package com.skripsi.atma;

import com.skripsi.atma.R.drawable;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.SharedPreferences;

public class CookingGame extends Activity {
	//declare all variable
	private SharedPreferences saveGame;
	private String prefName = "saveCooking";
	int nilaiMasakan;
	ProgressBar mProgressBar;
	CountDownTimer mCountDownTimer1;
	CountDownTimer mCountDownTimer2;
	public int i=0;
	public int menu=0;
	public int salt=0;
	public int sugar=0;
	public int chilli=0;
	public int pepper=0;
	public int garlic=0;
	public int garlic_onion=0;
	public int leek=0;
	public int olive_oil=0;
	public int ginger=0;
	public int tamarind=0;
	public int lime=0;
	public int blackpepper=0;
	public int cinamon=0;
	public int nutmeg=0;
	public int score=0;
	public int kaldu=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cooking__game_1);
		
		TextView MenuSop = (TextView)findViewById(R.id.textView2);
		MenuSop.setOnClickListener(new View.OnClickListener() { 
            @Override 
           public void onClick(View v) { 
            	setContentView(R.layout.activity_cooking__game_2);
            	TextView Resep1a = (TextView)findViewById(R.id.textView1);
            	TextView Resep1b = (TextView)findViewById(R.id.textView3);
            	Resep1a.setText(R.string.Resep1a);
            	Resep1b.setText(R.string.Resep1b);
            	menu=1;
            	timer();           	
           } 
    }); 
		
		TextView MenuBola = (TextView)findViewById(R.id.textView3);
		MenuBola.setOnClickListener(new View.OnClickListener() { 
            @Override 
           public void onClick(View v) { 
            	setContentView(R.layout.activity_cooking__game_2);
            	TextView Resep2a = (TextView)findViewById(R.id.textView1);
            	TextView Resep2b = (TextView)findViewById(R.id.textView3);
            	Resep2a.setText(R.string.Resep2a);
            	Resep2b.setText(R.string.Resep2b);
            	menu=2;
            	timer();
           } 
    }); 
		TextView MenuSapi = (TextView)findViewById(R.id.textView4);
		MenuSapi.setOnClickListener(new View.OnClickListener() { 
            @Override 
           public void onClick(View v) { 
            	setContentView(R.layout.activity_cooking__game_2);
            	TextView Resep3a = (TextView)findViewById(R.id.textView1);
            	TextView Resep3b = (TextView)findViewById(R.id.textView3);
            	Resep3a.setText(R.string.Resep3a);
            	Resep3b.setText(R.string.Resep3b);
            	menu=3;
            	timer();
           } 
    }); 
	}
	public void timer()
	{
		i=10;
		mCountDownTimer1=new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
                TextView time = (TextView) findViewById(R.id.textView2);
				time.setText(String.valueOf(i));
                i--;
            }
            @Override
            public void onFinish() {
            //Do what you want 
                Layout3();
            }
        };
        mCountDownTimer1.start();
	}
	public void Layout3()
	{
		//declare all object in layout 3
		i=0;
		setContentView(R.layout.activity_cooking__game_3);
		final ImageView img1=(ImageView) findViewById(R.id.imageView1);
		final ImageView img2=(ImageView) findViewById(R.id.imageView2);
		final ImageView img3=(ImageView) findViewById(R.id.imageView3);
		final ImageView img4=(ImageView) findViewById(R.id.imageView4);
		final ImageView img5=(ImageView) findViewById(R.id.imageView5);
		final ImageView img6=(ImageView) findViewById(R.id.imageView6);
		final ImageView img7=(ImageView) findViewById(R.id.imageView7);
		final ImageView img8=(ImageView) findViewById(R.id.imageView8);
		final ImageView img9=(ImageView) findViewById(R.id.imageView9);
		final ImageView img10=(ImageView) findViewById(R.id.imageView10);
		final ImageView img11=(ImageView) findViewById(R.id.imageView11);
		final ImageView img12=(ImageView) findViewById(R.id.imageView12);
		final ImageView img13=(ImageView) findViewById(R.id.imageView13);
		ImageView img14=(ImageView) findViewById(R.id.imageView14);
		ImageView img15=(ImageView) findViewById(R.id.imageView15);
		ImageView img16=(ImageView) findViewById(R.id.imageView16);
		ImageView img17=(ImageView) findViewById(R.id.imageView17);
		ImageView img18=(ImageView) findViewById(R.id.imageView18);
		ImageView img19=(ImageView) findViewById(R.id.imageView19);
		ImageView img20=(ImageView) findViewById(R.id.imageView20);
		
		if(menu==1)
		{
			img1.setImageResource(drawable.chicken);
			img2.setImageResource(drawable.potato);
			img3.setImageResource(drawable.carrots_icon);
			img4.setImageResource(drawable.snaps);
			img5.setImageResource(drawable.mushroom);
			img6.setImageResource(drawable.tomato);
			img7.setImageResource(drawable.egg);
			img8.setImageResource(drawable.corn);
			img9.setImageResource(drawable.celery);
			img10.setImageResource(drawable.cabbage);
			img11.setImageResource(drawable.ginger);
			img12.setImageResource(drawable.flour_icon);
			img13.setImageResource(drawable.butter);
			img14.setImageResource(drawable.black_pepper);
			img15.setImageResource(drawable.salt);
			img16.setImageResource(drawable.nutmeg);
			img17.setImageResource(drawable.garlic_icon);
			img18.setImageResource(drawable.garlic_onion);
			img19.setImageResource(drawable.kaldu);
			img20.setImageResource(drawable.leek);			
		}
		else if(menu==2)
		{
			img1.setImageResource(drawable.meat);
			img2.setImageResource(drawable.toast);
			img3.setImageResource(drawable.flour_icon);
			img4.setImageResource(drawable.egg);
			img5.setImageResource(drawable.cabbage);
			img6.setImageResource(drawable.celery);
			img7.setImageResource(drawable.olive_oil);
			img8.setImageResource(drawable.butter);
			img9.setImageResource(drawable.ginger);
			img10.setImageResource(drawable.celery);
			img11.setImageResource(drawable.corn);
			img12.setImageResource(drawable.chicken);
			img13.setImageResource(drawable.honey);
			img14.setImageResource(drawable.garlic_icon);
			img15.setImageResource(drawable.garlic_onion);
			img16.setImageResource(drawable.olive_oil);
			img17.setImageResource(drawable.chili_pepper);
			img18.setImageResource(drawable.lime);
			img19.setImageResource(drawable.black_pepper);
			img20.setImageResource(drawable.salt);			
		}
		else if(menu==3)
		{
			img1.setImageResource(drawable.meat);
			img2.setImageResource(drawable.honey);
			img3.setImageResource(drawable.butter);
			img4.setImageResource(drawable.soy_sauce);
			img5.setImageResource(drawable.barbeku_sauce);
			img6.setImageResource(drawable.tomato_sauce);
			img7.setImageResource(drawable.chilli_sauce);
			img8.setImageResource(drawable.chicken);
			img9.setImageResource(drawable.cabbage);
			img10.setImageResource(drawable.celery);
			img11.setImageResource(drawable.carrots_icon);
			img12.setImageResource(drawable.corn);
			img13.setImageResource(drawable.egg);
			img14.setImageResource(drawable.cinnamon);
			img15.setImageResource(drawable.ginger);
			img16.setImageResource(drawable.garlic_icon);
			img17.setImageResource(drawable.tamarind);
			img18.setImageResource(drawable.salt);
			img19.setImageResource(drawable.black_pepper);
			img20.setImageResource(drawable.sugar);
		}
		//countdown timer and progress bar
		mProgressBar=(ProgressBar)findViewById(R.id.progressBar1);     
        mProgressBar.setProgress(i);
           mCountDownTimer2=new CountDownTimer(30000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    Log.v("Log_tag", "Tick of Progress"+ i+ millisUntilFinished);
                    i++;
                    mProgressBar.setProgress(i);
                }

                @Override
                public void onFinish() {
                //Do what you want 
                    i++;
                    mProgressBar.setProgress(i);
                    Scoring();
                 }
            };
           mCountDownTimer2.start();
         //declare onClickListener for all object
   		img1.setOnClickListener(new View.OnClickListener() { 
               @Override 
              public void onClick(View v) { 
            	img1.setColorFilter(R.color.TransBlack);   
               	img1.setClickable(false);              	
              } 
   		 }); 
   		img2.setOnClickListener(new View.OnClickListener() { 
               @Override 
              public void onClick(View v) { 
            	img2.setColorFilter(R.color.TransBlack);	   
               	img2.setClickable(false);
              } 
   		 }); 
   		img3.setOnClickListener(new View.OnClickListener() { 
               @Override 
              public void onClick(View v) { 
            	img3.setColorFilter(R.color.TransBlack);   
               	img3.setClickable(false);
              } 
   		 }); 
   		img4.setOnClickListener(new View.OnClickListener() { 
               @Override 
              public void onClick(View v) { 
            	img4.setColorFilter(R.color.TransBlack);   
               	img4.setClickable(false);
              } 
   		 }); 
   		img5.setOnClickListener(new View.OnClickListener() { 
               @Override 
              public void onClick(View v) { 
                img5.setColorFilter(R.color.TransBlack);   
               	img5.setClickable(false);
              } 
   		 }); 
   		img6.setOnClickListener(new View.OnClickListener() { 
               @Override 
              public void onClick(View v) { 
            	img6.setColorFilter(R.color.TransBlack);	   
                img6.setClickable(false);
              } 
   		 }); 
   		img7.setOnClickListener(new View.OnClickListener() { 
               @Override 
              public void onClick(View v) { 
            	img7.setColorFilter(R.color.TransBlack);	   
                img7.setClickable(false);
              } 
   		 }); 
   		img8.setOnClickListener(new View.OnClickListener() { 
               @Override 
              public void onClick(View v) { 
            	img8.setColorFilter(R.color.TransBlack);   
                img8.setClickable(false);
              } 
   		 }); 
   		img9.setOnClickListener(new View.OnClickListener() { 
               @Override 
              public void onClick(View v) { 
            	img9.setColorFilter(R.color.TransBlack);
                img9.setClickable(false);
              } 
   		 }); 
   		img10.setOnClickListener(new View.OnClickListener() { 
               @Override 
              public void onClick(View v) { 
            	img10.setColorFilter(R.color.TransBlack);   
                img10.setClickable(false);
              } 
   		 });
   		img11.setOnClickListener(new View.OnClickListener() { 

               @Override 
              public void onClick(View v) { 
            	img11.setColorFilter(R.color.TransBlack);	   
                img11.setClickable(false);
              } 
   		 }); 
   		img12.setOnClickListener(new View.OnClickListener() { 
               @Override 
              public void onClick(View v) { 
            	img12.setColorFilter(R.color.TransBlack);
                img12.setClickable(false);
              } 
   		 }); 
   		img13.setOnClickListener(new View.OnClickListener() { 
               @Override 
              public void onClick(View v) { 
            	   if(menu==1)
            	   {
            		   Toast.makeText(getApplicationContext(), "Butter +1", Toast.LENGTH_SHORT).show(); 
            	   }
            	   else if(menu==2)
            	   {
            		   Toast.makeText(getApplicationContext(), "Honey +1", Toast.LENGTH_SHORT).show(); 
            	   }
            	   else
            	   {
            		   Toast.makeText(getApplicationContext(), "Egg +1", Toast.LENGTH_SHORT).show(); 
            	   }
              } 
   		 }); 
   		img14.setOnClickListener(new View.OnClickListener() { 
               @Override 
              public void onClick(View v) { 
            	   if(menu==1)
            	   {
            		   pepper=pepper+1;
            		   Toast.makeText(getApplicationContext(), "Pepper +1", Toast.LENGTH_SHORT).show(); 
            	   }
            	   else if(menu==2)
            	   {
            		   garlic=garlic+1;
            		   Toast.makeText(getApplicationContext(), "Garlic +1", Toast.LENGTH_SHORT).show(); 
            	   }
            	   else
            	   {
            		   cinamon=cinamon+1;
            		   Toast.makeText(getApplicationContext(), "Cinamon +1", Toast.LENGTH_SHORT).show(); 
            	   }
              } 
   		 }); 
   		img15.setOnClickListener(new View.OnClickListener() { 
               @Override 
              public void onClick(View v) { 
            	   if(menu==1)
            	   {
            		   salt=salt+1;
            		   Toast.makeText(getApplicationContext(), "Salt +1", Toast.LENGTH_SHORT).show(); 
            	   }
            	   else if(menu==2)
            	   {
            		   garlic_onion=garlic_onion+1;
            		   Toast.makeText(getApplicationContext(), "Garlic Onion +1", Toast.LENGTH_SHORT).show(); 
            	   }
            	   else
            	   {
            		   ginger=ginger+1;
            		   Toast.makeText(getApplicationContext(), "Ginger +1", Toast.LENGTH_SHORT).show(); 
            	   }
              } 
   		 }); 
   		img16.setOnClickListener(new View.OnClickListener() { 
               @Override 
              public void onClick(View v) { 
            	   if(menu==1)
            	   {
            		   nutmeg=nutmeg+1;
            		   Toast.makeText(getApplicationContext(), "Nutmeg +1", Toast.LENGTH_SHORT).show(); 
            	   }
            	   else if(menu==2)
            	   {
            		   olive_oil=olive_oil+1;
            		   Toast.makeText(getApplicationContext(), "Olive oil +1", Toast.LENGTH_SHORT).show(); 
            	   }
            	   else
            	   {
            		   garlic=garlic+1;
            		   Toast.makeText(getApplicationContext(), "Garlic +1", Toast.LENGTH_SHORT).show(); 
            	   }
              } 
   		 }); 
   		img17.setOnClickListener(new View.OnClickListener() { 
               @Override 
              public void onClick(View v) { 
            	   if(menu==1)
            	   {
            		   garlic=garlic+1;
            		   Toast.makeText(getApplicationContext(), "Garlic +1", Toast.LENGTH_SHORT).show(); 
            	   }
            	   else if(menu==2)
            	   {
            		   chilli=chilli+1;
            		   Toast.makeText(getApplicationContext(), "Chilli +1", Toast.LENGTH_SHORT).show(); 
            	   }
            	   else
            	   {
            		   tamarind=tamarind+1;
            		   Toast.makeText(getApplicationContext(), "Tamarind +1", Toast.LENGTH_SHORT).show(); 
            	   }
              } 
   		 }); 
   		img18.setOnClickListener(new View.OnClickListener() { 
               @Override 
              public void onClick(View v) { 
            	   if(menu==1)
            	   {
            		   garlic_onion=garlic_onion+1;
            		   Toast.makeText(getApplicationContext(), "Garlic onion +1", Toast.LENGTH_SHORT).show(); 
            	   }
            	   else if(menu==2)
            	   {
            		   lime=lime+1;
            		   Toast.makeText(getApplicationContext(), "Lime +1", Toast.LENGTH_SHORT).show(); 
            	   }
            	   else
            	   {
            		   salt=salt+1;
            		   Toast.makeText(getApplicationContext(), "Salt +1", Toast.LENGTH_SHORT).show(); 
            	   }
              } 
   		 }); 
   		img19.setOnClickListener(new View.OnClickListener() { 
               @Override 
              public void onClick(View v) { 
            	   if(menu==1)
            	   {
            		   kaldu=kaldu+1;
            		   Toast.makeText(getApplicationContext(), "Kaldu +1", Toast.LENGTH_SHORT).show(); 
            	   }
            	   else if(menu==2)
            	   {
            		   pepper=pepper+1;
            		   Toast.makeText(getApplicationContext(), "Pepper +1", Toast.LENGTH_SHORT).show(); 
            	   }
            	   else
            	   {
            		   blackpepper=blackpepper+1;
            		   Toast.makeText(getApplicationContext(), "Blackpepper +1", Toast.LENGTH_SHORT).show(); 
            	   }
              } 
   		 }); 
   		img20.setOnClickListener(new View.OnClickListener() { 
               @Override 
              public void onClick(View v) { 
            	   if(menu==1)
            	   {
            		   leek=leek+1;
            		   Toast.makeText(getApplicationContext(), "Leek +1", Toast.LENGTH_SHORT).show(); 
            	   }
            	   else if(menu==2)
            	   {
            		   salt=salt+1;
            		   Toast.makeText(getApplicationContext(), "Salt +1", Toast.LENGTH_SHORT).show(); 
            	   }
            	   else
            	   {
            		   sugar=sugar+1;
            		   Toast.makeText(getApplicationContext(), "Sugar +1", Toast.LENGTH_SHORT).show(); 
            	   }
              } 
   		 });
	}
	
	public void Scoring()
	{
		if(menu==1)
		{
			if(pepper==1)
				score=score+10;
			if(salt==1)
				score=score+10;
			if(nutmeg==1)
				score=score+10;
			if(garlic==2)
				score=score+10;
			if(garlic_onion==1)
				score=score+10;
			if(kaldu==2)
				score=score+10;
			if(leek==1)
				score=score+10;
		}
		else if (menu==2)
		{
			if(garlic==2)
				score=score+10;
			if(garlic_onion==1)
				score=score+10;
			if(olive_oil==5)
				score=score+10;
			if(chilli==5)
				score=score+10;
			if(lime==1)
				score=score+10;
			if(pepper==3)
				score=score+10;
			if(salt==2)
				score=score+10;
		}
		else if(menu==3)
		{
			if(cinamon==1)
				score=score+10;
			if(ginger==1)
				score=score+10;
			if(garlic==3)
				score=score+10;
			if(tamarind==1)
				score=score+10;
			if(salt==2)
				score=score+10;
			if(blackpepper==2)
				score=score+10;
			if(sugar==2)
				score=score+10;
		}
		Result();
	}
	
	public void Result()
	{
		setContentView(R.layout.activity_cooking__game_4);
		TextView Score = (TextView)findViewById(R.id.textView2);
		TextView Masakan = (TextView)findViewById(R.id.textView3);
		ImageView img=(ImageView)findViewById (R.id.ImageViewT);
			if(score<=30)
			{
				if(menu==1)
				img.setImageResource(drawable.sopayam);
				else if(menu==2)
				img.setImageResource(drawable.boladaging);
				else
				img.setImageResource(drawable.igabakar);
				
				img.setColorFilter(R.color.TransBlack);
				Masakan.setText("Your food is undercooked.");
				saveGame = getSharedPreferences(prefName, MODE_PRIVATE);
				SharedPreferences.Editor editor = saveGame.edit();
				editor.putInt("ipa1", 35);
				editor.putInt("ipa2", 15);
				editor.putInt("ips1", 15);
				editor.putInt("ips2", 35);
				editor.commit();
			}
			else if(score>=31 && score <=60 )
			{
				if(menu==1)
					img.setImageResource(drawable.sopayam);
					else if(menu==2)
					img.setImageResource(drawable.boladaging);
					else
					img.setImageResource(drawable.igabakar);
				Masakan.setText("Your food is welldone.");
				saveGame = getSharedPreferences(prefName, MODE_PRIVATE);
				SharedPreferences.Editor editor = saveGame.edit();
				editor.putInt("ipa1", 35);
				editor.putInt("ipa2", 15);
				editor.putInt("ips1", 15);
				editor.putInt("ips2", 35);
				editor.commit();
			}
			else
			{
				if(menu==1)
					img.setImageResource(drawable.sopayam);
					else if(menu==2)
					img.setImageResource(drawable.boladaging);
					else
					img.setImageResource(drawable.igabakar);
				Masakan.setText("Your food is perfect.");
				saveGame = getSharedPreferences(prefName, MODE_PRIVATE);
				SharedPreferences.Editor editor = saveGame.edit();
				editor.putInt("ipa1", 15);
				editor.putInt("ipa2", 35);
				editor.putInt("ips1", 35);
				editor.putInt("ips2", 15);
				editor.commit();
			}
			Score.setText("Your Score is " + String.valueOf(score) + " / 70");
		//save to shredpref
		img.setOnClickListener(new View.OnClickListener() { 
            @Override 
           public void onClick(View v) { 
            	finish();
           } 
		 });
	}
}
