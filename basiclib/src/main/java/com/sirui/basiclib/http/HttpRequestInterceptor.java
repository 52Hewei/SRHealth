package com.sirui.basiclib.http;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.lzy.okgo.request.ProgressRequestBody;
import com.net.client.config.HttpContants;
import com.sirui.basiclib.config.NetUrl;
import com.sirui.basiclib.config.SRConstant;
import com.sirui.basiclib.utils.EncryptUtils;
import com.sirui.basiclib.utils.string.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.RealResponseBody;
import okio.Buffer;

import static com.sirui.basiclib.config.SRConstant.APP_ID;
import static com.sirui.basiclib.config.SRConstant.APP_ID_KEY;
import static com.sirui.basiclib.config.SRConstant.APP_SECRET;
import static com.sirui.basiclib.config.SRConstant.DATA;
import static com.sirui.basiclib.config.SRConstant.ENCODE_DATA_KEY;
import static com.sirui.basiclib.config.SRConstant.ENCODE_FIELDS_KEY;
import static com.sirui.basiclib.config.SRConstant.ERR_CODE_KEY;
import static com.sirui.basiclib.config.SRConstant.ERR_DESC_KEY;
import static com.sirui.basiclib.config.SRConstant.NONCE_KEY;
import static com.sirui.basiclib.config.SRConstant.REQUEST_ID_KEY;
import static com.sirui.basiclib.config.SRConstant.SIGNATURE_KEY;
import static com.sirui.basiclib.config.SRConstant.TIMESTAMP_KEY;


/**
 * 接口加密加签拦截器
 * Created by yellow on 17/5/23.
 */

public class HttpRequestInterceptor implements Interceptor {

    private static final String LOG_TAG = "HTTP_REQUEST_INTERCEPTOR";

    /**
     * 加密字段分隔符
     */
    private static final String DIVIDER = ",";

    private int FLAG_ENCRYPT;
    private int FLAG_DECRYPT;

    private MediaType APPLICATION_JSON;
    private MediaType FORM_DATA;
    private final String ENCODE_CHARSET = "UTF-8";
    private final String DIGEST_METHOD = "SHA-1";

    private static long requestId = new Random().nextInt(100000);
    private static String PUB_KEY_DATA = "";
    private static String PUB_KEY_FIELD = "";
    private Set<String> urlWhiteList;

    public HttpRequestInterceptor() {
        initVariable();
    }

    /**
     * 初始化常量数据
     */
    private void initVariable() {
        APPLICATION_JSON = MediaType.parse("application/json;charset=utf-8");
        FORM_DATA = MediaType.parse("application/x-www-form-urlencoded");
        FLAG_ENCRYPT = 1;
        FLAG_DECRYPT = 2;

        urlWhiteList = new HashSet<>();
        urlWhiteList.add(NetUrl.URL_GET_PUBLIC_KEY);
    }

    public void addWhiteUrl(String url){
        urlWhiteList.add(url);
    }

