package com.example.mayakoneval.etsyhw_volleyball;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    Button mButton;
    EditText mEdit;
    String words, test;
    static Context context;
    private static ListView mainListView;
    private static ArrayAdapter<String> listAdapter;
    public static Object c;
    static ArrayList<String> toAdd = new ArrayList<String>();
    static ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("hi:", "hello");
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Picasso.with(this).load("https://img1.etsystatic.com/120/1/10780513/il_fullxfull.1031765799_llw9.jpg").into(image);
        mButton = (Button) findViewById(R.id.button);
        mEdit = (EditText) findViewById(R.id.keywords);
        image = (ImageView) findViewById(R.id.iV);

        mainListView = (ListView) findViewById(R.id.listings);
        listAdapter = new ArrayAdapter<String>(this, R.layout.row, toAdd);
        mainListView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();



        mButton.setOnClickListener(
                new View.OnClickListener() {

                    public void onClick(View view) {

                        words = mEdit.getText().toString();
                        Log.d("keyword:", words);
                        String keywords = words;
                        String url = "https://openapi.etsy.com/v2/listings/active/?api_key=liwecjs0c3ssk6let4p1wqt9&keywords=" + keywords + "&fields=title,description,category_path,price,url&includes=MainImage";

                        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new JsonResponseListener(), new MyErrorListener());

                        mRequestQueue.add(jsonObjectRequest);
                        Log.d("JsonObjectRequest sent", jsonObjectRequest.toString());
                        toAdd.clear();
                        listAdapter.notifyDataSetChanged();

                    }
                });
    }


    public static class JsonResponseListener implements Response.Listener<JSONObject> {
        public static Object data;
        int count = 0;
        ArrayList titles = new ArrayList<String>();
        ArrayList descriptions = new ArrayList<String>();
        ArrayList prices = new ArrayList<String>();
        ArrayList images = new ArrayList<String>();
        public String delim = "[\"\"]";
        public String imageURL = "";
        @Override
        public void onResponse(JSONObject response) {
            try {
                //Log.d("count:",(response.getInt("count"))+"");
                count = response.getInt("count");
                data = response.getJSONArray("results");
                Log.d("Data:", data+"");
                //Log.d("length:", response.length() + "");
                Log.d("count_response:", ""+parseData(data+""));
                for (int i = 0; i < parseData(data+""); i++) {
                    JSONObject person = (JSONObject) response.getJSONArray("results").get(i);
                    titles.add(i, person.getString("title"));
                    Log.d("Title:", person.getString("title")+"");
                    /*descriptions.add(i, person.getString("description"));
                    prices.add(i, person.getString("price"));*/
                    images.add(i, person.get("MainImage"));
                    Log.d("loop:", i+"");
                }
                for(int i = 0; i<titles.size(); i++) {
                    toAdd.add(titles.get(i) + "");
                    Log.d("Images:", parseImage(images.get(i) + ""));
                    mainListView.setAdapter(listAdapter);
                    listAdapter.notifyDataSetChanged();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        public String parseImage(String im){
            String[] tokens = im.split(delim);
            for(int i = 0; i<tokens.length; i++){
                if(tokens[i].equals("url_fullxfull")){
                    imageURL = tokens[i+2];
                }
            }
        return imageURL;
        }
        public int parseData(String a){
            String[]tokens = a.split("title");
            int count = tokens.length-2;
            Log.d("count:", count+"");
            return 24;
        }
    }

    private class MyErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("error:", error.toString());
        }
    }
}