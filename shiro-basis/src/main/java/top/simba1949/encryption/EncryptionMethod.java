package top.simba1949.encryption;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @author SIMBA1949
 * @date 2020/6/27 12:52
 */
public class EncryptionMethod {
    public static void main(String[] args) {
        String password = "123456";

        // 使用 md5 setter 方法不会对密码进行加密
        Md5Hash md5HashWithoutEncryption = new Md5Hash();
        md5HashWithoutEncryption.setBytes(password.getBytes());
        System.out.println(md5HashWithoutEncryption.toHex());

        // 使用 md5，使用构造方法才会对密码进行加密
        Md5Hash md5Hash = new Md5Hash(password);
        System.out.println("使用md5构造方法：" + md5Hash.toHex());

        // 使用 md5 + salt + 散列次数
        // 盐
        String salt = "X0*7ps";
        // 散列次数
        int hashIterations = 1024;
        Md5Hash md5HashWithSaltAndHashIterations = new Md5Hash(password, salt, hashIterations);
        System.out.println("使用md5构造方法（加盐加散列次数）：" + md5HashWithSaltAndHashIterations.toHex());
    }
}
