package com.spectre.security.services.tools;


import com.spectre.payload.tools.PlayerInfoDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class PlayerTrackerService {

    public PlayerInfoDTO fetchPlayerInfo(String handle) {
        try {
            Document mainDoc = Jsoup.connect("https://robertsspaceindustries.com/citizens/" + handle)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            Element root = mainDoc.selectFirst(".profile-content.overview-content.clearfix");

            String sid = getTextByLabel(root, "Spectrum Identification (SID)");
            String rank = getTextByLabel(root, "Organization rank");
            String location = getTextByLabel(root, "Location");
            String language = getTextByLabel(root, "Fluency");
            String joinDate = getTextByLabel(root, "Enlisted");

            Elements infoElements = mainDoc.select(".info");
            String organization = getElementText(infoElements, 1, ".value");

            String memberCount = getMemberCount(sid);

            return new PlayerInfoDTO(
                    handle.toUpperCase(),
                    defaultIfBlank(sid, "Nicht verfügbar"),
                    defaultIfBlank(organization, "Nicht verfügbar"),
                    defaultIfBlank(rank, "Nicht verfügbar"),
                    defaultIfBlank(location, "Nicht verfügbar"),
                    defaultIfBlank(language, "Nicht verfügbar"),
                    defaultIfBlank(joinDate, "Nicht verfügbar"),
                    defaultIfBlank(memberCount, "Nicht verfügbar")
            );

        } catch (Exception e) {
            return new PlayerInfoDTO(
                    handle.toUpperCase(),
                    "Nicht verfügbar",
                    "Nicht verfügbar",
                    "Nicht verfügbar",
                    "Nicht verfügbar",
                    "Nicht verfügbar",
                    "Nicht verfügbar",
                    "Nicht verfügbar"
            );
        }
    }

    private String getTextByLabel(Element root, String labelText) {
        if (root == null) return null;
        Elements labels = root.select(".label");
        for (Element label : labels) {
            if (label.text().trim().equalsIgnoreCase(labelText)) {
                Element value = label.nextElementSibling();
                return value != null ? value.text().trim() : null;
            }
        }
        return null;
    }

    private String getElementText(Elements elements, int index, String selector) {
        Element element = elements.size() > index ? elements.get(index).selectFirst(selector) : null;
        return element != null ? element.text().trim() : null;
    }

    private String getMemberCount(String sid) {
        try {
            if (sid == null || sid.isBlank()) return null;

            Document doc = Jsoup.connect("https://robertsspaceindustries.com/orgs/" + sid + "/members")
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            Element countEl = doc.selectFirst(".logo .count");
            return countEl != null ? countEl.text().trim() : null;

        } catch (Exception e) {
            return null;
        }
    }

    private String defaultIfBlank(String input, String fallback) {
        return (input == null || input.isBlank()) ? fallback : input;
    }
}