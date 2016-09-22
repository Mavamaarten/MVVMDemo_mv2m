package com.icapps.mvvm.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.icapps.mvvm.MVPApplication;
import com.icapps.mvvm.R;
import com.icapps.mvvm.adapter.PostsAdapter;
import com.icapps.mvvm.databinding.ActivityPostsBinding;
import com.icapps.mvvm.viewmodel.PostsActivityViewModel;
import com.icapps.mvvm.viewmodel.manager.MessageManager;

import it.cosenonjaviste.mv2m.ViewModelActivity;


public class PostsActivity extends ViewModelActivity<PostsActivityViewModel> implements PostsActivityViewModel.PostsActivityViewModelListener {
    private PostsAdapter postsAdapter;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);

        ActivityPostsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_posts);
        binding.setViewModel(viewModel);

        postsAdapter = new PostsAdapter(viewModel, viewModel.getModel().getPosts());
        binding.rcvPosts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.rcvPosts.setAdapter(postsAdapter);
    }

    @Override
    public PostsActivityViewModel createViewModel() {
        return new PostsActivityViewModel(((MVPApplication)getApplication()).getPostRepository(), this, new MessageManager());
    }

    @Override
    public void onPostsUpdated() {
        postsAdapter.notifyDataSetChanged();
    }
}
