package com.fukuhara.douglas.bakingappudacity.recipes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fukuhara.douglas.bakingappudacity.R;
import com.fukuhara.douglas.bakingappudacity.recipes.model.Ingredient;
import com.fukuhara.douglas.bakingappudacity.recipes.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dofukuhara on 17/01/2018.
 */

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.StepListViewHolder> {

    private ArrayList<Ingredient> mIngredientList;
    private ArrayList<Step> mStepList;
    private StepClickListener mStepClickListener;
    private IngredientClickListener mIngredientClickListener;

    public interface StepClickListener {
        void onStepItemClick(int stepId);
    }

    public interface IngredientClickListener {
        void onIngredientClick();
    }

    public StepListAdapter(StepClickListener listener) {
        mIngredientList = new ArrayList<>();
        mStepList = new ArrayList<>();
        mStepClickListener = listener;
        mIngredientClickListener = (IngredientClickListener) listener;
    }

    public void setStepList(ArrayList<Step> list) {
        mStepList = list;
    }

    public void setIngredientList(ArrayList<Ingredient> list) {
        mIngredientList = list;
    }

    @Override
    public StepListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutIdForListItem = R.layout.step_list_item;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new StepListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepListViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mStepList != null ? mStepList.size() + 1 : 0;
    }

    class StepListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_step_name)
        TextView mTvStepName;

        Context mContext;

        public StepListViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();

            itemView.setOnClickListener(this);
        }

        void bind(int stepItemIndex) {
            if (stepItemIndex == 0) {
                mTvStepName.setText(mContext.getString(R.string.ingredient_list));
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(mStepList.get(stepItemIndex-1).getId());
                sb.append(" - ");
                sb.append(mStepList.get(stepItemIndex-1).getShortDescription());
                mTvStepName.setText(sb);
            }
        }

        @Override
        public void onClick(View view) {
            int clickPosition = getAdapterPosition();

            if (clickPosition == 0) {
                mIngredientClickListener.onIngredientClick();
            } else {
                mStepClickListener.onStepItemClick(mStepList.get(clickPosition - 1).getId());
            }
        }
    }
}
