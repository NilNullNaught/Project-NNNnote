import cn.nilnullnaught.nnnnote.oss.config.AliyunOssConfig;
import cn.nilnullnaught.nnnnote.oss.utils.AliyunOssUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CopyObjectResult;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Test {

    @org.junit.Test
    public void test() {

        String source = "temporary/2022/01/22/af001bb346f14ed2953c78fa85d6c0b4.jpeg";
        String destination = "af001bb346f14ed2953c78fa85d6c0b4.jpeg";
        OSS ossClient = null;

        try {
            // 创建OSS实例。
            ossClient = new OSSClientBuilder().build(
                    "oss-cn-shanghai.aliyuncs.com",
                    "LTAI5tSJA3Yvh8xCs594cezp",
                    "SOfoROARddkycUJv4Z3aKTUsB4FX6t");
            CopyObjectResult result = ossClient.copyObject("nilnullnaught-nnnnote-01", source, "nilnullnaught-nnnnote-01", destination);
        } finally {
            if (ossClient != null) {
                // 关闭OSSClient。
                ossClient.shutdown();
            }
        }
    }

    @org.junit.Test
    public void test2(){
        String oldUrl = "https://nilnullnaught-nnnnote-01.oss-cn-shanghai.aliyuncs.com/2022/01/22/3f9d657af44f4199a7f483fbff51ef28.jpe";
        oldUrl = oldUrl.substring(oldUrl.lastIndexOf(".com/")+5);
        System.out.println(oldUrl);
    }
}
