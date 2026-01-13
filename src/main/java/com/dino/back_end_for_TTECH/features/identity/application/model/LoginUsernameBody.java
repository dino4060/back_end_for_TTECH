package com.dino.back_end_for_TTECH.features.identity.application.model;

import com.dino.back_end_for_TTECH.features.identity.application.pattern.AuthFacade;
import com.dino.back_end_for_TTECH.features.identity.application.pattern.ILoginBodyStrategy;
import com.dino.back_end_for_TTECH.features.profile.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginUsernameBody (
        @NotBlank(message = "Username is required")
        String username,

        @Size(message = "Password gồm 8 ký tự", min = 8)
        String password

) implements ILoginBodyStrategy {
        @Override
        public User checkBody(AuthFacade authFacade) {
                var user = authFacade.checkUsername(username);
                authFacade.checkPassword(user, password);
                return user;
        }
}
