package com.owen.twitter.sdk.utils;

import android.text.TextUtils;

import java.util.Locale;
import java.util.TimeZone;

/**
 * User: Ranger
 * Date: 01/06/2017
 * Time: 11:59 AM
 */

public class SDKUtils {


    /**
     * 获取系统设置时区ID
     *
     * @return 时区ID
     */
    public static String getTimeZoneID() {
        return TimeZone.getDefault().getID();
    }

    /**
     * 获取时间戳（单位：秒）
     * @return 时间戳（单位：秒）
     */
    public static long getTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取系统设置语言码,安卓和ios统一,以ios为准
     *
     * @return 语言码
     */
    public static String getLanguageCode() {
        //简体中文，做特殊处理，返回"zh-Hans"
        if (Locale.getDefault().getLanguage().equals("zh")&&Locale.getDefault().getCountry().equals("CN")) {
            return "zh-Hans";
        }
        //繁体中文，做特殊处理
        if (Locale.getDefault().getLanguage().equals("zh")&&Locale.getDefault().getCountry().equals("TW")||Locale.getDefault().getLanguage().equals("zh")&&Locale.getDefault().getCountry().equals("HK")) {
            return "zh-Hant";
        }
        //印度尼西亚，做特殊处理
        if (Locale.getDefault().getLanguage().equals("in")) {
            return "id";
        }
        //越南
        if (Locale.getDefault().getLanguage().equals("vi")) {
            return "vi";
        }
        return Locale.getDefault().getLanguage();
    }

    /**
     * 是否为新版本，版本号必须按指定格式传入，否则返回false
     * @param oldVersion 旧版本号（格式为x.x.x）
     * @param newVersion 新版本号（格式为x.x.x）
     * @return 是否为较新版本
     */
    public static boolean isNewVersion(String oldVersion, String newVersion) {
        if(TextUtils.isEmpty(oldVersion) || TextUtils.isEmpty(newVersion)) {
            return false;
        }
        final String [] oldVersionArray = oldVersion.split("\\.");
        final String [] newVersionArray = newVersion.split("\\.");
        for(int i = 0; i < (oldVersionArray.length > newVersionArray.length ? newVersionArray.length : oldVersionArray.length); i++) {
            try {
                if(Integer.parseInt(newVersionArray[i]) > Integer.parseInt(oldVersionArray[i])) {
                    return true;
                }
            } catch (NumberFormatException e) {
                // 格式错误，无法转换为数字
                return false;
            }
        }
        // 如果前面数字都相同，判断新版本号的段数是否比旧版本号多（如1.1.0.1比1.1.0为更新的版本）
        return newVersionArray.length > oldVersionArray.length;
    }

    /**
     * 加密处理邮箱（从第三个字符开始到@之前的字符，用“****”替代）
     * @param email 加密前的邮箱地址
     * @return 加密后的邮箱地址
     */
    public static String encryptEmail(String email) {
        if(TextUtils.isEmpty(email)) {
            return email;
        }
        return email.replace(email.substring(2, email.indexOf("@")), "****");
    }
}
