package com.example.programmer.excelentnote;

import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DataBase dataBase;
    SQLiteDatabase workerWithDB;
    int widgetId = 5;
    Intent resultIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toast.makeText(getApplicationContext(), "YES", Toast.LENGTH_LONG).show();

        initializeButtonListener();
        initializeDataBase();
        initializeMainFrame();
        initializeWidgetId();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void widgetStart(String insert_data) {
        putToDataBase(insert_data);

        // Записываем значения с экрана в Preferences
        SharedPreferences sp = getSharedPreferences("widget_preference", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("widget_text", getFromDataBase());
        editor.commit();

        // положительный ответ
        setResult(RESULT_OK, resultIntent);

        finish();
    }

    private void fillMainEditText() {
        ((EditText)findViewById(R.id.main_edit_text)).setText(getFromDataBase());
    }

    private String getFromMainEditText() {
        return ((EditText)findViewById(R.id.main_edit_text)).getText().toString();
    }

    private void putToDataBase(String text) {
        ContentValues content = new ContentValues();
        content.put("value", text);
        if(getFromDataBase() != null) {
            workerWithDB.update("WidgetTable", content, "id = ?", new String[] {widgetId + ""});
        }
        else {
            content.put("id", widgetId);
            workerWithDB.insert("WidgetTable", null, content);
        }
    }

    private String getFromDataBase() {
        String result_data = null;
        int num_data_column, num_id_column;
        Cursor c = workerWithDB.query("WidgetTable", null, null, null, null, null, null);
        try {
            if(c.moveToFirst()) {
                do {
                    num_id_column = c.getColumnIndex("id");
                    if (c.getInt(num_id_column) == widgetId) {
                        num_data_column = c.getColumnIndex("value");
                        result_data = c.getString(num_data_column);

                    }
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "getDBException", Toast.LENGTH_LONG).show();
        }
        c.close();
        return result_data;
    }

    private void initializeWidgetId() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null) {
            widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            Toast.makeText(getApplicationContext(), "Ошибка Id виджета", Toast.LENGTH_LONG).show();
            finish();
        }
        else {
        }

        // формируем intent ответа
        resultIntent = new Intent();
        resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        // отрицательный ответ
        setResult(RESULT_CANCELED, resultIntent);
    }

    private void initializeDataBase() {
        dataBase = new DataBase(this);
        workerWithDB = dataBase.getWritableDatabase();
    }

    private void initializeMainFrame() {
        FrameLayout main_frame = (FrameLayout)findViewById(R.id.main_frame);
        FrameLayout separate_layout = new FrameLayout(this) {
            //вызываем, когда получим размеры фрейма с элементами
            @Override
            protected void onSizeChanged(int left, int top, int right, int bottom) {
                super.onSizeChanged(left, top, right, bottom);
                startFillingSeparate();
                fillMainEditText();
            }
        };
        separate_layout.setId(R.id.separate_layout);

        main_frame.addView(separate_layout);
    }

    private void startFillingSeparate() {
        int lines_count = 0, origin_height, new_height;
        EditText edit_text = (EditText)findViewById(R.id.main_edit_text);
        FrameLayout separate_layout = (FrameLayout) findViewById(R.id.separate_layout);

        origin_height = edit_text.getHeight();
        new_height = findViewById(R.id.test_text_view).getHeight();
        new_height = new_height - origin_height;

        lines_count = separate_layout.getHeight() / new_height;

        fillingSeparate(separate_layout, 0, origin_height, 1);
        fillingSeparate(separate_layout, 1, new_height, lines_count);
    }

    private void fillingSeparate(FrameLayout separate_layout, int begin_pos, int separate_margin, int lines_count) {
        int current_pos = separate_margin * (begin_pos + 1);

        for(int i = begin_pos; i < lines_count; ++i) {
            ImageView image_view = new ImageView(this);
            image_view.setBackground(getResources().getDrawable(R.drawable.separate));

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            params.topMargin = current_pos;

            FrameLayout frame_lay = new FrameLayout(this);
            frame_lay.setLayoutParams(params);
            frame_lay.addView(image_view);

            separate_layout.addView(frame_lay);
            current_pos += separate_margin;
        }
    }

    private void initializeButtonListener() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String insert_data = ((EditText)findViewById(R.id.main_edit_text)).getText().toString();
            if(insert_data != null && insert_data != "") {
                widgetStart(insert_data);

            }
            }
        };
        findViewById(R.id.button_ok).setOnClickListener(onClickListener);
    }
}
