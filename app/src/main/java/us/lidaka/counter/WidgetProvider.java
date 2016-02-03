package us.lidaka.counter;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {
    public static final String INCREMENT_ACTION = "us.lidaka.counter.AppWidgetProvider.INCREMENT_ACTION";
    public static final String DECREMENT_ACTION = "us.lidaka.counter.AppWidgetProvider.DECREMENT_ACTION";

    public static final String COUNT_KEY = "us.lidaka.counter.AppWidgetProvider.COUNT";
    public static final String STEP_KEY = "us.lidaka.counter.AppWidgetProvider.STEP";
    public static final String LABEL_KEY = "us.lidaka.counter.AppWidgetProvider.LABEL";

    private static void setExtras(Intent intent, int id, String label, int count, int step) {
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        intent.putExtra(LABEL_KEY, label);
        intent.putExtra(COUNT_KEY, count);
        intent.putExtra(STEP_KEY, step);
    }

    public static RemoteViews createRemoteViews(Context context, int id, String label, int count, int step) {
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.activity_widget);

        Intent incIntent = new Intent(context, WidgetProvider.class);
        setExtras(incIntent, id, label, count, step);
        incIntent.setAction(INCREMENT_ACTION);
        PendingIntent incPendingIntent = PendingIntent.getBroadcast(context, id, incIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        view.setTextViewText(R.id.widget_label, label);
        view.setOnClickPendingIntent(R.id.widget_label, incPendingIntent);

        view.setTextViewText(R.id.count_value, String.valueOf(count));
        view.setOnClickPendingIntent(R.id.count_value, incPendingIntent);

        Intent decIntent = new Intent(context, WidgetProvider.class);
        setExtras(decIntent, id, label, count, step);
        decIntent.setAction(DECREMENT_ACTION);
        PendingIntent decPendingIntent = PendingIntent.getBroadcast(context, id, decIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.decrement_button, decPendingIntent);

        return view;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        l("+onReceive: " + intent.getAction());

        String action = intent.getAction();
        if (!isKnownAction(action)) {
            return;
        }

        Bundle bundle = intent.getExtras();
        b(bundle);
        if (!hasRequiredExtras(bundle)) {
            l(bundle == null ? "bundle null" : "bundle missing key");
            return;
        }

        int id = bundle.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID);
        int count = bundle.getInt(COUNT_KEY);
        int step = bundle.getInt(STEP_KEY);
        String label = bundle.getString(LABEL_KEY);

        if (action.equals(INCREMENT_ACTION)) {
            count += step;
        } else if (action.equals(DECREMENT_ACTION)) {
            count -= step;
        }

        // TODO: update view instead of replacing?
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        RemoteViews view = createRemoteViews(context, id, label, count, step);
        manager.updateAppWidget(id, view);
        l("-onReceive");
    }

    private static boolean hasRequiredExtras(Bundle bundle) {
        return bundle != null &&
                bundle.containsKey(AppWidgetManager.EXTRA_APPWIDGET_ID) &&
                bundle.containsKey(COUNT_KEY) &&
                bundle.containsKey(STEP_KEY) &&
                bundle.containsKey(LABEL_KEY);
    }

    private static boolean isKnownAction(String action) {
        return action.equals(INCREMENT_ACTION) || action.equals(DECREMENT_ACTION);
    }

    private static void l(String s) {
        Log.d("alidaka", s);
    }

    private static void b(Bundle bundle) {
        if (bundle != null) {
            l("bundle:");
            for (String key : bundle.keySet()) {
                l(String.format("| %s=%s", key, bundle.get(key)));
            }
            if (bundle.containsKey(AppWidgetManager.EXTRA_APPWIDGET_OPTIONS)) {
                l("| extras:");
                Bundle extras = bundle.getBundle(AppWidgetManager.EXTRA_APPWIDGET_OPTIONS);
                for (String k : extras.keySet()) {
                    l(String.format("| | %s=%s", k, extras.get(k)));
                }
            }
        }
    }
}
