package com.donation.donationEvent;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.donation.R;
import com.donation.database.TokenHandler;
import com.donation.model.AddressModel;
import com.donation.model.CenterAddressModel;
import com.donation.model.in.UserModelIn;
import com.donation.model.out.AddressModelOut;
import com.donation.model.out.DonationEventModelOut;
import com.donation.model.out.UserModelOut;
import com.donation.retrofit.ApiClient;
import com.donation.retrofit.ApiServiceDonationEvent;
import com.donation.retrofit.ApiServiceSession;
import com.donation.utils.DatePickerFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//import com.donation.ConfirmFragment;

public class CreateEventFragment extends Fragment {

    EditText txtValidityFrom;
    EditText txtValidityUntil;
    EditText editEventName;
    EditText txtEventDescription;
    EditText txtEventEmail;
    EditText txtPhone;
    Button btnEventSave, btnaddCenter;
    DonationEventModelOut donationEvent = new DonationEventModelOut();
    CenterAddressModel centerAddressModel;
    EventViewModel eventViewModel;
    UserModelIn user;
    private TokenHandler tokenHandler;
    Retrofit retrofit;


    public CreateEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().get("CentersAddresses") != null) {
            centerAddressModel = (CenterAddressModel) getArguments().get("CentersAddresses");

        }
    }

    private void getUserById() {
        String token;
        tokenHandler = new TokenHandler(getContext(), null, null, 1);
        token = tokenHandler.getUserToken();
        retrofit = ApiClient.getClient();
        ApiServiceSession sessionService = retrofit.create(ApiServiceSession.class);
        Call<UserModelIn> call = sessionService.GetUserByToken(token);
        call.enqueue(new Callback<UserModelIn>() {
            @Override
            public void onResponse(Call<UserModelIn> call, Response<UserModelIn> response) {

                if (response.message().equals("Bad Request")) {
                    Log.println(Log.INFO, "User", response.errorBody().toString());
                    Toast.makeText(getContext(), "No se pudo obtener el usuario", Toast.LENGTH_LONG).show();
                } else {
                    user = response.body();
                }
            }

            @Override
            public void onFailure(Call<UserModelIn> call, Throwable t) {
                Log.println(Log.ERROR, "User Failure", t.getMessage());
                Toast.makeText(getContext(), "Error al obtener usuario", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupInfo() {

        editEventName.setText(eventViewModel.getEvent().getValue().getName());
        txtEventDescription.setText(eventViewModel.getEvent().getValue().getDescription());
        txtEventEmail.setText(eventViewModel.getEvent().getValue().getEmail());
        txtValidityFrom.setText(eventViewModel.getEvent().getValue().getValidityFrom());
        txtValidityUntil.setText(eventViewModel.getEvent().getValue().getValidityUntil());
        txtPhone.setText(eventViewModel.getEvent().getValue().getPhone());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        eventViewModel = new ViewModelProvider(getActivity()).get(EventViewModel.class);
        tokenHandler = new TokenHandler(getContext(), null, null, 1);

        return view;
    }


    private String twoDigits(int n) {
        return (n <= 9) ? ("0" + n) : String.valueOf(n);
    }

    private void showDatePickerDialog(final EditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final String selectedDate = twoDigits(dayOfMonth) + "/" + twoDigits(month + 1) + "/" + year;
                editText.setText(selectedDate);
            }
        });
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Date today = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String todayString = dateFormat.format(today);
        editEventName = view.findViewById(R.id.editEventName);
        txtEventDescription = view.findViewById(R.id.txtEventDescription);
        txtEventEmail = view.findViewById(R.id.txtEventEmail);
        txtValidityFrom = view.findViewById(R.id.txtValidityFrom);
        txtValidityUntil = view.findViewById(R.id.txtValidityUntil);
        txtPhone = view.findViewById(R.id.txtEventPhone);
        btnEventSave = view.findViewById(R.id.btnEventSave);
        btnaddCenter = view.findViewById(R.id.btnaddCenterEvent);


        if (eventViewModel.getEvent().getValue() != null) {
            setupInfo();
        } else {
            txtValidityFrom.setText(todayString);
        }
        txtValidityFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(txtValidityFrom);
            }
        });

        txtValidityUntil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(txtValidityUntil);
            }
        });

        btnEventSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getUserById();
                String token;
                tokenHandler = new TokenHandler(getContext(), null, null, 1);
                token = tokenHandler.getUserToken();
                retrofit = ApiClient.getClient();
                ApiServiceSession sessionService = retrofit.create(ApiServiceSession.class);
                Call<UserModelIn> calluser = sessionService.GetUserByToken(token);
                calluser.enqueue(new Callback<UserModelIn>() {
                    @Override
                    public void onResponse(Call<UserModelIn> call, Response<UserModelIn> response) {

                        if (response.message().equals("Bad Request")) {
                            Log.println(Log.INFO, "User", response.errorBody().toString());
                            Toast.makeText(getContext(), "No se pudo obtener el usuario", Toast.LENGTH_LONG).show();
                        } else {
                            user = response.body();
                            UserModelOut userOut = new UserModelOut();
                            userOut.setName(user.getName());
                            userOut.setPhone(user.getPhone());
                            userOut.setPassword(".");
                            userOut.setEmail(user.getEmail());
                            createDonationEvent();
                            donationEvent.setCreatorUser(userOut);
                            if (isValidEvent()) {
//                                Retrofit retrofit = ApiClient.getClient();
                                ApiServiceDonationEvent apiServiceDonationEvent = retrofit.create(ApiServiceDonationEvent.class);

                                Call<DonationEventModelOut> callevent = apiServiceDonationEvent.addDonationEvent(donationEvent);
                                callevent.enqueue(new Callback<DonationEventModelOut>() {
                                    @Override
                                    public void onResponse(Call<DonationEventModelOut> call, Response<DonationEventModelOut> response) {
                                        if (response.message().equals("Bad Request")) {
                                            Log.println(Log.INFO, "DonationEvent BAD", response.errorBody().toString());
                                            Toast.makeText(getContext(), R.string.EventFailureCommit, Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(getContext(), R.string.EventSuccessCommit, Toast.LENGTH_LONG).show();
                                            closeKeyboard();
                                            Navigation.findNavController(getView()).navigate(R.id.nav_homeFragment);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<DonationEventModelOut> call, Throwable t) {
                                        Log.println(Log.ERROR, "Event Create Failure", t.getMessage());
                                        Toast.makeText(getContext(), "Error al ingresar la campaña", Toast.LENGTH_LONG).show();
                                    }
                                });

                            } else {
                                Toast.makeText(getContext(), R.string.FormErrors, Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModelIn> call, Throwable t) {
                        Log.println(Log.ERROR, "User Failure", t.getMessage());
                        Toast.makeText(getContext(), "Error al obtener usuario", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

        btnaddCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDonationEvent();
                eventViewModel.setEvent(donationEvent);
                final NavController navigation = Navigation.findNavController(view);
                navigation.navigate(R.id.addCentersFragment);
            }
        });

    }

    private void createDonationEvent() {

        donationEvent.setName(editEventName.getText().toString());
        donationEvent.setDescription(txtEventDescription.getText().toString());
        donationEvent.setEmail(txtEventEmail.getText().toString());
        donationEvent.setValidityFrom(txtValidityFrom.getText().toString());
        donationEvent.setValidityUntil(txtValidityUntil.getText().toString());
        donationEvent.setPhone(txtPhone.getText().toString());
        donationEvent.setCenters(getCenters(centerAddressModel));
    }

    private List<AddressModelOut> getCenters(CenterAddressModel centerAddressModel) {
        List<AddressModelOut> centers = new ArrayList<>();
        AddressModelOut addressModelOut;
        if (centerAddressModel != null) {
            for (AddressModel addressModel : centerAddressModel.getMarkers()) {
                addressModelOut = new AddressModelOut();
                addressModelOut.setName(addressModel.getName());
                addressModelOut.setLatitute(addressModel.getLatitute());
                addressModelOut.setLongitute(addressModel.getLongitute());
                centers.add(addressModelOut);
            }
        }
        return centers;
    }

    private boolean isValidEvent() {
        return isValidName() &&
                areValidDates() &&
                isValidDescription() &&
                isValidEmail() &&
                areValidCenters() &&
                isValidPhone() &&
                isValidUser();
    }

    private boolean isValidUser() {
        return donationEvent.getCreatorUser() != null;
    }

    private boolean isValidPhone() {
        boolean isValid = true;

        if (TextUtils.isEmpty(donationEvent.getPhone())) {
            txtPhone.setError(getResources().getString(R.string.DateNull));
            // Toast.makeText(getContext(), getResources().getString(R.string.PhoneNull), Toast.LENGTH_LONG).show();
            isValid = false;
        }
        return isValid;
    }

    private boolean areValidCenters() {
        return donationEvent.getCenters().size() > 0;
    }

    private boolean isValidEmail() {
        boolean isValid = true;

        if (!Patterns.EMAIL_ADDRESS.matcher(donationEvent.getEmail()).matches()) {
            isValid = false;
            txtEventEmail.setError(getResources().getString(R.string.WrongEmail));
        }
        return isValid;
    }

    private boolean areValidDates() {

        return isValidFrom() && isValidUntil();
    }


    boolean isBeforeToday(Date d) {
        Date today = new Date();

        return isNotToday(d, today) && (d.compareTo(today) < 0);
    }

    private boolean isNotToday(Date d, Date today) {
        return (d.getTime() != today.getTime());
    }

    private boolean isValidFrom() {

        boolean isValid = true;

        if (TextUtils.isEmpty(donationEvent.getValidityFrom())) {
            txtValidityFrom.setError(getResources().getString(R.string.DateFromNull));
            Toast.makeText(getContext(), getResources().getString(R.string.DateFromNull), Toast.LENGTH_LONG).show();
            isValid = false;
        } else {
            try {
                Date from = new SimpleDateFormat("dd/MM/yyyy").parse(donationEvent.getValidityFrom());
                if (isBeforeToday(from)) {
                    txtValidityFrom.setError(getResources().getString(R.string.DateBeforeToday));
                    Toast.makeText(getContext(), getResources().getString(R.string.DateBeforeToday), Toast.LENGTH_LONG).show();
                    isValid = false;
                }
            } catch (ParseException e) {
                txtValidityFrom.setError(getResources().getString(R.string.DateFormat));
                Toast.makeText(getContext(), getResources().getString(R.string.DateFormat), Toast.LENGTH_LONG).show();
            }
        }
        return isValid;
    }

    private boolean isValidUntil() {


        boolean isValid = true;

        if (TextUtils.isEmpty(donationEvent.getValidityUntil())) {
            txtValidityUntil.setError(getResources().getString(R.string.DateUntilNull));
            //  Toast.makeText(getContext(), getResources().getString(R.string.DateUntilNull), Toast.LENGTH_LONG).show();
            isValid = false;
        } else {
            String fromString = txtValidityFrom.getText().toString();
            String untilString = txtValidityUntil.getText().toString();
            Date from = null;
            Date until = null;
            try {
                from = new SimpleDateFormat("dd/MM/yyyy").parse(fromString);
                until = new SimpleDateFormat("dd/MM/yyyy").parse(untilString);
                if (until.before(from)) {
                    txtValidityUntil.setError(getResources().getString(R.string.DateBeforeFrom));
                    //Toast.makeText(getContext(), getResources().getString(R.string.DateBeforeFrom), Toast.LENGTH_LONG).show();
                    isValid = false;
                }
            } catch (ParseException e) {
                txtValidityUntil.setError(getResources().getString(R.string.DateFormat));
                //  Toast.makeText(getContext(), getResources().getString(R.string.DateFormat), Toast.LENGTH_LONG).show();
            }
        }
        return isValid;
    }

    private boolean isValidDescription() {
        boolean isValid = true;

        if (TextUtils.isEmpty(donationEvent.getDescription())) {
            txtEventDescription.setError(getResources().getString(R.string.DescriptionNull));
            isValid = false;
        }
        return isValid;
    }

    private boolean isValidName() {
        boolean isValid = true;

        if (TextUtils.isEmpty(donationEvent.getName())) {
            editEventName.setError(getResources().getString(R.string.NameNull));
            isValid = false;
        }
        return isValid;
    }

    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}