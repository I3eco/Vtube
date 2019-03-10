package com.vtube.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class ContentDTO implements Idto{

	@NonNull
	private String content;
}
