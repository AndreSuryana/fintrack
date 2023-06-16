package com.andresuryana.fintrack.ui.auth.login;

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
import com.andresuryana.fintrack.data.prefs.SessionHelper;
import com.andresuryana.fintrack.data.prefs.SessionHelperImpl;
import com.andresuryana.fintrack.databinding.FragmentLoginBinding;
import com.andresuryana.fintrack.ui.base.BaseFragment;
import com.andresuryana.fintrack.util.StringUtil;

import java.util.Objects;

public class LoginFragment extends BaseFragment implements LoginView {

    // Layout binding
    FragmentLoginBinding binding;

    // Presenter
    LoginPresenter presenter;

    // Navigation controller
    NavController navController;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater);

        // Init presenter
        SessionHelper session = new SessionHelperImpl(requireContext());
        presenter = new LoginPresenter(requireContext(), session, this);

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
    public void onLoginSuccess(User user) {
        // Navigate to main fragment
        showMessage(getString(R.string.success_login, user.getName()));
        navController.navigate(R.id.action_loginFragment_to_mainFragment);
    }

    @Override
    public void onLoginFailure(String message) {
        // Clear password input and show snackbar
        binding.etPassword.setText("");
        showErrorMessage(message);
    }

    private void setupTextInput() {
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
        // Button login
        binding.btnLogin.setOnClickListener(v -> doLogin());

        // Button register
        binding.btnRegister.setOnClickListener(v -> navController.navigate(R.id.action_loginFragment_to_registerFragment));
    }

    private void doLogin() {
        // Check text input errors
        if (binding.tilEmail.getError() == null && binding.tilPassword.getError() == null) {
            // Get credentials
            String email = Objects.requireNonNull(binding.etEmail.getText()).toString();
            String password = Objects.requireNonNull(binding.etPassword.getText()).toString();

            presenter.login(email, password);
        } else {
            // There is an error
            showErrorMessage("Please check your input");
        }
    }
}