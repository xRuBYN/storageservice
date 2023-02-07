package com.esempla.task.domain;

import com.esempla.task.service.dto.UserReservationResponse;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;


@Entity
@Table(name = "user_reservation")
public class UserReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "total_size")
    private Long totalSize;
    @Column(name = "used_size")
    private Long usedSize;
    @Column(name = "activated")
    private boolean activated;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "created_date")
    private Instant createdDate;
    @OneToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public Long getUsedSize() {
        return usedSize;
    }

    public void setUsedSize(Long usedSize) {
        this.usedSize = usedSize;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserReservationResponse toUserReservationResponse() {
        UserReservationResponse userReservationResponse = new UserReservationResponse();
        userReservationResponse.setActivated(this.activated);
        userReservationResponse.setUser_id(this.user.getId());
        userReservationResponse.setCreatedBy(this.createdBy);
        userReservationResponse.setUsedSize(this.usedSize);
        userReservationResponse.setCreatedDate(this.createdDate);
        userReservationResponse.setTotalSize(this.totalSize);
        return userReservationResponse;
    }
}
