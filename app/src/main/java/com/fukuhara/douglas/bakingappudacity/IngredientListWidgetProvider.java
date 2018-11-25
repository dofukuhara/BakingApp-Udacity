package com.fukuhara.douglas.bakingappudacity;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.fukuhara.douglas.bakingappudacity.recipes.model.WidgetIngredientModel;
import com.fukuhara.douglas.bakingappudacity.service.ListViewWidgetService;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link IngredientListWidgetProviderConfigureActivity IngredientListWidgetProviderConfigureActivity}
 */
public class IngredientListWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        String widgetText = WidgetIngredientModel.retrieveWidgetRecipeName(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_list_widget_provider);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        Intent intent = new Intent(context, ListViewWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        views.setRemoteAdapter(R.id.lv_ingredient_list_in_widget, intent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            //IngredientListWidgetProviderConfigureActivity.deleteTitlePref(context, appWidgetId);
            IngredientListWidgetProviderConfigureActivity.deleteIngredientListFromPref(context, appWidgetId);
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
}

