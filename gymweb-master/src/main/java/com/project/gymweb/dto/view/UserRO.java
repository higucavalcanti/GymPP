package com.project.gymweb.dto.view;

import java.util.UUID;

public record UserRO(UUID id, String username, String email) {
}
