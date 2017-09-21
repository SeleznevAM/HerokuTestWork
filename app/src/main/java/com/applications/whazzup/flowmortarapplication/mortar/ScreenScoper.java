package com.applications.whazzup.flowmortarapplication.mortar;

import android.support.annotation.Nullable;
import android.util.Log;

import com.applications.whazzup.flowmortarapplication.di.DaggerService;
import com.applications.whazzup.flowmortarapplication.flow.AbstractScreen;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import mortar.MortarScope;


public class ScreenScoper {
    private static final String TAG = "ScreenScoper";
    private static Map<String, MortarScope> sScopeMap = new HashMap<>();

    public static MortarScope getScreenScope(AbstractScreen screen) {
        if (!sScopeMap.containsKey(screen.getScopeName())) {
            Log.e(TAG, "getScreenScope: create new Scope");
            return createScreenScope(screen);
        } else {
            Log.e(TAG, "getScreenScope: return has scope");
            return sScopeMap.get(screen.getScopeName());
        }
    }

    /**
     * метод для сохранения  скоупа в коллецию
     * ключом для мапы являеться имя передвавемого скоупа
     *
     * @param scope то значение которое сохраняем
     */
    public static void registerScope(MortarScope scope) {
        sScopeMap.put(scope.getName(), scope);
    }

    /**
     * метод для удаления области видимости
     * 1. получаем МортарСкоуп из мапы по имени скоупа
     *
     * @param scopeName имя скопа который удаляем
     */
    public static void destroyScreenScope(String scopeName) {
        Log.e("ScreeScoper", scopeName + " is deleted");
        MortarScope mortarScope = sScopeMap.get(scopeName);
        mortarScope.destroy();
        cleanScopeMap();
    }

    /**
     * Метод для отчистки скоупа из мапы
     */
    private static void cleanScopeMap() {
        Iterator<Map.Entry<String, MortarScope>> iterator = sScopeMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, MortarScope> entry = iterator.next();
            if (entry.getValue().isDestroyed()) {
                iterator.remove();
            }
        }

    }

    private static MortarScope createScreenScope(AbstractScreen screen) {
        Log.e(TAG, "createScreenScope with name " + screen.getScopeName());
        String parentScopeName = getParentScopeName(screen);
        MortarScope parentScope = sScopeMap.get(parentScopeName);
        Object screenComponent = screen.createScreenComponent(parentScope.getService(DaggerService.INSTANCE.getSERVICE_NAME()));
        MortarScope newScope = parentScope.buildChild()
                .withService(DaggerService.INSTANCE.getSERVICE_NAME(), screenComponent)
                .build(screen.getScopeName());
        registerScope(newScope);
        return newScope;
    }
    @Nullable
    private static String getParentScopeName(AbstractScreen screen) {
        try {
            String genericName = ((Class) ((ParameterizedType) screen.getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getName();
            String parentScopeName = genericName;
            if (parentScopeName.contains("$")) {
                parentScopeName = parentScopeName.substring(0, genericName.indexOf("$"));
            }
            return parentScopeName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
