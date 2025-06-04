package org.holoeasy.pool;

import java.util.UUID;

public class KeyAlreadyExistsException extends IllegalStateException {
    public KeyAlreadyExistsException(UUID key) {
        super("Id '" + key + "' already exists");
    }
}