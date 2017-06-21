package studio.tmaker.jason.tmcore;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Created by jasontsai on 2017/6/13.
 */

public class TMCore {
    private static TMCore instance = null;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private TMCore() {
        // ....
    }

    public static TMCore getInstance() {
        if (instance == null) {
            instance = new TMCore();
        }

        return instance;
    }

    public boolean validationEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    public <T> T convertJsonString(String string, Class<T> tClass) {
        if (isJSONValid(string)) {
            JsonParser parser = new JsonParser();
            JsonElement mJson =  parser.parse(string);
            Gson gson = new Gson();
            T o = gson.fromJson(mJson, tClass);
            return o;
        }else {
            return null;
        }

    }

    public static boolean isJSONValid(String jsonInString) {
        try {
            Gson gson = new Gson();
            gson.fromJson(jsonInString, Object.class);
            return true;
        } catch(com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }
}
