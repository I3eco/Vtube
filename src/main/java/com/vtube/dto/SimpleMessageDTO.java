package com.vtube.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class SimpleMessageDTO implements Idto{
	@NonNull
	private String message;
}
