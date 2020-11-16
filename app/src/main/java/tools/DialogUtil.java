package tools;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import net.babiran.app.R;

import java.util.Objects;

public class DialogUtil {
    public static void showDialog(Activity activity, Dialog dialog, boolean cancelable, boolean hasBack, boolean wrapContent) {
        if (activity.isFinishing()) {
            return;
        }

        dialog.setCancelable(cancelable);
        if (hasBack)

            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.dialog_bg));
        else
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(activity,R.drawable.dialog_bg_no_padding));
        dialog.show();
        Display display = Objects.requireNonNull(activity).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        width = (int) ((width) * 0.8);
        int height = (wrapContent) ? ConstraintLayout.LayoutParams.WRAP_CONTENT : ConstraintLayout.LayoutParams.MATCH_PARENT;

        dialog.getWindow().setLayout(width, height);
    }

    public static void showDialog(Activity activity, Dialog dialog, boolean cancelable) {
        dialog.setCancelable(cancelable);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(activity,R.drawable.dialog_bg_custom));
        dialog.show();

        int width = ConstraintLayout.LayoutParams.MATCH_PARENT;
        int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        dialog.getWindow().setLayout(width, height);
    }
}
