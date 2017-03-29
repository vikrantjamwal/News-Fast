package com.app.vik.newsfast.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.app.vik.newsfast.MainActivity;
import com.app.vik.newsfast.R;

/**
 * Implementation of App Widget functionality.
 */
public class CollectionWidget extends AppWidgetProvider {

    SharedPreferences sharedPrefer;

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.collection_widget);

        Intent launchMain = new Intent(context, MainActivity.class);
        PendingIntent pendingMainIntent = PendingIntent.getActivity(context, 0, launchMain, 0);
        views.setOnClickPendingIntent(R.id.widget, pendingMainIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            setRemoteAdapter(context, views);
        }

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int i=0; i<appWidgetIds.length; i++) {
            sharedPrefer=context.getSharedPreferences("TaskWidget", 0);
            SharedPreferences.Editor editor=sharedPrefer.edit();
            editor.putInt("widget_key", appWidgetIds[i]);
            editor.commit();

            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();

        if(action.equals("android.appwidget.action.APPWIDGET_UPDATE")){
            int[] appid=new int[1];
            sharedPrefer=context.getSharedPreferences("TaskWidget", 0);
            appid[0]=sharedPrefer.getInt("widget_key",0);
            onUpdate(context, AppWidgetManager.getInstance(context),appid);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_list,
                new Intent(context, WidgetService.class));
    }

}

