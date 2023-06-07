package com.andresuryana.fintrack.ui.auth.register;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.andresuryana.fintrack.R;
import com.andresuryana.fintrack.data.model.User;
import com.andresuryana.fintrack.databinding.FragmentRegisterBinding;
import com.andresuryana.fintrack.ui.base.BaseFragment;
import com.andresuryana.fintrack.util.StringUtil;

import java.util.Objects;

public class RegisterFragment extends BaseFragment implements RegisterView {

    // Layout binding
    FragmentRegisterBinding binding;

    // Presenter
    RegisterPresenter presenter;

    // Navigation controller
    NavController navController;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater);

        // Init presenter
        presenter = new RegisterPresenter(requireContext(), this);

        // Get nav controller
        navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Text Input
        setupTextInput();

        // Button
        setupButton();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Clear layout binding
        binding = null;
    }

    @Override
    public void onRegisterSuccess(User user) {
        // Navigate to main fragment
        showMessage(getString(R.string.success_register));
        navController.navigate(R.id.action_registerFragment_to_mainFragment);
    }

    @Override
    public void onRegisterFailure(String message) {
        // Clear password input and show snackbar
        binding.etPassword.setText("");
        showErrorMessage(message);
    }

    private void setupTextInput() {
        // Name
        TextWatcher nameTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String nameString = s.toString();
                if (nameString.isEmpty()) {
                    // Empty user_name
                    binding.tilUserName.setError(getString(R.string.helper_empty_user_name));
                } else {
                    if (StringUtil.containsOnlyLettersWithSpaces(nameString)) {
                        // Valid user_name
                        binding.tilUserName.setError(null);
                    } else {
                        // Invalid user_name
                        binding.tilUserName.setError(getString(R.string.helper_invalid_user_name));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Nothing
            }
        };

        binding.etUserName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.etUserName.addTextChangedListener(nameTextWatcher);
            } else {
                binding.etUserName.removeTextChangedListener(nameTextWatcher);
            }
        });

        // Email
        TextWatcher emailTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String emailString = s.toString();
                if (emailString.isEmpty()) {
                    // Empty email
                    binding.tilEmail.setError(getString(R.string.helper_empty_email));
                } else {
                    if (StringUtil.isValidEmail(emailString)) {
                        // Valid email
                        binding.tilEmail.setError(null);
                    } else {
                        // Invalid email
                        binding.tilEmail.setError(getString(R.string.helper_invalid_email));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Nothing
            }
        };

        binding.etEmail.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.etEmail.addTextChangedListener(emailTextWatcher);
            } else {
                binding.etEmail.removeTextChangedListener(emailTextWatcher);
            }
        });

        // Password
        TextWatcher passwordTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String passwordString = s.toString();
                if (passwordString.isEmpty()) {
                    // Empty password
                    binding.tilPassword.setError(getString(R.string.helper_empty_password));
                } else {
                    if (StringUtil.isValidPassword(passwordString)) {
                        // Valid password
                        binding.tilPassword.setError(null);
                    } else {
                        // Invalid password
                        binding.tilPassword.setError(getString(R.string.helper_invalid_password));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Nothing
            }
        };

        binding.etPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                binding.etPassword.addTextChangedListener(passwordTextWatcher);
            } else {
                binding.etPassword.removeTextChangedListener(passwordTextWatcher);
            }
        });
    }

    private void setupButton() {
        // Button register
        binding.btnRegister.setOnClickListener(v -> doRegister());

        // Button login
        binding.btnLogin.setOnClickListener(v -> navController.popBackStack());
    }

    private void doRegister() {
        // Check text input errors
        if (binding.tilUserName.getError() == null && binding.tilEmail.getError() == null && binding.tilPassword.getError() == null) {
            // Get credentials
            String name = Objects.requireNonNull(binding.etUserName.getText()).toString();
            String email = Objects.requireNonNull(binding.etEmail.getText()).toString();
            String password = Objects.requireNonNull(binding.etPassword.getText()).toString();

            if (!(name.isEmpty() && email.isEmpty() && password.isEmpty())) {
                presenter.register(name, email, password);
            }
        } else {
            // There is an error
            showErrorMessage("Please check your input");
        }
    }
}