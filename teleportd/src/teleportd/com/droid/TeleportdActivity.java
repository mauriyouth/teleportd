package teleportd.com.droid;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.GridView;

public class TeleportdActivity extends MapActivity {
    /** Called when the activity is first created. */
	MapView mapView;
	GridView gridview;
	MapController mc;
	GeoPoint point; //user actual coordinate
	LocationManager lm;
	TeleportdAPIParser tp;
	String bestProvider;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mapView = (MapView) findViewById(R.id.mapView);
        gridview = (GridView) findViewById(R.id.gridView);
        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_launcher); // overlay icon
        
        mc = mapView.getController();
        lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        tp=new TeleportdAPIParser();
        
        //Criteria criteria = new Criteria();
		//bestProvider = lm.getBestProvider(criteria, false);
		//Location location = lm.getLastKnownLocation(lm.NETWORK_PROVIDER);
       // point = new GeoPoint((int) (location.getLatitude() * 1E6),(int) (location.getLongitude() * 1E6));
        
        point = new GeoPoint((int) (22.312381*1E6),(int)(114.225242*1E6));
		mc.setCenter(point);
        //mc.zoomToSpan(5, 5);
        
        List<Overlay> mapOverlays = mapView.getOverlays();
       
        Marker marker = new Marker(drawable);
        
        OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!"); // one overlayitem example 
        
        marker.addOverlay(overlayitem);
        
        mapOverlays.add(marker); // adding the whole overlays (list) on the maps
        
        tp.execute(point); //background task, fetch pictures and show them on the UI
        mapView.setBuiltInZoomControls(true);
        mapView.invalidate();
       
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}