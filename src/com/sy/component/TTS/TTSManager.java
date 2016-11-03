package com.sy.component.TTS;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;

public class TTSManager implements OnInitListener {
	private static final TTSManager instance = new TTSManager();
	private static final int REQ_TTS_STATUS_CHECK = 0;
	private Activity mActivity;
	private TextToSpeech mTts;
	private boolean mIsCanUse = false;
	
	private TTSManager() {

	}

	public static TTSManager getInstance() {
		return instance;
	}

	public void Init(Activity activity) {
		mActivity = activity;
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		activity.startActivityForResult(checkIntent, REQ_TTS_STATUS_CHECK);
	}

	public boolean setSpeechRate(float rate)
	{
		if (mIsCanUse == false) return false;
		mTts.setSpeechRate(rate);
		return true;
	}
	
	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		// TTS Engine初始化完成
		if (status == TextToSpeech.SUCCESS) {
			int result = mTts.setLanguage(Locale.US);			
			// 設置發音語言
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED)
			// 判斷語言是否可用
			{
				Log.v("TTSManager", "Language is not available");
				mIsCanUse = false;
			} else {
				//mTts.speak("This is an example of speech synthesis.",	TextToSpeech.QUEUE_ADD, null);
				Log.v("TTSManager", "Language is available");
				mIsCanUse = true;
			}
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQ_TTS_STATUS_CHECK) {
			switch (resultCode) {
			case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS:
			// 這個返回結果表明TTS Engine可以用
			{
				mTts = new TextToSpeech(mActivity, this);
				Log.v("TTSManager", "TTS Engine is installed!");
			}
				break;
			case TextToSpeech.Engine.CHECK_VOICE_DATA_BAD_DATA:
				// 需要的語音數據已損壞
			case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_DATA:
				// 缺少需要語言的語音數據
			case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_VOLUME:
			// 缺少需要語言的發音數據
			{
				// 這三種情況都表明數據有錯,重新下載安裝需要的數據
				Log.v("TTSManager", "Need language stuff:" + resultCode);
				Intent dataIntent = new Intent();
				dataIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				mActivity.startActivity(dataIntent);

			}
				break;
			case TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL:
				// 檢查失敗
			default:
				Log.v("TTSManager", "Got a failure. TTS apparently not available");
				break;
			}
		} else {
			// 其他Intent返回的結果
		}
	}
	
	public void onPause() {
		// TODO Auto-generated method stub		
		if (mTts != null)		
		{			
			// activity暫停時也停止TTS
			mTts.stop();
		}
	}


	public void onDestroy() {
		// TODO Auto-generated method stub		
		// 釋放TTS的資源
		mTts.shutdown();
	}
	
	public void speak(String strText)
	{
		mTts.speak(strText,	TextToSpeech.QUEUE_ADD, null);
	}

}
