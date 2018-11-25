package com.fukuhara.douglas.bakingappudacity.recipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dofukuhara on 16/01/2018.
 */

public class StepList implements Parcelable {

    @SerializedName("steps")
    private ArrayList<Step> steps;

    protected StepList(Parcel in) {
        steps = in.createTypedArrayList(Step.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(steps);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StepList> CREATOR = new Creator<StepList>() {
        @Override
        public StepList createFromParcel(Parcel in) {
            return new StepList(in);
        }

        @Override
        public StepList[] newArray(int size) {
            return new StepList[size];
        }
    };
}
