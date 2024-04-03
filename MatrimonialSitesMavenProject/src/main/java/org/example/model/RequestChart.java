package org.example.model;

import org.example.enums.RequestStatus;
import org.omg.CORBA.PRIVATE_MEMBER;

import javax.persistence.*;

@Entity
public class RequestChart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    @ManyToOne
    @JoinColumn(name = "my_profile_id")
    private Profile myProfile;

    @ManyToOne
    @JoinColumn(name = "other_profile_id")
    private Profile otherProfile;

    public RequestChart() {

    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Profile getMyProfile() {
        return myProfile;
    }

    public void setMyProfile(Profile myProfile) {
        this.myProfile = myProfile;
    }

    public Profile getOtherProfile() {
        return otherProfile;
    }

    public void setOtherProfile(Profile otherProfile) {
        this.otherProfile = otherProfile;
    }
}

