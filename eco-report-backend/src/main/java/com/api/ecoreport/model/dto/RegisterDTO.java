package com.api.ecoreport.model.dto;

import com.api.ecoreport.model.enums.UserRole;

public record RegisterDTO(String email, String password, String name, String neighborhood, UserRole role) {
}
