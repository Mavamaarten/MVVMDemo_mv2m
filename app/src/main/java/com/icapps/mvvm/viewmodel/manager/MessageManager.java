package com.icapps.mvvm.viewmodel.manager;

import android.support.annotation.StringRes;
import android.widget.Toast;

import it.cosenonjaviste.mv2m.ActivityHolder;

/**
 * Created by maartenvangiel on 21/09/16.
 */

public class MessageManager {

    public void showMessage(ActivityHolder activityHolder, @StringRes int message){
        if(activityHolder != null){
            Toast.makeText(activityHolder.getActivity(), message, Toast.LENGTH_LONG).show();
        }
    }

}
