package com.fukuhara.douglas.bakingappudacity.recipes.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fukuhara.douglas.bakingappudacity.R;
import com.fukuhara.douglas.bakingappudacity.recipes.model.Ingredient;
import com.fukuhara.douglas.bakingappudacity.recipes.model.IngredientList;

import java.util.ArrayList;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dofukuhara on 20/01/2018.
 */

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.IngredientListViewHolder>{

    private ArrayList<Ingredient> mIngList;

    public IngredientListAdapter() {
        mIngList = new ArrayList<>();
    }

    public IngredientListAdapter(ArrayList<Ingredient> ingList) {
        mIngList = ingList;
    }

    public void setIngredientList(ArrayList<Ingredient> ingList) {
        mIngList = ingList;
    }

    @Override
    public IngredientListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutIdForListItem = R.layout.ingredient_list_item;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new IngredientListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientListViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mIngList != null ? mIngList.size() : 0;
    }


    class IngredientListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_ing_quantity)
        TextView mIngQuantity;

        @BindView(R.id.tv_ing_ingredient)
        TextView mIngIngredient;

        public IngredientListViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind(int listIdx) {
            StringBuilder sb = new StringBuilder();
            sb.append(mIngList.get(listIdx).getQuantity());
            sb.append(" (");
            sb.append(mIngList.get(listIdx).getMeasure());
            sb.append(")");

            mIngQuantity.setText(sb);
            mIngIngredient.setText(mIngList.get(listIdx).getIngredient());
        }


    }
}
