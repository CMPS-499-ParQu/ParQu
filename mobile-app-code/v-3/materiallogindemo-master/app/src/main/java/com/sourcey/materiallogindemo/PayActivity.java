package com.sourcey.materiallogindemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;


public class PayActivity extends AppCompatActivity {

    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId("AQj5TcpWZpheoZotigyxlbX2abhLIpqxal0jaD_kZS-EicAyAdyaJnqFL48ol1YruaCjsEQOuqYbP9bt");
    Button button;
    String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Log.d("PayActivity", "onMapReady: map is ready");
        Intent intent = new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startActivity(intent);

        button = (Button) findViewById(R.id.button22);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });

    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }




    private void processPayment() {
         amount = "100";
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "QR", "Donate for ParQu", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent i = new Intent(this, PaymentActivity.class);
        i.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        i.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(i, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("paymentDetails", paymentDetails)
                                .putExtra("paymentAmount", amount)
                        );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }

            else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){

        }


    }
}
