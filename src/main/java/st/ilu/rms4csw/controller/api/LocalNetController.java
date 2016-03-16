package st.ilu.rms4csw.controller.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import st.ilu.rms4csw.util.NetworkUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Hannes Güdelhöfer
 */
@RestController
@RequestMapping("/api/v1/localnet")
public class LocalNetController {
    @RequestMapping
    public Boolean localNet(HttpServletRequest request) {
        return NetworkUtil.isLocalNetworkRequest(request);
    }
}
