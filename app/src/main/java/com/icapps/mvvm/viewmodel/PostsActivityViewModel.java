package com.icapps.mvvm.viewmodel;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.icapps.mvvm.R;
import com.icapps.mvvm.activity.CommentsActivity;
import com.icapps.mvvm.adapter.PostsAdapter;
import com.icapps.mvvm.model.Post;
import com.icapps.mvvm.model.PostRepository;
import com.icapps.mvvm.viewmodel.manager.MessageManager;
import com.icapps.mvvm.viewmodel.model.PostsActivityModel;

import it.cosenonjaviste.mv2m.ArgumentManager;
import it.cosenonjaviste.mv2m.ViewModel;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by maartenvangiel on 20/09/16.
 */

public class PostsActivityViewModel extends ViewModel<Void, PostsActivityModel> implements SwipeRefreshLayout.OnRefreshListener, PostsAdapter.PostsAdapterListener {
    private Subscription subscription;
    private PostRepository postRepository;
    private PostsActivityViewModelListener listener;
    private MessageManager messageManager;

    public PostsActivityViewModel(PostRepository postRepository, PostsActivityViewModelListener listener, MessageManager messageManager) {
        this.postRepository = postRepository;
        this.listener = listener;
        this.messageManager = messageManager;
    }

    @Override
    public void resume() {
        if (!model.loaded.get()) {
            loadData();
        }
    }

    private void loadData() {
        model.loading.set(true);
        subscription = postRepository.getPosts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(p -> {
                    model.posts.clear();
                    model.posts.addAll(p);
                    model.loading.set(false);
                    model.loaded.set(true);
                    listener.onPostsUpdated();
                }, throwable -> {
                    model.loading.set(false);
                    messageManager.showMessage(activityHolder, R.string.error_loading);
                });
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onPostClicked(Post post, int position, View titleView, View bodyView) {
        ArgumentManager.startActivity(activityHolder.getActivity(), CommentsActivity.class, post);
    }

    @NonNull
    @Override
    protected PostsActivityModel createModel() {
        return new PostsActivityModel();
    }

    public interface PostsActivityViewModelListener {
        void onPostsUpdated();
    }
}
