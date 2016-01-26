package us.lidaka.counter;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by augustus on 1/24/16.
 */
public class WidgetProvider extends AppWidgetProvider {
    public static final String INCREMENT_ACTION = "IncrementAction";
    public static final String DECREMENT_ACTION = "DecrementAction";

    public static final String INITIALIZED_KEY = "initialized";
    public static final String COUNT_KEY = "count";
    public static final String STEP_KEY = "step";
    public static final String LABEL_KEY = "label";

    private static boolean isWidgetInitialized(int widgetId, AppWidgetManager appWidgetManager) {
        Bundle bundle = appWidgetManager.getAppWidgetOptions(widgetId);
        return (bundle != null) && bundle.getBoolean(INITIALIZED_KEY);
    }

    private static RemoteViews createRemoteViews(Context context, int id, String label, int count) {
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.activity_widget);

        Intent incIntent = new Intent(context, WidgetProvider.class);
        incIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        incIntent.setAction(INCREMENT_ACTION);
        PendingIntent incPendingIntent = PendingIntent.getBroadcast(context, 0, incIntent, 0);

        view.setTextViewText(R.id.widget_label, label);
        view.setPendingIntentTemplate(R.id.widget_label, incPendingIntent);

        view.setTextViewText(R.id.count_value, String.valueOf(count));
        view.setPendingIntentTemplate(R.id.count_value, incPendingIntent);

        Intent decIntent = new Intent(context, WidgetProvider.class);
        decIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        decIntent.setAction(DECREMENT_ACTION);
        PendingIntent decPendingIntent = PendingIntent.getBroadcast(context, 0, decIntent, 0);
        view.setPendingIntentTemplate(R.id.decrement_button, decPendingIntent);

        return view;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int id : appWidgetIds) {
            Bundle bundle = appWidgetManager.getAppWidgetOptions(id);
            if (isWidgetInitialized(id, appWidgetManager)) {
                continue;
            }

            if (bundle == null) {
                bundle = new Bundle();
            }

            // TODO: parameterize in configuration activity
            String label = "Hello, widget!";
            bundle.putBoolean(INITIALIZED_KEY, true);
            bundle.putInt(COUNT_KEY, 0);
            bundle.putInt(STEP_KEY, 1);
            bundle.putString(LABEL_KEY, label);
            appWidgetManager.updateAppWidgetOptions(id, bundle);

            RemoteViews view = createRemoteViews(context, id, label, 0);
            appWidgetManager.updateAppWidget(id, view);
        }
    }

    private static boolean hasRequiredExtras(Bundle bundle) {
        return bundle.containsKey(AppWidgetManager.EXTRA_APPWIDGET_ID) &&
                bundle.containsKey(COUNT_KEY) &&
                bundle.containsKey(STEP_KEY);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        l("+onReceive");

        Bundle bundle = intent.getExtras();
        if (bundle == null || !bundle.containsKey(AppWidgetManager.EXTRA_APPWIDGET_ID) || !bundle.containsKey(AppWidgetManager.EXTRA_APPWIDGET_OPTIONS)) {
            return;
        }

        int id = bundle.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
        bundle = bundle.getBundle(AppWidgetManager.EXTRA_APPWIDGET_OPTIONS);

        if (bundle == null || !hasRequiredExtras(bundle)) {
            return;
        }

        int count = bundle.getInt(COUNT_KEY);
        int step = bundle.getInt(STEP_KEY);
        String label = bundle.getString(LABEL_KEY);

        String action = intent.getAction();
        if (action.equals(INCREMENT_ACTION)) {
            count += step;
        } else if (action.equals(DECREMENT_ACTION)) {
            count -= step;
        }

        bundle.putInt(COUNT_KEY, count);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidgetOptions(id, bundle);

        // TODO: update view instead of replacing?
        RemoteViews view = createRemoteViews(context, id, label, count);
        manager.updateAppWidget(id, view);
        l("-onReceive");
    }

    private static void l(String s) {
        Log.d("alidaka", s);
    }
}
