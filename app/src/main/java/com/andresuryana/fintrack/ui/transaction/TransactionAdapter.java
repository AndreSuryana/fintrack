package com.andresuryana.fintrack.ui.transaction;

import static com.andresuryana.fintrack.util.StringUtil.formatDate;
import static com.andresuryana.fintrack.util.StringUtil.formatRupiah;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.andresuryana.fintrack.R;
import com.andresuryana.fintrack.data.model.Transaction;
import com.andresuryana.fintrack.databinding.ItemTransactionBinding;
import com.andresuryana.fintrack.util.IconUtil;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private final List<Transaction> categories;
    private final OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Transaction transaction);
    }

    public TransactionAdapter(OnItemClickListener itemClickListener) {
        this.categories = new ArrayList<>();
        this.itemClickListener = itemClickListener;
    }

    public void setList(List<Transaction> categories) {
        // Calculate list differences
        TransactionDiffCallback diffCallback = new TransactionDiffCallback(this.categories, categories);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        // Update data
        this.categories.clear();
        this.categories.addAll(categories);

        diffResult.dispatchUpdatesTo(this);
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTransactionBinding binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TransactionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        holder.onBind(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemTransactionBinding binding;

        public TransactionViewHolder(ItemTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void onBind(Transaction transaction) {
            Drawable icon;
            Drawable typeIcon;
            if (transaction.getType() == Transaction.Type.INCOME) {
                icon = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_income);
                typeIcon = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_income_arrow);
                if (typeIcon != null) {
                    typeIcon.setTintList(ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary)));
                }
            } else {
                icon = IconUtil.getIconByName(itemView.getContext(), transaction.getCategoryIconName());
                typeIcon = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_outcome_arrow);
                if (typeIcon != null) {
                    typeIcon.setTintList(ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), R.color.colorError)));
                }
            }
            binding.ivIcon.setImageDrawable(icon);
            binding.tvTitle.setText(transaction.getTitle());
            binding.tvAmount.setText(formatRupiah(transaction.getAmount()));
            binding.tvDate.setText(formatDate(transaction.getDate(), null));
            binding.ivType.setImageDrawable(typeIcon);
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Transaction transaction = categories.get(position);
                itemClickListener.onItemClick(transaction);
            }
        }
    }
}
