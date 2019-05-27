package br.com.lucasfsilva.exemplocmera;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {
    private ImageView imvFoto;
    private Button btnCamera;

    private String caminho;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imvFoto = (ImageView) findViewById(R.id.imvFoto);
        btnCamera = (Button) findViewById(R.id.btnCamera);


        // Permissões
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acionarCamera();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap bitmap = BitmapFactory.decodeFile(caminho);
                imvFoto.setImageBitmap(bitmap);
            }
        }
    }

    private void acionarCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File file = criarFoto();

            if (file != null) {
                caminho = file.getAbsolutePath();
                Uri uri = FileProvider.getUriForFile(getApplicationContext(), "br.com.lucasfsilva.exemplocmera.fileprovider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 1);
            }
        }
    }

    private File criarFoto() {
        String nome = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File diretorio = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imagem = null;
        try {
            imagem = File.createTempFile(nome, ".jpg", diretorio);
        } catch (IOException e) {
            Log.d("criarFoto", e.toString());
        }
        return imagem;
    }
}
