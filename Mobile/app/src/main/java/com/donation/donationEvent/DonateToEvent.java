package com.donation.donationEvent;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.donation.R;
import com.donation.model.in.DonationEventModelIn;
import com.donation.utils.DatePickerFragment;

public class DonateToEvent extends Fragment {
    Button btnConfirmar;
    TextView txtEventName;
    EditText txtDate, txtdescription;
    Spinner spinCenter;
    DonationEventModelIn event;

    public DonateToEvent() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_donate_to_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnConfirmar = view.findViewById(R.id.btnContribution);
        txtDate = view.findViewById(R.id.txtcontributionDate);
        txtdescription = view.findViewById(R.id.txtContributionDesc);
        spinCenter = view.findViewById(R.id.spinContributionCenter);
        txtEventName = view.findViewById(R.id.txtNameEvent);

        if (getArguments().get("eventToDonate") != null) {
            event = (DonationEventModelIn) getArguments().get("eventToDonate");
            txtEventName.setText(event.getName());
            setCenters();
        }

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    if (sendMail()) {
                        askforCalendar();
                        Toast.makeText(getContext(), R.string.contributionSuccess, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), R.string.contributionFailure, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(txtDate);
            }
        });
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

    private String twoDigits(int n) {
        return (n <= 9) ? ("0" + n) : String.valueOf(n);
    }

    private void askforCalendar() {
        new AlertDialog.Builder(getContext())
                .setTitle(getResources().getString(R.string.menu_donation))
                .setMessage(getResources().getString(R.string.askCalendar))
                .setIcon(getResources().getDrawable(R.drawable.small_logo))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        addCalendar();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void setCenters() {
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getContext(),
                        R.layout.support_simple_spinner_dropdown_item,
                        event.getCentersName());
        spinCenter.setAdapter(adapter);
    }

    private boolean sendMail() {
        boolean ok = true;
        Intent mailIntent = new Intent(Intent.ACTION_SEND);
        mailIntent.setType("text/plain");
        mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{event.getEmail()});
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, getSubject());
        mailIntent.putExtra(Intent.EXTRA_TEXT, gettextMail());

        try {
            startActivity(Intent.createChooser(mailIntent, getResources().getString(R.string.sendMail)));
        } catch (Exception e) {
            Log.println(Log.ERROR, "Contribute Event", e.getMessage());
            ok = false;
        }
        return ok;
    }

    private String getSubject() {
        return getResources().getString(R.string.mailSubject) + " " + event.getName();
    }

    private String gettextMail() {
        String textMail;
        textMail = "Hola! \n\n Quiero contribuir para la campaña \n " +
                event.getName() + ".\n\n" +
                "Se entregará el " + txtDate.getText().toString() + ". \n\n " +
                "Centro de recepción " + spinCenter.getSelectedItem().toString();

        return textMail;
    }

    private String getDescriptionCalendar() {
        String descCalendar;
        descCalendar = "Recordatorio para la campaña \n " + event.getName();
        return descCalendar;
    }

    private boolean isValid() {
        boolean valid = true;
        if (TextUtils.isEmpty(txtDate.getText())) {
            txtDate.setError(getResources().getString(R.string.DateNull));
            valid = false;
        }
        return valid;
    }

    private void addCalendar() {
        Intent insertCalendarIntent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, getResources().getString(R.string.calendarTitle))
                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, txtDate.getText().toString())
                .putExtra(CalendarContract.Events.EVENT_LOCATION, spinCenter.getSelectedItem().toString())
                .putExtra(CalendarContract.Events.DESCRIPTION, getDescriptionCalendar()) // Description
                .putExtra(Intent.EXTRA_EMAIL, event.getEmail())
                .putExtra(CalendarContract.Events.ACCESS_LEVEL, CalendarContract.Events.ACCESS_PRIVATE)
                .putExtra(CalendarContract.Events.VISIBLE, CalendarContract.Events.ACCESS_PRIVATE)
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);
        startActivity(insertCalendarIntent);
    }
}