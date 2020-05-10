package Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kofigyan.stateprogressbar.StateProgressBar;

import net.babiran.app.DownLoadImageTask;
import net.babiran.app.OrderList;
import net.babiran.app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import Fragments.DescriptionDialog;
import Handlers.DatabaseHandler;
import Models.Basket;
import Models.Factor;
import ui_elements.MyTextView;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Tohid on 2/7/2017 AD.
 */

public class FactorListAdapter extends BaseAdapter {
    Context context;
    ArrayList<Factor> factors = new ArrayList<>();
    LayoutInflater inflater;
    private String share;
    private DatabaseHandler db;
    private String mainId;
    ArrayList<Basket> baskets = new ArrayList<>();
    private String basketjson = "";
    private SharedPreferences.Editor editor;


    public FactorListAdapter(Context context, ArrayList<Factor> factors) {
        this.context = context;
        this.factors = factors;
        this.inflater = LayoutInflater.from(context);
    }

    public FactorListAdapter(Context context, ArrayList<Factor> factors, String prev) {
        this.context = context;
        this.factors = factors;


        // this.prev = prev;
        this.inflater = LayoutInflater.from(context);

    }


    @Override
    public int getCount() {
        return factors.size();
    }

    @Override
    public Factor getItem(int i) {
        return factors.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Integer.parseInt(factors.get(i).id);
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertView == null) {

            db = new DatabaseHandler(context);

            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.factor_item,
                    viewGroup, false);

            holder.free_price = (MyTextView) convertView.findViewById(R.id.txt_freeprice);
            holder.dis_price = (MyTextView) convertView.findViewById(R.id.txt_price);
            holder.type = (MyTextView) convertView.findViewById(R.id.txt_type);
            holder.date = (MyTextView) convertView.findViewById(R.id.txt_date);
            holder.item_Button = (LinearLayout) convertView.findViewById(R.id.item_button);
            holder.LnPiyk = (LinearLayout) convertView.findViewById(R.id.LnPiyk);
            holder.state = (MyTextView) convertView.findViewById(R.id.txt_type_state);
            holder.stateProgressBar = (StateProgressBar) convertView.findViewById(R.id.your_state_progress_bar_id);

            holder.btn_type_state_peygiri = (Button) convertView.findViewById(R.id.btn_type_state_peygiri);
            holder.Peygiri = (MyTextView) convertView.findViewById(R.id.txt_type_state_peygiri);
            holder.Sefaresh = (MyTextView) convertView.findViewById(R.id.txt_type_state_sefartesh);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (!factors.get(i).free_price.equals("null") && !factors.get(i).free_price.equals("") && factors.get(i).free_price != null) {
            holder.free_price.setText(ConvertEnToPe(factors.get(i).free_price) + " تومان ");

        }

        if (!factors.get(i).dis_price.equals("null") && !factors.get(i).dis_price.equals("") && factors.get(i).dis_price != null) {
            holder.dis_price.setText(ConvertEnToPe(factors.get(i).dis_price) + " تومان ");

        }

        if (!factors.get(i).date.equals("null") && !factors.get(i).date.equals("") && factors.get(i).date != null) {
            holder.date.setText(ConvertEnToPe(factors.get(i).date));

        }

        if (!factors.get(i).type.equals("null") && !factors.get(i).type.equals("") && factors.get(i).type != null) {
            holder.type.setText(ConvertEnToPe(factors.get(i).type));

        }
        holder.state.setText(factors.get(i).state);
//
        String ar[] = factors.get(i).piyek.split("##");
        System.out.println("ar[]====" + Arrays.toString(ar));
        if (ar[3].equals("null"))//3==> trak   4 ==> ref
        {
            holder.Sefaresh.setText("0");
            holder.Sefaresh.setVisibility(View.GONE);
            holder.btn_type_state_peygiri.setVisibility(View.VISIBLE);
        } else {
            holder.Sefaresh.setText(ar[3]);
            holder.Peygiri.setVisibility(View.VISIBLE);
            holder.btn_type_state_peygiri.setVisibility(View.GONE);
        }
        if (ar[4].equals("null")) {
            holder.Peygiri.setText("0");
        } else {
            holder.Peygiri.setText(ar[4]);
        }

        System.out.println("ar[4]====" + ar[4]);

        Log.e("orders:", factors.get(i).products.size() + "");
        CheckInput(factors.get(i).state, viewGroup, holder.stateProgressBar);

