package Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;

import net.babiran.app.MainActivity;
import net.babiran.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapters.CategoryAdapter;
import Models.Category;
import tools.AppConfig;


/**
 * Created by Mohammad on 6/13/2017.
 */


public class CategoryFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_category_frgment, container, false);

        MainActivity.category.setVisibility(View.VISIBLE);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.category_list_grid);

        if(getCategory()!= null){
            CategoryAdapter categoryAdapter = new CategoryAdapter(getCategory(), getActivity());
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity() , 2 , LinearLayoutManager.VERTICAL , false);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(categoryAdapter);
            categoryAdapter.notifyDataSetChanged();

        }

        return v;
    }

    public ArrayList<Category> getCategory(){

        ArrayList<Category> categoryArrayList = new ArrayList<>();

        for (int i = 0; i < AppConfig.categories.length(); i++) {
            try {
                JSONObject c = AppConfig.categories.getJSONObject(i);

                Category category = new Category(c.getString("id") ,c.getString("name"), c.getString("parent_id")
                        ,c.getString("icon"),c.getString("slide_image"));

                if(category.parent_id.equals("0")){
                    categoryArrayList.add(category);
                }

            } catch (JSONException e) {

                e.printStackTrace();
                AppConfig.error(e);

            }
        }
        return categoryArrayList ;
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
                    System.out.println("===MainActivity=====");
                    if(MainActivity.productlist.getVisibility() == View.VISIBLE){
                        MainActivity.productlist.setVisibility(View.INVISIBLE);
                        MainActivity.secondcategory.setVisibility(View.VISIBLE);
                        //AppConfig.fragmentManager.beginTransaction().replace(R.id.Categorycontainer,new CategoryFragment()).commit();
                    }
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
}

