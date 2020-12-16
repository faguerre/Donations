package com.donation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.donation.database.LoginDBHandler;
import com.donation.database.TokenHandler;
import com.donation.model.in.UserModelIn;
import com.donation.retrofit.ApiClient;
import com.donation.retrofit.ApiServiceSession;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InitialFragment extends Fragment {

    TokenHandler tokenHandler;
    LoginDBHandler loginDBHandler;
    final String[] token = new String[1];
    Boolean isExternal = false;


    public InitialFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_initial, container, false);
    }

    private boolean isLogged() {
        token[0] = tokenHandler.getUserToken();
        isExternal = loginDBHandler.existsUser();
        return (!token[0].equals("")) || isExternal;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnLogin = view.findViewById(R.id.btnLogin);
        Button btnLogout = view.findViewById(R.id.btnLogout);
        Button btnDonation = view.findViewById(R.id.btnDonation);
        Button btnEditInfo = view.findViewById(R.id.btnEdit);
        Button btnAcercaDe = view.findViewById(R.id.btnAcercaDe);
        Button btnViewDonations = view.findViewById(R.id.btnViewDonations);
        Button btnViewMenu = view.findViewById(R.id.btnVerMenu);

        tokenHandler = new TokenHandler(this.getContext(), null, null, 1);
        loginDBHandler = new LoginDBHandler(this.getContext(), null, null, 1);

        if (isLogged()) {
            btnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);

        } else {
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
        }
        if (isExternal) {
            btnEditInfo.setVisibility(View.GONE);
        } else {
            btnEditInfo.setVisibility(View.VISIBLE);
        }

        final NavController navigation = Navigation.findNavController(view);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.navigate(R.id.nav_login);
            }
        });
        btnViewDonations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.navigate(R.id.donationsViewFragment);
            }
        });
        /*btnViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // navigation.navigate(R.id.menu);
            }
        });*/

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Retrofit retrofit = ApiClient.getClient();
                ApiServiceSession sessionService = retrofit.create(ApiServiceSession.class);
                token[0] = tokenHandler.getUserToken();
                Call<ResponseBody> call = sessionService.Logout(token[0]);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        tokenHandler.deleteToken(token[0]);
                        loginDBHandler.deleteUser(tokenHandler.getUserToken());
                        Log.println(Log.INFO, "Token Delete", response.message());
                        disconectUser();
                        navigation.navigate(R.id.nav_logout);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.println(Log.ERROR, "Token Delete", t.getMessage());

                        Toast.makeText(getContext(), "No se pudo realizar el logout", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        btnDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.navigate(R.id.donationFragment);
            }
        });

        btnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation.navigate(R.id.nav_editInfo);
            }
        });

    }

    private void disconectUser() {
        LoginDBHandler loginDBHandler = new LoginDBHandler(getContext(), null, null, 1);

        if (loginDBHandler.existsUser()) {
            UserModelIn user = loginDBHandler.getUser();
            loginDBHandler.deleteUser(user.getEmail());
            GoogleSignInClient mGoogleSignInClient;
            GoogleSignInOptions gso;
            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this.getActivity(), gso);
            mGoogleSignInClient.revokeAccess();
        }
    }
}