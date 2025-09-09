package com.example.inditex.commons;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for marking unimplemented code during development.
 * Use Todo.TODO() as a placeholder for methods not yet implemented.
 *
 * @deprecated Not intended for production use. Remove or replace before release.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Todo {
    public static <T> T TODO() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
