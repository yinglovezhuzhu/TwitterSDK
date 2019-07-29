package com.owen.twitter.sdk.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * User: Ranger
 * Date: 26/05/2017
 * Time: 10:37 AM
 */

public class ResourcesUtils {
    public static final String ANIMATION = "animation";
    public static final String COLOR = "color";
    public static final String DRAWABLE = "drawable";
    public static final String LAYOUT = "layout";
    public static final String MENU = "menu";
    public static final String STRING = "string";
    public static final String STYLE = "style";
    public static final String DIMEN = "dimen";
    public static final String ATTR = "attr";
    public static final String ID = "id";

    public static int getResourcesID(String name, String type, Context context) {
        int id = 0x7f000000;
        try {
            id = context.getResources().getIdentifier(name, type, context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d("getResourcesID -> exception ->" + e.toString());
        }
        return id;
    }

    public static int getAnimationID(String name, Context ctx) {
        return getResourcesID(name, ANIMATION, ctx);
    }

    public static int getColorID(String name, Context ctx) {
        return getResourcesID(name, COLOR, ctx);
    }

    public static int getDrawableID(String name, Context ctx) {
        return getResourcesID(name, DRAWABLE, ctx);
    }

    public static int getLayoutID(String name, Context ctx) {
        return getResourcesID(name, LAYOUT, ctx);
    }

    public static int getMenuID(String name, Context ctx) {
        return getResourcesID(name, MENU, ctx);
    }

    public static int getStringID(String name, Context ctx) {
        return getResourcesID(name, STRING, ctx);
    }

    public static int getStyleID(String name, Context ctx) {
        return getResourcesID(name, STYLE, ctx);
    }

    public static int getDimenID(String name, Context ctx) {
        return getResourcesID(name, DIMEN, ctx);
    }

    public static int getAttrID(String name, Context ctx) {
        return getResourcesID(name, ATTR, ctx);
    }

    public static int getID(String name, Context ctx) {
        return getResourcesID(name, ID, ctx);
    }

    /**
     * 通过资源名称获取drawable对象
     * @param name drawable资源名称
     * @param ctx Context对象
     * @return Drawable对象
     */
    public static Drawable getDrawable(String name, Context ctx) {
        return ctx.getResources().getDrawable(getDrawableID(name, ctx));
    }

    public static int getColor(String name, Context ctx) {
        return ctx.getResources().getColor(getColorID(name, ctx));
    }

    /**
     * 通过字符资源名称获取string
     * @param name 字符资源名称（本地strings.xml中的名字）
     * @param ctx Context对象
     * @return 字符资源内容，如果没有，返回空字符
     */
    public static String getString(String name, Context ctx) {
        final int resId = getStringID(name, ctx);
        if(0 == resId) {
            return "";
        }
        return ctx.getResources().getString(resId);
    }

}
