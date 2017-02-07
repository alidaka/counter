package us.lidaka.counter;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RemoteViews;
import android.widget.TextView;

public class WidgetConfigureActivity extends AppCompatActivity {

    // TODO: remove after dev complete, needed to deploy straight to activity for UI testing
    private static final boolean TEST = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(RESULT_CANCELED);

        Intent intent = getIntent();
        int id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if (!TEST) {
            if (id == AppWidgetManager.INVALID_APPWIDGET_ID) {
                finish();
                return;
            }
        }

        setContentView(R.layout.activity_widget_configure);

        Button button = (Button)findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCounter(v);
            }
        });
    }

    public void createCounter(View view) {
        Intent intent = getIntent();
        int id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        if (!TEST) {
            if (id == AppWidgetManager.INVALID_APPWIDGET_ID) {
                return;
            }
        }

        WidgetState initialState = parseConfiguration(id);
        initialState.persist(this);

        RemoteViews remoteViews = WidgetViewFactory.createRemoteViews(this, initialState);

        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(id, remoteViews);

        Intent result = new Intent();
        result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        setResult(RESULT_OK, result);
        finish();
    }

    private WidgetState parseConfiguration(int id) {
        EditText labelField = (EditText)findViewById(R.id.label_field);
        String label = labelField.getText().toString();

        int step = getIntOrDefault(R.id.step_field, 1);
        int count = getIntOrDefault(R.id.count_field, 0);

        return new WidgetState(id, label, step, count);
    }

    private int getIntOrDefault(int id, int def) {
        EditText et = (EditText)findViewById(id);
        int value = def;
        if (et != null) {
            Editable e = et.getText();
            if (e != null) {
                String s = e.toString();
                if (!s.isEmpty()) {
                    value = Integer.parseInt(s);
                }
            }
        }

        return value;
    }
}
