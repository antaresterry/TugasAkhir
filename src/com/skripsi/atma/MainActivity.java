package com.skripsi.atma;

import com.skripsi.atma.chat.ChatMenu;

import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.*;
import android.webkit.CookieSyncManager;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.content.*;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		CookieSyncManager.createInstance(getApplicationContext());
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    
	    GridView gridview = (GridView) findViewById(R.id.gridview);	    
	    gridview.setAdapter(new ImageAdapter(this));

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	if (position == 0)
	        	{
	        		Intent d = new Intent(MainActivity.this, MediaInformasi.class);
	        		startActivity(d);
	        	}
	        	else if (position == 1){
	        		Intent p = new Intent(MainActivity.this, Permainan.class);
	        		startActivity(p);
	        	}
	        	else if(position == 2){
        			Intent t = new Intent(MainActivity.this, ChatMenu.class);
        			startActivity(t);
        		}
	        	else if (position == 3){
		        	Intent m = new Intent(MainActivity.this, TesMinat.class);
		        	startActivity(m);
	        	}
	        	else if (position == 4){
		        	Intent m = new Intent(MainActivity.this, Summary.class);
		        	startActivity(m);
	        	}
	        	else if (position == 5){
		        	Intent m = new Intent(MainActivity.this, About.class);
		        	startActivity(m);
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
	        } 
	        else {
	            imageView = (ImageView) convertView;
	        }

	        imageView.setImageResource(mThumbIds[position]);
	        return imageView;
	    }

	    // references to our images
	    private Integer[] mThumbIds = {
	    		R.drawable.content, R.drawable.permainan,
	            R.drawable.chat, R.drawable.tes_minat,
	            R.drawable.summary, R.drawable.about
	    };
	}
	
}
