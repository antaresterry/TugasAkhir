package com.skripsi.atma;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.skripsi.atma.R;
import com.skripsi.atma.RSSItem;
import com.skripsi.atma.SimpleRss2Parser;
import com.skripsi.atma.SimpleRss2ParserCallback;

public class FacebookPage extends Activity {
	private Button btnLoad;
	private ListView lvFeedItems;
	
	private SimpleRss2ParserCallback mCallback;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_informasi_facebook);
		
		initViews();
	}
	
	private SimpleRss2ParserCallback getCallback(){
		if(mCallback == null){
			mCallback = new SimpleRss2ParserCallback() {
				
				@Override
				public void onFeedParsed(List<RSSItem> items) {					
					lvFeedItems.setAdapter(
							new MyListAdapter(FacebookPage.this,R.layout.feed_list_item,(ArrayList<RSSItem>) items)
							);
				}
				
				@Override
				public void onError(Exception ex) {
					Toast.makeText(FacebookPage.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
				}
			};
		}
		 return mCallback;
	}

	private void initViews(){
		btnLoad = (Button) findViewById(R.id.btnLoad);
		lvFeedItems = (ListView) findViewById(R.id.lvFeedItems);
		
		btnLoad.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SimpleRss2Parser parser = new SimpleRss2Parser("https://www.facebook.com/feeds/page.php?format=rss20&id=149136258435991", getCallback());
				parser.parseAsync();
			}
		});
	}
	
	private class MyListAdapter extends ArrayAdapter<RSSItem> {

        private ArrayList<RSSItem> items;
        private Context ctx;
        private int layout;

        public MyListAdapter(Context context, int layout, ArrayList<RSSItem> items) {
                super(context, layout, items);
                this.items = items;
                this.ctx = context;
                this.layout = layout;
        }
        
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(layout, null);
                }
                
                RSSItem o = items.get(position);
                if (o != null) {
                	TextView tvPubDate = ((TextView) v.findViewById(R.id.tvPubDate));
                	TextView tvDescription = ((TextView) v.findViewById(R.id.tvDescription));
                	
                	if (tvPubDate != null) {
                		tvPubDate.setText(o.getDate());
                    }
                	
                	if (tvDescription != null) {
                		String desc = o.getDescription();
                		desc = Pattern.compile("<br/><a [^>]*><img [^>]*></a><br/><a href=\"http://www.facebook.com/l.php\\?u=[^>]*>[^<>]*</a><br/>[^<>]*<br/>[^<>]*").matcher(desc).replaceAll("");
                		desc = Pattern.compile("<br/><a [^>]*><img [^>]*></a>").matcher(desc).replaceAll("");
                		desc = Pattern.compile("<br /> <br />").matcher(desc).replaceAll("<br />");
                    	tvDescription.setText(Html.fromHtml(desc + "<br/><a href=\"" + o.getLink() + "\">Read more...</a><br/>"));
                    	tvDescription.setMovementMethod(LinkMovementMethod.getInstance());
                    }
                }
                return v;
        }
    }  
}