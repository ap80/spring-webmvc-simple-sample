package com.innovez.sample.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Contoh greeting controller serderhana.
 * 
 * @author zakyalvan
 */
@Controller
public class GreetingController {
	private static final Logger LOGGER = LoggerFactory.getLogger(GreetingController.class);
	
	/**
	 * Handle request dengan uri "/hello". Jika request parameter "name" tidak
	 * diberikan maka nilai default untuk parameter "name" akan di berikan
	 * dengan nilai "Spring User"
	 * 
	 * @param name
	 * @param model
	 * @return Nama view untuk menampilkan hasil response dari request terhadap
	 *         controller method ini
	 */
	@RequestMapping(value="/hello")
	public String hello(@RequestParam(value="name", required=false, defaultValue="Spring User") String name, Model model) {
		LOGGER.debug("Handle greet hello request for name {}", name);
		model.addAttribute("name", name);
		return "greet/hello";
	}
}
