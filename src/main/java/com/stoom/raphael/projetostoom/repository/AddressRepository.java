package com.stoom.raphael.projetostoom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stoom.raphael.projetostoom.model.Address;

/** Classe responsável para lidar com as transações de  {@link Address} com o banco de dados 
 * 
 * @author Raphael
 * @version 1.0
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
