package us.lidaka.counter;

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

    public static final String STATE_KEY = "us.lidaka.counter.AppWidgetProvider.STATE";

    // called e.g. on device reboot
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int id : appWidgetIds) {
            WidgetState state = WidgetState.load(context, id);
            if (state != null) {
                RemoteViews view = WidgetViewFactory.createRemoteViews(context, state);
                appWidgetManager.updateAppWidget(id, view);
            }
        }
    }

    // click callback
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        l("+onReceive: " + intent.getAction());

        String action = intent.getAction();
        if (!isKnownAction(action)) {
            return;
        }

        WidgetState state = unpackState(intent);
        if (state == null) {
            l("intent missing state data");
            return;
        }

        if (action.equals(INCREMENT_ACTION)) {
            state.count += state.step;
        } else if (action.equals(DECREMENT_ACTION)) {
            state.count -= state.step;
        }
        state.persist(context);

        RemoteViews view = WidgetViewFactory.createRemoteViews(context, state);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(state.id, view);
        l("-onReceive");
    }

    private static WidgetState unpackState(Intent intent) {
        WidgetState state = null;

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            state = (WidgetState)bundle.getSerializable(STATE_KEY);
        }

        return state;
    }

    private static boolean isKnownAction(String action) {
        return action.equals(INCREMENT_ACTION) || action.equals(DECREMENT_ACTION);
    }

    private static void l(String s) {
        Log.d("alidaka", s);
    }
}
