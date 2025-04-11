package com.example.quickquizapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quickquizapp.R;
import com.example.quickquizapp.fragment.HomeFragment;

public class NavigationMenuActivity extends AppCompatActivity {

    private ImageButton btnMenu;
    private LinearLayout menuItemsLayout, menuLayout;
    private boolean isMenuVisible = false;

    private LinearLayout itemSearch, itemActivity, itemHome, itemClass, itemAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_menu);

        btnMenu = findViewById(R.id.btnToggleMenu);
        menuItemsLayout = findViewById(R.id.menuItemsLayout);
        menuLayout = findViewById(R.id.menuLayout);

        itemSearch = findViewById(R.id.itemSearch);
        itemActivity = findViewById(R.id.itemActivity);
        itemHome = findViewById(R.id.itemHome);
        itemClass = findViewById(R.id.itemClass);
        itemAccount = findViewById(R.id.itemAccount);

        menuItemsLayout.setVisibility(View.GONE);
        menuItemsLayout.setTranslationX(-menuItemsLayout.getWidth());

        btnMenu.setOnClickListener(v -> {
            if (!isMenuVisible) {
                menuItemsLayout.setVisibility(View.VISIBLE);
                menuItemsLayout.setAlpha(0f);
                menuItemsLayout.setTranslationX(-menuItemsLayout.getWidth());
                menuItemsLayout.animate()
                        .alpha(1f)
                        .translationX(0)
                        .setDuration(300)
                        .start();
                isMenuVisible = true;
            } else {
                menuItemsLayout.animate()
                        .alpha(0f)
                        .translationX(-menuItemsLayout.getWidth())
                        .setDuration(300)
                        .withEndAction(() -> menuItemsLayout.setVisibility(View.GONE))
                        .start();
                isMenuVisible = false;
            }
        });

        itemSearch.setOnClickListener(v -> startActivity(new Intent(this, SearchActivity.class)));
        itemActivity.setOnClickListener(v -> startActivity(new Intent(this, HoatDongActivity.class)));
        itemHome.setOnClickListener(v -> startActivity(new Intent(this, HomeFragment.class)));
        itemClass.setOnClickListener(v -> startActivity(new Intent(this, ClassListActivity.class)));
        itemAccount.setOnClickListener(v -> startActivity(new Intent(this, EditProfileActivity.class)));
    }
}
