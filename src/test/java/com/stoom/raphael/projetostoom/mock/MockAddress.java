package com.stoom.raphael.projetostoom.mock;

import com.stoom.raphael.projetostoom.vo.AddressVO;

public class MockAddress {
	
	public static AddressVO mockVo() {
		return new AddressVO("Praça da Sé"
				, "000"
				, "lado ímpar"
				, "Sé"
				, "São Paulo"
				, "SP"
				, "Brasil"
				, "01001-000"
				, (double)-23.54
				, (double)-46.44);
	}
	
}
