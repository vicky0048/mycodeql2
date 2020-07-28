package pl.dmichalski.reservations.business.validation;

import java.util.Optional;

import org.springframework.stereotype.Component;
import pl.dmichalski.reservations.business.domain.entity.room.RoomEntity;

import static pl.dmichalski.reservations.business.util.constant.ConstMessagesEN.ValidationMessages.REQUIRED_DATA_NOT_FILLED_OR_BAD_DATA;

@Component
public class RoomValidator extends ValidationSupport implements Validator<RoomEntity> {

    @Override
    public Optional<ValidationError> validate(RoomEntity room) {
        if (isNullValue(room.getRoomStatus()) ||
                !isValueGreaterThanZero(room.getNumberOfPeople()) ||
                isNullOrEmptyString(room.getDescription())) {
            return Optional.of(new ValidationError(REQUIRED_DATA_NOT_FILLED_OR_BAD_DATA));
        }
        return Optional.empty();
    }

}
