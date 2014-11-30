package sd3;

public final class Serializer {
	public static String jsonify(Game g){
		StringBuilder str = new StringBuilder(30);
		
		String state;
		if(g.running){
			state = "play";
		}else{
			state = "over";
		}
		str.append("{\"type\":\"state\",\"time\":100,\"state\":\""+state+"\",\"grid\":[");
		
		str.append("{\"type\":\"player\",\"X\":"+g.GetPlayer().GetX()+",\"Y\":"+g.GetPlayer().GetY()+",\"id\":"+System.identityHashCode(g.GetPlayer())+"}");
		for(Ship s: g.GetEnemies())
		{
			str.append(",{\"type\":\""+s.GetType()+"\",\"X\":"+s.GetX()+",\"Y\":"+s.GetY()+",\"id\":"+System.identityHashCode(s)+"}");
		}
		str.append("]}");
		return str.toString();
	}

}
