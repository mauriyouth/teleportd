package teleportd.com.droid.map;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;


public class DynamicItemizedOverlay<T extends OverlayItem> extends
        ItemizedOverlay<T> {

    public ArrayList<T> items = null;
    private MapView mapView = null;
    private View root = null;
    private Drawable defaultMarker;

    public DynamicItemizedOverlay(Drawable defaultMarker, MapView mapView) {
        super (boundCenterBottom(defaultMarker));
        this .defaultMarker = defaultMarker;
        this .mapView = mapView;
        items = new ArrayList<T>();
        populate();
    }

    public void updateMap() {
        populate();
    }

    public void clearMap() {
        items.clear();
    }

    public static Drawable adjustBound(Drawable marker) {
        return boundCenterBottom(marker);
    }

    public void addNewItem(T item) {
        items.add(item);
        populate();
    }
    
    public void addItems(ArrayList<T> item) {
        items.addAll(item);
        populate();
    }

    public void removeItem(int index) {
        items.remove(index);
        populate();
    }
    
    public void removeItem(T index) {
        items.remove(index);
        populate();
    }

    @Override
    protected T createItem(int index) {
        return items.get(index);
    }

    @Override
    public int size() {
        return items.size();
    }

   
}