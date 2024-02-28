package com.belov.mycloud.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Users", schema = "my_cloud")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;

    @Column(unique = true, nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private String path;

    @Column(name = "auth_token")
    private String authToken;
}
