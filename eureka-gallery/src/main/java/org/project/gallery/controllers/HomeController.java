package org.project.gallery.controllers;

import java.util.List;

import org.project.gallery.entities.Gallery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/")
public class HomeController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Environment env;

	/*
	 * Home(): it is useful for debugging scenario in case there were multiple
	 * instances of "gallery service" running at different ports. Therefore, balance
	 * will be loading among them, and which instance received the request will be
	 * displayed.
	 */

	/**
	 * Method used to determine which instance received a request from gallery
	 * service.
	 * 
	 * @return Message containing port number.
	 */
	@RequestMapping("/")
	public String home() {

		// This is useful for debugging
		// when having multiple instances of "gallery service" running at different
		// ports. Therefore, balance will be loading among them is, and display which
		// instance received the request.
		return "Hello from Gallery Service running at port: " + env.getProperty("local.server.port");
	}

	@RequestMapping("/{id}")
	public Gallery getGallery(@PathVariable final int id) {

		Gallery gallery = new Gallery();
		gallery.setId(id);

		// Get list of available images
		List<Object> images = restTemplate.getForObject("http://image-service/images/", List.class);
		gallery.setImages(images);

		return gallery;
	}

	// -------- Admin Area --------
	// This method is only accessible to admin roles
	@RequestMapping("/admin")
	public String homeAdmin() {
		return "This is the admin area of Gallery service running at port: " + env.getProperty("local.server.port");
	}

}
