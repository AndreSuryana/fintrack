package com.andresuryana.fintrack.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Pair;

import androidx.core.content.ContextCompat;

import com.andresuryana.fintrack.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IconUtil {

    private static final Map<String, Integer> icons = new HashMap<>();

    static {
        // Add icon name-resourceId mappings here
        icons.put("ic_attach_money", R.drawable.ic_attach_money);
        icons.put("ic_local_dining", R.drawable.ic_local_dining);
        icons.put("ic_directions_car", R.drawable.ic_directions_car);
        icons.put("ic_shopping_cart", R.drawable.ic_shopping_cart);
        icons.put("ic_house", R.drawable.ic_house);
        icons.put("ic_school", R.drawable.ic_school);
        icons.put("ic_account_circle", R.drawable.ic_account_circle);
        icons.put("ic_fitness_center", R.drawable.ic_fitness_center);
        icons.put("ic_flight", R.drawable.ic_flight);
        icons.put("ic_work", R.drawable.ic_work);
        // Add more mappings as needed
    }

    public static Drawable getIconByName(Context context, String iconName) {
        if (icons.containsKey(iconName)) {
            Integer resId = icons.getOrDefault(iconName, R.drawable.ic_attach_money);
            return resId != null ? ContextCompat.getDrawable(context, resId) : null;
        } else {
            return null;
        }
    }

    public static Integer getIconIdByName(String iconName) {
        return icons.getOrDefault(iconName, R.drawable.ic_attach_money);
    }

    public static List<Pair<String, Integer>> getIconResources() {
        // Convert the map entries to a list of pairs
        List<Pair<String, Integer>> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : icons.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            Pair<String, Integer> pair = new Pair<>(key, value);
            list.add(pair);
        }
        return list;
    }
}
