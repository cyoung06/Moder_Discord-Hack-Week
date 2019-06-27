package kr.kro.syeyoung.moder;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.security.auth.login.LoginException;

import kr.kro.syeyoung.moder.event.CommandHandler;
import kr.kro.syeyoung.moder.event.ModerationEventHandler;
import kr.kro.syeyoung.moder.event.RoleEventHandler;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;

public class Main {
	
	private ShardManager shards;

	private static Properties configuration;

	static {
		configuration = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("config.properties");
			configuration.load(fis);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private final static String token = configuration.getProperty("token");
	
	private static Main INSTANCE;
	
	public Properties getConfig() {
		return configuration;
	}
	
	public Main() throws LoginException, InterruptedException {
//		jda = new JDABuilder(AccountType.BOT).setToken(token).setStatus(OnlineStatus.ONLINE).setGame(Game.of(GameType.LISTENING, "+help")).build();
//		jda.awaitReady();
		INSTANCE = this;
		
		DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
	    builder.setToken(token);
	    builder.setGame(Game.of(GameType.LISTENING, "+help"));
	    builder.setStatus(OnlineStatus.ONLINE);
	    builder.setShardsTotal(2);
	    shards = builder.build();
		
		shards.addEventListener(new ModerationEventHandler());
		shards.addEventListener(new RoleEventHandler());
		shards.addEventListener(new CommandHandler());
	}
	
	public static void main(String args[]) {
		try {
			new Main();
		} catch (LoginException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static Main getInstance() {
		return INSTANCE;
	}
	
	public ShardManager getJDA() {
		return shards;
	}
}
