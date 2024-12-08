package com.dnd.backend.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GameStatus {
    PLANNED("Planned"),
    IN_PROGRESS("In progress"),
    FINISHED("Finished");

    private final String status;
}
