package com.example.programmer.excelentnote;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import static com.example.programmer.excelentnote.R.layout.widget;

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

        SharedPreferences share_data = context.getSharedPreferences("widget_preference", Context.MODE_PRIVATE);

        for(int widget_id : appWidgetIds) {
            updateWidget(context, appWidgetManager, share_data, widget_id);
        }
    }

    public static void updateWidget(Context context, AppWidgetManager manager, SharedPreferences share_data, int widget_id) {
        String text_data = share_data.getString(widget_id + "", null);
        RemoteViews widget_view = new RemoteViews(context.getPackageName(), widget);
        widget_view.setTextViewText(R.id.main_text_view, text_data);

        //обработка нажатия на виджет
        Intent configIntent = new Intent(context, MainActivity.class);
        configIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
        configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widget_id);
        PendingIntent pIntent = PendingIntent.getActivity(context, widget_id, configIntent, 0);
        widget_view.setOnClickPendingIntent(R.id.main_text_view, pIntent);

        //обновление виджета
        manager.updateAppWidget(widget_id, widget_view);

    }
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}
