package us.lidaka.counter

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class WidgetConfigureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setResult(RESULT_CANCELED)

        val intent = intent
        val id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        if (!TEST) {
            if (id == AppWidgetManager.INVALID_APPWIDGET_ID) {
                finish()
                return
            }
        }

        setContentView(R.layout.activity_widget_configure)

        val button = findViewById<Button>(R.id.submit)
        button.setOnClickListener { v -> createCounter(v) }
    }

    private fun createCounter(view: View) {
        val intent = intent
        val id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        if (!TEST) {
            if (id == AppWidgetManager.INVALID_APPWIDGET_ID) {
                return
            }
        }

        val initialState = parseConfiguration(id)
        initialState.persist(this)

        val remoteViews = WidgetViewFactory.createRemoteViews(this, initialState)

        val manager = AppWidgetManager.getInstance(this)
        manager.updateAppWidget(id, remoteViews)

        val result = Intent()
        result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
        setResult(RESULT_OK, result)
        finish()
    }

    private fun parseConfiguration(id: Int): WidgetState {
        val labelField = findViewById<EditText>(R.id.label_field)
        val label = labelField.text.toString()

        val step = getIntOrDefault(R.id.step_field, 1)
        val count = getIntOrDefault(R.id.count_field, 0)

        return WidgetState(id, label, step, count)
    }

    private fun getIntOrDefault(id: Int, def: Int): Int {
        val et = findViewById<EditText>(id)
        var value = def
        if (et != null) {
            val e = et.text
            if (e != null) {
                val s = e.toString()
                if (!s.isBlank()) {
                    value = Integer.parseInt(s)
                }
            }
        }

        return value
    }

    companion object {

        // TODO: remove after dev complete, needed to deploy straight to activity for UI testing
        private val TEST = true
    }
}
