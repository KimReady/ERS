package com.naver.ers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button logBtn;
    private Button crashBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logBtn = findViewById(R.id.log_button);
        crashBtn = findViewById(R.id.crash_button);

        logBtn.setOnClickListener(buttonListener);
        crashBtn.setOnClickListener(buttonListener);
    }

    Button.OnClickListener buttonListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == logBtn.getId()) {
                Reporter.log.w("Fake Error", "Log", new Throwable("Mock Throwable"));
            } else if(v.getId() == crashBtn.getId()) {
                throw new AssertionError("Test Crash");
            }
        }
    };
}
