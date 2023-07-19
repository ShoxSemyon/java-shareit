package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.ReadOnlyProperty;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.User;

import javax.persistence.*;

/**
 * TODO Sprint add-controllers.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;// — уникальный идентификатор вещи;

    String name;// — краткое название;
    String description;//— развёрнутое описание;

    @Column(name = "is_available")
    Boolean available;// — статус о том, доступна или нет вещь для аренды;

    @ManyToOne(fetch = FetchType.LAZY)
    User owner;// — владелец вещи;

    @Column(name = "request_id")
    Long request;//

}
