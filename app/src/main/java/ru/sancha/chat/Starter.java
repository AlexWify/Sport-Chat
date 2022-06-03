package ru.sancha.chat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class Starter extends AppCompatActivity {

    private static Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        // Настраиваем таймер закрытия окна
        timer = new Timer();
        TimerTask timerWinClose = new TimerTask() {
            @Override
            public void run() {
                finish();
            }
        };

        // Запускаем таймер закрытия окна
        timer.schedule(timerWinClose, 3000);
    }

}
