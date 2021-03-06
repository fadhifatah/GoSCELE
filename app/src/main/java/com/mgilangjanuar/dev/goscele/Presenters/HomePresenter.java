package com.mgilangjanuar.dev.goscele.Presenters;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.mgilangjanuar.dev.goscele.Adapters.HomePostAdapter;
import com.mgilangjanuar.dev.goscele.Models.HomePostModel;
import com.mgilangjanuar.dev.goscele.Models.ListHomePostModel;
import com.mgilangjanuar.dev.goscele.Services.HomePostService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by muhammadgilangjanuar on 5/20/17.
 */

public class HomePresenter {

    private Activity activity;
    private View view;

    private HomePostService homePostService;
    private ListHomePostModel listHomePostModel;

    public HomePresenter(Activity activity, View view) {
        this.activity = activity;
        this.view = view;
        this.homePostService = new HomePostService();
        this.listHomePostModel = new ListHomePostModel(activity);
    }

    public HomePostAdapter buildAdapter() {
        if (listHomePostModel.getSavedHomePostModelList() == null) {
            buildModel();
        }
        return new HomePostAdapter(activity, listHomePostModel.homePostModelList);
    }

    public void buildModel() {
        try {
            listHomePostModel.clear();
            listHomePostModel.homePostModelList = new ArrayList<>();
            for (Map<String, String> e : homePostService.getPosts()) {
                HomePostModel model = new HomePostModel();
                model.url = e.get("url");
                model.title = e.get("title");
                model.author = e.get("author");
                model.date = e.get("date");
                model.content = e.get("content");
                listHomePostModel.homePostModelList.add(model);
            }
            listHomePostModel.save();
        } catch (IOException e) {
            Log.e("HomePresenter", String.valueOf(e.getMessage()));
        }
    }

    public void clear() {
        this.listHomePostModel.clear();
    }

    public interface HomeServicePresenter {
        void setupHome(View view);
    }
}
