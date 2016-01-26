package us.lidaka.counter;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * Created by augustus on 1/24/16.
 */
public class WidgetProvider extends AppWidgetProvider {
    public static final String INCREMENT_ACTION = "IncrementAction";
    public static final String DECREMENT_ACTION = "DecrementAction";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int id : appWidgetIds) {
            Intent incIntent = new Intent(context, WidgetProvider.class);
            incIntent.setAction(INCREMENT_ACTION);
            PendingIntent incPendingIntent = PendingIntent.getBroadcast(context, 0, incIntent, 0);
            RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.activity_widget);

            view.setTextViewText(R.id.widget_label, "Hello, widget!");
            view.setPendingIntentTemplate(R.id.widget_label, incPendingIntent);

            view.setTextViewText(R.id.count_value, "0");
            view.setPendingIntentTemplate(R.id.count_value, incPendingIntent);

            Intent decIntent = new Intent(context, WidgetProvider.class);
            incIntent.setAction(DECREMENT_ACTION);
            PendingIntent decPendingIntent = PendingIntent.getBroadcast(context, 0, decIntent, 0);
            view.setPendingIntentTemplate(R.id.decrement_button, decPendingIntent);

            appWidgetManager.updateAppWidget(id, view);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();
        if (action.equals(INCREMENT_ACTION)) {

        } else if (action.equals(DECREMENT_ACTION)) {

        }
    }
}
