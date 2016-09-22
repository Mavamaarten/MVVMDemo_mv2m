package com.icapps.mvvm.viewmodel.model;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Parcel;
import android.os.Parcelable;

import com.icapps.mvvm.model.Comment;
import com.icapps.mvvm.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maartenvangiel on 21/09/16.
 */

public class CommentsModel implements Parcelable {
    public final ObservableField<Post> post = new ObservableField<>();
    public final ObservableBoolean loading = new ObservableBoolean(false);
    public final ObservableBoolean loaded = new ObservableBoolean(false);
    public final ObservableInt commentCount = new ObservableInt(0);
    public List<Comment> comments = new ArrayList<>();

    public CommentsModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.post.get(), flags);
        dest.writeTypedList(this.comments);
        dest.writeByte(loading.get() ? (byte) 1 : (byte) 0);
        dest.writeByte(loaded.get() ? (byte) 1 : (byte) 0);
        dest.writeInt(commentCount.get());
    }

    protected CommentsModel(Parcel in) {
        this.post.set(in.readParcelable(Post.class.getClassLoader()));
        this.comments = in.createTypedArrayList(Comment.CREATOR);
        this.loading.set(in.readByte() == 1);
        this.loaded.set(in.readByte() == 1);
        this.commentCount.set(in.readInt());
    }

    public static final Creator<CommentsModel> CREATOR = new Creator<CommentsModel>() {
        @Override
        public CommentsModel createFromParcel(Parcel source) {
            return new CommentsModel(source);
        }

        @Override
        public CommentsModel[] newArray(int size) {
            return new CommentsModel[size];
        }
    };
}
