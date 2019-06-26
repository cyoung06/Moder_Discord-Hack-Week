package kr.kro.syeyoung.moder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.security.auth.login.LoginException;

import kr.kro.syeyoung.moder.event.ModerationEventHandler;
import kr.kro.syeyoung.moder.event.RoleEventHandler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;

public class Main {
	
	private JDA jda;

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
		jda = new JDABuilder(AccountType.BOT).setToken(token).setStatus(OnlineStatus.ONLINE).setGame(Game.of(GameType.LISTENING, "+help")).build();
		jda.awaitReady();
		INSTANCE = this;
		
		jda.addEventListener(new ModerationEventHandler());
		jda.addEventListener(new RoleEventHandler());
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
	
	public JDA getJDA() {
		return jda;
	}
}
