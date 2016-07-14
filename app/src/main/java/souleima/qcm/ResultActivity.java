package souleima.qcm;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ResultActivity extends AppCompatActivity {

    TextView resultText,scorePercentage,endQuizText;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Bundle b = getIntent().getExtras();
        int score = b.getInt("score");
        int quizCount= b.getInt("quizCount");

        resultText=(TextView)findViewById(R.id.resultText);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        scorePercentage=(TextView)findViewById(R.id.scorePercentage);
        endQuizText=(TextView)findViewById(R.id.endQuizText);
        ratingBar.setNumStars(5);
        ratingBar.setMax(5);

        resultText.setText(" You have answered "+score+"/"+quizCount+ " questions correct from the first trial "); //mettre dans @string??
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        Float curRate = Float.valueOf(decimalFormat.format(((float)score/(float)quizCount)));
        ratingBar.setStepSize(curRate);
        ratingBar.setRating(curRate * 5f);
        scorePercentage.setText("Your score is " + curRate*100 + " %");// mettre dans @string ??


    }

}
