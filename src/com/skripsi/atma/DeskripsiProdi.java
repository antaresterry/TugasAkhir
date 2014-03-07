package com.skripsi.atma;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.Menu;
import android.widget.TextView;

public class DeskripsiProdi extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deskripsi_prodi);
		
		Intent intent = getIntent();
		if (intent.getData() != null) {
			if(intent.getDataString().compareTo("ProdiManajemen") == 0){
				ProdiManajemen();
			}
			else if(intent.getDataString().compareTo("ProdiAkuntansi") == 0){
				ProdiAkuntansi();
			}
			else if(intent.getDataString().compareTo("ProdiEP") == 0){
				ProdiEP();
			}
			else if(intent.getDataString().compareTo("ProdiIAB") == 0){
				ProdiIAB();
			}
			else if(intent.getDataString().compareTo("ProdiIlkom") == 0){
				ProdiIlkom();
			}
			else if(intent.getDataString().compareTo("ProdiHospitality") == 0){
				ProdiHospitality();
			}
			else if(intent.getDataString().compareTo("ProdiPBInggris") == 0){
				ProdiPBInggris();
			}
			else if(intent.getDataString().compareTo("ProdiIPTeologi") == 0){
				ProdiIPTeologi();
			}
			else if(intent.getDataString().compareTo("ProdiBK") == 0){
				ProdiBK();
			}
			else if(intent.getDataString().compareTo("ProdiPGSD") == 0){
				ProdiPGSD();
			}
			else if(intent.getDataString().compareTo("ProdiTMesin") == 0){
				ProdiTMesin();
			}
			else if(intent.getDataString().compareTo("ProdiTElektro") == 0){
				ProdiTElektro();
			}
			else if(intent.getDataString().compareTo("ProdiTIndustri") == 0){
				ProdiTIndustri();
			}
			else if(intent.getDataString().compareTo("ProdiHukum") == 0){
				ProdiHukum();
			}
			else if(intent.getDataString().compareTo("ProdiKedokteran") == 0){
				ProdiKedokteran();
			}
			else if(intent.getDataString().compareTo("ProdiPsikologi") == 0){
				ProdiPsikologi();
			}
			else if(intent.getDataString().compareTo("ProdiBiologi") == 0){
				ProdiBiologi();
			}
		}
	}
	
	public void ProdiManajemen(){
		TextView name = (TextView)findViewById(R.id.NamaProdi);
		name.setText(getString(R.string.Prodi1));
		
		TextView desc = (TextView)findViewById(R.id.DeskripsiProdi);
		desc.setText(Html.fromHtml(getString(R.string.Prodi1_deskripsi)));
		
		TextView pem = (TextView)findViewById(R.id.PeminatanProdi);
		pem.setText(Html.fromHtml(getString(R.string.Prodi1_peminatan)));
		
		TextView pros = (TextView)findViewById(R.id.ProspekProdi);
		pros.setText(getString(R.string.Prodi1_prospek));
	}
	
	public void ProdiAkuntansi(){
		TextView name = (TextView)findViewById(R.id.NamaProdi);
		name.setText(getString(R.string.Prodi2));
		
		TextView desc = (TextView)findViewById(R.id.DeskripsiProdi);
		desc.setText(Html.fromHtml(getString(R.string.Prodi2_deskripsi)));
		
		TextView pem = (TextView)findViewById(R.id.PeminatanProdi);
		pem.setText(Html.fromHtml(getString(R.string.Prodi2_peminatan)));
		
		TextView pros = (TextView)findViewById(R.id.ProspekProdi);
		pros.setText(getString(R.string.Prodi2_prospek));
	}
	
	public void ProdiEP(){
		TextView name = (TextView)findViewById(R.id.NamaProdi);
		name.setText(getString(R.string.Prodi3));
		
		TextView desc = (TextView)findViewById(R.id.DeskripsiProdi);
		desc.setText(Html.fromHtml(getString(R.string.Prodi3_deskripsi)));
		
		TextView pem = (TextView)findViewById(R.id.PeminatanProdi);
		pem.setText(Html.fromHtml(getString(R.string.Prodi3_peminatan)));
		
		TextView pros = (TextView)findViewById(R.id.ProspekProdi);
		pros.setText(getString(R.string.Prodi3_prospek));
	}
	
	public void ProdiIAB(){
		TextView name = (TextView)findViewById(R.id.NamaProdi);
		name.setText(getString(R.string.Prodi4));
		
		TextView desc = (TextView)findViewById(R.id.DeskripsiProdi);
		desc.setText(Html.fromHtml(getString(R.string.Prodi4_deskripsi)));
		
		TextView pem = (TextView)findViewById(R.id.PeminatanProdi);
		pem.setText(Html.fromHtml(getString(R.string.Prodi4_peminatan)));
		
		TextView pros = (TextView)findViewById(R.id.ProspekProdi);
		pros.setText(getString(R.string.Prodi4_prospek));
	}
	
	public void ProdiIlkom(){
		TextView name = (TextView)findViewById(R.id.NamaProdi);
		name.setText(getString(R.string.Prodi5));
		
		TextView desc = (TextView)findViewById(R.id.DeskripsiProdi);
		desc.setText(Html.fromHtml(getString(R.string.Prodi5_deskripsi)));
		
		TextView pem = (TextView)findViewById(R.id.PeminatanProdi);
		pem.setText(Html.fromHtml(getString(R.string.Prodi5_peminatan)));
		
		TextView pros = (TextView)findViewById(R.id.ProspekProdi);
		pros.setText(getString(R.string.Prodi5_prospek));
	}
	
	public void ProdiHospitality(){
		TextView name = (TextView)findViewById(R.id.NamaProdi);
		name.setText(getString(R.string.Prodi6));
		
		TextView desc = (TextView)findViewById(R.id.DeskripsiProdi);
		desc.setText(Html.fromHtml(getString(R.string.Prodi6_deskripsi)));
		
		TextView pem = (TextView)findViewById(R.id.PeminatanProdi);
		pem.setText(Html.fromHtml(getString(R.string.Prodi6_peminatan)));
		
		TextView pros = (TextView)findViewById(R.id.ProspekProdi);
		pros.setText(getString(R.string.Prodi6_prospek));
	}
	
	public void ProdiPBInggris(){
		TextView name = (TextView)findViewById(R.id.NamaProdi);
		name.setText(getString(R.string.Prodi7));
		
		TextView desc = (TextView)findViewById(R.id.DeskripsiProdi);
		desc.setText(Html.fromHtml(getString(R.string.Prodi7_deskripsi)));
		
		TextView pem = (TextView)findViewById(R.id.PeminatanProdi);
		pem.setText(Html.fromHtml(getString(R.string.Prodi7_peminatan)));
		
		TextView pros = (TextView)findViewById(R.id.ProspekProdi);
		pros.setText(getString(R.string.Prodi7_prospek));
	}
	
	public void ProdiIPTeologi(){
		TextView name = (TextView)findViewById(R.id.NamaProdi);
		name.setText(getString(R.string.Prodi8));
		
		TextView desc = (TextView)findViewById(R.id.DeskripsiProdi);
		desc.setText(Html.fromHtml(getString(R.string.Prodi8_deskripsi)));
		
		TextView pem = (TextView)findViewById(R.id.PeminatanProdi);
		pem.setText(Html.fromHtml(getString(R.string.Prodi8_peminatan)));
		
		TextView pros = (TextView)findViewById(R.id.ProspekProdi);
		pros.setText(getString(R.string.Prodi8_prospek));
	}
	
	public void ProdiBK(){
		TextView name = (TextView)findViewById(R.id.NamaProdi);
		name.setText(getString(R.string.Prodi9));
		
		TextView desc = (TextView)findViewById(R.id.DeskripsiProdi);
		desc.setText(Html.fromHtml(getString(R.string.Prodi9_deskripsi)));
		
		TextView pem = (TextView)findViewById(R.id.PeminatanProdi);
		pem.setText(Html.fromHtml(getString(R.string.Prodi9_peminatan)));
		
		TextView pros = (TextView)findViewById(R.id.ProspekProdi);
		pros.setText(Html.fromHtml(getString(R.string.Prodi9_prospek)));
	}
	
	public void ProdiPGSD(){
		TextView name = (TextView)findViewById(R.id.NamaProdi);
		name.setText(getString(R.string.Prodi10));
		
		TextView desc = (TextView)findViewById(R.id.DeskripsiProdi);
		desc.setText(Html.fromHtml(getString(R.string.Prodi10_deskripsi)));
		
		TextView pem = (TextView)findViewById(R.id.PeminatanProdi);
		pem.setText(Html.fromHtml(getString(R.string.Prodi10_peminatan)));
		
		TextView pros = (TextView)findViewById(R.id.ProspekProdi);
		pros.setText(getString(R.string.Prodi10_prospek));
	}
	
	public void ProdiTMesin(){
		TextView name = (TextView)findViewById(R.id.NamaProdi);
		name.setText(getString(R.string.Prodi11));
		
		TextView desc = (TextView)findViewById(R.id.DeskripsiProdi);
		desc.setText(Html.fromHtml(getString(R.string.Prodi11_deskripsi)));
		
		TextView pem = (TextView)findViewById(R.id.PeminatanProdi);
		pem.setText(Html.fromHtml(getString(R.string.Prodi11_peminatan)));
		
		TextView pros = (TextView)findViewById(R.id.ProspekProdi);
		pros.setText(getString(R.string.Prodi11_prospek));
	}
	
	public void ProdiTElektro(){
		TextView name = (TextView)findViewById(R.id.NamaProdi);
		name.setText(getString(R.string.Prodi12));
		
		TextView desc = (TextView)findViewById(R.id.DeskripsiProdi);
		desc.setText(Html.fromHtml(getString(R.string.Prodi12_deskripsi)));
		
		TextView pem = (TextView)findViewById(R.id.PeminatanProdi);
		pem.setText(Html.fromHtml(getString(R.string.Prodi12_peminatan)));
		
		TextView pros = (TextView)findViewById(R.id.ProspekProdi);
		pros.setText(getString(R.string.Prodi12_prospek));
	}
	
	public void ProdiTIndustri(){
		TextView name = (TextView)findViewById(R.id.NamaProdi);
		name.setText(getString(R.string.Prodi13));
		
		TextView desc = (TextView)findViewById(R.id.DeskripsiProdi);
		desc.setText(Html.fromHtml(getString(R.string.Prodi13_deskripsi)));
		
		TextView pem = (TextView)findViewById(R.id.PeminatanProdi);
		pem.setText(Html.fromHtml(getString(R.string.Prodi13_peminatan)));
		
		TextView pros = (TextView)findViewById(R.id.ProspekProdi);
		pros.setText(getString(R.string.Prodi13_prospek));
	}
	
	public void ProdiHukum(){
		TextView name = (TextView)findViewById(R.id.NamaProdi);
		name.setText(getString(R.string.Prodi14));
		
		TextView desc = (TextView)findViewById(R.id.DeskripsiProdi);
		desc.setText(Html.fromHtml(getString(R.string.Prodi14_deskripsi)));
		
		TextView pem = (TextView)findViewById(R.id.PeminatanProdi);
		pem.setText(Html.fromHtml(getString(R.string.Prodi14_peminatan)));
		
		TextView pros = (TextView)findViewById(R.id.ProspekProdi);
		pros.setText(getString(R.string.Prodi14_prospek));
	}
	
	public void ProdiKedokteran(){
		TextView name = (TextView)findViewById(R.id.NamaProdi);
		name.setText(getString(R.string.Prodi15));
		
		TextView desc = (TextView)findViewById(R.id.DeskripsiProdi);
		desc.setText(Html.fromHtml(getString(R.string.Prodi15_deskripsi)));
		
		TextView pem = (TextView)findViewById(R.id.PeminatanProdi);
		pem.setText(Html.fromHtml(getString(R.string.Prodi15_peminatan)));
		
		TextView pros = (TextView)findViewById(R.id.ProspekProdi);
		pros.setText(getString(R.string.Prodi15_prospek));
	}
	
	public void ProdiPsikologi(){
		TextView name = (TextView)findViewById(R.id.NamaProdi);
		name.setText(getString(R.string.Prodi16));
		
		TextView desc = (TextView)findViewById(R.id.DeskripsiProdi);
		desc.setText(Html.fromHtml(getString(R.string.Prodi16_deskripsi)));
		
		TextView pem = (TextView)findViewById(R.id.PeminatanProdi);
		pem.setText(Html.fromHtml(getString(R.string.Prodi16_peminatan)));
		
		TextView pros = (TextView)findViewById(R.id.ProspekProdi);
		pros.setText(getString(R.string.Prodi16_prospek));
	}
	
	public void ProdiBiologi(){
		TextView name = (TextView)findViewById(R.id.NamaProdi);
		name.setText(getString(R.string.Prodi17));
		
		TextView desc = (TextView)findViewById(R.id.DeskripsiProdi);
		desc.setText(Html.fromHtml(getString(R.string.Prodi17_deskripsi)));
		
		TextView pem = (TextView)findViewById(R.id.PeminatanProdi);
		pem.setText(Html.fromHtml(getString(R.string.Prodi17_peminatan)));
		
		TextView pros = (TextView)findViewById(R.id.ProspekProdi);
		pros.setText(Html.fromHtml(getString(R.string.Prodi17_prospek)));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.deskripsi_prodi, menu);
		return true;
	}
}
