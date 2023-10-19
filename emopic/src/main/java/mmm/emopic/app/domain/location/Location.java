package mmm.emopic.app.domain.location;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mmm.emopic.app.base.BaseEntity;
import mmm.emopic.app.domain.photo.Photo;

import javax.persistence.*;
import java.awt.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String full_address;
    private String address_1depth;
    private String address_2depth;
    private String address_3depth;
    private String address_4depth;

    @Column(scale = 6)
    private Double latitude;

    @Column(scale = 6)
    private Double longitude;

    private Long photoId;

    @Builder
    public Location(Long id, String full_address, String address_1depth, String address_2depth, String address_3depth, String address_4depth, Double latitude, Double longitude, Long photoId) {
        this.id = id;
        this.full_address = full_address;
        this.address_1depth = address_1depth;
        this.address_2depth = address_2depth;
        this.address_3depth = address_3depth;
        this.address_4depth = address_4depth;
        this.latitude = latitude;
        this.longitude = longitude;
        this.photoId = photoId;
    }
}
