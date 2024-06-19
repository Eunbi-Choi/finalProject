package com.cookandroid.finalproject2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MypageFragment extends Fragment {
    CalendarView calView;
    private ImageView bookImageView;
    private LinearLayout bookImageLayout;
    private TextView profileText;
    private EditText profileEditText;
    private Button editButton;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage, container, false);

        calView = view.findViewById(R.id.calendarView);
        bookImageLayout = view.findViewById(R.id.bookImageLayout);

        profileEditText = view.findViewById(R.id.profileEditText);
        profileText = view.findViewById(R.id.profileText);
        editButton = view.findViewById(R.id.editBtn);

        // 캘린더 날짜 선택 이벤트 처리
        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                getParentFragmentManager().setFragmentResultListener("requestKey2", getViewLifecycleOwner(), new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        String bookImage = bundle.getString("bundleKey2");
                        Log.d("cal  bundle", bookImage);

                        View bookView = getLayoutInflater().inflate(R.layout.book_item2, null);
                        ImageView bookImageView = bookView.findViewById(R.id.bookImageView2);

                        new LoadImageTask(bookImageView).execute(bookImage);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                        bookView.setLayoutParams(params);
                        bookImageLayout.addView(bookView);
                    }
                });
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TextView를 숨기고 EditText를 보이도록 변경
                profileText.setVisibility(View.GONE);
                profileEditText.setVisibility(View.VISIBLE);

                // EditText에 기존 TextView의 텍스트 설정
                String currentText = profileText.getText().toString();
                profileEditText.setText(currentText);

                // 커서를 텍스트의 끝으로 이동
                profileEditText.setSelection(profileEditText.getText().length());
            }
        });

        // ProfileEditActivity.java에서 수정 버튼 아래에 추가
        Button saveButton = view.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EditText에서 수정된 텍스트 가져오기
                String editedText = profileEditText.getText().toString();

                // 수정된 텍스트를 TextView에 설정
                profileText.setText(editedText);

                // TextView를 보이게 하고 EditText를 숨기기
                profileText.setVisibility(View.VISIBLE);
                profileEditText.setVisibility(View.GONE);

                // 저장 처리 등 추가 작업 수행
                // 예: 데이터베이스에 저장하거나 다른 처리 수행
            }
        });

        return view;
    }
}
