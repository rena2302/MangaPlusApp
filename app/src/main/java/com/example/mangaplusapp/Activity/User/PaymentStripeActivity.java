package com.example.mangaplusapp.Activity.User;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mangaplusapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentStripeActivity extends AppCompatActivity {
    AppCompatButton btn;
    PaymentSheet paymentSheet;
    String paymentIntentClientSecret;
    PaymentSheet.CustomerConfiguration configuration;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    private String mangaId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_stripe);
        auth=FirebaseAuth.getInstance();
        currentUser=auth.getCurrentUser();
        mangaId = getIntent().getStringExtra("ID_MANGA");
        fetchApi();
        btn = findViewById(R.id.btnSubmitPaymentStripe);
        btn.setOnClickListener(v -> {
            if(paymentIntentClientSecret!=null){
                paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret,
                        new PaymentSheet.Configuration("MangaPlus",configuration));
            }
            else{
                Toast.makeText(getApplicationContext(),"Wait a second, page is loading,...",Toast.LENGTH_SHORT).show();
            }
        });
        paymentSheet=new PaymentSheet(this,this::onPaymentSheetResult);
    }
    private void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult){
        if(paymentSheetResult instanceof  PaymentSheetResult.Canceled){
            Toast.makeText(this,"Canceled", Toast.LENGTH_SHORT).show();
        }
        if(paymentSheetResult instanceof  PaymentSheetResult.Failed){
            Toast.makeText(this,((PaymentSheetResult.Failed) paymentSheetResult).getError().getMessage(), Toast.LENGTH_SHORT).show();
        }
        if(paymentSheetResult instanceof  PaymentSheetResult.Completed){
            fetchApi();
            isBought();
        }
    }
    public void isBought(){
        if(currentUser == null){
            Toast.makeText(this,"You're not login", Toast.LENGTH_SHORT).show();
            return;
        }else {
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("ID_MANGA", mangaId);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(currentUser.getUid()).child("HistoryPayment").child(mangaId)
                    .setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(PaymentStripeActivity.this,"Payment Success", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void fetchApi(){ // POST
        RequestQueue queue = Volley.newRequestQueue(this);
            String url ="https://d81a-1-53-17-251.ngrok-free.app/";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            configuration= new PaymentSheet.CustomerConfiguration(
                                    jsonObject.getString("customer"),
                                    jsonObject.getString("ephemeralKey")
                            );
                            paymentIntentClientSecret = jsonObject.getString("paymentIntent");
                            PaymentConfiguration.init(getApplicationContext(),jsonObject.getString("publishableKey"));
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("authKey", "abc");
                return paramV;
            }
        };
        queue.add(stringRequest);
    }
}