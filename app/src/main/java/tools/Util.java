package tools;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Util {
    private static String[] persianNumbers = new String[]{"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹"};
    public static String convertEnToPe(String value) {
        char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            if (Character.isDigit(value.charAt(i))) {
                builder.append(arabicChars[(int) (value.charAt(i)) - 48]);
            } else {
                builder.append(value.charAt(i));
            }
        }
        return builder.toString();
    }

    public static String convertToFormalString(String input) {
        String priceString = "";
        for (int i = 0; i < input.length(); i++) {
            int j = input.length() - i;
            if (j % 3 != 1) {
                priceString += input.substring(i, i + 1);
            } else {
                priceString += input.substring(i, i + 1) + ",";
            }

        }
        return priceString.substring(0, priceString.length() - 1);
    }

    public static String latinNumberToPersian(String input) {
        String output = input.replaceAll("0", "٠");
        output = output.replaceAll("1", "١");
        output = output.replaceAll("2", "٢");
        output = output.replaceAll("3", "٣");
        output = output.replaceAll("4", "۴");
        output = output.replaceAll("5", "۵");
        output = output.replaceAll("6", "۶");
        output = output.replaceAll("7", "٧");
        output = output.replaceAll("8", "٨");
        output = output.replaceAll("9", "٩");
        return output;
    }

    public static String persianNumberToLatin(String input) {
        String output = input.replaceAll("۰", "0");
        output = output.replaceAll("۱", "1");
        output = output.replaceAll("۲", "2");
        output = output.replaceAll("۳", "3");
        output = output.replaceAll("۴", "4");
        output = output.replaceAll("۵", "5");
        output = output.replaceAll("۶", "6");
        output = output.replaceAll("۷", "7");
        output = output.replaceAll("۸", "8");
        output = output.replaceAll("۹", "9");
        return output;
    }

    public static String farsiNumberReplacement(String text) {
        text = text.replaceAll("۰", "0");
        text = text.replaceAll("۱", "1");
        text = text.replaceAll("۲", "2");
        text = text.replaceAll("۳", "3");
        text = text.replaceAll("۴", "4");
        text = text.replaceAll("۵", "5");
        text = text.replaceAll("۶", "6");
        text = text.replaceAll("۷", "7");
        text = text.replaceAll("۸", "8");
        text = text.replaceAll("۹", "9");

        return text;
    }
    public static String PersianNumber(String text) {
        if (text.length() == 0) {
            return "";
        }
        String out = "";
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if ('0' <= c && c <= '9') {
                int number = Integer.parseInt(String.valueOf(c));
                out += persianNumbers[number];
            } else if (c == '٫') {
                out += '،';
            } else {
                out += c;
            }
        }
        return out;
    }

    public static String createTransactionID() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
}
