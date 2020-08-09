package net.babiran.app.Sefaresh;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import net.babiran.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Models.Feature;
import Models.Image;
import Models.Product;
import Models.ProductNew;
import tools.AppConfig;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab3 extends Fragment {


    private View mView;
    NewProListfoodAdapter newProListfoodAdapter;
    private RecyclerView recyclerView;
    String IDs;
    public ArrayList<ProductNew> Newproducts = new ArrayList<>();
    public ArrayList<Product> NewproductsO = new ArrayList<>();
    private String startTime = null;
    private String endTime = null;

    //noshidani

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_tab3, container, false);

        IDs = getActivity().getIntent().getExtras().getString("IDDD");
        startTime = getActivity().getIntent().getExtras().getString("startTime");
        endTime = getActivity().getIntent().getExtras().getString("endTime");
        INIT();

        if (Newproducts != null) {
            newProListfoodAdapter = new NewProListfoodAdapter(getActivity(), Newproducts, NewproductsO, startTime, endTime);

            recyclerView.setAdapter(newProListfoodAdapter);


        }

        return mView;
    }

    private void INIT() {
        Newproducts.clear();


        recyclerView =  mView.findViewById(R.id.recyclerView);


        ArrayList<Feature> featuresArray;
        ArrayList<Image> imagesArray;
        for (int i = 0; i < AppConfig.NewPro.length(); i++) {
            featuresArray = new ArrayList<>();
            imagesArray = new ArrayList<>();
            try {
                JSONObject c = AppConfig.NewPro.getJSONObject(i);

                JSONArray features = c.getJSONArray("features");
                for (int fea = 0; fea < features.length(); fea++) {
                    try {
                        JSONObject f = features.getJSONObject(fea);
                        Feature feature = new Feature(
                                f.getString("value"), f.getString("name"));
                        featuresArray.add(fea, feature);
                    } catch (JSONException ex) {

                    }
                }

                JSONArray images = c.getJSONArray("images");
                ;
                for (int img = 0; img < images.length(); img++) {

                    try {
                        JSONObject im = images.getJSONObject(img);
                        Image image = new Image(
                                im.getString("image_link"));
                        imagesArray.add(img, image);
                    } catch (JSONException ex) {

                    }
                }

                String gh = c.getString("category_id1");
                if (gh.equals(IDs)) {
                    String fg = c.getString("foodcat_id");
                    Log.e("xdffv", fg + "");
                    if (3 == Integer.parseInt(fg)) {
                        Log.e("rtgfhxfgn", "xfgfghnfhjff");
                        ProductNew product = new ProductNew(c.getString("mokhalafat"), c.getString("id"), c.getString("name"), c.getString("description"),
                                c.getString("price"), c.getString("stock"), "", c.getString("discount_price"), imagesArray, featuresArray);
                        Product producto = new Product(c.getString("id"), c.getString("name"), c.getString("description"),
                                c.getString("price"), c.getString("stock"), "", c.getString("discount_price"), imagesArray, featuresArray);

                        NewproductsO.add(producto);
                        Newproducts.add(product);
                    }

                }


            } catch (JSONException e) {
                Log.e("rtgfhxfgn", e.getMessage());
                AppConfig.error(e);

            }
        }
    }

}
