package com.fukuhara.douglas.bakingappudacity.recipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dofukuhara on 16/01/2018.
 */

public class Recipe implements Parcelable{

    private Recipe (Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.servings = in.readString();
        this.image = in.readString();
        this.ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        this.steps = in.createTypedArrayList(Step.CREATOR);
    }

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("ingredients")
    private ArrayList<Ingredient> ingredients;

    @SerializedName("steps")
    private ArrayList<Step> steps;

    @SerializedName("servings")
    private String servings;

    @SerializedName("image")
    private String image;

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public String getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(servings);
        parcel.writeString(image);
        parcel.writeTypedList(ingredients);
        parcel.writeTypedList(steps);
    }

    @Override
    public String toString() {
        return getName();
    }
}
