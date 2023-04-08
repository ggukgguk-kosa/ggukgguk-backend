package com.ggukgguk.api.auth.enums;

import java.util.Arrays;

public enum MemberRoleEnum {

	ROLE_MEMBER("ROLE_MEMBER", 'M'), ROLE_ADMIN("ROLE_ADMIN", 'A');

	private final String label;
	private final char code;
	
	MemberRoleEnum(String label, char code) {
		this.label = label;
		this.code = code;
	}
	
	public String label() {
		return label;
	}
	
	public char code() {
		return code;
	}
	
	public static MemberRoleEnum valueOfLabel(String label) {
        return Arrays.stream(values())
                .filter(value -> value.label.equals(label))
                .findAny()
                .orElse(null);
	}

	public static MemberRoleEnum valueOfCode(char code) {
        return Arrays.stream(values())
                .filter(value -> value.code == code)
                .findAny()
                .orElse(null);
	}
}
