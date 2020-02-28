package com.expenses.extract.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.expenses.extract.exceptions.BadRequestException;
import com.expenses.extract.service.ExtractService;

@RestController
@RequestMapping(value="/extract")
public class ExtractRest {
	@Autowired
	private ExtractService extractService;

	@PostMapping("/ofx")
	@ResponseStatus(code = HttpStatus.OK)
	public void readExtract(@RequestBody String filePath) throws BadRequestException {
		extractService.readExtract(filePath);
	}
	
	@PostMapping("/csv")
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public void readtExtract(@RequestBody String filePath) {
		System.out.println("Em implementação!");
	}
	
	@GetMapping("/health")
	@ResponseStatus(code = HttpStatus.OK)
	public void health() {}
}
