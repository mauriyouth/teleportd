package teleportd.com.droid.map;

import java.util.Timer;

import com.google.android.maps.MapView;

import teleportd.com.droid.MyTimerTask;
import teleportd.com.droid.TeleportdAPIParser;
import teleportd.com.droid.image.ImageAdapter;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.widget.GridView;

public class OnGestureListenerTel implements OnGestureListener {
	Timer t = new Timer();
	Boolean scheduledTask = false;
	private long minTime = 300;
	
	TeleportdAPIParser backgroundTask;
	Handler handler;
	MapView mapView;
	ImageAdapter adapter;
	GridView gridview;
	Marker marker;
	
	
	public OnGestureListenerTel(TeleportdAPIParser backgroundTask,Handler handler, MapView mapview, ImageAdapter adapter, GridView gridView){
		backgroundTask=this.backgroundTask;
		this.handler=handler;
		this.mapView=mapview;
		this.adapter=adapter;
		this.gridview=gridView;
	}
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
		Long dt =  e2.getEventTime() - e2.getDownTime();

		//mapView.get
		if(dt>minTime){
			Log.i("long", dt.toString());
			if(!scheduledTask){
				t.schedule(new MyTimerTask(backgroundTask, handler, mapView, adapter, gridview,marker), 1000);
				scheduledTask=true;
				return false;
			}

			t.cancel();
			t.purge();

			t=new Timer();
			t.schedule(new MyTimerTask(backgroundTask, handler, mapView, adapter, gridview,marker), 1000);
			scheduledTask=true;
		}
		return false;
	}
	
	public void zoomEvent(){
		
		if(!scheduledTask){
			t.schedule(new MyTimerTask(backgroundTask, handler, mapView, adapter, gridview,marker), 1000);
			scheduledTask=true;
		 
		}
		
		else {
			t.cancel();
			t.purge();

			t=new Timer();
			t.schedule(new MyTimerTask(backgroundTask, handler, mapView, adapter, gridview,marker), 1000);
			scheduledTask=true;
		}

	
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
}