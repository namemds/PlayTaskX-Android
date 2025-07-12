package com.cdut.playtask.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cdut.playtask.R;
import com.cdut.playtask.network.Message;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private final List<Message> messages;

    public ChatAdapter(List<Message> messages) {
        this.messages = messages;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView leftText, rightText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            leftText = itemView.findViewById(R.id.left_text);
            rightText = itemView.findViewById(R.id.right_text);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).role.equals("user") ? 1 : 0;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message msg = messages.get(position);
        if (msg.role.equals("user")) {
            holder.leftText.setVisibility(View.GONE);
            holder.rightText.setVisibility(View.VISIBLE);
            holder.rightText.setText(msg.content);
        } else {
            holder.rightText.setVisibility(View.GONE);
            holder.leftText.setVisibility(View.VISIBLE);
            holder.leftText.setText(msg.content);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
