package com.dino.back_end_for_TTECH.features.identity.application.model;

import com.dino.back_end_for_TTECH.features.identity.application.pattern.AuthFacade;
import com.dino.back_end_for_TTECH.features.identity.application.pattern.ILoginBodyStrategy;
import com.dino.back_end_for_TTECH.features.profile.domain.User;
import jakarta.validation.constraints.Size;

public record LoginPhoneBody(
        @Size(message = "SĐT gồm 10 ký tự", min = 10)
        String phone,

        @Size(message = "Password ít nhất 8 kí tự", min = 8)
        String password
) implements ILoginBodyStrategy {

        @Override
        public User checkBody(AuthFacade authFacade) {
                var user = authFacade.checkPhone(phone);
                authFacade.checkPassword(user, password);
                return user;
        }
}