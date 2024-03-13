package com.belov.mycloud.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.util.MimeType;

import java.nio.file.Path;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_files", schema = "my_cloud")
public class UserFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_file_seq")
//    @SequenceGenerator(name = "user_file_seq", sequenceName = "user_files_id_seq")
    private long ID;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "file_hash", nullable = false)
    private String fileHash;

    @ManyToOne
    @JoinColumn(nullable = false, referencedColumnName = "name")
    private User user;

    @Column(columnDefinition = "boolean default false")
    private boolean deleted;
}