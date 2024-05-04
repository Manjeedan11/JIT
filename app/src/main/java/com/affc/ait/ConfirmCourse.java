package com.affc.ait;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.affc.ait.db.DatabaseHandler;
import com.affc.ait.models.Branch;
import com.affc.ait.models.Course;
import com.affc.ait.models.Student;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ConfirmCourse extends AppCompatActivity {

    private String body;
    private Course course;
    private Branch branch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_course);

        DatabaseHandler databaseHandler = new DatabaseHandler(this);

        TextView container = findViewById(R.id.confirm_course_text);

        Intent intent = getIntent();
        branch = databaseHandler.getBranchByID(intent.getIntExtra("branch_ID", -1));
        course = databaseHandler.getCourseByID(intent.getIntExtra("course_ID", -1));

        body= "Course: " + course.getCourse_name() + "\n" +
                "Branch: " + branch.getBranch_name() + "\n" +
                "Start Date: " + course.getStart_date() + "\n" +
                "End Date: " + course.getEnd_date() + "\n" +
                "Fee: " + course.getFee() + "\n" +
                "Description: " + course.getDescription() + "\n" +
                "Max Participants: " + course.getMax_p() + "\n" +
                "Branch Address: " + branch.getLocation() + "\n";

        container.setText(body);
    }

    public void performEnroll(View view) {
        EditText codeField = findViewById(R.id.code);
        String code = codeField.getText().toString();
        if (code.equals("M563432")) {
            body = body + "Discounted Fee(25%): " +   (course.getFee() *1.25);
        }
        else if(code.equals("S663435")) {
            body = body + "Discounted Fee(40%): " +   (course.getFee() *1.4);

        }
        else if (code.equals("L763434")) {
            body = body + "Discounted Fee(60%): " +   (course.getFee() *1.6);

        }
        else {
            Toast.makeText(this, "invalid code", Toast.LENGTH_SHORT).show();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);

        // Send the generated code to the email using AsyncTask
        new SendEmailTask().execute();
    }


    // AsyncTask to send email in background
    private class SendEmailTask extends AsyncTask<Integer, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... params) {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String userEmail = sharedPreferences.getString("email", null);
            String stringSenderEmail = "isuruyahampath1@gmail.com";
            String stringReceiverEmail = userEmail; // Retrieve email from SharedPreferences
            String stringPasswordSenderEmail = "efzn qwof iaea qgmw";

            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();

            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            try {
                MimeMessage mimeMessage = new MimeMessage(session);
                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

                mimeMessage.setSubject("Enrollment Confirmation");
                mimeMessage.setText(body);
                Transport.send(mimeMessage);

                return true;

            } catch (AddressException e) {
                Log.e("E", "ERROR ADDRESS EXCEPTION");
                e.printStackTrace();
            } catch (MessagingException e) {
                Log.e("E", "ERROR MESSAGE EXCEPTION");
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(ConfirmCourse.this, "Code Sent Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ConfirmCourse.this, "Failed to send code", Toast.LENGTH_SHORT).show();
            }
        }
    }



}