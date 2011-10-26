package teleportd.com.droid;

import java.io.IOException;
import java.net.URLEncoder;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import com.google.api.client.extensions.android2.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.GenericUrl;
import org.codehaus.jackson.JsonToken;

import teleportd.com.droid.TPortItemList.TPortItem;

import android.os.AsyncTask;


public class parser extends AsyncTask<String, Void, Void> {
	
	private final HttpTransport http= AndroidHttp.newCompatibleTransport();
	private HttpRequestFactory fact;
	private HttpRequest request; 
	private HttpResponse response; 
	private String urlString;
	private JsonFactory jsonFactory; 
	private JsonParser jp;
	private TPortItemList tpl;
	
	@Override
	protected Void doInBackground(String... params) {
		urlString=params[0];
		fact=http.createRequestFactory();
		jsonFactory = new JsonFactory();
		tpl= new TPortItemList();
		TPortItem tpi= new TPortItem();
		
			///urlString="http://v1.api.teleportd.com:8080/search?apikey=1c5a31ccf46cd172e604e103c97239bd&loc=%5B34.19,-119.49,5.0,5.0%5D";
			try {
				urlString=URLEncoder.encode(urlString,"UTF-8");
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
								publishProgress();
							}
							
							jp.nextValue();	
						}
					
					//tpl.i.add(tpi);
						
	
				}
							
			
			
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return null;
	}



	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
		
	}

}
