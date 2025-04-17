package de.spookly.lobby.factory.data;


import java.util.List;

import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import de.spookly.lobby.sidebar.SidebarPage;

@Getter
public enum ScoreboardData implements SidebarPage {
	PAGE_1(List.of(
			Component.empty(),
			title("lobby.scoreboard.points", NamedTextColor.YELLOW),
			icon('\ue101').append(Component.text("%points%")),
			Component.empty(),
			title("lobby.scoreboard.rank", NamedTextColor.GREEN),
			Component.text("%rang%"),
			Component.empty(),
			title("lobby.scoreboard.server", NamedTextColor.AQUA),
			Component.text("%server%"),
			Component.empty()
//            MiniMessage.miniMessage().deserialize("<gradient:#FFAA00:#FFFF55>spookly.de")
	)),
	PAGE_2(List.of(
			Component.empty(),
			title("lobby.scoreboard.discord", NamedTextColor.YELLOW),
			Component.text("spookly.de/discord"),
			Component.empty(),
			title("lobby.scoreboard.website", NamedTextColor.GREEN),
			Component.text("https://spookly.de"),
			Component.empty(),
			title("lobby.scoreboard.threads", NamedTextColor.AQUA),
			Component.text("@spookly.de"),
			Component.empty()
//            MiniMessage.miniMessage().deserialize("<gradient:#FFAA00:#FFFF55>spookly.de")
	));

	private final List<Component> lines;


	ScoreboardData(List<Component> lines) {
		this.lines = lines;
	}

	public Component getDisplayName() {
		return Component.text("\uE002").font(Key.key("scoreboard"));
	}

    @Override
    public List<Component> getLines() {
        return this.lines;
    }

	private static Component title(String text, TextColor color) {
		return Component.translatable(text, color).font(Key.key("small"));
	}

	private static Component icon(char icon) {
		return Component.empty()
				.append(Component.text(icon)
						.font(Key.key("icons"))
						.color(NamedTextColor.WHITE)
						.decoration(TextDecoration.ITALIC, false)
				);
	}

}
