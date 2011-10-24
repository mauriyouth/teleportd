package teleportd.com.droid;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;

public class TeleportdActivity extends MapActivity {
    /** Called when the activity is first created. */
	MapView mapView;
	MapController mc;
	GeoPoint point; //user actual coordinate
	LocationManager lm;
	TeleportdAPIParser tp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mapView = (MapView) findViewById(R.id.mapView);
        mc = mapView.getController();
        lm = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        
       /* String coordinates[] = {"1.352566007", "103.78921587"};
        double lat = Double.parseDouble(coordinates[0]);
        double lng = Double.parseDouble(coordinates[1]);
 
        p = new GeoPoint(
            (int) (lat * 1E6), 
            (int) (lng * 1E6));*/
        point = new GeoPoint(19240000,-99120000);
        mc.animateTo(point);
        mc.setZoom(13);
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_launcher);
        Marker itemizedoverlay = new Marker(drawable);
     
        OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        mapView.invalidate();
        tp=new TeleportdAPIParser();
        tp.execute(point);
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}