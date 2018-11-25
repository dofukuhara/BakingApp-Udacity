package com.fukuhara.douglas.bakingappudacity;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.fukuhara.douglas.bakingappudacity.recipes.model.Ingredient;
import com.fukuhara.douglas.bakingappudacity.recipes.model.Recipe;
import com.fukuhara.douglas.bakingappudacity.recipes.model.WidgetIngredientModel;
import com.fukuhara.douglas.bakingappudacity.utils.Const;
import com.fukuhara.douglas.bakingappudacity.utils.NetworkUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The configuration screen for the {@link IngredientListWidgetProvider IngredientListWidgetProvider} AppWidget.
 */
public class IngredientListWidgetProviderConfigureActivity extends Activity {

    private Spinner mSpRecipeChooser;
    private ProgressBar mPbRecipeLoading;
    private Context mContext;
    private Button mBtnCreateWidget;
    private Button mBtnCancelWidgetCreation;
    private ListView mIngredientListView;
    private ArrayList<Ingredient> mIngredient;

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    public IngredientListWidgetProviderConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.ingredient_list_widget_provider_configure);

        mContext = this;
        mSpRecipeChooser = findViewById(R.id.sp_recipe_chooser);
        mSpRecipeChooser.setEnabled(false);
        mPbRecipeLoading = findViewById(R.id.pb_widget_recipe_loading);
        mPbRecipeLoading.setVisibility(View.VISIBLE);
        mBtnCreateWidget = findViewById(R.id.btn_create_widget_operation);
        mBtnCancelWidgetCreation = findViewById(R.id.btn_cancel_widget_operation);
        mIngredientListView = findViewById(R.id.lv_ingredient_list_in_widget);

        mBtnCancelWidgetCreation.setOnClickListener(cancelWidgetOperation());

        Call<ArrayList<Recipe>> call = NetworkUtils.buildRetroFitRequestForRecipes(
                getString(R.string.base_baking_app_json_URL));

        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                mPbRecipeLoading.setVisibility(View.GONE);
                mSpRecipeChooser.setEnabled(true);

                ArrayAdapter<Recipe> arrayAdapter = new ArrayAdapter<Recipe>(mContext,
                        android.R.layout.simple_spinner_dropdown_item, response.body());
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                mSpRecipeChooser.setAdapter(arrayAdapter);

                mBtnCreateWidget.setOnClickListener(createWidgetOperation());
                mBtnCreateWidget.setEnabled(true);

            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                mPbRecipeLoading.setVisibility(View.GONE);
            }
        });

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }

    private View.OnClickListener cancelWidgetOperation() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, getString(R.string.widget_operation_canceled), Toast.LENGTH_SHORT).show();
                finish();
            }
        };
    }

    private View.OnClickListener createWidgetOperation() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe recipe = (Recipe) mSpRecipeChooser.getSelectedItem();
                mIngredient = recipe.getIngredients();

                WidgetIngredientModel.createWidgetIngredientModel(mContext, mIngredient, mAppWidgetId, recipe.getName());

                final Context context = IngredientListWidgetProviderConfigureActivity.this;

                // It is the responsibility of the configuration activity to update the app widget
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                IngredientListWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                // Make sure we pass back the original appWidgetId
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        };
    }

    static void deleteIngredientListFromPref(Context context, int appWidgetId) {
        SharedPreferences.Editor sharedPref = context.getSharedPreferences(
                Const.SHARED_INGREDIENTS_PREFERENCES, Context.MODE_PRIVATE).edit();

        sharedPref.remove(Const.SHARED_PREF_KEY_JSON + appWidgetId);
        sharedPref.apply();
    }
}

