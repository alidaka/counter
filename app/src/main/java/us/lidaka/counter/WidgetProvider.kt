package us.lidaka.counter

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews

class WidgetProvider : AppWidgetProvider() {

    // called e.g. on device reboot
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        for (id in appWidgetIds) {
            val state = WidgetState.load(context, id)
            if (state != null) {
                val view = WidgetViewFactory.createRemoteViews(context, state)
                appWidgetManager.updateAppWidget(id, view)
            }
        }
    }

    // click callback
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        l("+onReceive: " + intent.action!!)

        val action = intent.action
        if (!isKnownAction(action!!)) {
            return
        }

        val state = unpackState(intent)
        if (state == null) {
            l("intent missing state data")
            return
        }

        if (action == INCREMENT_ACTION) {
            state.count += state.step
        } else if (action == DECREMENT_ACTION) {
            state.count -= state.step
        }
        state.persist(context)

        val view = WidgetViewFactory.createRemoteViews(context, state)
        val manager = AppWidgetManager.getInstance(context)
        manager.updateAppWidget(state.id, view)
        l("-onReceive")
    }

    companion object {
        const val INCREMENT_ACTION = "us.lidaka.counter.AppWidgetProvider.INCREMENT_ACTION"
        const val DECREMENT_ACTION = "us.lidaka.counter.AppWidgetProvider.DECREMENT_ACTION"

        const val STATE_KEY = "us.lidaka.counter.AppWidgetProvider.STATE"

        private fun unpackState(intent: Intent): WidgetState? {
            return intent.extras?.getSerializable(STATE_KEY) as WidgetState
        }

        private fun isKnownAction(action: String): Boolean {
            return action == INCREMENT_ACTION || action == DECREMENT_ACTION
        }

        private fun l(s: String) {
            Log.d("alidaka", s)
        }
    }
}
