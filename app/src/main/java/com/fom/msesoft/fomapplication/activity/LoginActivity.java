package com.fom.msesoft.fomapplication.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import org.androidannotations.annotations.UiThread;

import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.model.Person;
import com.fom.msesoft.fomapplication.repository.PersonRepository;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import java.io.IOException;
import java.io.Serializable;


@EActivity(R.layout.activity_login)
@Fullscreen
public class LoginActivity extends AppCompatActivity {
    public Person person;

    @RestService
    PersonRepository personRepository;

    @ViewById(R.id.progress_bar_login)
    ProgressBar progressBar;

    @ViewById(R.id.input_layout_email)
    TextInputLayout inputLayoutEmail;

    @ViewById(R.id.input_email)
    EditText inputEmail;

    @AfterTextChange(R.id.input_email)
    void onTextChancedInputEmail(){
        validateEmail();
    }

    @ViewById(R.id.input_layout_login_password)
    TextInputLayout inputLayoutLoginPassword;

    @ViewById(R.id.input_login_password)
    EditText password;

    @ViewById(R.id.btn_signin)
    Button sign;

    @Click(R.id.btn_signin)
    void btn_signin(){
        sign(inputEmail.getText().toString(),password.getText().toString());
    }

    @Background
    public void sign(String email, String password) {
        preExecute();
        person = personRepository.signIn(email,password);
        chechSign(person);
    }
    @UiThread
    void preExecute(){
        CharSequence cs="";
        sign.setText(cs);

        progressBar.setVisibility(View.VISIBLE);
    }

    @UiThread
    void chechSign (Person person) {
        if(person!=null) {
            Intent intent = new Intent(this, MainActivity_.class);
            intent.putExtra("person",person);
            startActivity(intent);
            this.finish();
            registerGCM();
            progressBar.setVisibility(View.GONE);

        }
    }
    @Background
    void registerGCM () {
        GoogleCloudMessaging gcm = null;//Google Cloud referansı
        String PROJECT_ID = "369106327986";
        String regid = null;


        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(this);//GCM objesi oluşturduk ve gcm referansına bağladık
            }
            regid = gcm.register(PROJECT_ID);//gcm objesine PROJECT_ID mizi göndererek regid değerimizi aldık.Bu değerimizi hem sunucularımıza göndereceğiz Hemde Androidde saklıyacağız

        }

        catch (IOException e) {
            e.printStackTrace();
        }

        personRepository.registerGCM(person.getUniqueId(),regid);


    }



    @AfterTextChange(R.id.input_email)
    void onTextChancedInputName(){
        validateEmail();
    }


    @AfterTextChange(R.id.input_login_password)
    void onTextChancedInputPassword(){
        validatePassword();
    }

    private void submitForm() {


        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }

        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
    }


    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (password.getText().toString().trim().isEmpty()) {
            inputLayoutLoginPassword.setError(getString(R.string.err_msg_password));
            requestFocus(password);
            return false;
        } else {
            inputLayoutLoginPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
