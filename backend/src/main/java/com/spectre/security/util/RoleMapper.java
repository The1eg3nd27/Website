package com.spectre.security.util;

import java.util.List;

public class RoleMapper {

    private static final String ADMIN_ROLE_ID = "1151938988064649217";
    private static final String USER_ROLE_ID = "1151938673412149288";
    private static final String DEV_ROLE_ID   = "1371899852589367316";

    public static String mapToInternalRole(List<String> discordRoleIds) {
        if (discordRoleIds.contains(ADMIN_ROLE_ID)) {
            return "ROLE_ADMIN";
        } else if (discordRoleIds.contains(DEV_ROLE_ID)) {
            return "ROLE_DEVELOPER";
        } else if (discordRoleIds.contains(USER_ROLE_ID)) {
            return "ROLE_USER";
        }
        return "ROLE_GUEST";
    }
}
