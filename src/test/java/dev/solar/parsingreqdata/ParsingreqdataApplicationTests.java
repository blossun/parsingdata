package dev.solar.parsingreqdata;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class ParsingreqdataApplicationTests {
    private static final Logger log = LoggerFactory.getLogger(ParsingreqdataApplicationTests.class);

    @Autowired
    RestService restService;

    @Test
    void contextLoads() {
    }


    @Test
    void 파싱() throws ParseException, JSONException {
        ResponseEntity<String> response = restService.getItemsFromMockApi();
        log.debug("response : {}" , response.toString());
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
//        JSONObject bodyObj = (JSONObject) jsonObj.get("body");
        JSONArray bodyArray = (JSONArray) jsonObj.get("body");
//        JSONArray bodyObj = (JSONArray) jsonParser.parse("body");

        for (int i = 0; i < bodyArray.size(); i++) {
            JSONObject itemObj = (JSONObject) bodyArray.get(i);
            String itemHash = (String) itemObj.get("hash");
            log.debug("itemHash : {}", itemHash);
            JSONObject dataObj = (JSONObject) itemObj.get("data");
            JSONArray thumbImages = (JSONArray) dataObj.get("thumb_images");
            for (int j = 0; j < thumbImages.size(); j++) {
                String imgUrl = (String) thumbImages.get(j);
                log.debug("imgUrl : {}", imgUrl);
//                System.out.println("imgUrl : " + imgUrl);
            }
        }

    }
}
