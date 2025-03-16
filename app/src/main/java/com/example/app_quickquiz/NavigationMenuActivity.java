package com.example.app_quickquiz;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class NavigationMenuActivity extends AppCompatActivity {

    private ImageButton btnMenu;
    private LinearLayout menuItemsLayout, menuLayout;
    private boolean isMenuVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_menu);

        btnMenu = findViewById(R.id.btnToggleMenu);
        menuItemsLayout = findViewById(R.id.menuItemsLayout);
        menuLayout = findViewById(R.id.menuLayout);

        //menuLayout.setVisibility(View.GONE);
        // Đặt ban đầu ẩn menu và dịch nó ra ngoài trái
        menuItemsLayout.setVisibility(View.GONE);
        menuItemsLayout.setTranslationX(-menuItemsLayout.getWidth());

        btnMenu.setOnClickListener(v -> {
            if (!isMenuVisible) {
                // Hiện menu với hiệu ứng mượt
                menuItemsLayout.setVisibility(View.VISIBLE);
                menuItemsLayout.setAlpha(0f);

                menuLayout.setVisibility(View.VISIBLE);

                menuItemsLayout.setTranslationX(-menuItemsLayout.getWidth()); // slide từ trái vào
                menuItemsLayout.animate()
                        .alpha(1f)
                        .translationX(0)
                        .setDuration(300)
                        .start();
                isMenuVisible = true;
            } else {
                // Ẩn menu với hiệu ứng mượt
                menuItemsLayout.animate()
                        .alpha(0f)
                        .translationX(-menuItemsLayout.getWidth())
                        .setDuration(300)
                        .withEndAction(() ->{
                            menuItemsLayout.setVisibility(View.GONE);
                           // menuLayout.setVisibility(View.GONE);
                        } )
                        .start();
                isMenuVisible = false;
            }
        });
    }
}
