package net.babiran.app.Ticket.Page;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import net.babiran.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParvazKhareji extends Fragment implements DatePickerDialog.OnDateSetListener {

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_parvaz_khareji, container, false);
        return mView;
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
