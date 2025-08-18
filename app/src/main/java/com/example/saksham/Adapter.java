// Adapter.java
package com.example.saksham;

import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.view.MotionEvent;

public class Adapter extends RecyclerView.Adapter<Adapter.EmojiViewHolder> {

    private final List<Integer> emojiList;

    public Adapter(List<Integer> emojiList) {
        this.emojiList = emojiList;
    }

    @NonNull
    @Override
    public EmojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emoji_item, parent, false);
        return new EmojiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiViewHolder holder, int position) {
        holder.emojiImageView.setImageResource(emojiList.get(position));
        holder.emojiImageView.setTag(emojiList.get(position));

        // enabling drag and drop of emojis
        holder.emojiImageView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("emoji_id", String.valueOf(v.getTag()));
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);

                // Start the drag operation
                v.startDragAndDrop(data, shadowBuilder, v, 0);

                // Add a performClick() call for accessibility purposes
                v.performClick();
                return true;
            }
            return false;
        });
    }


    @Override
    public int getItemCount() {
        return emojiList.size();
    }

    public static class EmojiViewHolder extends RecyclerView.ViewHolder {
        ImageView emojiImageView;

        public EmojiViewHolder(@NonNull View itemView) {
            super(itemView);
            emojiImageView = itemView.findViewById(R.id.emojiImageView);
        }
    }
}
