package dev.solar.parsingreqdata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.solar.parsingreqdata.domain.Image;
import dev.solar.parsingreqdata.domain.SideDish;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class ParsingreqdataApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(ParsingreqdataApplicationTests.class);

    private List<String> emsemsArr = Arrays.asList("HBDEF", "HDF73", "HF778", "HFB53", "H077F", "H4665", "H1AA9", "HEDFB");
    private List<String> soupArr = Arrays.asList("H72C3", "HA6EE", "H8CD0", "HE2E9", "HAA47", "H3254", "H26C7", "HFFF9");
    private List<String> sideArr = Arrays.asList("HBBCC", "H1939", "H8EA5", "H602F", "H9F0B", "H0FC6", "HCCFE", "HB9C1");

    private String insertImageFormat = "INSERT INTO `sidedish`.`image`(`id`, `url`, `side_dish`, `thumb_image_order`, `detail_section_image_order`) VALUES (%s, '%s', %s, %s, %s);";

    @Test
    void 파싱() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(hello("https://h3rb9c0ugl.execute-api.ap-northeast-2.amazonaws.com/develop/baminchan/detail"));
        JsonNode body = node.get("body");

        long i = 1;
        long imageId = 1;
        for (JsonNode jsonNode : body) {
            String hash = jsonNode.get("hash").asText();
            JsonNode data = jsonNode.get("data");
            SideDish sideDish = new SideDish();
            sideDish.setId(i);

            JsonNode hello;

            if (emsemsArr.indexOf(hash) != -1) {
                hello = objectMapper.readTree(hello("https://h3rb9c0ugl.execute-api.ap-northeast-2.amazonaws.com/develop/baminchan/main/" + hash));
            } else if (soupArr.indexOf(hash) != -1) {
                hello = objectMapper.readTree(hello("https://h3rb9c0ugl.execute-api.ap-northeast-2.amazonaws.com/develop/baminchan/soup/" + hash));
            } else if (sideArr.indexOf(hash) != -1) {
                hello = objectMapper.readTree(hello("https://h3rb9c0ugl.execute-api.ap-northeast-2.amazonaws.com/develop/baminchan/side/" + hash));
            } else {
                continue;
            }

            sideDish.setAlt(hello.get("alt").asText());
            sideDish.setTitle(hello.get("title").asText());
            sideDish.setDescription(hello.get("description").asText());

            String n_price = hello.get("n_price") == null ? hello.get("s_price").asText() : hello.get("n_price").asText();
            sideDish.setNormalPrice(Integer.parseInt(n_price.replaceAll(",", "").replaceAll("원", "")));
            sideDish.setSalePrice(Integer.parseInt(hello.get("s_price").asText().replaceAll(",", "").replaceAll("원", "")));
            sideDish.setPoint((int) (sideDish.getSalePrice() * 0.01));
            sideDish.setDeliveryInfo(data.get("delivery_info").asText());
            sideDish.setDeliveryFee(data.get("delivery_fee").asText());

            System.out.println(String.format(insertImageFormat, imageId, hello.get("image").asText(), null, null, null));
            sideDish.setMainImageId(imageId);
            imageId++;

            System.out.println(String.format(insertImageFormat, imageId, data.get("top_image").asText(), null, null, null));
            sideDish.setTopImageId(imageId);
            imageId++;

            System.out.println(sideDish);

            int thumbCount = 0;
            for (JsonNode thumb_image : data.get("thumb_images")) {
                System.out.println(String.format(insertImageFormat, imageId, thumb_image.asText(), i, thumbCount, null));
                thumbCount++;
                imageId++;
            }

            int detailCount = 0;
            for (JsonNode detail_section : data.get("detail_section")) {
                System.out.println(String.format(insertImageFormat, imageId, detail_section.asText(), i, null, detailCount));
                detailCount++;
                imageId++;
            }

            i++;
        }
    }

    private String hello(String url) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }
}
