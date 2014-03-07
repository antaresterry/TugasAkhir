package com.skripsi.atma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;


public class MediaInformasi extends Activity {

		/*
		//name listView with faculty names
		Resources res = getResources();
		String[] fakList = res.getStringArray(R.array.Nama_Fakultas);  
		ListView lv = new ListView(this);
		lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, fakList));
		setContentView(lv); */

    private static final String KEY1 = "GROUP";
    private static final String KEY2 = "CHILD";
    
    private String[] fakList = {
    		"Fakultas Ekonomi",
    		"Fakultas Ilmu Administrasi Bisnis dan Ilmu Komunikasi",
    		"Fakultas Keguruan dan Ilmu Pendidikan",
    		"Fakultas Teknik",
            "Fakultas Hukum",
            "Fakultas Kedokteran",
            "Fakultas Psikologi",
            "Fakultas Teknobiologi",
    		};
    private String[][] CHILDREN = { 
    		{ "Program Studi Manajemen", "Program Studi Akuntansi", "Program Studi Ekonomi Pembangunan" },
    		{ "Program Studi Ilmu Administrasi Bisnis", "Program Studi Ilmu Komunikasi" , "Program Studi Hospitality" },
    		{ "Program Studi Pendidikan Bahasa Inggris", "Program Studi Ilmu Pendidikan Teologi", "Program Studi Bimbingan dan Konseling", "Program Studi Pendidikan Guru Sekolah Dasar" },
    		{ "Program Studi Teknik Mesin", "Program Studi Teknik Elektro", "Program Studi Teknik Industri" },
    		{ "Program Studi Hukum" },
    		{ "Program Studi Kedokteran Umum" },
    		{ "Program Studi Psikologi" },
    		{ "Program Studi Biologi" },
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_informasi);
        
