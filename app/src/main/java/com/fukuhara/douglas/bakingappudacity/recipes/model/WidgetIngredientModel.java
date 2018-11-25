package com.fukuhara.douglas.bakingappudacity.recipes.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.fukuhara.douglas.bakingappudacity.utils.Const;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dofukuhara on 23/01/2018.
 */

public class WidgetIngredientModel {

    public static void createWidgetIngredientModel(Context context,
                                                   ArrayList<Ingredient> ingredients,
                                                   int widigetId,
                                                   String recipeName) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                Const.SHARED_INGREDIENTS_PREFERENCES, Context.MODE_PRIVATE);

        JSONArray jsonArray;
        try {
            jsonArray=new JSONArray();

                for (int i = 0; i < ingredients.size(); i++) {
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put(Const.INGREDIENT_QUANTITY, ingredients.get(i).getQuantity());
                    jsonObject.put(Const.INGREDIENT_MEASURE, ingredients.get(i).getMeasure());
                    jsonObject.put(Const.INGREDIENT_INGREDIENT, ingredients.get(i).getIngredient());

                    jsonArray.put(jsonObject);
                }
                sharedPref.edit().putString(Const.SHARED_PREF_KEY_JSON + widigetId,
                        jsonArray.toString()).apply();
                sharedPref.edit().putString(Const.SHARED_PREF_KEY_RECIPE_NAME + widigetId,
                        recipeName).apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Ingredient> retrieveWidgetIngredientmodel(Context context, int widigetId) {
        ArrayList<Ingredient> arrayList = new ArrayList<>();

        SharedPreferences sharedPref = context.getSharedPreferences(
                Const.SHARED_INGREDIENTS_PREFERENCES, Context.MODE_PRIVATE);
        String json = sharedPref.getString(Const.SHARED_PREF_KEY_JSON + widigetId, "[]");

        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                Ingredient ingredient = new Ingredient(
                        object.getString(Const.INGREDIENT_QUANTITY),
                        object.getString(Const.INGREDIENT_MEASURE),
                        object.getString(Const.INGREDIENT_INGREDIENT)
                );

                arrayList.add(ingredient);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;
    }

    public static String retrieveWidgetRecipeName(Context context, int widgetId) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                Const.SHARED_INGREDIENTS_PREFERENCES, Context.MODE_PRIVATE);

        return sharedPref.getString(Const.SHARED_PREF_KEY_RECIPE_NAME + widgetId, "");
    }
}
