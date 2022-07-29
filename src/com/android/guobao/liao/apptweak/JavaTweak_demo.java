package com.android.guobao.liao.apptweak;

import java.io.IOException;
import android.util.Log;

@SuppressWarnings("unused")
public class JavaTweak_demo { //�滻�����������࣬����������ͳһ��ǰ׺��com.android.guobao.liao.apptweak.JavaTweak_***��
    static protected void loadDexFile(ClassLoader loader, String dex) {
        //�˺����ڿ�����һЩ��ʼ���������������native��̬�⣬����sotweak���������android.jar���е�ϵͳ�����ȵ�
        //������ò��Ǳ���ģ�����ش����app���������о��������á�
        //����ش����app�����������󣬿��Գ������ηſ���������ô��룬��ĳЩ�ӹ̣��������л��޸��������󣬵�����֤���޸������������⡣
        //JavaTweakBridge.setPluginFlags(JavaTweakBridge.PLUGIN_FLAG_DISABLE_SYSCALL);
        //JavaTweakBridge.setPluginFlags(JavaTweakBridge.PLUGIN_FLAG_DISABLE_OPENAT);
        //JavaTweakBridge.setPluginFlags(JavaTweakBridge.PLUGIN_FLAG_DISABLE_SYSCALL|JavaTweakBridge.PLUGIN_FLAG_DISABLE_OPENAT);

        long handle = JavaTweakBridge.nativeLoadLib("libsodemo.so");
        JavaTweakBridge.writeToLogcat(Log.INFO, "nativeLoadLib: libname = libsodemo.so, handle = 0x%x", handle);

        JavaTweakBridge.hookJavaMethod("android.app.ActivityThread", "performLaunchActivity");
        JavaTweakBridge.hookJavaMethod("javax.net.ssl.SSLContext", "init");
        //JavaTweakBridge.hookJavaMethod("javax.crypto.Cipher", "getInstance(java.lang.String)"); //static����hook���ӣ���־�Ƚ϶࣬Ĭ�ϲ�hook
        //JavaTweakBridge.hookJavaMethod("javax.crypto.spec.SecretKeySpec", "(byte[],java.lang.String)"); //constructor����hook���ӣ���־�Ƚ϶࣬Ĭ�ϲ�hook
    }

    static protected void defineJavaClass(Class<?> clazz) {
        String name = clazz.getName();
        if (name.equals("okhttp3.RealCall") || name.equals("okhttp3.internal.connection.RealCall") || name.equals("okhttp3.OkHttpClient")) {
            String RealCall = null;
            if (!name.equals("okhttp3.OkHttpClient")) {
                RealCall = name;
            } else if (ReflectUtil.classForName("okhttp3.RealCall", false, clazz.getClassLoader()) != null) {
                RealCall = "okhttp3.RealCall";
            } else if (ReflectUtil.classForName("okhttp3.internal.connection.RealCall", false, clazz.getClassLoader()) != null) {
                RealCall = "okhttp3.internal.connection.RealCall";
            }
            JavaTweakBridge.hookJavaMethod(clazz.getClassLoader(), RealCall, "()okhttp3.Response", "execute");
            JavaTweakBridge.hookJavaMethod(clazz.getClassLoader(), RealCall, "(okhttp3.Callback)void", "enqueue");
            return;
        }
        if (name.equals("org.apache.http.impl.client.DefaultRequestDirector") || name.equals("org.apache.http.impl.client.DefaultHttpClient")) {
            JavaTweakBridge.hookJavaMethod(clazz.getClassLoader(), "org.apache.http.impl.client.DefaultRequestDirector", "execute(org.apache.http.HttpHost,org.apache.http.HttpRequest,org.apache.http.protocol.HttpContext)", "executing");
        }
    }

    static private Object performLaunchActivity(Object thiz, Object record, Object intent) {
        JavaTweakBridge.writeToLogcat(Log.INFO, "currentClassLoader: %s", TweakUtil.currentClassLoader());
        return JavaTweakBridge.callOriginalMethod(thiz, record, intent);
    }

    static private Object getInstance(String transformation) { //��̬����û��this����
        return JavaTweakBridge.callStaticOriginalMethod(transformation);
    }

    static private void SecretKeySpec(Object thiz, byte[] key, String algorithm) { //���췽��û�з���ֵ
        JavaTweakBridge.callOriginalMethod(thiz, key, algorithm);
    }

    static private void init(Object thiz, Object km, Object tm, Object random) {
        JavaTweakBridge.callOriginalMethod(thiz, km, tm, random);
    }

    static private Object execute(Object thiz) throws Exception {
        //JavaTweakBridge.writeToLogcat(Log.INFO, Log.getStackTraceString(new Throwable()));
        Object hr = JavaTweakBridge.callOriginalMethod(thiz);
        return TweakUtil.returnWithException(hr, hr == null ? new IOException(thiz.toString()) : null);
    }

    static private void enqueue(Object thiz, Object callback) {
        //JavaTweakBridge.writeToLogcat(Log.INFO, Log.getStackTraceString(new Throwable()));
        JavaTweakBridge.callOriginalMethod(thiz, callback);
    }

    static private Object executing(Object thiz, Object target, Object request, Object context) throws Exception {
        //JavaTweakBridge.writeToLogcat(Log.INFO, Log.getStackTraceString(new Throwable()));
        Object hr = JavaTweakBridge.callOriginalMethod(thiz, target, request, context);
        return TweakUtil.returnWithException(hr, hr == null ? new IOException(thiz.toString()) : null);
    }
}
