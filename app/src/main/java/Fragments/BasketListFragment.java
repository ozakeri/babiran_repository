package Fragments;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;

import net.babiran.app.MainActivity;
import net.babiran.app.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;

import Adapters.BasketListAdapter;
import Handlers.DatabaseHandler;
import Models.Basket;
import Models.EventbusModel;
import tools.AppConfig;
import tools.GlobalValues;
import ui_elements.MyTextView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static tools.AppConfig.products;


public class BasketListFragment extends Fragment implements
        AdapterView.OnItemSelectedListener {

    public String category_id = "0";
    public String category_parent_id = "-1";
    private GlobalValues globalValues = new GlobalValues();

    String[] payment = {"پرداخت با کارت خوان درب منزل", "پرداخت نقدی در محل تحویل", "پرداخت آنلاین از درگاه بانکی"};
    View v;
    RequestQueue queue;
    RecyclerView basket_recyclerView;
    String prev = "";
    String id = "";
    String address1 = "";
    String address2 = "";
    MyTextView totalprice, AddValue, PayValue;
    MyTextView discount;
    public ArrayList<String> addresses;

    LinearLayout addLinear, typePayLinear;

    String selectedAdd = "";
    String selectedPay = "";
    private int credit = 0;

    PopupWindow mPopupWindow;
    int TotalPrice = 0;
    int discountPrice = 0;
    int rawPrice = 0;

    int rawPrice_dis = 0;
    String basketjson = "";

    RelativeLayout completeBuy;
    public static final String TAG = "TAG";
    DatabaseHandler db;
    ImageView listsabad;
    public static boolean needToRefrish = false;
    private BasketListAdapter adp;


    public BasketListFragment() {
        MainActivity.btnBack.setVisibility(View.VISIBLE);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.basket_list_fragment, container, false);

        final SharedPreferences.Editor editor = getActivity().getSharedPreferences("productsArray", MODE_PRIVATE).edit();

        addresses = new ArrayList<>();
        db = new DatabaseHandler(getActivity());

        if (db.getRowCount() > 0) {
            HashMap<String, String> userDetailsHashMap = db.getUserDetails();

            id = userDetailsHashMap.get("id");
            address1 = userDetailsHashMap.get("address");
            address2 = userDetailsHashMap.get("address2");

//            AppConfig.tempActivity = (MainActivity) getActivity();
            basket_recyclerView = v.findViewById(R.id.basket_recyclerView);

            AddValue = (MyTextView) v.findViewById(R.id.addValue);
            PayValue = (MyTextView) v.findViewById(R.id.payValue);

            totalprice = (MyTextView) v.findViewById(R.id.totalprice);
            discount = (MyTextView) v.findViewById(R.id.dis_txt);
            completeBuy = (RelativeLayout) v.findViewById(R.id.compelete);

            addLinear = (LinearLayout) v.findViewById(R.id.addressLinear);
            typePayLinear = (LinearLayout) v.findViewById(R.id.paymentLinear);


            basket_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            basket_recyclerView.setNestedScrollingEnabled(false);

            updateList();
            if (products != null){
                adp = new BasketListAdapter(getActivity(), products,BasketListFragment.this);
                adp.notifyDataSetChanged();
                basket_recyclerView.setAdapter(adp);
            }


            // discountPrice = rawPrice - TotalPrice ;
            discountPrice = rawPrice - rawPrice_dis;

            if (!address1.equals("null") && address1.length() > 0) {
                addresses.add(address1);
            }
            if (!address2.equals("null") && address2.length() > 0) {
                addresses.add(address2);
            }

            addLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);

                    if ((address1.equals("") && address2.equals("")) || (address1.equals("null") && address2.equals("null")) || (address1 == null && address2 == null)) {
                        EditProfileFrgment.prev_edit = "basket";


                        //Toast.makeText(getActivity(), "لطفا مشخصات خود را تکمیل کنید", Toast.LENGTH_LONG).show();
                        LayoutInflater inflaterToast = getLayoutInflater();
                        View layout = inflaterToast.inflate(R.layout.toast, (ViewGroup) getActivity().findViewById(R.id.toast_layout_root));

                        TextView text = (TextView) layout.findViewById(R.id.text);
                        text.setText("لطفا پروفایل کاربری خود را تکمیل کنید");

                        Toast toast = new Toast(getActivity());
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                        //AppConfig.fragmentManager.beginTransaction().replace(R.id.Editcontainer, new EditProfileFrgment(EditProfileFrgment.prev_edit, true)).commit();
                        //MainActivity.basketlist.setVisibility(View.INVISIBLE);
                        return;
                    }

                    // Inflate the custom layout/view
                    View customView = inflater.inflate(R.layout.address_pop_up, null);


                    // Initialize a new instance of popup window
                    mPopupWindow = new PopupWindow(
                            customView,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );

                    mPopupWindow.setFocusable(true);
                    mPopupWindow.update();


                    // Set an elevation value for popup window
                    // Call requires API level 21
                    if (Build.VERSION.SDK_INT >= 21) {
                        mPopupWindow.setElevation(5.0f);
                    }


                    final MyTextView firstAdd = (MyTextView) customView.findViewById(R.id.firstAdd);
                    final MyTextView secondAdd = (MyTextView) customView.findViewById(R.id.secondAdd);
                    final CheckBox firstChk = (CheckBox) customView.findViewById(R.id.firstCheck);
                    final CheckBox secondChk = (CheckBox) customView.findViewById(R.id.secondCheck);

                    final LinearLayout firstAddLinear = (LinearLayout) customView.findViewById(R.id.firstAddLinear);
                    final LinearLayout secondAddLinear = (LinearLayout) customView.findViewById(R.id.secondAddLinear);


                    // Get a reference for the custom view close button
                    ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);
                    LinearLayout confirm = (LinearLayout) customView.findViewById(R.id.confirmAdd);

                    firstChk.setChecked(true);

                    firstAddLinear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            secondChk.setChecked(false);
                            firstChk.setChecked(true);
                        }
                    });

                    secondAddLinear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            firstChk.setChecked(false);
                            secondChk.setChecked(true);

                        }
                    });

                    firstChk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            secondChk.setChecked(false);
                        }
                    });


                    secondChk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            firstChk.setChecked(false);
                        }
                    });


                    if (!address1.equals("null") && !address1.equals("") && address1.length() > 0) {
                        firstAdd.setText(address1);
                    } else {
                        firstAddLinear.setVisibility(View.GONE);

                    }
                    if (!address2.equals("null") && !address2.equals("") && address2.length() > 0) {
                        secondAdd.setText(address2);
                    } else {
                        secondAddLinear.setVisibility(View.GONE);
                    }

                    // Set a click listener for the popup window close button
                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Dismiss the popup window
                            mPopupWindow.dismiss();
                        }
                    });

                    confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (firstChk.isChecked()) {
                                selectedAdd = address1;
                                AddValue.setText(address1);
                                mPopupWindow.dismiss();

                            }
                            if (secondChk.isChecked()) {
                                selectedAdd = address2;
                                AddValue.setText(address2);

                                mPopupWindow.dismiss();
                            }
                        }
                    });

                    // Finally, show the popup window at the center location of root relative layout
                    mPopupWindow.showAtLocation(addLinear, Gravity.CENTER, 0, 0);
                }
            });
            typePayLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);

                    // Inflate the custom layout/view
                    View customView = inflater.inflate(R.layout.type_pay_pop_up, null);


                    // Initialize a new instance of popup window
                    mPopupWindow = new PopupWindow(
                            customView,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );

                    mPopupWindow.setFocusable(true);
                    mPopupWindow.update();


                    // Set an elevation value for popup window
                    // Call requires API level 21
                    if (Build.VERSION.SDK_INT >= 21) {
                        mPopupWindow.setElevation(5.0f);
                    }


                    //       final MyTextView firstPay = (MyTextView) customView.findViewById(R.id.firstPay);
                    final MyTextView secondPay = (MyTextView) customView.findViewById(R.id.secondPay);
                    final MyTextView _3secondPay = (MyTextView) customView.findViewById(R.id._3secondPay);
                    final MyTextView txt_credit = (MyTextView) customView.findViewById(R.id.txt_credit);
                    //    final CheckBox firstChk = (CheckBox) customView.findViewById(R.id.firstCheck_pay);
                    final CheckBox secondChk = (CheckBox) customView.findViewById(R.id.secondCheck_pay);
                    final CheckBox _3Chk = (CheckBox) customView.findViewById(R.id._3secondCheck_pay);
                    final CheckBox checkbox_credit = (CheckBox) customView.findViewById(R.id.checkbox_credit);

                    //   final LinearLayout firstPayLinear = (LinearLayout) customView.findViewById(R.id.firstPayLinear);
                    final LinearLayout secondPayLinear = (LinearLayout) customView.findViewById(R.id.secondPayLinear);
                    final LinearLayout _3PayLinear = (LinearLayout) customView.findViewById(R.id._3PayLinear);
                    final LinearLayout layout_credit = (LinearLayout) customView.findViewById(R.id.layout_credit);


                    // Get a reference for the custom view close button
                    ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);
                    LinearLayout confirm = (LinearLayout) customView.findViewById(R.id.confirmPay);

                    //      firstChk.setChecked(true);

             /*       firstPayLinear.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                          //  secondChk.setChecked(false);
                        //    firstChk.setChecked(true);
                          //  _3Chk.setChecked(false);
                        }
                    });
*/
                    secondPayLinear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            secondChk.setChecked(true);
                            _3Chk.setChecked(false);
                            checkbox_credit.setChecked(false);

                        }
                    });
                    _3PayLinear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            secondChk.setChecked(false);
                            _3Chk.setChecked(true);
                            checkbox_credit.setChecked(false);

                        }
                    });


                    secondChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                _3Chk.setChecked(false);
                                checkbox_credit.setChecked(false);
                            }
                        }
                    });

                    checkbox_credit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                _3Chk.setChecked(false);
                                secondChk.setChecked(false);
                            }
                        }
                    });

                    _3Chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                checkbox_credit.setChecked(false);
                                secondChk.setChecked(false);
                            }
                        }
                    });

                    layout_credit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //           firstChk.setChecked(false);
                            secondChk.setChecked(false);
                            checkbox_credit.setChecked(true);
                            _3Chk.setChecked(false);

                        }
                    });


                    //     firstChk.setClickable(false);
                    //_3Chk.setClickable(false);

