package com.mo2a.example.flickrjava;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus{IDLE, PROCESSING, NOT_INITIALIZED, FAILED_OR_EMPTY, OK}


class GetRawData extends AsyncTask<String, Void, String> {

    private static final String TAG = "GetRawData";
    private DownloadStatus downloadStatus;
    private OnDownloadComplete callBack;

    interface OnDownloadComplete{
        void onDownloadComplete(String data, DownloadStatus status);
    }

    GetRawData(OnDownloadComplete callBack) {
        this.downloadStatus = DownloadStatus.IDLE;
        this.callBack= callBack;
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection conn= null;
        BufferedReader reader= null;

        if(strings == null){
            downloadStatus= DownloadStatus.NOT_INITIALIZED;
            return null;
        }

        try {
            downloadStatus= DownloadStatus.PROCESSING;
            URL url= new URL(strings[0]);
            conn= (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int response= conn.getResponseCode();
            Log.d(TAG, "doInBackground: response code: "+ response);

            StringBuilder result= new StringBuilder();
            reader= new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while((line= reader.readLine()) != null){
                result.append(line).append("\n");
            }

            downloadStatus= DownloadStatus.OK;
            return result.toString();
        }catch (MalformedURLException e){
            Log.e(TAG, "doInBackground: invalid url" + e.getMessage());
        }catch (IOException e){
            Log.e(TAG, "doInBackground: error reading data" + e.getMessage());
        }catch (SecurityException e){
            Log.e(TAG, "doInBackground: security error maybe permission" + e.getMessage());
        }finally {
            if(conn != null){
                conn.disconnect();
            }
            if(reader != null){
                try {
                    reader.close();
                }catch (IOException e){
                    Log.e(TAG, "doInBackground: Error closing stream " + e.getMessage());
                }
            }
        }
        downloadStatus= DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
//        Log.d(TAG, "onPostExecute: starts with "+ s);
        if(callBack != null){
            callBack.onDownloadComplete(s, downloadStatus);
        }
        Log.d(TAG, "onPostExecute: ends");
    }

    void runInSameThread(String s){
        Log.d(TAG, "runInSameThread: starts");
//        onPostExecute(doInBackground(s));
        if(callBack != null){
            String result= doInBackground(s);
            callBack.onDownloadComplete(doInBackground(s), downloadStatus);
        }
        Log.d(TAG, "runInSameThread: ends");
    }
}
