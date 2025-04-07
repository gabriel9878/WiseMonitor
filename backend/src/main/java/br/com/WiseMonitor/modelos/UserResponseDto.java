package br.com.WiseMonitor.modelos;

import java.util.List;

public record UserResponseDto(
		
		String login,
		String email,
		UserRole role,
		List<Device> devices

		
) {}
