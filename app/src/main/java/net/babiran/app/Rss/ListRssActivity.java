package net.babiran.app.Rss;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.babiran.app.R;
import net.babiran.app.Servic.GETING;
import net.babiran.app.Servic.GETINGBlog;
import net.babiran.app.Servic.MyInterFace;
import net.babiran.app.Servic.MyServices;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import tools.AppConfig;
import ui_elements.MyTextView;

public class ListRssActivity extends AppCompatActivity
{
   private Toolbar toolbar;
   private String Url="",name="";
    private AlertDialog prograsDialog;
   private RecyclerView recyclerView;
   private LinearLayoutManager linearLayoutManager;
   private AdapterUserList mAdapter;
    private AdapterUserListMy mAdaptermy;
   private List<RssFeedModel> mFeedModelList = new ArrayList<>();
    private String TAG="TAG";
    private String mFeedTitle;
    private String mFeedLink;
    private String mFeedDescription;
    public static String ID_ME = "";


    List<String> list = new ArrayList<>();

    MyTextView label;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_rss);
        ListtoListActivity.ID_ME = null;
        Bundle bundle = getIntent().getExtras();
        label = (MyTextView) findViewById(R.id.label);

        Url=bundle.getString("link");
        name=bundle.getString("name");
        if(!TextUtils.isEmpty(name)){label.setText(name);}
        /////////////////////////////////////
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (toolbar != null)
        {
            setSupportActionBar(toolbar);
        }
////////////////////////////////////////////
        INIT();

      prograsDialog.show();
      if(!TextUtils.isEmpty(getIntent().getExtras().getString("id")))
      {
          //my
          Listed();
      }
      else
      {
          new FetchFeedTask().execute((Void) null);//XML
      }


        //  new Title().execute((Void) null);                        //HTML


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(ListRssActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position)
            {


                if(!TextUtils.isEmpty(getIntent().getExtras().getString("id")))
                {
                    String Link = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                            .itemView.findViewById(R.id.txt_rc_rss_link)).getText().toString();

                    Intent intent =new Intent(ListRssActivity.this,LastListActivity.class);
                    intent.putExtra("id",Link);
                    startActivity(intent);

                    System.out.println("id=====11=" + Link);
                }
                else
                {
                    String Link = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                            .itemView.findViewById(R.id.txt_rc_rss_link)).getText().toString();
                    String tit = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                            .itemView.findViewById(R.id.txt_rc_rss)).getText().toString();
                    Intent intent =new Intent(ListRssActivity.this,ShowRssActivity.class);
                    intent.putExtra("link",Link);
                    intent.putExtra("title",tit);

                    System.out.println("Link====" + Link);
                    System.out.println("tit====" + tit);

                    startActivity(intent);
                }

            }

            @Override
            public void onLongItemClick(View view, int position)
            {

            }
        }));


    }


    private void Listed()
    {
        List<GETING> s = AppConfig.GETT;
        for (int i = 0 ; i<s.size() ; i++)
        {

            if(s.get(i).getParentId()==Integer.parseInt(getIntent().getExtras().getString("id")))
            {
                AppConfig.GETT =s;
                list.add(s.get(i).getName()+"##"+s.get(i).getId()+"##"+s.get(i).getHasChild());
                System.out.println("response====" + s.get(i).getId());
                System.out.println("response====" + s.get(i).getParentId());
            }
        }
        prograsDialog.dismiss();
        mAdaptermy = new AdapterUserListMy(ListRssActivity.this, list);


        recyclerView.setAdapter(mAdaptermy);



        //    prograsDialog.dismiss();
    }



    private void INIT()
    {
        prograsDialog = new SpotsDialog(ListRssActivity.this);
        recyclerView=(RecyclerView)findViewById(R.id.rec);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

    }

    /////////////////////////     XML          XML          ///////////////////////////////////////////////////////////////////////////

    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean>
    {

        private String urlLink;

        @Override
        protected void onPreExecute()
        {
            //  mSwipeLayout.setRefreshing(true);
            // urlLink = mEditText.getText().toString();
            //urlLink="http://www.irna.ir/fa/rss.aspx?kind=-1";
            urlLink=Url;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(urlLink))
                return false;

            try {
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "http://" + urlLink;

                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();
                mFeedModelList = parseFeed(inputStream);
                return true;
            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            } catch (XmlPullParserException e) {
                Log.e(TAG, "Error", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success)
        {
            //   mSwipeLayout.setRefreshing(false);

            if (success)
            {
                //  textView.setText("Feed Title: " + mFeedTitle +"\n" );
                //  textView.setText("Feed Description: " + mFeedDescription+"\n" );
                //   textView.setText("Feed Link: " + mFeedLink+"\n" );

                recyclerView.setVisibility(View.VISIBLE);
                mAdapter = new AdapterUserList(ListRssActivity.this, mFeedModelList);

                recyclerView.setAdapter(mAdapter);
                prograsDialog.dismiss();

                // Fill RecyclerView
                //    mRecyclerView.setAdapter(new RssFeedListAdapter(mFeedModelList));
                //    Log.e("LIST    :  ","L      "+mFeedModelList.get(5).link);

            } else {
                Toast.makeText(ListRssActivity.this,
                        "Enter a valid Rss feed url",
                        Toast.LENGTH_LONG).show();
            }
        }
    }



    public List<RssFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException,
            IOException {
        String title = null;
        String link = null;
        String description = null;
        String img = null;
        boolean isItem = false;
        List<RssFeedModel> items = new ArrayList<>();

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if(name == null)
                    continue;

                if(eventType == XmlPullParser.END_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if(name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("MyXmlParser", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT)
                {


                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();

                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                    Log.e("NA 1 ",title);
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                    Log.e("NA 2 ",link);
                }
                else if (name.equalsIgnoreCase("media:content"))
                {

                    description = xmlPullParser.getAttributeValue(null, "url");
                    Log.e("NA  3",name);
                    Log.e("FF  ",  xmlPullParser.getAttributeValue(null, "url"));

                }





                if (title != null && link != null && description != null) {
                    if(isItem) {
                        RssFeedModel item = new RssFeedModel(title, link, description);
                        items.add(item);
                    }
                    else {
                        mFeedTitle = title;
                        mFeedLink = link;
                        mFeedDescription = description;
                    }

                    title = null;
                    link = null;
                    description = null;
                    isItem = false;
                }
            }

            return items;
        } finally {
            inputStream.close();
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////









}
