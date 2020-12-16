package com.donation.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.donation.R;
import com.donation.model.in.UserModelIn;
import com.donation.model.out.UserModelOut;
import com.donation.retrofit.ApiClient;
import com.donation.retrofit.ApiServiceUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewUserFragment extends DialogFragment {

    UserModelOut user;
    EditText txtEmail;
    EditText txtName;
    EditText txtPhone;
    EditText txtPassword;
    EditText txtConfirmPassword;
    NavController navigation;

    public NewUserFragment() {
        user = new UserModelOut();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnNewUser = view.findViewById(R.id.btnConfirm);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtName = view.findViewById(R.id.txtName);
        txtPassword = view.findViewById(R.id.txtPassword);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtConfirmPassword = view.findViewById(R.id.txtconfirmPassword);
        navigation = Navigation.findNavController(view);

        btnNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
                if (isFormValid()) {
                    postUser();
                } else {
                    Toast.makeText(getContext(), R.string.FormErrors, Toast.LENGTH_LONG).show();
                }
                closeKeyboard();
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

    private void createUser() {
        user.setEmail(txtEmail.getText().toString());
        user.setName(txtName.getText().toString());
        user.setPassword(txtPassword.getText().toString());
        if (!(txtPhone.getText().toString().isEmpty())) {
            user.setPhone(Integer.parseInt(txtPhone.getText().toString()));
        }
    }

    private boolean isFormValid() {
        boolean isValid = true;

        if (TextUtils.isEmpty(user.getEmail())) {
            txtEmail.setError(getResources().getString(R.string.EmailNull));
            isValid = false;
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(user.getEmail()).matches()) {
                isValid = false;
                txtEmail.setError(getResources().getString(R.string.WrongEmail));
            } else {
                if (TextUtils.isEmpty(user.getName())) {
                    isValid = false;
                    txtName.setError(getResources().getString(R.string.NameNull));
                } else {
                    if (!isPasswordValid()) {
                        isValid = false;
                    }
                }
            }
        }
        return isValid;
    }

    private boolean isPasswordValid() {
        boolean isValid = true;
        if (TextUtils.isEmpty(txtPassword.getText().toString())) {
            isValid = false;
            txtPassword.setError(getResources().getString(R.string.PasswordNull));

        } else {
            if (!txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString())) {
                txtConfirmPassword.setError(getResources().getString(R.string.NotEqualPassword));
                isValid = false;
            } else {
                if (txtPassword.getText().toString().length() < 4 || txtPassword.getText().toString().length() > 8) {
                    txtPassword.setError(getResources().getString(R.string.WrongLengthPassword));
                    isValid = false;
                }
            }
        }
        return isValid;
    }

    private void postUser() {
        Retrofit retrofit = ApiClient.getClient();
        ApiServiceUser userService = retrofit.create(ApiServiceUser.class);

        Call<UserModelIn> call = userService.addUser(user);
        call.enqueue(new Callback<UserModelIn>() {
            @Override
            public void onResponse(Call<UserModelIn> call, Response<UserModelIn> response) {

                if (response.message().equals("Bad Request")) {
                    Log.println(Log.INFO, "User Response BAD", response.errorBody().toString());
                    Toast.makeText(getContext(), "No se pudo ingresar el usuario", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Usuario ingresado", Toast.LENGTH_LONG).show();

                    navigation.navigate(R.id.nav_homeFragment);
                }
            }

            @Override
            public void onFailure(Call<UserModelIn> call, Throwable t) {
                Log.println(Log.ERROR, "User Failure", t.getMessage());
                Toast.makeText(getContext(), "Error al ingresar el usuario", Toast.LENGTH_LONG).show();
            }
        });
    }
}