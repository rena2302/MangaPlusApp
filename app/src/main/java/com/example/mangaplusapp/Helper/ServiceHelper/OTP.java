package com.example.mangaplusapp.Helper.ServiceHelper;

import java.sql.Time;
import java.util.Random;

import papaya.in.sendmail.SendMail;

public class OTP {
    public void sendOTPByEmail(String otp,String emailUser) {
        // sender pass is verification 2 case, got code pass app.
        // use connect https smtp email don't block by fire ware and private network
        SendMail mailTask = new SendMail("softwaretestact@gmail.com","lyzf yzcd mdyi yojp",emailUser,"Manga Plus App's OTP ","Your OTP is -> "+otp);
        mailTask.execute();
    }
    public String generateOTP() {
        int length = 4;
        String numbers = "0123456789"; //
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(numbers.length());
            otp.append(numbers.charAt(index));
        }
        return otp.toString();
    }
    public void reSendOtp(Time time){
        
    }
}
