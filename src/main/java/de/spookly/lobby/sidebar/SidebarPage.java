package de.spookly.lobby.sidebar;

import java.util.List;

import net.kyori.adventure.text.Component;

public interface SidebarPage {
	Component getDisplayName();

	List<Component> getLines();
}
