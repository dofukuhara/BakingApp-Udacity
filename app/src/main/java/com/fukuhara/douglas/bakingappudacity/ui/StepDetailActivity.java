package com.fukuhara.douglas.bakingappudacity.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.fukuhara.douglas.bakingappudacity.R;
import com.fukuhara.douglas.bakingappudacity.recipes.model.Ingredient;
import com.fukuhara.douglas.bakingappudacity.recipes.model.Step;
import com.fukuhara.douglas.bakingappudacity.utils.Const;

import java.util.ArrayList;

/**
 * An activity representing a single Step detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link StepListActivity}.
 */
public class StepDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();

            Intent intent = getIntent();
            if(intent.hasExtra(Const.BUNDLE_INGREDIENT_ARRAY_LIST_PARCELABLE)) {

                arguments.putParcelableArrayList(Const.BUNDLE_INGREDIENT_ARRAY_LIST_PARCELABLE,
                        intent.getParcelableArrayListExtra(Const.BUNDLE_INGREDIENT_ARRAY_LIST_PARCELABLE));

            } else if (intent.hasExtra(Const.BUNDLE_STEP_ID) &&
                    intent.hasExtra(Const.BUNDLE_STEP_ARRAY_LIST_PARCELABLE)) {

                arguments.putInt(Const.BUNDLE_STEP_ID,
                        intent.getIntExtra(Const.BUNDLE_STEP_ID, 0));
                arguments.putParcelableArrayList(Const.BUNDLE_STEP_ARRAY_LIST_PARCELABLE,
                        intent.getParcelableArrayListExtra(Const.BUNDLE_STEP_ARRAY_LIST_PARCELABLE));

            }

            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_detail_container, fragment)
                    .commit();

        }
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
            NavUtils.navigateUpTo(this, new Intent(this, StepListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
