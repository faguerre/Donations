package com.donation.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.donation.R;
import com.donation.database.LoginDBHandler;
import com.donation.database.TokenHandler;
import com.donation.geofencing.DonationGeoReferences;
import com.donation.model.in.LoginModelIn;
import com.donation.model.in.UserModelIn;
import com.donation.model.out.LoginModelExternalOut;
import com.donation.model.out.LoginModelOut;
import com.donation.retrofit.ApiClient;
import com.donation.retrofit.ApiServiceSession;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LoginFragment extends DialogFragment {

    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;
    static int CODE_GOOGLE = 100;
    private NavController navigation;
    LoginDBHandler loginDB;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void signIn() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this.getActivity(), gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, CODE_GOOGLE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    EditText txtUserMail;
    EditText txtUserPassword;
    DonationGeoReferences donationGeoReferences;
    TokenHandler tokenHandler;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginDB = new LoginDBHandler(this.getContext(), null, null, 1);
        Button btnLogin = view.findViewById(R.id.btnConfirm);
        Button btnNewUser = view.findViewById(R.id.btnNew);
        SignInButton btnLoginGoogle = view.findViewById(R.id.btnGoogle);
        txtUserMail = view.findViewById(R.id.txtEditEmail);
        txtUserPassword = view.findViewById(R.id.txtPassword);
        tokenHandler = new TokenHandler(this.getContext(), null, null, 1);

        navigation = Navigation.findNavController(view);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginModelOut loginModelOut = new LoginModelOut();
                loginModelOut.Email = txtUserMail.getText().toString();
                loginModelOut.Password = txtUserPassword.getText().toString();
                login(loginModelOut);
                closeKeyboard();
            }
        });

        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });
        btnNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigation.navigate(R.id.nav_NewUser);
                //navigation.popBackStack();
                navigation.navigate(R.id.nav_NewUser);
            }
        });
    }

    private void login(final LoginModelOut loginModelOut) {
        Retrofit retrofit = ApiClient.getClient();
        ApiServiceSession sessionService = retrofit.create(ApiServiceSession.class);
        Call<LoginModelIn> call = sessionService.Login(loginModelOut);
        call.enqueue(new Callback<LoginModelIn>() {
            @Override
            public void onResponse(Call<LoginModelIn> call, Response<LoginModelIn> response) {
                if (response.isSuccessful()) {
                    String token = response.body().getToken();
                    tokenHandler.addToken(token);
                    loginDB.addUser(token, new UserModelIn(loginModelOut.getEmail()),"N");
                    //navigation.navigate(R.id.nav_homeFragment);
                    navigation.popBackStack(R.id.nav_homeFragment, false);
                    donationGeoReferences = new DonationGeoReferences(getContext());
                    donationGeoReferences.generateList();
                } else {
                    Log.println(Log.INFO, "login failure", response.message());
                    Toast.makeText(getContext(), "No se pudo realizar el login", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginModelIn> call, Throwable t) {
                Log.println(Log.ERROR, "login failure", t.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            Log.println(Log.WARN, "Google", "Bienvenid@  " + account.getDisplayName() + "!");
            Toast.makeText(this.getContext(), "Bienvenid@ " + account.getEmail(), Toast.LENGTH_LONG).show();

            loginDB.addUserGoogle(account);
            LoginModelExternalOut loginModelExternalOut = new LoginModelExternalOut();
            loginModelExternalOut.Email = account.getEmail();
            loginModelExternalOut.Name = account.getDisplayName();
            loginExternal(loginModelExternalOut);
            navigation.navigate(R.id.nav_homeFragment);

        } catch (ApiException e) {
            Log.println(Log.WARN, "Google", "signInResult:failed code=" + e.getStatusCode());

        }
    }

    private void loginExternal(final LoginModelExternalOut loginModelExternalOut) {
        Retrofit retrofit = ApiClient.getClient();
        ApiServiceSession sessionService = retrofit.create(ApiServiceSession.class);
        Call<LoginModelIn> call = sessionService.LoginExternal(loginModelExternalOut);
        call.enqueue(new Callback<LoginModelIn>() {
            @Override
            public void onResponse(Call<LoginModelIn> call, Response<LoginModelIn> response) {
                if (response.isSuccessful()) {
                    String token = response.body().getToken();
                    tokenHandler.addToken(token);
                    loginDB.addUser(token, new UserModelIn(loginModelExternalOut.Email),"S");
                    navigation.navigate(R.id.nav_homeFragment);
                } else {
                    Log.println(Log.INFO, "User Response BAD", response.message());
                    Toast.makeText(getContext(), "No se pudo realizar el login", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginModelIn> call, Throwable t) {
                Log.println(Log.ERROR, "login external failure", t.getMessage());
            }
        });
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}