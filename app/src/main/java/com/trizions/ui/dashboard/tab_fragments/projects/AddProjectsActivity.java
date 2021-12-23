package com.trizions.ui.dashboard.tab_fragments.projects;

import static android.view.View.GONE;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.trizions.BaseActivity;
import com.trizions.R;
import com.trizions.utils.Utils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class AddProjectsActivity extends BaseActivity {

    @BindView(R.id.imageViewUploadProjectImage)
    ImageView imageViewUploadProjectImage;
    @BindView(R.id.textViewSelectImage)
    TextView textViewSelectImage;
    @BindView(R.id.editTextAddProjectName)
    EditText editTextAddProjectName;
    @BindView(R.id.editTextAddProjectStatus)
    EditText editTextAddProjectStatus;
    @BindView(R.id.editTextAddProjectDetails)
    EditText editTextAddProjectDetails;
    @BindView(R.id.editTextAddProjectAddress)
    EditText editTextAddProjectAddress;
    @BindView(R.id.mTextViewErrorProjectName)
    TextView mTextViewErrorProjectName;
    @BindView(R.id.mTextViewErrorProjectStatus)
    TextView mTextViewErrorProjectStatus;
    @BindView(R.id.mTextViewErrorProjectDetails)
    TextView mTextViewErrorProjectDetails;
    @BindView(R.id.mTextViewErrorProjectAddress)
    TextView mTextViewErrorProjectAddress;
    @BindView(R.id.layoutNext)
    LinearLayout layoutNext;
    @BindView(R.id.progressBar)
    FrameLayout progressBar;


    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    private String[] cameraPermissions;
    private String[] storagePermissions;
    private Uri imageUri;
    String filePathAndName;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    String userId;
    private StorageReference filepath;
    Uri downloadImageUri;
    String timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_projects);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        timeStamp = "" + System.currentTimeMillis();
        cameraPermissions = new String[]{Manifest.permission.CAMERA};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    @OnClick(R.id.imageViewUploadProjectImage)
    void onImageViewUploadProjectImage() {
        try {
            showImagePickerDialog();
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.layoutNext)
    void onLayoutNextClick() {
        try {
            if (validate(editTextAddProjectName.getText().toString(), editTextAddProjectStatus.getText().toString(),
                    editTextAddProjectDetails.getText().toString(), editTextAddProjectAddress.getText().toString())) {
                showProgress();
                Utils.hideSoftKeyboard(this);
                if (imageUri == null) {
                    //upload without image
//                    uploadDataWithoutImage();
                    downloadImageUri=null;
                } else {
                    //with Image
                    //store image to firebase storage with name and path
                    filePathAndName = "Projects/" + "" + timeStamp;
                    filepath = FirebaseStorage.getInstance().getReference(filePathAndName).child(imageUri.getEncodedPath());
                    getPathFromFirebaseStorage();
                }
                Intent clientInfoIntent=new Intent(getApplicationContext(),AddProjectClientInfoActivity.class);
                clientInfoIntent.putExtra("projectImage",downloadImageUri);
                clientInfoIntent.putExtra("projectName",editTextAddProjectName.getText().toString());
                clientInfoIntent.putExtra("projectStatus",editTextAddProjectStatus.getText().toString());
                clientInfoIntent.putExtra("projectDetails",editTextAddProjectDetails.getText().toString());
                clientInfoIntent.putExtra("projectAddress",editTextAddProjectAddress.getText().toString());
                startActivity(clientInfoIntent);
            }

        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    private void getPathFromFirebaseStorage() {
        try {
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    downloadImageUri = uriTask.getResult();
                    if (uriTask.isSuccessful()) {
                        //url uri received,upload to DB
//                        uploadDataWithImage();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideProgress();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    private void showImagePickerDialog() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (checkCameraPermission()) {
                        pickFromCamera();
                    } else {
                        requestCameraPermission();
                    }
                } else {
                    if (checkStoragePermission()) {
                        pickFromGallery();
                    } else {
                        requestStoragePermission();
                    }
                }
            }
        }).show();
    }

    private void pickFromCamera() {
        //using media to pic high quality image
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_image_title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_image_description");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent pickCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        pickCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(pickCameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void pickFromGallery() {
        Intent picGalleryIntent = new Intent(Intent.ACTION_PICK);
        picGalleryIntent.setType("image/*");
        startActivityForResult(picGalleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private boolean checkCameraPermission() {
        boolean resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);
        return resultCamera;
    }

    private boolean checkStoragePermission() {
        boolean resultStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return resultStorage;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(getApplicationContext(), "Camera permission required..", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(getApplicationContext(), "Storage permission required..", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_GALLERY_CODE && resultCode == RESULT_OK) {
            imageUri = data.getData();
            imageViewUploadProjectImage.setImageURI(imageUri);
            if (imageUri != null) {
                textViewSelectImage.setVisibility(GONE);
            }
        } else if (requestCode == IMAGE_PICK_CAMERA_CODE && resultCode == RESULT_OK) {
            imageViewUploadProjectImage.setImageURI(imageUri);
            if (imageUri != null) {
                textViewSelectImage.setVisibility(GONE);
            }
        }
    }

    private boolean validate(String name, String status, String details, String address) {
        boolean valid = true;
        try {
            if (name.isEmpty()) {
                editTextAddProjectName.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewErrorProjectName.setVisibility(View.VISIBLE);
                mTextViewErrorProjectName.setText(getResources().getString(R.string.error_client_name));
                valid = false;
            } else {
                editTextAddProjectName.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                mTextViewErrorProjectName.setVisibility(GONE);
            }
            if (status.isEmpty()) {
                editTextAddProjectStatus.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewErrorProjectStatus.setVisibility(View.VISIBLE);
                mTextViewErrorProjectStatus.setText(getResources().getString(R.string.error_client_role));
                valid = false;
            } else {
                editTextAddProjectStatus.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                mTextViewErrorProjectStatus.setVisibility(GONE);
            }
            if (details.isEmpty()) {
                editTextAddProjectDetails.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewErrorProjectDetails.setVisibility(View.VISIBLE);
                mTextViewErrorProjectDetails.setText(getResources().getString(R.string.error_client_bussiness));
                valid = false;
            } else {
                editTextAddProjectDetails.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                mTextViewErrorProjectDetails.setVisibility(GONE);
            }
            if (address.isEmpty()) {
                editTextAddProjectAddress.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewErrorProjectAddress.setVisibility(View.VISIBLE);
                mTextViewErrorProjectAddress.setText(getResources().getString(R.string.error_client_mobile));
                valid = false;
            } else {
                editTextAddProjectAddress.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                mTextViewErrorProjectAddress.setVisibility(GONE);
            }

        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
        return valid;
    }

    public void showProgress() {
        try {
            progressBar.setVisibility(View.VISIBLE);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    public void hideProgress() {
        try {
            progressBar.setVisibility(View.GONE);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

}