package ru.sancha.chat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.github.library.bubbleview.BubbleTextVew;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import android.text.format.DateFormat;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "LOG_SANYA";
    private static int SING_IN_CODE = 1;
    private RelativeLayout activity_main;
    private FirebaseListAdapter<Message> adapter, adapter_cat;
    private final String FareBaseLink = "https://chat-7efa2-default-rtdb.europe-west1.firebasedatabase.app";
    private String category;
    private final String APP_PREFERENCES = "Settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, Starter.class);
        startActivity(intent);

        activity_main = findViewById(R.id.activity_main);
        FloatingActionButton sendBtn = findViewById(R.id.btnSend);

        // Кнопка для смены чата
        FloatingActionButton btnFl = findViewById(R.id.fab);
        btnFl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaySettings();
            }
        });

        SharedPreferences mySharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        category = mySharedPreferences.getString("category", "");

        // Действия при нажатии на кнопку
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText textFiled = findViewById(R.id.messageField);
                if (textFiled.getText().toString().length() <= 0) {
                    Snackbar.make(activity_main, "Пусто - негусто)", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                // отправка сообщения в БД Фаербазе
                FirebaseDatabase.getInstance(FareBaseLink).getReference().push().setValue(
                        new Message(
                                FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                textFiled.getText().toString(),
                                category
                        )
                );
                textFiled.setText("");
            }
        });

        // не зареган пользователь
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
            // регестрирует пользователя
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SING_IN_CODE);
        else {
            category = mySharedPreferences.getString("category", "");
            if (category.equals("")) displaySettings();
            else {
                Snackbar.make(activity_main, "Мы Вас узнали)\n" + "Вы выбрали: " + category, Snackbar.LENGTH_SHORT).show();
                displayAllMessages();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SING_IN_CODE) {
            if (resultCode == RESULT_OK) {
                Snackbar.make(activity_main, "Регистрация прошла успешно)", Snackbar.LENGTH_LONG).show();
                SharedPreferences mySharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                if (mySharedPreferences.getString("category", "").equals("")) displaySettings();
                displayAllMessages();
            } else {
                Snackbar.make(activity_main, "К сожалению вы не зарегестрированы :(", Snackbar.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void displaySettings() {
        Intent intent = new Intent(MainActivity.this, Settings.class);
        startActivity(intent);
    }

    // Вывод сообщений на экран
    private void displayAllMessages() {
        ListView listOfMessages = findViewById(R.id.list_of_messages);

        Query query = FirebaseDatabase.getInstance(FareBaseLink).getReference();

        FirebaseListOptions<Message> options = new FirebaseListOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLayout(R.layout.list_item)
                .build();

        adapter = new FirebaseListAdapter<Message>(options) {
            @Override
            protected void populateView(View v, Message message, int position) {
                TextView mess_user, mess_time;
                BubbleTextVew mess_text;
                mess_user = v.findViewById(R.id.message_user);
                mess_time = v.findViewById(R.id.message_time);
                mess_text = v.findViewById(R.id.message_text);

                // Для фильтрации старых без категории
                if (message.getCategory() == null) message.setCategory("");

                // Фильтруем по выбранной категории
                if (!message.getCategory().equals(category)) {
                    mess_user.setVisibility(v.GONE);
                    mess_text.setVisibility(v.GONE);
                    mess_time.setVisibility(v.GONE);
//                    adapter.notifyDataSetChanged();
                } else {
//                Log.e("LOG_SANYA", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

                    mess_user.setText(message.getUserName());
                    mess_text.setText(message.getCategory() + " : " + message.getTextMessage());
                    mess_time.setText(DateFormat.format("dd-mm-yyyy HH:mm:ss", message.getMessageTime()));
                }

//                Log.e("LOG_SANYA", model.getUserName() + model.getTextMessage() + DateFormat.format("dd-mm-yyyy HH:mm:ss", model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        adapter.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        if (adapter != null) adapter.stopListening();
        SharedPreferences mySharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        category = mySharedPreferences.getString("category", "");
        displayAllMessages();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) adapter.stopListening();
    }

}