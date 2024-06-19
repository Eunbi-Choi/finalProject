package com.cookandroid.finalproject2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.cookandroid.finalproject2.R;
import com.cookandroid.finalproject2.BookResponse;
import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {
    private List<BookResponse.Item> books;

    public BooksAdapter(List<BookResponse.Item> books) {
        this.books = books;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        BookResponse.Item book = books.get(position);
        holder.titleTextView.setText(book.getVolumeInfo().getTitle());
        holder.authorsTextView.setText(book.getVolumeInfo().getAuthors() != null ?
                String.join(", ", book.getVolumeInfo().getAuthors()) : "Unknown author");

        String imageUrl = book.getVolumeInfo().getImageLinks() != null ?
                book.getVolumeInfo().getImageLinks().getThumbnail() : null;

        if (imageUrl != null) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .into(holder.bookImageView);
        } else {
            holder.bookImageView.setImageResource(R.drawable.ic_launcher_foreground); // 기본 이미지 설정
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public ImageView bookImageView;
        public TextView titleTextView;
        public TextView authorsTextView;

        public BookViewHolder(View itemView) {
            super(itemView);
            bookImageView = itemView.findViewById(R.id.bookImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorsTextView = itemView.findViewById(R.id.authorsTextView);
        }
    }
}
