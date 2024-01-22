package ipb.pt.timetableapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/data")
public class DataController {
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/load")
    public void load() {
        ResponseEntity<String> data = restTemplate.exchange("http://localhost:8081/lessons", HttpMethod.GET, null, String.class);
        System.out.println(data.getBody());
    }
}
