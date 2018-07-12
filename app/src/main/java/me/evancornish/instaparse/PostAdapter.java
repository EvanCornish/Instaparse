package me.evancornish.instaparse;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import me.evancornish.instaparse.model.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    ArrayList<Post> posts;
    Context context;

    public PostAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.item_post, viewGroup, false);
        return new ViewHolder(postView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Post post = posts.get(position);
        viewHolder.tvName.setText(post.getUser().getString("name"));
        viewHolder.tvUsername.setText("@" + post.getUser().getString("username"));
        viewHolder.tvDescription.setText(post.getDescription());
        viewHolder.tvTime.setText(post.getCreatedAt().toString());
        Glide.with(context)
                .load(post.getImage().getUrl())
                .into(viewHolder.ivPic);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView ivPic;
        TextView tvName;
        TextView tvUsername;
        TextView tvDescription;
        TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);

            ivPic = itemView.findViewById(R.id.ivPic);
            tvName = itemView.findViewById(R.id.tvDName);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTime = itemView.findViewById(R.id.tvTime);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                Post post = posts.get(getAdapterPosition());
                Intent intent = new Intent(context, DetailedPostActivity.class);
                intent.putExtra("Post",post.getObjectId());
                context.startActivity(intent);
            }
        }
    }
}
