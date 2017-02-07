package us.lidaka.counter;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetViewFactory {
    // TODO: create OR UPDATE http://stackoverflow.com/questions/16948467/changing-textview-text-in-an-android-widget
    public static RemoteViews createRemoteViews(Context context, WidgetState state) {
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.activity_widget);

        view.setTextViewText(R.id.count_value, String.valueOf(state.count));
        PendingIntent incIntent = createCallbackIntent(context, state, WidgetProvider.INCREMENT_ACTION);
        view.setOnClickPendingIntent(R.id.count_value, incIntent);

        view.setTextViewText(R.id.widget_label, state.label);
        PendingIntent decIntent = createCallbackIntent(context, state, WidgetProvider.DECREMENT_ACTION);
        view.setOnClickPendingIntent(R.id.widget_label, decIntent);

        return view;
    }

    private static PendingIntent createCallbackIntent(Context context, WidgetState state, String type) {
        Intent intent = new Intent(context, WidgetProvider.class);
        intent.putExtra(WidgetProvider.STATE_KEY, state);
        intent.setAction(type);

        return PendingIntent.getBroadcast(context, state.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
