package discover;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TestController {

    @RequestMapping({"", "/"})
    public Object index() {
        return Map.of("time", System.currentTimeMillis());
    }


}
