package com.icapps.mvvm.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import com.icapps.mvvm.MVPApplication;
import com.icapps.mvvm.R;
import com.icapps.mvvm.adapter.CommentsAdapter;
import com.icapps.mvvm.databinding.ActivityCommentsBinding;
import com.icapps.mvvm.viewmodel.CommentsActivityViewModel;
import com.icapps.mvvm.viewmodel.manager.MessageManager;

import it.cosenonjaviste.mv2m.ViewModelActivity;


/**
 * Created by maartenvangiel on 20/09/16.
 */

public class CommentsActivity extends ViewModelActivity<CommentsActivityViewModel> implements CommentsActivityViewModel.CommentsActivityViewModelListener {

    private CommentsAdapter commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCommentsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_comments);
        binding.setViewModel(viewModel);

        commentsAdapter = new CommentsAdapter(viewModel.getModel().comments);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);

        binding.rcvComments.setLayoutManager(layoutManager);
        binding.rcvComments.setNestedScrollingEnabled(false);
        binding.rcvComments.setAdapter(commentsAdapter);

        setTitle("Post details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public CommentsActivityViewModel createViewModel() {
        return new CommentsActivityViewModel(((MVPApplication) getApplication()).getPostRepository(), this, new MessageManager());
    }

    @Override
    public void onCommentsUpdated() {
        commentsAdapter.notifyDataSetChanged();
    }
}