package net.babiran.app.Ticket.Page;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import net.babiran.app.R;
import net.babiran.app.Ticket.Activity.ShowFinalActivity;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Ghater extends Fragment implements DatePickerDialog.OnDateSetListener {
    private List<String> HashAllCarBrand;
    private View mView;
    LinearLayout ln_BTN;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_ghater, container, false);

        INIT();
        return mView;
    }

    private void INIT() {
        ln_BTN = (LinearLayout) mView.findViewById(R.id.dfhfh);
        ln_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ShowFinalActivity.class));
            }
        });
    }


    //define Brand Spiner
    private void SpinerBrandSet() {


        Spinner SpinnerBRAND = (Spinner) mView.findViewById(R.id.spn_frg_gen_brand);

        SpinnerBRAND.setAdapter(new ArrayAdapter<>(getContext(), R.layout
                .layout_spinner_textview, R.id.textView31, HashAllCarBrand));
        //   SpinnerBRAND.setSelection(0);

        SpinnerBRAND.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  Toast.makeText(RegisterCarActivity.this,HashAllCarBrand.get(position),Toast.LENGTH_SHORT).show();

                HashAllCarBrand.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
