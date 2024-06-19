package com.cookandroid.finalproject2;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    HomeFragment homeFragment;
    WriteFragment writeFragment;
    ReadingFragment readingFragment;
    MypageFragment mypageFragment;
    Fragment activeFragment;
    CommentFragment commentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("책갈피");
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();
        writeFragment = new WriteFragment();
        readingFragment = new ReadingFragment();
        mypageFragment = new MypageFragment();
        commentFragment = new CommentFragment();
        activeFragment = homeFragment;

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.main_container, homeFragment, "HomeFragmentTag").commit();
        fragmentManager.beginTransaction().add(R.id.main_container, writeFragment, "WriteFragmentTag").hide(writeFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main_container, readingFragment, "ReadingFragmentTag").hide(readingFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main_container, mypageFragment, "MypageFragmentTag").hide(mypageFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main_container, commentFragment, "CommentFragment").hide(commentFragment).commit();

        setBottomNavigationView(bottomNavigationView);

        // 앱 초기 실행 시 홈화면으로 설정
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.fragment_home);
        }
    }

    private void setBottomNavigationView(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                int itemId = item.getItemId();
                if (itemId == R.id.fragment_home) {
                    selectedFragment = homeFragment;
                } else if (itemId == R.id.fragment_reading) {
                    selectedFragment = readingFragment;
                } else if (itemId == R.id.fragment_write) {
                    selectedFragment = writeFragment;
                } else if (itemId == R.id.fragment_mypage) {
                    selectedFragment = mypageFragment;
                }

                if (selectedFragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.hide(activeFragment).hide(commentFragment).show(selectedFragment).commit();

                    activeFragment = selectedFragment;
                }
                return true;
            }
        });
    }

    public void showCommentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .hide(writeFragment)
                .show(commentFragment)
                .addToBackStack(null)
                .commit(); // Add transaction to back stack

        activeFragment = commentFragment;

        commentFragment = (CommentFragment) fragmentManager.findFragmentByTag("CommentFragment");
        if (commentFragment != null) {
            commentFragment.clearTextView();
        }
    }
}
