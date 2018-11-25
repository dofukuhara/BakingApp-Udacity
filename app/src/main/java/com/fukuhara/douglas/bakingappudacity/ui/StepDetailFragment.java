package com.fukuhara.douglas.bakingappudacity.ui;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fukuhara.douglas.bakingappudacity.R;
import com.fukuhara.douglas.bakingappudacity.recipes.adapter.IngredientListAdapter;
import com.fukuhara.douglas.bakingappudacity.recipes.model.Ingredient;
import com.fukuhara.douglas.bakingappudacity.recipes.model.Step;
import com.fukuhara.douglas.bakingappudacity.utils.Const;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A fragment representing a single Step detail screen.
 * This fragment is either contained in a {@link StepListActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment {

    private ArrayList<Ingredient> mIngredientArrayList;
    private ArrayList<Step> mStepArrayList;
    private int mStepId;
    private Step mStep;

    private boolean isStep = false;

    private Context mContext;

    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;
    private long mPlaybackPosition;
    private int mCurrentWindow;
    private boolean mPlayWhenReady = true;
    private String mVideoURL;
    private Button mButtonPreviousStep;
    private Button mButtonNextStep;
    private ImageView mIvStepThumb;
    private boolean mSavePlayerInstanceAfterRelease = false;

    private StepDetailFragment mStepDetailFragment;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // if mPlayer is not null, it means that 'onSaveInstanceState' was executed before
        // onPause/onStop -> releasePlayer()
        // In this case, all "class variable" player controls are NULL, so we need to
        // acquire these values from the player
        if (mPlayer != null) {
            outState.putLong(Const.BUNDLE_PLAYBACK_POSITION, mPlayer.getCurrentPosition());
            outState.putInt(Const.BUNDLE_CURRENT_WINDOW, mPlayer.getCurrentWindowIndex());
            outState.putBoolean(Const.BUNDLE_IS_PLAYING_VIDEO, mPlayer.getPlayWhenReady());
        } else {
            // if mPlayer is NULL it may be due to 2 reasons:
            // 1- The player was created but onPause was executed before onSaveInstanceState, which will release the player and clear this instance
            // 2- The player was not even created. E.g: the step does not have a Video content.

            // To check if the 1st statement is true, a boolean variable was created to know if can or cannot store the class variable into the bundle
            if (mSavePlayerInstanceAfterRelease) {
                outState.putLong(Const.BUNDLE_PLAYBACK_POSITION, mPlaybackPosition);
                outState.putInt(Const.BUNDLE_CURRENT_WINDOW, mCurrentWindow);
                outState.putBoolean(Const.BUNDLE_IS_PLAYING_VIDEO, mPlayWhenReady);
            }

        }
        if (isStep) {
            outState.putBoolean(Const.BUNDLE_IS_STEP, isStep);
            outState.putInt(Const.BUNDLE_STEP_ID, mStepId);
        }
    }

    public StepDetailFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isStep && Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isStep && (Util.SDK_INT <= 23 || mPlayer == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (isStep && Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isStep && Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void initializePlayer() {
        if (mPlayer == null && !TextUtils.isEmpty(mVideoURL)) {
            mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector());

            mPlayer.seekTo(mCurrentWindow, mPlaybackPosition);
            mPlayer.setPlayWhenReady(mPlayWhenReady);

            MediaSource mediaSource = buildMediaSource(Uri.parse(mVideoURL));
            mPlayer.prepare(mediaSource, true, false);

            mPlayerView.setPlayer(mPlayer);
        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mSavePlayerInstanceAfterRelease = true;
            mPlaybackPosition = mPlayer.getCurrentPosition();
            mCurrentWindow = mPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        String userAgent = Util.getUserAgent(getContext(), getContext().getApplicationInfo().name);
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(userAgent)).
                createMediaSource(uri);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStepDetailFragment = this;

        Bundle bundle = getArguments();
        String title;

        if (bundle.containsKey(Const.BUNDLE_INGREDIENT_ARRAY_LIST_PARCELABLE)) {
            mIngredientArrayList = bundle.getParcelableArrayList(
                    Const.BUNDLE_INGREDIENT_ARRAY_LIST_PARCELABLE);

            title = getString(R.string.ingredient_list);

        } else {
            isStep = true;

            mStepArrayList = bundle.getParcelableArrayList(
                    Const.BUNDLE_STEP_ARRAY_LIST_PARCELABLE);
            mStepId = bundle.getInt(Const.BUNDLE_STEP_ID);

            mStep = mStepArrayList.get(mStepId);
            title = mStep.getShortDescription();
        }

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(title);
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(Const.BUNDLE_PLAYBACK_POSITION)) {
                mPlaybackPosition = savedInstanceState.getLong(Const.BUNDLE_PLAYBACK_POSITION);
            }
            if (savedInstanceState.containsKey(Const.BUNDLE_CURRENT_WINDOW)) {
                mCurrentWindow = savedInstanceState.getInt(Const.BUNDLE_CURRENT_WINDOW);
            }
            if (savedInstanceState.containsKey(Const.BUNDLE_IS_PLAYING_VIDEO)) {
                mPlayWhenReady = savedInstanceState.getBoolean(Const.BUNDLE_IS_PLAYING_VIDEO);
            }
            if (savedInstanceState.containsKey(Const.BUNDLE_IS_STEP)) {
                isStep = savedInstanceState.getBoolean(Const.BUNDLE_IS_STEP);
            }
            if (savedInstanceState.containsKey(Const.BUNDLE_STEP_ID)) {
                mStepId = savedInstanceState.getInt(Const.BUNDLE_STEP_ID);
            }
            if (savedInstanceState.containsKey(Const.BUNDLE_IS_STEP)) {
                isStep = savedInstanceState.getBoolean(Const.BUNDLE_IS_STEP);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;

        if (isStep) {
            rootView = createStepView(inflater, container);
        } else {
            rootView = createIngredientView(inflater, container);
        }

       return rootView;
    }

    private View createIngredientView(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_ingredient_list, container, false);
        mContext = rootView.getContext();

        RecyclerView rvIngredientList = rootView.findViewById(R.id.rv_ingredient_list);

        IngredientListAdapter adapter = new IngredientListAdapter(mIngredientArrayList);

        rvIngredientList.setHasFixedSize(true);
        rvIngredientList.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false));
        rvIngredientList.setAdapter(adapter);

        return rootView;
    }

    private View createStepView(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        mContext = rootView.getContext();

        TextView tvRecipeDescription = rootView.findViewById(R.id.tv_recipe_step);
        tvRecipeDescription.setText(mStep.getDescription());

        mButtonPreviousStep = rootView.findViewById(R.id.btn_previous_step);
        mButtonNextStep = rootView.findViewById(R.id.btn_next_step);

        mIvStepThumb = rootView.findViewById(R.id.iv_step_thumbnail);

        mPlayerView = rootView.findViewById(R.id.epStepVideo);
        if (!TextUtils.isEmpty(mStep.getVideoURL())) {
            mVideoURL = mStep.getVideoURL();
            mIvStepThumb.setVisibility(View.GONE);
        } else if (!TextUtils.isEmpty(mStep.getThumbnailURL())) {
            mPlayerView.setVisibility(View.GONE);

            Picasso.Builder builder = new Picasso.Builder(mContext);
            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    // In case that Picasso could not load the given image, hide the view
                    mIvStepThumb.setVisibility(View.GONE);
                }
            });
            builder.build().load(mStep.getThumbnailURL()).into(mIvStepThumb);
        } else {
            mPlayerView.setVisibility(View.GONE);
            mIvStepThumb.setVisibility(View.GONE);
        }

        // Controlling Previous and Next Button display in the UI
        if (mStepId == 0) {
            mButtonPreviousStep.setVisibility(View.GONE);
        }else {
            mButtonPreviousStep.setVisibility(View.VISIBLE);
            createPreviousButtonListener();
        }
        if (mStepId == mStepArrayList.size() - 1) {
            mButtonNextStep.setVisibility(View.GONE);
        } else {
            mButtonNextStep.setVisibility(View.VISIBLE);
            createNextButtonListener();
        }

        return rootView;
    }

    private void createPreviousButtonListener() {
        mButtonPreviousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStepId = mStepId - 1;

                Bundle arguments = new Bundle();
                arguments.putInt(Const.BUNDLE_STEP_ID,
                        mStepId);
                arguments.putParcelableArrayList(Const.BUNDLE_STEP_ARRAY_LIST_PARCELABLE,
                        mStepArrayList);

                StepDetailFragment fragment = new StepDetailFragment();
                fragment.setArguments(arguments);
                getFragmentManager().beginTransaction()
                        .replace(R.id.step_detail_container, fragment)
                        .commit();

            }
        });
    }

    private void createNextButtonListener() {
        mButtonNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStepId = mStepId + 1;

                Bundle arguments = new Bundle();
                arguments.putInt(Const.BUNDLE_STEP_ID,
                        mStepId);
                arguments.putParcelableArrayList(Const.BUNDLE_STEP_ARRAY_LIST_PARCELABLE,
                        mStepArrayList);

                StepDetailFragment fragment = new StepDetailFragment();
                fragment.setArguments(arguments);
                getFragmentManager().beginTransaction()
                        .replace(R.id.step_detail_container, fragment)
                        .commit();

            }
        });
    }
}
