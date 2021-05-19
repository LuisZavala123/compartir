package com.example.redessociales;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private static final int REQUEST_VIDEO_CODE = 1000;
    private Button btnCompartirLink,btnCompartirFoto,btnCompartirVideo;
    ShareDialog shareDialog;

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto sharePhoto = new SharePhoto.Builder().setBitmap(bitmap).build();
            if (ShareDialog.canShow(SharePhotoContent.class)){
                SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(sharePhoto).build();
                shareDialog.show(content);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);


        btnCompartirLink=(Button)findViewById(R.id.btnCompartiLink);
        btnCompartirFoto=(Button)findViewById(R.id.btnCompartirFoto);
        btnCompartirVideo=(Button)findViewById(R.id.btnCompartirVideo);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Toast.makeText(MainActivity.this,"Autenticacion exitosa",Toast.LENGTH_LONG)
                .show();
                btnCompartirLink.setVisibility(View.VISIBLE);
                btnCompartirFoto.setVisibility(View.VISIBLE);
                btnCompartirVideo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(MainActivity.this,"Autenticacion cancelada",Toast.LENGTH_LONG)
                        .show();
                btnCompartirLink.setVisibility(View.GONE);
                btnCompartirFoto.setVisibility(View.GONE);
                btnCompartirVideo.setVisibility(View.GONE);
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this,"Autenticacion fallida",Toast.LENGTH_LONG)
                        .show();
                btnCompartirLink.setVisibility(View.GONE);
                btnCompartirFoto.setVisibility(View.GONE);
                btnCompartirVideo.setVisibility(View.GONE);
            }




        });

        btnCompartirLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(MainActivity.this,"Se ha compartido de forma correcta",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(MainActivity.this,"Se ha canselado",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
                ShareLinkContent linkContent = new ShareLinkContent.Builder().setQuote("Se compartio el link")
                        .setContentUrl(Uri.parse("http://youtube.com"))
                        .build();
                if(ShareDialog.canShow(ShareLinkContent.class)){
                    shareDialog.show(linkContent);
                }
            }
        });
        btnCompartirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(MainActivity.this,"Se ha compartido de forma correcta",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(MainActivity.this,"Se ha canselado",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });

                Picasso.with(getBaseContext()).load("https://comicvine1.cbsistatic.com/uploads/scale_super/11118/111187046/5661781-5629844810-gravi.jpg")
                        .into(target);
            }
        });

        btnCompartirVideo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Seleccionar Video"),REQUEST_VIDEO_CODE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_VIDEO_CODE) {
            Uri selectedVideo = data.getData();
            ShareVideo video = new ShareVideo.Builder()
                    .setLocalUrl(selectedVideo)
                    .build();
            ShareVideoContent videoContent = new ShareVideoContent.Builder()
                    .setContentTitle("Este es un video")
                    .setContentDescription("Un video mas")
                    .setVideo(video)
                    .build();
            if (ShareDialog.canShow(ShareVideoContent.class)) {
                shareDialog.show(videoContent);
            }
        }else{
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if(isLoggedIn){
            Toast.makeText(MainActivity.this,"Autenticacion exitosa",Toast.LENGTH_LONG)
                    .show();
        }else{
            Toast.makeText(MainActivity.this,"Autenticacion fallida",Toast.LENGTH_LONG)
                    .show();
            }
        }
    }
}