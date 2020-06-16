package io.quarkus.hibernate.orm.hibernate_runtime;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BasicEntity {

    @Id
    private long id;

    public BasicEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
