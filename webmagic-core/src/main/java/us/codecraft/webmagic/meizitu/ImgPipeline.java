package us.codecraft.webmagic.meizitu;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;
 
 
@ThreadSafe
public class ImgPipeline extends FilePersistentBase implements Pipeline {
 
    private Logger logger = LoggerFactory.getLogger(getClass());
    public ImgPipeline() {
        setPath("/data/webmagic/");
    }
 
    public ImgPipeline(String path) {
        setPath(path);
    }
 
 
    @Override
    public void process(ResultItems resultItems, Task task) {
        String fileStorePath = this.path;
        try {
 
            String imgShortNameNew="(http://)";
            CloseableHttpClient httpclient = HttpClients.createDefault();
            Object object = resultItems.get("img");
            if (object != null && object instanceof List)
            {
                List<String> images=(List)object;
                for (String imageLink : images) {
                    String replaceFirst = imageLink.replaceAll(imgShortNameNew, "").replaceFirst(".*?/", "");
                    String subpath = replaceFirst.substring(0, replaceFirst.lastIndexOf("/")).replaceAll("/", "_");

                    StringBuffer sb = new StringBuffer();
                    StringBuffer imgFileNameNewYuan = sb.append(fileStorePath)
                            .append(subpath) ;
                    StringBuffer imgFileNameNew = imgFileNameNewYuan
                            .append(replaceFirst.substring(replaceFirst.lastIndexOf("/")));

                    HttpGet httpget = new HttpGet(imageLink);
                    HttpResponse response = httpclient.execute(httpget);
                    HttpEntity entity = response.getEntity();
                    InputStream in = entity.getContent();

                    File file = getFile(imgFileNameNew.toString().replaceAll("\\\\", "/"));

                    try {
                        FileOutputStream fout = new FileOutputStream(file);
                        int l = -1;
                        byte[] tmp = new byte[1024];
                        while ((l = in.read(tmp)) != -1) {
                            fout.write(tmp, 0, l);
                        }
                        fout.flush();
                        fout.close();
                    } finally {

                        in.close();
                    }

                }
            }
            
            httpclient.close();
        } catch (IOException e) {
            logger.warn("write file error", e);
        }
    }
}