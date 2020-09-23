package net.babiran.app.Sefaresh;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import net.babiran.app.ActivityComments;
import net.babiran.app.R;
import net.babiran.app.Rss.FullScreenActivity;

import java.util.ArrayList;
import java.util.Calendar;

import Fragments.CountDialog;
import Handlers.DatabaseHandler;
import Models.Product;
import Models.ProductNew;
import ui_elements.MyTextView;

public class NewProListfoodAdapter extends RecyclerView.Adapter<NewProListfoodAdapter.MyViewHolder> {
    RequestQueue queue;
    DatabaseHandler db;
    String id_user = "-1";
    Context context;
    ProgressDialog d;
    ArrayList<ProductNew> productArray = new ArrayList<>();
    ArrayList<Product> productArrayO = new ArrayList<>();
    LayoutInflater inflater;
    private boolean open = false;

    private Calendar now = Calendar.getInstance();
    private int start = 0;
    private int end = 0;
    private boolean b = false;
    private String startTime = null;
    private String endTime = null;

    public NewProListfoodAdapter(Context context, ArrayList<ProductNew> products, ArrayList<Product> productsO, String startTime, String endTime) {

        this.context = context;
        this.productArray = products;
        this.productArrayO = productsO;
        this.inflater = LayoutInflater.from(context);
        this.startTime = startTime;
        this.endTime = endTime;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private MyTextView name, addToBasket, noProduct, price_dis, price_free, txt_material;
        private LinearLayout item_Button;
        private ImageView img, img_material;
        private CardView material_carView, comment_carView;
        private LinearLayout layout_material;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.new_pro_txt_name);
            img = itemView.findViewById(R.id.new_pro_img);
            item_Button = itemView.findViewById(R.id.new_pro_item_button);
            addToBasket = itemView.findViewById(R.id.addToNew);
            noProduct = itemView.findViewById(R.id.noProduct);
            price_dis = itemView.findViewById(R.id.new_pro_txt_price);
            price_free = itemView.findViewById(R.id.new_pro_txt_free_price);
            txt_material = itemView.findViewById(R.id.txt_material);
            material_carView = itemView.findViewById(R.id.material_carView);
            comment_carView = itemView.findViewById(R.id.comment_carView);
            layout_material = itemView.findViewById(R.id.layout_material);
            img_material = itemView.findViewById(R.id.img_material);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_new_adp_sefaresh, parent, false);

        return new NewProListfoodAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.name.setText(productArray.get(i).name);
        if (isEven(i)) {
            holder.item_Button.setBackgroundResource(R.color.bac);
        } else {
            holder.item_Button.setBackgroundResource(R.color.bac2);
        }
        if (productArray.get(i).getStock() != null) {
            if (productArray.get(i).getStock().equals("0")) {
                holder.noProduct.setVisibility(View.VISIBLE);
                holder.addToBasket.setVisibility(View.GONE);
            } else {
                holder.noProduct.setVisibility(View.GONE);
                holder.addToBasket.setVisibility(View.VISIBLE);
            }
        }


        if (productArray.get(i).getMokhalafat() != null) {
            holder.txt_material.setText(productArray.get(i).getMokhalafat());
        }


        holder.item_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    ProductNew product = productArray.get(i);
                    Product product1 = productArrayO.get(i);
                    Di(product, productArray.get(i).mokhalafat, product1);
                    //     context.startActivity(new Intent(context, MainActivity.class));

                } catch (Exception e) {
                    Log.e("SAA", e.getMessage());
                }


            }
        });
        holder.addToBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (startTime != null && endTime != null) {
                    int current = now.get(Calendar.HOUR_OF_DAY);
                    start = Integer.parseInt(startTime.trim());
                    end = Integer.parseInt(endTime.trim());

                    for (int j = start; j <= end; j++) {
                        if (current == j) {
                            b = true;
                        }
                    }

                    if (b) {
                        FragmentManager fm = ((Activity) context).getFragmentManager();
                        CountDialog countDialog = new CountDialog(productArray.get(i));
                        countDialog.show(fm, "CountDialog");
                    } else {
                        Toast.makeText(context, "خارج از ساعت کاری", Toast.LENGTH_LONG).show();
                    }
                } else {
                    FragmentManager fm = ((Activity) context).getFragmentManager();
                    CountDialog countDialog = new CountDialog(productArray.get(i));
                    countDialog.show(fm, "CountDialog");
                }


            }
        });


        if (!productArray.get(i).dis_price.equals("null") && !productArray.get(i).dis_price.equals("") && productArray.get(i).dis_price != null) {
            holder.price_dis.setText(ConvertEnToPe(convertToFormalString(Integer.parseInt(productArray.get(i).dis_price) + "")) + " ت ");
            //   price_free.setVisibility(INVISIBLE);
        }

        if (!productArray.get(i).price.equals("null") && !productArray.get(i).price.equals("") && productArray.get(i).price != null) {
            holder.price_free.setText(ConvertEnToPe(convertToFormalString(productArray.get(i).price + "")) + " ت ");
            holder.price_free.setPaintFlags(holder.price_free.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        System.out.println("images111111========" + productArray.get(i).images.size());

        for (int j = 0; j < productArray.get(i).images.size(); j++) {
            if (productArray.get(i).images.get(j) != null && productArray.get(i).images.get(j).toString().length() > 5) {

                System.out.println("images111111========" + productArray.get(i).images.get(j));

                Glide.with(context).load(productArray.get(i).images.get(j).image_link).fitCenter().placeholder(R.drawable.logoloading).into(holder.img);
            }
        }

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int j = 0; j < productArray.get(i).images.size(); j++) {
                    Intent intent = new Intent(context, FullScreenActivity.class);
                    intent.putExtra("imgUrl", productArray.get(i).images.get(j).image_link);
                    context.startActivity(intent);
                }

            }
        });

        holder.material_carView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (open) {
                    open = false;
                    holder.layout_material.setVisibility(View.GONE);
                    holder.img_material.setBackground(context.getResources().getDrawable(R.drawable.arrow_down));
                } else {
                    open = true;
                    holder.layout_material.setVisibility(View.VISIBLE);
                    holder.img_material.setBackground(context.getResources().getDrawable(R.drawable.arrow_up));
                }

            }
        });

        holder.comment_carView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProductNew productNew = productArray.get(i);
                Product product = productArrayO.get(i);


                System.out.println("productArray====" + productArray.size());
                System.out.println("productArrayO====" + productArrayO.size());

                Gson gson = new Gson();
                String proObj = gson.toJson(product);
                Intent intent = new Intent(context, ActivityComments.class);
                intent.putExtra("product", proObj);
                intent.putExtra("product_id", productNew.id);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public long getItemId(int i) {
        return Integer.parseInt(productArray.get(i).id);
    }

    @Override
    public int getItemCount() {
        return productArray.size();
    }

