/*
 * Created by ishaanjav
 * github.com/ishaanjav
 */

package app.ij.mlwithtensorflowlite;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import app.ij.mlwithtensorflowlite.ml.Model;
import android.animation.LayoutTransition;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button camera, gallery,remediesButton;
    ImageView imageView;
    TextView result;
    int imageSize = 256;
    Boolean isDiseaseIdentified=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout rootLayout = findViewById(R.id.root_layout);

        // Enable layout animations
       Button remediesButton;
        camera = findViewById(R.id.button);
        gallery = findViewById(R.id.button2);
        animateButton(camera, 0);
        gallery.postDelayed(() -> animateButton(gallery, 200), 500);
        result = findViewById(R.id.result);
        imageView = findViewById(R.id.imageView);

        remediesButton = findViewById(R.id.remediesButton);
        remediesButton.setVisibility(View.GONE);
        remediesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDiseaseIdentified) {
                    String disease = result.getText().toString();
                    Intent intent = new Intent(MainActivity.this, RemediesActivity.class);
                    intent.putExtra("disease", disease);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "No disease identified.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, 1);
            }
        });
    }
    private boolean isGrapeOrTomatoLeaf(Bitmap image) {
        double aspectRatio = (double) image.getWidth() / (double) image.getHeight();
        if (aspectRatio < 0.5 || aspectRatio > 2.0) {
            return false;
        }

        int leafPixelCount = 0;
        int[] pixelValues = new int[imageSize * imageSize];
        image.getPixels(pixelValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
        for (int i = 0; i < imageSize; i++) {
            for (int j = 0; j < imageSize; j++) {
                int val = pixelValues[i * imageSize + j];
                int r = (val >> 16) & 0xFF;
                int g = (val >> 8) & 0xFF;
                int b = val & 0xFF;
                // Check if the pixel is green and has low blue and red values
                if (g > r && g > b && b < 100 && r < 100) {
                    leafPixelCount++;
                }
            }
        }
        return leafPixelCount >= 5000;
    }
    public void classifyImage(Bitmap image) {
        try {
            if (!isGrapeOrTomatoLeaf(image)) {
                result.setText("Error: Input image is not a grape or tomato leaf image");
                isDiseaseIdentified = false;
                return;
            }

            // Check if the image is a grape or tomato leaf image based on color histogram
            int leafPixelCount = 0;
            int[] pixelValues = new int[imageSize * imageSize];
            image.getPixels(pixelValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = pixelValues[i * imageSize + j];
                    int r = (val >> 16) & 0xFF;
                    int g = (val >> 8) & 0xFF;
                    int b = val & 0xFF;
                    // Check if the pixel is green and has low blue and red values
                    if (g > r && g > b && b < 100 && r < 100) {
                        leafPixelCount++;
                    }
                }
            }
            if (leafPixelCount < 5000) {
                result.setText("Error: Input image is not a grape or tomato leaf image,scan again!");
                return;
            }

            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 256, 256, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            // Iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets the result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // Find the index of the class with the highest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            float confidenceThreshold = 0.5f;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence && confidences[i] >= confidenceThreshold) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }

            String[] results={"Grape black rot disease", "Grape esca(Black_Measles) disease", "Grape healthy", "Tomato bacterial spot disease", "Tomato early blight disease", "Tomato late blight disease", "Tomato target spot disease", "Tomato mosaic virus disease", "Tomato healthy", "Unknown"};
            if (maxConfidence < confidenceThreshold || maxPos == results.length - 1) {
                result.setText("Unknown"); // Set the class label as "Unknown"
            } else {
                String className = results[maxPos];
                result.setText(className);
            }
            isDiseaseIdentified = true;

            // Show the remedies button
            Button remediesButton = findViewById(R.id.remediesButton);
            remediesButton.setVisibility(View.VISIBLE);
            // Release model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 3){
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            }else{
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                classifyImage(image);
            }
            TextView instructionText = findViewById(R.id.instructionText);
            instructionText.setVisibility(View.INVISIBLE);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void animateButton(View view, int delay) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f);
        scaleX.setDuration(500);
        scaleY.setDuration(500);
        scaleX.setStartDelay(delay);
        scaleY.setStartDelay(delay);
        scaleX.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleY.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleX.start();
        scaleY.start();
    }

}