package ir.farhadkargaran.zarrinpalpayment_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetUnverfiedTransactionActivity extends AppCompatActivity {

    AppCompatTextView tv_result;
    private String merchand_id = "54f56e5e-9e60-402d-aa97-39d4b4deeba6";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        setContentView(R.layout.activity_get_unverfied_transaction);

        AppCompatButton btn_payment = findViewById(R.id.btn_payment);
        tv_result = findViewById(R.id.tv_result);
        tv_result.setMovementMethod(new ScrollingMovementMethod());
        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlist();
            }
        });
    }
    private void getlist()
    {
        RequestQueue queue = Volley.newRequestQueue(GetUnverfiedTransactionActivity.this);
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("merchant_id", merchand_id);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,"https://api.zarinpal.com/pg/v4/payment/unVerified.json", new JSONObject(postParam), new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                tv_result.setText(response.toString());
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