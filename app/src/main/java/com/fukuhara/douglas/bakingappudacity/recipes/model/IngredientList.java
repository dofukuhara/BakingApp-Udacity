package com.fukuhara.douglas.bakingappudacity.recipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dofukuhara on 16/01/2018.
 */

public class IngredientList implements Parcelable{

    @SerializedName("ingredients")
    private ArrayList<Ingredient> ingredients;

    protected IngredientList(Parcel in) {
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
    }

    public static final Creator<IngredientList> CREATOR = new Creator<IngredientList>() {
        @Override
        public IngredientList createFromParcel(Parcel in) {
            return new IngredientList(in);
        }

        @Override
        public IngredientList[] newArray(int size) {
            return new IngredientList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(ingredients);
    }
}
