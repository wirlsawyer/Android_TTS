package com.example.android_tts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.sy.component.TTS.TTSManager;

public class MainActivity extends Activity  {

	private static final String TAG = "TTS Demo";
	
	private EditText inputText = null;
	private Button speakBtn = null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TTSManager.getInstance().Init(this);

		inputText = (EditText) findViewById(R.id.inputText);
		speakBtn = (Button) findViewById(R.id.speakBtn);
		inputText.setText("This is an example of speech synthesis.");
		speakBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TTSManager.getInstance().speak(inputText.getText().toString());
				// 朗讀輸入框裏的內容
			}
		});
		speakBtn.setEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		TTSManager.getInstance().onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		TTSManager.getInstance().onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TTSManager.getInstance().onDestroy();
	}
}
