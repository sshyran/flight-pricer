package io.yac.flight.pricer.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "qpx_cache")
public class QpxCache {

    @Id
    @SequenceGenerator(name = "qpx_cache_id_seq", sequenceName = "qpx_cache_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qpx_cache_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "request_hash", nullable = false)
    private int requestHash;

    @Column(name = "request_json", nullable = false)
    private String requestJson;

    @Column(name = "response_json", nullable = false)
    private String responseJson;

    @Version
    private Long version;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;


    public QpxCache() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRequestHash() {
        return requestHash;
    }

    public void setRequestHash(int requestHash) {
        this.requestHash = requestHash;
    }

    public String getRequestJson() {
        return requestJson;
    }

    public void setRequestJson(String requestJson) {
        this.requestJson = requestJson;
    }

    public String getResponseJson() {
        return responseJson;
    }

    public void setResponseJson(String responseJson) {
        this.responseJson = responseJson;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PrePersist
    public void prePersit() {
        this.setCreatedAt(new Date());
        this.setUpdatedAt(new Date());
    }

    @PreUpdate
    public void preUpdate() {
        this.setUpdatedAt(new Date());
    }
}
