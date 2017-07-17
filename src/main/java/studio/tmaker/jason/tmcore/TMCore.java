package studio.tmaker.jason.tmcore;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.service.media.MediaBrowserService;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

/**
 * Created by jasontsai on 2017/6/13.
 */

public class TMCore {
    Gson gson = new Gson();
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

    public static String randomString(int length) {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(length);
        for(int i=0;i<length;++i)
            sb.append("0123456789qwertyuiopasdfghjklzxcvbnm".charAt(random.nextInt("0123456789qwertyuiopasdfghjklzxcvbnm".length())));
        return sb.toString();
    }

    public String converter(Date date, String format) {
        //先行定義時間格式
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        //透過SimpleDateFormat的format方法將Date轉為字串
        return sdf.format(date);
    }

    public TMLocation converter(String gps) {
        String[] parts = gps.split(",");
        if (parts.length == 2) {
            TMLocation tmLocation = new TMLocation();
            tmLocation.setLatitude(Double.parseDouble(parts[0]));
            tmLocation.setLongitude(Double.parseDouble(parts[1]));
            return tmLocation;
        }else {
            return null;
        }
    }

    public boolean validationEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    public <T> T converJson(String string, Class<T> tClass) {
        if (isJSONValid(string)) {
            return gson.fromJson(string, tClass);
        }else {
            return null;
        }
    }

    public <T> ApiResponse<T> convertJson2ApiResponse(String string, Class<T> tClass) {
        Type type = TypeBuilder
                .newInstance(ApiResponse.class)
                .addTypeParam(tClass)
                .build();
        if (isJSONValid(string)) {
            return gson.fromJson(string, type);
        }else {
            return null;
        }
    }

    public <T> ApiResponse<List<T>> convertJson2ApiResponseList(String string, Class<T> tClass) {
        Type type = TypeBuilder
                .newInstance(ApiResponse.class)
                .beginSubType(List.class)
                .addTypeParam(tClass)
                .endSubType()
                .build();
        if (isJSONValid(string)) {
            return gson.fromJson(string, type);
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

    public void makeDelay(int seconds, final Block block) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                block.completion();
            }
        }, seconds * 1000);
    }

    public interface Block {
        void completion();
        void failure();
    }
}


