package teleportd.com.droid.map;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import teleportd.com.droid.image.GridImages;
import teleportd.com.droid.image.ImageAdapter;
import com.google.android.maps.MapView;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

public class GridFetchImage extends AsyncTask<Void, Void, Void> {
	
	private HttpClient client; 
	private HttpGet getRequest;
	private JsonFactory jsonFactory; 
	private JsonParser jp;	
	MapView mapView;
	private GridView gridview;
	ImageAdapter adapter;
	GridImages gridImages;
	
	public static ArrayList<String> shas;
	public static ArrayList<String> thumbs;
	
	protected String generateAPIRequest(){
		StringBuffer request= new StringBuffer("http://v1.api.teleportd.com:8080/search?apikey=1c5a31ccf46cd172e604e103c97239bd&loc=%5B");
		request.append(mapView.getMapCenter().getLatitudeE6()/1E6);
		request.append(",");
		request.append(mapView.getMapCenter().getLongitudeE6()/1E6);
		Log.i("mapView.getMapCenter().getLatitudeE6()", ((Integer) mapView.getMapCenter().getLatitudeE6()).toString());
		request.append(",");
		request.append(mapView.getLongitudeSpan()/1E6);
		request.append(",");
		request.append(mapView.getLatitudeSpan()/1E6);
		request.append("%5D");
		return request.toString(); 
	}
	
	@Override
	protected  Void doInBackground(Void... params) {
		client = new DefaultHttpClient();
		jsonFactory = new JsonFactory();
		
		Log.i("latitudespan,",((Integer)mapView.getLatitudeSpan()).toString());
		Log.i("longitudespan,",((Integer)mapView.getLongitudeSpan()).toString());

		try {
			Log.i("sss",generateAPIRequest());
			getRequest=new HttpGet(generateAPIRequest());
			HttpResponse response=client.execute(getRequest);
			getRequest.setHeader("encoding", "gzip");				
			jp = jsonFactory.createJsonParser(new BufferedInputStream(response.getEntity().getContent()));
			String thumb="";
			String sha="";

			while(jp.nextValue()!=null){

				jp.nextValue();
				while(jp.getCurrentToken()!=JsonToken.END_OBJECT && jp.getCurrentToken()!=null){

					if(jp.getCurrentName().equals("sha")){
						adapter.sha.add(jp.getText());
						shas.add(jp.getText());
					}

					if(jp.getCurrentName().equals("thumb")){
						adapter.URLS.add(jp.getText());
						thumbs.add(jp.getText());
					}

					jp.nextValue();	

				}

			}
			jp.close();
			client.getConnectionManager().shutdown();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		return null;
		
	


		
	}
	
	int nextcol=0;
	ArrayList<String> added_shas;
	ArrayList<Integer> added_int;
	ArrayList<String> loading_shas;
	ArrayList<String> loading_thumbs;
	
	ArrayList<String> known_shas;
	ArrayList<Boolean> known_bool;
	
	void onQuery(){
		
		nextcol=0;
		added_shas.clear();
		added_int.clear();
		loading_shas.clear();
		loading_thumbs.clear();
		prefetchOp();
	}
	
	void prefetchOp(){
		Boolean atLeastOne=false;
		
		for(int i=0; i<shas.size();i++){
			String t=thumbs.get(i);
			String sha=shas.get(i);
			if(!added_shas.contains(shas.get(i))){
				
				added_shas.add(sha);
				added_int.add((nextcol++)%4);
				
				loading_shas.add(sha);
				loading_thumbs.add(t);
				
				if(!known_shas.contains(sha)){
					known_shas.add(sha);
					known_bool.add(true);
					adapter.URLS.add(t);
				}
				else {}
				atLeastOne=true;
				
			}
			if(!atLeastOne){
				animate();
			}
		}
		
	}
	
	void handleImageForUrl(String url){
		//String sha=
	}
	
	void animate(){}
	
//	 (void)prefetchOp
//	 {
//	     NSArray *shas = [[[Factory sharedFactory] central] shas];
//	     NSDictionary *thumbs = [[[Factory sharedFactory] central] thumbs];
//	     bool atLeastOne = NO;
//	     
//	     @synchronized([[Factory sharedFactory] central]) {
//	         for(NSString *sha in shas) {
//	             Thumb *t = [thumbs objectForKey:sha];
//	             if(![added_ objectForKey:sha] && t.thumb) {
//	                 [added_ setObject:[NSNumber numberWithInt:((nextCol_++) % 4)] forKey:sha];
//	                 [loading_ setObject:sha forKey:t.thumb];
//	                 if(![known_ objectForKey:t.sha]) {
//	                     [known_ setObject:@"YES" forKey:t.sha];
//	                     [[ImageLoader defaultImageLoader] prefetchImageForUrl:t.thumb];
//	                 }
//	                 else {
//	                     [self performSelectorOnMainThread:@selector(handleImageForUrl:) withObject:t.thumb waitUntilDone:NO];
//	                 }
//	                 atLeastOne = YES;
//	             }
//	         }
//	     }
//	     if(!atLeastOne)
//	         [self animate];
//	 }

}
