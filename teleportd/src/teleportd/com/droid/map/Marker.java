package teleportd.com.droid.map;

import java.util.ArrayList;

import teleportd.com.droid.Thumb;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;

//ItemizedOverlay manage a whole set of Overlay
public class Marker extends ItemizedOverlay<Thumb> {
	//private Context con;
	
	private ArrayList<Thumb> mOverlays = new ArrayList<Thumb>();


	public ArrayList<Thumb> getmOverlays() {
		return mOverlays;
	}

	public void setmOverlays(ArrayList<Thumb> done) {
		this.mOverlays = done;
	}

	public Marker(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		// TODO Auto-generated constructor stub
	}
	
	public Marker(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		//con=context;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Thumb createItem(int i) {
		// TODO Auto-generated method stub
		return (Thumb) mOverlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mOverlays.size();
	}
	
	public void addOverlay(Thumb overlay) {
		
	    mOverlays.add(overlay);
	    
	}
	
	public void poupulateMap(){
		populate();
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow){
	super.draw(canvas, mapView, false);
	}
/*	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(con);
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet());
	  dialog.show();
	  return true;
	}*/
}