        holder.item_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,"show advertising with id : \n"+categories.get(i).id, Toast.LENGTH_SHORT).show();
                //  AppConfig.fragmentManager.beginTransaction().replace(R.id.Productcontainer, new ProductFragment(factors.get(i))).commit();

                if (factors.get(i).products != null && factors.get(i).products.size() > 0) {

                    Gson gson = new Gson();
                    String productList = gson.toJson(factors.get(i).products);
                    Intent intent = new Intent(context, OrderList.class);
                    intent.putExtra("prolist", productList);
                    context.startActivity(intent);

                }

            }
        });

        holder.LnPiyk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ar[] = factors.get(i).piyek.split("##"); //0=> name   1=> image    2=> mob       3==> trak   4 ==> ref
                String dd = factors.get(i).piyek;
                Log.e("DDD", dd);
                Log.e("LOGGG", ar[0] + "\n" + ar[1] + "\n" + ar[2]);
                if (ar[0].equals("پیک ندارد")) {
                    Toast.makeText(context, "مرسوله به مرحله ارسال نرسیده است لطفا صبور باشید..", Toast.LENGTH_SHORT).show();

                } else {
                    Dilog(ar[1], ar[0], ar[2]);
                }

            }
        });


        // SharedPreferences prefs = context.getSharedPreferences(id, MODE_PRIVATE);
        editor = context.getSharedPreferences("productsArray", MODE_PRIVATE).edit();

        holder.btn_type_state_peygiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (db.getRowCount() > 0) {
                    HashMap<String, String> userDetailsHashMap = db.getUserDetails();
                    mainId = userDetailsHashMap.get("id");
                    System.out.println("mainId===" + mainId);

                    if (factors.get(i).products != null && factors.get(i).products.size() > 0) {
                        for (int j = 0; j < factors.get(i).products.size(); j++) {
                            Basket basket = new Basket(factors.get(i).products.get(j).id, factors.get(i).products.get(j).count);
                            baskets.add(basket);
                        }
                        Gson gson = new Gson();
                        basketjson = gson.toJson(baskets);
                        System.out.println("basketjson2===" + basketjson);

                        FragmentManager fm = ((Activity) context).getFragmentManager();
                        DescriptionDialog descriptionDialog = new DescriptionDialog(context, mainId, factors.get(i).address, basketjson, factors.get(i).type,0, editor);
                        descriptionDialog.show(fm, "DescriptionDialog");
                    }
                }

            }
        });


        return convertView;
    }


    private void CheckInput(String input, View view, StateProgressBar stateProgressBar) {
        if (input.equals("سفارش شما ثبت و در حال بررسی است")) {
            StateProgresbarNewOreder(view, "ok", "", "", "", "", stateProgressBar);
        } else if (input.equals("در حال آماده سازی")) {
            StateProgresbarNewOreder(view, "ok", "ok", "", "", "", stateProgressBar);
        } else if (input.equals("تحویل پیک و در حال ارسال")) {
            StateProgresbarNewOreder(view, "ok", "ok", "ok", "", "", stateProgressBar);
        } else if (input.equals("تحویل داده شده")) {
            StateProgresbarNewOreder(view, "ok", "ok", "ok", "ok", "ok", stateProgressBar);
        }


    }

    private void StateProgresbarNewOreder(View v, String P, String F, String A, String L, String B, StateProgressBar stateProgressBar) {
        String[] descriptionData = {"ثبت شده", "در حال آماده سازی", "تحویل پیک شده", "در حال ارسال", "تحویل داده"};

        // StateProgressBar stateProgressBar = (StateProgressBar)  v. findViewById(R.id.your_state_progress_bar_id);
        //stateProgressBar.setStateDescriptionData(descriptionData);

        if (TextUtils.isEmpty(P)) {
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
        } else if (TextUtils.isEmpty(F)) {
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
        } else if (TextUtils.isEmpty(A)) {
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
        } else if (TextUtils.isEmpty(L)) {
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
        }


    }


    private class ViewHolder {

        MyTextView free_price, dis_price, type, date, state, Peygiri, Sefaresh;
        StateProgressBar stateProgressBar;
        ListView orders;
        LinearLayout item_Button, LnPiyk;
        Button btn_type_state_peygiri;

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


    private void Dilog(String url, String Name, String mob) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.coustom_dialog_biuty);
        dialog.setCancelable(true);
        dialog.show();

        TextView txtName = (TextView) dialog.findViewById(R.id.name_payke);
        ImageView img = (ImageView) dialog.findViewById(R.id.img_payke);
        TextView txtM = (TextView) dialog.findViewById(R.id.mob_payke);

        new DownLoadImageTask(img, context).execute(url);
        txtName.setText(Name);
        txtM.setText(mob);
    }

}
