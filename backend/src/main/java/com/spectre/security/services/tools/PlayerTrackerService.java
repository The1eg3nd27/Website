package com.spectre.security.services.tools;

import com.spectre.payload.tools.PlayerInfoDTO;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerTrackerService {

    public PlayerInfoDTO getPlayerInfo(String handle) {
        String url = "https://robertsspaceindustries.com/citizens/" + handle;

        try {
            Document doc = Jsoup.connect(url).get();

            String name = doc.selectFirst("h2.profile-header-username").text();
            String organization = doc.selectFirst(".profile-organization-name") != null
                    ? doc.selectFirst(".profile-organization-name").text() : "None";
            String rank = doc.selectFirst(".profile-organization-rank") != null
                    ? doc.selectFirst(".profile-organization-rank").text() : "None";
            String bio = doc.selectFirst(".profile-bio-content") != null
                    ? doc.selectFirst(".profile-bio-content").text() : "No bio";
            String image = doc.selectFirst(".profile-avatar img") != null
                    ? doc.selectFirst(".profile-avatar img").attr("src") : "";

            return PlayerInfoDTO.builder()
                    .handle(name)
                    .organization(organization)
                    .rank(rank)
                    .bio(bio)
                    .image(image)
                    .pageUrl(url)
                    .build();

        } catch (Exception e) {
            return PlayerInfoDTO.builder()
                    .handle(handle)
                    .bio("Profile not found or error occurred")
                    .organization("Unknown")
                    .rank("Unknown")
                    .pageUrl(url)
                    .build();
        }
    }
}
