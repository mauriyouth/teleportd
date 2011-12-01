package teleportd.com.droid.map;

import teleportd.com.droid.TeleportdActivity.OnGestureListenerTel;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.google.android.maps.MapView;



public class TeleportdMapView extends MapView {

	OnGestureListenerTel zoomListener ;

	public OnGestureListenerTel getZoomListener() {
		return zoomListener;
	}


	public void setZoomListener(OnGestureListenerTel zoomListener) {
		this.zoomListener = zoomListener;
	}


	public TeleportdMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs,defStyle);	
	}


	public TeleportdMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	

	 public TeleportdMapView(Context context, String apiKey) {
	  super(context, apiKey);
	 }

	 
	 int oldZoomLevel=-1;
	 @Override
	 public void dispatchDraw(Canvas canvas) {
		 super.dispatchDraw(canvas);
	  
	  if (getZoomLevel() != oldZoomLevel) {
		 zoomListener.zoomEvent();
	   oldZoomLevel = getZoomLevel();
	  }
	 
	 }
	
	
}
