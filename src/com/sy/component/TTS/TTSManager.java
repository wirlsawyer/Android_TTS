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
		// TTS Engine��l�Ƨ���
		if (status == TextToSpeech.SUCCESS) {
			int result = mTts.setLanguage(Locale.US);			
			// �]�m�o���y��
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED)
			// �P�_�y���O�_�i��
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
			// �o�Ӫ�^���G���TTS Engine�i�H��
			{
				mTts = new TextToSpeech(mActivity, this);
				Log.v("TTSManager", "TTS Engine is installed!");
			}
				break;
			case TextToSpeech.Engine.CHECK_VOICE_DATA_BAD_DATA:
				// �ݭn���y���ƾڤw�l�a
			case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_DATA:
				// �ʤֻݭn�y�����y���ƾ�
			case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_VOLUME:
			// �ʤֻݭn�y�����o���ƾ�
			{
				// �o�T�ر��p������ƾڦ���,���s�U���w�˻ݭn���ƾ�
				Log.v("TTSManager", "Need language stuff:" + resultCode);
				Intent dataIntent = new Intent();
				dataIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				mActivity.startActivity(dataIntent);

			}
				break;
			case TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL:
				// �ˬd����
			default:
				Log.v("TTSManager", "Got a failure. TTS apparently not available");
				break;
			}
		} else {
			// ��LIntent��^�����G
		}
	}
	
	public void onPause() {
		// TODO Auto-generated method stub		
		if (mTts != null)		
		{			
			// activity�Ȱ��ɤ]����TTS
			mTts.stop();
		}
	}


	public void onDestroy() {
		// TODO Auto-generated method stub		
		// ����TTS���귽
		mTts.shutdown();
	}
	
	public void speak(String strText)
	{
		mTts.speak(strText,	TextToSpeech.QUEUE_ADD, null);
	}

}
