package com.andresuryana.fintrack.ui.transaction;

import androidx.recyclerview.widget.DiffUtil;

import com.andresuryana.fintrack.data.model.Transaction;

import java.util.List;

public class TransactionDiffCallback extends DiffUtil.Callback {

    private final List<Transaction> oldList;
    private final List<Transaction> newList;

    public TransactionDiffCallback(List<Transaction> oldList, List<Transaction> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getUid().equals(newList.get(newItemPosition).getUid());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
