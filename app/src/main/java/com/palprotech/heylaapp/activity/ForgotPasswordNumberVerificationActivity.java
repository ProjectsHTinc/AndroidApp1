package com.palprotech.heylaapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.customview.CustomOtpEditText;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.CommonUtils;
import com.palprotech.heylaapp.utils.HeylaAppConstants;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by Admin on 24-10-2017.
 */

public class ForgotPasswordNumberVerificationActivity extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener {

    private static final String TAG = ForgotPasswordNumberVerificationActivity.class.getName();

    private ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
    private Button btnSubmit;
    private TextView txtResend;
    private CustomOtpEditText otpEditText;
    private String mobileNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_number_verification);

        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        mobileNo = getIntent().getStringExtra("mobile_no");

        btnSubmit = (Button) findViewById(R.id.sendcode);
        btnSubmit.setOnClickListener(this);

        txtResend = (TextView) findViewById(R.id.resend);
        txtResend.setOnClickListener(this);

        otpEditText = (CustomOtpEditText) findViewById(R.id.otp_view);
    }

    @Override
    public void onClick(View v) {

        if (CommonUtils.isNetworkAvailable(getApplicationContext())) {
            if (v == btnSubmit) {
                if (otpEditText.hasValidOTP()) {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(HeylaAppConstants.PARAMS_MOBILE_NUMBER, mobileNo);
                        jsonObject.put(HeylaAppConstants.PARAMS_OTP, otpEditText.getOTP());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                    String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.FORGOT_PASSWORD_OTP_REQUEST;
                    serviceHelper.makeGetServiceCall(jsonObject.toString(), url);

                } else {
                    AlertDialogHelper.showSimpleAlertDialog(this, "Invalid OTP");
                }

            } else if (v == txtResend) {

                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put(HeylaAppConstants.PARAMS_MOBILE_NUMBER, mobileNo);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
                String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.RESEND_OTP_REQUEST;
                serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
            }
        } else {
            AlertDialogHelper.showSimpleAlertDialog(getApplicationContext(), "No Network connection available");
        }
    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    private boolean validateSignInResponse(JSONObject response) {
        boolean signInSuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(HeylaAppConstants.PARAM_MESSAGE);
                Log.d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (((status.equalsIgnoreCase("activationError")) || (status.equalsIgnoreCase("alreadyRegistered")) ||
                            (status.equalsIgnoreCase("notRegistered")) || (status.equalsIgnoreCase("error")))) {
                        signInSuccess = false;
                        Log.d(TAG, "Show error dialog");
                        AlertDialogHelper.showSimpleAlertDialog(this, msg);

                    } else {
                        signInSuccess = true;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateSignInResponse(response)) {
            try {
                String userId = response.getString("User_id");
                Intent homeIntent = new Intent(getApplicationContext(), UpdatePasswordActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                homeIntent.putExtra("user_id", userId);
                startActivity(homeIntent);
                this.finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String error) {

    }
}