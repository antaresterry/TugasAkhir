package com.skripsi.atma;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Summary extends Activity {

	private SharedPreferences saveGame;
	private String prefEngine = "saveEngine";
	private String prefAnatomy = "saveAnatomy";
	private String prefCooking = "saveCooking";
	private String prefItem = "saveItem";
	private String prefDetective = "DetectiveGameResult";
	private String prefTycoon = "tg_result";
	private String prefCard = "CardGameResult";
	private String prefInterest = "InterestTestResult";
	private int[] engine = new int[4];
	private int[] anatomy = new int[4];
	private int[] cooking = new int[4];
	private int[] item = new int[4];
	private int[] card = new int[4];
	private int[] tycoon = new int[4];
	private int[] detective = new int[4];
	private float[] interest = new float[4];
	private float[] games = new float[4];
	private float[] totalGames = new float[4];
	private float ipa1;
	private float ipa2;
	private float ips1;
	private float ips2;
	private boolean edg;
	private boolean ag;
	private boolean cg;
	private boolean ifg;
	private boolean dg;
	private boolean tg;
	private boolean scg;
	private boolean it;
	private int countGame = 0;
	private String[] topCategory = new String[2];
	private float[] topFloat = new float[2];
	public DatabaseHelper myDbHelper = new DatabaseHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summary);
		TextView result = (TextView) findViewById(R.id.result);
		cekSP();
		calculation();

		//if(!it && countGame == 0)
		//	result.setText("anda belum mendapatkan hasil");
		//else
		//	result.setText(String.valueOf(ipa1) + " " + String.valueOf(ipa2) + " " + String.valueOf(ips1) + " " + String.valueOf(ips2));
		
		
		if(!it && countGame == 0)
			result.setText("Rangkuman ini membutuhkan hasil Anda dari permainan dan atau tes minat. \nSilahkan memainkan setiap permainan yang tersedia dan menyelesaikan tes minat untuk mendapatkan rangkuman berisi rekomendasi pemilihan fakultas yang paling sesuai dan paling mendekati dengan keadaan Anda yang sebenarnya.");
		else{
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
			ContentValues updateValues = new ContentValues();
			
			String ipa_1 = "\"ipa1\"", ipa_2 = "\"ipa2\"", ips_1 = "\"ips1\"", ips_2 = "\"ips2\"";
			
	        updateValues.put("nilai", ipa1);
		    DatabaseHelper.updatetable("SUMMARY", updateValues, "kategori=" + ipa_1);

	        updateValues.put("nilai", ipa2);
		    DatabaseHelper.updatetable("SUMMARY", updateValues, "kategori=" + ipa_2);

	        updateValues.put("nilai", ips1);
		    DatabaseHelper.updatetable("SUMMARY", updateValues, "kategori=" + ips_1);
		    
	        updateValues.put("nilai", ips2);
		    DatabaseHelper.updatetable("SUMMARY", updateValues, "kategori=" + ips_2);
	        
			String[] pilihtabel = {"kategori", "nilai"};
			String orderBy = "nilai DESC";
			Cursor c = myDbHelper.selectrecord("SUMMARY", pilihtabel, null, null, null, null, orderBy);
			if(c!=null){
				if(c.moveToFirst()){
					int i = 0;
		    		do{
		    			topCategory[i] = c.getString(c.getColumnIndex("kategori"));
						topFloat[i] = c.getFloat(c.getColumnIndex("nilai"));
						i++;
		    		}
		    		while(c.moveToNext() && i < 2);
				}
			}
			c.close();
			if(topCategory[0].equals("ipa1"))
				result.setText(result.getText().toString().replace("recommend_1", "Fakultas Teknik karena Anda memiliki sifat yang kreatif, penuh dengan ide, mudah memecahkan persoalan yang membutuhkan kemampuan matematis, lebih menyukai praktek daripada teori, serta tidak menyukai pekerjaan yang sifatnya kaku atau terlalu terorganisir."));
			else if(topCategory[0].equals("ipa2"))
				result.setText(result.getText().toString().replace("recommend_1", "Fakultas Kedokteran karena Anda memiliki sifat analitis, kritis, dan observatif yang merupakan karakter penting yang harus dimiliki oleh seorang ahli medis. Kemampuan analisa yang baik juga sangat membantu dalam ilmu biologi, sehingga Anda juga dapat mempertimbangkan untuk memilih Fakultas Teknobiologi."));
			else if(topCategory[0].equals("ips1"))
				result.setText(result.getText().toString().replace("recommend_1", "Fakultas Psikologi, Hukum, atau Keguruan dan Ilmu Pendidikan karena ketiga fakultas ini sangat memerlukan karakter Anda yang sosial, pandai berkomunikasi, mampu menganalisa keadaan sekitar untuk membuat keputusan yang paling bijaksana, serta mudah dalam menyampaikan ide - ide yang menyangkut kepentingan umum."));
			else if(topCategory[0].equals("ips2"))
				result.setText(result.getText().toString().replace("recommend_1", "Fakultas Ekonomi atau Ilmu Administrasi, Bisnis, dan Komunikasi karena Anda adalah seorang yang terorganisir, memiliki leadership yang tinggi, mampu menggunakan sumber daya yang ada pada Anda dengan sebaik - baiknya, serta mampu melihat prospek yang baik dari setiap hal yang Anda lakukan."));
			
			if(topCategory[1].equals("ipa1"))
				result.setText(result.getText().toString().replace("recommend_2", "Fakultas Teknik karena Anda memiliki sifat yang kreatif, penuh dengan ide, mudah memecahkan persoalan yang membutuhkan kemampuan matematis, lebih menyukai praktek daripada teori, serta tidak menyukai pekerjaan yang sifatnya kaku atau terlalu terorganisir."));
			else if(topCategory[1].equals("ipa2"))
				result.setText(result.getText().toString().replace("recommend_2", "Fakultas Kedokteran karena Anda memiliki sifat analitis, kritis, dan observatif yang merupakan karakter penting yang harus dimiliki oleh seorang ahli medis. Kemampuan analisa yang baik juga sangat membantu dalam ilmu biologi, sehingga Anda juga dapat mempertimbangkan untuk memilih Fakultas Teknobiologi."));
			else if(topCategory[1].equals("ips1"))
				result.setText(result.getText().toString().replace("recommend_2", "Fakultas Psikologi, Hukum, atau Keguruan dan Ilmu Pendidikan karena ketiga fakultas ini sangat memerlukan karakter Anda yang sosial, pandai berkomunikasi, mampu menganalisa keadaan sekitar untuk membuat keputusan yang paling bijaksana, serta mudah dalam menyampaikan ide - ide yang menyangkut kepentingan umum."));
			else if(topCategory[1].equals("ips2"))
				result.setText(result.getText().toString().replace("recommend_2", "Fakultas Ekonomi atau Ilmu Administrasi, Bisnis, dan Komunikasi karena Anda adalah seorang yang terorganisir, memiliki leadership yang tinggi, mampu menggunakan sumber daya yang ada pada Anda dengan sebaik - baiknya, serta mampu melihat prospek yang baik dari setiap hal yang Anda lakukan."));
				
			//result.setText(topCategory[0] + " " + String.valueOf(topFloat[0]) + " " + topCategory[1] + " " + String.valueOf(topFloat[1]));
		}

		Button Back = (Button) findViewById(R.id.BackButton);
		Back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
				System.exit(0);
			};
		});
		
	}
	
	public void cekSP()
	{
		//cek engine
		File engines = new File(getApplicationContext().getFilesDir().getParentFile().getPath() + "/shared_prefs/"+ prefEngine +".xml");
		
		if (engines.exists()){
			try{
				saveGame = getSharedPreferences(prefEngine, MODE_PRIVATE);
				engine[0] = saveGame.getInt("ipa1", 0);
				engine[1] = saveGame.getInt("ipa2", 0);
				engine[2] = saveGame.getInt("ips1", 0);
				engine[3] = saveGame.getInt("ips2", 0);
				edg =  true;
				countGame++;
				
			}
			catch(NullPointerException e){
				e.printStackTrace();			
			}
		}
		else{
			edg = false;
		}
		//cek anatomy
		File anatomys = new File(getApplicationContext().getFilesDir().getParentFile().getPath() + "/shared_prefs/"+ prefAnatomy +".xml");
		
		if (anatomys.exists()){
			try{
				saveGame = getSharedPreferences(prefAnatomy, MODE_PRIVATE);
				anatomy[0] = saveGame.getInt("ipa1", 0);
				anatomy[1] = saveGame.getInt("ipa2", 0);
				anatomy[2] = saveGame.getInt("ips1", 0);
				anatomy[3] = saveGame.getInt("ips2", 0);
				ag = true;
				countGame++;
			}
			catch(NullPointerException e){
				e.printStackTrace();			
			}
		}
		else{
			ag = false;
		}
		//cek cooking
		File cookings = new File(getApplicationContext().getFilesDir().getParentFile().getPath() + "/shared_prefs/"+ prefCooking +".xml");
		
		if (cookings.exists()){
			try{
				saveGame = getSharedPreferences(prefCooking, MODE_PRIVATE);
				cooking[0] = saveGame.getInt("ipa1", 0);
				cooking[1] = saveGame.getInt("ipa2", 0);
				cooking[2] = saveGame.getInt("ips1", 0);
				cooking[3] = saveGame.getInt("ips2", 0);
				cg = true;
				countGame++;
			}
			catch(NullPointerException e){
				e.printStackTrace();			
			}
		}
		else{
			cg = false;
		}
		//cek item
		File items = new File(getApplicationContext().getFilesDir().getParentFile().getPath() + "/shared_prefs/"+ prefItem +".xml");
		
		if (items.exists()){
			try{
				saveGame = getSharedPreferences(prefItem, MODE_PRIVATE);
				item[0] = saveGame.getInt("ipa1", 0);
				item[1] = saveGame.getInt("ipa2", 0);
				item[2] = saveGame.getInt("ips1", 0);
				item[3] = saveGame.getInt("ips2", 0);
				ifg = true;
				countGame++;
			}
			catch(NullPointerException e){
				e.printStackTrace();			
			}
		}
		else{
			ifg = false;
		}
		//cek detective
		File detectives = new File(getApplicationContext().getFilesDir().getParentFile().getPath() + "/shared_prefs/"+ prefDetective +".xml");
		
		if (detectives.exists()){
			try{
				saveGame = getSharedPreferences(prefItem, MODE_PRIVATE);
				detective[0] = saveGame.getInt("ipa1", 0);
				detective[1] = saveGame.getInt("ipa2", 0);
				detective[2] = saveGame.getInt("ips1", 0);
				detective[3] = saveGame.getInt("ips2", 0);
				dg = true;
				countGame++;
			}
			catch(NullPointerException e){
				e.printStackTrace();			
			}
		}
		else{
			dg = false;
		}
		//cek card
		File cards = new File(getApplicationContext().getFilesDir().getParentFile().getPath() + "/shared_prefs/"+ prefCard +".xml");
		
		if (cards.exists()){
			try{
				saveGame = getSharedPreferences(prefCard, MODE_PRIVATE);
				card[0] = saveGame.getInt("cg_ipa1", 0);
				card[1] = saveGame.getInt("cg_ipa2", 0);
				card[2] = saveGame.getInt("cg_ips1", 0);
				card[3] = saveGame.getInt("cg_ips2", 0);
				scg = true;
				countGame++;
			}
			catch(NullPointerException e){
				e.printStackTrace();			
			}
		}
		else{
			scg = false;
		}
		//cek tycoon
		File tycoons = new File(getApplicationContext().getFilesDir().getParentFile().getPath() + "/shared_prefs/"+ prefTycoon +".xml");
		
		if (tycoons.exists()){
			try{
				saveGame = getSharedPreferences(prefItem, MODE_PRIVATE);
				tycoon[0] = saveGame.getInt("tg_ipa1", 0);
				tycoon[1] = saveGame.getInt("tg_ipa2", 0);
				tycoon[2] = saveGame.getInt("tg_ips1", 0);
				tycoon[3] = saveGame.getInt("tg_ips2", 0);
				tg = true;
				countGame++;
			}
			catch(NullPointerException e){
				e.printStackTrace();			
			}
		}
		else{
			tg = false;
		}
		//cek interest
		File interests = new File(getApplicationContext().getFilesDir().getParentFile().getPath() + "/shared_prefs/"+ prefInterest +".xml");
		
		if (interests.exists()){
			try{
				saveGame = getSharedPreferences(prefInterest, MODE_PRIVATE);
				interest[0] = saveGame.getFloat("tes_ipa1", 0);
				interest[1] = saveGame.getFloat("tes_ipa2", 0);
				interest[2] = saveGame.getFloat("tes_ips1", 0);
				interest[3] = saveGame.getFloat("tes_ips2", 0);
				it = true;
			}
			catch(NullPointerException e){
				e.printStackTrace();			
			}
		}
		else{
			scg = false;
		}
	}
	
	public void calculation(){		
		if(it){
			if(countGame != 0){
				if(edg){
					totalGames[0] += (float) engine[0];
					totalGames[1] += (float) engine[1];
					totalGames[2] += (float) engine[2];
					totalGames[3] += (float) engine[3];
				}
				if(ag){
					totalGames[0] += (float) anatomy[0];
					totalGames[1] += (float) anatomy[1];
					totalGames[2] += (float) anatomy[2];
					totalGames[3] += (float) anatomy[3];
				}
				if(cg){
					totalGames[0] += (float) cooking[0];
					totalGames[1] += (float) cooking[1];
					totalGames[2] += (float) cooking[2];
					totalGames[3] += (float) cooking[3];
				}
				if(ifg){
					totalGames[0] += (float) item[0];
					totalGames[1] += (float) item[1];
					totalGames[2] += (float) item[2];
					totalGames[3] += (float) item[3];
				}
				if(dg){
					totalGames[0] += (float) detective[0];
					totalGames[1] += (float) detective[1];
					totalGames[2] += (float) detective[2];
					totalGames[3] += (float) detective[3];
				}
				if(tg){
					totalGames[0] += (float) tycoon[0];
					totalGames[1] += (float) tycoon[1];
					totalGames[2] += (float) tycoon[2];
					totalGames[3] += (float) tycoon[3];
				}
				if(scg){
					totalGames[0] += (float) engine[0];
					totalGames[1] += (float) engine[1];
					totalGames[2] += (float) engine[2];
					totalGames[3] += (float) engine[3];
				}
				games[0] = (float) totalGames[0] / countGame;
				games[1] = (float) totalGames[1] / countGame;
				games[2] = (float) totalGames[2] / countGame;
				games[3] = (float) totalGames[3] / countGame;
				ipa1 = (float) (interest[0]*0.65 + games[0]*0.35);
				ipa2 = (float) (interest[1]*0.65 + games[1]*0.35);
				ips1 = (float) (interest[2]*0.65 + games[2]*0.35);
				ips2 = (float) (interest[3]*0.65 + games[3]*0.35);
				TextView result = (TextView) findViewById(R.id.result);
				result.setText("Berdasarkan count_game permainan yang telah Anda selesaikan dan hasil yang telah Anda dapatkan dari tes minat, Anda dapat mempertimbangkan untuk memilih recommend_1 \nSelain itu, Anda juga dapat mempertimbangkan untuk memilih recommend_2".replace("count_game", String.valueOf(countGame)));
			}
			else{
				ipa1 = interest[0];
				ipa2 = interest[1];
				ips1 = interest[2];
				ips2 = interest[3];
				TextView result = (TextView) findViewById(R.id.result);
				result.setText("Berdasarkan hasil yang telah Anda dapatkan dari tes minat, Anda dapat mempertimbangkan untuk memilih recommend_1 \nSelain itu, Anda juga dapat mempertimbangkan untuk memilih recommend_2");
			}
		}
		else{
			if(edg){
				totalGames[0] += (float) engine[0];
				totalGames[1] += (float) engine[1];
				totalGames[2] += (float) engine[2];
				totalGames[3] += (float) engine[3];
			}
			if(ag){
				totalGames[0] += (float) anatomy[0];
				totalGames[1] += (float) anatomy[1];
				totalGames[2] += (float) anatomy[2];
				totalGames[3] += (float) anatomy[3];
			}
			if(cg){
				totalGames[0] += (float) cooking[0];
				totalGames[1] += (float) cooking[1];
				totalGames[2] += (float) cooking[2];
				totalGames[3] += (float) cooking[3];
			}
			if(ifg){
				totalGames[0] += (float) item[0];
				totalGames[1] += (float) item[1];
				totalGames[2] += (float) item[2];
				totalGames[3] += (float) item[3];
			}
			if(dg){
				totalGames[0] += (float) detective[0];
				totalGames[1] += (float) detective[1];
				totalGames[2] += (float) detective[2];
				totalGames[3] += (float) detective[3];
			}
			if(tg){
				totalGames[0] += (float) tycoon[0];
				totalGames[1] += (float) tycoon[1];
				totalGames[2] += (float) tycoon[2];
				totalGames[3] += (float) tycoon[3];
			}
			if(scg){
				totalGames[0] += (float) engine[0];
				totalGames[1] += (float) engine[1];
				totalGames[2] += (float) engine[2];
				totalGames[3] += (float) engine[3];
			}
			if(countGame != 0){
				ipa1 = (float) totalGames[0] / countGame;
				ipa2 = (float) totalGames[1] / countGame;
				ips1 = (float) totalGames[2] / countGame;
				ips2 = (float) totalGames[3] / countGame;
				TextView result = (TextView) findViewById(R.id.result);
				result.setText("Berdasarkan count_game permainan yang telah Anda selesaikan, Anda dapat mempertimbangkan untuk memilih recommend_1 \nSelain itu, Anda juga dapat mempertimbangkan untuk memilih recommend_2".replace("count_game", String.valueOf(countGame)));
			}
		}
	}
}
