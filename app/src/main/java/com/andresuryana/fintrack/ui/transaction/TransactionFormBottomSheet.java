package com.andresuryana.fintrack.ui.transaction;

import static com.andresuryana.fintrack.util.DatePickerHelper.getDate;
import static com.andresuryana.fintrack.util.DatePickerHelper.parseDateString;
import static com.andresuryana.fintrack.util.DatePickerHelper.showDatePickerDialog;
import static com.andresuryana.fintrack.util.StringUtil.formatDate;
import static com.andresuryana.fintrack.util.StringUtil.formatRupiah;
import static com.andresuryana.fintrack.util.StringUtil.parseRupiah;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.andresuryana.fintrack.R;
import com.andresuryana.fintrack.data.model.Category;
import com.andresuryana.fintrack.data.model.Transaction;
import com.andresuryana.fintrack.databinding.BottomSheetAddTransactionBinding;
import com.andresuryana.fintrack.util.StringUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TransactionFormBottomSheet extends BottomSheetDialogFragment {

    // Layout binding
    private BottomSheetAddTransactionBinding binding;

    // Layout state
    private final LayoutState layoutState;

    // Adapter
    private final ArrayAdapter<Transaction.Type> typeAdapter;
    private final ArrayAdapter<Category> categoryAdapter;

    // Current selected dropdown
    private Transaction.Type selectedTransactionType;
    private Category selectedCategory;

    // Current transaction (if available)
    private Transaction transaction;
    private final List<Category> categories;

    // Callback
    private OnAddResultCallback onAddResultCallback;
    private OnModifyResultCallback onModifyResultCallback;

    // Constants
    private static final String DATE_PATTERN = "dd/mm/yyyy";

    public TransactionFormBottomSheet(Context context, List<Category> categories, OnAddResultCallback onAddResultCallback) {
        this.onAddResultCallback = onAddResultCallback;
        this.layoutState = LayoutState.ADD_TRANSACTION;
        this.categories = categories;
        this.typeAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, Transaction.Type.values());
        this.categoryAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, categories);
    }

    public TransactionFormBottomSheet(Context context, Transaction transaction, List<Category> categories, OnModifyResultCallback onModifyResultCallback) {
        this.transaction = transaction;
        this.onModifyResultCallback = onModifyResultCallback;
        this.layoutState = LayoutState.MODIFY_TRANSACTION;
        this.categories = categories;
        this.typeAdapter = new ArrayAdapter<Transaction.Type>(context, android.R.layout.simple_list_item_1, Transaction.Type.values()) {
            @NonNull
            @Override
            public Filter getFilter() {
                return new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {
                        FilterResults result = new FilterResults();
                        result.values = Transaction.Type.values();
                        result.count = Transaction.Type.values().length;
                        return result;
                    }

                    @Override
                    protected void publishResults(CharSequence constraint, FilterResults results) {
                        if (results != null && results.count > 0) {
                            notifyDataSetChanged();
                        } else {
                            notifyDataSetInvalidated();
                        }
                    }
                };
            }
        };
        this.categoryAdapter = new ArrayAdapter<Category>(context, android.R.layout.simple_list_item_1, categories) {
            @NonNull
            @Override
            public Filter getFilter() {
                return new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {
                        FilterResults result = new FilterResults();
                        result.values = Transaction.Type.values();
                        result.count = Transaction.Type.values().length;
                        return result;
                    }

                    @Override
                    protected void publishResults(CharSequence constraint, FilterResults results) {
                        if (results != null && results.count > 0) {
                            notifyDataSetChanged();
                        } else {
                            notifyDataSetInvalidated();
                        }
                    }
                };
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout binding
        binding = BottomSheetAddTransactionBinding.inflate(inflater);

        // Setup dropdown
        setupDropdown();

        // Setup text input layout
        setupTextInput();

        // Setup date picker
        setupDatePicker();

        // Check layout state
        switch (layoutState) {
            default:
            case ADD_TRANSACTION: {
                initAddTransaction();
                break;
            }
            case MODIFY_TRANSACTION: {
                initModifyTransaction();
                break;
            }
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Clear layout binding
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        adjustHeight();
    }

    private void adjustHeight() {
        View view = getView();
        if (view != null) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
            if (params != null) {
                params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
                view.setLayoutParams(params);

                View parent = (View) view.getParent();
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(parent);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(view.getHeight());
            }
        }
    }

    private void initAddTransaction() {
        // Set title
        binding.tvTitle.setText(getString(R.string.title_add_transaction));

        // Show add category button
        binding.btnAddTransaction.setVisibility(View.VISIBLE);

        // Button add category
        binding.btnAddTransaction.setOnClickListener(view -> {
            if (isInputValid() && isDropdownSelected()) {
                // Get value
                String title = Objects.requireNonNull(binding.etTransactionTitle.getText()).toString().trim();
                String amountString = String.valueOf(parseRupiah(Objects.requireNonNull(binding.etTransactionAmount.getText()).toString()));
                long amount = Long.parseLong(amountString);
                Date date = parseDateString(Objects.requireNonNull(binding.etTransactionDate.getText()).toString(), DATE_PATTERN);
                Editable notesText = binding.etTransactionNotes.getText();
                String notes = notesText != null ? Objects.requireNonNull(notesText).toString() : null;

                // If type is INCOME, selectedCategory must be null
                if (selectedTransactionType == Transaction.Type.INCOME) {
                    selectedCategory = null;
                }

                onAddResultCallback.onSuccess(selectedTransactionType, title, amount, selectedCategory, date, notes);
                dismiss();
            } else {
                onAddResultCallback.onFailed(getString(R.string.error_check_input));
                dismiss();
            }
        });

        // Hide modify button container
        binding.btnModifyContainer.setVisibility(View.GONE);
    }

    private void initModifyTransaction() {
        // Set title
        binding.tvTitle.setText(getString(R.string.title_modify_transaction));

        // Prefill transaction data
        if (transaction != null) {
            selectedTransactionType = transaction.getType();
            binding.acTransactionType.setText(transaction.getType().toString());
            if (transaction.getType() == Transaction.Type.OUTCOME) {
                // Only set category info if transaction type is OUTCOME
                int categoryPosition = categoryAdapter.getPosition(getCategoryByUid(transaction.getCategoryName()));
                Category category = categoryAdapter.getItem(categoryPosition);
                selectedCategory = category;
                binding.acCategory.setText(category.getName());
            } else {
                // Hide category dropdown if INCOME
                binding.tilCategory.setVisibility(View.GONE);
            }
            binding.etTransactionTitle.setText(transaction.getTitle());
            binding.etTransactionAmount.setText(formatRupiah(transaction.getAmount()));
            binding.etTransactionDate.setText(formatDate(transaction.getDate(), DATE_PATTERN));
            binding.etTransactionNotes.setText(transaction.getNotes());
        }

        // Show modify button container
        binding.btnModifyContainer.setVisibility(View.VISIBLE);

        // Button edit category
        binding.btnEdit.setOnClickListener(view -> {
            if (isInputValid() && isDropdownSelected()) {
                // Get value
                String amountString = String.valueOf(parseRupiah(Objects.requireNonNull(binding.etTransactionAmount.getText()).toString()));
                String dateString = Objects.requireNonNull(binding.etTransactionDate.getText()).toString();
                String title = Objects.requireNonNull(binding.etTransactionTitle.getText()).toString().trim();
                long amount = Long.parseLong(amountString);
                Date date = parseDateString(dateString, DATE_PATTERN);
                Editable notesText = binding.etTransactionNotes.getText();
                String notes = notesText != null ? Objects.requireNonNull(notesText).toString() : null;

                // If type is INCOME, selectedCategory must be null
                if (selectedTransactionType == Transaction.Type.INCOME) {
                    selectedCategory = null;
                }

                onModifyResultCallback.onEdit(transaction, selectedTransactionType, title, amount, selectedCategory, date, notes);
                dismiss();
            } else {
                onModifyResultCallback.onFailed(getString(R.string.error_check_input));
                dismiss();
            }
        });

        // Button delete transaction
        binding.btnDelete.setOnClickListener(view -> {
            if (transaction != null) {
                onModifyResultCallback.onDelete(transaction);
                dismiss();
            } else {
                onModifyResultCallback.onFailed(getString(R.string.error_transaction_not_found));
                dismiss();
            }
        });

        // Hide add transaction button
        binding.btnAddTransaction.setVisibility(View.GONE);
    }

    private void setupDropdown() {
        // Transaction type
        binding.acTransactionType.setAdapter(typeAdapter);
        binding.acTransactionType.setOnItemClickListener(((parent, view, position, id) -> {
            // Update selection
            selectedTransactionType = typeAdapter.getItem(position);

            // Hide category dropdown if type is INCOME
            if (selectedTransactionType == Transaction.Type.INCOME) {
                binding.tilCategory.setVisibility(View.GONE);
            } else {
                binding.tilCategory.setVisibility(View.VISIBLE);
            }

            // Remove error
            if (binding.acTransactionType.getError() != null) {
                binding.acTransactionType.setError(null);
            }
        }));

        // Categories
        binding.acCategory.setAdapter(categoryAdapter);
        binding.acCategory.setOnItemClickListener(((parent, view, position, id) -> {
            // Update selection
            selectedCategory = categoryAdapter.getItem(position);

            // Remove error
            if (binding.acCategory.getError() != null) {
                binding.acCategory.setError(null);
            }
        }));
    }

    private void setupTextInput() {
        // Transaction title
        TextWatcher titleTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nameString = s.toString();
                if (nameString.isEmpty()) {
                    // Empty transaction title
                    binding.tilTransactionTitle.setError(getString(R.string.helper_empty_transaction_title));
                } else {
                    if (StringUtil.containsOnlyLettersWithSpaces(nameString)) {
                        // Valid transaction title
                        binding.tilTransactionTitle.setError(null);
                    } else {
                        // Invalid transaction title
                        binding.tilTransactionTitle.setError(getString(R.string.helper_invalid_transaction_title));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Nothing
            }
        };

        binding.etTransactionTitle.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.etTransactionTitle.addTextChangedListener(titleTextWatcher);
            } else {
                binding.etTransactionTitle.removeTextChangedListener(titleTextWatcher);
            }
        });

        // Amount
        TextWatcher amountTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Amount should not be over 16 digits
                long amount;
                try {
                    amount = parseRupiah(s.toString());
                } catch (NumberFormatException e) {
                    amount = 0L;
                }

                // Apply the maximum amount limit
                String amountString = Long.toString(amount);
                if (amountString.length() > 16) {
                    // Remove last character if digits more than 16
                    amount = Long.parseLong(amountString.substring(0, amountString.length() - 1));
                }

                // Format the amount as desired
                String formattedAmount = formatRupiah(amount);

                // Update the amount field
                binding.etTransactionAmount.removeTextChangedListener(this);
                binding.etTransactionAmount.setText(formattedAmount);
                binding.etTransactionAmount.setSelection(formattedAmount.length());
                binding.etTransactionAmount.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Nothing
            }
        };

        binding.etTransactionAmount.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.etTransactionAmount.addTextChangedListener(amountTextWatcher);
            } else {
                binding.etTransactionAmount.removeTextChangedListener(amountTextWatcher);
            }
        });
    }

    private void setupDatePicker() {
        // Show date picker on date input clicked
        binding.etTransactionDate.setOnClickListener(view -> showDatePickerDialog(requireContext(), (datePicker, year, month, dayOfMonth) -> {
            Log.d(this.getClass().getSimpleName(), "setupDatePicker: clicked");
            // Set date into edit text
            Date date = getDate(year, month, dayOfMonth);
            binding.etTransactionDate.setText(formatDate(date, DATE_PATTERN));
        }));
    }

    private boolean isInputValid() {
        // Check for error
        boolean isTypeValid = binding.tilTransactionType.getError() == null;
        boolean isCategoryValid = selectedTransactionType == Transaction.Type.INCOME || binding.tilCategory.getError() == null;
        boolean isTitleValid = binding.tilTransactionTitle.getError() == null;
        boolean isAmountValid = binding.tilTransactionAmount.getError() == null;
        boolean isDateValid = binding.tilTransactionDate.getError() == null;

        return isTypeValid || isCategoryValid || isTitleValid || isAmountValid || isDateValid;
    }

    private boolean isDropdownSelected() {
        // Check dropdown selection
        if (selectedTransactionType == null) {
            binding.tilTransactionType.setError(getString(R.string.helper_empty_transaction_type));
            return false;
        }
        if (selectedTransactionType == Transaction.Type.OUTCOME) {
            if (selectedCategory == null) {
                binding.tilCategory.setError(getString(R.string.helper_empty_transaction_category));
                return false;
            }
        }
        return true;
    }

    private Category getCategoryByUid(String categoryUid) {
        for (Category category : this.categories) {
            if (category.getUid().equals(categoryUid)) {
                return category;
            }
        }
        return null;
    }

    public interface OnAddResultCallback {
        void onSuccess(Transaction.Type type, String title, long amount, @Nullable Category category, Date date, @Nullable String notes);

        void onFailed(String message);
    }

    public interface OnModifyResultCallback {
        void onEdit(Transaction oldTransaction, Transaction.Type type, String title, long amount, @Nullable Category category, Date date, @Nullable String notes);

        void onDelete(Transaction transaction);

        void onFailed(String message);
    }

    private enum LayoutState {
        ADD_TRANSACTION,
        MODIFY_TRANSACTION
    }
}