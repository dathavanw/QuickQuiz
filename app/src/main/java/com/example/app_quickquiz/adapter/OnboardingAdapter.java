package com.example.app_quickquiz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.R;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {


    // thay ảnh và nội dung rồi chạy thôi nha 
    private final int[] images = {R.drawable.onboarding3, R.drawable.onboarding4, R.drawable.onboarding1, R.drawable.onboarding2, R.drawable.onboarding5};

    private final String[] text_title = {
            "Welcome",
            "Quick Quizzes",
            "Fast Assignments",
            "Instant Results",
            "Smart Learning",
    };

    private final String[] texts = {
            "QuickQuiz is a smart learning platform that connects teachers and students through a flexible quiz system.",
            "Students can take multiple choice exercises right on their phones or tablets with a clear, easy-to-use interface.\n"+ "The application will automatically grade and display results immediately after submission.",
            "Teachers can create quizzes, distribute assignments to individual classes or groups of students with just a few simple steps.\n" + "Support quick question entry, customize test time and view instant result statistics.",
            "Both teachers and students can review detailed results for each question, helping to accurately assess ability and improve direction.",
            "QuickQuiz is committed to providing a convenient, stable and reliable learning experience for every user.",
    };

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout for each onboarding screen
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.onboarding_screen, parent, false);
        return new OnboardingViewHolder(view);
    }
   @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        // Set image and text for each screen
        holder.imageView.setImageResource(images[position]);
        holder.textViewContent.setText(texts[position]);
        holder.textViewKeyWord.setText(text_title[position]);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public static class OnboardingViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewContent, textViewKeyWord ;

        public OnboardingViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.onboarding_image);
            textViewContent = itemView.findViewById(R.id.onboarding_text);
            textViewKeyWord = itemView.findViewById(R.id.onboarding_text_title);
        }
    }
}


