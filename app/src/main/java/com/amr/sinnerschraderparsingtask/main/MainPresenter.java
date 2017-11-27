package com.amr.sinnerschraderparsingtask.main;

import android.content.Context;

import com.amr.sinnerschraderparsingtask.data.models.LinkModel;
import com.amr.sinnerschraderparsingtask.data.models.OutputModel;
import com.amr.sinnerschraderparsingtask.utils.PageTitle;
import com.amr.sinnerschraderparsingtask.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by Amr El-Madah on 11/27/2017.
 */

class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;
    private OutputModel outputModel;
    private Context mContext;

    MainPresenter(Context context, MainContract.View view) {
        mContext = checkNotNull(context);
        mView = checkNotNull(view);
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void validateInput(String input) {
        if (input.isEmpty()) {
            mView.showErrorInput();
        } else {
            getPagesTitle(input);
        }
    }

    @Override
    public void getPagesTitle(final String input) {
        mView.showLoadingIndicator(true);
        outputModel = new OutputModel();
        outputModel = StringUtils.extractUrls(input.trim(), outputModel);
        if (outputModel.getLinks() != null && !outputModel.getLinks().isEmpty()) {
            new PageTitle(mContext, outputModel.getLinks(), new PageTitle.OnAllTitlesExtracted() {
                @Override
                public void setAllTitlesExtracted(List<LinkModel> links) {
                    outputModel.setLinks(links);
                    extractOutput(input);
                }
            }, 0).execute();
        } else {
            extractOutput(input);
        }
    }

    void extractOutput(String input) {
        outputModel = StringUtils.extractMentions(input, outputModel);
        outputModel = StringUtils.extractEmojis(input, outputModel);
        Gson gson = new Gson();
        Type type = new TypeToken<OutputModel>() {
        }.getType();
        String output = gson.toJson(outputModel, type);
        mView.showResultSuccess(StringUtils.formatStringToJson(output));
        mView.showLoadingIndicator(false);
    }
}
