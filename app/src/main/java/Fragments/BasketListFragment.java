package Fragments;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;

import net.babiran.app.AppController;
import net.babiran.app.MainActivity;
import net.babiran.app.R;

import java.util.ArrayList;
import java.util.HashMap;

import Adapters.BasketListAdapter;
import Handlers.DatabaseHandler;
import Models.Basket;
import tools.AppConfig;
import ui_elements.MyTextView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static tools.AppConfig.products;


public class BasketListFragment extends Fragment implements
        AdapterView.OnItemSelectedListener {

    public String category_id = "0";
    public String category_parent_id = "-1";

    String[] payment = {"پرداخت با کارت خوان درب منزل", "پرداخت نقدی در محل تحویل", "پرداخت آنلاین از درگاه بانکی"};
    View v;
    RequestQueue queue;
    ListView listView;
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


    public BasketListFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.basket_list_fragment, container, false);

        //  getFragmentManager().beginTransaction().detach(this).attach(this).commit();


        final SharedPreferences.Editor editor = getActivity().getSharedPreferences("productsArray", MODE_PRIVATE).edit();


        addresses = new ArrayList<>();
        db = new DatabaseHandler(getActivity());

        if (db.getRowCount() > 0) {
            HashMap<String, String> userDetailsHashMap = db.getUserDetails();

            id = userDetailsHashMap.get("id");
            address1 = userDetailsHashMap.get("address");
            address2 = userDetailsHashMap.get("address2");

//            AppConfig.tempActivity = (MainActivity) getActivity();
            listView = (ListView) v.findViewById(R.id.basket_listView);
            listsabad = (ImageView) v.findViewById(R.id.listsabad);

            AddValue = (MyTextView) v.findViewById(R.id.addValue);
            PayValue = (MyTextView) v.findViewById(R.id.payValue);

            totalprice = (MyTextView) v.findViewById(R.id.totalprice);
            discount = (MyTextView) v.findViewById(R.id.dis_txt);
            completeBuy = (RelativeLayout) v.findViewById(R.id.compelete);

            addLinear = (LinearLayout) v.findViewById(R.id.addressLinear);
            typePayLinear = (LinearLayout) v.findViewById(R.id.paymentLinear);


//            Log.e("proSize", AppConfig.products.size() + "");

            ///      Log.e("proContent", AppConfig.products.toString() );


            ArrayList<Basket> baskets = new ArrayList<>();

            if (products != null) {
                if (products.size() > 0) {
                    if (listsabad.getVisibility() == View.VISIBLE) {
                        listsabad.setVisibility(View.INVISIBLE);
                    }
                    for (int i = 0; i < products.size(); i++) {
                        Basket basket = new Basket(products.get(i).getId(), products.get(i).count);
                        baskets.add(basket);
                    }
                    Gson gson = new Gson();
                    basketjson = gson.toJson(baskets);


                    BasketListAdapter adp = new BasketListAdapter(getActivity(), products);
                    adp.notifyDataSetChanged();
                    listView.setAdapter(adp);

                    TotalPrice = 0;
                    discountPrice = 0;

                    rawPrice = 0;
                    rawPrice_dis = 0;
                    Log.e("proSizein", products.size() + "");

                    int p = 0;
                    int p_dis = 0;
                    int dis1 = 0;
                    int dis2 = 0;
                    int dis3 = 0;
                    int count = 0;
                    for (int i = 0; i < products.size(); i++) {

                        if (products.get(i).getPrice() != null && !products.get(i).getPrice().equals("null") && !products.get(i).getPrice().equals("")) {
                            p = Integer.parseInt(products.get(i).getPrice());
                        }
                        if (products.get(i).getDis_price() != null && !products.get(i).getDis_price().equals("null") && !products.get(i).getDis_price().equals("")) {
                            p_dis = Integer.parseInt(products.get(i).getDis_price());
                        }

                        System.out.println("count======" + products.get(i).getCount());
                        System.out.println("count======" + products.get(i).count);
                        if (products.get(i).count != null && !products.get(i).count.equals("null") && !products.get(i).count.equals("")) {
                            count = Integer.parseInt(products.get(i).count);
                        }


                    /*if(prefs.getString("pro_id","0").equals(products.get(i).id)) {
                        count = Integer.parseInt(prefs.getString("count", "0"));
                    }*/


                        TotalPrice += p * count * (100 - (dis1 +
                                dis2 + dis3)
                        ) * 0.01;


                        Log.e("priceeee", p + "");
                        Log.e("priceeee", p_dis + "");
                        Log.e("counttt", count + "");


                        rawPrice += p * count;
                        rawPrice_dis += p_dis * count;

                    }

                } else {
                    listsabad.setVisibility(View.VISIBLE);
                    listsabad.setImageResource(R.drawable.shopping_cart);

                }
            } else {
                listView.setVisibility(View.GONE);

            }


            Log.e("raw_dis", rawPrice + " " + rawPrice_dis);
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

                    }
                    if (!address2.equals("null") && !address2.equals("") && address2.length() > 0) {
                        secondAdd.setText(address2);
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

                            Log.e("here", "confirm");
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
                    //    final CheckBox firstChk = (CheckBox) customView.findViewById(R.id.firstCheck_pay);
                    final CheckBox secondChk = (CheckBox) customView.findViewById(R.id.secondCheck_pay);
                    final CheckBox _3Chk = (CheckBox) customView.findViewById(R.id._3secondCheck_pay);

                    //   final LinearLayout firstPayLinear = (LinearLayout) customView.findViewById(R.id.firstPayLinear);
                    final LinearLayout secondPayLinear = (LinearLayout) customView.findViewById(R.id.secondPayLinear);
                    final LinearLayout _3PayLinear = (LinearLayout) customView.findViewById(R.id._3PayLinear);


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
                            //    firstChk.setChecked(false);
                            //secondChk.setChecked(true);
                            //_3Chk.setChecked(false);
                            Toast.makeText(getActivity(), "فعلا امکان پرداخت از طریق کارتخوان بی سیم مقدور نمی باشد", Toast.LENGTH_LONG).show();

                        }
                    });
                    _3PayLinear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //           firstChk.setChecked(false);
                            //           secondChk.setChecked(false);
                            _3Chk.setChecked(true);

                        }
                    });


                    secondChk.setClickable(false);
                    //     firstChk.setClickable(false);
                    _3Chk.setClickable(false);

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
                            //_3Chk.setChecked(false);
                            Toast.makeText(getActivity(), "فعلا امکان پرداخت از طریق کارتخوان بی سیم مقدور نمی باشد", Toast.LENGTH_LONG).show();
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

                                    Log.e("here", "confirm");
                         /*   if (firstChk.isChecked())
                            {
                                selectedPay = "0";
                                PayValue.setText(firstPay.getText().toString());
                                mPopupWindow.dismiss();

                            }*/
                                    if (secondChk.isChecked()) {
                                        selectedPay = "1";
                                        PayValue.setText(secondPay.getText().toString());
                                        mPopupWindow.dismiss();
                                    }
                                    if (_3Chk.isChecked()) {
                                        selectedPay = "2";
                                        PayValue.setText(_3secondPay.getText().toString());

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
            }else {
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
                            Toast.makeText(getActivity(), "لطفا مشخصات خود را تکمیل کنید", Toast.LENGTH_LONG).show();
                            AppConfig.fragmentManager.beginTransaction().replace(R.id.Editcontainer, new EditProfileFrgment(EditProfileFrgment.prev_edit)).commit();
                            MainActivity.basketlist.setVisibility(View.INVISIBLE);

                        } else {
                            if (AddValue.getText().toString() != null && !AddValue.getText().toString().equals("") && !AddValue.getText().toString().equals("null")) {

                                if (PayValue.getText().toString() != null && !PayValue.getText().toString().equals("") && !PayValue.getText().toString().equals("null")) {

                                    FragmentManager fm = getActivity().getFragmentManager();
                                    DescriptionDialog descriptionDialog = new DescriptionDialog(getActivity(), id, selectedAdd, basketjson, selectedPay, editor);
                                    descriptionDialog.show(fm, "DescriptionDialog");

                                    System.out.println("id=====" + id);
                                    System.out.println("selectedAdd=====" + selectedAdd);
                                    System.out.println("basketjson=====" + basketjson);
                                    System.out.println("selectedPay=====" + selectedPay);
                                    System.out.println("editor=====" + editor);


                                    SharedPreferences.Editor editor1 = AppController.getInstance().getSharedPreferences().edit();
                                    editor1.putString("selectedAdd" + basketjson, selectedAdd);
                                    editor1.apply();

                                    SharedPreferences.Editor editor2 = AppController.getInstance().getSharedPreferences().edit();
                                    editor2.putString("selectedPay" + basketjson, selectedPay);
                                    editor2.apply();


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
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    if (MainActivity.product.getVisibility() == View.VISIBLE) {
                        MainActivity.product.setVisibility(View.INVISIBLE);

                    }
                   /* if(prev.equals("category")){
                        OtherCategoryActivity.productContainer.setVisibility(View.INVISIBLE);

                    }*/
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

                    }
                    return true;

                }

                return false;
            }

        });


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


}
