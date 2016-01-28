package us.codecraft.webmagic.meizitu;
import us.codecraft.webmagic.Spider;
 
 
public class ImgSpiderTest {
 
    public static void main(String[] args){
 
        String fileStorePath = "D:\\webmagic\\data\\";
        String urlPattern = "http://www.meizitu.com/[a-z]/[0-9]{1,4}.html";
        ImgProcessor imgspider=new ImgProcessor("http://www.meizitu.com",urlPattern);
 
        //webmagic�ɼ�ͼƬ������ʾ�������վ�����������֮��,��������ɼ�
        Spider.create(imgspider)
                .addUrl("http://www.meizitu.com")
                .addPipeline(new ImgPipeline(fileStorePath))
                .thread(1)       //�˴��߳����ɵ���
                .run();
 
    }
}