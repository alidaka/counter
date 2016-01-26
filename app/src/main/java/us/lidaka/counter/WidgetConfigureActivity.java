package us.lidaka.counter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by augustus on 1/25/16.
 */
public class WidgetConfigureActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView tv = (TextView)findViewById(R.id.widget_label);
        tv.setText("Hello, widget!");
        // TODO: attach onClick

        Button counter = (Button)findViewById(R.id.count_value);
        counter.setText("0");
        // TODO: attach onClick

        Button decrementer = (Button)findViewById(R.id.decrement_button);
        // TODO: attach onClick
    }
}
