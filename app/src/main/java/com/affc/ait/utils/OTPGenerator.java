package com.affc.ait.utils;

import java.util.Random;

public class OTPGenerator {

    public static String generateOTP() {
        // Define the length of the OTP
        int otpLength = 6;

        // Define characters allowed in the OTP
        String numbers = "0123456789";

        // Use StringBuilder to efficiently build the OTP
        StringBuilder sb = new StringBuilder(otpLength);

        // Create an instance of Random class
        Random random = new Random();

        // Generate the OTP by appending random digits to StringBuilder
        for (int i = 0; i < otpLength; i++) {
            // Generate a random index to select a character from numbers
            int index = random.nextInt(numbers.length());
            // Append the selected character to StringBuilder
            sb.append(numbers.charAt(index));
        }

        // Convert StringBuilder to String and return the OTP
        return sb.toString();
    }
}
