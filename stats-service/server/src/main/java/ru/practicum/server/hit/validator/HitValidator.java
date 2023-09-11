package ru.practicum.server.hit.validator;

import ru.practicum.dto.hit.HitDto;

public class HitValidator {

    public static void checkHit(HitDto hitDto) {
        if (hitDto.getApp().contains(" ")) {
            throw new HitValidationException("app cannot leave spaces in names");
        }

        if (hitDto.getUri().contains(" ")) {
            throw new HitValidationException("uri cannot leave spaces in names");
        }
    }
}
