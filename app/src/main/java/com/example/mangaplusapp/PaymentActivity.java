package com.example.mangaplusapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import vn.momo.momo_partner.AppMoMoLib;
import vn.momo.momo_partner.MoMoParameterNameMap;
import vn.momo.momo_partner.MoMoParameterNamePayment;

public class PaymentActivity extends Activity {
    TextView tvEnvironment;
    TextView tvMerchantCode;
    TextView tvMerchantName;
    EditText edAmount;
    TextView tvMessage;
    Button btnPayMoMo;
    private  Map<String, Object> eventValue = new HashMap<>();
    private String amount = "10000";
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "RAU MÁ SẠCH";
    private String merchantCode = "MOMORPBF20220425";
    private String merchantNameLabel = "Nhà cung cấp";
    private String description = "Rau má";
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
    private void initView(){
        tvEnvironment = (TextView) findViewById(R.id.tvEnvironment);
        tvMerchantCode = (TextView) findViewById(R.id.tvMerchantCode);
        tvMerchantName = (TextView) findViewById(R.id.tvMerchantName);
        edAmount = (EditText) findViewById(R.id.edAmount);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        btnPayMoMo = (Button) findViewById(R.id.btnPayMoMo);
    }
    //example payment
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
    private void sendPaymentInfoToServer() {
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
                Log.d("erroRespon", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Xử lý phản hồi từ máy chủ của bạn sau khi gửi thông tin thành công
                Log.d("succesRespon", "onResponse: " + response.message());
            }
        });
    }

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
                        sendPaymentInfoToServer();
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
}
