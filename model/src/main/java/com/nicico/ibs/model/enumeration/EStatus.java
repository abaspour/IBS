package com.nicico.ibs.model.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EStatus {
	Active(1),
	Deactive(2);

	// ------------------------------

	private final Integer id;
}