    /**
     * 检查加密 KEY 是否存在，判断是否可以发送请求
     *
     * @return 是否可以发送请求
     */
    public static boolean canSendRequest() {
        return !TextUtils.isEmpty(PUB_KEY_DATA) && !TextUtils.isEmpty(PUB_KEY_FIELD);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        PUB_KEY_DATA = HttpKeyManager.getInstance().getDataRsaKey();
        PUB_KEY_FIELD = HttpKeyManager.getInstance().getFieldRsaKey();

        if (APPLICATION_JSON == null
                || FORM_DATA == null) {
            initVariable();
        }

        // 不在白名单，所有公钥不为空，则加密
        String requestUrl = chain.request().url().toString();
        if (urlWhiteList.contains(requestUrl)) {
            Log.d(SRConstant.TAG, "白名单接口，不加密放行：" + requestUrl);
            return chain.proceed(chain.request());
        } else {
            requestId = requestId + 1;
            if (!TextUtils.isEmpty(PUB_KEY_DATA) && !TextUtils.isEmpty(PUB_KEY_FIELD)) {
                Request originalRequest = chain.request();
                Request securedRequest = getSecuredRequest(originalRequest);

                Response securedResponse = chain.proceed(securedRequest);

                Response decryptJson = decryptResponse(securedResponse);

                if (decryptJson != null) {
                    return decryptJson;
                } else {
                    Log.w(HttpContants.LOG_TAG, "接口解密失败：" + requestUrl);
                    return securedResponse;
                }

            } else {
                Log.w(HttpContants.LOG_TAG, "未成功获取 KEY，接口请求取消：" + requestUrl);
                // 再次尝试获取加密密钥
                HttpKeyManager.getInstance().updateRsaKey();
            }
        }
        return new Response.Builder().request(chain.request()).protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create(APPLICATION_JSON,
                        "{ \"" + ERR_DESC_KEY + "\": \"请求失败\", \"" + ERR_CODE_KEY + "\": \"666666\" }"))
                .code(200).build();
    }


    /**
     * 获取加密请求对象
     *
     * @param originalRequest 原始请求对象
     * @return 加密后的请求对象，若出错则不处理直接返回原始对象
     */
    private Request getSecuredRequest(@NonNull Request originalRequest) {
        Request encryptRequest = null;
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonce = getRandomString(16);
        String encryptFiledNames = "";

        try {
            // 处理 Get 请求
            if (TextUtils.equals("GET", originalRequest.method())) {
                HttpUrl originalUrl = originalRequest.url();
                int size = originalUrl.querySize();
                HashMap<String, String> parameter = new HashMap<>();

                for (int i = 0; i < size; i++) {
                    parameter.put(originalUrl.queryParameterName(i), originalUrl.queryParameterValue(i));
                }
                encryptFiledNames = parameter.get(ENCODE_FIELDS_KEY) == null ? "" : parameter.get(ENCODE_FIELDS_KEY);
                // 加密特定加密字段
                Map<String, String> filedEncryptParameter = encryptParameter(parameter, encryptFiledNames);
                // 移除加密字段参数
                filedEncryptParameter.remove(ENCODE_FIELDS_KEY);

                JSONObject getJson = new JSONObject(filedEncryptParameter);
                // 整体加密
                String encryptQueryStr = encryptString(getJson.toString(), PUB_KEY_DATA, Base64.URL_SAFE)
                        .replace("\n", "").replace("=","");
                // 加签
                String digestStr = digestString(APP_SECRET, timeStamp, nonce, getJson.toString());
                // 重新组合加密 get 请求
                HttpUrl securedUrl = getSecureGetUrl(timeStamp, nonce, parameter.get(ENCODE_FIELDS_KEY), digestStr, encryptQueryStr, originalUrl);
                // 重新生成请求对象
                encryptRequest = originalRequest.newBuilder().url(securedUrl).build();

                return encryptRequest;

                // 处理 POST 请求
            } else if (canEncryptBody(originalRequest)) {
                RequestBody originalBody = originalRequest.body();
                JSONObject postJson = new JSONObject();

                // 处理标准 FORM 单个表单，转换成 JSON 对象
                if (FORM_DATA.equals(originalBody.contentType())) {
                    Map<String, Object> parameter = new HashMap<>();
                    if (originalBody instanceof ProgressRequestBody) {
                        // FIXME: 17/6/28 替换当前的反射解决方法
                        Field field = ProgressRequestBody.class.getDeclaredField("delegate");
                        field.setAccessible(true);
//                        FormBody formBody = (FormBody) FormBody.create(originalBody.contentType(), originalBody.toString());
                        FormBody formBody = (FormBody) field.get(originalBody);
                        for (int i = 0; i < formBody.size(); i++) {
                            String value = formBody.value(i);
                            // 存在用 post 发送 Json 的情况，在这里做特殊处理
                            if (value.startsWith("{") && value.endsWith("}")) {
                                parameter.put(formBody.name(i), new JSONObject(value));
                            }else if(value.startsWith("[") && value.endsWith("]")){
                                parameter.put(formBody.name(i), new JSONArray(value));
                            }else {
                                parameter.put(formBody.name(i), value);
                            }
                        }
                        postJson = new JSONObject(parameter);
                    }
                }

                // 从 JSON 请求中获取 JSON 对象
                if (APPLICATION_JSON.equals(originalBody.contentType())) {
                    Buffer buffer = new Buffer();
                    originalBody.writeTo(buffer);
                    postJson = new JSONObject(buffer.readUtf8());
                    encryptFiledNames = postJson.optString(ENCODE_FIELDS_KEY);
                }

                // 将需要加密的字段加密
                JSONObject encryptedJsonObj = processJsonField(postJson, encryptFiledNames, FLAG_ENCRYPT);
                // 移除加密字段参数
                encryptedJsonObj.remove(ENCODE_FIELDS_KEY);
                // 请求 Json 整体加密
                String encryptedJsonStr = encryptString(encryptedJsonObj.toString(), PUB_KEY_DATA, Base64.URL_SAFE);
                // 加签
                String digestStr = digestString(APP_SECRET, timeStamp, nonce, encryptedJsonObj.toString());
                // 重新组合加密表单
                FormBody formBody = getSecureFormBody(timeStamp, nonce, postJson.optString(ENCODE_FIELDS_KEY), digestStr, encryptedJsonStr);
                // 重新生成请求对象
                encryptRequest = originalRequest.newBuilder().post(formBody).build();

                return encryptRequest;
            } else {
                return originalRequest;
            }
        } catch (Exception e) {
            Log.e(SRConstant.TAG, "Interceptor Secured Request Error: " + e.getMessage());
            return originalRequest;
        }

    }

    /**
     * 解密从服务器获取的 Response
     *
     * @param securedResponse 服务直接返回的加密 Response
     * @return 解密后的 Response
     */
    @Nullable
    private Response decryptResponse(Response securedResponse) {
        RealResponseBody responseBody = (RealResponseBody) securedResponse.body();
        // 仅处理 JSON response
        if (securedResponse.isSuccessful()
                && responseBody != null
                && responseBody.contentType() != null
                && APPLICATION_JSON.subtype().equals(responseBody.contentType().subtype())) {
            try {
                String responseStr = responseBody.source().readUtf8();
                JSONObject securedJson = null;

                securedJson = new JSONObject(responseStr);
                String code = securedJson.optString(ERR_CODE_KEY);
                String encodeData = securedJson.optString(DATA);
                // 仅解密请求接口成功，且有数据返回的 Response，其余原样放行
                if (SRConstant.SUCCESS_CODE.equals(code) && !StringUtil.isEmpty(encodeData)) {
                    String nonce = securedJson.optString(NONCE_KEY);
                    String timesTamp = securedJson.optString(TIMESTAMP_KEY);
                    String signature = securedJson.optString(SIGNATURE_KEY);
                    String encodeFields = securedJson.optString(ENCODE_FIELDS_KEY);

                    String decryptString = decryptString(encodeData, PUB_KEY_DATA, Base64.NO_WRAP);
                    String localDigest = digestString(APP_SECRET, timesTamp, nonce, decryptString);
                    if (localDigest.equals(signature)) {
                        JSONObject encryptFieldJson = new JSONObject(decryptString);
                        JSONObject decryptJson = processJsonField(encryptFieldJson, encodeFields, FLAG_DECRYPT);
                        JSONObject fullDecryptJson = securedJson.put(DATA, decryptJson);
                        return securedResponse.newBuilder().body(ResponseBody.create(APPLICATION_JSON, fullDecryptJson.toString())).build();
                    }
                } else {
                    return securedResponse.newBuilder().body(ResponseBody.create(APPLICATION_JSON, securedJson.toString())).build();
                }

            } catch (Exception e) {
                Log.e(SRConstant.TAG, "Decrypt Response Error: " + e);
                e.printStackTrace();
            }
        }
        return securedResponse;
    }

    /**
     * 获取符合接口加密的表单对象
     *
     * @param timeStamp        时间戳
     * @param nonce            16 位随机字符串
     * @param encodeFieldNames {@link #DIVIDER} 分割的加密字段名
     * @param digestStr        加签摘要字符串
     * @param encryptedJsonStr JSON 整体加密后的字符串
     * @return 请求表单对象
     */
    private FormBody getSecureFormBody(String timeStamp, String nonce, String encodeFieldNames, String digestStr, String encryptedJsonStr) {
        FormBody.Builder builder = new FormBody.Builder();
        String urlSafeStr = encryptedJsonStr.replace("\n", "").replace("=", "");
        builder.add(NONCE_KEY, nonce)
                .add(APP_ID_KEY, APP_ID)
                .add(TIMESTAMP_KEY, timeStamp)
                .add(SIGNATURE_KEY, digestStr)
                .add(REQUEST_ID_KEY, String.valueOf(requestId))
                .add(ENCODE_DATA_KEY, urlSafeStr);
        if (!StringUtil.isEmpty(encodeFieldNames)) {
            builder.add(ENCODE_FIELDS_KEY, encodeFieldNames);
        }
        Log.d(SRConstant.TAG, NONCE_KEY + ":" + nonce + ";\n"
                + APP_ID_KEY + ":" + APP_ID + ";\n"
                + TIMESTAMP_KEY + ":" + timeStamp + ";\n"
                + SIGNATURE_KEY + ":" + digestStr + ";\n"
                + REQUEST_ID_KEY + ":" + requestId + ";\n"
                + ENCODE_DATA_KEY + ":" + urlSafeStr + ";\n"
                + ENCODE_FIELDS_KEY + ":" + encodeFieldNames + ";\n"
        );

        return builder.build();
    }

    /**
     * 获取符合接口加密的 HttpUrl 对象
     *
     * @param timeStamp        时间戳
     * @param nonce            16 位随机字符串
     * @param encodeFieldNames {@link #DIVIDER} 分割的加密字段名
     * @param digestStr        加签摘要字符串
     * @param encryptDataStr   全加密 URL 请求串
     * @param originalUrl      原始 HttpUrl 对象
     * @return 加密后 HttpUrl 对象
     */
    private HttpUrl getSecureGetUrl(String timeStamp, String nonce, String encodeFieldNames, String digestStr, String encryptDataStr, HttpUrl originalUrl) {
        String urlSafeStr = encryptDataStr.replace("\n", "").replace("=", "");
        String baseUrl = originalUrl.isHttps() ? "https://" : "http://";
        baseUrl += originalUrl.host() + ":" + originalUrl.port() + originalUrl.encodedPath();
        HttpUrl encryptUrl = HttpUrl.parse(baseUrl);
        if (encryptUrl != null) {
            HttpUrl.Builder builder = encryptUrl.newBuilder();
            builder.addQueryParameter(NONCE_KEY, nonce)
                    .addQueryParameter(APP_ID_KEY, APP_ID)
                    .addQueryParameter(TIMESTAMP_KEY, timeStamp)
                    .addQueryParameter(SIGNATURE_KEY, digestStr)
                    .addQueryParameter(REQUEST_ID_KEY, String.valueOf(requestId))
                    .addQueryParameter(ENCODE_DATA_KEY, urlSafeStr);
            if (!StringUtil.isEmpty(encodeFieldNames)) {
                builder.addQueryParameter(ENCODE_FIELDS_KEY, encodeFieldNames);
            }
            Log.d(SRConstant.TAG, NONCE_KEY + ":" + nonce + ";\n"
                    + APP_ID_KEY + ":" + APP_ID + ";\n"
                    + TIMESTAMP_KEY + ":" + timeStamp + ";\n"
                    + SIGNATURE_KEY + ":" + digestStr + ";\n"
                    + REQUEST_ID_KEY + ":" + requestId + ";\n"
                    + ENCODE_DATA_KEY + ":" + urlSafeStr + ";\n"
                    + ENCODE_FIELDS_KEY + ":" + encodeFieldNames + ";\n"
            );
            return builder.build();
        } else {
            return originalUrl;
        }
    }

    /**
     * 对 Json 特定字段加密
     *
     * @param jsonObj 需要加密的 JsonObject
     * @return 完成加密的 JsonObject
     */
    private JSONObject processJsonField(JSONObject jsonObj, String encodeFieldNames, int flag) {
        Log.d(SRConstant.TAG, "Original json string: " + jsonObj);
        if (!StringUtil.isEmpty(encodeFieldNames)) {
            try {
                String[] fieldNames = encodeFieldNames.split(DIVIDER);
                if (fieldNames.length > 0) {
                    for (String field : fieldNames) {
                        String originalValue = jsonObj.optString(field);
                        String processedValue = null;
                        if (FLAG_ENCRYPT == flag) {
                            processedValue = encryptString(originalValue, PUB_KEY_FIELD, Base64.NO_WRAP);
                        } else if (FLAG_DECRYPT == flag) {
                            processedValue = decryptString(originalValue, PUB_KEY_FIELD, Base64.NO_WRAP);
                        }
                        jsonObj.put(field, processedValue);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(SRConstant.TAG, "Processed json string: " + jsonObj);
        }
        return jsonObj;
    }

    /**
     * 对特定参数键值对进行加密
     *
     * @param parameter 使用 {swi.tyonline.config.SRConstant#ENCODE_FIELDS_KEY} 标记需要加密的键值对参数 Map
     * @return 加密处理后的参数 Map
     */
    private Map<String, String> encryptParameter(Map<String, String> parameter, String encryptFiledNames) {
        if (!TextUtils.isEmpty(encryptFiledNames)) {
            try {
                String[] pNames = encryptFiledNames.split(DIVIDER);
                if (pNames.length > 0) {
                    for (String s : pNames) {
                        String originalValue = parameter.get(s);
                        String encryptValue = encryptString(originalValue, PUB_KEY_FIELD, Base64.NO_WRAP);
                        parameter.put(s, encryptValue);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Set<Map.Entry<String, String>> entries = parameter.entrySet();
            Log.d(SRConstant.TAG, "Encrypted form string: " + entries);
        }
        return parameter;
    }

    /**
     * 将 Key-Value 形式的查询参数转换成 URL 查询字符串
     *
     * @param parameter 参数容器
     * @return URL 查询参数
     */
    private String parameterToUrlQueryStr(Map<String, String> parameter) {
        HttpUrl.Builder builder = new HttpUrl.Builder();
        for (Map.Entry<String, String> item : parameter.entrySet()) {
            builder.addQueryParameter(item.getKey(), item.getValue());
        }
        builder.scheme("http");
        builder.host("localhost");
        HttpUrl url = builder.build();
        Log.d(SRConstant.TAG, "URL generate string: " + url.toString());
        return url.query();
    }

    /**
     * 对字符串 UTF-8 编码后使用公钥加密
     *
     * @param str 需要加密的字符串
     * @return 加密数据的 BASE64 编码
     * @throws Exception 加密错误
     */
    private String encryptString(String str, String base64PubKey, int base64Flag) throws Exception {
        byte[] encryptData = EncryptUtils.encryptByPublicKey(str.getBytes(ENCODE_CHARSET), base64PubKey);
        return Base64.encodeToString(encryptData, base64Flag);
    }

    /**
     * BASE64 编码的密文使用公钥解密
     *
     * @param encryptBase64 需要解密的 BASE64 编码内容
     * @return 解密数据的 BASE64 编码
     * @throws Exception 加密错误
     */
    private String decryptString(String encryptBase64, String base64PubKey, int base64Flag) throws Exception {
        byte[] decryptData = EncryptUtils.decryptByPublicKey(Base64.decode(encryptBase64, base64Flag), base64PubKey);
        return new String(decryptData, ENCODE_CHARSET);
    }

    /**
     * 对字符串进行数字摘要
     *
     * @param params 多个 String，会 Arrays.sort 排序后进行拼接
     * @return 数字摘要的十六进制字符串
     */
    private String digestString(String... params) {
        Arrays.sort(params);
        StringBuilder strBuffer = new StringBuilder();
        MessageDigest digester;
        for (String p : params) {
            strBuffer.append(p);
        }
        Log.d(SRConstant.TAG, "Digest source: " + strBuffer.toString());
        try {
            digester = MessageDigest.getInstance(DIGEST_METHOD);
            digester.update(strBuffer.toString().getBytes(ENCODE_CHARSET));
            return EncryptUtils.getHexStr(digester.digest());
        } catch (Exception e) {
            Log.e(SRConstant.TAG, "Digest error: " + e.getMessage());
        }
        return "";
    }

    /**
     * 生成一定长度的随机字符串[A-Za-z0-9]
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    private String getRandomString(int length) {
        String container = "0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            randomString.append(container.charAt(random.nextInt(container.length())));
        }
        return randomString.toString();
    }

    /**
     * 是否能够进行请求体处理
     *
     * @param request 请求对象
     * @return 能否进行处理
     */
    private boolean canEncryptBody(Request request) {
        if (request == null) {
            return false;
        }
        if (!TextUtils.equals("POST", request.method())) {
            return false;
        }
        RequestBody body = request.body();
        if (body == null) {
            return false;
        }
        MediaType mediaType = body.contentType();
        if (mediaType == null) {
            return false;
        }
        if (!TextUtils.equals("x-www-form-urlencoded", mediaType.subtype())) {
            return false;
        }
        return true;
    }

}
