package com.ing.service;

import com.ing.dto.RegisterRequestDTO;
import com.ing.dto.RegisterResponseDTO;

public interface RegisterService {

	RegisterResponseDTO registerCustomer(RegisterRequestDTO requestDTO);

	String generateOTPandSendMail(String string);
	
}
