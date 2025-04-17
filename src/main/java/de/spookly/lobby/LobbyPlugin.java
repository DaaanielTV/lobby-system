package de.spookly.lobby;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.kyori.adventure.util.UTF8ResourceBundleControl;
import org.ipvp.canvas.MenuFunctionListener;

import de.spookly.lobby.commands.BuildCommand;
import de.spookly.lobby.commands.FlyCommand;
import de.spookly.lobby.commands.SetspawnCommand;
import de.spookly.lobby.listener.*;
import de.spookly.lobby.manager.ScoreboardManager;
import de.spookly.lobby.sidebar.SidebarCache;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;

@Getter
public class LobbyPlugin extends JavaPlugin {

    @Getter
    private static LobbyPlugin instance;

    private FileConfiguration generalConfig;
    private FileConfiguration npcConfig;
    private FileConfiguration locationConfig;

    private File generalConfigFile;
    private File npcConfigFile;
    private File locationConfigFile;

    private SidebarCache sidebarCache;
    private ScoreboardManager scoreboardManager;

    private InteractListener interactListener;
    private PlayerJoinListener playerJoinListener;
    private PlayerMoveListener playerMoveListener;
    private PlayerQuitListener playerQuitListener;
    private AsyncChatListener asyncChatListener;
    private de.spookly.lobby.listener.Listener listener;

    private SetspawnCommand setspawnCommand;
    private BuildCommand buildCommand;
    private FlyCommand flyCommand;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Create config files
        createConfigs();
        getLogger().info("Loaded configuration files");

        this.sidebarCache = new SidebarCache();
        this.scoreboardManager = new ScoreboardManager(this);
        getLogger().info("Loaded managers");

        this.interactListener = new InteractListener(this);
        this.playerJoinListener = new PlayerJoinListener(this);
        this.playerMoveListener = new PlayerMoveListener(this);
        this.playerQuitListener = new PlayerQuitListener(this);
        this.asyncChatListener = new AsyncChatListener();
        this.listener = new de.spookly.lobby.listener.Listener(this);
        this.setspawnCommand = new SetspawnCommand(this);
        this.buildCommand = new BuildCommand(this);
        this.flyCommand = new FlyCommand();
        getLogger().info("Loaded listener / commands");

        registerEvents();
        registerCommands();

        Bukkit.getScheduler().runTaskLater(this, this::postStartup, 1L);
    }

    private void createConfigs() {
        saveDefaultConfig();
        
        generalConfigFile = new File(getDataFolder(), "general_settings.yml");
        npcConfigFile = new File(getDataFolder(), "npc_settings.yml");
        locationConfigFile = new File(getDataFolder(), "location_settings.yml");

        if (!generalConfigFile.exists()) saveResource("general_settings.yml", false);
        if (!npcConfigFile.exists()) saveResource("npc_settings.yml", false);
        if (!locationConfigFile.exists()) saveResource("location_settings.yml", false);

        generalConfig = YamlConfiguration.loadConfiguration(generalConfigFile);
        npcConfig = YamlConfiguration.loadConfiguration(npcConfigFile);
        locationConfig = YamlConfiguration.loadConfiguration(locationConfigFile);
    }

    private void registerEvents() {
        registerEvent(listener);
        registerEvent(interactListener);
        registerEvent(playerJoinListener);
        registerEvent(playerQuitListener);
        registerEvent(playerMoveListener);
        registerEvent(asyncChatListener);
        registerEvent(new MenuFunctionListener());
    }

    private void registerEvent(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    private void registerCommands() {
        getCommand("build").setExecutor(buildCommand);
        getCommand("fly").setExecutor(flyCommand);
        getCommand("setspawn").setExecutor(setspawnCommand);
    }

    @Override
    public void onDisable() {
        try {
            generalConfig.save(generalConfigFile);
            npcConfig.save(npcConfigFile);
            locationConfig.save(locationConfigFile);
        } catch (Exception e) {
            getLogger().severe("Could not save config files!");
            e.printStackTrace();
        }
    }

    protected void postStartup() {
        registerTranslatables();
    }

    private void registerTranslatables() {
        TranslationRegistry registry = TranslationRegistry.create(Key.key("spookly_lobby:value"));
        ResourceBundle bundleUS = ResourceBundle.getBundle("SpooklyLobby.Translations", Locale.US, UTF8ResourceBundleControl.get());
        ResourceBundle bundleDE = ResourceBundle.getBundle("SpooklyLobby.Translations", Locale.GERMANY, UTF8ResourceBundleControl.get());

        registry.registerAll(Locale.US, bundleUS, true);
        registry.registerAll(Locale.GERMAN, bundleDE, true);

        GlobalTranslator.translator().addSource(registry);
    }
}
