package com.example.muskankatiyar;





import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    String sender = smsMessage.getDisplayOriginatingAddress();
                    String messageBody = smsMessage.getMessageBody();

                    // Toast message to show received SMS details
                    Toast.makeText(context, "Received SMS from: " + sender + "\nMessage: " + messageBody, Toast.LENGTH_SHORT).show();

                    // Parse the reply
                    // Parse the reply
                    try {
                        String[] parts = messageBody.split(", ");
                        if (parts.length == 3) {
                            String timeTaken = parts[0].split(": ")[1].trim();
                            String incentive = parts[1].split(": ")[1].trim();
                            String techStack = parts[2].split(": ")[1].trim();

                            // Log parsed values for debugging
                            Log.d("SmsReceiver", "Parsed values: Time Taken = " + timeTaken + ", Incentive = " + incentive + ", Tech Stack = " + techStack);

                            // Store the reply in SQLite
                            DatabaseHelper db = new DatabaseHelper(context);
                            db.addReply(timeTaken, incentive, techStack,sender);
                            Toast.makeText(context, sender+"Reply saved: " + messageBody, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Invalid reply format", Toast.LENGTH_SHORT).show();
                            Log.d("SmsReceiver", "Invalid reply format: " + messageBody);
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Error parsing reply: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("SmsReceiver", "Error parsing reply: ", e);
                    }
                }
            } else {
                Toast.makeText(context, "No PDUs found in the SMS", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Received null bundle", Toast.LENGTH_SHORT).show();
        }
    }
}


