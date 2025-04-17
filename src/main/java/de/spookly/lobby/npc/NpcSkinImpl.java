package de.spookly.lobby.npc;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class NpcSkinImpl implements NpcSkin {
    private String texture;
    private String signature;
    private String name;

    @Override
    public String getName() {
        return this.name;
    }
}
