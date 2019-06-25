package kr.kro.syeyoung.moder;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;

public class Main {
	
	private JDA jda;
	
	private final static String token = "";
	
	public Main() throws LoginException, InterruptedException {
		jda = new JDABuilder(AccountType.BOT).setToken(token).setStatus(OnlineStatus.ONLINE).setGame(Game.of(GameType.LISTENING, "+help")).build();
		jda.awaitReady();
	}
	
	public static void main(String args[]) {
		try {
			new Main();
		} catch (LoginException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
