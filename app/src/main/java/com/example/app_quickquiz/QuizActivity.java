package com.example.app_quickquiz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_quickquiz.activity.HistoryActivity;
import com.example.app_quickquiz.adapter.QuestionWithAnswersAdapter;
import com.example.app_quickquiz.database.Database;
import com.example.app_quickquiz.model.Answer;
import com.example.app_quickquiz.model.QuestionWithAnswers;
import com.example.app_quickquiz.model.User_Answers;
import com.example.app_quickquiz.model.User_Quiz_Results;
import com.example.app_quickquiz.repository.QuizRepository;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    // private QuestionAdapter adapter;
    private QuizRepository quizRepository;
    private CountDownTimer countDownTimer;
    private TextView timerTextView;
    private Button btnSubmit;

    private String quiz_id;
    private String userId3 ;
    private List<QuestionWithAnswers> questionList;
    private String quizResultId; // Khai báo biến ở mức lớp

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_dat);

        quizRepository = new QuizRepository();
        timerTextView = findViewById(R.id.timerTextView);
        btnSubmit = findViewById(R.id.btnsubmit);
        btnSubmit.setOnClickListener(v -> validateAnswersBeforeSubmit());



        recyclerView = findViewById(R.id.rvQuestions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        userId3 = preferences.getString("userId", null);
        Log.d("USER_ID TỪ SHARED_PREFERENCES", "Giá Trị: " + userId3);




        quiz_id = getIntent().getStringExtra("quizId");
        int time_limit = getIntent().getIntExtra("timeLimit", 0);

        Log.d("QuizActivity", "Quiz ID: " + quiz_id);
        Log.d("QuizActivity", "Time Limit: " + time_limit);


        // Gọi dữ liệu
        quizRepository.getQuestionsWithAnswersByQuizId(quiz_id, new Database.OnGetDataListener<List<QuestionWithAnswers>>() {
            @Override
            public void onSuccess(List<QuestionWithAnswers> questionWithAnswersList) {
                QuestionWithAnswersAdapter adapter = new QuestionWithAnswersAdapter(QuizActivity.this, questionWithAnswersList);
                recyclerView.setAdapter(adapter);
                startCountdown(time_limit);  // Bắt đầu đếm ngược
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(QuizActivity.this, "Lỗi khi tải câu hỏi", Toast.LENGTH_SHORT).show();
            }
        });




    }


    private void startCountdown(int timeLimitInMinutes) {
        timerTextView = findViewById(R.id.timerTextView);
        long totalTimeInMillis = timeLimitInMinutes * 60 * 1000;

        countDownTimer = new CountDownTimer(totalTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                timerTextView.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
            }


            @Override
            public void onFinish() {
                Log.d("Countdown", "Hết giờ! onFinish() được gọi.");
                SubmitQuiz(); // tự động nộp bài
            }
        };
        countDownTimer.start();
    }








    public void SubmitQuiz(){
        // Bước 1: Lấy danh sách đáp án mà người dùng đã chọn từ RecyclerView



        QuestionWithAnswersAdapter adapter = (QuestionWithAnswersAdapter) recyclerView.getAdapter();

        if (adapter != null) {
            List<QuestionWithAnswers> questionsFromUI = getQuestionsFromUI(recyclerView, adapter);

            // Gán danh sách câu hỏi vào questionList
            setQuestionListFromRecyclerView(questionsFromUI);

            // Xử lý tiếp theo (ví dụ: gửi danh sách câu trả lời lên backend)
            List<User_Answers> userAnswers = getUserAnswersFromRecyclerView();
            quizRepository.submitQuizResults(userId3, quiz_id, userAnswers);
            // Nộp bài và hiển thị điểm
            quizRepository.calculateScore(userAnswers, new QuizRepository.ScoreCallback() {
                @Override
                public void onScoreCalculated(int score) {
                    // Hiển thị dialog khi tính điểm xong
                    showSubmissionDialog(QuizActivity.this, score);
                }
            });

        } else {
            Log.e("Debug", "Adapter is null, cannot collect questions from UI!");
        }
        //   List<User_Answers> userAnswers = getUserAnswersFromRecyclerView();
        // Bước 2: Gửi danh sách đáp án đến Repository để xử lý
        //   quizRepository.submitQuizResults(userId, quiz_id, userAnswers);

    }



    private List<User_Answers> getUserAnswersFromRecyclerView() {
        // Tạo quizResultId duy nhất
        quizResultId = FirebaseDatabase.getInstance().getReference("User_Quiz_Results").push().getKey();
        Log.d("Debug", "Generated quizResultId: " + quizResultId);

        // Kiểm tra danh sách câu hỏi
        if (questionList == null || questionList.isEmpty()) {
            Log.e("Debug", "questionList is null or has no questions!");
            return new ArrayList<>(); // Trả về danh sách rỗng để tránh null
        }

        // Khởi tạo danh sách userAnswers
        List<User_Answers> userAnswers = new ArrayList<>();
        Log.d("Debug", "questionList size: " + questionList.size());

        for (QuestionWithAnswers questionWithAnswers : questionList) {
            if (questionWithAnswers != null) {
                String selectedAnswerId = questionWithAnswers.getSelectedAnswerId();
                if (selectedAnswerId != null) {
                    // Tạo đối tượng User_Answers và thêm vào danh sách
                    userAnswers.add(new User_Answers(
                            userId3,
                            questionWithAnswers.getQuestion().getId(),
                            selectedAnswerId,
                            quizResultId
                    ));
                    Log.d("Debug", "Added User_Answers: userId=" + userId3
                            + ", questionId=" + questionWithAnswers.getQuestion().getId()
                            + ", selectedAnswerId=" + selectedAnswerId
                            + ", quizResultId=" + quizResultId);
                } else {
                    Log.e("Debug", "selectedAnswerId is null for question: "
                            + questionWithAnswers.getQuestion());
                }
            } else {
                Log.e("Debug", "questionWithAnswers is null!");
            }
        }

        return userAnswers; // Trả về danh sách đã kiểm tra
    }








    public List<QuestionWithAnswers> getQuestionsFromUI(RecyclerView recyclerView, QuestionWithAnswersAdapter adapter) {
        List<QuestionWithAnswers> questionsFromUI = new ArrayList<>(); // Danh sách câu hỏi và đáp án

        // Lặp qua tất cả các phần tử trong Adapter
        for (int i = 0; i < adapter.getItemCount(); i++) {
            QuestionWithAnswers qwa = adapter.getQuestionAtPosition(i); // Lấy đối tượng từ Adapter
            questionsFromUI.add(qwa); // Thêm vào danh sách
        }

        Log.d("Debug", "Collected questions from UI, size: " + questionsFromUI.size());
        return questionsFromUI; // Trả về danh sách
    }


    public void setQuestionListFromRecyclerView(List<QuestionWithAnswers> questionsFromUI) {
        if (questionsFromUI != null && !questionsFromUI.isEmpty()) {
            questionList = questionsFromUI; // Gán dữ liệu từ giao diện
            Log.d("Debug", "QuestionList updated, size: " + questionList.size());
        } else {
            Log.e("Debug", "questionsFromUI is null or empty! QuestionList not updated.");
            // Khởi tạo danh sách rỗng nếu không có dữ liệu
            questionList = new ArrayList<>();
        }
    }


    private void showSubmissionDialog(Context context, int score) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Nộp bài thành công!");
        builder.setMessage("Điểm của bạn: " + score + "\nCảm ơn bạn đã tham gia bài kiểm tra.");

        builder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss(); // Đóng dialog
            Intent intent = new Intent(context, StudentActivity.class);
            context.startActivity(intent);
        });

        builder.setNegativeButton("Xem lại bài làm", (dialog, which) -> {
            Intent intent = new Intent(context, HistoryActivity.class);  // chuyển sang tab History
            context.startActivity(intent);
        });

        builder.setCancelable(false); // Không cho phép đóng dialog bằng cách nhấn bên ngoài
        AlertDialog dialog = builder.create();
        dialog.show();
    }




    private boolean allAnswersSelected() {
        for (QuestionWithAnswers question : questionList) {
            // Kiểm tra xem câu hỏi đã có đáp án được chọn chưa
            if (question.getSelectedAnswerId() == null || question.getSelectedAnswerId().isEmpty()) {
                return false; // Có ít nhất một câu hỏi chưa được trả lời
            }
        }
        return true; // Tất cả các câu hỏi đã được trả lời
    }
    private void validateAnswersBeforeSubmit() {
        QuestionWithAnswersAdapter adapter = (QuestionWithAnswersAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            setQuestionListFromRecyclerView(getQuestionsFromUI(recyclerView, adapter));

            if (!allAnswersSelected()) {
                Toast.makeText(this, "Vui lòng chọn đáp án cho tất cả các câu hỏi trước khi nộp bài!", Toast.LENGTH_LONG).show();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có chắc chắn muốn nộp bài không?")
                        .setPositiveButton("Nộp bài", (dialog, which) -> SubmitQuiz())
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        } else {
            Toast.makeText(this, "Không lấy được danh sách câu hỏi!", Toast.LENGTH_SHORT).show();
        }
    }



}