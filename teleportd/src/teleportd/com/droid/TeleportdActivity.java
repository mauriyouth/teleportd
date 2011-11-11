package teleportd.com.droid;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import teleportd.com.droid.image.ImageAdapter;
import teleportd.com.droid.map.Marker;
import teleportd.com.droid.map.Thumb;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.GridView;


public class TeleportdActivity extends MapActivity {
	/** Called when the activity is first created. */
	MapView mapView;
	MapController mc;
	GeoPoint point; //user actual coordinate
	LocationManager lm;
	GridView gridview ;
	GestureDetector gd;
	OnGestureListener mapGestureListener;
	TeleportdAPIParser backgroundTask;
	private Context con;
	private Handler handler = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mapView = (MapView) findViewById(R.id.mapView);
		mc = mapView.getController();
		point = new GeoPoint(48870000,2340000);
		mc.setCenter(point);
		mc.setZoom(10);
		mapView.setBuiltInZoomControls(true);
		con=getBaseContext();
		
		mapGestureListener = new OnGestureListener() {
			Timer t = new Timer();
			Boolean scheduledTask = false;
			int width = mapView.getWidth();
			int height =mapView.getHeight();
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {
				if ((Math.abs(e1.getRawY() - e2.getRawY())) > (height/2) | (Math.abs(e1.getRawX() - e2.getRawX())) > (width/2)){
					Log.i("true","true");
				}
						
		              
				Log.i("onScroll","onScroll 1");
				Log.i("onScroll",String.format("dsX: %s, dsY: %s",distanceX,distanceY));
				mapView.getWidth();
				mapView.getHeight();
				
//				if(backgroundTask.getStatus()==Status.FINISHED){
//					if(!scheduledTask){
//						t.schedule(new MyTimerTask(), 5000);
//						scheduledTask=true;
//						return false;
//						}
//					
//					t.cancel();
//					t=new Timer();
//					t.schedule(new MyTimerTask(), 5000);
//					scheduledTask=true;
//					}
//				Log.i("onScroll","onScroll 2");
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean onDown(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		


	}

	@Override
	protected void onStart() {
		super.onStart();
		String longi= ((Integer) mapView.getLongitudeSpan()).toString();
		String lati= ((Integer) mapView.getLatitudeSpan()).toString();
		Log.i("longi",longi);
		Log.i("lati",lati);
		backgroundTask=new TeleportdAPIParser(); //background task, fetch pictures and show them on the UI
		backgroundTask.execute();
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



	private class TeleportdAPIParser extends AsyncTask<Void, Object, ArrayList<Thumb>> {
		private HttpClient client; 
	    private HttpGet getRequest;
		private String urlString;
		private JsonFactory jsonFactory; 
		private JsonParser jp;
		private ArrayList<Thumb> thumbs;
		MapView mapView;
		Marker marker;
		List<Overlay> mapOverlays;
		ImageAdapter adapter;
		GridView gridview;


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			client = AndroidHttpClient.newInstance("Android");
			mapView=(MapView) findViewById(R.id.mapView);
			con=getBaseContext();
			Drawable drawable = getResources().getDrawable(R.drawable.pin);
			marker = new Marker(drawable, con, mapGestureListener);
			mapOverlays = mapView.getOverlays();
			adapter=new ImageAdapter();
			gridview=(GridView) findViewById(R.id.gridView);
			adapter= new ImageAdapter();


		}


		@Override
		protected  ArrayList<Thumb> doInBackground(Void... params) {
			thumbs=new ArrayList<Thumb> ();
			jsonFactory = new JsonFactory();

			Log.i("latitudespan,",((Integer)mapView.getLatitudeSpan()).toString());
			Log.i("longitudespan,",((Integer)mapView.getLongitudeSpan()).toString());


			urlString="http://v1.api.teleportd.com:8080/search?apikey=1c5a31ccf46cd172e604e103c97239bd&loc=%5B48.87,2.34,10.0,5.0%5D";
			try {
				//urlString=URLEncoder.encode(urlString,"UTF-8");
				getRequest=new HttpGet(urlString);
				HttpResponse response=client.execute(getRequest);
				getRequest.setHeader("encoding", "gzip");				
				jp = jsonFactory.createJsonParser(new BufferedInputStream(response.getEntity().getContent()));
				String sha="";
				String thumb="";
				String full="";
				int log=0;
				int lat=0;


				while(jp.nextValue()!=null){

					jp.nextValue();
					while(jp.getCurrentToken()!=JsonToken.END_OBJECT && jp.getCurrentToken()!=null){

						if(jp.getCurrentName().equals("sha")){
							//tpi.sha=jp.getText();
							sha=jp.getText();
						}

						if(jp.getCurrentName().equals("date")){
							//tpi.date=jp.getIntValue();			
						}

						if(jp.getCurrentName().equals("age")){
							//tpi.age=jp.getIntValue();
						}

						if(jp.getCurrentName().equals("thumb")){
							//tpi.thumb=jp.getText();
							thumb=jp.getText();
							adapter.URLS.add(jp.getText());

						}

						if(jp.getCurrentName().equals("rank")){
							//tpi.rank=jp.getIntValue();
						}

						if(jp.getCurrentName().equals("grade")){
							//tpi.grade=jp.getIntValue();
						}

						if(jp.getCurrentName().equals("loc")){
							jp.nextValue().toString();
							//tpi.loc[0]=(int) (jp.getFloatValue()*1E6);
							lat=(int) (jp.getFloatValue()*1E6);
							jp.nextValue().toString();
							//tpi.loc[1]=(int) (jp.getFloatValue()*1E6);
							log=(int) (jp.getFloatValue()*1E6);
							jp.nextValue();
							//publishProgress(new GeoPoint (tpi.loc[0],tpi.loc[1]));


						}

						jp.nextValue();	

					}
					thumbs.add(new Thumb(sha,thumb,new GeoPoint(lat,log)));
					thumbs.get(thumbs.size()-1).aggregation=(con.getResources().getDrawable(R.drawable.aggregation));
					thumbs.get(thumbs.size()-1).pin=(con.getResources().getDrawable(R.drawable.pin));
					//Log.i("s",((Integer) i).toString());
					Log.i("s","s");

					//tpl.i.add(new TPortItem(tpi.sha, tpi.loc,tpi.age, tpi.date,tpi.thumb, tpi.rank, tpi.grade));


				}
				jp.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			return consolidateOp();
		}

		protected ArrayList<Thumb> consolidateOp()
		{
			Rect visibleRect= new Rect();
			ArrayList<Thumb> constructor = new ArrayList<Thumb> ();
			mapView.getDrawingRect(visibleRect);
			
			for(Thumb thumb : thumbs){ 
				Point currentDevicePosition = new Point();
				mapView.getProjection().toPixels(thumb.loc,currentDevicePosition);
				if(visibleRect.contains(currentDevicePosition.x, currentDevicePosition.y)){
					constructor.add(thumb);
				}


			}

			ArrayList<Thumb> done= new ArrayList<Thumb>();

			final double delta=0.02;

			while(constructor.size()>0){
				GeoPoint oPt= constructor.get(0).loc;
				Boolean merged=false;
				for(int i=1;i<constructor.size();i++){
					GeoPoint dpt=constructor.get(i).loc;
					double dd=Math.pow(Math.pow(oPt.getLatitudeE6()-dpt.getLatitudeE6(), 2.0f)+Math.pow(oPt.getLongitudeE6()-dpt.getLongitudeE6(),2.0f), 0.5f);
					if(dd /mapView.getLongitudeSpan() < delta){
						constructor.get(0).merged=true;
						constructor.get(0).halo++;
						constructor.remove(i);
						merged=true;
						break;
					}
				}
				if(!merged) {
					done.add(constructor.get(0));
					constructor.remove(0);
				} 
			}
			return done;

		}
		
		

		private void consolidateOpDone(ArrayList<Thumb> done) {
			
			if(mapOverlays.size()==0){
				   marker.setmOverlays(done);
				   mapOverlays.add(marker);
				   marker.poupulateMap();
				
			}
			else{
				
				ArrayList<Thumb> annotations = ((Marker)mapOverlays.get(0)).getmOverlays();
				ArrayList<Thumb> delPoints = new ArrayList<Thumb>();
				
				Boolean remove=false;
				
				for(Thumb pt:annotations) {
					for(Thumb d:done){
						if(d.sha.equals(pt.sha)){
							done.remove(d);
							remove=true;
							}
					}
			        if(remove){ 
			            delPoints.add(pt);
			            remove=false;
			            }
			        
			     
			    }
				
			     for(Thumb pt:delPoints) {
			            annotations.remove(pt);
			        }
			     for(Thumb a:done) {
			    	 annotations.add(a);
			 
			        }
			     
			     marker.setmOverlays(annotations);
			     mapOverlays.add(marker);
			     marker.poupulateMap();
			}
			
		
		}


		@Override
		protected void onPostExecute(ArrayList<Thumb> result) {
			super.onPostExecute(result);
			Log.i("post", "post");
			consolidateOpDone(result);
			mapView.invalidate();
			gridview.setAdapter(adapter);

		}

	}
	
	
    public class MyTimerTask extends TimerTask {
        private Runnable runnable = new Runnable() {
            public void run() {
            	// a task can be executed only once, so we have to instantiate a new on
            	backgroundTask.cancel(true);
            	backgroundTask= new TeleportdAPIParser();
                backgroundTask.execute();
            }
        };

        public void run() {
            handler.post(runnable);
        }
    }


}