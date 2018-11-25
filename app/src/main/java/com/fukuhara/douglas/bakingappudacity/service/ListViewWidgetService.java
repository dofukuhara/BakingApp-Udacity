package com.fukuhara.douglas.bakingappudacity.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.fukuhara.douglas.bakingappudacity.R;
import com.fukuhara.douglas.bakingappudacity.recipes.model.Ingredient;
import com.fukuhara.douglas.bakingappudacity.recipes.model.WidgetIngredientModel;

import java.util.ArrayList;

/**
 * Created by dofukuhara on 22/01/2018.
 */

public class ListViewWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);

        ArrayList<Ingredient> ingredients = WidgetIngredientModel.retrieveWidgetIngredientmodel(
                getApplicationContext(), widgetId);
        return new AppWidgetListView(this.getApplicationContext(), ingredients);
    }

    class AppWidgetListView implements RemoteViewsService.RemoteViewsFactory {

        private ArrayList<Ingredient> mIngredientList;
        private Context mContext;

        public AppWidgetListView(Context context, ArrayList<Ingredient> ingredients) {
            this.mContext = context;
            this.mIngredientList = ingredients;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return mIngredientList != null ? mIngredientList.size() : 0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_list_widget_item);

            StringBuilder sb = new StringBuilder();
            sb.append(mIngredientList.get(position).getQuantity());
            sb.append(" (");
            sb.append(mIngredientList.get(position).getMeasure());
            sb.append(")");
            view.setTextViewText(R.id.tv_ing_qty_widget, sb);

            view.setTextViewText(R.id.tv_ing_ingredient_widget, mIngredientList.get(position).getIngredient());

            return view;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
