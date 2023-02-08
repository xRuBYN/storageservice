package com.esempla.task.domain;

import com.esempla.task.service.dto.StorageFileResponse;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "storage_file")
public class StorageFile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "size")
    private Long size;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "path")
    private String path;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "created_date")
    private Instant createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StorageFile that = (StorageFile) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(size, that.size) && Objects.equals(mimeType, that.mimeType) && Objects.equals(path, that.path) && Objects.equals(createdBy, that.createdBy) && Objects.equals(createdDate, that.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, size, mimeType, path, createdBy, createdDate);
    }

    @Override
    public String toString() {
        return "StorageFile{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", size=" + size +
            ", mimeType='" + mimeType + '\'' +
            ", path='" + path + '\'' +
            ", createdBy='" + createdBy + '\'' +
            ", createdTime=" + createdDate +
            '}';
    }

    public StorageFileResponse toStorageFileResponse(){
        StorageFileResponse storageFileResponse = new StorageFileResponse();
        storageFileResponse.setId(this.id);
        storageFileResponse.setCreatedBy(this.createdBy);
        storageFileResponse.setName(this.name);
        storageFileResponse.setPath(this.path);
        storageFileResponse.setSize(this.size);
        storageFileResponse.setMimeType(this.mimeType);
        storageFileResponse.setCreatedDate(this.createdDate);
        return storageFileResponse;
    }
}
