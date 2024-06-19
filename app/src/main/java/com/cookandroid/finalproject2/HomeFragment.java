package com.cookandroid.finalproject2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cookandroid.finalproject2.BooksAdapter;
import com.cookandroid.finalproject2.GoogleBooksApi;
import com.cookandroid.finalproject2.BookResponse;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private BooksAdapter booksAdapter;
    private List<BookResponse.Item> books = new ArrayList<>();
    private Bundle bundle;
    private LinearLayout heartLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        bundle = new Bundle();
        heartLayout = view.findViewById(R.id.heartLayout);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        booksAdapter = new BooksAdapter(books);
        recyclerView.setAdapter(booksAdapter);

        fetchBooks();

        getParentFragmentManager().setFragmentResultListener("requestKey", getViewLifecycleOwner(), new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                String bookTitle = bundle.getString("bundleKey1");
                String bookImage = bundle.getString("bundleKey2");

                View bookView = getLayoutInflater().inflate(R.layout.book_item2, null);
                TextView bookTitleTextView = bookView.findViewById(R.id.bookTitleTextView2);
                ImageView bookImageView = bookView.findViewById(R.id.bookImageView2);

                bookTitleTextView.setText(bookTitle);
                new LoadImageTask(bookImageView).execute(bookImage);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                bookView.setLayoutParams(params);
                heartLayout.addView(bookView);
            }
        });


        return view;
    }

    private void fetchBooks() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/books/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GoogleBooksApi googleBooksApi = retrofit.create(GoogleBooksApi.class);
        Call<BookResponse> call = googleBooksApi.getBestSellers("bestsellers", "AIzaSyC3i8aajPEFvKtQ1Ly4dO_lNk99VcZFRtI"); // 여기에 API 키 입력
        call.enqueue(new Callback<BookResponse>() {
            @Override
            public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    books.clear();
                    books.addAll(response.body().getItems());
                    booksAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BookResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
