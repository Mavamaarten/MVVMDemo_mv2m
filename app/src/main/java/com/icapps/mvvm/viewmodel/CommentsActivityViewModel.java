package com.icapps.mvvm.viewmodel;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;

import com.icapps.mvvm.R;
import com.icapps.mvvm.model.Post;
import com.icapps.mvvm.model.PostRepository;
import com.icapps.mvvm.viewmodel.manager.MessageManager;
import com.icapps.mvvm.viewmodel.model.CommentsModel;

import it.cosenonjaviste.mv2m.ViewModel;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by maartenvangiel on 21/09/16.
 */

public class CommentsActivityViewModel extends ViewModel<Post, CommentsModel> implements SwipeRefreshLayout.OnRefreshListener {
    private final PostRepository postRepository;
    private final CommentsActivityViewModelListener listener;

    private MessageManager messageManager;
    private Subscription subscription;

    public CommentsActivityViewModel(PostRepository postRepository, CommentsActivityViewModelListener listener, MessageManager messageManager) {
        this.postRepository = postRepository;
        this.listener = listener;
        this.messageManager = messageManager;
    }

    private void loadData() {
        model.loading.set(true);
        subscription = postRepository.getPostComments(getModel().post.get().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(c -> {
                    model.comments.clear();
                    model.comments.addAll(c);
                    model.loading.set(false);
                    model.loaded.set(true);
                    model.commentCount.set(c.size());
                    listener.onCommentsUpdated();
                }, throwable -> {
                    messageManager.showMessage(activityHolder, R.string.error_loading);
                    model.loading.set(false);
                });
    }

    @Override
    public void resume() {
        if(!model.loaded.get()){
            loadData();
        }
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @NonNull
    @Override
    protected CommentsModel createModel() {
        CommentsModel model = new CommentsModel();
        model.post.set(getArgument());
        return model;
    }

    public interface CommentsActivityViewModelListener {
        void onCommentsUpdated();
    }
}
