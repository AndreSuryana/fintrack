package com.andresuryana.fintrack.data.repository;

import com.andresuryana.fintrack.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserRepositoryImpl implements UserRepository {

    private final FirebaseAuth auth;

    public UserRepositoryImpl() {
        this.auth = FirebaseAuth.getInstance();
    }

    @Override
    public void login(User user, AuthCallback callback) {
        try {
            auth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Get firebase credentials
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                // Get user id, display name & user token
                                user.setUserId(firebaseUser.getUid());
                                user.setName(firebaseUser.getDisplayName());
                                firebaseUser.getIdToken(true).addOnCompleteListener(tokenResult -> {
                                    if (tokenResult.isSuccessful()) {
                                        user.setToken(tokenResult.getResult().getToken());
                                        callback.onSuccess(user);
                                    }
                                });
                            }
                        } else {
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "An error occurred";
                            callback.onFailure(errorMessage);
                        }
                    });
        } catch (Exception e) {
            callback.onFailure(e.getMessage());
        }
    }

    @Override
    public void register(User user, AuthCallback callback) {
        try {
            auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                // Update user profile (display name)
                                UserProfileChangeRequest userProfile = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(user.getName())
                                        .build();
                                firebaseUser.updateProfile(userProfile)
                                        .addOnCompleteListener(updateTask -> {
                                            if (!updateTask.isSuccessful()) {
                                                String errorMessage = task.getException() != null ? task.getException().getMessage() : "An error occurred";
                                                callback.onFailure(errorMessage);

                                                // Delete user if profile update failed
                                                firebaseUser.delete();
                                            }
                                        });

                                // Get user id & token
                                firebaseUser.getIdToken(true).addOnCompleteListener(tokenResult -> {
                                    if (tokenResult.isSuccessful()) {
                                        user.setUserId(firebaseUser.getUid());
                                        user.setToken(tokenResult.getResult().getToken());
                                        callback.onSuccess(user);
                                    }
                                });
                            }
                        } else {
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "An error occurred";
                            callback.onFailure(errorMessage);
                        }
                    });
        } catch (Exception e) {
            callback.onFailure(e.getMessage());
        }
    }
}
