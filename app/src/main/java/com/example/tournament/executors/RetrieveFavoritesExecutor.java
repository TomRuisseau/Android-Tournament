package com.example.tournament.executors;

import android.os.Handler;
import android.os.Looper;

import com.example.tournament.dataClasses.Candidate;
import com.example.tournament.interfaces.CandidatesFetchedCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RetrieveFavoritesExecutor {
    private ExecutorService mExecutorService;
    private Handler handler;

    public RetrieveFavoritesExecutor() {
        this.mExecutorService = Executors.newSingleThreadExecutor();
        this.handler = new Handler(Looper.getMainLooper());
    }

    public  void getFavorites(CandidatesFetchedCallback callback){
        mExecutorService.execute(() -> {
            try {

                handler.post(() -> {
                    callback.onCandidatesFetched(new ArrayList<>());
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void shutdown() {
        mExecutorService.shutdown();
    }
}
