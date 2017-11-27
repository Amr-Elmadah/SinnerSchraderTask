package com.amr.sinnerschraderparsingtask.main;

import com.amr.sinnerschraderparsingtask.BasePresenter;
import com.amr.sinnerschraderparsingtask.BaseView;

/**
 * Created by Amr El-Madah on 11/27/2017.
 */

public interface MainContract {


    interface View extends BaseView<Presenter> {
        void showLoadingIndicator(boolean active);

        void showErrorInput();

        void showResultSuccess(String result);
    }

    interface Presenter extends BasePresenter {
        void validateInput(String input);

        void getPagesTitle(String input);
    }

}
