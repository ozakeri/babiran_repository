package net.babiran.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import Models.Product;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import ui_elements.MyTextView;

/**
 * Created by Alireza on 1/31/2017.
 */

public class ActivityComments extends AppCompatActivity {
    private List<Comment> mCommentList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private Product mProduct;
    private CommentAdapter adaptr;
    private ProgressDialog myProgressDialog;
    private LinearLayoutManager layoutManager;
    private MyTextView txt_null;
    private String reqTag;
    private String reqOneTag;
    private String nonceReqTag;
    private int page = 1;
    private boolean loading = false;
    int pastVisibleItems, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_comment);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        if (getIntent() != null) {
            if (getIntent().hasExtra("product_id") && getIntent().hasExtra("product")) {
                String mProductId = getIntent().getStringExtra("product_id");
                mProduct = new Gson().fromJson(getIntent().getStringExtra("product"), Product.class);
                mProduct.setId(mProductId);
            }
        }
//        if (mProduct == null) finish();
        if (mProduct == null) {
            mProduct = new Product();
            mProduct.setId(String.valueOf(1000));
        }
        ;
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.btnBack.setVisibility(View.GONE);
                MainActivity.viewLogo.setVisibility(View.VISIBLE);
                finish();
            }
        });

        txt_null = findViewById(R.id.txt_null);
        txt_null.setVisibility(View.GONE);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.btn_add_new_comment);
        floatingActionButton.setBackgroundTintList(new ColorStateList(new int[][]{new int[]{0}}, new int[]{R.color.colorPrimary}));
        this.mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        this.layoutManager = new LinearLayoutManager(ActivityComments.this);
        this.mRecyclerView.setLayoutManager(layoutManager);
        adaptr = new CommentAdapter();


        findViewById(R.id.btn_add_new_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAddNewCommant dialogAddNewCommant = new DialogAddNewCommant(ActivityComments.this, mProduct);
                dialogAddNewCommant.setmCallback(new AddCommentCallback() {
                    @Override
                    public void onAddComment(Comment comment) {
                        mCommentList.add(comment);
                        adaptr = new CommentAdapter();
                        mRecyclerView.setAdapter(adaptr);
                    }
                });
                dialogAddNewCommant.show();
            }
        });

        loadProductsPageOne();
        if (mCommentList.size() == 0){
            txt_null.setVisibility(View.VISIBLE);
        }
    }

    public void showProgressDialog(String s) {
        myProgressDialog = new ProgressDialog(ActivityComments.this);
        myProgressDialog.setMessage(s);
        myProgressDialog.show();
    }

    private void loadProductsPageOne() {
        if (new ConnectionDetector(this).isConnectingToInternet()) {
            findViewById(R.id.box_error).setVisibility(View.GONE);
            findViewById(R.id.progress_bar).setVisibility(View.GONE);
            showProgressDialog("در حال دریافت لیست نظرات...");
            reqOneTag = RequestHandler.getComments(ActivityComments.this, Long.parseLong(mProduct.getId()), page, new GetCommentsCallBack() {
                @Override
                public void onGetCommentsSuccessAction(Comment[] comments) {
                    loading = false;
                    page++;
                    stopProgressDialog();
                    findViewById(R.id.box_error).setVisibility(View.GONE);
                    findViewById(R.id.progress_bar).setVisibility(View.GONE);
                    if (comments != null && comments.length > 0) {
                        mCommentList.addAll(new ArrayList<>(Arrays.asList(comments)));
                        adaptr = new CommentAdapter();
                        mRecyclerView.setAdapter(adaptr);
                    }
                }

                @Override
                public void onGetCommentsErrorAction(String error) {
                    loading = false;
                    stopProgressDialog();
                    findViewById(R.id.box_error).setVisibility(View.VISIBLE);
                    findViewById(R.id.progress_bar).setVisibility(View.GONE);
                    findViewById(R.id.btn_try_again).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            loadProductsPageOne();
                        }
                    });
                }
            });

        } else {
            loading = false;
            Toast.makeText(this, "connection_error", Toast.LENGTH_LONG).show();
            findViewById(R.id.progress_bar).setVisibility(View.GONE);
            findViewById(R.id.box_error).setVisibility(View.VISIBLE);
            findViewById(R.id.box_error).findViewById(R.id.btn_try_again).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadProductsPageOne();
                }
            });
        }

    }

    private void stopProgressDialog() {
        if (myProgressDialog != null) myProgressDialog.dismiss();
    }

    public static void newInstance(Context context, Product mProduct) {
        Intent intent = new Intent(context, ActivityComments.class);
        intent.putExtra("product_id", mProduct.getId());
        intent.putExtra("product", new Gson().toJson(mProduct));
        context.startActivity(intent);
    }

    private class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = ActivityComments.this.getLayoutInflater().inflate(R.layout.row_comment, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position) {
            final Comment comment = mCommentList.get(position);
            holder.textViewComment.setText(comment.comment);
            PersianCalendar persianCal = new PersianCalendar();
            String dtStart = comment.created_at;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date = format.parse(dtStart);
                persianCal.setGregorianChange(date);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.textViewDate.setText(persianCal.getPersianShortDateTime());
            holder.ratingBar.setRating(comment.rate);
        }

        @Override
        public int getItemCount() {
            return mCommentList == null ? 0 : mCommentList.size();
        }
    }

    private class CommentViewHolder extends RecyclerView.ViewHolder {
        ExpandableTextView textViewComment;
        TextView textViewDate;
        TextView textViewName;
        ImageView imageView;
        View root;
        RatingBar ratingBar;

        public CommentViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.iv_avatar);
            this.textViewComment = (ExpandableTextView) itemView.findViewById(R.id.tv_comment);
            this.textViewDate = (TextView) itemView.findViewById(R.id.tv_comment_date);
            this.textViewName = (TextView) itemView.findViewById(R.id.tv_comment_sender);
            this.ratingBar = (RatingBar) itemView.findViewById(R.id.ratingbar);
            this.root = itemView;
        }
    }

    @Override
    public void onBackPressed() {
        try {
            MainActivity.btnBack.setVisibility(View.GONE);
            MainActivity.viewLogo.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.getMessage();
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reqTag != null)
            AppController.getInstance().cancelPendingRequests(reqTag);
        if (reqOneTag != null)
            AppController.getInstance().cancelPendingRequests(reqOneTag);
        if (nonceReqTag != null)
            AppController.getInstance().cancelPendingRequests(nonceReqTag);
    }
}
