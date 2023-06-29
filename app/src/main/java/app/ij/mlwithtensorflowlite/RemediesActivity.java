package app.ij.mlwithtensorflowlite;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RemediesActivity extends AppCompatActivity {

    private TextView remediesTitle;
    private TextView remedy1;
    private TextView remedy2;
    private TextView remedy3;
    private TextView remedy4;
    private TextView remedy5;
    private Button scanAgainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remedies);

        // Get the identified disease from MainActivity
        String identifiedDisease = getIntent().getStringExtra("disease");
        Button scanAgainButton = findViewById(R.id.scanAgainButton);
        // Find views by their IDs
        remediesTitle = findViewById(R.id.remediesTitle);
        remedy1 = findViewById(R.id.remedy1);
        remedy2 = findViewById(R.id.remedy2);
        remedy3 = findViewById(R.id.remedy3);
        remedy4 = findViewById(R.id.remedy4);
        remedy5 = findViewById(R.id.remedy5);

        // Set the remedies based on the identified disease
        setRemedies(identifiedDisease);
        scanAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to MainActivity
                Intent intent = new Intent(RemediesActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setRemedies(String disease) {
        switch (disease) {
            case "Grape black rot disease":
                remediesTitle.setText("Remedies for Grape black rot disease");
                remedy1.setText("1.Prune affected vines immediately: Remove infected clusters and prune infected parts to limit spread");
                remedy2.setText("2.Apply fungicides: Follow spray schedule, use recommended fungicides to control black rot");
                remedy3.setText("3.Promote air circulation: Space vines for better airflow, prune to create open structure");
                remedy4.setText("4.Remove debris: Clean up fallen leaves, mummies to prevent fungal spore buildup");
                remedy5.setText("5.Monitor regularly: Inspect for lesions, act early to reduce spread and crop loss");
                break;
            case "Grape esca(Black_Measles) disease":
                remediesTitle.setText("Remedies for Grape esca(Black_Measles) disease");
                remedy1.setText("1.Practice proper vineyard hygiene: Remove infected plants, sanitize tools, and dispose of diseased material");
                remedy2.setText("2.Apply trunk protectants: Use approved products to protect vine trunks from infection");
                remedy3.setText("3.Implement cultural practices: Manage canopy density, reduce water stress, and maintain balanced nutrition.\n");
                remedy4.setText("4.Monitor and prune: Regularly inspect vines, prune affected wood to limit disease progression");
                remedy5.setText("5.Explore alternative treatments: Investigate biocontrol agents or natural compounds for potential control of esca");
                break;
            // Add more cases for other diseases
            case "Tomato bacterial spot disease":
                remediesTitle.setText("Remedies for Tomato bacterial spot disease");
                remedy1.setText("1.Practice crop rotation: Rotate tomato plants with non-susceptible crops to reduce bacterial spot disease pressure");
                remedy2.setText("2.Remove infected plants: Promptly remove and destroy infected tomato plants to prevent disease spread");
                remedy3.setText("3.Use disease-free seeds and transplants: Start with healthy seeds or transplants from reliable sources to minimize the risk of introducing bacterial spot into your tomato garden");
                remedy4.setText("4.Avoid overhead watering: Use drip irrigation or soaker hoses to keep the foliage dry, as wet leaves create favorable conditions for bacterial spot development");
                remedy5.setText("5.Apply copper-based sprays: Use copper-based bactericides according to the manufacturer's instructions to control bacterial spot. Start spraying preventively before symptoms appear and continue on a regular schedule.");
                break;
            case "Tomato early blight disease":
                remediesTitle.setText("Remedies for Tomato early blight disease");
                remedy1.setText("1.Rotate crops: Avoid planting tomatoes in the same location for consecutive years to minimize the risk of early blight disease buildup");
                remedy2.setText("2.Mulch soil: Apply a layer of organic mulch around tomato plants to prevent soil splashing and reduce the chance of early blight spores splashing onto the leaves");
                remedy3.setText("3.Water at the base: Water tomatoes at the base, avoiding overhead irrigation, to keep foliage dry and reduce humidity, which discourages early blight development");
                remedy4.setText("4.Remove infected foliage: Promptly remove and dispose of any leaves or plants showing signs of early blight, such as dark brown lesions with concentric rings, to prevent the disease from spreading further");
                remedy5.setText("5.Apply copper-based fungicides: Use copper-based fungicides according to the recommended application schedule to protect tomato plants against early blight and control its spread");
                break;
            case "Tomato late blight disease":
                remediesTitle.setText("Remedies for Tomato late blight disease");
                remedy1.setText("1.Remove infected plants: Immediately remove and destroy any tomato plants showing signs of late blight disease to prevent further spread.");
                remedy2.setText("2.Apply copper-based fungicides: Use recommended copper-based fungicides as a preventive measure to control late blight disease in tomatoes.");
                remedy3.setText("3.Avoid overhead watering: Minimize water splashing by using drip irrigation or soaker hoses to keep the foliage dry, as moisture promotes the development of late blight.");
                remedy4.setText("4.Provide proper spacing: Properly space tomato plants to allow for good air circulation, which helps reduce humidity levels and minimize the risk of late blight infection.");
                remedy5.setText("5.Rotate crops: Practice crop rotation by avoiding planting tomatoes or related crops in the same location for at least two to three years to disrupt the disease cycle and reduce the risk of late blight recurrence.");
                break;
            case "Tomato target spot disease":
                remediesTitle.setText("Remedies for Tomato target spot disease");
                remedy1.setText("1.Remove infected plant parts: Prune and discard affected leaves and stems to prevent disease spread");
                remedy2.setText("2.Use copper-based fungicides: Apply recommended fungicides to control target spot disease on tomato plants.");
                remedy3.setText("3.Avoid overhead watering: Water at the base of plants to minimize moisture on foliage, reducing disease development.");
                remedy4.setText("4.Mulch around plants: Use organic mulch to create a barrier between soil and tomato leaves, preventing spore splashing.");
                remedy5.setText("5.Rotate crops: Practice crop rotation to reduce the buildup of pathogens in the soil and minimize disease recurrence.");
                break;
            case "Tomato mosaic virus disease":
                remediesTitle.setText("Remedies for Tomato target spot disease");
                remedy1.setText("1.Practice strict hygiene: Clean and disinfect tools, greenhouse surfaces, and hands to prevent the spread of the Tomato mosaic virus.");
                remedy2.setText("2.Use disease-resistant varieties: Choose tomato cultivars that are resistant to the Tomato mosaic virus to minimize the risk of infection.");
                remedy3.setText("3.Control aphid populations: Aphids can transmit the virus, so employ measures like insecticidal soaps or natural predators to manage aphid populations.");
                remedy4.setText("4.Avoid smoking near tomato plants: Tobacco can harbor the virus, so refrain from smoking near tomato plants to prevent potential transmission.");
                remedy5.setText("5.Remove infected plants: Promptly remove and destroy any plants showing symptoms of the Tomato mosaic virus to prevent further spread.");
                break;

            default:
                remediesTitle.setText("Remedies not found");
                break;
        }
    }


}
