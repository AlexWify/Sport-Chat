package ru.sancha.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    private final String APP_PREFERENCES = "Settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        SharedPreferences mySharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        Button button_close = (Button) findViewById(R.id.button_close);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mySharedPreferences.getString("category", "").equals("")) {
                    finish();
//                    Intent intent = new Intent(Settings.this, MainActivity.class);
//                    startActivity(intent);
                }
            }
        });

        SharedPreferences.Editor editor = mySharedPreferences.edit();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case -1:
                        Toast.makeText(getApplicationContext(), "Ничего не выбрано",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButtonRed:
                        editor.putString("category", "Борьба");
                        editor.apply();
                        break;
                    case R.id.radioButtonGreen:
                        editor.putString("category", "Волейбол");
                        editor.apply();
                        break;
                    case R.id.radioButtonY:
                        editor.putString("category", "Баскетбол");
                        editor.apply();
                        break;
                    case R.id.radioButtonBl:
                        editor.putString("category", "Футбол");
                        editor.apply();
                        break;
                    case R.id.radioButtonWhite:
                        editor.putString("category", "Хоккей");
                        editor.apply();
                        break;
                    case R.id.radioButtonBlack:
                        editor.putString("category", "Шахматы");
                        editor.apply();
                        break;

                    default:
                        break;
                }
            }
        });

    }


}
