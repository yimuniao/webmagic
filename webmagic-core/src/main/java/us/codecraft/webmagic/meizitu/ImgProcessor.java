package us.codecraft.webmagic.meizitu;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Xpath2Selector;
import us.codecraft.webmagic.utils.UrlUtils;
import java.util.List;
 
public class ImgProcessor implements PageProcessor {
 
    private String urlPattern;
 
    private Site site;
 
    public ImgProcessor(String startUrl, String urlPattern) {
        this.site = Site.me().setDomain(UrlUtils.getDomain(startUrl));
        this.urlPattern= urlPattern;
    }
 
    @Override
    public void process(Page page) {
 
        String imgRegex = "http://www.meizitu.com/wp-content/uploads/20[0-9]{2}[a-z]/[0-9]{1,4}/[0-9]{1,4}/[0-9]{1,4}.jpg";
        List<String> requests = page.getHtml().links().regex(urlPattern).all();
        System.out.println("links===== "+ requests);
        page.addTargetRequests(requests);
//        String imgHostFileName = page.getHtml().xpath("//title/text()").toString().replaceAll("[|\\pP¡®¡¯¡°¡±\\s(ÃÃ×ÓÍ¼)]", "");
//        List<String> listProcess = page.getHtml().$("div#picture").regex(imgRegex).all();
//        List<String> listProcess = page.getHtml().xpath("//div[@id='picture']/p//img/@src").all();
//        page.putField("img", listProcess);
//        System.out.println("-------   "+listProcess);
        Xpath2Selector xpath2Selector = new Xpath2Selector("//div[@id='picture']/p//img/@src");
        List<String> imgList = xpath2Selector.selectList(page.getRawText());
//        selectList.forEach((a)-> {System.out.println(a);});
        for (String string : imgList) {
            System.out.println("=========" + string);
        }
        
        page.putField("img", imgList);
    }
 
    @Override
    public Site getSite() {
        return site;
    }
}