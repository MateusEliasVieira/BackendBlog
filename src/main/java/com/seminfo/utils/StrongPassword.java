package com.seminfo.utils;

import com.seminfo.domain.domainException.BusinessRulesException;

public class StrongPassword {

    public static void isStrong(String password) {
        int countLetter = 0;
        int countSpecialCharacters = 0;
        int countNumbers = 0;

        for (int i = 0; i < password.length(); i++) {
            if (password.toLowerCase().charAt(i) >= 'a' && password.toLowerCase().charAt(i) <= 'z') {
                countLetter++;
            } else if (password.toLowerCase().charAt(i) == '!' || password.toLowerCase().charAt(i) == '@'
                    || password.toLowerCase().charAt(i) == '#' || password.toLowerCase().charAt(i) == '$'
                    || password.toLowerCase().charAt(i) == '%' || password.toLowerCase().charAt(i) == '*'
                    || password.toLowerCase().charAt(i) == '(' || password.toLowerCase().charAt(i) == ')'
                    || password.toLowerCase().charAt(i) == '-' || password.toLowerCase().charAt(i) == '+') {
                countSpecialCharacters++;
            } else if (password.toLowerCase().charAt(i) == '0' || password.toLowerCase().charAt(i) == '1'
                    || password.toLowerCase().charAt(i) == '2' || password.toLowerCase().charAt(i) == '3'
                    || password.toLowerCase().charAt(i) == '4' || password.toLowerCase().charAt(i) == '5'
                    || password.toLowerCase().charAt(i) == '6' || password.toLowerCase().charAt(i) == '7'
                    || password.toLowerCase().charAt(i) == '8' || password.toLowerCase().charAt(i) == '9') {
                countNumbers++;
            }
        }

        if (!(countLetter >= 2 && countSpecialCharacters >= 2 && countNumbers >= 2)) {
            throw new BusinessRulesException(Feedback.WEAK_PASSWORD);
        }

    }
}
