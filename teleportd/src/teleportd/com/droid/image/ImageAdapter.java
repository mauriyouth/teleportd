package teleportd.com.droid.image;

import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    public ArrayList<String> URLS;
    public ArrayList<String> sha;

    public ImageAdapter() {
    	URLS=new ArrayList<String>();
    	sha=new ArrayList<String>();
    
    }

    public int getCount() {
        return URLS.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(parent.getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY );
      
        } else {
            imageView = (ImageView) convertView;
        }

        ImageLoader.initialize(parent.getContext());
        ImageLoader.start(URLS.get(position), imageView);
        return imageView;
    }
}
