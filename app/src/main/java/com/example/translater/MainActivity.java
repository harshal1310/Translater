package com.example.translater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.IOException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] courses = { "AF", "AR", "BE", "BG", "BN","CA", "CS","CY","DA","EN","FR","GU","HI", "IT","KN","KO","MR","MT","RO","RU","UK"};

    int langcode[]={      0,    1,     2,    3,     4,  5,   6,    7,  8,  11,   17,  20,  22,  28,  31,  32,   36,  38,  43,  44,  55};
    EditText editText;
    TextView tv,x;
    Button translaterbutton;
    int todest;
    //String sourcelangtext;

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.text);

        translaterbutton = findViewById(R.id.button);
        Spinner spin = findViewById(R.id.spin);
        tv = findViewById(R.id.translatetext);




        spin.setOnItemSelectedListener(this);
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                courses);
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);


        spin.setAdapter(ad);

        translaterbutton.setOnClickListener(v -> identifylanguage());


    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        todest=langcode[position];


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private void identifylanguage() {
        FirebaseLanguageIdentification languageIdentification=FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
      String text = editText.getText().toString();
      tv.setText("Translating..");
        languageIdentification.identifyLanguage(text).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(@Nullable String s) {

                if (s!= "und") {
                    getcode(s);
                  //  Log.d("Check", "Language: " + s);
                    //Toast.makeText(MainActivity.this,"l is="+s,Toast.LENGTH_LONG).show();
                } else {
                    Log.d("Check", "Can't identify language.");
                }
            }
        }      ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Language id", "exception processing " + e);
                Toast.makeText(MainActivity.this,
                        "Language could not be identified",
                        Toast.LENGTH_LONG).show();
            }
        });


    }

    private void getcode(String s) {

        if(s==null)
            return;
        int languagecode;
        Log.d("check",s);
        switch(s)
        {   case  "af":
            languagecode=FirebaseTranslateLanguage.AF;
            break;
            case  "ar":
                languagecode=FirebaseTranslateLanguage.AR;
                break;
            case  "be":
                languagecode=FirebaseTranslateLanguage.BE;
                break;
            case  "bg":
                languagecode=FirebaseTranslateLanguage.BG;
                break;
            case  "bn":
                languagecode=FirebaseTranslateLanguage.BN;
                break;
            case  "ca":
                languagecode=FirebaseTranslateLanguage.CA;
                break;
            case  "cs":
                languagecode=FirebaseTranslateLanguage.CS;
                break;
            case  "cy":
                languagecode=FirebaseTranslateLanguage.CY;
                break;
            case "da":
                languagecode=FirebaseTranslateLanguage.DA;
                break;
            case  "en":
                languagecode=FirebaseTranslateLanguage.EN;
                break;
            case  "fr":
                languagecode=FirebaseTranslateLanguage.FR;
                break;
            case  "gu":
                languagecode=FirebaseTranslateLanguage.GU;
                break;
            case  "hi":
                languagecode=FirebaseTranslateLanguage.HI;
                break;
            case  "it":
                languagecode=FirebaseTranslateLanguage.IT;
                break;
            case  "kn":
                languagecode=FirebaseTranslateLanguage.KN;
                break;
            case  "ko":
                languagecode=FirebaseTranslateLanguage.KO;
                break;
            case  "mr":
                languagecode=FirebaseTranslateLanguage.MR;
                break;
            case  "mt":
                languagecode=FirebaseTranslateLanguage.MT;
                break;
            case  "ro":
                languagecode=FirebaseTranslateLanguage.RO;
                break;
            case  "ru":
                languagecode=FirebaseTranslateLanguage.RU;
                break;
            case  "uk":
                languagecode=FirebaseTranslateLanguage.UK;
                break;

            default:
                languagecode=0;
        }
        Log.d("check","langcode="+languagecode);
    translatefun(languagecode);
    }

    public void translatefun(int code) {
        String text = editText.getText().toString();



        FirebaseTranslatorOptions options =
                new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(code)//FirebaseTranslateLanguage.EN)
                        .setTargetLanguage(todest)
                        .build();

        final FirebaseTranslator translator=FirebaseNaturalLanguage.getInstance().getTranslator(options);

        FirebaseModelDownloadConditions conditions=new FirebaseModelDownloadConditions.Builder().build();
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                translator.translate(text).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                    Log.d("check",s);
                        tv.setText(s);
                    }
                });


            }
        });
    }


}