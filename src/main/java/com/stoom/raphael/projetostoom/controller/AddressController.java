package com.stoom.raphael.projetostoom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.stoom.raphael.projetostoom.model.Address;
import com.stoom.raphael.projetostoom.model.Geocode;
import com.stoom.raphael.projetostoom.services.AddressServices;
import com.stoom.raphael.projetostoom.vo.AddressVO;

/** Controlador responsável pelas requisições relacionadas a Address
 * 
 * @author Raphael
 * @version 1.0
 */
@RestController
@RequestMapping("/api/address/v1")
public class AddressController {

	@Autowired
	private AddressServices service;

	/**
	 * Este metodo lida com requisições POST e insere o {@link Address} no banco de dados 
	 * verificando se a latitude e longitude são válidos. Caso sejam inválidos ou não sejam
	 * informados, será utilizada a Geocoding API para obter esses dados corretamente
	 * 
	 * @param address {@link AddressVO} - O address que será inserido
	 * @return {@link AddressVO} - O address inserido
	 */
	@PostMapping(produces = { "application/json", "application/xml" }, consumes = { "application/json",
			"application/xml" })
	public AddressVO create(@RequestBody AddressVO address) {
		String url = "https://maps.googleapis.com/maps/api/geocode/json?address=";
		String key = "&key=AIzaSyCj0cY2yEvVfYhAaTz3-P2MW-YRKmhz5Uw";
		String geocodeUrl;
		String geocodeStatus;
		boolean isLatLngValid;
		try {
			geocodeUrl = new StringBuilder().append(url)
					.append(address.getLatitude().toString() + "," + address.getLongitude().toString()).append(key)
					.toString();
			System.out.println("geocodeUrl = " + geocodeUrl);
			geocodeStatus = restTemplate(new RestTemplateBuilder()).getForObject(geocodeUrl, Geocode.class).getStatus();
			System.out.println("geocodeStatus = " + geocodeStatus);
			isLatLngValid = geocodeStatus.equals("OK");
		} catch (HttpClientErrorException | NullPointerException ex) {
			isLatLngValid = false;
		}

		if (!isLatLngValid) {

			String addressParameter = new StringBuilder().append(address.getStreetName()).append(",")
					.append(address.getNumber()).append(",").append(address.getComplement()).append(",")
					.append(address.getCity()).append(",").append(address.getState()).append(",")
					.append(address.getCountry()).append(",").append(address.getZipcode()).toString().replace(" ", "+");
			geocodeUrl = new StringBuilder().append(url).append(addressParameter).append(key).toString();
			System.out.println("isLatLngValid = " + isLatLngValid);
			System.out.println("key = " + key);
			System.out.println("url = " + url);
			System.out.println("addressParameter = " + addressParameter);
			System.out.println("geocodeUrl = " + geocodeUrl);
			var geocode = restTemplate(new RestTemplateBuilder()).getForObject(geocodeUrl, Geocode.class);
			address.setLatitude(geocode.getResults().get(0).getGeometry().getLocation().getLat());
			address.setLongitude(geocode.getResults().get(0).getGeometry().getLocation().getLng());
		}
		return service.insert(address);
	}

	/**
	 * Este método lida com requisições GET e recupera o {@link AddressVO}  
	 * cujo ID seja igual ao informado.
	 * 
	 * @param id {@link Long} - The ID of the disired {@link Address}
	 * @return {@link AddressVO} - The address found
	 */
	@GetMapping(value = "/{id}", produces = { "application/json", "application/xml" })
	public AddressVO findById(@PathVariable("id") Long id) {
		return service.findById(id);
	}

	/**
	 * Este método lida com requisições PUT e faz um update do {@link Address} 
	 * cujo ID seja igual ao informado.
	 * 
	 * @param address {@link AddressVO} - The address wich will be updated
	 * @return {@link AddressVO} - The updated address
	 */
	@PutMapping(produces = { "application/json", "application/xml" }, consumes = { "application/json",
			"application/xml" })
	public AddressVO update(@RequestBody AddressVO address) {
		return service.update(address);
	}

	/**
	 * Este método lida com requisições DELETE e apaga o {@link Address}
	 * cujo ID seja igual ao informado.
	 * 
	 * @param id {@link Long} - The ID of the {@link Address} wich will be deleted
	 * @return {@link ResponseEntity} - A status Ok
	 */
	@DeleteMapping(value = "/{id}", produces = { "application/json", "application/xml" })
	public ResponseEntity<?> delete(@PathVariable("id") Long id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}
	
	/** Método responsável pela criação de um {@link RestTemplate} válido
	 * 
	 * @param builder - {@link RestTemplateBuilder}
	 * @return {@link RestTemplate}
	 */
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}
