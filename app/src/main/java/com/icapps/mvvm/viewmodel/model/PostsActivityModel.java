package com.icapps.mvvm.viewmodel.model;

import android.databinding.ObservableBoolean;
import android.os.Parcel;
import android.os.Parcelable;

import com.icapps.mvvm.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maartenvangiel on 21/09/16.
 */
public class PostsActivityModel implements Parcelable {
    public List<Post> posts = new ArrayList<>();
    public final ObservableBoolean loading = new ObservableBoolean(false);
    public final ObservableBoolean loaded = new ObservableBoolean(false);

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.posts);
        dest.writeByte(this.loading.get() ? (byte) 1 : (byte) 0);
        dest.writeByte(this.loaded.get() ? (byte) 1 : (byte) 0);
    }

    public PostsActivityModel() {

    }

    protected PostsActivityModel(Parcel in) {
        this.posts = in.createTypedArrayList(Post.CREATOR);
        this.loading.set(in.readByte() == 1);
        this.loaded.set(in.readByte() == 1);
    }

    public static final Creator<PostsActivityModel> CREATOR = new Creator<PostsActivityModel>() {
        @Override
        public PostsActivityModel createFromParcel(Parcel source) {
            return new PostsActivityModel(source);
        }

        @Override
        public PostsActivityModel[] newArray(int size) {
            return new PostsActivityModel[size];
        }
    };
}
