package dk.profundo.spouse;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WidgetConfigure extends Activity {
	static final String TAG = "WidgetConfigure";

	private static final String PREFS_NAME
            = "dk.profundo.spouse.SpouseWidgetProvider";
    private static final String PREF_PREFIX_KEY = "prefix_";

	
	public static final int PICK_CONTACT    = 1;
	
	private OnClickListener okListener = new OnClickListener() {

		public void onClick(View v) {
			final Context context = WidgetConfigure.this;
			// Push widget update to surface with newly set prefix
			
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);
			
			saveUri(context, mAppWidgetId, mContactData);
			
			SpouseWidgetProvider.updateAppWidget(context, appWidgetManager,
					mAppWidgetId);

			// Make sure we pass back the original appWidgetId
			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					mAppWidgetId);
			setResult(RESULT_OK, resultValue);
			finish();
		}
	};
	

	private OnClickListener cancelListener = new OnClickListener() {
		
		public void onClick(View v) {
			setResult(Activity.RESULT_CANCELED);
			finish();
		}
	};
	
	private OnClickListener selectContactListener = new OnClickListener() {
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_PICK, People.CONTENT_URI);  
            startActivityForResult(intent, PICK_CONTACT);  
		}
	};

	private Uri mContactData;
	
	

	@Override  
    public void onActivityResult(int reqCode, int resultCode, Intent data) {  
        super.onActivityResult(reqCode, resultCode, data);  
  
        Context context = WidgetConfigure.this;
        switch (reqCode) {  
            case (PICK_CONTACT):  
                if (resultCode == Activity.RESULT_OK) {  
                    Uri contactData = data.getData();  
                    updateContactView(context, contactData);
                    
                }  
                break;  
        }  
    }

	private void updateContactView(Context context, Uri contactData) {
		Cursor c = managedQuery(contactData, null, null, null, null);  
		TextView nameField = (TextView) findViewById(R.id.nameField);
		ImageView contactPhoto = (ImageView) findViewById(R.id.contactPhoto);
		LinearLayout contactLayout = (LinearLayout) findViewById(R.id.contactLayout);
		if (c.moveToFirst()) {  
			String name = c.getString(c.getColumnIndexOrThrow(People.NAME));  
			Bitmap cpb = People.loadContactPhoto(context,contactData, R.drawable.icon,null);
			nameField.setText(name);
			contactPhoto.setImageBitmap(cpb);
			contactLayout.setEnabled(true);
			mContactData = contactData;
		} else {
			nameField.setText("Change Contact ...");
			contactPhoto.setImageDrawable(context.getResources().getDrawable(R.drawable.icon));
			contactLayout.setEnabled(false);
			mContactData = null;
		}
	}  
	
	int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// final Context context = WidgetConfigure.this;

		setResult(RESULT_CANCELED);

		setContentView(R.layout.configure);
		findViewById(R.id.confok).setOnClickListener(okListener);
		findViewById(R.id.confcancel).setOnClickListener(cancelListener);
		findViewById(R.id.select_spouse_button).setOnClickListener(selectContactListener);

		// Find the widget id from the intent.
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		// If they gave us an intent without the widget id, just bail.
		if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			finish();
		}
	}

	// Write the prefix to the SharedPreferences object for this widget
    public static void saveUri(Context context, int appWidgetId, Uri contactData) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, contactData.toString());
        prefs.commit();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    public static Uri loadUri(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String prefix = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (prefix != null) {
            return Uri.parse(prefix);
        } else {
            return null;
        }
    }

    public static void deletePref(Context context, int i) {
		
	}


}
