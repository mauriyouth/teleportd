package teleportd.com.droid;

import java.io.IOException;
import java.net.URL;
import java.util.ListIterator;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import com.google.android.maps.GeoPoint;
import com.google.api.client.extensions.android2.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.GenericUrl;
import org.codehaus.jackson.JsonToken;

import teleportd.com.droid.TPortItemList.TPortItem;

import android.os.AsyncTask;
import android.util.Log;

public class TeleportdAPIParser extends AsyncTask<GeoPoint, Void, Void> {
	
	private final HttpTransport http= AndroidHttp.newCompatibleTransport();
	private HttpRequestFactory fact;
	private HttpRequest request; 
	private HttpResponse response; 
	private URL url;
	private String urlString;
	private JsonFactory jsonFactory; 
	private JsonParser jp;
	private TPortItemList tpl;


	GeoPoint point;


	
	@Override
	protected Void doInBackground(GeoPoint... params) {
		point=params[0];
		fact=http.createRequestFactory();
		jsonFactory = new JsonFactory();
		tpl= new TPortItemList();
		TPortItem tpi= new TPortItem();
		
		
		
			//loc=[34.19,-119.49,5.0,5.0]
			urlString="http://v1.api.teleportd.com:8080/search?apikey=1c5a31ccf46cd172e604e103c97239bd&loc=%5B34.19,-119.49,5.0,5.0%5D";
			try {
				request = fact.buildGetRequest(new GenericUrl(urlString));
				response = request.execute();
				jp = jsonFactory.createJsonParser(response.getContent());
				

				while(jp.nextValue()!=JsonToken.NOT_AVAILABLE){
					
					jp.nextValue();
					while(jp.getCurrentToken()!=JsonToken.END_OBJECT && jp.getCurrentToken()!=null){							
						
							if(jp.getCurrentName().equals("sha")){
								tpi.sha=jp.getText();
							}
							
							if(jp.getCurrentName().equals("date")){
								tpi.date=jp.getIntValue();			
							}
							
							if(jp.getCurrentName().equals("age")){
								tpi.age=jp.getIntValue();
							}
							
							if(jp.getCurrentName().equals("thumb")){
								tpi.thumb=jp.getText();			
							}
							
							if(jp.getCurrentName().equals("rank")){
								tpi.rank=jp.getIntValue();
							}
							
							if(jp.getCurrentName().equals("grade")){
								tpi.grade=jp.getIntValue();
							}
							
							if(jp.getCurrentName().equals("loc")){
								jp.nextValue().toString();
								tpi.loc[0]=(int) (jp.getFloatValue()*1E6);
								jp.nextValue().toString();
								tpi.loc[1]=(int) (jp.getFloatValue()*1E6);
								jp.nextValue();
							}
							
							jp.nextValue();	
						}
					
					tpl.i.add(tpi);
					
						
						
	
				}
							
			
			
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return null;
	}

}
