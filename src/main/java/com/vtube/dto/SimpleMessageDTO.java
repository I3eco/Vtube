package com.vtube.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**Simple text object which can be returned as JSON
 * @author I3eco
 * 
 */
@Data
@NoArgsConstructor
public class SimpleMessageDTO implements Idto{
	@NonNull
	private String message;
}
