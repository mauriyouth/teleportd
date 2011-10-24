package teleportd.com.droid;

import java.io.IOException;
import java.net.URL;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;

import com.google.android.maps.GeoPoint;
import com.google.api.client.extensions.android2.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.GenericUrl;
import android.os.AsyncTask;

public class TeleportdAPIParser extends AsyncTask<GeoPoint, Void, Void> {
	
	private final HttpTransport http= AndroidHttp.newCompatibleTransport();
	private HttpRequestFactory fact;
	private HttpRequest request; 
	private HttpResponse response; 
	private URL url;
	private String urlString;
	GeoPoint point;

	JsonFactory jsonFactory = new JsonFactory();
	JsonParser jp;

	@Override
	protected Void doInBackground(GeoPoint... params) {
		point=params[0];
		fact=http.createRequestFactory();
		try {
			//loc=[34.19,-119.49,5.0,5.0]
			urlString="http://v1a.teleportd.com:8080/search?apikey=1c5a31ccf46cd172e604e103c97239bd&loc=%5B34.19,-119.49,5.0,5.0%5D&full=yes";
			request = fact.buildGetRequest(new GenericUrl(urlString));
			response = request.execute();
			response.parseAs(TPortItemList.class);
			jp =jsonFactory.createJsonParser(response.getContent());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
