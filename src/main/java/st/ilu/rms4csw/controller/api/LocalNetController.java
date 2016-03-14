package st.ilu.rms4csw.controller.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import st.ilu.rms4csw.util.NetworkUtil;

/**
 * Created by hannes on 14.03.16.
 */
@Controller
@RequestMapping("/api/v1/localnet")
public class LocalNetController {

    @RequestMapping()
    public ResponseEntity<Void> localNet() {
        if(NetworkUtil.isLocalNetworkRequest()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
