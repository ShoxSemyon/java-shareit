package ru.practicum.shareit.booking.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(schema = "public", name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;


    LocalDateTime startDate;
    LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    User booker;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    BookingStatus bookingStatus;
}
