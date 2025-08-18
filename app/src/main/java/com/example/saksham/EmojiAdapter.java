package com.example.saksham;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView; // Import ImageView
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder> {

    private List<Emoji> emojiList;
    private OnEmojiClickListener listener;

    public interface OnEmojiClickListener {
        void onEmojiClick(Emoji emoji);
    }

    public EmojiAdapter(List<Emoji> emojiList, OnEmojiClickListener listener) {
        this.emojiList = emojiList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EmojiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.emoji_item, parent, false);
        return new EmojiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiViewHolder holder, int position) {
        Emoji currentEmoji = emojiList.get(position);
        // Set the image resource for the ImageView
        holder.emojiImageView.setImageResource(currentEmoji.getImageResourceId());

        // Set click listener for the entire item view
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEmojiClick(currentEmoji);
            }
        });
    }

    @Override
    public int getItemCount() {
        return emojiList.size();
    }

    static class EmojiViewHolder extends RecyclerView.ViewHolder {
        ImageView emojiImageView; // Change to ImageView

        EmojiViewHolder(@NonNull View itemView) {
            super(itemView);
            emojiImageView = itemView.findViewById(R.id.emojiImageView); // Find ImageView
        }
    }
}
