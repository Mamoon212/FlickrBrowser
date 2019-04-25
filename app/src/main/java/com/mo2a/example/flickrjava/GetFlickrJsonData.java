package com.mo2a.example.flickrjava;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class GetFlickrJsonData extends AsyncTask<String, Void, List<Photo>> implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetFlickrJsonData";
    private List<Photo> photoList= null;
    private String baseUrl;
    private String lang;
    private boolean matchAll;
    private final OnDataAvailable callback;
    private boolean runningOnSameThread= false;

    interface OnDataAvailable{
        void onDataAvailable(List<Photo> data, DownloadStatus status);
    }

    GetFlickrJsonData( OnDataAvailable callback, String baseUrl, String lang, boolean matchAll) {
        Log.d(TAG, "GetFlickrJsonData: called");
        this.baseUrl = baseUrl;
        this.lang = lang;
        this.matchAll = matchAll;
        this.callback = callback;
    }

    void executeOnSameThread(String searchCriteria){
        Log.d(TAG, "executeOnSameThread: starts");
        runningOnSameThread= true;
        String destinationUri= createUri(searchCriteria, lang, matchAll);
        GetRawData getRawData= new GetRawData(this);
        getRawData.runInSameThread(destinationUri);
        Log.d(TAG, "executeOnSameThread: ends");
    }

    @Override
    protected List<Photo> doInBackground(String... params) {
        Log.d(TAG, "doInBackground: starts");
        String destinationUri= createUri(params[0], lang, matchAll);
        GetRawData getRawData= new GetRawData(this);
        getRawData.runInSameThread(destinationUri);
        Log.d(TAG, "doInBackground: ends");
        return photoList;
    }

    @Override
    protected void onPostExecute(List<Photo> photos) {
        Log.d(TAG, "onPostExecute: starts");
        if(callback != null){
            callback.onDataAvailable(photoList, DownloadStatus.OK);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    private String createUri(String searchCristeria, String lang, boolean matchAll){
        Log.d(TAG, "createUri: starts");
        return Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter("tags", searchCristeria)
                .appendQueryParameter("tagmode", matchAll? "ALL" : "ANY")
                .appendQueryParameter("lang", lang)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .build().toString();
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: status= "+ status);
        if(status == DownloadStatus.OK){
            photoList= new ArrayList<>();
            try{
                JSONObject jsonData= new JSONObject(data);
                JSONArray itemsArray= jsonData.getJSONArray("items");
                for(int i=0; i<itemsArray.length(); i++){
                    JSONObject jsonPhoto= itemsArray.getJSONObject(i);
                    String title= jsonPhoto.getString("title");
                    String author= jsonPhoto.getString("author");
                    String authorId= jsonPhoto.getString("author_id");
                    String tags= jsonPhoto.getString("tags");

                    JSONObject jsonMedia= jsonPhoto.getJSONObject("media");
                    String photoUrl= jsonMedia.getString("m");

                    String link= photoUrl.replaceFirst("_m.", "_b.");

                    Photo photoObject=new Photo(title, author, authorId, link,tags, photoUrl);
                    photoList.add(photoObject);

                    Log.d(TAG, "onDownloadComplete: "+ photoObject.toString());
                }
            }catch (JSONException e){
                Log.e(TAG, "onDownloadComplete: " + e.getMessage());
                e.printStackTrace();
                status= DownloadStatus.FAILED_OR_EMPTY;
            }
        }

        if(runningOnSameThread && callback != null){
            callback.onDataAvailable(photoList, status);
        }
        Log.d(TAG, "onDownloadComplete: ends");
    }


}
