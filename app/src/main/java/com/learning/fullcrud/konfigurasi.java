package com.learning.fullcrud;

public class konfigurasi {
    private static String URL = "http://192.168.43.78/crud_android_api";

    public static final String URL_ADD      = URL + "/create.php";
    public static final String URL_UPDATE   = URL + "/update.php";
    public static final String URL_DELETE   = URL + "/delete.php?id=";
    public static final String URL_ALL      = URL + "/listing.php";
    public static final String URL_DETAIL   = URL + "/detail.php?id=";

    // kunci yang digunakan untuk mengirim permintaan ke API
    public static final String KEY_ID       = "id";
    public static final String KEY_NAMA     = "nama";
    public static final String KEY_POSISI   = "posisi";
    public static final String KEY_GAJI     = "gaji";

    // json tags
    public static final String JSON_ARRAY   = "result";
    public static final String JSON_ID      = "id";
    public static final String JSON_POSISI  = "posisi";
    public static final String JSON_GAJI    = "gaji";
}
