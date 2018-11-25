package com.fukuhara.douglas.bakingappudacity.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.fukuhara.douglas.bakingappudacity.R;
import com.fukuhara.douglas.bakingappudacity.recipes.adapter.RecipeListAdapter;
import com.fukuhara.douglas.bakingappudacity.recipes.model.Recipe;
import com.fukuhara.douglas.bakingappudacity.utils.Const;
import com.fukuhara.douglas.bakingappudacity.utils.NetworkUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesListActivity extends AppCompatActivity implements RecipeListAdapter.RecipeClickListener {

    @BindView(R.id.rv_recipes_list)
    RecyclerView mRecipesRecyclerView;

    @BindView(R.id.pb_recipes_list)
    ProgressBar mRecipesProgressBar;

    private RecipeListAdapter mRecipeListAdapter;

    private ArrayList<Recipe> mRecipesList;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mRecipesList != null) {
            outState.putParcelableArrayList(Const.BUNDLE_RECIPES_ARRAY_LIST_PARCELABLE, mRecipesList);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);

        ButterKnife.bind(this);

        initializeLayoutParams();

        if (savedInstanceState == null || !savedInstanceState.containsKey(Const.BUNDLE_RECIPES_ARRAY_LIST_PARCELABLE)) {
            getJsonRecipeList();
        } else {
            mRecipesList = savedInstanceState.getParcelableArrayList(Const.BUNDLE_RECIPES_ARRAY_LIST_PARCELABLE);
            mRecipeListAdapter.setRecipesList(mRecipesList);
            mRecipesRecyclerView.setAdapter(mRecipeListAdapter);
        }
    }

    private void initializeLayoutParams() {

        mRecipesRecyclerView.setHasFixedSize(true);

        if (getResources().getBoolean(R.bool.isTablet)) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this,
                    getResources().getInteger(R.integer.number_of_columns));
            mRecipesRecyclerView.setLayoutManager(gridLayoutManager);

        } else {
            mRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false));
        }

        mRecipeListAdapter = new RecipeListAdapter(this);
    }

    private void getJsonRecipeList() {
        mRecipesProgressBar.setVisibility(View.VISIBLE);

       Call<ArrayList<Recipe>> call = NetworkUtils.buildRetroFitRequestForRecipes(
                getString(R.string.base_baking_app_json_URL));

        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                mRecipesProgressBar.setVisibility(View.GONE);

                mRecipesList = response.body();

                mRecipeListAdapter.setRecipesList(mRecipesList);
                mRecipesRecyclerView.setAdapter(mRecipeListAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                mRecipesProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onRecipeItemClick(Recipe recipe) {
        Intent intent = new Intent(this, StepListActivity.class);
        intent.putExtra(Const.RECIPE_PARCELABLE_EXTRA, recipe);
        startActivity(intent);

    }
}