/*    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.row_new_adp_sefaresh,
                    viewGroup, false);


            holder.name = (MyTextView) convertView.findViewById(R.id.new_pro_txt_name);
            holder.img = (ImageView) convertView.findViewById(R.id.new_pro_img);
            holder.item_Button = (LinearLayout) convertView.findViewById(R.id.new_pro_item_button);
            holder.addToBasket = (MyTextView) convertView.findViewById(R.id.addToNew);
            holder.noProduct = (MyTextView) convertView.findViewById(R.id.noProduct);
            holder.price_dis = (MyTextView) convertView.findViewById(R.id.new_pro_txt_price);
            holder.price_free = (MyTextView) convertView.findViewById(R.id.new_pro_txt_free_price);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(productArray.get(i).name);
        if (isEven(i)) {
            holder.item_Button.setBackgroundResource(R.color.bac);
        } else {
            holder.item_Button.setBackgroundResource(R.color.bac2);
        }
        System.out.println("name====" + productArray.get(i).name);
        System.out.println("getStock====" + productArray.get(i).getStock());
        if (productArray.get(i).getStock() != null) {
            if (productArray.get(i).getStock().equals("0")) {
                holder.noProduct.setVisibility(View.VISIBLE);
                holder.addToBasket.setVisibility(View.GONE);
            } else {
                holder.noProduct.setVisibility(View.GONE);
                holder.addToBasket.setVisibility(View.VISIBLE);
            }
        }

        holder.item_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    ProductNew product = productArray.get(i);
                    Product product1 = productArrayO.get(i);
                    Di(product, productArray.get(i).mokhalafat, product1);
                    //     context.startActivity(new Intent(context, MainActivity.class));

                } catch (Exception e) {
                    Log.e("SAA", e.getMessage());
                }


            }
        });
        holder.addToBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (startTime != null && endTime != null) {
                    int current = now.get(Calendar.HOUR_OF_DAY);
                    start = Integer.parseInt(startTime.trim());
                    end = Integer.parseInt(endTime.trim());

                    for (int j = start; j <= end; j++) {
                        if (current == j) {
                            b = true;
                        }
                    }

                    if (b) {
                        FragmentManager fm = ((Activity) context).getFragmentManager();
                        CountDialog countDialog = new CountDialog(productArray.get(i));
                        countDialog.show(fm, "CountDialog");
                    } else {
                        Toast.makeText(context, "خارج از ساعت کاری", Toast.LENGTH_LONG).show();
                    }
                } else {
                    FragmentManager fm = ((Activity) context).getFragmentManager();
                    CountDialog countDialog = new CountDialog(productArray.get(i));
                    countDialog.show(fm, "CountDialog");
                }


            }
        });


        if (!productArray.get(i).dis_price.equals("null") && !productArray.get(i).dis_price.equals("") && productArray.get(i).dis_price != null) {
            holder.price_dis.setText(ConvertEnToPe(convertToFormalString(Integer.parseInt(productArray.get(i).dis_price) + "")) + " ت ");
            //   price_free.setVisibility(INVISIBLE);
        }

        if (!productArray.get(i).price.equals("null") && !productArray.get(i).price.equals("") && productArray.get(i).price != null) {
            holder.price_free.setText(ConvertEnToPe(convertToFormalString(productArray.get(i).price + "")) + " ت ");
            holder.price_free.setPaintFlags(holder.price_free.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        for (int j = 0; j < productArray.get(i).images.size(); j++) {
            if (productArray.get(i).images.get(j) != null && productArray.get(i).images.get(j).toString().length() > 5) {
                Glide.with(context).load(productArray.get(i).images.get(j).image_link).fitCenter().placeholder(R.drawable.logoloading).into(holder.img);
            }
        }


        return convertView;
    }*/


    private class ViewHolder {

        MyTextView name, addToBasket, noProduct, txt_material;
        LinearLayout item_Button;
        ImageView img;
        MyTextView price_dis, price_free;

    }

    private static boolean isEven(int number) {
        return (number & 1) == 0;
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


    private void Di(final ProductNew product, final String s, final Product producto) {

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        //TODO Dilaog
//        Bitmap map=appStore.takeScreenShot(this);
//
//        Bitmap fast=appStore.fastblur(map, 10);
//        final Drawable draw=new BitmapDrawable(getResources(),fast);


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.coustom_dialog_exit);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        //  dialog.getWindow().setBackgroundDrawable(draw);
        //    dialog.setCancelable(false);
        dialog.show();


        LinearLayout buttonnaz = (LinearLayout) dialog.findViewById(R.id.btn_exit_main_ok);
        LinearLayout buttonmos = (LinearLayout) dialog.findViewById(R.id.btn_exit_main_cancell);
        final TextView txt = (TextView) dialog.findViewById(R.id.gdhdhdhd);
        final ImageView imageView = (ImageView) dialog.findViewById(R.id.sfdgadfgbadefb);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        buttonmos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt.setText(s);
//                Gson gson = new Gson();
//                String proObj = gson.toJson(product);
//                Intent intent = new Intent(context, productInfo.class);
//                intent.putExtra("product", proObj);
//                context.startActivity(intent);
            }
        });
        buttonnaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Gson gson = new Gson();
                String proObj = gson.toJson(producto);
                Intent intent = new Intent(context, ActivityComments.class);
                intent.putExtra("product", proObj);
                intent.putExtra("product_id", product.id);
                context.startActivity(intent);

            }
        });

    }
}
