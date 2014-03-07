package com.skripsi.atma;

import java.io.File;
import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.content.*;
import android.content.res.Resources;

public class TesMinat extends Activity {

	//Buat objek random
	Random rnd = new Random();
	//Buat variable set sebagai penanda set soal yang digunakan
	public int set, nmr_soal=0;
	public String ipa1, ipa2, ips1, ips2, temp;
	public String[] urutan = new String[4];
	//shared preferences for summary.
	private SharedPreferences testresult;
	private String testprefName = "InterestTestResult";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tes_minat_disclaimer);
		ipa1 = "a"; ipa2 = "b"; ips1 = "c"; ips2 = "d";
		//Menerima Intent dari Main Menu
	    getIntent();
	    
		Button setuju = (Button) findViewById(R.id.button1);
		Button kembali = (Button) findViewById(R.id.button2);
		setuju.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				setsoal();
			};
		});
		kembali.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
				System.exit(0);
			};
		});  	
	}
	
	public void setsoal(){
		setContentView(R.layout.activity_tes_minat);
		//Buat objek textview untuk menampilkan soal dari String Array
		TextView soal = (TextView) findViewById(R.id.textView1);
		Resources res = getResources();
		String[] soal_set_1 = res.getStringArray(R.array.Soal_Set_1);
		
		//Untuk set soal yang sesuai, tampilkan string yang sesuai dari res/values/strings.xml
		soal.setText(soal_set_1[nmr_soal].toString());
				
		//Buat objek gridview untuk jawaban soal
		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(new ImageAdapter(this));
		//Berikan listener jika salah satu item dari gridview diplih
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				nmr_soal++;
				if(nmr_soal < 15){
					if(position == 0)
						ipa1+="a";
					else
						if(position == 1)
							ipa2+="b";
						else
							if(position == 2)
								ips1+="c";
							else
								if(position == 3)
									ips2+="d";
					setsoal();
				}
				else
				{
					urutan[0] = ipa1;
					urutan[1] = ipa2;
					urutan[2] = ips1;
					urutan[3] = ips2;
					for(int i = 1; i <= 4-1; ++i)
					{
						for(int j = i; j < 4; ++j)
							if(urutan[i-1].length() < urutan[j].length()){
								temp = urutan[i-1];
								urutan[i-1] = urutan[j];
								urutan[j] = temp;
							}
						}
					interesttestresult();
					/*
					Toast.makeText(TesMinat.this, "Tertinggi = " + urutan[0].charAt(0), Toast.LENGTH_SHORT).show();
					Toast.makeText(TesMinat.this, "Tertinggi kedua = " + urutan[1].charAt(0), Toast.LENGTH_SHORT).show();
					Toast.makeText(TesMinat.this, "Tertinggi ketiga = " + urutan[2].charAt(0), Toast.LENGTH_SHORT).show();
					Toast.makeText(TesMinat.this, "Terendah = " + urutan[3].charAt(0), Toast.LENGTH_SHORT).show();		
					*/
				}
			}
		});
	}
	
	public void interesttestresult(){
		setContentView(R.layout.activity_tes_minat_disclaimer);
		
		String result1 = "", result2 = "";
		
		TextView resulttitle = (TextView) findViewById(R.id.textView1);
		TextView result = (TextView) findViewById(R.id.textView2);
		Button cont = (Button) findViewById(R.id.button1);
		Button cont1 = (Button) findViewById(R.id.button2);
		
		resulttitle.setText("Hasil Anda");
		if(urutan[0].charAt(0) == 'a')
			result1 = "Dari tes ini, Anda dapat mempertimbangkan untuk memilih Fakultas Teknik karena Anda memiliki sifat yang kreatif, penuh dengan ide, mudah memecahkan persoalan yang membutuhkan kemampuan matematis, lebih menyukai praktek daripada teori, serta tidak menyukai pekerjaan yang sifatnya kaku atau terlalu terorganisir.";
		else if(urutan[0].charAt(0) == 'b')
			result1 = "Dari tes ini, Anda dapat mempertimbangkan untuk memilih Fakultas Kedokteran karena Anda memiliki sifat analitis, kritis, dan observatif yang merupakan karakter penting yang harus dimiliki oleh seorang ahli medis. Kemampuan analisa yang baik juga sangat membantu dalam ilmu biologi, sehingga Anda juga dapat mempertimbangkan untuk memilih Fakultas Teknobiologi.";
		else if(urutan[0].charAt(0) == 'c')
			result1 = "Dari tes ini, Anda dapat mempertimbangkan untuk memilih Fakultas Psikologi, Hukum, atau Keguruan dan Ilmu Pendidikan karena ketiga fakultas ini sangat memerlukan karakter Anda yang sosial, pandai berkomunikasi, mampu menganalisa keadaan sekitar untuk membuat keputusan yang paling bijaksana, serta mudah dalam menyampaikan ide - ide yang menyangkut kepentingan umum.";
		else if(urutan[0].charAt(0) == 'd')
			result1 = "Dari tes ini, Anda dapat mempertimbangkan untuk memilih Fakultas Ekonomi atau Ilmu Administrasi, Bisnis, dan Komunikasi karena Anda adalah seorang yang terorganisir, memiliki leadership yang tinggi, mampu menggunakan sumber daya yang ada pada Anda dengan sebaik - baiknya, serta mampu melihat prospek yang baik dari setiap hal yang Anda lakukan.";
		
		if(urutan[1].charAt(0) == 'a')
			result2 = "Selain itu, Anda juga dapat mempertimbangkan untuk memilih Fakultas Teknik karena Anda memiliki sifat yang kreatif, penuh dengan ide, mudah memecahkan persoalan yang membutuhkan kemampuan matematis, lebih menyukai praktek daripada teori, serta tidak menyukai pekerjaan yang sifatnya kaku atau terlalu terorganisir.";
		else if(urutan[1].charAt(0) == 'b')
			result2 = "Selain itu, Anda juga dapat mempertimbangkan untuk memilih Fakultas Kedokteran karena Anda memiliki sifat analitis, kritis, dan observatif yang merupakan karakter penting yang harus dimiliki oleh seorang ahli medis. Kemampuan analisa yang baik juga sangat membantu dalam ilmu biologi, sehingga Anda juga dapat mempertimbangkan untuk memilih Fakultas Teknobiologi.";
		else if(urutan[1].charAt(0) == 'c')
			result2 = "Selain itu, Anda juga dapat mempertimbangkan untuk memilih Fakultas Psikologi, Hukum, atau Keguruan dan Ilmu Pendidikan karena ketiga fakultas ini sangat memerlukan karakter Anda yang sosial, pandai berkomunikasi, mampu menganalisa keadaan sekitar untuk membuat keputusan yang paling bijaksana, serta mudah dalam menyampaikan ide - ide yang menyangkut kepentingan umum.";
		else if(urutan[1].charAt(0) == 'd')
			result2 = "Selaim itu, Anda juga dapat mempertimbangkan untuk memilih Fakultas Ekonomi atau Ilmu Administrasi, Bisnis, dan Komunikasi karena Anda adalah seorang yang terorganisir, memiliki leadership yang tinggi, mampu menggunakan sumber daya yang ada pada Anda dengan sebaik - baiknya, serta mampu melihat prospek yang baik dari setiap hal yang Anda lakukan.";
		
		
		result.setText(result1 + result2);
		
		File engines = new File(getApplicationContext().getFilesDir().getParentFile().getPath() + "/shared_prefs/"+ testprefName +".xml");
		if (engines.exists() == false){
			testsharedpref();
		}
		
		cont.setText("");
		cont1.setText("Back to Main Menu");
		cont.setVisibility(View.INVISIBLE);
		cont.setClickable(false);
		
		cont1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				finish();
				System.exit(0);
			};
		});  	
		
	}

	public void testsharedpref(){
		testresult = getSharedPreferences(testprefName, MODE_PRIVATE);
		SharedPreferences.Editor editor = testresult.edit();
		editor.putFloat("tes_ipa1", (((float) ipa1.length() / 15) * 100));
		editor.putFloat("tes_ipa2", (((float) ipa2.length() / 15) * 100));
		editor.putFloat("tes_ips1", (((float) ips1.length() / 15) * 100));
		editor.putFloat("tes_ips2", (((float) ips2.length( )/ 15) * 100));
		editor.commit();
	}
	
	//Buat Image Adapter sebagai pengelola gambar dari setiap item pada gridview
	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;

	    public ImageAdapter(Context c) {
	        mContext = c;
	    }

	    public int getCount() {
	    	return set1_1.length;
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        return 0;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView;
	        if (convertView == null) {
	        	imageView = new ImageView(mContext);
	            
	            DisplayMetrics metrics = new DisplayMetrics();
	            getWindowManager().getDefaultDisplay().getMetrics(metrics);
	            switch(metrics.densityDpi){
	                 case DisplayMetrics.DENSITY_LOW:
	                            imageView.setLayoutParams(new GridView.LayoutParams(115, 115));
	                            break;
	                 case DisplayMetrics.DENSITY_MEDIUM:
	                            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
	                            break;
	                 case DisplayMetrics.DENSITY_HIGH:
	                             imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
	                             break;
	            }
	            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
	            imageView.setPadding(2, 2, 2, 2);
	        } else {
	            imageView = (ImageView) convertView;
	        }
	        //Ambil resource sesuai dengan set
	        	if(nmr_soal == 0)
	        		imageView.setImageResource(set1_1[position]);
	        	else
	        	if(nmr_soal == 1)
		        	imageView.setImageResource(set1_2[position]);
	        	else
		        if(nmr_soal == 2)
			       	imageView.setImageResource(set1_3[position]);
		        else
			    if(nmr_soal == 3)
				    imageView.setImageResource(set1_4[position]);
			    else
				if(nmr_soal == 4)
					imageView.setImageResource(set1_5[position]);
				else
				if(nmr_soal == 5)
					imageView.setImageResource(set1_6[position]);
				else
				if(nmr_soal == 6)
					imageView.setImageResource(set1_7[position]);
				else
				if(nmr_soal == 7)
					imageView.setImageResource(set1_8[position]);
				else
				if(nmr_soal == 8)
					imageView.setImageResource(set1_9[position]);
				else
				if(nmr_soal == 9)
					imageView.setImageResource(set1_10[position]);
				else
				if(nmr_soal == 10)
					imageView.setImageResource(set1_11[position]);
				else
				if(nmr_soal == 11)
					imageView.setImageResource(set1_12[position]);
				else
				if(nmr_soal == 12)
					imageView.setImageResource(set1_13[position]);
				else
				if(nmr_soal == 13)
					imageView.setImageResource(set1_14[position]);
				else
				if(nmr_soal == 14)
					imageView.setImageResource(set1_15[position]);
	        	
	        return imageView;
	    }

	    //Referensi gambar untuk setiap set; diambil dari res/drawable-<hdpi><ldpi><mdpi><xhdpi>    
	    private Integer[] set1_1 = {
	    		R.drawable.no1_1, R.drawable.no1_2, R.drawable.no1_3, R.drawable.no1_4
	    };
	    private Integer[] set1_2 = {
	    		R.drawable.no2_1, R.drawable.no2_2, R.drawable.no2_3, R.drawable.no2_4
	    };
	    private Integer[] set1_3 = {
	    		R.drawable.no3_1, R.drawable.no3_2, R.drawable.no3_3, R.drawable.no3_4
	    };
	    private Integer[] set1_4 = {
	    		R.drawable.no4_1, R.drawable.no4_2, R.drawable.no4_3, R.drawable.no4_4
	    };
	    private Integer[] set1_5 = {
	    		R.drawable.no5_1, R.drawable.no5_2, R.drawable.no5_3, R.drawable.no5_4
	    };
	    private Integer[] set1_6 = {
	    		R.drawable.no6_1, R.drawable.no6_2, R.drawable.no6_3, R.drawable.no6_4
	    };
	    private Integer[] set1_7 = {
	    		R.drawable.no7_1, R.drawable.no7_2, R.drawable.no7_3, R.drawable.no7_4
	    };
	    private Integer[] set1_8 = {
	    		R.drawable.no8_1, R.drawable.no8_2, R.drawable.no8_3, R.drawable.no8_4
	    };
	    private Integer[] set1_9 = {
	    		R.drawable.no9_1, R.drawable.no9_2, R.drawable.no9_3, R.drawable.no9_4
	    };
	    private Integer[] set1_10 = {
	    		R.drawable.no10_1, R.drawable.no10_2, R.drawable.no10_3, R.drawable.no10_4
	    };
	    private Integer[] set1_11 = {
	    		R.drawable.no11_1, R.drawable.no11_2, R.drawable.no11_3, R.drawable.no11_4
	    };
	    private Integer[] set1_12 = {
	    		R.drawable.no12_1, R.drawable.no12_2, R.drawable.no12_3, R.drawable.no12_4
	    };
	    private Integer[] set1_13 = {
	    		R.drawable.no13_1, R.drawable.no13_2, R.drawable.no13_3, R.drawable.no13_4
	    };
	    private Integer[] set1_14 = {
	    		R.drawable.no14_1, R.drawable.no14_2, R.drawable.no14_3, R.drawable.no14_4
	    };
	    private Integer[] set1_15 = {
	    		R.drawable.no15_1, R.drawable.no15_2, R.drawable.no15_3, R.drawable.no15_4
	    };
	}	
}
