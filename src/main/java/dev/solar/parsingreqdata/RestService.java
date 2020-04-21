package dev.solar.parsingreqdata;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class RestService {

    public ResponseEntity<String> getItemsFromMockApi() {
        String url = "https://h3rb9c0ugl.execute-api.ap-northeast-2.amazonaws.com/develop/baminchan/detail";

        RestTemplate restTemplate = new RestTemplate();

        UriComponents builder = UriComponentsBuilder.fromHttpUrl(url).build(false);

        ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, String.class );
        return response;
    }
}
