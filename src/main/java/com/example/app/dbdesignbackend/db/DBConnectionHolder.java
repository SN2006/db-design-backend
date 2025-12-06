package com.example.app.dbdesignbackend.db;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.sql.Connection;

@Component
@RequestScope
@Getter
@Setter
public class DBConnectionHolder {

    private Connection connection;

    public boolean hasConnection() {
        return this.connection != null;
    }

}
