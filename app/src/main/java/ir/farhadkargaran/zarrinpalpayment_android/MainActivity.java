package ir.farhadkargaran.zarrinpalpayment_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import com.zarinpal.ZarinPalBillingClient;

public class MainActivity extends AppCompatActivity {
    private String merchand_id = "54f56e5e-9e60-402d-aa97-39d4b4deeba6";
    private ZarinPalBillingClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatButton btn_payment_old = findViewById(R.id.btn_payment_old);
        AppCompatButton btn_payment_new = findViewById(R.id.btn_payment_new);
        btn_payment_old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,OldMethod.class);
                startActivity(intent);
            }
        });
        btn_payment_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,NewMethod.class);
                startActivity(intent);
            }
        });
    }
}