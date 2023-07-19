package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.ReadOnlyProperty;
import ru.practicum.shareit.user.dto.UserDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@ValidateDateStartAndEnd
public class BookingShortDto {
    long id;
    @ReadOnlyProperty
    long bookerId;
}
