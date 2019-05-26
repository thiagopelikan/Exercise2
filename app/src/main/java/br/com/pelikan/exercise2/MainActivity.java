package br.com.pelikan.exercise2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    protected static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;

   // private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        myToolbar.setTitle("Take a picure and Share");

        findViewById(R.id.captureButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                File directory = getFilesDir();
//                File file = new File(directory, "fname_" +
//                        String.valueOf(System.currentTimeMillis()) + ".jpg");
//                if(!file.exists()){
//                    try {
//                        file.createNewFile();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                imageUri = FileProvider.getUriForFile(
//                        MainActivity.this,
//                        "br.com.pelikan.exercise2.fileprovider",
//                        file);
//                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"fname_" +
//                        String.valueOf(System.currentTimeMillis()) + ".jpg"));
//                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
//                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.share_menu) {

            Drawable mDrawable = ((ImageView)findViewById(R.id.image)).getDrawable();
            Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();

            File directory = getFilesDir();
            File file = new File(directory, "fname_" +
                    String.valueOf(System.currentTimeMillis()) + ".jpg");
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try (FileOutputStream out = new FileOutputStream(file)) {
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Uri imageUri = FileProvider.getUriForFile(
                    MainActivity.this,
                    "br.com.pelikan.exercise2.fileprovider",
                    file);

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("image/*");
            startActivity(Intent.createChooser(shareIntent, "Share Image"));

            return true;
        }
        return false;

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                try {
                    Bundle extras = data.getExtras();
                    Bitmap bmp = (Bitmap) extras.get("data");
                    ((ImageView)findViewById(R.id.image)).setImageBitmap(bmp);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT);
            }
        }


    }
}
