package com.example.gainyourmuscle;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    //FIREBASE
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //storage
    StorageReference storageReference;
    //path where images of user profile and cover will be stored
    String storagePath= "Users_Profile_Imgs/";

    //VIEWS FROM PROFILE FRAGMENT
    ImageView avatarTv;
    TextView nameTv, emailTv, genderTv, dobTv, phoneTv;
    FloatingActionButton fab;

    //progress dialog
    ProgressDialog pd;

    //permission constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;

    //arrays of permission to be requested;
    String cameraPermissions[];
    String storagePermissions[];

    //uri of picked image;
    Uri image_uri;

    //checking for profile
    String profilePhoto;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        storageReference = getInstance().getReference(); //firebase storage reference

        //init arrays of permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //init views
        avatarTv = view.findViewById(R.id.avatarTv);
        nameTv = view.findViewById(R.id.nameTv);
        emailTv = view.findViewById(R.id.emailTv);
        genderTv = view.findViewById(R.id.genderTv);
        dobTv = view.findViewById(R.id.dobTv);
        phoneTv = view.findViewById(R.id.phoneTv);
        fab = view.findViewById(R.id.fab);

        //init progress dialog
        pd = new ProgressDialog(getActivity());

        //Getting information of current logged in user
        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //checks until required data get
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get data
                    String name = "" + ds.child("name").getValue();
                    String email = "" + ds.child("email").getValue();
                    String gender = "" + ds.child("gender").getValue();
                    String dob = "" + ds.child("dateOfBirth").getValue();
                    String phone = "" + ds.child("phone").getValue();
                    String image = "" + ds.child("image").getValue();

                    //set DATA
                    nameTv.setText(name);
                    emailTv.setText(email);
                    genderTv.setText(gender);
                    dobTv.setText(dob);
                    phoneTv.setText(phone);
                    try {
                        //image successfully upload
                        Picasso.get().load(image).into(avatarTv);

                    } catch (Exception e) {
                        //default uploaded
                        Picasso.get().load(R.drawable.ic_add_a_photo_black_24dp).into(avatarTv);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //fab button click
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });


        return view;
    }

    private boolean checkStoragePermission() {
        //check if storage permission is enabled or not
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        //request runtime storage permissions
        requestPermissions(storagePermissions, STORAGE_REQUEST_CODE);

    }

    private boolean checkCameraPermission() {
        //check if storage permission is enabled or not
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        //request runtime storage permissions
        requestPermissions(cameraPermissions, CAMERA_REQUEST_CODE);

    }

    private void showEditProfileDialog() {
        /*Show Dialog containing options
        1.dateOfBirth Edit
        2.email Edit
        3.gender Edit
        4.image Edit
        5.name Edit
        6.phone Edit
         */

        //options to show in dialog
        String options[] = {"Edit Your Profile Picture", "Edit Your Name", "Edit Gender", "Edit Your Date-Of-Birth", "Edit Your Phone Number"};
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //set Title
        builder.setTitle("EDIT YOUR PROFILE");
        //set itmes to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog item clicks

                if (which == 0) {
                    //Edit PP
                    pd.setMessage("Edit Your Profile Picture");
                    profilePhoto = "image";
                    showImagePicDialog();

                } else if (which == 1) {
                    //Edit Name
                    pd.setMessage("Edit Your Name");
                    showDetailUpdateDialog("name");

                } else if (which == 2) {
                    //Edit Gender
                    pd.setMessage("Edit Your Gender");
                    showDetailUpdateDialog("gender");

                } else if (which == 3) {
                    //Edit DOB
                    pd.setMessage("Edit Your Date of Birth");
                    showDetailUpdateDialog("dateOfBirth");

                } else if (which == 4) {
                    //Edit Phone NUmber
                    pd.setMessage("Edit Your Phone Number");
                    showDetailUpdateDialog("phone");

                }

            }
        });

        //Create and show dialog
        builder.create().show();
    }

    private void showDetailUpdateDialog(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update "+key);
        //set layout of dailog
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);

        //add edit text
        final EditText editText = new EditText(getActivity());
        editText.setHint("Enter "+key);
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        //add button in dialog to update
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = editText.getText().toString().trim();

                if (!TextUtils.isEmpty(value)){
                    pd.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key,value);

                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                }
                else {
                    Toast.makeText(getActivity(), "Please Enter"+key, Toast.LENGTH_SHORT).show();
                }

            }
        });
        //add button in dialog to cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        //create and show dialog
        builder.create().show();
    }


    private void showImagePicDialog() {
        //show dialog containing options Camera and Gallery to pick image

        String options[] = {"Camera", "Gallery"};
        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //set title
        builder.setTitle("Choose photo ::");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //handle dialog item click
                if (which == 0) {
                    //Camera clicked
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }

                } else if (which == 1) {
                    //Gallery clicked
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }

                }

            }
        });
        //create and show dialog
        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                //picking form camera, first check if camera and storage permission allowed or not
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        //permission enabled
                        pickFromCamera();
                    } else {
                        //permission denied
                        Toast.makeText(getActivity(), "CAMERA AND STORAGE DISBALE.PLEASE ENABLE IT", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
            case STORAGE_REQUEST_CODE: {

                //picking form gallery, first check if gallery permission allowed or not
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        //permission enabled
                        pickFromGallery();
                    } else {
                        //permission denied
                        Toast.makeText(getActivity(), "STORAGE PERMISSION IS DISABLE. PLEASE ENABLE IT", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //This method is called after picking image from camera or gallery
        if (resultCode == RESULT_OK){

            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                //image is picked form galley. get uri of image
                image_uri = data.getData();
                uploadProfilePhoto(image_uri);

            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE){
                //image is picked form camera. get uri of image
                uploadProfilePhoto(image_uri);


            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfilePhoto(Uri uri) {
        pd.show();

        String filePathAndName =  storagePath+ ""+ profilePhoto +" " +user.getUid();

        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //image is uploaded to storage it's uri and store in user's database
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadUri = uriTask.getResult();

                //check if image is uploaded or not and uri is received
                if(uriTask.isSuccessful()){
                    //image uploaded
                    //add/update uri in user's database
                    HashMap<String, Object> results = new HashMap<>();

                    results.put(profilePhoto, downloadUri.toString());

                    databaseReference.child(user.getUid()).updateChildren(results)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //uri in database of user is added successfully
                                    //dismiss progress bar
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Image Uploaded. Thank you", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //uri in database of user is added successfully
                                    //dismiss progress bar
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Error Occur in Image Uploading. Sorry for error", Toast.LENGTH_SHORT).show();

                                }
                            });
                }
                else {
                    pd.dismiss();
                    Toast.makeText(getActivity(), "Error have been occur. Please Mail us for consult.", Toast.LENGTH_SHORT).show();

                }

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void pickFromCamera() {
//Intent of picking image from device camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        //put image uri
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        //intent to start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

    }

    private void pickFromGallery() {
        //pick from galley
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }


}
