package net.babiran.app;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Mohammad on 7/27/2017.
 */

public class CommonQuestionActivity extends AppCompatActivity {
    private StickyAndExpandableAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_quide_usage);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.btnBack.setVisibility(View.GONE);
                MainActivity.viewLogo.setVisibility(View.VISIBLE);
                finish();
            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new StickyAndExpandableAdapter(mRecyclerView);
        mAdapter.addAll(new ArrayList<CommonQuestion>());

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(false);

        // Set layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        RequestHandler.getCommonQuestions(this, new GetCommonQuestionsCallBack() {
            @SuppressLint("LongLogTag")
            @Override
            public void onGetCommonQuestionsSuccessAction(CommonQuestion[] commonQuestions) {
                mAdapter.addAll(handle(commonQuestions), false);
                mAdapter.notifyDataSetChanged();
                Log.e("CommonQuestionActivityOK.this", mAdapter.getList().size() + "");
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onGetCommonQuestionsErrorAction(String error) {
                Log.e("CommonQuestionActivity.this", error);
            }
        });
    }

    private List<CommonQuestion> handle(CommonQuestion[] commonQuestions) {
        if (commonQuestions == null) return null;
        List<String> headers = new ArrayList<>();
        List<CommonQuestion> subjectList = new ArrayList<>();
        for (int i = 0; i < commonQuestions.length; i++) {
            if (headers.contains(commonQuestions[i].subject)) {
                commonQuestions[i].hraderId = headers.indexOf(commonQuestions[i].subject);
            } else {
                headers.add(commonQuestions[i].subject);
                commonQuestions[i].hraderId = headers.indexOf(commonQuestions[i].subject);
            }
            subjectList.add(commonQuestions[i]);
            CommonQuestion parent = commonQuestions[i].clone();
            parent.isParent = true;
            List<CommonQuestion> list = new ArrayList<>();
            list.add(commonQuestions[i]);
            parent.childs.addAll(list);
            subjectList.add(parent);
        }
        Collections.sort(subjectList, new Comparator<CommonQuestion>() {
            @Override
            public int compare(CommonQuestion commonQuestion, CommonQuestion t1) {
                return String.valueOf(commonQuestion.hraderId).compareTo(String.valueOf(t1.hraderId));
            }
        });
        return subjectList;
    }
}