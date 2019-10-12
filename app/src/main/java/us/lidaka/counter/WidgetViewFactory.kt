package us.lidaka.counter

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews

object WidgetViewFactory {
    // TODO: create OR UPDATE http://stackoverflow.com/questions/16948467/changing-textview-text-in-an-android-widget
    fun createRemoteViews(context: Context, state: WidgetState): RemoteViews {
        val view = RemoteViews(context.packageName, R.layout.activity_widget)

        view.setTextViewText(R.id.count_value, state.count.toString())
        val incIntent = createCallbackIntent(context, state, WidgetProvider.INCREMENT_ACTION)
        view.setOnClickPendingIntent(R.id.count_value, incIntent)

        view.setTextViewText(R.id.widget_label, state.label)
        val decIntent = createCallbackIntent(context, state, WidgetProvider.DECREMENT_ACTION)
        view.setOnClickPendingIntent(R.id.widget_label, decIntent)

        return view
    }

    private fun createCallbackIntent(context: Context, state: WidgetState, type: String): PendingIntent {
        val intent = Intent(context, WidgetProvider::class.java)
        intent.putExtra(WidgetProvider.STATE_KEY, state)
        intent.action = type

        return PendingIntent.getBroadcast(context, state.id, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}
