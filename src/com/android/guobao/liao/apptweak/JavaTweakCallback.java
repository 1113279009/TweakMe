package com.android.guobao.liao.apptweak;

@SuppressWarnings("unused")
public class JavaTweakCallback {
    static private void loadDexFile(ClassLoader loader, String dex) {
        JavaTweak.loadDexFile(loader, dex); //javatweak.dex��Զ���ǵ�һ�����ص�, ��һ����ص�, Ҳ��ĿǰΨһ��һ���ص�
    }

    static private void defineJavaClass(Class<?> clazz) {
        JavaTweak.defineJavaClass(clazz);
    }
}
