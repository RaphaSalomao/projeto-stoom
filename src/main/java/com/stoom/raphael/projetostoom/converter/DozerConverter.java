package com.stoom.raphael.projetostoom.converter;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

/** Classe responsável para conversão do objeto entidade para o Value Object equivalente
 * 
 * @author Raphael
 *
 */
public class DozerConverter {

	private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();
	
	/** Método que converte uma entidade para o Value Object equivalente ou vice-versa
	 * 
	 * @param origin {@link Object} - Objeto origem
	 * @param destination {@link Object.class} - Classe do objeto destino
	 * @return
	 */
	public static <O,D> D parseObject(O origin, Class<D> destination) {
		return mapper.map(origin, destination);
	}
	
}
