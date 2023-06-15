package com.andresuryana.fintrack.ui.category;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andresuryana.fintrack.databinding.ItemIconCategoryBinding;

import java.util.List;
import java.util.Objects;

public class IconSpinnerAdapter extends ArrayAdapter<Pair<String, Integer>> {

    private final LayoutInflater inflater;
    private final List<Pair<String, Integer>> icons;

    public IconSpinnerAdapter(Context context, List<Pair<String, Integer>> icons) {
        super(context, 0, icons);
        this.inflater = LayoutInflater.from(context);
        this.icons = icons;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, parent);
    }

    public int getPositionByName(String iconName) {
        for (Pair<String, Integer> pair : this.icons) {
            if (Objects.equals(pair.first, iconName)) {
                return getPosition(pair);
            }
        }
        return NO_SELECTION;
    }

    private View createView(int position, ViewGroup parent) {
        // Inflate layout
        ItemIconCategoryBinding binding = ItemIconCategoryBinding.inflate(inflater, parent, false);
        
        // Set icon drawable according to the position
        binding.ivIcon.setImageResource(getItem(position).second);

        return binding.getRoot();
    }
}
