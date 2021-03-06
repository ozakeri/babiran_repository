package Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.babiran.app.MainActivity;
import net.babiran.app.R;
import net.babiran.app.Servic.MyInterFace;
import net.babiran.app.Servic.MyMesa;
import net.babiran.app.Servic.MyServices;
import net.babiran.app.Sharj.SharjHistoryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import tools.AppConfig;

public class ShajeFrgment extends Fragment {

    private String Mablagh = "88", Type = "88"; //Type=1=>mostaghim   && operator= 1 =>irancel ,2=>hamrahaval ,3=>rightel
    private int operator = 0;
    private ImageView Irancell, Hamrah, Righttel;
    private LinearLayout History;
    private EditText editText;
    private TextView tx;
    public static final int REQUEST_CODE_PAY = 101;
    private RelativeLayout layout_irancell, layout_hamrah, layout_ritel;
    private LinearLayout layout_afterSelect, pymeny_sharj;
    private RequestQueue queue;


    public ShajeFrgment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_transaction, container, false);
        AppConfig.frag = ShajeFrgment.this;

        INIT(view);


        return view;

    }

    private void INIT(View view) {
        Irancell = view.findViewById(R.id.irancel);
        Hamrah = view.findViewById(R.id.hamrahaval);
        Righttel = view.findViewById(R.id.righttel);
        layout_irancell = view.findViewById(R.id.layout_irancell);
        layout_hamrah = view.findViewById(R.id.layout_hamrah);
        layout_ritel = view.findViewById(R.id.layout_ritel);
        layout_afterSelect = view.findViewById(R.id.layout_afterSelect);
        pymeny_sharj = view.findViewById(R.id.pymeny_sharj);
        editText = view.findViewById(R.id.ed_number);
        tx = (TextView) view.findViewById(R.id.txt_show_op);
        editText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "IRANSansMobile(FaNum)_Bold.ttf"));

        History = (LinearLayout) view.findViewById(R.id.kdjfnbgkjdfnkj);
        History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SharjHistoryActivity.class));
            }
        });

        RadioBTNMablagh(view);
        RadioBTNType(view);
        opretator();


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText.getText().toString().length() > 0) {
                    pymeny_sharj.setVisibility(View.VISIBLE);
                } else {
                    pymeny_sharj.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        pymeny_sharj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(editText.getText().toString())) {

                    if (!Mablagh.equals("88") && !Type.equals("88") && operator != 0) {
                        //startActivity(new Intent(getActivity(), AfterOrderActivity.class));

                        String number = editText.getText().toString();
                        detectNumber(number);
                    } else {
                        Toast.makeText(getActivity(), "تمام موارد را به درستی انتخاب نمایید", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "شماره همراه خود را صحیح وارد نمایید", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void RadioBTNMablagh(View view) {
        //RadioGroup rg = (RadioGroup) view.findViewById(R.id.myRadioGroup);
        RadioButton r1 = view.findViewById(R.id.hezar);
        RadioButton r2 = view.findViewById(R.id.dohezar);
        RadioButton r3 = view.findViewById(R.id.hezar5);
        RadioButton r4 = view.findViewById(R.id.hezar10);
        RadioButton mostaghim = view.findViewById(R.id.mostaghim);
        RadioButton ghiremostaghim = view.findViewById(R.id.ghiremostaghim);
        r1.setTypeface((Typeface.createFromAsset(getActivity().getAssets(), "IRANSansMobile(FaNum)_Bold.ttf")));
        r2.setTypeface((Typeface.createFromAsset(getActivity().getAssets(), "IRANSansMobile(FaNum)_Bold.ttf")));
        r3.setTypeface((Typeface.createFromAsset(getActivity().getAssets(), "IRANSansMobile(FaNum)_Bold.ttf")));
        r4.setTypeface((Typeface.createFromAsset(getActivity().getAssets(), "IRANSansMobile(FaNum)_Bold.ttf")));
        mostaghim.setTypeface((Typeface.createFromAsset(getActivity().getAssets(), "IRANSansMobile(FaNum)_Bold.ttf")));
        ghiremostaghim.setTypeface((Typeface.createFromAsset(getActivity().getAssets(), "IRANSansMobile(FaNum)_Bold.ttf")));

      /*  r1.setText(Util.latinNumberToPersian(Util.convertToFormalString(("1000"))));
        r2.setText(Util.latinNumberToPersian(Util.convertToFormalString(("2000"))));
        r3.setText(Util.latinNumberToPersian(Util.convertToFormalString(("5000"))));
        r4.setText(Util.latinNumberToPersian(Util.convertToFormalString(("10000"))));*/

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setChecked(true);
                r2.setChecked(false);
                r3.setChecked(false);
                r4.setChecked(false);
                Mablagh = "5000";
            }
        });

        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r2.setChecked(true);
                r1.setChecked(false);
                r3.setChecked(false);
                r4.setChecked(false);
                Mablagh = "10000";
            }
        });

        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r3.setChecked(true);
                r2.setChecked(false);
                r1.setChecked(false);
                r4.setChecked(false);
                Mablagh = "15000";
            }
        });

        r4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r4.setChecked(true);
                r2.setChecked(false);
                r3.setChecked(false);
                r1.setChecked(false);
                Mablagh = "20000";
            }
        });

      /*  rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.hezar:
                        Mablagh = "1000";
                        // do operations specific to this selection
                        break;
                    case R.id.dohezar:
                        Mablagh = "2000";
                        // do operations specific to this selection
                        break;
                    case R.id.hezar5:
                        Mablagh = "5000";
                        // do operations specific to this selection
                        break;
                    case R.id.hezar10:
                        Mablagh = "10000";
                        // do operations specific to this selection
                        break;

                }
            }
        });*/
    }

    private void RadioBTNType(View view) {
        RadioGroup rg = (RadioGroup) view.findViewById(R.id.myRadi);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.mostaghim:
                        Type = "1";
                        // do operations specific to this selection
                        break;
                    case R.id.ghiremostaghim:
                        Type = "0";
                        // do operations specific to this selection
                        break;

                }
            }
        });
    }

    private void opretator() {
        layout_irancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_afterSelect.setVisibility(View.VISIBLE);
                operator = 1;
                tx.setText("ایرانسل");
                tx.setVisibility(View.VISIBLE);
                layout_irancell.setBackgroundResource(R.color.forooze_transparent);
                layout_hamrah.setBackgroundResource(0);
                layout_ritel.setBackgroundResource(0);
            }
        });
        layout_hamrah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_afterSelect.setVisibility(View.VISIBLE);
                operator = 2;
                tx.setText("همراه اول");
                tx.setVisibility(View.VISIBLE);
                layout_hamrah.setBackgroundResource(R.drawable.hamrah_background);
                layout_irancell.setBackgroundResource(0);
                layout_ritel.setBackgroundResource(0);
            }
        });
        layout_ritel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_afterSelect.setVisibility(View.VISIBLE);
                operator = 3;
                tx.setText("رایتل");
                tx.setVisibility(View.VISIBLE);
                layout_ritel.setBackgroundResource(R.drawable.rigtel_background);
                layout_irancell.setBackgroundResource(0);
                layout_hamrah.setBackgroundResource(0);
            }
        });
    }

    private void SenDToServer(String number) {
        try {
            MyInterFace n = MyServices.createService(MyInterFace.class);
            Call<MyMesa> call = n.BuySahrj(number, Mablagh, Type, String.valueOf(operator));

            call.enqueue(new Callback<MyMesa>() {
                @Override
                public void onResponse(@NonNull Call<MyMesa> call, @NonNull retrofit2.Response<MyMesa> response) {
                    try {
                        if (response.body() != null) {
                            Integer fetching = response.body().getSuccess();

                            if (fetching == 1) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(response.body().getUrl()));
                                startActivity(browserIntent);
                            } else {
                                Toast.makeText(getActivity(), "مشکلی در ارتباط با سرور پیش امده", Toast.LENGTH_LONG).show();
                            }
                        }

                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }


                }

                @Override
                public void onFailure(Call<MyMesa> call, Throwable t) {
                    Log.e("response 2  :", t.getMessage() + "\n" + t.toString());
                }
            });
        } catch (Exception ex) {
            Log.e("response 3 :", ex.getMessage());
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.layout_search.setVisibility(View.VISIBLE);
        MainActivity.btnBack.setVisibility(View.INVISIBLE);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(AppConfig.act);
                    builder.setTitle("می خواهید خارج شوید؟");
                    builder.setPositiveButton("بله", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if (AppConfig.checkReciveSms == true) {
                                AppConfig.checkReciveSms = false;
                            }
                            if (AppConfig.btnSubmitOk == true) {
                                AppConfig.btnSubmitOk = false;
                            }

                            AppConfig.act.finish();


                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //TODO
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    // }


                    return true;
                }
                return false;
            }
        });
    }

    public void detectNumber(String number) {

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        final String url = "http://babiran.net/api/checkoperator/" + number;

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());
                                if (!jsonObject.isNull("OperatorID")) {
                                    int operatorID = Integer.parseInt(jsonObject.getString("OperatorID"));

                                    if (operatorID == -1) {
                                        Toast.makeText(getActivity(), "شماره وارد شده معتبر نمی باشد", Toast.LENGTH_LONG).show();
                                        return;
                                    }

                                    if (operatorID == operator) {
                                        SenDToServer(number);
                                    } else {
                                        Toast.makeText(getActivity(), "شماره وارد شده با اپراتور همخوانی ندارد", Toast.LENGTH_LONG).show();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            System.out.println("response====" + response.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AppConfig.error(error);
                    }
                }
        );

        queue.add(getRequest);

    }
}
