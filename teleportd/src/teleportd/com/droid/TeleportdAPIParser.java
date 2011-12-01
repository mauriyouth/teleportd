package teleportd.com.droid;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import teleportd.com.droid.image.ImageAdapter;
import teleportd.com.droid.map.Marker;
import teleportd.com.droid.map.Thumb;

public class TeleportdAPIParser extends AsyncTask<Void, Object, Void> {
		private HttpClient client; 
		private HttpGet getRequest;
		private JsonFactory jsonFactory; 
		private JsonParser jp;
		private ArrayList<Thumb> thumbs;
		private MapView mapView;
		private ImageAdapter adapter;
		private Marker marker;
		private List<Overlay> mapOverlays;
		private GridView gridview;
		
		
		public TeleportdAPIParser(MapView mapview,ImageAdapter adapter,GridView gridview,Marker marker) {
			// TODO Auto-generated constructor stub
			this.mapView=mapview;
			this.adapter=adapter;
			this.gridview=gridview;
			this.marker=marker;
		}
		
		
		
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
		protected void onPreExecute() {
			super.onPreExecute();
			client = new DefaultHttpClient();
			thumbs=new ArrayList<Thumb> ();
			jsonFactory = new JsonFactory();
			mapOverlays=mapView.getOverlays();
		}


		@Override
		protected  Void doInBackground(Void... params) {
			Log.i("latitudespan,",((Integer)mapView.getLatitudeSpan()).toString());
			Log.i("longitudespan,",((Integer)mapView.getLongitudeSpan()).toString());

			try {
				Log.i("sss",generateAPIRequest());
				getRequest=new HttpGet(generateAPIRequest());
				HttpResponse response=client.execute(getRequest);
				getRequest.setHeader("encoding", "gzip");				
				jp = jsonFactory.createJsonParser(new BufferedInputStream(response.getEntity().getContent()));
				String sha="";
				String thumb="";
				int log=0;
				int lat=0;

				while(jp.nextValue()!=null){

					jp.nextValue();
					while(jp.getCurrentToken()!=JsonToken.END_OBJECT && jp.getCurrentToken()!=null){

						if(jp.getCurrentName().equals("sha")){
							sha=jp.getText();
							adapter.sha.add(sha);
						}

						if(jp.getCurrentName().equals("date")){
							//tpi.date=jp.getIntValue();			
						}

						if(jp.getCurrentName().equals("age")){
							//tpi.age=jp.getIntValue();
						}


						if(jp.getCurrentName().equals("thumb")){
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
							lat=(int) (jp.getFloatValue()*1E6);
							jp.nextValue().toString();
							log=(int) (jp.getFloatValue()*1E6);
							jp.nextValue();
							//publishProgress(new GeoPoint (tpi.loc[0],tpi.loc[1]));
						}

						jp.nextValue();	

					}
					thumbs.add(new Thumb(sha,thumb,new GeoPoint(lat,log)));
					
	

				}
				jp.close();
				client.getConnectionManager().shutdown();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			consolidateOp();
			
			return null;
			
		


			
		}

		protected void consolidateOp()
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
			
			
			Log.i("fetched images that fits in my mapview rect",((Integer) done.size()).toString());
			consolidateOpDone(done);
		
		

		}



		private void consolidateOpDone(ArrayList<Thumb> done) {
			Log.i("mapOverlays size",((Integer) mapOverlays.size()).toString());
			
		
			if(mapOverlays.size()==0){
				marker.setmOverlays(done);
				marker.poupulateMap();
				mapOverlays.add(marker);
				marker.poupulateMap();
				ArrayList<Thumb> annotations=marker.getmOverlays();
				Log.i("annotations size first time ",((Integer) annotations.size()).toString());

			}
			else
			{
				//same as, same object reference
				//marker.getmOverlays();
				//those currently on the mapview
			
				ArrayList<Thumb> annotations=marker.getmOverlays();
				Log.i("annotations size",((Integer) annotations.size()).toString());
				
				
				//those to be removed
				ArrayList<Thumb> delPoints = new ArrayList<Thumb>();

				Boolean found=false;
				
					//we check here if there's an overlayitem loaded in "done array" and already exist on the map
					for(Thumb pt:annotations) {
						found=false;
						for(Iterator<Thumb> it=done.iterator(); it.hasNext();){
							Thumb d=it.next();
							if(d.sha.equals(pt.sha)){
								it.remove();
								found=true;
								break;
							}
							
						}
						
						if(!found){ 
							delPoints.add(pt);
							found=false;
							Log.i("delete","added to delete list");
						}
						
					}
					
					int b=1;
					for(Thumb pt:delPoints) {
						marker.removeOverlay(pt);
						Log.i("removed",((Integer) b).toString());
						b++;
					}
					int c=1;
					for(Thumb a:done) {
						marker.addOverlay(a);
						Log.i("added",((Integer) c).toString());
						c++;
					}
					
					
				Log.i("annotations size",((Integer) annotations.size()).toString());	
				marker.poupulateMap();
				mapView.postInvalidate();

		}
			}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			gridview.setAdapter(adapter);
		}

	

	}
