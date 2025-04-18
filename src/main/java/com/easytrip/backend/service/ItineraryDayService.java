package com.easytrip.backend.service;

import com.easytrip.backend.dto.VacationDto;
import com.easytrip.backend.model.Activity;
import com.easytrip.backend.model.ItineraryDay;
import com.easytrip.backend.model.Vacation;
import com.easytrip.backend.repository.ItineraryDayRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItineraryDayService {

    private final ItineraryDayRepository itineraryDayRepository;
    private final GeminiService geminiService;

    public List<ItineraryDay> generateItineraryDays(Vacation vacation) throws JsonProcessingException {

        int persons = switch (vacation.getTravelCompanion()) {
            case SOLO -> 1;
            case COUPLE -> 2;
            case FAMILY -> 3;
            case FRIENDS -> 4;
            default -> 10;
        };

        List<String> activityTypes = vacation.getActivities().stream()
                .map(Activity::getActivityType)
                .collect(Collectors.toList());

        String prompt = generatePrompt(vacation.getCountry(), vacation.getDays(),
                activityTypes, vacation.getBudget(), persons);


//      String answer = geminiService.getAnswer(prompt);
        String answer = getAns();
        String jsonText = extractItineraryJson(answer);

        return mapJsonToItineraryDays(jsonText,vacation);
    }

    private List<ItineraryDay> mapJsonToItineraryDays(String jsonText, Vacation vacation) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonText);
        JsonNode itineraryNode = rootNode.path("itinerary");

        List<ItineraryDay> itineraryDays = new ArrayList<>();
        for (JsonNode dayNode : itineraryNode) {
            ItineraryDay itineraryDay = ItineraryDay.builder()
                    .dayNumber(dayNode.path("dayNumber").asInt())
                    .morning(dayNode.path("morning").asText())
                    .afternoon(dayNode.path("afternoon").asText())
                    .evening(dayNode.path("evening").asText())
                    .build();

            itineraryDays.add(itineraryDay);
        }

        for(ItineraryDay day : itineraryDays){
            day.setVacation(vacation);
            itineraryDayRepository.save(day);
        }

        return itineraryDays;
    }

    public String generatePrompt(String location, int days, List<String> activities,
                                 BigDecimal budget, int persons) {
        String template = """
        I want you to generate a 3-day vacation itinerary for me in the following format:
        {
        	"dayNumber": day number, (as int)
        	"morning": describe the morning activity (as string),
        	"afternoon": describe the afternoon activity (as string),
        	"evening": describe the evening activity (as string)
        }
        For the following details:
        Location: {{location}}
        Number of days: {{days}}
        Preferred activities: {{activities}}
        Budget: {{budget}} euros
        Person/s: {{persons}}
        Please generate the response as a JSON object in the format described above! Give me just the JSON nothing else!!!
        """;

        return template
                .replace("{{location}}", location)
                .replace("{{days}}", String.valueOf(days))
                .replace("{{activities}}", String.join(", ", activities))
                .replace("{{budget}}", String.valueOf(budget))
                .replace("{{persons}}", String.valueOf(persons));
    }

    private String extractItineraryJson(String response) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            JsonNode candidates = rootNode.path("candidates");
            JsonNode parts = candidates.get(0).path("content").path("parts");
            String text = parts.get(0).path("text").asText();

            return text.trim().replace("```json\n", "").replace("\n```", "");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    //I am using this to avoid using all the gemini requests xD
    public String getAns(){
        return "{\n" +
                "  \"candidates\": [\n" +
                "    {\n" +
                "      \"content\": {\n" +
                "        \"parts\": [\n" +
                "          {\n" +
                "            \"text\": \"```json\\n{\\n  \\\"itinerary\\\": [\\n    {\\n      \\\"dayNumber\\\": 1,\\n      \\\"morning\\\": \\\"Arrive in Bucharest (OTP Airport). Transfer to your hotel near the city center. Begin with a visit to the Palace of the Parliament, the second largest administrative building in the world.\\\",\\n      \\\"afternoon\\\": \\\"Explore Bucharest's Old Town (Centru Vechi). Wander through its narrow streets, admire the architecture, and have lunch at a traditional Romanian restaurant.\\\",\\n      \\\"evening\\\": \\\"Enjoy a traditional Romanian dinner with live music at a restaurant in the Old Town. Consider Caru' cu Bere for an authentic experience.\\\"\\n    },\\n    {\\n      \\\"dayNumber\\\": 2,\\n      \\\"morning\\\": \\\"Visit the Village Museum (Muzeul Satului), an open-air ethnographic museum showcasing traditional Romanian village life. Explore authentic houses and buildings from different regions.\\\",\\n      \\\"afternoon\\\": \\\"Visit the Romanian Athenaeum, a stunning concert hall and architectural landmark. Afterwards, relax in Herastrau Park, Bucharest's largest park.\\\",\\n      \\\"evening\\\": \\\"Enjoy a leisurely dinner at a restaurant in Herastrau Park or return to the city center for more dining options. Consider a food tour for a curated culinary experience.\\\"\\n    },\\n    {\\n      \\\"dayNumber\\\": 3,\\n      \\\"morning\\\": \\\"Take a day trip to Sinaia, a mountain resort town in the Carpathian Mountains. Visit Peles Castle, a magnificent Neo-Renaissance castle that was once the summer residence of the Romanian royal family.\\\",\\n      \\\"afternoon\\\": \\\"Explore Sinaia Monastery, a historical and religious site. Take a cable car up to a higher elevation for panoramic views of the surrounding mountains.\\\",\\n      \\\"evening\\\": \\\"Return to Bucharest. Have dinner at a restaurant near your hotel or try another restaurant in the Old Town.\\\"\\n    },\\n    {\\n      \\\"dayNumber\\\": 4,\\n      \\\"morning\\\": \\\"Visit the National Museum of Romanian History, delving deeper into Romania's rich past. Explore its exhibits showcasing artifacts from prehistoric times to the present day.\\\",\\n      \\\"afternoon\\\": \\\"Do some souvenir shopping at local markets or in the Old Town. Enjoy a final Romanian lunch.\\\",\\n      \\\"evening\\\": \\\"Depart from Bucharest (OTP Airport).\\\"\\n    }\\n  ]\\n}\\n```\"\n" +
                "          }\n" +
                "        ],\n" +
                "        \"role\": \"model\"\n" +
                "      },\n" +
                "      \"finishReason\": \"STOP\",\n" +
                "      \"avgLogprobs\": -0.24894108077094296\n" +
                "    }\n" +
                "  ],\n" +
                "  \"usageMetadata\": {\n" +
                "    \"promptTokenCount\": 138,\n" +
                "    \"candidatesTokenCount\": 487,\n" +
                "    \"totalTokenCount\": 625,\n" +
                "    \"promptTokensDetails\": [\n" +
                "      {\n" +
                "        \"modality\": \"TEXT\",\n" +
                "        \"tokenCount\": 138\n" +
                "      }\n" +
                "    ],\n" +
                "    \"candidatesTokensDetails\": [\n" +
                "      {\n" +
                "        \"modality\": \"TEXT\",\n" +
                "        \"tokenCount\": 487\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"modelVersion\": \"gemini-2.0-flash\"\n" +
                "}";
    }

}
