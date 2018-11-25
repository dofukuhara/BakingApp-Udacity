package com.fukuhara.douglas.bakingappudacity.recipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dofukuhara on 16/01/2018.
 */

public class Ingredient implements Parcelable{

    public Ingredient(Parcel in) {
        quantity = in.readString();
        measure = in.readString();
        ingredient = in.readString();
    }

    public Ingredient(String quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    @SerializedName("quantity")
    private String quantity;

    @SerializedName("measure")
    private String measure;

    @SerializedName("ingredient")
    private String ingredient;

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }

}
