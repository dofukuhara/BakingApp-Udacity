package com.fukuhara.douglas.bakingappudacity.recipes.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fukuhara.douglas.bakingappudacity.R;
import com.fukuhara.douglas.bakingappudacity.recipes.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dofukuhara on 16/01/2018.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder>{

    private ArrayList<Recipe> mRecipesList;
    private RecipeClickListener mRecipeClickListener;
    private Context mContext;

    public RecipeListAdapter(RecipeClickListener listener) {
        mRecipesList = new ArrayList<>();
        mRecipeClickListener = listener;
    }

    public void setRecipesList(ArrayList<Recipe> list) {
        mRecipesList = list;
    }

    @Override
    public RecipeListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();

        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new RecipeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeListViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mRecipesList != null ? mRecipesList.size() : 0;
    }

    public interface RecipeClickListener {
        void onRecipeItemClick(Recipe recipe);
    }

    class RecipeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_recipe_name)
        TextView mTvRecipeName;

        @BindView(R.id.iv_recipe_thumb)
        ImageView mIvRecipeThumb;

        public RecipeListViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        void bind(int recipeItemIndex) {
            mTvRecipeName.setText(mRecipesList.get(recipeItemIndex).getName());

            if (!TextUtils.isEmpty(mRecipesList.get(recipeItemIndex).getImage())) {
                Picasso.with(mContext)
                        .load(mRecipesList.get(recipeItemIndex).getImage())
                        .error(R.mipmap.ic_cake)
                        .into(mIvRecipeThumb);
            }
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mRecipeClickListener.onRecipeItemClick(mRecipesList.get(clickedPosition));
        }
    }
}
