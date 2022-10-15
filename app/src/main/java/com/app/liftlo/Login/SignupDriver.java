package com.app.liftlo.Login;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.liftlo.R;
import com.app.liftlo.utils.Check_internet_connection;
import com.app.liftlo.utils.FileUtils;
import com.app.liftlo.utils.JsonParser;
import com.app.liftlo.utils.ResponseData;
import com.app.liftlo.utils.ServerURL;
import com.app.liftlo.utils.UploadApi;
import com.github.thunder413.datetimeutils.DateTimeUnits;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import id.zelory.compressor.Compressor;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SignupDriver extends AppCompatActivity {


    EditText name, email, password, city,
            code1, code2, code3, code4, fname, car_color, car_model, car_number, address, nic, license_no;
    int[] code = new int[4];
    Random rand = new Random();
    String sms_code, code1_, code2_, code3_, code4_;
    TextView dob, gender;
    Button button, choose_image;
    String Sname, Semail, Snumber, Spassword, Sdob, Scity, Sgender, Sfname, Scar_color, Scar_model, Scar_number, Saddress, Snic, Slicense_no;
    RotateLoading whorlView;
    RelativeLayout relativeLayout;
    String server_response, cal_age;
    int daysBetween, index_;
    ArrayList<String> Array_gender = new ArrayList<>();
    ArrayList<String> Array_cities;
    SpinnerDialog spinnerDialog, spinnerDialog2;
    String date;
    String phonenumber;
    TextView number;
    ImageView profile_image, car_doc1, car_doc2, licenseImg, nicImg1, nicImg2;
    private static final int GALLERY1 = 300;
    private static final int MY_PERMISSIONS_REQUEST_IMAGE1 = 0x1;
    private static final int REQUEST_TAKE_PHOTO1 = 100;

    private static final int GALLERY2 = 301;
    private static final int MY_PERMISSIONS_REQUEST_IMAGE2 = 0x2;
    private static final int REQUEST_TAKE_PHOTO2 = 102;

    private static final int GALLERY3 = 302;
    private static final int MY_PERMISSIONS_REQUEST_IMAGE3 = 0x3;
    private static final int REQUEST_TAKE_PHOTO3 = 104;

    private static final int GALLERY4 = 304;
    private static final int MY_PERMISSIONS_REQUEST_IMAGE4 = 0x4;
    private static final int REQUEST_TAKE_PHOTO4 = 106;

    private static final int GALLERY5 = 305;
    private static final int MY_PERMISSIONS_REQUEST_IMAGE5 = 0x5;
    private static final int REQUEST_TAKE_PHOTO5 = 108;


    private static final int GALLERY6 = 306;
    private static final int MY_PERMISSIONS_REQUEST_IMAGE6 = 0x6;
    private static final int REQUEST_TAKE_PHOTO6 = 110;


    File camerafile1, camerafile2, camerafile3, camerafile4, camerafile5, camerafile6;
    File galleryfile1, galleryfile2, galleryfile3, galleryfile4, galleryfile5, galleryfile6;
    Uri file_uri;
    String mCurrentPhotoPath;
    File compressedfile1, compressedfile2, compressedfile3, compressedfile4, compressedfile5, compressedfile6;
    List<MultipartBody.Part> fileParts = new ArrayList<>();
    MultipartBody.Part part1, part2, part3, part4, part5, part6;
    MultipartBody.Part gallerypart1, gallerypart2, gallerypart3, gallerypart4, gallerypart5, gallerypart6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_driver);

        phonenumber = getIntent().getStringExtra("number");


        Gender();
        init();


    }


    public void init() {

        whorlView = findViewById(R.id.rotateloading);
        relativeLayout = findViewById(R.id.r);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        number = findViewById(R.id.number);
        password = findViewById(R.id.password);
        dob = findViewById(R.id.dob);
        city = findViewById(R.id.city);
        gender = findViewById(R.id.gender);
        button = findViewById(R.id.add);
        fname = findViewById(R.id.fname);
        car_color = findViewById(R.id.car_color);
        car_model = findViewById(R.id.car_model);
        car_number = findViewById(R.id.car_number);
        address = findViewById(R.id.address);
        nic = findViewById(R.id.nic);
        license_no = findViewById(R.id.license);


        profile_image = findViewById(R.id.profile_pic);
        car_doc1 = findViewById(R.id.car_pic1);
        car_doc2 = findViewById(R.id.car_pic2);
        licenseImg = findViewById(R.id.license1);
        nicImg1 = findViewById(R.id.card1);
        nicImg2 = findViewById(R.id.card2);

        number.setText(phonenumber);


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(SignupDriver.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(SignupDriver.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(SignupDriver.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog1();

                } else {
                    ActivityCompat.requestPermissions(SignupDriver.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_IMAGE1);
                }
            }
        });


        car_doc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(SignupDriver.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(SignupDriver.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(SignupDriver.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog2();

                } else {
                    ActivityCompat.requestPermissions(SignupDriver.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_IMAGE2);
                }
            }
        });


        car_doc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(SignupDriver.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(SignupDriver.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(SignupDriver.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog3();

                } else {
                    ActivityCompat.requestPermissions(SignupDriver.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_IMAGE3);
                }
            }
        });


        nicImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(SignupDriver.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(SignupDriver.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(SignupDriver.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog4();

                } else {
                    ActivityCompat.requestPermissions(SignupDriver.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_IMAGE5);
                }
            }
        });


        nicImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(SignupDriver.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(SignupDriver.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(SignupDriver.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog5();

                } else {
                    ActivityCompat.requestPermissions(SignupDriver.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_IMAGE6);
                }
            }
        });


        licenseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(SignupDriver.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(SignupDriver.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(SignupDriver.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog6();

                } else {
                    ActivityCompat.requestPermissions(SignupDriver.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_IMAGE4);
                }
            }
        });


        spinnerDialog2 = new SpinnerDialog(SignupDriver.this, Array_gender, "Select Gender", R.style.DialogAnimations_SmileWindow);// With 	Animation

        spinnerDialog2.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                gender.setText(item);
            }
        });

        findViewById(R.id.gender).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog2.showSpinerDialog();
            }
        });


        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dobDialog();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Sname = name.getText().toString().trim();
                Semail = email.getText().toString().toLowerCase();
                Snumber = number.getText().toString().trim();
                Spassword = password.getText().toString().trim();
                Sdob = dob.getText().toString().trim();
                Scity = city.getText().toString().trim();
                Sgender = gender.getText().toString().trim();
                Sfname = fname.getText().toString().trim();
                Scar_color = car_color.getText().toString().trim();
                Scar_model = car_model.getText().toString().trim();
                Scar_number = car_number.getText().toString().trim();
                Saddress = address.getText().toString().trim();
                Snic = nic.getText().toString().trim();
                Slicense_no = license_no.getText().toString().trim();


                if (Sname.equals("")) {
                    name.setError(getApplicationContext().getResources().getString(R.string.enter_name));
                    name.requestFocus();
                } else if (Semail.equals("")) {
                    email.setError(getApplicationContext().getResources().getString(R.string.enter_email));
                    email.requestFocus();
                } else if (Snumber.equals("")) {
                    number.setError(getApplicationContext().getResources().getString(R.string.enter_number));
                    number.requestFocus();
                } else if (Sgender.equals("")) {
                    gender.setError(getApplicationContext().getResources().getString(R.string.enter_gender));
                    gender.requestFocus();
                } else if (Sdob.equals("")) {
                    dob.setError(getApplicationContext().getResources().getString(R.string.enter_dob));
                    dob.requestFocus();
                } else if (Scity.equals("")) {
                    city.setError(getApplicationContext().getResources().getString(R.string.enter_city));
                    city.requestFocus();
                } else if (Spassword.equals("")) {
                    password.setError(getApplicationContext().getResources().getString(R.string.enter_password));
                    password.requestFocus();
                } else if (Sfname.equals("")) {
                    fname.setError(getApplicationContext().getResources().getString(R.string.enter_your_father_name));
                    fname.requestFocus();
                } else if (Scar_color.equals("")) {
                    car_color.setError(getApplicationContext().getResources().getString(R.string.enter_car_color));
                    car_color.requestFocus();
                } else if (Scar_model.equals("")) {
                    car_model.setError(getApplicationContext().getResources().getString(R.string.enter_car_model));
                    car_model.requestFocus();
                } else if (Scar_number.equals("")) {
                    car_number.setError(getApplicationContext().getResources().getString(R.string.enter_car_number));
                    car_number.requestFocus();
                } else if (Saddress.equals("")) {
                    address.setError(getApplicationContext().getResources().getString(R.string.enter_address));
                    address.requestFocus();
                } else if (Snic.equals("")) {
                    nic.setError(getApplicationContext().getResources().getString(R.string.enter_nic));
                    nic.requestFocus();
                } else if (Slicense_no.equals("")) {
                    license_no.setError(getApplicationContext().getResources().getString(R.string.enter_license_no));
                    license_no.requestFocus();
                } else if (fileParts.size() < 1) {
                    Toast.makeText(SignupDriver.this, getApplicationContext().getResources().getString(R.string.choose_all_images), Toast.LENGTH_SHORT).show();
                } else {

                    if (new Check_internet_connection(getApplicationContext()).isNetworkAvailable()) {


                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        whorlView.start();
                        RegisterDriver();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                getApplicationContext().getResources().getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
                    }


                }


            }
        });


    }


    private void openImageDialog1() {
        final Dialog sheetDialog = new Dialog(SignupDriver.this, R.style.dialog_theme);
        sheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sheetDialog.setCancelable(true);
        sheetDialog.setContentView(R.layout.attachimage_dialog);
        sheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));
        ImageView take_photo = sheetDialog.findViewById(R.id.take_photo);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    camerafile1 = null;
                    try {
                        camerafile1 = createImageFile();
                        Log.e("TAG", "dispatchTakePictureIntent1: " + camerafile1);
                    } catch (IOException e) {
                        Log.e("TAG", "IOException: " + e);
                    }
                    if (camerafile1 != null) {
                        file_uri = FileProvider.getUriForFile(SignupDriver.this, "com.dzone.ridesharing.fileprovider", camerafile1);
                        Log.e("TAG", "shah uri: " + file_uri);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO1);

                    }
                }
                sheetDialog.dismiss();
            }
        });

        ImageView open_gallery = sheetDialog.findViewById(R.id.open_gallery);
        open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY1);
                sheetDialog.dismiss();
            }
        });
        sheetDialog.show();

    }


    private void openImageDialog2() {
        final Dialog sheetDialog = new Dialog(SignupDriver.this, R.style.dialog_theme);
        sheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sheetDialog.setCancelable(true);
        sheetDialog.setContentView(R.layout.attachimage_dialog);
        sheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));
        ImageView take_photo = sheetDialog.findViewById(R.id.take_photo);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    camerafile2 = null;
                    try {
                        camerafile2 = createImageFile();
                        Log.e("TAG", "dispatchTakePictureIntent1: " + camerafile2);
                    } catch (IOException e) {
                        Log.e("TAG", "IOException: " + e);
                    }
                    if (camerafile2 != null) {
                        file_uri = FileProvider.getUriForFile(SignupDriver.this, "com.app.liftlo.fileprovider", camerafile2);
                        Log.e("TAG", "shah uri: " + file_uri);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO2);

                    }
                }
                sheetDialog.dismiss();
            }
        });

        ImageView open_gallery = sheetDialog.findViewById(R.id.open_gallery);
        open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY2);
                sheetDialog.dismiss();
            }
        });
        sheetDialog.show();

    }


    private void openImageDialog3() {
        final Dialog sheetDialog = new Dialog(SignupDriver.this, R.style.dialog_theme);
        sheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sheetDialog.setCancelable(true);
        sheetDialog.setContentView(R.layout.attachimage_dialog);
        sheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));
        ImageView take_photo = sheetDialog.findViewById(R.id.take_photo);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    camerafile3 = null;
                    try {
                        camerafile3 = createImageFile();
                        Log.e("TAG", "dispatchTakePictureIntent1: " + camerafile3);
                    } catch (IOException e) {
                        Log.e("TAG", "IOException: " + e);
                    }
                    if (camerafile3 != null) {
                        file_uri = FileProvider.getUriForFile(SignupDriver.this, "com.dzone.ridesharing.fileprovider", camerafile3);
                        Log.e("TAG", "shah uri: " + file_uri);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO3);

                    }
                }
                sheetDialog.dismiss();
            }
        });

        ImageView open_gallery = sheetDialog.findViewById(R.id.open_gallery);
        open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY3);
                sheetDialog.dismiss();
            }
        });
        sheetDialog.show();

    }


    private void openImageDialog4() {
        final Dialog sheetDialog = new Dialog(SignupDriver.this, R.style.dialog_theme);
        sheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sheetDialog.setCancelable(true);
        sheetDialog.setContentView(R.layout.attachimage_dialog);
        sheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));
        ImageView take_photo = sheetDialog.findViewById(R.id.take_photo);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    camerafile4 = null;
                    try {
                        camerafile4 = createImageFile();
                        Log.e("TAG", "dispatchTakePictureIntent1: " + camerafile4);
                    } catch (IOException e) {
                        Log.e("TAG", "IOException: " + e);
                    }
                    if (camerafile4 != null) {
                        file_uri = FileProvider.getUriForFile(SignupDriver.this, "com.dzone.ridesharing.fileprovider", camerafile4);
                        Log.e("TAG", "shah uri: " + file_uri);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO4);

                    }
                }
                sheetDialog.dismiss();
            }
        });

        ImageView open_gallery = sheetDialog.findViewById(R.id.open_gallery);
        open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY4);
                sheetDialog.dismiss();
            }
        });
        sheetDialog.show();

    }


    private void openImageDialog5() {
        final Dialog sheetDialog = new Dialog(SignupDriver.this, R.style.dialog_theme);
        sheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sheetDialog.setCancelable(true);
        sheetDialog.setContentView(R.layout.attachimage_dialog);
        sheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));
        ImageView take_photo = sheetDialog.findViewById(R.id.take_photo);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    camerafile5 = null;
                    try {
                        camerafile5 = createImageFile();
                        Log.e("TAG", "dispatchTakePictureIntent1: " + camerafile5);
                    } catch (IOException e) {
                        Log.e("TAG", "IOException: " + e);
                    }
                    if (camerafile5 != null) {
                        file_uri = FileProvider.getUriForFile(SignupDriver.this, "com.dzone.ridesharing.fileprovider", camerafile5);
                        Log.e("TAG", "shah uri: " + file_uri);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO5);

                    }
                }
                sheetDialog.dismiss();
            }
        });

        ImageView open_gallery = sheetDialog.findViewById(R.id.open_gallery);
        open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY5);
                sheetDialog.dismiss();
            }
        });
        sheetDialog.show();

    }


    private void openImageDialog6() {
        final Dialog sheetDialog = new Dialog(SignupDriver.this, R.style.dialog_theme);
        sheetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sheetDialog.setCancelable(true);
        sheetDialog.setContentView(R.layout.attachimage_dialog);
        sheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x7f000000));
        ImageView take_photo = sheetDialog.findViewById(R.id.take_photo);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    camerafile6 = null;
                    try {
                        camerafile6 = createImageFile();
                        Log.e("TAG", "dispatchTakePictureIntent1: " + camerafile6);
                    } catch (IOException e) {
                        Log.e("TAG", "IOException: " + e);
                    }
                    if (camerafile6 != null) {
                        file_uri = FileProvider.getUriForFile(SignupDriver.this, "com.dzone.ridesharing.fileprovider", camerafile6);
                        Log.e("TAG", "shah uri: " + file_uri);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file_uri);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO6);

                    }
                }
                sheetDialog.dismiss();
            }
        });

        ImageView open_gallery = sheetDialog.findViewById(R.id.open_gallery);
        open_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY6);
                sheetDialog.dismiss();
            }
        });
        sheetDialog.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO1 && resultCode == RESULT_OK) {
            if (file_uri != null) {
                try {


                    //check for avoiding double image sending
                    if (gallerypart1 != null) {
                        index_ = fileParts.indexOf(gallerypart1);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        gallerypart1 = null;
                    }

                    //check if part is empty or not
                    if (part1 == null) {
                        Log.e("part1", "empty");
                    } else {
                        //get the index from arraylist where the part value is located
                        index_ = fileParts.indexOf(part1);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        part1 = null;
                    }


                    compressedfile1 = new Compressor(this)
                            .setQuality(50)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(camerafile1);
                    delete(camerafile1);
                    //image parameter name
                    part1 = prepareFilePart("profile_pic[]", compressedfile1);
                    fileParts.add(part1);

                    profile_image.setImageURI(FileUtils.getUri(compressedfile1));

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("TAG", "IOException: " + e.toString());
                }

            }
        } else if (requestCode == REQUEST_TAKE_PHOTO2 && resultCode == RESULT_OK) {
            if (file_uri != null) {
                try {


                    if (gallerypart2 != null) {
                        index_ = fileParts.indexOf(gallerypart2);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        gallerypart2 = null;
                    }


                    if (part2 == null) {
                        Log.e("part2", "empty");
                    } else {
                        index_ = fileParts.indexOf(part2);
                        fileParts.remove(index_);
                        part2 = null;
                    }


                    compressedfile2 = new Compressor(this)
                            .setQuality(50)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(camerafile2);
                    delete(camerafile2);
                    part2 = prepareFilePart("profile_pic[]", compressedfile2);
                    fileParts.add(part2);

                    car_doc1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    car_doc1.setImageURI(FileUtils.getUri(compressedfile2));

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("TAG", "IOException: " + e.toString());
                }

            }
        } else if (requestCode == REQUEST_TAKE_PHOTO3 && resultCode == RESULT_OK) {
            if (file_uri != null) {
                try {


                    if (gallerypart3 != null) {
                        index_ = fileParts.indexOf(gallerypart3);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        gallerypart3 = null;
                    }


                    if (part3 == null) {
                        Log.e("part3", "empty");
                    } else {
                        index_ = fileParts.indexOf(part3);
                        fileParts.remove(index_);
                        part3 = null;
                    }


                    compressedfile3 = new Compressor(this)
                            .setQuality(50)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(camerafile3);
                    delete(camerafile3);
                    part3 = prepareFilePart("profile_pic[]", compressedfile3);
                    fileParts.add(part3);

                    car_doc2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    car_doc2.setImageURI(FileUtils.getUri(compressedfile3));

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("TAG", "IOException: " + e.toString());
                }

            }
        } else if (requestCode == REQUEST_TAKE_PHOTO4 && resultCode == RESULT_OK) {
            if (file_uri != null) {
                try {


                    if (gallerypart4 != null) {
                        index_ = fileParts.indexOf(gallerypart4);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        gallerypart4 = null;
                    }


                    if (part4 == null) {
                        Log.e("part4", "empty");
                    } else {
                        index_ = fileParts.indexOf(part4);
                        fileParts.remove(index_);
                        part4 = null;
                    }


                    compressedfile4 = new Compressor(this)
                            .setQuality(50)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(camerafile4);
                    delete(camerafile4);
                    part4 = prepareFilePart("profile_pic[]", compressedfile4);
                    fileParts.add(part4);

                    licenseImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    licenseImg.setImageURI(FileUtils.getUri(compressedfile4));

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("TAG", "IOException: " + e.toString());
                }

            }
        } else if (requestCode == REQUEST_TAKE_PHOTO5 && resultCode == RESULT_OK) {
            if (file_uri != null) {
                try {


                    if (gallerypart5 != null) {
                        index_ = fileParts.indexOf(gallerypart5);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        gallerypart5 = null;
                    }


                    if (part5 == null) {
                        Log.e("part5", "empty");
                    } else {
                        index_ = fileParts.indexOf(part5);
                        fileParts.remove(index_);
                        part5 = null;
                    }


                    compressedfile5 = new Compressor(this)
                            .setQuality(50)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(camerafile5);
                    delete(camerafile5);
                    part5 = prepareFilePart("profile_pic[]", compressedfile5);
                    fileParts.add(part5);

                    nicImg1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    nicImg1.setImageURI(FileUtils.getUri(compressedfile5));

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("TAG", "IOException: " + e.toString());
                }

            }
        } else if (requestCode == REQUEST_TAKE_PHOTO6 && resultCode == RESULT_OK) {
            if (file_uri != null) {
                try {


                    if (gallerypart6 != null) {
                        index_ = fileParts.indexOf(gallerypart6);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        gallerypart6 = null;
                    }


                    if (part6 == null) {
                        Log.e("part6", "empty");
                    } else {
                        index_ = fileParts.indexOf(part6);
                        fileParts.remove(index_);
                        part6 = null;
                    }


                    compressedfile6 = new Compressor(this)
                            .setQuality(50)
                            .setCompressFormat(Bitmap.CompressFormat.JPEG)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(camerafile6);
                    delete(camerafile6);
                    part6 = prepareFilePart("profile_pic[]", compressedfile6);
                    fileParts.add(part6);

                    nicImg1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    nicImg1.setImageURI(FileUtils.getUri(compressedfile6));

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("TAG", "IOException: " + e.toString());
                }

            }
        } else if (requestCode == GALLERY1 && resultCode == RESULT_OK) {
            if (data != null) {

                file_uri = data.getData();
                Log.e("imageUri", file_uri.toString());
                try {


                    if (part1 != null) {
                        index_ = fileParts.indexOf(part1);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        part1 = null;
                    }


                    if (gallerypart1 == null) {
                        Log.e("part1", "empty");
                    } else {
                        index_ = fileParts.indexOf(gallerypart1);
                        fileParts.remove(index_);
                        gallerypart1 = null;
                    }


                    galleryfile1 = new Compressor(this).compressToFile(FileUtils.getFile(this, file_uri));
                    gallerypart1 = prepareFilePart("profile_pic[]", galleryfile1);
                    fileParts.add(gallerypart1);

                    profile_image.setImageURI(FileUtils.getUri(galleryfile1));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == GALLERY2 && resultCode == RESULT_OK) {
            if (data != null) {

                file_uri = data.getData();
                Log.e("imageUri", file_uri.toString());
                try {


                    if (part2 != null) {
                        index_ = fileParts.indexOf(part2);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        part2 = null;
                    }


                    if (gallerypart2 == null) {
                        Log.e("part2", "empty");
                    } else {
                        index_ = fileParts.indexOf(gallerypart2);
                        fileParts.remove(index_);
                        gallerypart2 = null;
                    }


                    galleryfile2 = new Compressor(this).compressToFile(FileUtils.getFile(this, file_uri));
                    gallerypart2 = prepareFilePart("profile_pic[]", galleryfile2);
                    fileParts.add(gallerypart2);

                    car_doc1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    car_doc1.setImageURI(FileUtils.getUri(galleryfile2));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == GALLERY3 && resultCode == RESULT_OK) {
            if (data != null) {

                file_uri = data.getData();
                Log.e("imageUri", file_uri.toString());
                try {


                    if (part3 != null) {
                        index_ = fileParts.indexOf(part3);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        part3 = null;
                    }


                    if (gallerypart3 == null) {
                        Log.e("part3", "empty");
                    } else {
                        index_ = fileParts.indexOf(gallerypart3);
                        fileParts.remove(index_);
                        gallerypart3 = null;
                    }


                    galleryfile3 = new Compressor(this).compressToFile(FileUtils.getFile(this, file_uri));
                    gallerypart3 = prepareFilePart("profile_pic[]", galleryfile3);
                    fileParts.add(gallerypart3);

                    car_doc2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    car_doc2.setImageURI(FileUtils.getUri(galleryfile3));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == GALLERY4 && resultCode == RESULT_OK) {
            if (data != null) {

                file_uri = data.getData();
                Log.e("imageUri", file_uri.toString());
                try {


                    if (part4 != null) {
                        index_ = fileParts.indexOf(part4);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        part4 = null;
                    }


                    if (gallerypart4 == null) {
                        Log.e("part4", "empty");
                    } else {
                        index_ = fileParts.indexOf(gallerypart4);
                        fileParts.remove(index_);
                        gallerypart4 = null;
                    }


                    galleryfile4 = new Compressor(this).compressToFile(FileUtils.getFile(this, file_uri));
                    gallerypart4 = prepareFilePart("profile_pic[]", galleryfile4);
                    fileParts.add(gallerypart4);

                    licenseImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    licenseImg.setImageURI(FileUtils.getUri(galleryfile4));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == GALLERY5 && resultCode == RESULT_OK) {
            if (data != null) {

                file_uri = data.getData();
                Log.e("imageUri", file_uri.toString());
                try {


                    if (part5 != null) {
                        index_ = fileParts.indexOf(part5);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        part5 = null;
                    }


                    if (gallerypart5 == null) {
                        Log.e("part5", "empty");
                    } else {
                        index_ = fileParts.indexOf(gallerypart5);
                        fileParts.remove(index_);
                        gallerypart5 = null;
                    }


                    galleryfile5 = new Compressor(this).compressToFile(FileUtils.getFile(this, file_uri));
                    gallerypart5 = prepareFilePart("profile_pic[]", galleryfile5);
                    fileParts.add(gallerypart5);

                    nicImg1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    nicImg1.setImageURI(FileUtils.getUri(galleryfile5));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == GALLERY6 && resultCode == RESULT_OK) {
            if (data != null) {

                file_uri = data.getData();
                Log.e("imageUri", file_uri.toString());
                try {


                    if (part6 != null) {
                        index_ = fileParts.indexOf(part6);
                        //remove value from arraylist
                        fileParts.remove(index_);
                        part6 = null;
                    }


                    if (gallerypart6 == null) {
                        Log.e("part6", "empty");
                    } else {
                        index_ = fileParts.indexOf(gallerypart6);
                        fileParts.remove(index_);
                        gallerypart6 = null;
                    }


                    galleryfile6 = new Compressor(this).compressToFile(FileUtils.getFile(this, file_uri));
                    gallerypart6 = prepareFilePart("profile_pic[]", galleryfile6);
                    fileParts.add(gallerypart6);

                    nicImg2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    nicImg2.setImageURI(FileUtils.getUri(galleryfile6));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_IMAGE1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog1();
                } else {
                    Toast.makeText(this, "please enable permission manually", Toast.LENGTH_SHORT).show();
                }
                break;
            case MY_PERMISSIONS_REQUEST_IMAGE2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog2();
                } else {
                    Toast.makeText(this, "please enable permission manually", Toast.LENGTH_SHORT).show();
                }
                break;
            case MY_PERMISSIONS_REQUEST_IMAGE3:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog3();
                } else {
                    Toast.makeText(this, "please enable permission manually", Toast.LENGTH_SHORT).show();
                }
                break;
            case MY_PERMISSIONS_REQUEST_IMAGE4:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog4();
                } else {
                    Toast.makeText(this, "please enable permission manually", Toast.LENGTH_SHORT).show();
                }
                break;
            case MY_PERMISSIONS_REQUEST_IMAGE5:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog5();
                } else {
                    Toast.makeText(this, "please enable permission manually", Toast.LENGTH_SHORT).show();
                }
                break;
            case MY_PERMISSIONS_REQUEST_IMAGE6:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog6();
                } else {
                    Toast.makeText(this, "please enable permission manually", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @NonNull
    private RequestBody createPartFromString(String val) {
        return RequestBody.create(MultipartBody.FORM, val);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, File file) {
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse(FileUtils.getMimeType(file)), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir1 = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir1      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        Log.e("TAG", "createImageFile: " + mCurrentPhotoPath);
        return image;
    }

    private void delete(File file) {
        file.delete();
        if (file.exists()) {
            try {
                file.getCanonicalFile().delete();
            } catch (IOException e) {
                Log.e("TAG", "IOException: " + e.toString());
            }
            if (file.exists()) {
                getApplicationContext().deleteFile(file.getName());
            }
        }
    }


    private void RegisterDriver() {
        HashMap<String, RequestBody> map = new HashMap<>();

        map.put("type", createPartFromString("driver"));
        map.put("password", createPartFromString(Spassword));
        map.put("number", createPartFromString(Snumber));
        map.put("name", createPartFromString(Sname));
        map.put("email", createPartFromString(Semail));
        map.put("dob", createPartFromString(date));
        map.put("city", createPartFromString(Scity));
        map.put("gender", createPartFromString(Sgender));

        map.put("father_name", createPartFromString(Sfname));
        map.put("car_color", createPartFromString(Scar_color));
        map.put("car_model", createPartFromString(Scar_model));
        map.put("car_number", createPartFromString(Scar_number));
        map.put("license_number", createPartFromString(Slicense_no));
        map.put("nic", createPartFromString(Snic));
        map.put("address", createPartFromString(Saddress));


        Log.e("TAG", "dataUpload: " + map.toString());
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerURL.BaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        UploadApi rest = retrofit.create(UploadApi.class);
        Log.e("TAG", "fileParts: " + fileParts.toString());
        Call<ResponseData> call = rest.uploadDriver(map, fileParts);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                whorlView.stop();
                relativeLayout.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                Log.e("ServerResponse", response.body().getSuccess() + "/" + response.body().getStatus()
//                        + "/" + response.body().getMessage());

//                assert response.body() != null;
                if (response.body().getSuccess().equals("success")) {

                    Toast.makeText(SignupDriver.this, getApplicationContext().getResources().getString(R.string.account_created_successfully),
                            Toast.LENGTH_SHORT).show();

                    SignupDriver.this.finish();
                    intent = new Intent(SignupDriver.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Log.e("serverResponse", response.toString());
                    Toast.makeText(SignupDriver.this, getApplicationContext().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                whorlView.stop();
                relativeLayout.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.e("FailuerResponse", t.toString());
                Toast.makeText(SignupDriver.this, getApplicationContext().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }
        });
    }


    String server_response_text;
    JSONObject jp_obj;
    JSONArray jar_array;
    boolean server_check = false;
    Intent intent;

    //ASYNTASK REGISTER USER deprecated////////////////////////////////////
    public class RegisterUser extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                JSONObject obj = new JSONObject();


                obj.put("operation", "register");
                obj.put("type", "ride");
                obj.put("password", Spassword);
                obj.put("number", Snumber);
                obj.put("name", Sname);
                obj.put("email", Semail);
                obj.put("dob", date);
                obj.put("city", Scity);
                obj.put("gender", Sgender);


                String str_req = JsonParser.multipartFormRequestForFindFriends(ServerURL.Url, "UTF-8", obj, null);


                jp_obj = new JSONObject(str_req);
                jar_array = jp_obj.getJSONArray("JsonData");

                JSONObject c;

                c = jar_array.getJSONObject(0);

                if (c.length() > 0) {

                    server_response = c.getString("response");

                    if (server_response.equals("0")) {
                        server_response_text = c.getString("response-text");

                    }
                }

                server_check = true;

            } catch (Exception e) {
                e.printStackTrace();

                //server response/////////////////////////
                server_check = false;
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            whorlView.stop();
            relativeLayout.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


            if (server_check) {

                if (server_response.equals("1")) {

                    Toast.makeText(SignupDriver.this, getApplicationContext().getResources().getString(R.string.account_created_successfully),
                            Toast.LENGTH_SHORT).show();


                    SignupDriver.this.finish();
                    intent = new Intent(SignupDriver.this, LoginActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(SignupDriver.this, server_response_text, Toast.LENGTH_SHORT).show();

                }

            } else {

                Toast.makeText(SignupDriver.this, getApplicationContext().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }

        }
    }


    DatePicker datePicker;

    public void dobDialog() {

        //CUSTOM DIALOG///////////////////////////////
        final Dialog dialog = new Dialog(SignupDriver.this);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_dob);

        datePicker = dialog.findViewById(R.id.datePicker1);
        final Button Bdob = dialog.findViewById(R.id.btn);


        Bdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //set date to textview
                date = getDOB();
                dob.setText(date);

                //calulate months
                calculate_age(getDOB());

                dialog.dismiss();

            }
        });


        dialog.show();
    }


    //set dob to textview
    public String getDOB() {

        StringBuilder builder = new StringBuilder();

        String day = String.format("%02d", datePicker.getDayOfMonth() + 1);
        String month = String.format("%02d", datePicker.getMonth() + 1);
        builder.append(datePicker.getYear() + "-");
        builder.append(month + "-");
        builder.append(day);
        //builder.append((datePicker.getMonth() + 1) + "-");//month is 0 based
        //builder.append(datePicker.getDayOfMonth());


        return builder.toString();
    }


    //calculate age
    public String calculate_age(String dob) {

        //get current date
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
//        Toast.makeText(this, formattedDate+"", Toast.LENGTH_SHORT).show();


        //calculate days
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateBefore = myFormat.parse(dob);
            Date dateAfter = myFormat.parse(formattedDate);

            daysBetween = DateTimeUtils.getDateDiff(dateAfter, dateBefore, DateTimeUnits.DAYS);
//            Toast.makeText(this, diff+"", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cal_age;
    }


    public void Gender() {

        Array_gender = new ArrayList<String>();

        Array_gender.add(getApplicationContext().getResources().getString(R.string.male));
        Array_gender.add(getApplicationContext().getResources().getString(R.string.female));


    }


}

