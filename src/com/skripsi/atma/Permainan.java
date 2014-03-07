package com.skripsi.atma;

import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.content.*;

public class Permainan extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_permainan);

	    getIntent();
	    
	    GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setAdapter(new ImageAdapter(this));

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	if (position == 0){
	        		Intent p = new Intent(Permainan.this, ItemFindingHome.class);
	        		startActivity(p);
	        	}
		        else
			    if (position == 1){
				    Intent p = new Intent(Permainan.this, EngineHome.class);
				    startActivity(p);
				}
			    else
			    if (position == 2){
			    	Intent p = new Intent (Permainan.this, CookingGameHome.class);
			    	startActivity(p);
			    }
			    else
				if (position == 3){
				    Intent p = new Intent (Permainan.this, AnatomyHome.class);
				    startActivity(p);
				}
				else
				if (position == 4){
					Intent p = new Intent (Permainan.this, HangmanHome.class);
					startActivity(p);
				}
				else
				if (position == 5){
					Intent p = new Intent (Permainan.this, DetectiveGameMenu.class);
					startActivity(p);
				}
				else
				if (position == 6){
					Intent p = new Intent (Permainan.this, TycoonGameMenu.class);
					startActivity(p);
							    }
				else
				if (position == 7){
					Intent p = new Intent (Permainan.this, CardGame.class);
					startActivity(p);
				}
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

	        imageView.setImageResource(mThumbIds[position]);
	        return imageView;
	    }

	    // references to our images
	    private Integer[] mThumbIds = {
	    		R.drawable.inspecting, R.drawable.engine_ico,
	            R.drawable.getcooking_ico, R.drawable.anatomy_ico,
	            R.drawable.hangman_logo, R.drawable.dgicon,
	            R.drawable.tg_icon, R.drawable.cg_icon
	    };
	}
	
}
