package ir.farhadkargaran.zarrinpalpayment_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ArrowKeyMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zarinpal.ewallets.purchase.OnCallbackRequestPaymentListener;
import com.zarinpal.ewallets.purchase.OnCallbackVerificationPaymentListener;
import com.zarinpal.ewallets.purchase.PaymentRequest;
import com.zarinpal.ewallets.purchase.ZarinPal;

public class OldMethod extends AppCompatActivity {

    private String merchand_id = "54f56e5e-9e60-402d-aa97-39d4b4deeba6";
    Button btnrequest;
    TextView tv_result;
    Uri data;
    @Override
    protected void onResume() {
        super.onResume();
        data =getIntent().getData();
        if (data!=null) {
            tv_result.setText(tv_result.getText() + "\n" + "data:" + data.toString());
            ZarinPal.getPurchase(OldMethod.this).verificationPayment(data, new OnCallbackVerificationPaymentListener() {
                @Override
                public void onCallbackResultVerificationPayment(boolean isPaymentSuccess, String refID, PaymentRequest paymentRequest) {
                    if (isPaymentSuccess) {
                        tv_result.setText(tv_result.getText() + "\n" + "isPaymentSuccess:" + isPaymentSuccess  + ", refID:" +  refID);
                    } else {
                        tv_result.setText(tv_result.getText() + "\n" + "isPaymentSuccess:" + isPaymentSuccess  + ", refID:" +  refID);
                    }
                }
            });
        }
        else {
            tv_result.setText(tv_result.getText() + "\n" + "getIntent().getData() is NULL " );
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        setContentView(R.layout.activity_old_method);
        btnrequest=findViewById(R.id.btnsendreq);
        tv_result=findViewById(R.id.tv_result);
        tv_result.setMovementMethod(new ArrowKeyMovementMethod());
        tv_result.setText("Logs:");
        btnrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment(1000L);
            }
        });
    }
    private void payment(Long amount){
        try {
            ZarinPal purchase=ZarinPal.getPurchase(OldMethod.this);
            PaymentRequest paymentRequest=ZarinPal.getPaymentRequest();
            paymentRequest.setMerchantID(merchand_id);
            paymentRequest.setAmount(amount);
            paymentRequest.setCallbackURL("returnb://zarinpalpayment");
            paymentRequest.setDescription("پرداخت تست");
            paymentRequest.setMobile("0");
            purchase.startPayment(paymentRequest, new OnCallbackRequestPaymentListener() {
                @Override
                public void onCallbackResultPaymentRequest(int status, String authority, Uri paymentGatewayUri, Intent intent) {

                    if (status==100){
                        tv_result.setText(tv_result.getText() + "\n" + "status:" + status);
                        tv_result.setText(tv_result.getText() + "\n" + "authority:" + authority);
                        //آتوریتی را ذخیره کنید. یک کد منحصر بفرد است که چنانچه کربر بعد از پرداخت نتواند به برنامه برگردد
                        //با فراخوانی ای پی آی مخصوص تراکنش های وریفای نشده می توانید بطور خودکار پرداخت کاربر را وریفای کنید
                        tv_result.setText(tv_result.getText() + "\n" + "paymentGatewayUri:" + paymentGatewayUri);
                        tv_result.setText(tv_result.getText() + "\n" + "intent.getData().toString():" + intent.getData().toString());
                        startActivity(intent);
                    }else {
                        tv_result.setText(tv_result.getText() + "\n" + "خطا در ایجاد درخواست");

                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}