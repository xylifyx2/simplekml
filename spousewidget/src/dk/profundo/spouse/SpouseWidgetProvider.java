package dk.profundo.spouse;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.Contacts.People;
import android.util.Log;
import android.widget.RemoteViews;

public class SpouseWidgetProvider extends AppWidgetProvider {
	private static final String TAG = "SpouseWidgetProvider";
	
	@Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate");
        // For each widget that needs an update, get the text that we should display:
        //   - Create a RemoteViews object for it
        //   - Set the text in the RemoteViews object
        //   - Tell the AppWidgetManager to show that views object for the widget.
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            // String titlePrefix = WidgetConfigure.loadTitlePref(context, appWidgetId);
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
	
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG, "onDeleted");
        // When the user deletes the widget, delete the preference associated with it.
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            WidgetConfigure.deletePref(context, appWidgetIds[i]);
        }
    }

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "onEnabled");
        // When the first widget is created, register for the TIMEZONE_CHANGED and TIME_CHANGED
        // broadcasts.  We don't want to be listening for these if nobody has our widget active.
        // This setting is sticky across reboots, but that doesn't matter, because this will
        // be called after boot if there is a widget instance for this provider.
        PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(new ComponentName(
				SpouseBroadcastReceiver.class.getPackage().getName(),
				SpouseBroadcastReceiver.class.getSimpleName()),
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);
	}
	
	static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
            int appWidgetId) {
		Log.d(TAG, "updateAppWidget appWidgetId=" + appWidgetId);
		
		Uri contactUri = WidgetConfigure.loadUri(context, appWidgetId);
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
		Bitmap cpb = People.loadContactPhoto(context,contactUri, R.drawable.icon,null);
		views.setBitmap(R.id.widgetPicture, "setImageBitmap", cpb);
		Intent intent = new Intent(Intent.ACTION_VIEW, contactUri);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		views.setOnClickPendingIntent(R.id.widgetPicture, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
