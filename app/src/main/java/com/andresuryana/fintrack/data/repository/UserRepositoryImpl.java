package com.andresuryana.fintrack.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.andresuryana.fintrack.data.model.DashboardInfo;
import com.andresuryana.fintrack.data.model.User;
import com.andresuryana.fintrack.data.prefs.SessionHelper;
import com.andresuryana.fintrack.data.prefs.SessionHelperImpl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserRepositoryImpl implements UserRepository {

    private final DatabaseReference userRef;
    private final FirebaseAuth auth;
    private final SessionHelper session;

    public UserRepositoryImpl(Context context) {
        this.session = new SessionHelperImpl(context);
        this.auth = FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        if (session.isLoggedIn()) {
            this.userRef = db.getReference("users").child(session.getCurrentUserId());
        } else {
            this.userRef = null;
        }
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

    @Override
    public void logout(AuthCallback callback) {
        try {
            // Perform logout from firebase auth
            auth.signOut();

            // Clear session
            session.clearSession();
        } catch (Exception e) {
            callback.onFailure(e.getMessage());
        }
    }

    @Override
    public void getDashboardInfo(Callback<DashboardInfo> callback) {
        try {
            // Retrieve user info data
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Get values
                        Long incomeValue = snapshot.child("income").getValue(Long.class);
                        Long outcomeValue = snapshot.child("outcome").getValue(Long.class);

                        // Parse values
                        long income = incomeValue != null ? incomeValue : 0;
                        long outcome = outcomeValue != null ? outcomeValue : 0;

                        // Create dashboard info object
                        DashboardInfo dashboardInfo = new DashboardInfo(income, outcome);

                        callback.onSuccess(dashboardInfo);
                    } else {
                        callback.onFailure("No user info found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    callback.onFailure(error.getMessage());
                }
            });

        } catch (Exception e) {
            callback.onFailure(e.getMessage());
        }
    }
}
