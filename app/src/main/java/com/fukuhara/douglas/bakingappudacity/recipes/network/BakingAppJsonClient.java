package com.fukuhara.douglas.bakingappudacity.recipes.network;

import com.fukuhara.douglas.bakingappudacity.recipes.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by dofukuhara on 16/01/2018.
 */

public interface BakingAppJsonClient {

    @GET("/android-baking-app-json")
    Call<ArrayList<Recipe>> listOfRecipes();

}
