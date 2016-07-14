package souleima.qcm;

import android.app.ProgressDialog;

import android.content.Intent;
import android.content.pm.ActivityInfo;

import android.os.AsyncTask;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;

import android.widget.RadioButton;

import android.widget.RadioGroup;

import android.widget.TextView;

import android.widget.Toast;

import org.apache.http.HttpResponse;

import org.apache.http.client.ClientProtocolException;

import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpPost;

import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.params.BasicHttpParams;

import org.json.JSONArray;

import org.json.JSONException;

import org.json.JSONObject;

import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.util.ArrayList;

import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private TextView quizQuestion;

    private RadioGroup radioGroup;

    private RadioButton optionOne;

    private RadioButton optionTwo;

    private RadioButton optionThree;

    private RadioButton optionFour;

    private int currentQuizQuestion;

    private int quizCount;

    private int score;

    private boolean firstTrial=true;

    private QuizWrapper firstQuestion;

    private List<QuizWrapper> parsedObject;


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quiz);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        quizQuestion = (TextView)findViewById(R.id.quiz_question);

        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);

        optionOne = (RadioButton)findViewById(R.id.radio0);

        optionTwo = (RadioButton)findViewById(R.id.radio1);

        optionThree = (RadioButton)findViewById(R.id.radio2);

        optionFour = (RadioButton)findViewById(R.id.radio3);

        Button previousButton = (Button)findViewById(R.id.previousquiz);

        Button nextButton = (Button)findViewById(R.id.nextquiz);

        final Button btHint =(Button)findViewById(R.id.btHint);

        AsyncJsonObject asyncObject = new AsyncJsonObject();

        asyncObject.execute("");

        btHint.setVisibility(View.GONE);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioSelected = radioGroup.getCheckedRadioButtonId();
                int userSelection = getSelectedAnswer(radioSelected);

                int correctAnswerForQuestion = firstQuestion.getCorrectAnswer();

                if(userSelection == correctAnswerForQuestion){
                    // correct answer
                    if (firstTrial) { score ++; }
                    Toast.makeText(QuizActivity.this, "You got the answer correct", Toast.LENGTH_LONG).show();
                    currentQuizQuestion++;
                    /*if(currentQuizQuestion >= quizCount){
                        Toast.makeText(QuizActivity.this, "End of the Quiz Questions. You got "+score+" questions correct from the first trial", Toast.LENGTH_LONG).show();
                        return;
                    }*/

                    if(currentQuizQuestion >= quizCount){
                        Toast.makeText(QuizActivity.this, "End of the Quiz Questions", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                        Bundle b = new Bundle();
                        b.putInt("score", score);//Your score
                        b.putInt("quizCount",quizCount);
                        intent.putExtras(b); //Put your score to your next Intent
                        startActivity(intent);
                        finish();

                    }


                    else{
                        firstTrial=true;
                        firstQuestion = parsedObject.get(currentQuizQuestion);
                        quizQuestion.setText("Question " +( currentQuizQuestion +1 )+": "+firstQuestion.getQuestion());
                        String[] possibleAnswers = firstQuestion.getAnswers().split(",");
                        uncheckedRadioButton();
                        optionOne.setText(possibleAnswers[0]);
                        optionTwo.setText(possibleAnswers[1]);
                        optionThree.setText(possibleAnswers[2]);
                        optionFour.setText(possibleAnswers[3]);
                    }
                }
                else{
                    // failed question
                    firstTrial=false;
                    btHint.setVisibility(View.VISIBLE);
                    return;
                }
            }
        });
        btHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] possibleAnswers = firstQuestion.getAnswers().split(",");
                Toast.makeText(QuizActivity.this, "the correct answer is : "+possibleAnswers[firstQuestion.getCorrectAnswer()-1] +" \n try harder !!" ,
                        Toast.LENGTH_LONG).show();
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                currentQuizQuestion--;

                if(currentQuizQuestion < 0){

                    return;

                }

                uncheckedRadioButton();

                firstQuestion = parsedObject.get(currentQuizQuestion);

                quizQuestion.setText(firstQuestion.getQuestion());

                String[] possibleAnswers = firstQuestion.getAnswers().split(",");

                optionOne.setText(possibleAnswers[0]);

                optionTwo.setText(possibleAnswers[1]);

                optionThree.setText(possibleAnswers[2]);

                optionFour.setText(possibleAnswers[3]);

            }

        });

    }



    private class AsyncJsonObject extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog;

        @Override

        protected String doInBackground(String... params) {

            HttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());

            HttpPost httpPost = new HttpPost("http://qodorat.co.nf/index.php");

            String jsonResult = "";

            try {

                HttpResponse response = httpClient.execute(httpPost);

                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();

                System.out.println("Returned Json object " + jsonResult.toString());

            } catch (ClientProtocolException e) {

// TODO Auto-generated catch block

                e.printStackTrace();

            } catch (IOException e) {

// TODO Auto-generated catch block

                e.printStackTrace();

            }

            return jsonResult;

        }

        @Override

        protected void onPreExecute() {

// TODO Auto-generated method stub

            super.onPreExecute();

            progressDialog = ProgressDialog.show(QuizActivity.this, "Downloading Quiz","Wait....", true);

        }

        @Override

        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            progressDialog.dismiss();

            System.out.println("Resulted Value: " + result);

            parsedObject = returnParsedJsonObject(result);

            if(parsedObject == null){

                return;

            }

            quizCount = parsedObject.size();

            firstQuestion = parsedObject.get(0);

            quizQuestion.setText(firstQuestion.getQuestion());

            String[] possibleAnswers = firstQuestion.getAnswers().split(",");

            optionOne.setText(possibleAnswers[0]);

            optionTwo.setText(possibleAnswers[1]);

            optionThree.setText(possibleAnswers[2]);

            optionFour.setText(possibleAnswers[3]);

        }

        private StringBuilder inputStreamToString(InputStream is) {

            String rLine = "";

            StringBuilder answer = new StringBuilder();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            try {

                while ((rLine = br.readLine()) != null) {

                    answer.append(rLine);

                }

            } catch (IOException e) {

// TODO Auto-generated catch block

                e.printStackTrace();

            }

            return answer;

        }

    }

    private List<QuizWrapper> returnParsedJsonObject(String result){

        List<QuizWrapper> jsonObject = new ArrayList<QuizWrapper>();

        JSONObject resultObject = null;

        JSONArray jsonArray = null;

        QuizWrapper newItemObject = null;

        try {

            resultObject = new JSONObject(result);

            System.out.println("Testing the water " + resultObject.toString());

            jsonArray = resultObject.optJSONArray("quiz_questions");

        } catch (JSONException e) {

            e.printStackTrace();

        }

        for(int i = 0; i < jsonArray.length(); i++){

            JSONObject jsonChildNode = null;

            try {

                jsonChildNode = jsonArray.getJSONObject(i);

                int id = jsonChildNode.getInt("id");

                String question = jsonChildNode.getString("question");

                String answerOptions = jsonChildNode.getString("possible_answers");

                int correctAnswer = jsonChildNode.getInt("correct_answer");

                newItemObject = new QuizWrapper(id, question, answerOptions, correctAnswer);

                jsonObject.add(newItemObject);

            } catch (JSONException e) {

                e.printStackTrace();

            }

        }

        return jsonObject;

    }
//get the selected answer
    private int getSelectedAnswer(int radioSelected){

        int answerSelected = 0;

        if(radioSelected == R.id.radio0){

            answerSelected = 1;

        }

        if(radioSelected == R.id.radio1){

            answerSelected = 2;

        }

        if(radioSelected == R.id.radio2){

            answerSelected = 3;

        }

        if(radioSelected == R.id.radio3){

            answerSelected = 4;

        }

        return answerSelected;

    }
//uncheck the radio buttons
    private void uncheckedRadioButton(){

        optionOne.setChecked(false);

        optionTwo.setChecked(false);

        optionThree.setChecked(false);

        optionFour.setChecked(false);

    }

}