package teleportd.com.droid.map;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;




public class Marker extends ItemizedOverlay<Thumb>  {
	private GestureDetector gd;
	private ArrayList<Thumb> mOverlays = new ArrayList<Thumb>();
	public static Drawable pin;
	MapView mapView;





	public Marker( Context context, GestureDetector.OnGestureListener listener) {
		super(boundCenterBottom(pin));
		gd = new GestureDetector(context, listener);
		populate();
	}


	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		gd.onTouchEvent(event);
		return super.onTouchEvent(event, mapView);
	}




	public ArrayList<Thumb> getmOverlays() {
		return mOverlays;
	}

	public void setmOverlays(ArrayList<Thumb> done) {
		this.mOverlays = done;
	}



	@Override
	protected  synchronized Thumb createItem(int i) {
		if(i>=mOverlays.size())
			return null;
		return  mOverlays.get(i);
	}


	@Override
	public synchronized  int size() {

		return this.mOverlays.size();
	}

	public synchronized  void  addOverlay(Thumb overlay) {
		mOverlays.add(overlay);

		
	}

	public synchronized void removeOverlay(Thumb overlay){
		mOverlays.remove(overlay);
	
	
	}

	public void poupulateMap(){
		setLastFocusedIndex(-1);
		super.populate();
		
	}


	int i=0;
	//we overrided draw so we can remove shaddow of map pins 
	@Override
	public  void draw(android.graphics.Canvas canvas,MapView mapView,boolean shadow) {	
		super.draw(canvas, mapView, false);
		Log.i("draw called ", ((Integer) i).toString());
		i++;


	}

	@Override
	protected  int getIndexToDraw(int drawingOrder) {
		return super.getIndexToDraw(drawingOrder);
	}


	//not implemented yet
	@Override
	protected boolean onTap(int index) {
		// TODO Auto-generated method stub
		/*   BrewLocation brewLocation = brewLocations.get(index);
		      AlertDialog.Builder builder = new AlertDialog.Builder(context);
		      builder.setTitle("BrewLocation")
		               .setMessage(brewLocation.getName() + "\n\nVisit the pub detail page for more information?").setCancelable(true)
		               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		                  public void onClick(DialogInterface dialog, int id) {
		                     Intent i = new Intent(context, BrewLocationDetails.class);
		                     i.putExtra(BrewMapApp.PUB_INDEX, index);
		                     context.startActivity(i);
		                  }
		               }).setNegativeButton("No", new DialogInterface.OnClickListener() {
		                  public void onClick(DialogInterface dialog, int id) {
		                     dialog.cancel();
		                  }
		               });
		      AlertDialog alert = builder.create();
		      alert.show();

		      return true; // we'll handle the event here (true) not pass to another overlay (false)
		 */
		return true;
	}





}
