package com.icapps.mvvm;

import com.icapps.mvvm.model.Post;
import com.icapps.mvvm.model.PostRepository;
import com.icapps.mvvm.viewmodel.PostsActivityViewModel;
import com.icapps.mvvm.viewmodel.manager.MessageManager;
import com.icapps.mvvm.viewmodel.model.PostsActivityModel;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by maartenvangiel on 21/09/16.
 */

public class PostsViewModelTest {

    @Before
    public void setUp() throws Exception {
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @After
    public void tearDown() {
        RxAndroidPlugins.getInstance().reset();
    }

    @Test public void testLoadingPosts(){
        List<Post> posts = new ArrayList<>();
        posts.add(new Post());

        PostRepository postRepository = mock(PostRepository.class);
        when(postRepository.getPosts()).thenReturn(Observable.just(posts));

        MessageManager messageManager = mock(MessageManager.class);
        PostsActivityViewModel.PostsActivityViewModelListener listener = mock(PostsActivityViewModel.PostsActivityViewModelListener.class);

        PostsActivityViewModel viewModel = new PostsActivityViewModel(postRepository, listener, messageManager);
        PostsActivityModel model = viewModel.initAndResume();

        Assert.assertTrue(model.loaded.get());
        Assert.assertFalse(model.loading.get());
        Assert.assertEquals(model.getPosts(), posts);
        verify(messageManager, never()).showMessage(any(), anyInt());
    }

    @Test public void testLoadingPostsError(){
        PostRepository postRepository = mock(PostRepository.class);
        when(postRepository.getPosts()).thenReturn(Observable.error(new Exception()));

        MessageManager messageManager = mock(MessageManager.class);
        PostsActivityViewModel.PostsActivityViewModelListener listener = mock(PostsActivityViewModel.PostsActivityViewModelListener.class);

        PostsActivityViewModel viewModel = new PostsActivityViewModel(postRepository, listener, messageManager);
        PostsActivityModel model = viewModel.initAndResume();

        Assert.assertFalse(model.loaded.get());
        Assert.assertEquals(model.getPosts().size(), 0);
        verify(messageManager).showMessage(any(), anyInt());
    }

}
