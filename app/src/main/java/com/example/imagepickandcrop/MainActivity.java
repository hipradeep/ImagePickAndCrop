package com.example.imagepickandcrop;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity  {

    @BindView(R.id.selected_image)
    ImageView selected_image;
    @BindView(R.id.pick_image_button) Button pick_image_button;
    @BindView(R.id.crop_image_button) Button crop_image_button;
    @BindView(R.id.image_path_tv)
    TextView image_path_tv;
    PickResult imagePickResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        pick_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup())
                        .setOnPickResult(new IPickResult() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onPickResult(PickResult r) {
                                if (r.getError() == null) {
                                    imagePickResult=r;
                                    selected_image.setImageURI(r.getUri());
                                    String filePath = r.getPath();
                                    image_path_tv.setText("Image Path : "+filePath);
                                } else {
                                    //Handle possible errors
                                    Toast.makeText(MainActivity.this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
                                }                            }
                        }).show(MainActivity.this);


            }
        });
        crop_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           if (imagePickResult!=null){
               cropImage(imagePickResult);
           }else {
               Toast.makeText(MainActivity.this, "Pick Image First!", Toast.LENGTH_SHORT).show();
           }

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void cropImage(PickResult r) {
        CropImage.activity(r.getUri())
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                selected_image.setImageURI(resultUri);
                String filePath = resultUri.getPath();
                image_path_tv.setText("Image Path : "+filePath);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


}