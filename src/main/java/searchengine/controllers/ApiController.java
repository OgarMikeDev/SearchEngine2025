package searchengine.controllers;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import searchengine.config.SiteConfig;
import searchengine.config.SitesList;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.services.StatisticsService;

import java.io.FileWriter;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {
    private final StatisticsService statisticsService;
    private final SitesList sitesList;

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @RequestMapping("/startIndexing")
    public void startIndexing() {
        goTheLinksSites();
    }

    public void goTheLinksSites() {
        for (SiteConfig currentSiteConfigConfig : sitesList.getSites()) {
            System.out.println("Текущий сайт с файла конфигурации: " + currentSiteConfigConfig);
            String urlCurrentSite = currentSiteConfigConfig.getUrl();
            String nameCurrentSite = currentSiteConfigConfig.getName();
            try {
                Document document = Jsoup.connect(urlCurrentSite).get();
                String strHtmlCurrentPage = String.valueOf(document);

                FileWriter fileWriter = new FileWriter("src/main/resources/data/sites/site_" + nameCurrentSite + ".html");
                fileWriter.write(strHtmlCurrentPage);

                Elements elements = document.select(".menu__nav-link._is-extra");
                for (int numberCurrentPage = 0; numberCurrentPage < 10; numberCurrentPage++) {
                    Element currentElement = elements.get(numberCurrentPage);
                    String strCurrentElement = String.valueOf(currentElement);
                    goTheLinksPages(urlCurrentSite, nameCurrentSite, strCurrentElement, numberCurrentPage);
                }
            } catch (Exception ex) {
                ex.getMessage();
            }
        }
    }

    public void goTheLinksPages(String currentUrl, String nameCurrentSite, String strCurrentElement, int numberCurrentPage) {
        try {
            String template = "href=\"";
            int leftIndex = strCurrentElement.indexOf(template);
            if (leftIndex != -1) {
                leftIndex += template.length();
                int rightIndex = strCurrentElement.indexOf("\"", leftIndex);
                String partCurrentLink = strCurrentElement.substring(leftIndex, rightIndex);
                String fullCurrentLink = currentUrl + partCurrentLink;
                System.out.println("♨\n" + fullCurrentLink + "\n♨");
                String strHtmlCodeCurrentPage = String.valueOf(Jsoup.connect(fullCurrentLink).get());
                FileWriter fileWriter = new FileWriter(
                        "src/main/resources/data/pages/page" + nameCurrentSite + numberCurrentPage + ".html");
                fileWriter.write(strHtmlCodeCurrentPage);
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }
}

