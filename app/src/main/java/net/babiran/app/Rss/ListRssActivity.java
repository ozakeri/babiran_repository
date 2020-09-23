package net.babiran.app.Rss;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.babiran.app.R;
import net.babiran.app.Servic.GETING;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import dmax.dialog.SpotsDialog;
import rss.RssItem;
import rss.RssReader;
import tools.AppConfig;
import ui_elements.MyTextView;

public class ListRssActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String Url = "", name = "";
    private AlertDialog prograsDialog;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private AdapterUserList mAdapter;
    private AdapterUserListMy mAdaptermy;
    private List<RssFeedModel> mFeedModelList = new ArrayList<>();
    List<RssFeedModel> itemsList = new ArrayList<RssFeedModel>();
    private String TAG = "TAG";
    private String mFeedTitle;
    private String mFeedLink;
    private String mFeedDescription;
    public static String ID_ME = "";



    // RSS XML document CHANNEL tag
    private static String TAG_CHANNEL = "channel";
    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_DESRIPTION = "description";
    private static String TAG_ITEM = "item";
    private static String TAG_PUB_DATE = "pubDate";
    private static String TAG_GUID = "guid";
    private static String TAG_IMAGE = "url";


    List<String> list = new ArrayList<>();

    MyTextView label;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_rss);
        ListtoListActivity.ID_ME = null;
        label = (MyTextView) findViewById(R.id.label);

        if (!TextUtils.isEmpty(name)) {
            label.setText(name);
        }
        /////////////////////////////////////
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
////////////////////////////////////////////
        INIT();

        prograsDialog.show();
        if (getIntent().getExtras() != null && getIntent().getExtras().getString("id") != null) {

            if (!TextUtils.isEmpty(getIntent().getExtras().getString("id"))) {
                //my
                Listed();
            }
        }else if (getIntent().getExtras().getString("link") != null) {
            // new FetchFeedTask().execute((Void) null);//XML
            Url = getIntent().getExtras().getString("link");
            name = getIntent().getExtras().getString("name");
            this.getXmlFromUrl(Url);
            Handler handler = new Handler();
            Thread someThread = new Thread() {

                @Override
                public void run() {

                    //some actions
                    handler.post(new Runnable() {
                        public void run() {
                            RssReader rssReader = new RssReader(Url);
                            recyclerView.setVisibility(View.VISIBLE);
                            try {
                                List<RssItem> rssItems = rssReader.getItems();
                                for (RssItem r:rssItems) {
                                    RssFeedModel rssItem = new RssFeedModel(r.getTitle(), r.getLink(), r.getDescription(), r.getImageUrl());
                                    // adding item to list
                                    itemsList.add(rssItem);
                                }
                                mAdapter = new AdapterUserList(ListRssActivity.this, itemsList);
                                recyclerView.setAdapter(mAdapter);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            };

            someThread.start();
            prograsDialog.dismiss();
        }


        //  new Title().execute((Void) null);                        //HTML


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(ListRssActivity.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                if (getIntent().getExtras() != null && !TextUtils.isEmpty(getIntent().getExtras().getString("id"))) {
                    String Link = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                            .itemView.findViewById(R.id.txt_rc_rss_link)).getText().toString();

                    Intent intent = new Intent(ListRssActivity.this, LastListActivity.class);
                    intent.putExtra("id", Link);
                    startActivity(intent);

                } else {
               /*     String Link = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                            .itemView.findViewById(R.id.txt_rc_rss_link)).getText().toString();
                    String tit = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                            .itemView.findViewById(R.id.txt_rc_rss)).getText().toString();

                    String desc = ((TextView) recyclerView.findViewHolderForAdapterPosition(position)
                            .itemView.findViewById(R.id.txt_rc_rss)).getText().toString();*/

                    RssFeedModel rssFeedModel = itemsList.get(position);

                    Intent intent = new Intent(ListRssActivity.this, ShowRssActivity.class);
                    //link = "https://www.irna.ir/news/83908201/";
                    intent.putExtra("link", rssFeedModel.link);
                    intent.putExtra("title", rssFeedModel.title);
                    intent.putExtra("desc", rssFeedModel.description);
                    intent.putExtra("imgUrl", rssFeedModel.img_url);

                    startActivity(intent);
                }

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


    }


    private void Listed() {
        List<GETING> s = AppConfig.GETT;
        for (int i = 0; i < s.size(); i++) {

            if (s.get(i).getParentId() == Integer.parseInt(getIntent().getExtras().getString("id"))) {
                AppConfig.GETT = s;
                list.add(s.get(i).getName() + "##" + s.get(i).getId() + "##" + s.get(i).getHasChild());
            }
        }
        prograsDialog.dismiss();
        mAdaptermy = new AdapterUserListMy(ListRssActivity.this, list);


        recyclerView.setAdapter(mAdaptermy);


        //    prograsDialog.dismiss();
    }


    private void INIT() {
        prograsDialog = new SpotsDialog(ListRssActivity.this);
        recyclerView = (RecyclerView) findViewById(R.id.rec);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

    }

    /////////////////////////     XML          XML          ///////////////////////////////////////////////////////////////////////////


    @SuppressLint("ObsoleteSdkInt")
    public String getXmlFromUrl(String url) {
        String xml = null;

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xml;
    }

    public Document getDomElement(String xml) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);
        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
        return doc;
    }

    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }

    public final String getElementValue(Node elem) {
        Node child;
        if (elem != null) {
            if (elem.hasChildNodes()) {
                for (child = elem.getFirstChild(); child != null; child = child
                        .getNextSibling()) {
                    if (child.getNodeType() == Node.TEXT_NODE || (child.getNodeType() == Node.CDATA_SECTION_NODE)) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

 /*   private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        private String urlLink;

        @Override
        protected void onPreExecute() {

            System.out.println("Url=======" + Url);
            //  mSwipeLayout.setRefreshing(true);
            // urlLink = mEditText.getText().toString();
            //urlLink="http://www.irna.ir/fa/rss.aspx?kind=-1";
            urlLink = Url;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(urlLink))
                return false;

            try {
                if (!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
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
        protected void onPostExecute(Boolean success) {
            //   mSwipeLayout.setRefreshing(false);

            if (success) {
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
    }*/


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
                if (name == null)
                    continue;

                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("MyXmlParser", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {


                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();

                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                    Log.e("NA 1 ", title);
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                    Log.e("NA 2 ", link);
                } else if (name.equalsIgnoreCase("media:content")) {

                    description = xmlPullParser.getAttributeValue(null, "url");
                    Log.e("NA  3", name);
                    Log.e("FF  ", xmlPullParser.getAttributeValue(null, "url"));

                }


                if (title != null && link != null && description != null) {
                    if (isItem) {
                        RssFeedModel item = new RssFeedModel(title, link, description);
                        items.add(item);
                    } else {
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
