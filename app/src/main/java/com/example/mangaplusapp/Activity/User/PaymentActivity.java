package com.example.mangaplusapp.Activity.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangaplusapp.Activity.Base.BaseActivity;
import com.example.mangaplusapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.ByteString;
import okio.HashingSink;
import vn.momo.momo_partner.AppMoMoLib;
import vn.momo.momo_partner.MoMoParameterNameMap;
import vn.momo.momo_partner.MoMoParameterNamePayment;

public class PaymentActivity extends BaseActivity {
    TextView tvEnvironment;
    TextView tvMerchantCode;
    TextView tvMerchantName;
    EditText edAmount;
    TextView tvMessage;
    Button btnPayMoMo;
    FirebaseAuth firebaseAuth;
    private  Map<String, Object> eventValue = new HashMap<>();
    private String amount = "100000";
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "RAU MÁ SẠCH";
    private String merchantCode = "MOMORPBF20220425";
    private String merchantNameLabel = "Nhà cung cấp";
    private String description = "Rau má";
    private String mangaId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initView();
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);
        tvEnvironment.setText("Development Environment");
        tvMerchantCode.setText("Merchant Code: "+merchantCode);
        tvMerchantName.setText("Merchant Name: "+merchantName);
        btnPayMoMo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPayment();
            }
        });
    }
    /*------------------------------BEGIN-----------------------------------------*/
    private void initView(){
        tvEnvironment = (TextView) findViewById(R.id.tvEnvironment);
        tvMerchantCode = (TextView) findViewById(R.id.tvMerchantCode);
        tvMerchantName = (TextView) findViewById(R.id.tvMerchantName);
        edAmount = (EditText) findViewById(R.id.edAmount);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        btnPayMoMo = (Button) findViewById(R.id.btnPayMoMo);
        mangaId = getIntent().getStringExtra("ID_MANGA");
        firebaseAuth = FirebaseAuth.getInstance();
    }
    //example payment
    /*------------------------------END-----------------------------------------*/

    /*------------------------------BEGIN-----------------------------------------*/
    private void requestPayment() {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);

        if (edAmount.getText().toString() != null && edAmount.getText().toString().trim().length() != 0)
            amount = edAmount.getText().toString().trim();

        //client Required
        eventValue.put(MoMoParameterNamePayment.MERCHANT_NAME, merchantName);
        eventValue.put(MoMoParameterNamePayment.MERCHANT_CODE, merchantCode);
        eventValue.put(MoMoParameterNamePayment.AMOUNT, amount);
        eventValue.put("orderId", "orderId1");

        //client Optional
        eventValue.put(MoMoParameterNamePayment.FEE, fee);
        eventValue.put(MoMoParameterNamePayment.MERCHANT_NAME_LABEL, merchantNameLabel);
        eventValue.put(MoMoParameterNamePayment.DESCRIPTION, description);
        //client extra data
        eventValue.put(MoMoParameterNamePayment.REQUEST_ID,  merchantCode+"merchant_billId_"+System.currentTimeMillis());
        eventValue.put(MoMoParameterNamePayment.PARTNER_CODE, merchantCode);

        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("site_code", "003");
            objExtraData.put("site_name", "RAU MA SACH");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put(MoMoParameterNamePayment.EXTRA_DATA, objExtraData.toString());
        eventValue.put(MoMoParameterNamePayment.REQUEST_TYPE, "payment");
        eventValue.put(MoMoParameterNamePayment.LANGUAGE, "vi");
        eventValue.put(MoMoParameterNamePayment.EXTRA, "");
        //Request momo app
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);
    }
    /*------------------------------END-----------------------------------------*/

    /*------------------------------BEGIN-----------------------------------------*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if(data != null) {
                if(data.getIntExtra("status", -1) == 0) {
                    tvMessage.setText("message: " + "Get token " + data.getStringExtra("message"));
                    String token = data.getStringExtra("data"); //Token response
                    Log.d("requestCode", "onActivityResult: " + token);
                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if(env == null){
                        env = "app";
                    }
                    if(token != null && !token.equals("")) {
                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server

                        isBought();// demo payment successful

                        // IF Momo topup success, continue to process your order
                    } else {
                        tvMessage.setText("message: " + ("Khong thanh cong"));
                    }
                } else if(data.getIntExtra("status", -1) == 1) {
                    String message = data.getStringExtra("message") != null?data.getStringExtra("message"):"Thất bại";
                    tvMessage.setText("message: " + message);
                } else if(data.getIntExtra("status", -1) == 2) {
                    tvMessage.setText("message: " + ("Khong thanh cong"));
                } else {
                    tvMessage.setText("message: " + ("Khong thanh cong"));
                }
            } else {
                tvMessage.setText("message: " + ("Khong thanh cong"));
            }
        } else {
            tvMessage.setText("message: " + ("Khong thanh cong, vui long gui lai"));
        }
    }
    /*------------------------------END-----------------------------------------*/

    /*------------------------------BEGIN-----------------------------------------*/
    private void sendPaymentInfoToServer() {
        String signature = "";
        Map<String, String> data = new TreeMap<>();
        try {
            // Dữ liệu mẫu
            data.put("accessKey", "$accessKey"); // Missing Acesskey (Test)
            data.put("amount", String.valueOf(eventValue.get(MoMoParameterNamePayment.AMOUNT)));
            data.put("extraData", String.valueOf(eventValue.get(MoMoParameterNamePayment.EXTRA_DATA)));
            data.put("ipnUrl", "https://6755-113-161-88-245.ngrok-free.app/");
            data.put("orderId", String.valueOf(eventValue.get("orderId")));
            data.put("orderInfo", "Rau ma");
            data.put("partnerCode", String.valueOf(eventValue.get(MoMoParameterNamePayment.PARTNER_CODE)));
            data.put("redirectUrl", "https://6755-113-161-88-245.ngrok-free.app/");
            data.put("requestId", String.valueOf(eventValue.get(MoMoParameterNamePayment.REQUEST_ID)));
            data.put("requestType", "captureWallet");
            String secrectKey = "/Kdc/wHESkhvYKITgHfSeaXl35MNlzWjVYfVDLMdo1E"; //Missing SecretKey (Test)
            signature = generateSignature(data, secrectKey);
            Log.d("signature", "sendPaymentInfoToServer: "+ signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
        // Tạo một yêu cầu HTTP POST để gửi thông tin đến máy chủ của bạn

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json; charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        try {
            // Đặt các thuộc tính cho đối tượng JSON
            jsonObject.put("partnerCode", eventValue.get(MoMoParameterNamePayment.PARTNER_CODE));
            jsonObject.put("requestId", eventValue.get(MoMoParameterNamePayment.REQUEST_ID));
            jsonObject.put("amount", eventValue.get(MoMoParameterNamePayment.AMOUNT));
            jsonObject.put("orderId", eventValue.get("orderId"));
            jsonObject.put("orderInfo", "Rau ma");
            jsonObject.put("redirectUrl", "https://6755-113-161-88-245.ngrok-free.app/");
            jsonObject.put("ipnUrl","https://6755-113-161-88-245.ngrok-free.app/");
            jsonObject.put("extraData", eventValue.get(MoMoParameterNamePayment.EXTRA_DATA));
            jsonObject.put("lang","vi");
            jsonObject.put("signature", signature.toString());
            jsonObject.put("requestType", "captureWallet");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("requestCode", "sendPaymentInfoToServer: " + jsonObject.toString());
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        Request request = new Request.Builder()
                .url("https://test-payment.momo.vn/v2/gateway/api/create")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        // Thực hiện yêu cầu HTTP
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Xử lý lỗi khi gửi yêu cầu đến máy chủ của bạn
                Log.d("erroRespon", "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Xử lý phản hồi từ máy chủ của bạn sau khi gửi thông tin thành công
                Log.d("succesRespon", "onResponse: " + response.message());
            }
        });
    }
    /*------------------------------END-----------------------------------------*/

    /*------------------------------BEGIN-----------------------------------------*/
    public static String generateSignature(Map<String, String> data, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        // Sắp xếp dữ liệu theo thứ tự a-z
        TreeMap<String, String> sortedData = new TreeMap<>(data);

        // Tạo chuỗi từ dữ liệu đã sắp xếp
        StringBuilder message = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedData.entrySet()) {
            message.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        message.deleteCharAt(message.length() - 1); // Loại bỏ dấu & cuối cùng

        // Tạo chuỗi ký tự từ dữ liệu
        String messageString = message.toString();

        // Sử dụng HMAC_SHA256 để tạo chữ ký
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256Hmac.init(secretKeySpec);
        byte[] signatureBytes = sha256Hmac.doFinal(messageString.getBytes(StandardCharsets.UTF_8));

        // Chuyển đổi byte thành dạng hex
        StringBuilder hexStringBuilder = new StringBuilder();
        for (byte b : signatureBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexStringBuilder.append('0');
            }
            hexStringBuilder.append(hex);
        }
        return hexStringBuilder.toString();
    }
    /*------------------------------END-----------------------------------------*/

    /*------------------------------BEGIN-----------------------------------------*/
    public void isBought(){
        if(firebaseAuth.getCurrentUser() == null){
            Toast.makeText(this,"You're not login", Toast.LENGTH_SHORT).show();
            return;
        }else {
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("ID_MANGA", mangaId);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(firebaseAuth.getUid()).child("HistoryPayment").child(mangaId)
                    .setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(PaymentActivity.this, "Buy manga successful", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        startNewActivityAndFinishCurrent(MainActivity.class);
        super.onBackPressed();
    }
}
