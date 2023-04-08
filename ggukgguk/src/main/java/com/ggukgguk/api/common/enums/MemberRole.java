package com.ggukgguk.api.common.enums;

import java.util.Arrays;

public enum MemberRole {

	ROLE_MEMBER("ROLE_MEMBER", 'M'), ROLE_ADMIN("ROLE_ADMIN", 'A');

	private final String label;
	private final char code;
	
	MemberRole(String label, char code) {
		this.label = label;
		this.code = code;
	}
	
	public String label() {
		return label;
	}
	
	public char code() {
		return code;
	}
	
	public static MemberRole valueOfLabel(String label) {
        return Arrays.stream(values())
                .filter(value -> value.label.equals(label))
                .findAny()
                .orElse(null);
	}

	public static MemberRole valueOfCode(char code) {
        return Arrays.stream(values())
                .filter(value -> value.code == code)
                .findAny()
                .orElse(null);
	}
}
