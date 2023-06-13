package com.andresuryana.fintrack.ui.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andresuryana.fintrack.databinding.ItemIconCategoryBinding;

import java.util.List;

public class IconSpinnerAdapter extends ArrayAdapter<Integer> {

    private final LayoutInflater inflater;

    public IconSpinnerAdapter(Context context, List<Integer> icons) {
        super(context, 0, icons);
        this.inflater = LayoutInflater.from(context);
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

    private View createView(int position, ViewGroup parent) {
//        ImageView imageView;
//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.item_icon_category, parent, false);
//            imageView = convertView.findViewById(R.id.iv_icon);
//            convertView.setTag(imageView);
//        } else {
//            imageView = (ImageView) convertView.getTag();
//        }
//
//        Integer drawableResId = getItem(position);
//
//        if (drawableResId != null) {
//            Drawable icon = getContext().getResources().getDrawable(drawableResId);
//            imageView.setImageDrawable(icon);
//        }
//
//        return convertView;

        // Inflate layout
        ItemIconCategoryBinding binding = ItemIconCategoryBinding.inflate(inflater, parent, false);
        
        // Set icon drawable according to the position
        binding.ivIcon.setImageResource(getItem(position));

        return binding.getRoot();
    }
}
