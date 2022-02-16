package com.grocery.ui.dashboard.tab_fragments.products_and_services;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.grocery.BaseActivity;
import com.grocery.R;
import com.grocery.utils.Utils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class EditProductsAndServicesActivity extends BaseActivity {

    @BindView(R.id.imageViewUploadProductPicture)
    ImageView imageViewUploadProductPicture;
    @BindView(R.id.textViewRemovePicture)
    TextView textViewRemovePicture;
    @BindView(R.id.editTextProductName)
    EditText editTextProductName;
    @BindView(R.id.editTextProductNameDetails)
    EditText editTextProductNameDetails;
    @BindView(R.id.mTextViewErrorProductName)
    TextView mTextViewErrorProductName;
    @BindView(R.id.mTextViewErrorProductDetails)
    TextView mTextViewErrorProductDetails;
    @BindView(R.id.layoutEdit)
    LinearLayout layoutAdd;
    @BindView(R.id.textViewSelectImage)
    TextView textViewSelectImage;
    @BindView(R.id.progressBar)
    FrameLayout progressBar;

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    //image pick
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;
    //permission array
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
    String productId, productName, productImage, productDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_products_and_services);
        productId = getIntent().getStringExtra("productId");
        productName = getIntent().getStringExtra("productName");
        productDetails = getIntent().getStringExtra("productDetails");
        productImage = getIntent().getStringExtra("productImage");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        timeStamp = "" + System.currentTimeMillis();
        cameraPermissions = new String[]{Manifest.permission.CAMERA};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        setInfo();
    }

    private void setInfo() {
        try {
            editTextProductName.setText(productName);
            editTextProductNameDetails.setText(productDetails);
            try {
                Picasso.get().load(productImage).into(imageViewUploadProductPicture);
                textViewSelectImage.setVisibility(GONE);
            } catch (Exception e) {
                textViewSelectImage.setVisibility(View.VISIBLE);
            }
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.buttonBackArrow)
    void backButtonClick() {
        try {
            finish();
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }


    @OnClick(R.id.imageViewUploadProductPicture)
    void onImageViewUploadProductPictureClick() {
        try {
            showImagePickerDialog();

        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.textViewRemovePicture)
    void onTextViewRemovePictureClick() {
        try {
            imageViewUploadProductPicture.setImageDrawable(null);
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
    }

    @OnClick(R.id.layoutEdit)
    void onLayoutAddClick() {
        try {
            if (validate(editTextProductName.getText().toString(), editTextProductNameDetails.getText().toString())) {
                showProgress();
                Utils.hideSoftKeyboard(this);
                if (imageUri == null) {
                    //upload without image
                    uploadDataWithoutImage();
                } else {
                    //with Image
                    //store image to firebase storage with name and path
                    filePathAndName = "Products/" + "" + timeStamp;
                    filepath = FirebaseStorage.getInstance().getReference(filePathAndName).child(imageUri.getEncodedPath());
                    getPathFromFirebaseStorage();
                }
            }
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

    private void getPathFromFirebaseStorage() {
        try {
            filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful()) ;
                    downloadImageUri = uriTask.getResult();
                    if (uriTask.isSuccessful()) {
                        uploadDataWithImage();
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
            Log.e("Error Add item==> ", "" + exception);
        }
    }

    private void uploadDataWithImage() {
        try {
            HashMap<String, Object> addFieldInfo = new HashMap<>();
            addFieldInfo.put("productId", "" + productId);
            addFieldInfo.put("productImage", "" + downloadImageUri);
            addFieldInfo.put("productName", "" + editTextProductName.getText().toString());
            addFieldInfo.put("productDetails", "" + editTextProductNameDetails.getText().toString());
            addFieldInfo.put("userId", "" + userId);
            DocumentReference databaseReference = firebaseFirestore.collection("Products").document(productId);
            databaseReference.update(addFieldInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(@NonNull Void unused) {
                    hideProgress();
                    Toast.makeText(getApplicationContext(), "Edited", Toast.LENGTH_SHORT).show();
                    if (productImage != downloadImageUri.toString()) {
                        deleteFirebaseStorageImage();
                    }
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideProgress();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception exception) {
            Log.e("Error Add item==> ", "" + exception);
        }
    }

    private void uploadDataWithoutImage() {
        try {
            if (imageViewUploadProductPicture.getDrawable() == null) {
                deleteFirebaseStorageImage();
                productImage = "";
            }
            HashMap<String, Object> addFieldInfo = new HashMap<>();
            addFieldInfo.put("productId", "" + productId);
            addFieldInfo.put("productImage", "" + productImage);//No image
            addFieldInfo.put("productName", "" + editTextProductName.getText().toString());
            addFieldInfo.put("productDetails", "" + editTextProductNameDetails.getText().toString());
            addFieldInfo.put("userId", "" + userId);
            DocumentReference databaseReference = firebaseFirestore.collection("Products").document(productId);
            databaseReference.update(addFieldInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(@NonNull Void unused) {
                    hideProgress();
                    Toast.makeText(getApplicationContext(), "Edited", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideProgress();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception exception) {
            Log.e("Error Add item==> ", "" + exception);
        }
    }

    void deleteFirebaseStorageImage() {
        try {
            FirebaseStorage mFirebaseStorage = FirebaseStorage.getInstance();
            StorageReference photoRef = mFirebaseStorage.getReferenceFromUrl(productImage);
            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Photo deleted", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception exception) {
            Log.e("Error ==> ", "" + exception);
        }
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
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return result;
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
            imageViewUploadProductPicture.setImageURI(imageUri);
            if (imageUri != null) {
                textViewSelectImage.setVisibility(GONE);
            }
        } else if (requestCode == IMAGE_PICK_CAMERA_CODE && resultCode == RESULT_OK) {
            imageViewUploadProductPicture.setImageURI(imageUri);
            if (imageUri != null) {
                textViewSelectImage.setVisibility(GONE);
            }
        }
    }

    private boolean validate(String productName, String productDetails) {
        boolean valid = true;
        try {
            if (productName.isEmpty()) {
                editTextProductName.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewErrorProductName.setVisibility(View.VISIBLE);
                mTextViewErrorProductName.setText(getResources().getString(R.string.error_product_name));
                valid = false;
            } else {
                editTextProductName.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                mTextViewErrorProductName.setVisibility(GONE);
            }
            if (productDetails.isEmpty()) {
                editTextProductNameDetails.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_error));
                mTextViewErrorProductDetails.setVisibility(View.VISIBLE);
                mTextViewErrorProductDetails.setText(getResources().getString(R.string.error_product_details));
                valid = false;
            } else {
                editTextProductNameDetails.setBackground(getResources().getDrawable(R.drawable.background_rounded_edit_text_gray));
                mTextViewErrorProductDetails.setVisibility(GONE);
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