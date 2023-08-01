package ru.practicum.subscription.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.user.model.User;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Entity
@Table(name = "subscriptions", schema = "public")
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "author")
    private User author;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "subscriber")
    private User subscriber;
}