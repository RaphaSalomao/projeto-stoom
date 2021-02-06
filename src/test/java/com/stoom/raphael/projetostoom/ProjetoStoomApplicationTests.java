package com.stoom.raphael.projetostoom;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stoom.raphael.projetostoom.controller.AddressController;
import com.stoom.raphael.projetostoom.mock.MockAddress;
import com.stoom.raphael.projetostoom.model.Geocode;
import com.stoom.raphael.projetostoom.services.AddressServices;
import com.stoom.raphael.projetostoom.vo.AddressVO;

import junit.framework.Assert;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("deprecation")
@SpringBootTest
@AutoConfigureMockMvc
class ProjetoStoomApplicationTests {

	@Autowired
	private AddressController controller;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private AddressServices addressService;
	
	@Test
	@DisplayName("AddressController.create(): validação do status http 200")
	void case01() throws JsonProcessingException, Exception {
		var in = MockAddress.mockVo();
		mockMvc.perform(MockMvcRequestBuilders.post("/api/address/v1").contentType("application/json")
				.content(objectMapper.writeValueAsString(in))).andExpect(MockMvcResultMatchers.status().is(200));
	}

	@Test
	@DisplayName("AddressController.create(): validação de integridade do objeto de retorno informando latitude e longitude")
	void case02() {
		var in = MockAddress.mockVo();
		var out = controller.create(in);

		Assert.assertEquals(in.getCity(), out.getCity());
		Assert.assertEquals(in.getComplement(), out.getComplement());
		Assert.assertEquals(in.getCountry(), out.getCountry());
		Assert.assertEquals(in.getLatitude(), out.getLatitude());
		Assert.assertEquals(in.getLongitude(), out.getLongitude());
		Assert.assertEquals(in.getNeighbourhood(), out.getNeighbourhood());
		Assert.assertEquals(in.getNumber(), out.getNumber());
		Assert.assertEquals(in.getState(), out.getState());
		Assert.assertEquals(in.getStreetName(), out.getStreetName());
		Assert.assertEquals(in.getZipcode(), out.getZipcode());

		in.setId(out.getId());
		var entity = addressService.findById(out.getId());
		Assert.assertEquals(entity, in);
	}

	@Test
	@DisplayName("AddressController.create(): validacao de status http 422")
	void case03() throws JsonProcessingException, Exception {
		var in = new AddressVO();
		mockMvc.perform(MockMvcRequestBuilders.post("/api/address/v1").contentType("application/json")
				.content(objectMapper.writeValueAsString(in))).andExpect(MockMvcResultMatchers.status().is(422));
	}

	@Test
	@DisplayName("AddressController.findById(): validação do status http 200")
	void case04() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/address/v1/1"))
				.andExpect(MockMvcResultMatchers.status().is(200));
	}

	@Test
	@DisplayName("AddressController.findById(): validação de integridade do objeto de retorno")
	void case05() {
		var out = controller.findById((long) 1);
		var entity = addressService.findById((long) 1);
		Assert.assertEquals(entity, out);
	}

	@Test
	@DisplayName("AddressController.findById(): validação do status http 404")
	void case06() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/address/v1/-1"))
				.andExpect(MockMvcResultMatchers.status().is(404));
	}

	@Test
	@DisplayName("AddressController.update(): validação do status http 200")
	void case07() throws JsonProcessingException, Exception {
		var in = MockAddress.mockVo();
		in.setId((long) 1);
		mockMvc.perform(MockMvcRequestBuilders.put("/api/address/v1").contentType("application/json")
				.content(objectMapper.writeValueAsString(in))).andExpect(MockMvcResultMatchers.status().is(200));
	}

	@Test
	@DisplayName("AddressController.update(): validação de integridade do objeto de retorno")
	void case08() {
		var in = MockAddress.mockVo();
		in.setId((long) 1);
		in.setCountry("USA");
		var out = controller.update(in);
		var entity = addressService.findById((long) 1);
		Assert.assertEquals(in, out);
		Assert.assertEquals(in, entity);
	}

	@Test
	@DisplayName("AddressController.update(): validação do status http 422")
	void case09() throws JsonProcessingException, Exception {
		var in = MockAddress.mockVo();
		in.setId((long) 1);
		in.setCity(null);
		mockMvc.perform(MockMvcRequestBuilders.put("/api/address/v1").contentType("application/json")
				.content(objectMapper.writeValueAsString(in))).andExpect(MockMvcResultMatchers.status().is(422));
	}

	@Test
	@DisplayName("AddressController.update(): validação do status http 404")
	void case10() throws JsonProcessingException, Exception {
		var in = MockAddress.mockVo();
		in.setId((long) -1);
		mockMvc.perform(MockMvcRequestBuilders.put("/api/address/v1").contentType("application/json")
				.content(objectMapper.writeValueAsString(in))).andExpect(MockMvcResultMatchers.status().is(404));
	}

	@Test
	@DisplayName("AddressController.delete(): validação do status http 200")
	void case11() throws JsonProcessingException, Exception {
		long id = addressService.insert(MockAddress.mockVo()).getId();
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/address/v1/" + id))
				.andExpect(MockMvcResultMatchers.status().is(200));
	}
	
	@Test
	@DisplayName("AddressController.delete(): validação do status http 404")
	void case12() throws JsonProcessingException, Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/address/v1/-1"))
				.andExpect(MockMvcResultMatchers.status().is(404));
	}

	@Test
	@DisplayName("AddressController.create(): validação de integridade do objeto de retorno não informando latitude e longitude")
	void case13() {
		String url = "https://maps.googleapis.com/maps/api/geocode/json?address=";
		String key = "&key=AIzaSyCj0cY2yEvVfYhAaTz3-P2MW-YRKmhz5Uw";
		var in = MockAddress.mockVo();
		in.setLatitude((double) 0);
		in.setLongitude((double) 0);
		var out = controller.create(in);
		String geocodeUrl;
		
		String addressParameter = new StringBuilder().append(in.getStreetName()).append(",")
				.append(in.getNumber()).append(",").append(in.getComplement()).append(",")
				.append(in.getCity()).append(",").append(in.getState()).append(",")
				.append(in.getCountry()).append(",").append(in.getZipcode()).toString().replace(" ", "+");
		
		geocodeUrl = new StringBuilder().append(url).append(addressParameter).append(key).toString();
		
		RestTemplate restTemplate = new RestTemplateBuilder().build();
		var geocode = restTemplate.getForObject(geocodeUrl, Geocode.class);
		
		Double lat = geocode.getResults().get(0).getGeometry().getLocation().getLat();
		Double lng = geocode.getResults().get(0).getGeometry().getLocation().getLng();
		
		Assert.assertEquals(in.getCity(), out.getCity());
		Assert.assertEquals(in.getComplement(), out.getComplement());
		Assert.assertEquals(in.getCountry(), out.getCountry());
		Assert.assertEquals(lat, out.getLatitude());
		Assert.assertEquals(lng, out.getLongitude());
		Assert.assertEquals(in.getNeighbourhood(), out.getNeighbourhood());
		Assert.assertEquals(in.getNumber(), out.getNumber());
		Assert.assertEquals(in.getState(), out.getState());
		Assert.assertEquals(in.getStreetName(), out.getStreetName());
		Assert.assertEquals(in.getZipcode(), out.getZipcode());

		in.setId(out.getId());
		in.setLatitude(lat);
		in.setLongitude(lng);
		var entity = addressService.findById(out.getId());
		Assert.assertEquals(entity, in);
	}
}
