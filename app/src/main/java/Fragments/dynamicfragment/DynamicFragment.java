package Fragments.dynamicfragment;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import net.babiran.app.AppController;
import net.babiran.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Fragments.BasketListFragment;
import Models.TimeList;
import tools.AppConfig;
import tools.Util;
import ui_elements.DateTime;


public class DynamicFragment extends Fragment {
    View view;
    int val;
    private List<TimeList> timeLists = new ArrayList<>();
    private LinearLayout horizontal, leftLayoutRadioButton;
    SharedPreferences.Editor editor = AppController.getInstance().getSharedPreferences().edit();


    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dynamic, container, false);
        val = getArguments().getInt("someInt", 0);
        horizontal = view.findViewById(R.id.horizontal);
        leftLayoutRadioButton = view.findViewById(R.id.leftLayoutRadioButton);


        RadioGroup radioGroup = new RadioGroup(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 15);
        leftLayoutRadioButton.addView(radioGroup, params);


        String delivery_time = AppController.getInstance().getSharedPreferences().getString("delivery_time", "");
        if (delivery_time != null) {
            try {
                JSONArray jsonArray = new JSONArray(delivery_time);
                JSONObject jsonObject = jsonArray.getJSONObject(val);
                JSONArray jsonArray1 = jsonObject.getJSONArray("time_list");
                timeLists.clear();

                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                    TimeList timeList = new TimeList(jsonObject1.getString("id"), jsonObject1.getString("text"));
                    DateTime dateTime = new DateTime(AppConfig.act, timeList);
                    horizontal.addView(dateTime);
                    timeLists.add(timeList);

                    int status = Integer.parseInt(jsonObject1.getString("status"));
                    RadioButton radioButtonView = new RadioButton(getActivity());
                    radioButtonView.setText(Util.latinNumberToPersian(jsonObject1.getString("text")));
                    radioButtonView.setId(Integer.parseInt(jsonObject1.getString("id")));
                    radioButtonView.setTextSize(14);
                    radioButtonView.setTypeface((Typeface.createFromAsset(getActivity().getAssets(), "IRANSansMobile(FaNum)_Bold.ttf")));
                    RadioGroup.LayoutParams params1 = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params1.setMargins(15, 15, 15, 15);
                    radioButtonView.setLayoutParams(params1);
                    radioGroup.addView(radioButtonView, params1);

                    if (status == 0){
                        radioButtonView.setTextColor(getResources().getColor(R.color.blue_semi_transparent_pressed));
                        radioButtonView.setText(Util.latinNumberToPersian(jsonObject1.getString("text")) + "      تکمیل ظرفیت ");
                        radioButtonView.setEnabled(false);
                    }

                    radioButtonView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                editor.putString("selectTimeStr", String.valueOf(radioButtonView.getText()));
                                editor.putString("timeId", String.valueOf(radioButtonView.getId()));
                                editor.putString("timeValue", jsonObject.getString("date"));
                                editor.apply();

                                BasketListFragment.hidden();
                                radioButtonView.setChecked(false);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return view;
    }

    public static DynamicFragment addfrag(int val) {
        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", val);
        fragment.setArguments(args);
        return fragment;
    }
}