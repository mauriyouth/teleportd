package teleportd.com.droid.map;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;



public class Marker extends ItemizedOverlay<Thumb>  {
	private GestureDetector gd;
	 private ArrayList<Thumb> mOverlays = new ArrayList<Thumb>();
	
	
	public Marker(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		populate();
	}
	
	public Marker(Drawable defaultMarker, Context context, GestureDetector.OnGestureListener listener) {
		super(boundCenterBottom(defaultMarker));
		gd = new GestureDetector(context, listener);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		return gd.onTouchEvent(event);
	}




	public ArrayList<Thumb> getmOverlays() {
		return mOverlays;
	}

	public void setmOverlays(ArrayList<Thumb> done) {
		this.mOverlays = done;
	}

	

	@Override
	protected synchronized  Thumb createItem(int i) {
		return (Thumb) mOverlays.get(i);
	}

	@Override
	public synchronized int size() {
		return mOverlays.size();
	}
	
	public  void addOverlay(Thumb overlay) {
	    mOverlays.add(overlay);
	    
	    
	}
	
	public void poupulateMap(){
		setLastFocusedIndex(-1);
		populate();
	}
	
	@Override
	public  void draw(android.graphics.Canvas canvas,MapView mapView,boolean shadow) {
		setLastFocusedIndex(-1);	
		super.draw(canvas, mapView, false);

	}
	
	@Override
	protected synchronized int getIndexToDraw(int drawingOrder) {
		setLastFocusedIndex(-1);	
		return super.getIndexToDraw(drawingOrder);
	}
	
	
	//not implemented yet
	@Override
	protected boolean onTap(int index) {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	

	
}
