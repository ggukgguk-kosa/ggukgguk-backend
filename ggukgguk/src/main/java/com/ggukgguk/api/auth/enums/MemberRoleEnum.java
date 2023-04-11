package com.ggukgguk.api.auth.enums;

import java.util.Arrays;

public enum MemberRoleEnum {

	ROLE_MEMBER("ROLE_MEMBER", false), ROLE_ADMIN("ROLE_ADMIN", true);

	private final String label;
	private final boolean isAdmin;
	
	MemberRoleEnum(String label, boolean isAdmin) {
		this.label = label;
		this.isAdmin = isAdmin;
	}
	
	public String label() {
		return label;
	}
	
	public boolean isAdmin() {
		return isAdmin;
	}
	
	public static MemberRoleEnum valueOfLabel(String label) {
        return Arrays.stream(values())
                .filter(value -> value.label.equals(label))
                .findAny()
                .orElse(null);
	}

	public static MemberRoleEnum valueByBoolean(boolean isAdmin) {
        return Arrays.stream(values())
                .filter(value -> value.isAdmin == isAdmin)
                .findAny()
                .orElse(null);
	}
}