        Button ToFB = (Button) findViewById(R.id.ToFacebook);
        ToFB.setText("Go To Facebook Page");
        ToFB.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent p = new Intent(MediaInformasi.this, FacebookPage.class);
        		startActivity(p);
			}		
		});

        List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
        List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();

        for (int i = 0; i < fakList.length; i++) {

            Map<String, String> curGroupMap = new HashMap<String, String>();
            groupData.add(curGroupMap);
            curGroupMap.put(KEY1, fakList[i]);
            curGroupMap.put(KEY2, "");

            List<Map<String, String>> children = new ArrayList<Map<String, String>>();
            if (CHILDREN.length > i) {
                for (int j = 0; j < CHILDREN[i].length; j++) {

                    Map<String, String> curChildMap = new HashMap<String, String>();
                    children.add(curChildMap);
                    curChildMap.put(KEY1, CHILDREN[i][j]);
                    //curChildMap.put(KEY2, CHILDREN[i][j]);
                }
            }
            childData.add(children);
        }

        ExpandableListAdapter adapter = new SimpleExpandableListAdapter(this,groupData, R.layout.mediainformasi_expandable,
                new String[] { KEY1, KEY2 }, new int[] { android.R.id.text1,
                        android.R.id.text2 }, childData,
                android.R.layout.simple_expandable_list_item_2, new String[] {
                        KEY1, KEY2 }, new int[] { android.R.id.text1,
                        android.R.id.text2 });

        ExpandableListView listView = (ExpandableListView) findViewById(R.id.ExpandableListView);

        listView.setAdapter(adapter);

        listView.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                    int groupPosition, long id) {

                return false;
            }
        });

        listView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                    int groupPosition, int childPosition, long id) {
            	
            	String selString = CHILDREN[groupPosition][childPosition];
            	
            	 if (selString.equals("Program Studi Manajemen"))
                 {
            		 Intent intent = new Intent(MediaInformasi.this, DeskripsiProdi.class);
                     intent.setData(Uri.parse("ProdiManajemen"));
                     startActivity(intent);
                 }
            	 else if (selString.equals("Program Studi Akuntansi"))
                 {
            		 Intent intent = new Intent(MediaInformasi.this, DeskripsiProdi.class);
                     intent.setData(Uri.parse("ProdiAkuntansi"));
                     startActivity(intent);
                 }
            	 else if (selString.equals("Program Studi Ekonomi Pembangunan"))
                 {
            		 Intent intent = new Intent(MediaInformasi.this, DeskripsiProdi.class);
                     intent.setData(Uri.parse("ProdiEP"));
                     startActivity(intent);
                 }
            	 else if (selString.equals("Program Studi Ilmu Administrasi Bisnis"))
                 {
            		 Intent intent = new Intent(MediaInformasi.this, DeskripsiProdi.class);
                     intent.setData(Uri.parse("ProdiIAB"));
                     startActivity(intent);
                 }
            	 else if (selString.equals("Program Studi Ilmu Komunikasi"))
                 {
            		 Intent intent = new Intent(MediaInformasi.this, DeskripsiProdi.class);
                     intent.setData(Uri.parse("ProdiIlkom"));
                     startActivity(intent);
                 }
            	 else if (selString.equals("Program Studi Hospitality"))
                 {
            		 Intent intent = new Intent(MediaInformasi.this, DeskripsiProdi.class);
                     intent.setData(Uri.parse("ProdiHospitality"));
                     startActivity(intent);
                 }
            	 else if (selString.equals("Program Studi Pendidikan Bahasa Inggris"))
                 {
            		 Intent intent = new Intent(MediaInformasi.this, DeskripsiProdi.class);
                     intent.setData(Uri.parse("ProdiPBInggris"));
                     startActivity(intent);
                 }
            	 else if (selString.equals("Program Studi Ilmu Pendidikan Teologi"))
                 {
            		 Intent intent = new Intent(MediaInformasi.this, DeskripsiProdi.class);
                     intent.setData(Uri.parse("ProdiIPTeologi"));
                     startActivity(intent);
                 }
            	 else if (selString.equals("Program Studi Bimbingan dan Konseling"))
                 {
            		 Intent intent = new Intent(MediaInformasi.this, DeskripsiProdi.class);
                     intent.setData(Uri.parse("ProdiBK"));
                     startActivity(intent);
                 }
            	 else if (selString.equals("Program Studi Pendidikan Guru Sekolah Dasar"))
                 {
            		 Intent intent = new Intent(MediaInformasi.this, DeskripsiProdi.class);
                     intent.setData(Uri.parse("ProdiPGSD"));
                     startActivity(intent);
                 }
            	 else if (selString.equals("Program Studi Teknik Mesin"))
                 {
            		 Intent intent = new Intent(MediaInformasi.this, DeskripsiProdi.class);
                     intent.setData(Uri.parse("ProdiTMesin"));
                     startActivity(intent);
                 }
            	 else if (selString.equals("Program Studi Teknik Elektro"))
                 {
            		 Intent intent = new Intent(MediaInformasi.this, DeskripsiProdi.class);
                     intent.setData(Uri.parse("ProdiTElektro"));
                     startActivity(intent);
                 }
            	 else if (selString.equals("Program Studi Teknik Industri"))
                 {
            		 Intent intent = new Intent(MediaInformasi.this, DeskripsiProdi.class);
                     intent.setData(Uri.parse("ProdiTIndustri"));
                     startActivity(intent);
                 }
            	 else if (selString.equals("Program Studi Hukum"))
                 {
            		 Intent intent = new Intent(MediaInformasi.this, DeskripsiProdi.class);
                     intent.setData(Uri.parse("ProdiHukum"));
                     startActivity(intent);
                 }
            	 else if (selString.equals("Program Studi Kedokteran Umum"))
                 {
            		 Intent intent = new Intent(MediaInformasi.this, DeskripsiProdi.class);
                     intent.setData(Uri.parse("ProdiKedokteran"));
                     startActivity(intent);
                 }
            	 else if (selString.equals("Program Studi Psikologi"))
                 {
            		 Intent intent = new Intent(MediaInformasi.this, DeskripsiProdi.class);
                     intent.setData(Uri.parse("ProdiPsikologi"));
                     startActivity(intent);
                 }
            	 else if (selString.equals("Program Studi Biologi"))
                 {
            		 Intent intent = new Intent(MediaInformasi.this, DeskripsiProdi.class);
                     intent.setData(Uri.parse("ProdiBiologi"));
                     startActivity(intent);
                 }
            	 else 
            	 {
                 	Intent intent = new Intent(MediaInformasi.this, MainActivity.class);
                 	startActivity(intent);
                 }
            	 
                return true;
            }
        });
    }
}

