package top.simba1949.util;


import java.util.UUID;

/**
 * @author SIMBA1949
 * @date 2020/6/27 21:19
 */
public class EncryptionUtils {

    public static final int HASH_ITERATIONS = 1024;

    /**
     * 获取盐
     * @return
     */
    public static String getSalt(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().substring(0, 10);
    }
}