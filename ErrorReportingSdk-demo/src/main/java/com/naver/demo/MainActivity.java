package com.naver.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.naver.ers.LogLevel;
import com.naver.ers.Reporter;

public class MainActivity extends AppCompatActivity {
    private EditText tagText;
    private EditText msgText;
    private Button logBtn;
    private Button crashBtn;
    private Spinner logLevelSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tagText = findViewById(R.id.tag_edit);
        msgText = findViewById(R.id.msg_edit);
        logBtn = findViewById(R.id.log_button);
        crashBtn = findViewById(R.id.crash_button);
        logLevelSpinner = findViewById(R.id.log_level_spinner);
        logLevelSpinner.setAdapter(makeAdapter(R.array.log_level));

        logBtn.setOnClickListener(buttonListener);
        crashBtn.setOnClickListener(buttonListener);

    }

    Button.OnClickListener buttonListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == logBtn.getId()) {
                String tag = tagText.getText().toString();
                String msg = msgText.getText().toString();
                Throwable tr = new Throwable("Test Log");
                LogLevel logLevel = LogLevel.valueOf(String.valueOf(logLevelSpinner.getSelectedItem()));
                switch(logLevel) {
                    case VERBOSE:
                        Reporter.log.v(tag, msg, tr);
                        break;
                    case DEBUG:
                        Reporter.log.d(tag, msg, tr);
                        break;
                    case INFO:
                        Reporter.log.i(tag, msg, tr);
                        break;
                    case WARNING:
                        Reporter.log.w(tag, msg, tr);
                        break;
                    case ERROR:
                        Reporter.log.e(tag, msg, tr);
                        break;
                    case ASSERT:
                        Reporter.log.wtf(tag, msg, tr);
                        break;
                }
                Toast.makeText(getBaseContext(), "logging", Toast.LENGTH_SHORT).show();
            } else if(v.getId() == crashBtn.getId()) {
                throw new AssertionError("Test Crash");
            }
        }
    };

    /**
     * Spinner Adapter
     */
    private ArrayAdapter<CharSequence> makeAdapter(int items) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                items,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return adapter;
    }
}
