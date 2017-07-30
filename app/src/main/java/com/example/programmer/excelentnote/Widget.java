package com.example.programmer.excelentnote;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Programmer on 24.07.2017.
 */

public class Widget extends AppWidgetProvider {

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        //Toast.makeText(, "Yes", Toast.LENGTH_LONG).show();

        SharedPreferences sp = context.getSharedPreferences("widget_preference", Context.MODE_PRIVATE);
        String text = sp.getString("widget_text", null);

        RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget);
        widgetView.setTextViewText(R.id.main_text_view, text);

        for(int widget_id : appWidgetIds) {
            // Обновляем виджет
            appWidgetManager.updateAppWidget(widget_id, widgetView);
            Intent configIntent = new Intent(context, MainActivity.class);
            configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
            configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widget_id);
            PendingIntent pIntent = PendingIntent.getActivity(context, widget_id, configIntent, 0);
            widgetView.setOnClickPendingIntent(R.id.main_text_view, pIntent);
        }
        // Конфигурационный экран (первая зона)


    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
//        for(int widget_id : appWidgetIds) {
//
//        }
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}
