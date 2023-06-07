package com.andresuryana.fintrack.ui.splash;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.andresuryana.fintrack.R;
import com.andresuryana.fintrack.data.prefs.SessionHelper;
import com.andresuryana.fintrack.data.prefs.SessionHelperImpl;
import com.andresuryana.fintrack.databinding.FragmentSplashBinding;

public class SplashFragment extends Fragment implements SplashView {

    // Layout binding
    FragmentSplashBinding binding;

    // Presenter
    SplashPresenter presenter;

    // Navigation controller
    NavController navController;

    public SplashFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater);

        // Init presenter
        SessionHelper session = new SessionHelperImpl(requireContext());
        presenter = new SplashPresenter(session, this);

        // Get nav controller
        navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Delay and navigate according to user session
        new Handler().postDelayed(() -> presenter.checkUserSession(), 1500L);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Clear layout binding
        binding = null;
    }

    @Override
    public void authenticated() {
        navController.navigate(R.id.action_splashFragment_to_mainFragment);
    }

    @Override
    public void unauthenticated() {
        navController.navigate(R.id.action_splashFragment_to_loginFragment);
    }
}