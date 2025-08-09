package com.birblett.util;

import net.minecraft.util.PlayerInput;

public record InputRecord(boolean forward, boolean backward, boolean left, boolean right, boolean jump, boolean sneak, boolean sprint) {

    public static InputRecord fromLast(PlayerInput playerInput, InputRecord last) {
        return new InputRecord(
                playerInput.forward() && !last.forward(), playerInput.backward() && !last.backward(),
                playerInput.left() && !last.left(), playerInput.right() && !last.right(), playerInput.jump() &&
                !last.jump(), playerInput.sneak() && !last.sneak(), playerInput.sprint() && !last.sprint()
        );
    }

}