package kr.kro.syeyoung.moder.utils;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.function.Predicate;

import net.dv8tion.jda.core.audit.AuditLogEntry;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

public class AuditLogUtil {
	public static Optional<AuditLogEntry> getLastExecutor(Predicate<? super AuditLogEntry> p, Guild g) {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Optional<AuditLogEntry> ale = g.getAuditLogs().complete().stream().filter(p).findFirst();
		if (!ale.isPresent()) return Optional.ofNullable(null);
		
		AuditLogEntry al = ale.get();
		if (al.getCreationTime().isBefore(Instant.now().minusSeconds(2).atOffset(ZoneOffset.UTC))) {
			return Optional.ofNullable(null);
		}
		
		return ale;
	}
}
