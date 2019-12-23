package net.babiran.app;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import Models.Product;


/**
 * Created by Alireza on 2/4/2017.
 */

public class DialogAddNewCommant extends Dialog {

    View btnCancel, btnAdd;
    EditText etName, etComment, etEmail;
    Product mProduct;
    private ProgressDialog myProgressDialog;
    AddCommentCallback mCallback;

    public void setmCallback(AddCommentCallback mCallback) {
        this.mCallback = mCallback;
    }

    public DialogAddNewCommant(Context context, Product product) {
        super(context, R.style.NoTitleDialog);
        this.mProduct = product;
        init();
    }

    private void customize() {
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //Grab the window of the dialog, and change the width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    private void stopProgressDialog() {
        if (myProgressDialog != null) myProgressDialog.dismiss();
    }

    private void startProgressDialog() {
        myProgressDialog = new ProgressDialog(getContext());
        myProgressDialog.setMessage("در حال ارسال");
        myProgressDialog.show();
    }

    private void init() {
        setContentView(R.layout.dialog_add_comment);
        customize();
        btnCancel = findViewById(R.id.btn_cancel);
        btnAdd = findViewById(R.id.btn_send_comment);
        etComment = (EditText) findViewById(R.id.et_comment_content);
        etEmail = (EditText) findViewById(R.id.et_comment_email);
        etName = (EditText) findViewById(R.id.et_comment_name);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(etComment.getText().toString()) ||
                        !TextUtils.isEmpty(etEmail.getText().toString()) ||
                        !TextUtils.isEmpty(etName.getText().toString())) {
                    final Comment comment = new Comment();
                    java.util.Calendar calendar = java.util.Calendar.getInstance();
                    comment.created_at = (calendar.get(java.util.Calendar.YEAR) + "/" +
                            calendar.get(java.util.Calendar.MONTH) + "/" +
                            calendar.get(java.util.Calendar.DATE) + " " +
                            calendar.get(java.util.Calendar.HOUR) + ":" +
                            calendar.get(java.util.Calendar.MINUTE) + ":" +
                            calendar.get(java.util.Calendar.SECOND));
//                    comment.setAuthor(etName.getText().toString());
                    comment.comment = (etComment.getText().toString());
                    comment.rate = (((RatingBar) findViewById(R.id.ratingbar)).getRating());
                    if (!new ConnectionDetector(getContext()).isConnectingToInternet()) {
                        Toast.makeText(getContext(), "connection_error", Toast.LENGTH_LONG).show();
                        return;
                    }
                    startProgressDialog();
                    RequestHandler.SubmitComment(getContext(), mProduct.getId(), comment.comment, comment.rate, new SubmitCommentCallBack() {
                        @Override
                        public void onSubmitCommentSuccessAction() {
                            stopProgressDialog();
                            if (comment != null) {
                                Toast.makeText(getContext(), "دیدگاه با موفقیت ثبت شد.", Toast.LENGTH_LONG).show();
                                dismiss();
                                if (mCallback != null)
                                    mCallback.onAddComment(comment);
                            }
                        }

                        @Override
                        public void onSubmitCommentErrorAction(String error) {
                            stopProgressDialog();
                            Toast.makeText(getContext(), "خطا. دوباره نلاش کنید.", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
