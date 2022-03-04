package ir.farhadkargaran.zarrinpalpayment_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ArrowKeyMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.zarinpal.ewallets.purchase.OnCallbackRequestPaymentListener;
import com.zarinpal.ewallets.purchase.OnCallbackVerificationPaymentListener;
import com.zarinpal.ewallets.purchase.PaymentRequest;
import com.zarinpal.ewallets.purchase.ZarinPal;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OldMethod extends AppCompatActivity {

    private String merchand_id = "54f56e5e-9e60-402d-aa97-39d4b4deeba6";
    TextView tv_result;
    public SharedPreferences settings;
    public SharedPreferences.Editor editor;

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
        settings = getSharedPreferences("UserInfo", 0);
        editor = settings.edit();
        Button btncheck_unverified =findViewById(R.id.btncheck_unverified);
        Button btnrequest =findViewById(R.id.btnsendreq);
        tv_result=findViewById(R.id.tv_result);
        tv_result.setMovementMethod(new ArrowKeyMovementMethod());
        tv_result.setText("Logs:");
        btnrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment(1000L);
            }
        });
        btncheck_unverified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_unverfied_payment();
            }
        });
    }
    private void check_unverfied_payment()
    {
        String authority = settings.getString("authority","");
        if(authority.length()>0)
        {
            RequestQueue queue = Volley.newRequestQueue(OldMethod.this);
            Map<String, String> postParam= new HashMap<String, String>();
            postParam.put("merchant_id", merchand_id);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,"https://api.zarinpal.com/pg/v4/payment/unVerified.json", new JSONObject(postParam), new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response)
                {
                    if(response.toString().indexOf(authority)>0)
                    {
                        //User has payed sucessfully before, grant app features for him/her
                        Toast.makeText(OldMethod.this,"پرداخت شما تایید شد", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(OldMethod.this,"پرداخت تایید نشده ای یافت نشد", Toast.LENGTH_LONG).show();

                    }
                    editor.putString("authority","");
                    editor.apply();
                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    tv_result.setText(error.getMessage());            }
            }) {
                /**
                 * Passing some request headers
                 * */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            // Adding request to request queue
            queue.add(jsonObjReq);
        }

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
                        editor.putString("authority",authority);
                        editor.apply();
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