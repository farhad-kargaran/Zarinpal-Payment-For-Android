package ir.farhadkargaran.zarrinpalpayment_android;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.zarinpal.ZarinPalBillingClient;
import com.zarinpal.billing.purchase.Purchase;
import com.zarinpal.client.BillingClientStateListener;
import com.zarinpal.client.ClientState;
import com.zarinpal.provider.core.future.FutureCompletionListener;
import com.zarinpal.provider.core.future.TaskResult;
import com.zarinpal.provider.model.response.Receipt;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    private String merchand_id = "54f56e5e-9e60-402d-aa97-39d4b4deeba6";
    private ZarinPalBillingClient client;
    AppCompatButton btn_payment;
    AppCompatTextView tv_result,tv_log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        setContentView(R.layout.activity_main);

        initialize();
        setupBilling();
    }
    private void initialize() {
        btn_payment = findViewById(R.id.btn_payment);
        tv_result = findViewById(R.id.tv_result);
        tv_result.setMovementMethod(new ScrollingMovementMethod());
        tv_log = findViewById(R.id.tv_log);
        tv_log.setMovementMethod(new ScrollingMovementMethod());

        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_result.setText("");
                tv_log.setText("Logs:");
                tv_log.setText(tv_log.getText() + "\n" + "btn_payment clicked...");
                Purchase purchase = Purchase.newBuilder().asPaymentRequest(
                        merchand_id,
                        10000L, //Price in Rial
                        "return://zarinpalpayment", //This line is defined in AndroidManifest.xml
                        "1000IRR Purchase") //Optional Description
                        .build();
                client.launchBillingFlow(purchase, new FutureCompletionListener<Receipt>() {
                    @Override
                    public void onComplete(TaskResult<Receipt> task) {
                        if (task.isSuccess()) {
                            Receipt receipt = task.getSuccess();
                            tv_result.setText(tv_log.getText() + "\n" +
                                    "getAmount:" + receipt.getAmount() +
                                    "getDescription:" + receipt.getDescription() +
                                    "getTransactionID:" + receipt.getTransactionID() +
                                    "isSuccess:" + receipt.isSuccess() +
                                    "getProvider:" + receipt.getProvider() +
                                    "getStatus:" + receipt.getStatus() +
                                    "getRedirectURL:" + receipt.getRedirectURL());
                        } else {
                            task.getFailure().printStackTrace();
                            tv_result.setText(tv_log.getText() + "\n" + "task.getFailure().getMessage()" + task.getFailure().getMessage());
                            tv_result.setText(tv_log.getText() + "\n" + "System Error:" + System.err);
                        }
                    }
                });

            }
        });
    }
    private void setupBilling()
    {
        BillingClientStateListener listener = new BillingClientStateListener() {
            @Override
            public void onClientSetupFinished(@NotNull ClientState state) {
                tv_log.setText(tv_log.getText() + "\n" + "onClientSetupFinished() called: " + state.toString());
            }
            @Override
            public void onClientServiceDisconnected() {
                tv_log.setText(tv_log.getText() + "\n" + "onClientServiceDisconnected() called");

            }
        };
        client = ZarinPalBillingClient.newBuilder(this)
                .enableShowInvoice()
                .setListener(listener)
                .setNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                .build();
    }


}