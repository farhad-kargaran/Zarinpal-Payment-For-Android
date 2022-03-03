package ir.farhadkargaran.zarrinpalpayment_android;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;

import com.zarinpal.ZarinPalBillingClient;
import com.zarinpal.billing.purchase.Purchase;
import com.zarinpal.client.BillingClientStateListener;
import com.zarinpal.client.ClientState;
import com.zarinpal.provider.core.future.FutureCompletionListener;
import com.zarinpal.provider.core.future.TaskResult;
import com.zarinpal.provider.model.response.Receipt;

import org.jetbrains.annotations.NotNull;

public class NewMethod extends AppCompatActivity {

    private String merchand_id = "54f56e5e-9e60-402d-aa97-39d4b4deeba6";
    private ZarinPalBillingClient client;
    AppCompatTextView tv_result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        setContentView(R.layout.activity_new_method);
        initialize();
        BillingClientStateListener listener = new BillingClientStateListener() {
            @Override
            public void onClientSetupFinished(@NotNull ClientState state) {}
            @Override
            public void onClientServiceDisconnected() {}
        };
        client = ZarinPalBillingClient.newBuilder(this).enableShowInvoice().setListener(listener).setNightMode(AppCompatDelegate.MODE_NIGHT_NO).build();
    }
    private void initialize() {
        AppCompatButton btn_payment = findViewById(R.id.btn_payment);
        tv_result = findViewById(R.id.tv_result);
        tv_result.setMovementMethod(new ScrollingMovementMethod());
        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_result.setText("Logs:");
                tv_result.setText(tv_result.getText() + "\n" + "btn_payment clicked...");
                Purchase purchase = Purchase.newBuilder().asPaymentRequest(
                        merchand_id,1000L,"returna://zarinpalpayment","1000IRR Purchase"
                ).build();
                client.launchBillingFlow(purchase, new FutureCompletionListener<Receipt>() {
                    @Override
                    public void onComplete(TaskResult<Receipt> task) {
                        if (task.isSuccess()) {
                            Receipt receipt = task.getSuccess();
                            Log.v("ZP_RECEIPT", receipt.getTransactionID());
                        } else { task.getFailure().printStackTrace(); }
                    }
                });
            }
        });
    }

}