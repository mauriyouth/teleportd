package teleportd.com.droid;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class Thumb extends OverlayItem{

	String sha;
	String thumb;
	String full;
	GeoPoint loc;
	boolean merged;
	int halo;
	Drawable aggregation;	
	
//	public Thumb(Thumb copyme) {
//		this.sha=copyme.sha;
//		this.thumb=copyme.thumb;
//		this.loc=copyme.loc;
//	}



	public Thumb(String sha,String thumb, GeoPoint loc) {
		super(loc, "", "");
		this.sha = sha;
		this.thumb=thumb;
		this.loc = loc;
	}

	public Thumb(String sha, String thumb, String full, GeoPoint loc) {
		super(loc, "", "");
		this.sha = sha;
		this.thumb = thumb;
		this.full = full;
		this.loc = loc;
	}

	@Override
	public Drawable getMarker(int stateBitset){

		if (stateBitset == 0) {
			if (merged) {
				aggregation.setBounds(-aggregation.getIntrinsicWidth()/2, -aggregation.getIntrinsicHeight(), aggregation.getIntrinsicWidth() /2, 0);
				return aggregation;}}

		return null;

	}



}
