package com.andresuryana.fintrack.ui.category;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.andresuryana.fintrack.data.model.Category;
import com.andresuryana.fintrack.databinding.ItemCategoryBinding;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private final List<Category> categories;
    private final OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Category category);
    }

    public CategoryAdapter(OnItemClickListener itemClickListener) {
        this.categories = new ArrayList<>();
        this.itemClickListener = itemClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<Category> categories) {
        // Calculate list differences
        CategoryDiffCallback diffCallback = new CategoryDiffCallback(this.categories, categories);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        // Update data
        this.categories.clear();
        this.categories.addAll(categories);

        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCategoryBinding binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.onBind(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemCategoryBinding binding;

        public CategoryViewHolder(ItemCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(Category category) {
            binding.categoryIcon.setImageResource(category.getIconRes());
            binding.categoryName.setText(category.getName());
            binding.getRoot().setClickable(true);
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Category category = categories.get(position);
                itemClickListener.onItemClick(category);
            }
        }
    }
}
