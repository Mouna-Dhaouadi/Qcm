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
import java.util.Random;

public class Repeating_activity extends AppCompatActivity {

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

        setContentView(R.layout.activity_repeating_activity);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        quizQuestion = (TextView)findViewById(R.id.quiz_question_rep);

        radioGroup = (RadioGroup)findViewById(R.id.radioGroup_rep);

        optionOne = (RadioButton)findViewById(R.id.radio0_rep);

        optionTwo = (RadioButton)findViewById(R.id.radio1_rep);

        optionThree = (RadioButton)findViewById(R.id.radio2_rep);

        optionFour = (RadioButton)findViewById(R.id.radio3_rep);

        Button nextButton = (Button)findViewById(R.id.check_rep);

        final Button btHint =(Button)findViewById(R.id.btHint_rep);

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
                    Toast.makeText(Repeating_activity.this, "You got the answer correct", Toast.LENGTH_LONG).show();
                    currentQuizQuestion++;
                    if(currentQuizQuestion >= 1){
                        Toast.makeText(Repeating_activity.this, "End of the Quiz Questions. You got "+score+" questions correct from the first trial", Toast.LENGTH_LONG).show();
                        return;
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
                Toast.makeText(Repeating_activity.this, "the correct answer is : "+
                        possibleAnswers[firstQuestion.getCorrectAnswer()-1] +" \n try harder !!" ,
                        Toast.LENGTH_LONG).show();
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

            progressDialog = ProgressDialog.show(Repeating_activity.this, "Downloading Quiz","Wait....", true);

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

            int min = 0;
            int max = quizCount;

            Random r = new Random();
            int i = r.nextInt(max - min + 1) + min;

            firstQuestion = parsedObject.get(i);

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

        if(radioSelected == R.id.radio0_rep){

            answerSelected = 1;

        }

        if(radioSelected == R.id.radio1_rep){

            answerSelected = 2;

        }

        if(radioSelected == R.id.radio2_rep){

            answerSelected = 3;

        }

        if(radioSelected == R.id.radio3_rep){

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