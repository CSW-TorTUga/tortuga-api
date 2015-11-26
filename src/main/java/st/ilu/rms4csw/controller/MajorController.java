package st.ilu.rms4csw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import st.ilu.rms4csw.Main;
import st.ilu.rms4csw.controller.exception.NotFoundException;
import st.ilu.rms4csw.model.major.Major;
import st.ilu.rms4csw.repository.MajorRepository;
import st.ilu.rms4csw.patch.Patch;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by hannes on 24.11.15.
 */
@Controller
@RequestMapping("/api/v1/" + MajorController.API_BASE)
@ResponseBody
public class MajorController {

	public final static String API_BASE = "majors";


	private MajorRepository majorRepository;

	@Autowired
	public MajorController(MajorRepository majorRepository) {
		this.majorRepository = majorRepository;
	}

	@RequestMapping
	public List<Major> findAll() {
		return majorRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Major> postMajor(@RequestBody Major major, HttpServletResponse response) {
		Major ret = majorRepository.save(major);
		response.setHeader("Location", Main.getApiBase() + API_BASE + "/" + ret.getId());

		return new ResponseEntity<>(ret, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Major putMajor(@PathVariable String id, @RequestBody Major major) {
		major.setId(id);

		return majorRepository.save(major);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	public Major patchMajor(@PathVariable String id, @RequestBody Major major) {
		Major original = majorRepository.findOne(id);
		if (original == null) {
			throw new NotFoundException("Did not find Major with the id " + id);
		}

		Major patchedMajor = Patch.patch(original, major);

		return majorRepository.save(patchedMajor);
	}

}
