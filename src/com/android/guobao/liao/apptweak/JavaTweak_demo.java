package com.android.guobao.liao.apptweak;

@SuppressWarnings("unused")
public class JavaTweak_demo { //�滻�����������࣬����������ͳһ��ǰ׺��com.android.guobao.liao.apptweak.JavaTweak_***��
    static protected void loadDexFile(ClassLoader loader, String dex) {
        //�˺����ڿ�����һЩ��ʼ���������������native��̬�⣬����android.jar���е�ϵͳ�����ȵ�
        //JavaTweakBridge.nativeLoadLib("libdemo.so");

        //������һ������Ҫ����!!!!!!!!!!!!!!!!
        //packageName����Ϊʵ��ע���apk��Ӧ�İ���
        //certEncoded��������ͨ��apktweak���߻�ȡ����ȡ��������.
        //apktweak --apk xxx.apk --cert
        JavaTweak_sign.setAppCert("com.android.demo.tweakme", "MIICNzCCAaCgAwIBAgIEUgyXFD/kgi5oarxBGfIdvduMtxxevbXwQmiA==");

        JavaTweakBridge.hookJavaMethod("javax.net.ssl.SSLContext", "init");
        JavaTweakBridge.hookJavaMethod("javax.crypto.Cipher", "getInstance(java.lang.String)"); //static
        JavaTweakBridge.hookJavaMethod("javax.crypto.spec.SecretKeySpec", "(byte[],java.lang.String)", "SecretKeySpec"); //constructor
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

    static private Object getInstance(String transformation) { //��̬����û��this����
        return JavaTweakBridge.callStaticOriginalMethod(transformation);
    }

    static private void SecretKeySpec(Object thiz, byte[] key, String algorithm) { //���췽��û�з���ֵ
        JavaTweakBridge.callOriginalMethod(thiz, key, algorithm);
    }

    static private void init(Object thiz, Object km, Object tm, Object random) {
        JavaTweakBridge.callOriginalMethod(thiz, km, tm, random);
    }

    static private Object execute(Object thiz) {
        //JavaTweakBridge.writeToLogcat(Log.INFO, Log.getStackTraceString(new Throwable()));
        return JavaTweakBridge.callOriginalMethod(thiz);
    }

    static private void enqueue(Object thiz, Object callback) {
        //JavaTweakBridge.writeToLogcat(Log.INFO, Log.getStackTraceString(new Throwable()));
        JavaTweakBridge.callOriginalMethod(thiz, callback);
    }

    static private Object executing(Object thiz, Object target, Object request, Object context) {
        //JavaTweakBridge.writeToLogcat(Log.INFO, Log.getStackTraceString(new Throwable()));
        return JavaTweakBridge.callOriginalMethod(thiz, target, request, context);
    }
}
