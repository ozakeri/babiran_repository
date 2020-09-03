package Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.babiran.app.MainActivity;
import net.babiran.app.R;
import net.babiran.app.Servic.MyInterFace;
import net.babiran.app.Servic.MyMesa;
import net.babiran.app.Servic.MyServices;
import net.babiran.app.Sharj.SharjHistoryActivity;
import net.babiran.app.SharjActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import Adapters.ProductListAdapter;
import Models.Color;
import Models.Feature;
import Models.Image;
import Models.Moshakhasat;
import Models.Product;
import retrofit2.Call;
import retrofit2.Callback;
import tools.AppConfig;
import tools.Util;

public class ShajeFrgment extends Fragment {

    private String Mablagh = "88", Type = "88", operator = "88"; //Type=1=>mostaghim   && operator= 1 =>irancel ,2=>hamrahaval ,3=>rightel
    private ImageView Irancell, Hamrah, Righttel;
    private LinearLayout btn, History;
    private EditText editText;
    private TextView tx;
    public static final int REQUEST_CODE_PAY = 101;
    private RelativeLayout layout_irancell, layout_hamrah, layout_ritel;
    private LinearLayout layout_afterSelect;


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
        editText = (EditText) view.findViewById(R.id.ed_number);
        tx = (TextView) view.findViewById(R.id.txt_show_op);
        editText.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "IRANSansMobile(FaNum)_Bold.ttf"));
        History = (LinearLayout) view.findViewById(R.id.kdjfnbgkjdfnkj);
        History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SharjHistoryActivity.class));
            }
        });

        btn = (LinearLayout) view.findViewById(R.id.pymeny_sharj);
        RadioBTNMablagh(view);
        RadioBTNType(view);
        opretator();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(editText.getText().toString())) {
                    if (!Mablagh.equals("88") && !Type.equals("88") && !operator.equals("88")) {
                        SenDToServer();
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
                Mablagh = "1000";
            }
        });

        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r2.setChecked(true);
                r1.setChecked(false);
                r3.setChecked(false);
                r4.setChecked(false);
                Mablagh = "2000";
            }
        });

        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r3.setChecked(true);
                r2.setChecked(false);
                r1.setChecked(false);
                r4.setChecked(false);
                Mablagh = "5000";
            }
        });

        r4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r4.setChecked(true);
                r2.setChecked(false);
                r3.setChecked(false);
                r1.setChecked(false);
                Mablagh = "10000";
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
                operator = "1";
                tx.setText("ایرانسل");
                tx.setVisibility(View.VISIBLE);
                layout_irancell.setBackgroundResource(R.color.yellow_transparent);
                layout_hamrah.setBackgroundResource(0);
                layout_ritel.setBackgroundResource(0);
            }
        });
        layout_hamrah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_afterSelect.setVisibility(View.VISIBLE);
                operator = "2";
                tx.setText("همراه اول");
                tx.setVisibility(View.VISIBLE);
                layout_hamrah.setBackgroundResource(R.color.green_transparent);
                layout_irancell.setBackgroundResource(0);
                layout_ritel.setBackgroundResource(0);
            }
        });
        layout_ritel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_afterSelect.setVisibility(View.VISIBLE);
                operator = "3";
                tx.setText("رایتل");
                tx.setVisibility(View.VISIBLE);
                layout_ritel.setBackgroundResource(R.color.forooze_transparent);
                layout_irancell.setBackgroundResource(0);
                layout_hamrah.setBackgroundResource(0);
            }
        });
    }

    private void SenDToServer() {


        try {
            MyInterFace n = MyServices.createService(MyInterFace.class);
            System.out.println("---------" + editText.getText().toString() + " - " + Mablagh + " - " + Type + " - " + operator);
            Call<MyMesa> call = n.BuySahrj(editText.getText().toString(), Mablagh, Type, operator);

            call.enqueue(new Callback<MyMesa>() {
                @Override
                public void onResponse(@NonNull Call<MyMesa> call, @NonNull retrofit2.Response<MyMesa> response) {
                    try {
                        if (response.body() != null) {
                            System.out.println("response======" + response.body());
                            Integer fetching = response.body().getSuccess();

                            if (fetching == 1) {

                                Log.e("URL  ", response.body().getUrl());
                          /*  Log.e("URL  ", response.body().getUrl());
                            Intent intent = new Intent(SharjActivity.this, Actip2.class);
                            intent.putExtra("url", response.body().getUrl());
                            intent.putExtra("sharj", "sharj");*/

                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(response.body().getUrl()));
                                //startActivityForResult(intent, REQUEST_CODE_PAY);
                                startActivity(browserIntent);
                                getActivity().finish();
                            } else {
                                Toast.makeText(getActivity(), "مشکلی در ارتباط با سرور پیش امده", Toast.LENGTH_LONG).show();
                            }
                        }

                    } catch (Exception e) {
                        Log.e("EX", e.getMessage());
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
                    // handle back button's click listener


                    if (MainActivity.productlist.getVisibility() == View.VISIBLE) {
                        System.out.println("===MainActivity==111===");
                        //MainActivity.productlist.setVisibility(View.INVISIBLE);
                        FragmentManager fm = getFragmentManager();
                        if (fm != null) {
                            ProductListFragment fragm = (ProductListFragment) fm.findFragmentById(R.id.ProductListcontainer);
                            if (fragm != null) {
                                fragm.backpress();
                            }
                        }


                    } else {
                        System.out.println("===MainActivity==333===");
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

                    }


                    return true;
                }
                return false;
            }
        });
    }
}
