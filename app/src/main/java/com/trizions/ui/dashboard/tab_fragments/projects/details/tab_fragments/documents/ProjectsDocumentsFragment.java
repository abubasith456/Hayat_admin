package com.trizions.ui.dashboard.tab_fragments.projects.details.tab_fragments.documents;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.impulsiveweb.galleryview.GalleryView;
import com.trizions.BaseFragment;
import com.trizions.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class ProjectsDocumentsFragment extends BaseFragment {

    @BindView(R.id.textViewAddImage)
    TextView textViewAddImage;

    @BindView(R.id.textViewAddVideo)
    TextView textViewAddVideo;

    @BindView(R.id.textViewShowGallery)
    TextView textViewShowGallery;


    public static int PICK_IMAGE = 100;
    public static int PICK_VIDEO = 101;
    public ArrayList<String> paths = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    protected View createView(final LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_projects_documents, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @OnClick(R.id.textViewAddImage)
    public void addImage() {
        try {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_IMAGE);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.textViewAddVideo)
    public void addVideo() {
        try {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, PICK_VIDEO);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.textViewShowGallery)
    public void onShowGallery() {
        showGallery();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            if (data != null) {
                Uri contentUri = data.getData();
                String selectedImagePath = getImagePath(contentUri);
                Log.d("path ==>", selectedImagePath);
                paths.add(selectedImagePath);
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_VIDEO) {
            if (data != null) {
                Uri contentUri = data.getData();
                String selectedVideoPAth = getVideoPath(contentUri);
                Log.d("path ==>", selectedVideoPAth);
                paths.add(selectedVideoPAth);
            }
        }
    }

    public String getImagePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public String getVideoPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public void showGallery() {
        GalleryView.show(getActivity(), paths);
    }
}
