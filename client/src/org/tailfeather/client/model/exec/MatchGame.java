package org.tailfeather.client.model.exec;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.tailfeather.client.Console;
import org.tailfeather.client.ServerUtils;
import org.tailfeather.client.TailfeatherServerException;
import org.tailfeather.client.matchgame.MatchGameFrame;
import org.tailfeather.client.model.Command;
import org.tailfeather.entity.Checkin;
import org.tailfeather.entity.Location;
import org.tailfeather.entity.User;

@XmlRootElement(name = "matchgame")
@XmlAccessorType(XmlAccessType.FIELD)
public class MatchGame extends Executable {
	private final static Logger LOGGER = Logger.getLogger(MatchGame.class.getName());

	@XmlAttribute(name = "locationId")
	private String locationId;

	@Override
	public boolean enabled(Command command) {
		return command.getAcorn().isInPhaseTwo();
	}

	public static void main(String[] args) throws IOException {
		new MatchGameFrame().run();
	}

	@Override
	public void execute(Command command) {
		try {
			if (new MatchGameFrame().run()) {
				Location location = new Location();
				location.setId(locationId);

				Checkin checkin = new Checkin();
				checkin.setLocation(location);
				checkin.setUser(command.getAcorn().getActiveUser());
				checkin.setTime(new Date());

				ServerUtils.postCheckin(command.getAcorn().getActiveUser().getCheckinUri().toString(), checkin);

				// Print it directly
				Console.printLine();
				Console.printRedLine(">>> A SPECIAL MESSAGE HAS JUST ARRIVED <<<");
				Console.printLine();
				command.getAcorn().printPhaseThreeMessage();
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Game error", e);
		}
	}

	public void reportWin(Command command) {

		User user = command.getAcorn().getActiveUser();

		Location location = new Location();
		location.setId(command.getAcorn().getPhaseThreeTriggerLocationId());

		Checkin checkin = new Checkin();
		checkin.setLocation(location);
		checkin.setUser(user);
		checkin.setTime(new Date());

		try {
			ServerUtils.postCheckin(user.getCheckinUri().toString(), checkin);
		} catch (TailfeatherServerException e) {
			Console.printLine();
			Console.printRedLine("There was an error saving your information to the server.");
			Console.printRedLine("Contact a Tail Feather administrator.");
			Console.printLine();
			return;
		}

		command.getAcorn().printStatus();
	}
}
