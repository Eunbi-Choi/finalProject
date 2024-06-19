package com.cookandroid.finalproject2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class WriteFragment extends Fragment {

    private Bundle bundle;
    private LinearLayout searchBookLayout, recordingLayout, bookDescriptionLayout, writeLayout, recordingView, recdView;
    private Button searchBtn, writeBtn, heartBtn, closeBtn;
    private EditText bookNameEt;
    private GridLayout resultsLayout;
    private TextView searchBookText, recordingText, recordingTv;
    private String FILE_NAME;
    private String clientId = "VAaZYfdAo8KCU2G8cLMm"; // 애플리케이션 클라이언트 아이디
    private String clientSecret = "Q6tTpmLRbX"; // 애플리케이션 클라이언트 시크릿

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write, container, false);

        bundle = new Bundle();

        searchBookLayout = view.findViewById(R.id.L1);
        recordingLayout = view.findViewById(R.id.L2);
        writeLayout = view.findViewById(R.id.writeLayout);
        recordingView = view.findViewById(R.id.recordingView);
        recdView = view.findViewById(R.id.recdView);

        searchBtn = view.findViewById(R.id.searchBtn2);
        bookNameEt = view.findViewById(R.id.bookNameEt2);

        resultsLayout = view.findViewById(R.id.resultsLayout);
        bookDescriptionLayout = view.findViewById(R.id.bookDescriptionLayout);

        searchBookText = view.findViewById(R.id.searchBookText);
        recordingText = view.findViewById(R.id.recordingText);

        writeBtn = view.findViewById(R.id.writeBtn);
        heartBtn = view.findViewById(R.id.heartBtn);

        recordingTv = view.findViewById(R.id.recordingTv);
        closeBtn = view.findViewById(R.id.close_button);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordingView.setVisibility(View.GONE);
                recordingLayout.setVisibility(View.VISIBLE);
            }
        });

        searchBookText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBookLayout.setVisibility(View.VISIBLE);
                recordingLayout.setVisibility(View.GONE);
                bookDescriptionLayout.setVisibility(View.GONE);
                resultsLayout.setVisibility(View.GONE);
                writeLayout.setVisibility(View.GONE);
                recordingView.setVisibility(View.GONE);

                searchBookText.setTextColor(Color.BLACK);
                recordingText.setTextColor(Color.GRAY);
            }
        });

        recordingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBookLayout.setVisibility(View.GONE);
                recordingLayout.setVisibility(View.VISIBLE);
                resultsLayout.setVisibility(View.GONE);
                writeLayout.setVisibility(View.GONE);
                recordingView.setVisibility(View.GONE);

                searchBookText.setTextColor(Color.GRAY);
                recordingText.setTextColor(Color.BLACK);
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultsLayout.setVisibility(View.VISIBLE);
                bookDescriptionLayout.setVisibility(View.GONE);
                writeLayout.setVisibility(View.GONE);
                recordingView.setVisibility(View.GONE);

                String bookName = bookNameEt.getText().toString();
                new SearchBookTask().execute(bookName);
            }
        });

        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeLayout.setVisibility(View.VISIBLE);
                resultsLayout.setVisibility(View.GONE);
                bookDescriptionLayout.setVisibility(View.GONE);

                getParentFragmentManager().setFragmentResult("requestKey2", bundle);

                ((MainActivity) getActivity()).showCommentFragment();

                String bTitle, bImage, bAuthor;

                bTitle = bundle.getString("bundleKey1");
                bImage = bundle.getString("bundleKey2");
                bAuthor = bundle.getString("bundleKey3");

                View bookView = getLayoutInflater().inflate(R.layout.book_recording_list, null);
                TextView bookTitleTextView = bookView.findViewById(R.id.bookTitleTextView3);
                TextView bookAuthorTextView = bookView.findViewById(R.id.bookAuthor);
                ImageView bookImageView = bookView.findViewById(R.id.bookImageView3);

                bookTitleTextView.setText(bTitle);
                bookAuthorTextView.setText(bAuthor);
                new LoadImageTask(bookImageView).execute(bImage);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                bookView.setLayoutParams(params);

                bookView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        recordingLayout.setVisibility(View.GONE);
                        recordingView.setVisibility(View.VISIBLE);

                        FILE_NAME = bTitle + ".txt";

                        loadCommentFromFile();
                    }
                });
                recordingLayout.addView(bookView);
                //recdView.addView(bookView);
            }
        });

        // WriteFragment에서 heartBtn의 클릭 이벤트 설정
        heartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().setFragmentResult("requestKey", bundle);
            }
        });

        return view;
    }

    private class SearchBookTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String text = null;
            try {
                text = URLEncoder.encode(params[0], "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "Encoding error";
            }

            String apiURL = "https://openapi.naver.com/v1/search/book?query=" + text;
            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("X-Naver-Client-Id", clientId);
            requestHeaders.put("X-Naver-Client-Secret", clientSecret);
            return get(apiURL, requestHeaders);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray items = jsonObject.getJSONArray("items");

                resultsLayout.removeAllViews();

                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    String title = item.getString("title");
                    String image = item.getString("image");
                    String author = item.getString("author");
                    String publisher = item.getString("publisher");
                    String description = item.getString("description");

                    View bookView = getLayoutInflater().inflate(R.layout.book_item, null);
                    TextView bookTitleTextView = bookView.findViewById(R.id.bookTitleTextView);
                    ImageView bookImageView = bookView.findViewById(R.id.bookImageView);

                    bookTitleTextView.setText(title);
                    new LoadImageTask(bookImageView).execute(image);

                    bookView.setTag(item.toString());
                    //heartBtn.setTag(item.toString());  // heartBtn의 태그를 설정

                    bookView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            heartBtn.setTag(v.getTag());
                            String itemJson = (String) v.getTag();

                            try {
                                JSONObject item = new JSONObject(itemJson);

                                bundle.putString("bundleKey1", item.getString("title"));
                                bundle.putString("bundleKey2", item.getString("image"));
                                bundle.putString("bundleKey3", item.getString("author"));

                                bookDescriptionLayout.setVisibility(View.VISIBLE);

                                TextView title = bookDescriptionLayout.findViewById(R.id.desTitle);
                                TextView author = bookDescriptionLayout.findViewById(R.id.desAuthor);
                                TextView des = bookDescriptionLayout.findViewById(R.id.desBookIntroduce);
                                ImageView img = bookDescriptionLayout.findViewById(R.id.desBookImg);

                                title.setText(item.getString("title"));
                                author.setText(item.getString("author"));
                                des.setText(item.getString("description"));

                                new LoadImageTask(img).execute(item.getString("image"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = GridLayout.LayoutParams.WRAP_CONTENT;
                    params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                    bookView.setLayoutParams(params);

                    resultsLayout.addView(bookView);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private String get(String apiUrl, Map<String, String> requestHeaders) {
            HttpURLConnection con = connect(apiUrl);
            try {
                con.setRequestMethod("GET");
                for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                    con.setRequestProperty(header.getKey(), header.getValue());
                }

                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return readBody(con.getInputStream());
                } else {
                    return readBody(con.getErrorStream());
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "API request error";
            } finally {
                con.disconnect();
            }
        }

        private HttpURLConnection connect(String apiUrl) {
            try {
                URL url = new URL(apiUrl);
                return (HttpURLConnection) url.openConnection();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                throw new RuntimeException("API URL is incorrect: " + apiUrl, e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Connection failed: " + apiUrl, e);
            }
        }

        private String readBody(InputStream body) {
            InputStreamReader streamReader = new InputStreamReader(body);
            try (BufferedReader lineReader = new BufferedReader(streamReader)) {
                StringBuilder responseBody = new StringBuilder();
                String line;
                while ((line = lineReader.readLine()) != null) {
                    responseBody.append(line);
                }
                return responseBody.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return "Reading response error";
            }
        }
    }

    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new URL(imageUrl).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            } else {
                imageView.setImageResource(R.drawable.ic_launcher_foreground); // 이미지가 없을 때 표시할 기본 이미지
            }
        }
    }

    public void loadCommentFromFile() {
        FileInputStream fis = null;
        StringBuilder sb = new StringBuilder();
        try {
            fis = getActivity().openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            recordingTv.setText(sb.toString());
            Log.d("file_name", sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
