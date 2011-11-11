package teleportd.com.droid.map;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class Thumb extends OverlayItem{

	public String sha;
	String thumb;
	String full;
	public GeoPoint loc;
	public boolean merged;
	public int halo;
	public Drawable aggregation;
	public Drawable pin;
	Drawable[] layers = new Drawable[2];
	LayerDrawable layer;
	
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
				aggregation.setAlpha((int) Math.min(255*0.8f, 255*halo*0.05));
				pin.setBounds(-aggregation.getIntrinsicWidth()/2, -aggregation.getIntrinsicHeight(), aggregation.getIntrinsicWidth() /2, 0);
				layers[0] = aggregation;
				layers[1] = pin;
				layer = new LayerDrawable(layers);
				Drawable ret =layer.mutate();
				return ret;
				}
	}
		return null;
	}

	}
