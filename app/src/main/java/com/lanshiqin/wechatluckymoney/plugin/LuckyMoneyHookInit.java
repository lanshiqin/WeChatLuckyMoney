package com.lanshiqin.wechatluckymoney.plugin;

import android.os.Bundle;
import android.widget.TextView;

import com.lanshiqin.wechatluckymoney.constant.ConfigConsts;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class LuckyMoneyHookInit implements IXposedHookLoadPackage {

    private XSharedPreferences sharedPreferences;
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.equals(ConfigConsts.WECHAT_PACKAGE_NAME)){
            return;
        }

        // 红包详情页面显示自定义金额
        final String hookLuckyMoneyDetailClass = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";
        final String hookLuckyMoneyDetailMethod = "onCreate";
        final String luckyMoneyFieldName = "pLw";
        XposedHelpers.findAndHookMethod(hookLuckyMoneyDetailClass, loadPackageParam.classLoader,
                hookLuckyMoneyDetailMethod, Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        sharedPreferences = new XSharedPreferences(ConfigConsts.APP_PACKAGE_NAME,ConfigConsts.APP_SHARD_PREFERENCES_FILE_NAME);
                        boolean luckyMoneyStatus = sharedPreferences.getBoolean("luckyMoneyStatus",true);
                        if (!luckyMoneyStatus){
                            return;
                        }
                        String luckyMoneyValue = sharedPreferences.getString("luckyMoneyValue",ConfigConsts.WECHAT_LUCKY_MONEY_VALUE);
                        Class c = loadPackageParam.classLoader.loadClass(hookLuckyMoneyDetailClass);
                        Field luckyMoneyField = c.getDeclaredField(luckyMoneyFieldName);
                        luckyMoneyField.setAccessible(true);
                        TextView luckyMoneyTextView = (TextView) luckyMoneyField.get(param.thisObject);
                        if (luckyMoneyTextView!=null){
                            luckyMoneyTextView.setText(luckyMoneyValue);
                        }
                    }
        });
    }
}
