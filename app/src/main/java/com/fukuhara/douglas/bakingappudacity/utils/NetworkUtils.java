package com.fukuhara.douglas.bakingappudacity.utils;

import com.fukuhara.douglas.bakingappudacity.R;
import com.fukuhara.douglas.bakingappudacity.recipes.model.Recipe;
import com.fukuhara.douglas.bakingappudacity.recipes.network.BakingAppJsonClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dofukuhara on 22/01/2018.
 */

public final class NetworkUtils {
    public static Call<ArrayList<Recipe>> buildRetroFitRequestForRecipes(String url) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        BakingAppJsonClient client = retrofit.create(BakingAppJsonClient.class);

        return client.listOfRecipes();
    }
}