//                    firstChk.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            secondChk.setChecked(false);
//                        }
//                    });
//
                    secondChk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            _3Chk.setChecked(false);
                            //Toast.makeText(getActivity(), "فعلا امکان پرداخت از طریق کارتخوان بی سیم مقدور نمی باشد", Toast.LENGTH_LONG).show();
                        }
                    });


                    // Set a click listener for the popup window close button
                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Dismiss the popup window
                            mPopupWindow.dismiss();
                        }
                    });

                    confirm.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (secondChk.isChecked()) {
                                        selectedPay = "1";
                                        credit = 0;
                                        PayValue.setText(secondPay.getText().toString());
                                        mPopupWindow.dismiss();
                                    }

                                    if (_3Chk.isChecked()) {
                                        selectedPay = "2";
                                        credit = 0;
                                        PayValue.setText(_3secondPay.getText().toString());
                                        mPopupWindow.dismiss();
                                    }

                                    if (checkbox_credit.isChecked()) {
                                        selectedPay = "2";
                                        credit = 1;
                                        PayValue.setText(txt_credit.getText().toString());
                                        mPopupWindow.dismiss();
                                    }

                                }
                            });

                    // Finally, show the popup window at the center location of root relative layout
                    mPopupWindow.showAtLocation(addLinear, Gravity.CENTER, 0, 0);
                }
            });


            if (rawPrice > 0) {
                totalprice.setText(" جمع کل : " + ConvertEnToPe(convertToFormalString(String.valueOf(rawPrice_dis))) + " تومان ");
            } else {
                typePayLinear.setVisibility(View.GONE);
                addLinear.setVisibility(View.GONE);
                completeBuy.setVisibility(View.GONE);
                discount.setVisibility(View.GONE);
            }

            if (discountPrice > 0) {
                discount.setText(" تخفیف : " + ConvertEnToPe(convertToFormalString(String.valueOf(discountPrice))) + " تومان ");
            }

            completeBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//
                    if (products.size() > 0) {
                        if ((address1.equals("") && address2.equals("")) || (address1.equals("null") && address2.equals("null")) || (address1 == null && address2 == null)) {
                            EditProfileFrgment.prev_edit = "basket";
                            Toast.makeText(getActivity(), "لطفا پروفایل کاربری خود را تکمیل کنید", Toast.LENGTH_LONG).show();
                            //AppConfig.fragmentManager.beginTransaction().replace(R.id.Editcontainer, new EditProfileFrgment(EditProfileFrgment.prev_edit,true)).commit();
                            //MainActivity.basketlist.setVisibility(View.INVISIBLE);

                        } else {
                            if (AddValue.getText().toString() != null && !AddValue.getText().toString().equals("") && !AddValue.getText().toString().equals("null")) {

                                if (PayValue.getText().toString() != null && !PayValue.getText().toString().equals("") && !PayValue.getText().toString().equals("null")) {

                                    FragmentManager fm = getActivity().getFragmentManager();

                                    DescriptionDialog descriptionDialog = new DescriptionDialog(getActivity(), id, selectedAdd, basketjson, selectedPay, credit, editor);
                                    descriptionDialog.show(fm, "DescriptionDialog");

                                    //totalprice.setText("");
                                    AddValue.setText("");
                                    PayValue.setText("");

                                } else {
                                    Toast.makeText(getActivity(), "لطفا نوع پرداخت را انتخاب نمایید", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(getActivity(), "لطفا آدرس خود را مشخص نمایید", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else
                        Toast.makeText(getActivity(), "شما محصولی در سبد خرید ندارید", Toast.LENGTH_LONG).show();

                }
            });

        }

        /*getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    if (MainActivity.product.getVisibility() == View.VISIBLE) {
                        MainActivity.product.setVisibility(View.INVISIBLE);

                    }
                   *//* if(prev.equals("category")){
                        OtherCategoryActivity.productContainer.setVisibility(View.INVISIBLE);

                    }*//*
                    else {
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

                    }*/
        // return true;

        //}

        // return false;
        //}

        // });

        return v;

    }


    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();

        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }


    public String ConvertEnToPe(String value) {
        char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            if (Character.isDigit(value.charAt(i))) {
                builder.append(arabicChars[(int) (value.charAt(i)) - 48]);
            } else {
                builder.append(value.charAt(i));
            }
        }
        return builder.toString();
    }

    public String convertToFormalString(String input) {
        String priceString = "";
        for (int i = 0; i < input.length(); i++) {
            int j = input.length() - i;
            if (j % 3 != 1) {
                priceString += input.substring(i, i + 1);
            } else {
                priceString += input.substring(i, i + 1) + ",";
            }

        }
        return priceString.substring(0, priceString.length() - 1);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();


        MainActivity.layout_search.setVisibility(View.VISIBLE);
        MainActivity.btnBack.setVisibility(View.INVISIBLE);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();


        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener

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

                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Subscribe
    public void getEvent(EventbusModel model) {
        if (model.isBasket()) {
            updateList();
        }
    }

    public void updateList() {

        TotalPrice = 0;
        discountPrice = 0;
        rawPrice = 0;
        rawPrice_dis = 0;
        int p = 0;
        int p_dis = 0;
        int dis1 = 0;
        int dis2 = 0;
        int dis3 = 0;
        int count = 0;
        listsabad = (ImageView) v.findViewById(R.id.listsabad);
        ArrayList<Basket> baskets = new ArrayList<>();

        if (products != null) {
            if (products.size() > 0) {
                if (listsabad.getVisibility() == View.VISIBLE) {
                    listsabad.setVisibility(View.INVISIBLE);
                }
                for (int i = 0; i < products.size(); i++) {
                    Basket basket = new Basket();
                    basket.setProduct_id(products.get(i).getId());
                    basket.setCount(products.get(i).count);
                    basket.setColorCode(products.get(i).getColorCode());
                    baskets.add(basket);
                }
                Gson gson = new Gson();
                basketjson = gson.toJson(baskets);
               // adp = new BasketListAdapter(getActivity(), products,BasketListFragment.this);
               // adp.notifyDataSetChanged();
               // basket_recyclerView.setAdapter(adp);

                for (int i = 0; i < products.size(); i++) {

                    if (products.get(i).getPrice() != null && !products.get(i).getPrice().equals("null") && !products.get(i).getPrice().equals("")) {
                        p = Integer.parseInt(products.get(i).getPrice());
                    }
                    if (products.get(i).getDis_price() != null && !products.get(i).getDis_price().equals("null") && !products.get(i).getDis_price().equals("")) {
                        p_dis = Integer.parseInt(products.get(i).getDis_price());
                    }

                    if (products.get(i).count != null && !products.get(i).count.equals("null") && !products.get(i).count.equals("")) {
                        count = Integer.parseInt(products.get(i).count);
                    }

                    TotalPrice += p * count * (100 - (dis1 +
                            dis2 + dis3)
                    ) * 0.01;

                    rawPrice += p * count;
                    rawPrice_dis += p_dis * count;

                }

            } else {
                listsabad.setVisibility(View.VISIBLE);
                listsabad.setImageResource(R.drawable.shopping_cart);

            }

            if (rawPrice > 0) {
                totalprice.setText(" جمع کل : " + ConvertEnToPe(convertToFormalString(String.valueOf(rawPrice_dis))) + " تومان ");
            } else {
                typePayLinear.setVisibility(View.GONE);
                addLinear.setVisibility(View.GONE);
                completeBuy.setVisibility(View.GONE);
                discount.setVisibility(View.GONE);
            }

            discountPrice = rawPrice - rawPrice_dis;
            if (discountPrice > 0) {
                discount.setText(" تخفیف : " + ConvertEnToPe(convertToFormalString(String.valueOf(discountPrice))) + " تومان ");
            }


        } else {
            basket_recyclerView.setVisibility(View.GONE);
        }
    }

}
