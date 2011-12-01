package teleportd.com.droid;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends Activity {
	public static final int SPLASH_TIMEOUT = 10000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				startActivity(new Intent(SplashScreen.this, TeleportdActivity.class));
				finish();
			}
		}, SPLASH_TIMEOUT);
	}

}
