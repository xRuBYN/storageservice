package com.esempla.task.repository;

import com.esempla.task.domain.StorageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StorageFileRepository extends JpaRepository<StorageFile, Long> {

    List<StorageFile> findAllByCreatedBy(String createdBy);

    StorageFile findStorageFileById(Long id);
}
