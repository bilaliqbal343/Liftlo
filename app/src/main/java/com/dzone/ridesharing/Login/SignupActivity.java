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


public class SignupActivity extends AppCompatActivity {


    EditText name, email, password, city,
            code1, code2, code3, code4;
    int[] code = new int[4];
    Random rand = new Random();
    String sms_code, code1_, code2_, code3_, code4_;
    TextView dob, gender;
    Button button, choose_image;
    String Sname, Semail, Snumber, Spassword, Sdob, Scity, Sgender;
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
    ImageView profile_image;
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


    File camerafile1, camerafile2, camerafile3, camerafile4, camerafile5;
    File galleryfile1, galleryfile2, galleryfile3, galleryfile4, galleryfile5;
    Uri file_uri;
    private String mCurrentPhotoPath;
    private File compressedfile1, compressedfile2, compressedfile3, compressedfile4, compressedfile5;
    private List<MultipartBody.Part> fileParts = new ArrayList<>();
    MultipartBody.Part part1, part2, part3, part4, part5;
    MultipartBody.Part gallerypart1, gallerypart2, gallerypart3, gallerypart4, gallerypart5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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
        choose_image = findViewById(R.id.choose_image);
        profile_image = findViewById(R.id.pimage);

        number.setText(phonenumber);


        choose_image = findViewById(R.id.choose_image);
        choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openImageDialog1();

                } else {
                    ActivityCompat.requestPermissions(SignupActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_IMAGE1);
                }
            }
        });


        spinnerDialog2 = new SpinnerDialog(SignupActivity.this, Array_gender, "Select Gender", R.style.DialogAnimations_SmileWindow);// With 	Animation

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
                } else if (fileParts.size() < 1) {
                    Toast.makeText(SignupActivity.this, getApplicationContext().getResources().getString(R.string.choose_image), Toast.LENGTH_SHORT).show();
                } else {

                    if (new Check_internet_connection(getApplicationContext()).isNetworkAvailable()) {


                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        relativeLayout.setVisibility(View.VISIBLE);
                        whorlView.start();
//                        new RegisterUser().execute();
                        RegisterPassenger();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                getApplicationContext().getResources().getString(R.string.check_internet_connection), Toast.LENGTH_LONG).show();
                    }


                }


            }
        });


    }


    private void openImageDialog1() {
        final Dialog sheetDialog = new Dialog(SignupActivity.this, R.style.dialog_theme);
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
                        file_uri = FileProvider.getUriForFile(SignupActivity.this, "com.dzone.ridesharing.fileprovider", camerafile1);
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
            default:
                Toast.makeText(this, "Can't Access FIles", Toast.LENGTH_SHORT).show();
        }
    }


    @NonNull
    private RequestBody createPartFromString(String val) {
        return RequestBody.create(okhttp3.MultipartBody.FORM, val);
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


    private void RegisterPassenger() {
        HashMap<String, RequestBody> map = new HashMap<>();

        map.put("type", createPartFromString("ride"));
        map.put("password", createPartFromString(Spassword));
        map.put("number", createPartFromString(Snumber));
        map.put("name", createPartFromString(Sname));
        map.put("email", createPartFromString(Semail));
        map.put("dob", createPartFromString(date));
        map.put("city", createPartFromString(Scity));
        map.put("gender", createPartFromString(Sgender));


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
        Call<ResponseData> call = rest.uploadData(map, fileParts);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                whorlView.stop();
                relativeLayout.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.e("ServerResponse", response.body().getSuccess() + "/" + response.body().getStatus()
                        + "/" + response.body().getMessage());

                if (response.body().getSuccess().equals("success")) {

                    Toast.makeText(SignupActivity.this, getApplicationContext().getResources().getString(R.string.account_created_successfully),
                            Toast.LENGTH_SHORT).show();

                    SignupActivity.this.finish();
                    intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Log.e("serverResponse", response.toString());
                    Toast.makeText(SignupActivity.this, getApplicationContext().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                whorlView.stop();
                relativeLayout.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.e("FailuerResponse", t.toString());
                Toast.makeText(SignupActivity.this, getApplicationContext().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(SignupActivity.this, getApplicationContext().getResources().getString(R.string.account_created_successfully),
                            Toast.LENGTH_SHORT).show();


                    SignupActivity.this.finish();
                    intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(SignupActivity.this, server_response_text, Toast.LENGTH_SHORT).show();

                }

            } else {

                Toast.makeText(SignupActivity.this, getApplicationContext().getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            }

        }
    }


    DatePicker datePicker;

    public void dobDialog() {

        //CUSTOM DIALOG///////////////////////////////
        final Dialog dialog = new Dialog(SignupActivity.this);
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

