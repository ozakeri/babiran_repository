package net.babiran.app.Ticket.Page;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import net.babiran.app.R;
import net.babiran.app.Ticket.Activity.ShowDetileActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Otobos extends Fragment implements DatePickerDialog.OnDateSetListener {


    LinearLayout ln_BTN;
    private View mView;
    private List<String> ListOstan = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_otobos, container, false);
        INIT();
        return mView;
    }


    private void INIT() {
        ln_BTN = (LinearLayout) mView.findViewById(R.id.rggggggg);
        ln_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ShowDetileActivity.class));
            }
        });
    }


    private void DateButton() {
        PersianCalendar persianCalendarqw = new PersianCalendar();
        persianCalendarqw.setPersianDate(persianCalendarqw.getPersianYear(), persianCalendarqw.getPersianMonth() + 1, persianCalendarqw.getPersianDay() - 10);
        ///////////
        PersianCalendar persianCalendar = new PersianCalendar();

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                persianCalendar.getPersianYear(),
                persianCalendar.getPersianMonth(),
                persianCalendar.getPersianDay()

        );

        datePickerDialog.setMaxDate(persianCalendarqw);

        datePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    }


}
