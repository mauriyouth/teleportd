package teleportd.com.droid;

import java.util.List;
import teleportd.com.droid.image.ImageAdapter;
import teleportd.com.droid.map.Marker;
import teleportd.com.droid.map.OnGestureListenerTel;
import teleportd.com.droid.map.TeleportdMapView;
import teleportd.com.droid.map.Thumb;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.Overlay;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.GridView;


public class TeleportdActivity extends MapActivity {
	/** Called when the activity is first created. */
	TeleportdMapView mapView;
	MapController mc;
	GeoPoint point; //user actual coordinate
	LocationManager lm;
	GridView gridview ;
	GestureDetector gd;
	OnGestureListenerTel mapGestureListener;
	TeleportdAPIParser backgroundTask;
	private Context con;
	private Handler handler = new Handler();
	List<Overlay> mapOverlays;
	Marker marker;
	ImageAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mapView = (TeleportdMapView) findViewById(R.id.mapView);
		gridview=(GridView) findViewById(R.id.gridView);
		mc = mapView.getController();
		mapOverlays=mapView.getOverlays();
		point = new GeoPoint(37768700 ,-122444900);
		mc.setCenter(point);
		mc.setZoom(13);
		mapView.setBuiltInZoomControls(true);
		con=getBaseContext();
		adapter= new ImageAdapter();
		
	}
	
	

	@Override
	protected void onStart() {
		super.onStart();
		//locolizeMe();
		Thumb.aggregation=(con.getResources().getDrawable(R.drawable.aggregation));
		Thumb.pin=(con.getResources().getDrawable(R.drawable.pin));
		Marker.pin=(con.getResources().getDrawable(R.drawable.pin));
		mapGestureListener = new OnGestureListenerTel(backgroundTask, handler, mapView, adapter, gridview);
		marker=new Marker(con, mapGestureListener);
		mapView.setZoomListener(mapGestureListener);
		backgroundTask= new TeleportdAPIParser(mapView,adapter, gridview,marker); //background task, fetch pictures and show them on the UI
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


	protected void locolizeMe(){
//		LocationManager locationManager =
//			(LocationManager)getSystemService(Context.LOCATION_SERVICE);
//			List<String> fournisseurs = locationManager.getProviders(true);
		LocationManager locationManager =
		(LocationManager)getSystemService(Context.LOCATION_SERVICE);
			//LocationProvider locationProvider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);
			// Or use LocationManager.GPS_PROVIDER
			 
			Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			Log.i("lastKnownLocation", lastKnownLocation.toString());
			//return point;
	}

	
	
	//button1 locate
	//buton2  orbit
	//buton3  search
	//buton4  snap
	//buton5 profile
	
	public void buttonManage(View view){
		int id = view.getId();
		switch (id) {
		case R.id.button1:
			break;
			
		case R.id.button2:
			mc.setZoom(2);
			break;
			
		case R.id.button3:
			break;
			
		case R.id.button4:
			break;
			
		case R.id.button5:
			break;
		

		default:
			break;
		}
	}


}