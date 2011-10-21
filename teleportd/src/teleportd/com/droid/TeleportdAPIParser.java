package teleportd.com.droid;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;

import android.os.AsyncTask;

public class TeleportdAPIParser extends AsyncTask<Void, Void, Void> {

	JsonFactory jsonFactory = new JsonFactory();
	JsonParser jp = jsonFactory.createJsonParser();

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		return null;
	}

}
