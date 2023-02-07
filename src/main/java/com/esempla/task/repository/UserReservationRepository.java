package com.esempla.task.repository;

import com.esempla.task.domain.User;
import com.esempla.task.domain.UserReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReservationRepository extends JpaRepository<UserReservation, Long> {
    UserReservation findUserReservationByUser(User user);

    UserReservation findUserReservationByUserId(Long id);

}
