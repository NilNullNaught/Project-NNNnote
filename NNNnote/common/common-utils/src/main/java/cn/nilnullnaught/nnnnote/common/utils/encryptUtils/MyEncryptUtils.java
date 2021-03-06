package cn.nilnullnaught.nnnnote.common.utils.encryptUtils;

import java.util.ArrayList;

public class MyEncryptUtils {

    private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public static String encodeByBCrypt(String rawPassword) {
        return bCryptPasswordEncoder.encode(rawPassword);
    }

    public static Boolean checkByBCrypt(String rawPassword, String encodedPassword) {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
    }

    public static void main(String[] args) {

        // region <- 测试 BCrypt 加密与解密效率 ->

//        long startTime=System.currentTimeMillis(); //获取开始时间
//        ArrayList<String> strings = new ArrayList<>();
//        for (int i =0;i<=99;i++){
//            String code =  MyEncryptUtils.encodeByBCrypt("123456789");
//            strings.add(code);
//            System.out.println(code);
//        }
//        long endTime=System.currentTimeMillis(); //获取结束时间
//        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
//
//        for (String code : strings){
//            Boolean flag = MyEncryptUtils.checkByBCrypt("123456789",code);
//            System.out.println(flag);
//        }
//
//        endTime=System.currentTimeMillis(); //获取结束时间
//        System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
        // endregion

        for (int i = 1; i < 9; i++) {
            long raw = 11111111;

            String password = String.valueOf(raw * i);
            String code = MyEncryptUtils.encodeByBCrypt(password);
            System.out.println(password + "=" + code);

        }
    }
}
