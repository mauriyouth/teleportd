package teleportd.com.droid;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import teleportd.com.droid.TPortItemList.TPortItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.api.client.extensions.android2.AndroidHttp;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;

public class TeleportdActivity extends MapActivity {
    /** Called when the activity is first created. */
	MapView mapView;
	MapController mc;
	GeoPoint point; //user actual coordinate
	LocationManager lm;
	GridView gridview ;
	String bestProvider;
	String urlString;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mapView = (MapView) findViewById(R.id.mapView);
        mc = mapView.getController();
        lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        point = new GeoPoint(48870000,2340000);
		mc.setCenter(point);
		mc.setZoom(6);
		
		
		
		//GridView gridview=(GridView) findViewById(R.id.gridView);
        mapView.setBuiltInZoomControls(true);

     
 
    }

	@Override
	protected void onStart() {
		super.onStart();
		String longi= ((Integer) mapView.getLongitudeSpan()).toString();
		String lati= ((Integer) mapView.getLatitudeSpan()).toString();
		Log.i("longi",longi);
		Log.i("lati",lati);
	
		new TeleportdAPIParser().execute(); //background task, fetch pictures and show them on the UI
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("destroy", "destroy");
	}




	private class TeleportdAPIParser extends AsyncTask<Void, Object, Void> {
		
		private final HttpTransport http= AndroidHttp.newCompatibleTransport();
		private HttpRequestFactory fact;
		private HttpRequest request; 
		private HttpResponse response; 
		private String urlString;
		private JsonFactory jsonFactory; 
		private JsonParser jp;
		MapView mapView;
		Marker marker;
		List<Overlay> mapOverlays;
		ImageAdapter adapter;
		GridView gridview;
		
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mapView=(MapView) findViewById(R.id.mapView);
			Drawable drawable = getResources().getDrawable(R.drawable.puce);
			marker = new Marker(drawable);
			mapOverlays = mapView.getOverlays();
			adapter=new ImageAdapter();
			gridview=(GridView) findViewById(R.id.gridView);
			adapter= new ImageAdapter();
			
			
		}
		
		
		@Override
		protected  Void doInBackground(Void... params) {
			
			fact=http.createRequestFactory();
			jsonFactory = new JsonFactory();
			TPortItem tpi= new TPortItem();
			TPortItemList tpl=new TPortItemList();
			
	
				urlString="http://v1.api.teleportd.com:8080/search?apikey=1c5a31ccf46cd172e604e103c97239bd&loc=%5B48.87,2.34,10.0,5.0%5D&window=15";
				try {
					//urlString=URLEncoder.encode(urlString,"UTF-8");
					request = fact.buildGetRequest(new GenericUrl(urlString));
					HttpHeaders header=new HttpHeaders();
					header.setAcceptEncoding("gzip");
					request.setHeaders(header);
					response = request.execute();
					jp = jsonFactory.createJsonParser(new BufferedInputStream(response.getContent()));
					int i=0;

					while(jp.nextValue()!=null){
						
						jp.nextValue();
						while(jp.getCurrentToken()!=JsonToken.END_OBJECT && jp.getCurrentToken()!=null){							
							
								if(jp.getCurrentName().equals("sha")){
									tpi.sha=jp.getText();
								}
								
								if(jp.getCurrentName().equals("date")){
									tpi.date=jp.getIntValue();			
								}
								
								if(jp.getCurrentName().equals("age")){
									tpi.age=jp.getIntValue();
								}
								
								if(jp.getCurrentName().equals("thumb")){
									tpi.thumb=jp.getText();
									if(adapter.URLS.size()<9)
										adapter.URLS.add(tpi.thumb);
										
								}
								
								if(jp.getCurrentName().equals("rank")){
									tpi.rank=jp.getIntValue();
								}
								
								if(jp.getCurrentName().equals("grade")){
									tpi.grade=jp.getIntValue();
								}
								
								if(jp.getCurrentName().equals("loc")){
									jp.nextValue().toString();
									tpi.loc[0]=(int) (jp.getFloatValue()*1E6);
									jp.nextValue().toString();
									tpi.loc[1]=(int) (jp.getFloatValue()*1E6);
									jp.nextValue();
									if(i<48)
										publishProgress(new GeoPoint (tpi.loc[0],tpi.loc[1]));
									i++;
									
								}
								
								jp.nextValue();	
								
							}
						
						//Log.i("s",((Integer) i).toString());
						Log.i("s","s");

						//tpl.i.add(new TPortItem(tpi.sha, tpi.loc,tpi.age, tpi.date,tpi.thumb, tpi.rank, tpi.grade));
							
		
					}	
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			return null;
		}

		@Override
		protected void onProgressUpdate(Object... values) {
			super.onProgressUpdate(values);
			marker.addOverlay(new OverlayItem((GeoPoint) values[0], "Hola, Mundo!", "I'm in Mexico City!"));
			mapOverlays.add(marker); // adding the whole overlays (list) on the maps
			
		
		}


		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Log.i("post", "post");
			mapView.invalidate();
			gridview.setAdapter(adapter);
			
		}


		

	

	}
}