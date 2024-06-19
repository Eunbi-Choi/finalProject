package com.cookandroid.finalproject2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import java.io.FileOutputStream;
import java.io.IOException;

public class CommentFragment extends Fragment {
    private EditText commentEditText;
    private String FILE_NAME;

    public CommentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        commentEditText = view.findViewById(R.id.comment_edit_text);
        commentEditText.setText("");

        getParentFragmentManager().setFragmentResultListener("requestKey2", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                FILE_NAME = bundle.getString("bundleKey1") + ".txt";
            }
        });

        Button closeButton = view.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle back navigation
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        Button storeButton = view.findViewById(R.id.comment_submit_button);
        storeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCommentToFile();
            }
        });
        return view;
    }

    private void saveCommentToFile() {
        String comment = commentEditText.getText().toString();
        FileOutputStream fos = null;
        try {
            fos = getActivity().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(comment.getBytes());
            Toast.makeText(getActivity(), "저장 완료", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "오류", Toast.LENGTH_SHORT).show();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void clearTextView() {
        if (commentEditText != null) {
            commentEditText.setText("");
        }
    }
}

