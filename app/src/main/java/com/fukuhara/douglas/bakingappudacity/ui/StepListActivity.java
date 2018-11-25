package com.fukuhara.douglas.bakingappudacity.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.fukuhara.douglas.bakingappudacity.R;
import com.fukuhara.douglas.bakingappudacity.recipes.adapter.StepListAdapter;
import com.fukuhara.douglas.bakingappudacity.recipes.model.Recipe;
import com.fukuhara.douglas.bakingappudacity.recipes.model.Step;
import com.fukuhara.douglas.bakingappudacity.utils.Const;

/**
 * An activity representing a list of Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepListActivity extends AppCompatActivity
        implements StepListAdapter.StepClickListener, StepListAdapter.IngredientClickListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private Recipe mRecipe;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mRecipe != null) {
            outState.putParcelable(Const.BUNDLE_PARCELABLE_EXTRA, mRecipe);
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        if (savedInstanceState != null && savedInstanceState
                .containsKey(Const.BUNDLE_PARCELABLE_EXTRA)) {
            mRecipe = savedInstanceState.getParcelable(Const.BUNDLE_PARCELABLE_EXTRA);
        } else {
            Intent intent = getIntent();
            if (intent.hasExtra(Const.RECIPE_PARCELABLE_EXTRA)) {
                mRecipe = intent.getParcelableExtra(Const.RECIPE_PARCELABLE_EXTRA);

            } else {
                // TODO - persist recipe after come back from step/ingredient detail
            }

        }

        setTitle(mRecipe.getName());

        View recyclerView = findViewById(R.id.step_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        if (mRecipe != null ) {
            StepListAdapter adapter = new StepListAdapter(this);
            adapter.setStepList(mRecipe.getSteps());
            adapter.setIngredientList(mRecipe.getIngredients());
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onStepItemClick(int stepId) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putInt(Const.BUNDLE_STEP_ID, stepId);
            args.putParcelableArrayList(Const.BUNDLE_STEP_ARRAY_LIST_PARCELABLE, mRecipe.getSteps());

            try {
                Fragment fragment = StepDetailFragment.class.newInstance();
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.step_detail_container, fragment)
                        .commit();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra(Const.BUNDLE_STEP_ID, stepId);
            intent.putParcelableArrayListExtra(Const.BUNDLE_STEP_ARRAY_LIST_PARCELABLE, mRecipe.getSteps());

            startActivity(intent);
        }
    }

    @Override
    public void onIngredientClick() {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelableArrayList(Const.BUNDLE_INGREDIENT_ARRAY_LIST_PARCELABLE, mRecipe.getIngredients());

            try {
                Fragment fragment = StepDetailFragment.class.newInstance();
                fragment.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.step_detail_container, fragment)
                        .commit();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            Intent intent = new Intent (this, StepDetailActivity.class);
            intent.putParcelableArrayListExtra(Const.BUNDLE_INGREDIENT_ARRAY_LIST_PARCELABLE, mRecipe.getIngredients());

            startActivity(intent);
        }
    }

}
