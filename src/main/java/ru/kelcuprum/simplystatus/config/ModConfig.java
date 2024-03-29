package ru.kelcuprum.simplystatus.config;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ModConfig {
    public static boolean debugPresence = false;
    /**
     * Базовое ID RPC мода
     */
    public static String baseID;
    /**
     * ID с другим названием в духе Minecraft
     */
    public static String mineID;
    /**
     * default иконки для GUI конфигов
     */
    public static AssetsConfig defaultAssets;
    /**
     * default иконки для GUI конфигов с использованием ссылок
     */
    public static AssetsConfig defaultUrlsAssets;
    /**
     * Список доступных для выбора ассеты
     */
    public static String[] assetsList = new String[]{"Default"};
    /**
     * JSON обьект ассетов
     */
    public static JSONObject assets;
    /**
     * String версия конфигов
     */
    public static String MOD_CONFIG_STRING = "";
    /**
     * Использование default конфигов мода, чтобы не указывать в коде и не искать потом их везде
     * + Облеглчение работы ребят которые делают кастомы
     */
    public ModConfig() throws Exception {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream stream = loader.getResourceAsStream("simplystatus.config.mod.json");
        MOD_CONFIG_STRING = IOUtils.toString(stream, StandardCharsets.UTF_8);
        JSONObject config = new JSONObject(MOD_CONFIG_STRING);
        for (String key : config.keySet()) {
            switch (key.toLowerCase()) {
                case "baseid" -> baseID = config.getString(key);
                case "debugpresence" -> debugPresence = config.getBoolean(key);
                case "mineid" -> mineID = config.getString(key);
                case "assets" -> {
                    JSONObject json = config.getJSONObject(key);
                    assets = json;
                    for (String keyJSON : json.keySet()) {
                        switch (keyJSON.toLowerCase()) {
                            case "default" -> defaultAssets = new AssetsConfig(json.getJSONObject(keyJSON));
                            case "~urls" -> defaultUrlsAssets = new AssetsConfig(json.getJSONObject(keyJSON));
                        }
                    }
                }
                case "assets_list" -> assetsList = jsonArrayToStringArray(config.getJSONArray("assets_list"));
            }
        }
        if(baseID == null) throw new Exception("Не найден baseID, который требуется для запуска мода!");
        if(assets == null) throw new Exception("Не найден набор иконок, который требуется для запуска мода!");
        if(defaultAssets == null) throw new Exception("Не найдены стандартные иконки, который требуется для запуска мода!");
        if(defaultUrlsAssets == null) throw new Exception("Не найдены стандартные ссылки на иконки, который требуется для запуска мода!");
        MOD_CONFIG_STRING = config.toString();
    }
    public String[] jsonArrayToStringArray(JSONArray jsonArray) {
        int arraySize = jsonArray.length();
        String[] stringArray = new String[arraySize];

        for(int i=0; i<arraySize; i++) {
            stringArray[i] = jsonArray.getString(i);
        }

        return stringArray;
    };
}
