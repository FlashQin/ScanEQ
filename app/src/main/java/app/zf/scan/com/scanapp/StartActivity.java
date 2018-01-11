package app.zf.scan.com.scanapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;



import java.util.Timer;
import java.util.TimerTask;

import app.zf.scan.com.BaseHelper.BaseActivity;

/**
 * 
 * @author qidong yemian
 *
 */

public class StartActivity extends BaseActivity {
	private Editor editor;
	private SharedPreferences preferences,sp;
	private String versionName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
setContentView(R.layout.activity_start);


		try {
			PackageManager pm = getPackageManager();
			PackageInfo pinfo = pm.getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS);
			versionName = pinfo.versionName;
		} catch (Exception e) {
			// TODO: handle exception
		}
		sp = getSharedPreferences("USER_INFO", MODE_PRIVATE);
		String phone = sp.getString("account", "");

			if (!phone .equals("")) {

				Timer timer = new Timer();
				final Intent intent = new Intent(this, MainActivity.class);
				TimerTask task = new TimerTask() {

					@Override
					public void run() {
						startActivity(intent);
						StartActivity.this.finish();
					}
				};

				timer.schedule(task, 1500);
			}else {
				Timer timer = new Timer();
				final Intent intent = new Intent(this, LoginActivity.class);
				TimerTask task = new TimerTask() {

					@Override
					public void run() {
						startActivity(intent);
						StartActivity.this.finish();
					}
				};

				timer.schedule(task, 1500);
			}
		}
		


	}



