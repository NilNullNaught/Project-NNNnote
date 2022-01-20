import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Test {

    @org.junit.Test
    public void test() {
        String url = "https://nilnullnaught-nnnnote-01.oss-cn-shanghai.aliyuncs.com/2022/01/18/d03491dd73f746beba5d8f4696c93e1a.jpeg";
        url = url.substring(url.lastIndexOf(".com/")+5);
        System.out.println(url);
    }

}
