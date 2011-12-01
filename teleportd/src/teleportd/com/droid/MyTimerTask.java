package teleportd.com.droid;

import java.util.TimerTask;

import com.google.android.maps.MapView;

import android.os.Handler;
import android.widget.GridView;

import teleportd.com.droid.TeleportdAPIParser;
import teleportd.com.droid.image.ImageAdapter;
import teleportd.com.droid.map.Marker;

public class MyTimerTask extends TimerTask {
	TeleportdAPIParser backgroundTask;
	Handler handler;
	MapView mapView;
	ImageAdapter adapter;
	GridView gridView;
	Marker marker;
	public MyTimerTask(TeleportdAPIParser backgroundTask,Handler handler, MapView mapview, ImageAdapter adapter, GridView gridView,Marker marker) {
		// TODO Auto-generated constructor stub
		backgroundTask=this.backgroundTask;
		this.handler=handler;
		this.mapView=mapview;
		this.adapter=adapter;
		this.gridView=gridView;
		this.marker=marker;
		
	}
	private Runnable runnable = new Runnable() {
		public void run() {
			// a task can be executed only once, so we have to instantiate a new on
			if (backgroundTask==null){
				backgroundTask= new TeleportdAPIParser(mapView,adapter, gridView,marker);
				backgroundTask.execute();
				
			}
			else
			{
				backgroundTask.cancel(true);
				backgroundTask= new TeleportdAPIParser(mapView,adapter, gridView,marker);
				backgroundTask.execute();
			}
		
		}
	};

	public void run() {
		handler.post(runnable);
	}
}