package com.example.saksham;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

public class MainActivity2 extends AppCompatActivity {

    private CardView emojiCardView;
    private EditText textBox;
    private DrawingView drawingView;
    private final List<String> droppedEmojiNames = new ArrayList<>();

    private final Map<List<String>, String> emojiRules = new HashMap<>(); // Rule-based map

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        drawingView = findViewById(R.id.drawingView);
        Button clearButton = findViewById(R.id.clearButton);
        Button emojisButton = findViewById(R.id.Emojis);
        emojiCardView = findViewById(R.id.emojiCardView);
        RecyclerView emojiRecyclerView = findViewById(R.id.emojiRecyclerView);
        textBox = findViewById(R.id.textBox);

        clearButton.setOnClickListener(v -> {
            drawingView.clear();
            droppedEmojiNames.clear();
            textBox.setText("");
        });

        emojisButton.setOnClickListener(v -> {
            emojiCardView.setVisibility(emojiCardView.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
        });

        List<Integer> emojiList = Arrays.asList(
                R.drawable.happy, R.drawable.sad, R.drawable.angry, R.drawable.apple, R.drawable.bandage,
                R.drawable.bath, R.drawable.butterfly, R.drawable.toothbrush, R.drawable.cake, R.drawable.crabs,
                R.drawable.dog, R.drawable.donut, R.drawable.ear, R.drawable.elephant, R.drawable.eyes,
                R.drawable.hospital, R.drawable.lovely, R.drawable.medicines, R.drawable.plates, R.drawable.rose,
                R.drawable.school, R.drawable.sick, R.drawable.sleeping_bed, R.drawable.soap, R.drawable.stethoscope,
                R.drawable.strawberry, R.drawable.swan, R.drawable.teeth, R.drawable.toilet
        );

        Adapter adapter = new Adapter(emojiList);
        emojiRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        emojiRecyclerView.setAdapter(adapter);

        // RULES (you can add more)
        emojiRules.put(Arrays.asList("hospital", "medicines"), "I need to go to the hospital for medicine.");
        emojiRules.put(Arrays.asList("apple", "cake", "donut"), "I love sweet and fruity snacks!");
        emojiRules.put(Arrays.asList("toothbrush", "teeth"), "Brush your teeth regularly!");
        emojiRules.put(Arrays.asList("bandage", "sick", "hospital"), "You might need to visit the hospital.");
        emojiRules.put(Arrays.asList("dog", "butterfly", "rose"), "You are enjoying nature with your pet.");
        emojiRules.put(Arrays.asList("sleeping_bed", "soap", "bath"), "It's time to sleep after a bath.");
        emojiRules.put(Arrays.asList("sad", "sick"), "I'm feeling sick and sad.");
        emojiRules.put(Arrays.asList("school", "happy"), "I am happy to go to school.");

        drawingView.setOnDragListener((v, event) -> {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);

                case DragEvent.ACTION_DROP:
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    int emojiId = Integer.parseInt(item.getText().toString());

                    drawingView.addEmoji(emojiId, event.getX(), event.getY());

                    String emojiName = getEmojiNameFromId(emojiId);
                    if (emojiName != null) {
                        droppedEmojiNames.add(emojiName.toLowerCase());
                        predictSentenceFromRules();
                    } else {
                        Toast.makeText(this, "Unknown emoji dropped", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                default:
                    return true;
            }
        });
    }

    /**
     * Matches dropped emojis against predefined rule-based combinations.
     */
    private void predictSentenceFromRules() {
        List<String> sortedDropped = new ArrayList<>(droppedEmojiNames);
        Collections.sort(sortedDropped);

        for (Map.Entry<List<String>, String> entry : emojiRules.entrySet()) {
            List<String> rule = new ArrayList<>(entry.getKey());
            Collections.sort(rule);

            if (sortedDropped.containsAll(rule)) {
                textBox.setText(entry.getValue());
                return;
            }
        }

        textBox.setText("No match found for these emojis.");
    }

    /**
     * Converts emoji resource ID to readable name.
     */
    private String getEmojiNameFromId(int id) {
        if (id == R.drawable.happy) return "happy";
        if (id == R.drawable.sad) return "sad";
        if (id == R.drawable.angry) return "angry";
        if (id == R.drawable.apple) return "apple";
        if (id == R.drawable.bandage) return "bandage";
        if (id == R.drawable.bath) return "bath";
        if (id == R.drawable.butterfly) return "butterfly";
        if (id == R.drawable.toothbrush) return "toothbrush";
        if (id == R.drawable.cake) return "cake";
        if (id == R.drawable.crabs) return "crabs";
        if (id == R.drawable.dog) return "dog";
        if (id == R.drawable.donut) return "donut";
        if (id == R.drawable.ear) return "ear";
        if (id == R.drawable.elephant) return "elephant";
        if (id == R.drawable.eyes) return "eyes";
        if (id == R.drawable.hospital) return "hospital";
        if (id == R.drawable.lovely) return "lovely";
        if (id == R.drawable.medicines) return "medicines";
        if (id == R.drawable.plates) return "plates";
        if (id == R.drawable.rose) return "rose";
        if (id == R.drawable.school) return "school";
        if (id == R.drawable.sick) return "sick";
        if (id == R.drawable.sleeping_bed) return "sleeping_bed";
        if (id == R.drawable.soap) return "soap";
        if (id == R.drawable.stethoscope) return "stethoscope";
        if (id == R.drawable.strawberry) return "strawberry";
        if (id == R.drawable.swan) return "swan";
        if (id == R.drawable.teeth) return "teeth";
        if (id == R.drawable.toilet) return "toilet";
        return null;
    }
}
