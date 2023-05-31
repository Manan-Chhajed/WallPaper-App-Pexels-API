package com.example.wallpaperapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface{

    private EditText searchEdt;
    private ImageView searchIV;
    private RecyclerView categoryRV, wallpaerRV;
    private ProgressBar loadingPB;
    private ArrayList<String> wallpaperArrayList;
    private ArrayList<CategoryRVModal> categoryRVModelArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private WallpaperRVAdapter wallpaperRVAdapter;
//    563492ad6f917000010000012250b96bbfe640668514d7641540e461

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEdt = findViewById(R.id.idEditSearch);
        searchIV = findViewById(R.id.idIVSearch);
        categoryRV = findViewById(R.id.idRVCategory);
        wallpaerRV = findViewById(R.id.idRVWallpapers);
        loadingPB = findViewById(R.id.idRLLoading);

        wallpaperArrayList = new ArrayList<>();
        categoryRVModelArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false);
        categoryRV.setLayoutManager(linearLayoutManager);
        categoryRVAdapter = new CategoryRVAdapter(categoryRVModelArrayList, this, this::onCategoryClick);
        categoryRV.setAdapter(categoryRVAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        wallpaerRV.setLayoutManager(gridLayoutManager);
        wallpaperRVAdapter = new WallpaperRVAdapter(wallpaperArrayList, this);
        wallpaerRV.setAdapter(wallpaperRVAdapter);

        getCategories();

        getWallpapers();

        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchStr = searchEdt.getText().toString();
                if(searchStr.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter your search Query", Toast.LENGTH_SHORT).show();
                }
                else{
                    getWallpapersbyCategory(searchStr);
                }
            }
        });
    }

    private void getWallpapersbyCategory(String category) {
        wallpaperArrayList.clear();
        loadingPB.setVisibility(View.VISIBLE);
        String url = "https://api.pexels.com/v1/search?query="+category+"&per_page=30&page=1";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray photoArray = null;
                loadingPB.setVisibility(View.GONE);
                try {
                    photoArray = response.getJSONArray("photos");
                    for(int i=0; i<photoArray.length(); i++){
                        JSONObject photoObj = photoArray.getJSONObject(i);
                        String imgUrl = photoObj.getJSONObject("src").getString("portrait");
                        wallpaperArrayList.add(imgUrl);
                    }
                    wallpaperRVAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Failed to load wallpapers", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String > headers = new HashMap<>();
                headers.put("Authorization", "563492ad6f917000010000012250b96bbfe640668514d7641540e461");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void getWallpapers() {
        wallpaperArrayList.clear();
        loadingPB.setVisibility(View.VISIBLE);
        String url = "https://api.pexels.com/v1/curated?per_page=30";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadingPB.setVisibility(View.GONE);
                        try {
                            JSONArray photoArray = response.getJSONArray("photos");
                            for(int i=0; i<photoArray.length(); i++){
                                JSONObject photoObj = photoArray.getJSONObject(i);
                                String imgUrl = photoObj.getJSONObject("src").getString("portrait");
                                wallpaperArrayList.add(imgUrl);
                            }
                            wallpaperRVAdapter.notifyDataSetChanged();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Failed to load wallpaper", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "563492ad6f917000010000012250b96bbfe640668514d7641540e461");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }

    private void getCategories() {
        categoryRVModelArrayList.add(new CategoryRVModal("Technology", "https://elements-video-cover-images-0.imgix.net/files/32a57979-dbac-40bc-b382-4f72cfbf6fbe/inline_image_preview.jpg?auto=compress&crop=edges&fit=crop&fm=jpeg&h=630&w=1200&s=3af7193eab4c8dc844f7aa2feabbdd0b"));
        categoryRVModelArrayList.add(new CategoryRVModal("Travel", "https://www.britishairways.com/assets/images/MediaHub/Media-Database/Royalty-free-RF/People/Travelling/1036067520_1200x675.jpg"));
        categoryRVModelArrayList.add(new CategoryRVModal("Nature", "https://images.unsplash.com/photo-1619726578880-5da6b1be246c?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTJ8fG5hdHVyYWwlMjBiYWNrZ3JvdW5kfGVufDB8fDB8fA%3D%3D&w=1000&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModal("Architecture", "https://images.unsplash.com/photo-1488972685288-c3fd157d7c7a?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NHx8YXJjaGl0ZWN0dXJlfGVufDB8fDB8fA%3D%3D&w=1000&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModal("Art", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSZWO2bCkfUEQ1K09fvjDnBNMeNYtLQMNJv4g&usqp=CAU"));
        categoryRVModelArrayList.add(new CategoryRVModal("Music", "https://images.unsplash.com/photo-1605020420620-20c943cc4669?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8N3x8Z3VpdGFyfGVufDB8fDB8fA%3D%3D&w=1000&q=80"));
        categoryRVModelArrayList.add(new CategoryRVModal("Cars", "https://wallpaperaccess.com/full/5379.jpg"));
        categoryRVModelArrayList.add(new CategoryRVModal("Programming", "https://images.unsplash.com/photo-1542831371-29b0f74f9713?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8M3x8cHJvZ3JhbW1pbmd8ZW58MHx8MHx8&w=1000&q=80"));


        categoryRVAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryRVModelArrayList.get(position).getCategory();
        getWallpapersbyCategory(category);
    }
}