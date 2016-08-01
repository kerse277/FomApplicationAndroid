package com.fom.msesoft.fomapplication.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.activity.ProfileActivity_;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class GCMNotificationIntentService extends IntentService {

	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;

	public GCMNotificationIntentService() {
		super("GcmIntentService");
	}

	public static final String TAG = "GCMNotificationIntentService";

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification(null);
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification(null);
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				CustomPerson customPerson = new CustomPerson();
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					customPerson = objectMapper.readValue(""+extras.get(Config.MESSAGE_KEY),CustomPerson.class);
				} catch (IOException e) {
					e.printStackTrace();
				}

				sendNotification(customPerson);

			}
		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(CustomPerson customPerson) {

		String id = customPerson.getUniqueId();
		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent = new Intent(this,ProfileActivity_.class);
		intent.putExtra("uniqueId",customPerson.getUniqueId());
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_info_outline_black_48dp)
				.setContentTitle("FomApplication")
				.setStyle(new NotificationCompat.BigTextStyle().bigText("assa"))
				.setContentText(customPerson.getFirstName()+" "+customPerson.getLastName()+"\n"
						+" Kişisi sizi arkadaş eklemek istiyor...");

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

	}

}
