package com.stoom.raphael.projetostoom.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stoom.raphael.projetostoom.converter.DozerConverter;
import com.stoom.raphael.projetostoom.exception.ResourceNotFoundException;
import com.stoom.raphael.projetostoom.model.Address;
import com.stoom.raphael.projetostoom.repository.AddressRepository;
import com.stoom.raphael.projetostoom.vo.AddressVO;

/**
 * Classe responsável pelas regras de negócio do objeto {@link Address}
 * 
 * @author Raphael
 * @version 1.0
 */
@Service
public class AddressServices {

	@Autowired
	AddressRepository repository;

	/**
	 * Método que insere {@link Address} no banco de dados
	 * 
	 * @param address {@link AddressVO} - Value object de address que será inserido 
	 * @return {@link AddressVO} - Value object de address inserido
	 */
	public AddressVO insert(AddressVO address) {
		var entity = DozerConverter.parseObject(address, Address.class);
		var vo = DozerConverter.parseObject(repository.save(entity), AddressVO.class);
		return vo;
	}

	/**
	 * Método que recupera um {@link AddressVO} do banco de dados
	 * 
	 * @param id {@link Long} - O ID do {@link AddressVO} desejado
	 * @return {@link AddressVO} - O {@link AddressVO} com o ID informado
	 * @throws ResourceNotFoundException
	 */
	public AddressVO findById(Long id) {
		var vo = DozerConverter.parseObject(
				repository.findById(id)
						.orElseThrow(() -> new ResourceNotFoundException("There are no records for this id: " + id)),
				AddressVO.class);
		return vo;
	}

	/**
	 * Método que realiza um update de um registro na tabela address
	 * 
	 * @param address {@link AddressVO} - O address value object com os novos atributos
	 * @return {@link AddressVO} - O address value object com os atributos atualizados
	 * @throws ResourceNotFoundException
	 */
	public AddressVO update(AddressVO address) {
		var entity = repository.findById(address.getId()).orElseThrow(
				() -> new ResourceNotFoundException("There are no records for this id: " + address.getId()));
		entity = DozerConverter.parseObject(address, Address.class);
		var vo = DozerConverter.parseObject(repository.save(entity), AddressVO.class);
		return vo;
	}

	/**
	 * Método que deleta um {@link Address} da tabela address
	 * 
	 * @param id {@link Long} - O ID do address que será eliminado
	 * @throws ResourceNotFoundException
	 */
	public void delete(Long id) {
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("There are no records for this id: " + id));
		repository.delete(entity);
	}

}
