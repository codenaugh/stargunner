package games.caravan.networking;

import games.caravan.CaravanNetworkingGame;
import games.caravan.character.GhostAvatar;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import java.util.Vector;

import sage.networking.client.GameConnectionClient;

public class GameClientTCP extends GameConnectionClient 
{ 
	private CaravanNetworkingGame game; 
	private UUID id; 
	private GhostAvatar ghost;
 
	public GameClientTCP(InetAddress remAddr, int remPort, ProtocolType pType, CaravanNetworkingGame game) throws IOException 
	{ 
		super(remAddr, remPort, pType); 
		this.game = game; 
		this.id = UUID.randomUUID(); 
	} 
	
	protected void processPacket(Object msg) // override 
	{ 
		// extract incoming message into substrings. Then process: 
		String message = (String) msg; 
		String[] msgTokens = message.split(","); 
	 
		if(msgTokens.length > 0) 
		{ 		
			if(msgTokens[0].compareTo("join") == 0) 
			{
				// receive �join� 
				// format: join, success or join, failure 			
				if(msgTokens[1].compareTo("success") == 0) 
				{ 
					game.setConnected(true); 
					sendCreateMessage(game.getPlayerPosition()); 
				} 
				else if(msgTokens[1].compareTo("failure") == 0) 
					game.setConnected(false); 
			} 
			if(msgTokens[0].compareTo("bye") == 0) // receive �bye� 
			{ 
				// format: bye, remoteId 
				UUID ghostID = UUID.fromString(msgTokens[1]); 
				removeGhostAvatar(ghostID); 
			} 
			if(msgTokens[0].compareTo("create") == 0) // receive �create�� 
			{  
				// format: create, remoteId, x,y,z or dsfr, remoteId, x,y,z 
				UUID ghostID = UUID.fromString(msgTokens[1]); 
				// extract ghost x,y,z, position from message, then: 
				Point3D ghostPosition = new Point3D(Double.parseDouble(msgTokens[2]), Double.parseDouble(msgTokens[3]), Double.parseDouble(msgTokens[4])); 
				if (ghost==null){
					createGhostAvatar(ghostID, ghostPosition);
				}
			} 
			if(msgTokens[0].compareTo("move") == 0) // receive �move� 
			{ 
				UUID ghostID = UUID.fromString(msgTokens[1]); 
				// extract ghost x,y,z, position from message, then: 
				Point3D ghostPosition = new Point3D(Double.parseDouble(msgTokens[2]), Double.parseDouble(msgTokens[3]), Double.parseDouble(msgTokens[4]));
				moveGhostAvatar(ghostID, ghostPosition);
			} 
			if (msgTokens[0].compareTo("dsfr") == 0) // receive �details for� 
			{
				boolean exists = false;
				// format: dsfr, remoteId, x,y,z 
				UUID ghostID = UUID.fromString(msgTokens[1]);
				Point3D location = new Point3D(Double.parseDouble(msgTokens[2]), Double.parseDouble(msgTokens[3]), Double.parseDouble(msgTokens[4]));
				
				if (ghost==null){
					createGhostAvatar(ghostID, location);
				}
				
			} 
			if(msgTokens[0].compareTo("wsds") == 0) // receive �wants details� 
			{ 
				Point3D pos = game.getPlayerPosition();
				UUID remID = UUID.fromString(msgTokens[1]);
				sendDetailsForMessage(remID, pos);
			} 
		}
	} 

	private void createGhostAvatar(UUID ghostID, Point3D ghostPosition) {
		ghost = new GhostAvatar(ghostID, ghostPosition);
		game.addGameWorldObject(ghost);
	}

	private void removeGhostAvatar(UUID ghostID) {
		game.removeGameWorldObject(ghost);
		ghost = null;		
	}
	
	private void moveGhostAvatar(UUID ghostID, Point3D ghostPosition) {	
		if (ghost!=null) 
			ghost.move(ghostPosition);
	}

	public void sendCreateMessage(Point3D pos) 
	{	
		// format: (create, localId, x,y,z) 
		try 
		{ 
			String message = new String("create," + id.toString()); 
			message += "," + pos.getX()+"," + pos.getY() + "," + pos.getZ(); 
			sendPacket(message); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
	}
	 
	public void sendJoinMessage() 
	{
		// format: join, localId 
		try 
		{ 
			sendPacket(new String("join," + id.toString()));
		}
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
	}
	 
	
	public void sendByeMessage() 
	{  
		try 
		{ 
			String message = new String("bye," + id.toString()); 
			sendPacket(message); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
	} 
	
	public void sendDetailsForMessage(UUID remId, Point3D pos) 
	{
		try 
		{ 
			String message = new String("dsfr," + id.toString() + "," + remId.toString());
			message += "," + pos.getX(); 
			message += "," + pos.getY(); 
			message += "," + pos.getZ(); 
			sendPacket(message); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
	}
	
	public void sendMoveMessage(Point3D pos) 
	{
		try 
		{ 
			String message = new String("move," + id.toString()); 
			message += "," + pos.getX(); 
			message += "," + pos.getY(); 
			message += "," + pos.getZ(); 
			sendPacket(message); 
		} 
		catch (IOException e) 
		{ 
			e.printStackTrace();
		}
	}
}
