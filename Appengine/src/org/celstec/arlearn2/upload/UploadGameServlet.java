package org.celstec.arlearn2.upload;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HeaderParam;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.celstec.arlearn2.beans.GamePackage;
import org.celstec.arlearn2.beans.RunPackage;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.game.ScoreDefinition;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.Team;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.delegators.GameDelegator;
import org.celstec.arlearn2.delegators.RunDelegator;
import org.celstec.arlearn2.delegators.ScoreDefinitionDelegator;
import org.celstec.arlearn2.delegators.TeamsDelegator;
import org.celstec.arlearn2.delegators.UsersDelegator;
import org.celstec.arlearn2.delegators.generalitems.CreateGeneralItems;
import org.codehaus.jettison.json.JSONObject;

import com.google.gdata.util.AuthenticationException;

public class UploadGameServlet extends HttpServlet {

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		long gameId = 0l;
		String auth = null;
		try {
			ServletFileUpload upload = new ServletFileUpload();
			FileItemIterator iter = upload.getItemIterator(req);

			String json = "";
			while (iter.hasNext()) {
				FileItemStream item = iter.next();
				String name = item.getFieldName();
				InputStream stream = item.openStream();
				if (item.isFormField()) {
					String value = Streams.asString(stream);
					System.out.println("Form field " + name + " with value "+ value + " detected.");
					if ("gameId".equals(name)) gameId = Long.parseLong(value);
					if ("auth".equals(name)) auth = value;
					
				} else {
					// Image here.
					System.out.println("File field " + name
							+ " with file name " + item.getName()
							+ " detected.");
					json = Streams.asString(stream);

				}
			}

			res.setContentType("text/plain");
			// String json = slurp(req.getInputStream());
			// json = json.substring(json.indexOf("{"));
			System.out.println(json);
			JSONObject jObject = new JSONObject(json);
			System.out.println(jObject);
			JsonBeanDeserializer jbd = new JsonBeanDeserializer(json);
			GamePackage arlPackage = (GamePackage) jbd
					.deserialize(GamePackage.class);
			if (arlPackage.getGame() != null)
				unpackGame(arlPackage, req, auth);
			RunPackage runPackage = (RunPackage) jbd
					.deserialize(RunPackage.class);
			if (runPackage.getRun() != null)
				unpackRun(runPackage, req, gameId, auth);

		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}

	private void unpackRun(RunPackage runPackage, HttpServletRequest req,
			long gameId, String auth) throws AuthenticationException {
		System.out.println(runPackage.getRun());
		System.out.println(runPackage.getRun().getTitle());
		Run run = runPackage.getRun();
		run.setGameId(gameId);
		if (run != null) {
			RunDelegator rd = new RunDelegator(auth == null?req.getHeader("Authorization"):auth);
			run = rd.createRun(run);
			if (run.getRunId() != null) {
				TeamsDelegator td = new TeamsDelegator(rd);
				for (Iterator iterator = runPackage.getTeams().iterator(); iterator
						.hasNext();) {
					Team t = (Team) iterator.next();
					t.setRunId(run.getRunId());
					Team tDb = td.createTeam(t);
					if (tDb.getTeamId() != null) {
						UsersDelegator ud = new UsersDelegator(rd);
						for (Iterator iterator2 = t.getUsers().iterator(); iterator2
								.hasNext();) {
							User u = (User) iterator2.next();
							u.setTeamId(tDb.getTeamId());
							u.setRunId(run.getRunId());
							ud.createUser(u);

						}
					}
				}

			}
		}

	}

	private void unpackGame(GamePackage arlPackage, HttpServletRequest req, String auth) {
		auth = auth == null?req.getHeader("Authorization"):auth;
		GameUnpacker gu = new GameUnpacker(arlPackage, auth);
		gu.unpack();
	}
	private void unpackGame_old(GamePackage arlPackage, HttpServletRequest req, String auth)
			throws AuthenticationException {
		Game game = arlPackage.getGame();
		if (game != null) {
			GameDelegator gd = new GameDelegator(auth == null?req.getHeader("Authorization"):auth);
			game.setGameId(null);
			game = gd.createGame(game);
			Long gameId = game.getGameId();
			Iterator<GeneralItem> it = arlPackage.getGeneralItems().iterator();
			CreateGeneralItems cr = new CreateGeneralItems(
					auth == null?req.getHeader("Authorization"):auth);
			while (it.hasNext()) {
				GeneralItem generalItem = (GeneralItem) it.next();
				generalItem.setGameId(gameId);

				cr.createGeneralItem(generalItem);
			}
			if (arlPackage.getScoreDefinitions() != null) {
				Iterator<ScoreDefinition> scoreIterator = arlPackage
						.getScoreDefinitions().getScoreDefinitions().iterator();
				ScoreDefinitionDelegator sdd = new ScoreDefinitionDelegator(cr);
				while (scoreIterator.hasNext()) {
					ScoreDefinition scoreDefinition = (ScoreDefinition) scoreIterator
							.next();
					scoreDefinition.setGameId(gameId);
					sdd.createScoreDefinition(scoreDefinition);

				}
			}
		}

	}

	public static String slurp(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}
}
