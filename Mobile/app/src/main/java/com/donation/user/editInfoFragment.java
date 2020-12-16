package com.donation.user;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.donation.R;
import com.donation.database.LoginDBHandler;
import com.donation.database.TokenHandler;
import com.donation.model.in.UserModelIn;
import com.donation.model.out.UserModelOut;
import com.donation.retrofit.ApiClient;
import com.donation.retrofit.ApiServiceSession;
import com.donation.retrofit.ApiServiceUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class editInfoFragment extends Fragment {

    UserModelOut userOut;
    EditText txtEmail;
    EditText txtName;
    EditText txtPhone;
    EditText txtPassword;
    EditText txtConfirmPassword;
    TokenHandler dbHandler;
    UserModelIn userIn;
    Retrofit retrofit;
    LoginDBHandler loginDB;

    public editInfoFragment() {
        retrofit = ApiClient.getClient();
        userOut = new UserModelOut();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_info, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnEditUser = view.findViewById(R.id.btnConfirmInfo);
        txtEmail = view.findViewById(R.id.txtEditEmail);
        txtName = view.findViewById(R.id.txtEditName);
        txtPassword = view.findViewById(R.id.txtEditPassword);
        txtPhone = view.findViewById(R.id.txtEditPhone);
        txtConfirmPassword = view.findViewById(R.id.txtEditConfirmPassword);
        dbHandler = new TokenHandler(this.getContext(), null, null, 1);
        loginDB = new LoginDBHandler(this.getContext(), null, null, 1);

        ApiServiceUser userService = retrofit.create(ApiServiceUser.class);

        if (loginDB.existsUser()) {
            UserModelIn user = loginDB.getUser();
            txtEmail.setText(user.getEmail());
            txtName.setText(user.getName());
        } else {
            getUserById(dbHandler.getUserToken());

            btnEditUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createUser(v);

                    if (isFormValid()) {
                        updateUser();
                    } else {
                        Toast.makeText(getContext(), R.string.FormErrors, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void getUserById(String token) {

        ApiServiceSession sessionService = retrofit.create(ApiServiceSession.class);
        Call<UserModelIn> call = sessionService.GetUserByToken(token);
        call.enqueue(new Callback<UserModelIn>() {
            @Override
            public void onResponse(Call<UserModelIn> call, Response<UserModelIn> response) {

                if (response.message().equals("Bad Request")) {
                    Log.println(Log.INFO, "User", response.errorBody().toString());
                    Toast.makeText(getContext(), "No se pudo obtener el usuario", Toast.LENGTH_LONG).show();
                } else {
                    userIn = response.body();
                    showUserInfo(userIn);
                }
            }

            @Override
            public void onFailure(Call<UserModelIn> call, Throwable t) {
                Log.println(Log.ERROR, "User Failure", t.getMessage());
                Toast.makeText(getContext(), "Error al obtener datos", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showUserInfo(UserModelIn user) {
        txtEmail.setText(user.getEmail());
        txtName.setText(user.getName());
        txtPassword.setText(user.getPassword());
        txtPhone.setText(String.valueOf(user.getPhone()));
    }


    private void createUser(View view) {
        userOut.setEmail(txtEmail.getText().toString());
        userOut.setName(txtName.getText().toString());
        userOut.setPassword(txtPassword.getText().toString());
        if (!(txtPhone.getText().toString().isEmpty())) {
            userOut.setPhone(Integer.parseInt(txtPhone.getText().toString()));
        }
    }

    private boolean isFormValid() {
        boolean isValid = true;

        if (userOut.getEmail().isEmpty()) {
            isValid = false;
            txtEmail.setHint(R.string.EmailNull);
            txtEmail.setHintTextColor(getResources().getColor(R.color.Error));
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(userOut.getEmail()).matches()) {
                isValid = false;
                Toast.makeText(getContext(), R.string.WrongEmail, Toast.LENGTH_LONG).show();
                txtEmail.setTextColor(getResources().getColor(R.color.Error));
            } else {
                if (userOut.getName().isEmpty()) {
                    isValid = false;
                    txtName.setHint(R.string.NameNull);
                    txtName.setHintTextColor(getResources().getColor(R.color.Error));
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
        if (txtPassword.getText().toString().isEmpty()) {
            isValid = false;
            Toast.makeText(getContext(), R.string.PasswordNull, Toast.LENGTH_LONG).show();
        } else {
            if (!txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString())) {
                Toast.makeText(getContext(), R.string.NotEqualPassword, Toast.LENGTH_LONG).show();
                isValid = false;
            } else {
                if (txtPassword.getText().toString().length() < 4 || txtPassword.getText().toString().length() > 8) {
                    Toast.makeText(getContext(), R.string.WrongLengthPassword, Toast.LENGTH_LONG).show();
                    isValid = false;
                }
            }
        }
        return isValid;
    }

    private void updateUser() {

        ApiServiceUser userService = retrofit.create(ApiServiceUser.class);

        if (userIn != null) {

            Call<UserModelIn> call = userService.editUser(userIn.getId(), userOut);
            call.enqueue(new Callback<UserModelIn>() {
                @Override
                public void onResponse(Call<UserModelIn> call, Response<UserModelIn> response) {
                    Log.println(Log.INFO, "User", response.message());

                    if (response.message().equals("Bad Request")) {

                        Toast.makeText(getContext(), "No se pudo actualizar el usuario", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Usuario actualizado", Toast.LENGTH_LONG).show();
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
}